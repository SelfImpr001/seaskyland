package com.cntest.birt.repository;

import java.util.List;

import com.cntest.birt.domain.ReportScript;
import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;

	public interface ReportScriptRepository extends Repository<ReportScript, Long> {

	void findReport(Query<ReportScript> query, List ids);

	List<ReportScript> findByIn(String directory);

	void cleatSession();

	List<ReportScript> getPrens(Long org_id);

	void getPrens(Long org_id, Query<ReportScript> query);


	void findReportmerge(Query<ReportScript> query);

	
}
