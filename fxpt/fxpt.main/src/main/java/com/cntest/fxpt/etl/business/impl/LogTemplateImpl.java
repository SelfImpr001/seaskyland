package com.cntest.fxpt.etl.business.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.Log;
import com.cntest.fxpt.etl.business.IImportTemplateService;
import com.cntest.fxpt.repository.LogDao;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Service("Log.TemplateService")
public class LogTemplateImpl extends AbstractTemplateImpl implements IImportTemplateService {
	@Autowired(required = false)
	private LogDao logdao;

	@Override
	public void template(OutputStream out) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void template(OutputStream out, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("日志信息", 0);

		String optionName = request.getParameter("optionName");
		String optionValue = request.getParameter("optionValue");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String status = request.getParameter("status");
		Map<String, String> params = new HashMap<String, String>();
		params.put("optionValue", optionValue);
		params.put("status", status);
		if ("dateTime".equals(optionValue)) {
			if (startDate == null || "".equals(startDate))
				startDate = " ";
			if (endDate == null || "".equals(endDate))
				endDate = " ";
			params.put("optionName", startDate + "," + endDate);
		} else {
			params.put("optionName", optionName);
		}

		List<Log> loglist = logdao.downLogQueryByParams(params);

		List<String> headers = new ArrayList<>();
		headers.add("序号");
		headers.add("操作时间");
		headers.add("操作人");
		headers.add("所在IP");
		headers.add("操作项");
		headers.add("被操作对象");
		headers.add("状态");

		int idx = 0;
		for (String header : headers) {
			sheet.setColumnView(idx, 15);// 设置列宽
			Label label = new Label(idx, 0, header, getTextFormat());
			sheet.addCell(label);
			idx++;
		}

		int rowIndex = 1;
		for (Log log : loglist) {
			int colIndex = 0;
			Label label = new Label(0, rowIndex, rowIndex + "", getTextFormat());
			Label labe2 = new Label(1, rowIndex, log.getHandleTime(), getTextFormat());
			Label labe3 = new Label(2, rowIndex, log.getHandlePro(), getTextFormat());
			Label labe4 = new Label(3, rowIndex, log.getHandleIP(), getTextFormat());
			Label labe5 = new Label(4, rowIndex, log.getHandleOption(), getTextFormat());
			Label labe6 = new Label(5, rowIndex, log.getSuferHandleOption(), getTextFormat());
			Label labe7 = new Label(6, rowIndex, log.getStatus(), getTextFormat());
			sheet.addCell(label);
			sheet.addCell(labe2);
			sheet.addCell(labe3);
			sheet.addCell(labe4);
			sheet.addCell(labe5);
			sheet.addCell(labe6);
			sheet.addCell(labe7);
			colIndex++;
			rowIndex++;
		}
		wb.write();
		wb.close();
	}

}
