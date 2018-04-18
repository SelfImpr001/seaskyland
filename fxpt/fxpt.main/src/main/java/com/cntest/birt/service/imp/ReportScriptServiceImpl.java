package com.cntest.birt.service.imp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.birt.repository.ReportScriptRepository;
import com.cntest.birt.service.ReportScriptService;
import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.fxpt.util.UploadHelper;

@Transactional
@Service
public class ReportScriptServiceImpl extends AbstractEntityService<ReportScript, Long>implements ReportScriptService {

	private static Logger logger = LoggerFactory.getLogger(ReportScriptServiceImpl.class);

	@Autowired
	private ReportScriptRepository ReportScriptRepository;

	@Autowired
	public ReportScriptServiceImpl(ReportScriptRepository ReportScriptRepository) {
		this.setRepository(ReportScriptRepository);
		this.ReportScriptRepository = ReportScriptRepository;
	}

	@Override
	public void query(Query<ReportScript> query, List<OrgBirt> orgList) {
		List ids = new ArrayList<>();
		for (OrgBirt orgBirt : orgList) {
			ids.add(orgBirt.getPk());
		}
		
		// TODO Auto-generated method stub
		ReportScriptRepository.findReport(query,ids);
		// super.query(query);
	}

	public void save(ReportScript reportScript) {
		ReportScriptRepository.save(reportScript);
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(readOnly = true)
	public ReportScript load(Long pk) throws BusinessException {
		return super.load(pk);
	}

	@Override
	@Transactional
	public void update(ReportScript reportScript) {
		long begin = System.currentTimeMillis();
		String name = reportScript.getName();
		ReportScript re = ReportScriptRepository.load(reportScript.getPk());
		if (!re.getDirectory().equals(reportScript.getDirectory())) {
			File file = new File(re.getDirectory());
			file.delete();
			if (re.getWordDocment() != null) {
				file = new File(re.getWordDocment());
				file.delete();
				reportScript.setWordDocment(null);
				reportScript.setWordName(null);
			}
		}
		ReportScriptRepository.cleatSession();
		ReportScriptRepository.update(reportScript);

	}

	@Override
	@Transactional
	public ReportScript mutiRoportWord(ReportScript Rep) throws BirtException, BusinessException {

		IReportEngine engine = null;
		EngineConfig config = null;
		ReportScript ReportScript = ReportScriptRepository.load(Rep.getPk());
		String path = "";

		String savePath = null;
		config = new EngineConfig();
		config.setLogConfig(null, Level.FINEST);
		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		engine = factory.createReportEngine(config);

		IReportRunnable reportDesign = null;
		// 脚本1
		reportDesign = engine.openReportDesign(ReportScript.getDirectory());
		ReportDesignHandle reportDesignHandle = (ReportDesignHandle) reportDesign.getDesignHandle();
		reportDesignHandle.getParameters();
		reportDesignHandle.getDataSources();
		reportDesignHandle.getDataSets();
		reportDesignHandle.getCubes();

		IRunAndRenderTask task = engine.createRunAndRenderTask(reportDesign);
		IGetParameterDefinitionTask parameterDefinitionTask = engine.createGetParameterDefinitionTask(reportDesign);
		HashMap<String, String> params = parameterDefinitionTask.getDefaultValues();
		params.put("aIndicator", "");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddhhmmss");
		String name = dateFormat.format(new Date()) + ".docx";
		path = UploadHelper.getReport() + "download/" + name;
		DocxRenderOption options = new DocxRenderOption();
		options.setOutputFormat("docx");
		options.setOutputFileName(path);
		ReportScript.setWordDocment(path);
		ReportScript.setWordName(Rep.getSource() + ".docx");
		task.setRenderOption(options);
		task.setParameterValues(params);

		task.run();
		task.close();
		Platform.shutdown();

		return ReportScript;
	}

	@Override
	public void mutiRoport(ReportScript reportScript) throws BirtException, IOException {
		List<ReportScript> ReportScripts = ReportScriptRepository.findByIn(reportScript.getDirectory());
		String activeIndicator = null;
		int i = 0;
		List<String> list = new ArrayList();
		String suffix = null;
		String msg = "";
		// //获取脚本地址并组装脚本所在path
		for (ReportScript report : ReportScripts) {
			if (i != 0) {
				list.add(report.getDirectory());
			} else {
				activeIndicator = report.getDirectory();
				suffix = report.getSuffix();
			}

		}
		IReportEngine engine = null;
		EngineConfig config = null;
		String savePath = null;
		config = new EngineConfig();
		config.setLogConfig(null, Level.FINEST);
		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		engine = factory.createReportEngine(config);

		IReportRunnable reportDesign = null;
		reportDesign = engine.openReportDesign(activeIndicator);
		ReportDesignHandle reportDesignHandle = (ReportDesignHandle) reportDesign.getDesignHandle();
		reportDesignHandle.getParameters();
		reportDesignHandle.getDataSources();
		reportDesignHandle.getDataSets();
		reportDesignHandle.getCubes();

		org.eclipse.birt.report.model.core.Module reportDesignModule = reportDesign.getDesignHandle().getModule();

		for (String path : list) {
			// 脚本名称
			IReportRunnable reportDesign002 = engine.openReportDesign(path);
			Module reportDesignModule002 = reportDesign002.getDesignHandle().getModule();
			int slotId = reportDesignHandle.getBody().getSlotID();
			ContainerSlot containerSlot = reportDesignModule002.getSlot(slotId);
			for (int j = 0; j < containerSlot.getCount(); j++) {
				DesignElement designElement = containerSlot.getContent(j);
				reportDesignModule.add(designElement, slotId);
			}
		}

		reportDesignHandle.getDataSets().iterator();
		String filePath = UploadHelper.getReport();
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddhhmmss");
		String fileName = reportScript.getName() + "." + suffix.split("\\.")[1];
		savePath = file.getPath() + "\\" + fileName;

		file = new File(savePath);
		reportDesignHandle.saveAs(file.getPath());

		reportDesignHandle.close();

		long begin = System.currentTimeMillis();
		reportScript.setDirectory(savePath);
		reportScript.setSuffix(suffix);
		reportScript.setCreatedTime(new Date());
		reportScript.setSuffix(fileName);
		ReportScriptRepository.save(reportScript);
		long end = System.currentTimeMillis();
		engine.destroy();
		Platform.shutdown();
	}

	public void queryMerge(Query<ReportScript> query) {
		ReportScriptRepository.findReportmerge(query);
		
	}
	

}
