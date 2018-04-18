/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain;




/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 陈勇2016年9月7日
 * @version 1.0
 **/
public class Log {
	
	private Long id;//  记录日记Id 
	private String handleTime;//  操作时间   
	private String handlePro;//  操作人id    
	private String handleOption;//  操作项      
	private String suferHandleOption  ;//  被操作对象
	private String status ;//  状态    
	private String logInfo;//详情信息
	private String handleOptionLocation;//操作地址
	private String handleIP;// 操作人电脑IP
	private String erreInfo;//报错信息
	
	
	public String getErreInfo() {
		return erreInfo;
	}
	public void setErreInfo(String erreInfo) {
		this.erreInfo = erreInfo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	public String getHandlePro() {
		return handlePro;
	}
	public void setHandlePro(String handlePro) {
		this.handlePro = handlePro;
	}
	public String getHandleOption() {
		return handleOption;
	}
	public void setHandleOption(String handleOption) {
		this.handleOption = handleOption;
	}
	public String getSuferHandleOption() {
		return suferHandleOption;
	}
	public void setSuferHandleOption(String suferHandleOption) {
		this.suferHandleOption = suferHandleOption;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogInfo() {
		return logInfo;
	}
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	public String getHandleOptionLocation() {
		return handleOptionLocation;
	}
	public void setHandleOptionLocation(String handleOptionLocation) {
		this.handleOptionLocation = handleOptionLocation;
	}
	public String getHandleIP() {
		return handleIP;
	}
	public void setHandleIP(String handleIP) {
		this.handleIP = handleIP;
	}
}

