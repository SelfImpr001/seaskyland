/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain.event;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cntest.common.disruptor.PersistanceEvent;
import com.cntest.common.event.RepositoryEvent;
import com.cntest.common.page.Page;
import com.cntest.common.specification.Specification;
import com.cntest.fxpt.domain.ExamCheckin;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.ExamineeCheckin;
import com.cntest.fxpt.domain.spec.examinee.StudentSpecificationBuilder;
import com.cntest.fxpt.repository.IExamStudentDao;
import com.cntest.util.SpringContext;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月6日
 * @version 1.0
 **/
public class ExamCheckinEvent extends RepositoryEvent implements PersistanceEvent {
	private static final Logger logger = LoggerFactory.getLogger(ExamCheckinEvent.class);
	
	private ExamCheckin examCheckin;
	
	private IExamStudentDao examStudentDao;
	
	private Specification spec;
	
	private static String sql1 = "insert  into kn_examinee_checkin   (status, checkinDate, exam_student_fact_id, exam_checkin_id) values (?, ?, ?, ?)";
	
	private static String sql2 =" update  kn_exam_checkin  set checkedTotal=?, failureTotal=? where exam_checkin_id=?";
	
	private static String sql3 = "update  kn_exam_checkin  set endDate=?, status=? where exam_checkin_id=?";
	
	private static String sql4 = "INSERT INTO kn_student_exam (examId, studentGuid, exam_student_fact_id)VALUES(?,?,?)";
	
	private static String sql5 = "SELECT a.guid FROM kn_studentbase a  where a.xh=? and a.name=? and a.schoolCode=? and a.grade=?";
	
	private JdbcTemplate jdbcTemplate;
	
	//private PlatformTransactionManager tx;
	
	public ExamCheckinEvent(ExamCheckin examCheckin){
		this.examCheckin = examCheckin;
		this.jdbcTemplate = SpringContext.getBean("jdbcTemplate");
		//this.tx = SpringContext.getBean("jdbcTransactionManager");
	}

	public void checkin() {
		
		this.examStudentDao = SpringContext.getBean("IExamStudentDao");
		this.spec = StudentSpecificationBuilder.build(examCheckin.mySpecs());

		this.doCheckin(null);
		
		this.examCheckin.endCheckin();
		logger.debug("execute sql ",sql3);
		//this.tx.
		jdbcTemplate.batchUpdate(sql3, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {		
				ps.setTimestamp(1, new java.sql.Timestamp(examCheckin.getEnd().getTime()));		
				ps.setInt(2, examCheckin.getStatus());				
				ps.setLong(3, examCheckin.getPk());
			}

			@Override
			public int getBatchSize() {
				return 1;
		}});		
	}
	
	private void doCheckin(Page<ExamStudent> pager) {
		if(pager == null) {
			pager = new Page<ExamStudent>();
			pager.setPagesize(1000);
			pager.setCurpage(1);
		    pager.addParameter("q", "false");			
		}

		this.examStudentDao.list(pager, examCheckin.getExam().getId());
		final List<ExamStudent> students =  pager.getList();
		final ArrayList<Object[]> studentExamArgs = new ArrayList<Object[]>();
		logger.debug("execute sql {} {} times ",sql1,students.size());
		jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				ExamStudent student = students.get(index);
				ExamineeCheckin checkin = new ExamineeCheckin(examCheckin,student,spec);
				boolean b = checkin.start();
				ps.setInt(1, checkin.getStatus());				
				ps.setTimestamp(2, new java.sql.Timestamp(checkin.getCheckinDate().getTime()));				
				ps.setLong(3, checkin.getExamineeId());
				ps.setLong(4, examCheckin.getPk());
				if(b) {
					Object[] args = new Object[] {student.getStudentId(),student.getName(),student.getSchool().getCode(),examCheckin.getExam().getExamStudentJiebieName()};
					String guid = jdbcTemplate.queryForObject(sql5,args, String.class);
					studentExamArgs.add(new Object[] {examCheckin.getExam().getId(),guid,checkin.getExamineeId()});
				}				   
			}
			
			@Override
			public int getBatchSize() {
				return students.size();
		}});
		
		logger.debug("execute sql ",sql4);
		jdbcTemplate.batchUpdate(sql4, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object[] args = studentExamArgs.get(i);
				ps.setLong(1, (Long)args[0]);				
				ps.setString(2, args[1] + "");				
				ps.setLong(3, (Long)args[2]);
			}

			@Override
			public int getBatchSize() {
				return studentExamArgs.size();
		}});		

		logger.debug("execute sql ",sql2);
		jdbcTemplate.batchUpdate(sql2, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, examCheckin.getCheckedTotal());				
				ps.setInt(2, examCheckin.getFailureTotal());				
				ps.setLong(3, examCheckin.getPk());
			}

			@Override
			public int getBatchSize() {
				return 1;
		}});

		while(pager.hasNext()) {
			pager.setList(null);
			this.doCheckin(pager);
		}
	}
}

