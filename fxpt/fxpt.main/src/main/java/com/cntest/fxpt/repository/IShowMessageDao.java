package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.domain.ShowMessage;

public interface IShowMessageDao {
	/**
	 *  删除显示字段信息
	 * @param examid  考试id
	 * @param showType  删除类型：1学生信息，2明细表，3成绩
	 */
	public void deleteMessageByExamid(Long examid,int showType,Long testPaperId);
	/**
	 * 添加字段显示信息
	 * @param examid
	 * @param  testPaperId 试卷Id
	 * @param showType  删除类型：1学生信息，2明细表，3成绩
	 * @param dataField  导入字段详情
	 */
	public void addMessageByExamid(Long examid,Long testPaperId,int showType,List<DataField> dataField);
	
	/**
	 * 查询对应的显示字段信息
	 * @param examid
	 * @param showType  删除类型：1学生信息，2明细表，3成绩
	 * @return
	 */
	public List<ShowMessage> findShowMessageByExamid(Long examid,Long testPaperId,int showType);
}
