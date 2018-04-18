/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.web.view;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 分页器
 * </pre>
 * 
 * @author 李贵庆 2013-9-6
 * @version 1.0
 **/
public class Paging {

	private int currentPage = 1;

	private int totalRecord = 0;

	private int pageSize = 20;

	public Paging() {

	}
	
	public Paging(int currentPage, int totalRecord) {
		this.currentPage = currentPage;
		this.totalRecord = totalRecord;
	}
	
	public Paging(int currentPage, int totalRecords, int pageSize) {
		super();
		this.currentPage = currentPage;
		this.totalRecord = totalRecords;
		this.pageSize = pageSize;
	}
	
	public int[] getPageNum() {
		int range = 10;
		int startNum = 1;
        int endNum = startNum + range -1;
        int totalPage = getTotalPage();
        if(endNum < totalPage && this.currentPage > (startNum+endNum+1)/2 ){
            startNum = this.currentPage - (int) Math.ceil(((double) range)/2);
            endNum = startNum + range - 1;

            if(endNum>totalPage){
                endNum = totalPage;
                startNum = totalPage - range + 1;
            }
        }
        if(endNum > totalPage){
            endNum = totalPage;
        }

        return new int[]{startNum, endNum};
	}
	
	public int getFirst() {
		return (currentPage-1) * pageSize ;
	}
	
	public int getLast() {
		return (currentPage) * pageSize ;
	}
	
	public int getTotalPage() {
		return (int)Math.ceil((double)totalRecord/pageSize);
	}

	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getPrePage() {
		int prePage = this.currentPage - 1;
		return prePage <= 0? 1 : prePage;
	}
	
	public int getNextPage() {
		int nextPage = this.currentPage + 1;                                                      
        return nextPage > this.getTotalPage() ? this.getTotalPage() : nextPage;  
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String toString() {
		return new ToStringBuilder(this).append("Records", this.totalRecord)
				.append("pageSize", this.pageSize)
				.append("currenPage", this.currentPage).build();
	}
}
