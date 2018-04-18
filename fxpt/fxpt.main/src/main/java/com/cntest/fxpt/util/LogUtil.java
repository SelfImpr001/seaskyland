/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.fxpt.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cntest.security.UserDetails;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 陈勇2015年10月24日
 * @version 1.0
 **/
public class LogUtil {
	private static JdbcTemplate jdbcTemplate;

	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		LogUtil.jdbcTemplate = jdbcTemplate;
	}

	public static void logMain(Long examid, String studentId) {
		// 判断该考试是否存在获取过的学生
		int count = jdbcTemplate.queryForObject(
				"SELECT COUNT(1) FROM dw_agg_exam_log_bound WHERE examId = ? AND studentId = ? ",
				new Object[] { examid, studentId }, Integer.class);
		if (count == 0) {
			jdbcTemplate.update("INSERT INTO dw_agg_exam_log_bound (examId,studentId,titsTime) VALUES(?,?,NOW())",
					examid, studentId);
		} else {
			// 访问量
			// jdbcTemplate.update("UPDATE cntest_fxpt_cy.dw_agg_exam_log_bound
			// SET tits=tits+1 WHERE examId = ? AND studentId = ?
			// ",exam.getId(),studentId);
		}
	}

	/**
	 * @author 陈勇
	 * @param handleOptionLocation
	 *            操作地址(点击地址)
	 * @param handleOption
	 *            操作项
	 * @param suferHandleOption
	 *            被操作对象
	 * @param status
	 *            状态
	 * @param logInfo
	 *            操作详情
	 */
	public static void log(String handleOptionLocation, String handleOption, String suferHandleOption, String status,
			String logInfo, String erreInfo) {
		logMain(handleOptionLocation, handleOption, suferHandleOption, status, logInfo, erreInfo, null);
	}

	public static void logSetIp(String handleOptionLocation, String handleOption, String suferHandleOption,
			String status, String logInfo, String erreInfo, String ip) {
		logMain(handleOptionLocation, handleOption, suferHandleOption, status, logInfo, erreInfo, ip);
	}

	public static void logMain(String handleOptionLocation, String handleOption, String suferHandleOption,
			String status, String logInfo, String erreInfo, String ip) {
		Subject subject = SecurityUtils.getSubject();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		String username = "";
		if (subject.isAuthenticated()) {
			if (subject.getPrincipal() instanceof UserDetails) {
				UserDetails us = (UserDetails) subject.getPrincipal();
				username = us.getUserName();
			} else if (subject.getPrincipal() instanceof String) {
				username = (String) subject.getPrincipal();
			}

		} else {
			// 弹出异常信息提示登陆超时，请重新登陆
			response.setHeader("sessionstatus", "timeout");
			// throw new UnknownAccountException();// 没找到帐号
			return;
		}

		String setIp = "";
		if (ip == null) {
			setIp = getIP(request);
		}
		if (username == null || "null".equals(username))
			username = "无登录操作";

		StringBuffer sql = new StringBuffer("INSERT INTO 4a_log ");
		sql.append("(");
		sql.append("handleTime, ");
		sql.append("handlePro, ");
		sql.append("handleOption, ");
		sql.append("handleOptionLocation, ");
		sql.append("suferHandleOption, ");
		sql.append("STATUS, ");
		sql.append("logInfo, ");
		sql.append("handleIP, ");
		sql.append("erreInfo ");
		sql.append(")");
		sql.append("VALUES");
		sql.append("( ?,?,?,?,?,?,?,?,? )");
		jdbcTemplate.update(sql.toString(), getDate(), username, handleOption, handleOptionLocation, suferHandleOption,
				status, logInfo, ip, erreInfo);
	}

	public static String getIP(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_Client_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getDate() {
		SimpleDateFormat datefor = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		return datefor.format(new Date());
	}

	public static String datefor(Object d) {
		if ((d instanceof java.util.Date || d instanceof java.sql.Date) && d != null) {
			SimpleDateFormat datefor = new SimpleDateFormat("YYYY-MM-dd");
			return datefor.format(d);
		} else {
			return "";
		}
	}

	private static String getString(String s) {
		if (StringUtils.isEmpty(s)) {
			return "";
		}
		return s;
	}

	public static String e(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		return sw.toString();
	}

	public static void main(String[] args) {
		System.out.println(new Exception("sss"));
	}
}
