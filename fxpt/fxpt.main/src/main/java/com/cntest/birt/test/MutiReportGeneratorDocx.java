//package com.cntest.birt.test;
//
//import java.util.HashMap;
//import java.util.logging.Level;
//
//import org.eclipse.birt.core.framework.Platform;
//import org.eclipse.birt.report.engine.api.DocxRenderOption;
//import org.eclipse.birt.report.engine.api.EngineConfig;
//import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
//import org.eclipse.birt.report.engine.api.IReportEngine;
//import org.eclipse.birt.report.engine.api.IReportEngineFactory;
//import org.eclipse.birt.report.engine.api.IReportRunnable;
//import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
//import org.eclipse.birt.report.model.api.ReportDesignHandle;
//import org.eclipse.birt.report.model.core.ContainerSlot;
//import org.eclipse.birt.report.model.core.DesignElement;
//import org.eclipse.birt.report.model.core.Module;
//
//
//public class MutiReportGeneratorDocx {
//
//	public static void main(String args[]) throws Exception {
//		MutiReportGeneratorDocx rg = new MutiReportGeneratorDocx();
//		rg.executeReport("");
//	}
//	@SuppressWarnings({ "unchecked" })
//	public void executeReport(String activeIndicator)  {
//
//		IReportEngine engine = null;
//		EngineConfig config = null;
//
//		try {
//			config = new EngineConfig();
//			config.setLogConfig("c:/temp/test", Level.FINEST);
//			Platform.startup(config);
//			IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
//			engine = factory.createReportEngine(config);
//
//			IReportRunnable reportDesign = null;	
//			//脚本1
//			reportDesign = engine.openReportDesign("E:\\birtR\\new_report_0.rptdesign");
//			//脚本2
//			IReportRunnable reportDesign001 = engine.openReportDesign("E:\\birtR\\new_report_001.rptdesign");
//			//脚本3
//			IReportRunnable hhcDesign = engine.openReportDesign("E:\\birtR\\hhc.rptdesign");
//			
//			ReportDesignHandle reportDesignHandle = (ReportDesignHandle) reportDesign.getDesignHandle();
//			reportDesignHandle.getParameters();
//			reportDesignHandle.getDataSources();
//			reportDesignHandle.getDataSets();
//			reportDesignHandle.getCubes();
//			
//			Module reportDesignModule = reportDesign.getDesignHandle().getModule();
//			Module reportDesignModule001 = reportDesign001.getDesignHandle().getModule();
//			
//			Module reportDesignModulehhc = hhcDesign.getDesignHandle().getModule();
//
//
//			int slotId = reportDesignHandle.getBody().getSlotID();
//			ContainerSlot containerSlot = reportDesignModule001.getSlot(slotId);
//			for (int j = 0; j < containerSlot.getCount(); j++) {
//				DesignElement designElement = containerSlot.getContent(j);
//				reportDesignModule.add(designElement, slotId);
//			}
//
//			slotId = reportDesignHandle.getBody().getSlotID();
//			ContainerSlot containerSlothhc = reportDesignModulehhc.getSlot(slotId);
//			for (int j = 0; j < containerSlothhc.getCount(); j++) {
//				DesignElement designElement = containerSlothhc.getContent(j);
//				reportDesignModule.add(designElement, slotId);
//			}
//
//			slotId = reportDesignHandle.getDataSources().getSlotID();
//			containerSlothhc = reportDesignModulehhc.getSlot(slotId);
//			for (int j = 0; j < containerSlothhc.getCount(); j++) {
//				DesignElement designElement = containerSlothhc.getContent(j);
//				reportDesignModule.add(designElement, slotId);
//			}
//			
//			reportDesignHandle.getDataSets().iterator();
//			
//			reportDesignHandle.saveAs("E:\\birtR\\new_report_0k1.rptdesign");
//			reportDesignHandle.close();
//			
//			IRunAndRenderTask task = engine.createRunAndRenderTask(reportDesign);
//			IGetParameterDefinitionTask parameterDefinitionTask = engine.createGetParameterDefinitionTask(reportDesign);
//			HashMap<String, String> params = parameterDefinitionTask.getDefaultValues();
//			params.put("aIndicator", activeIndicator);
//
//			DocxRenderOption options = new DocxRenderOption();
//			options.setOutputFormat("docx");
//			options.setOutputFileName("E:\\birtR\\output\\测试.docx");
//			
//			task.setRenderOption(options);
//			task.setParameterValues(params);
//
//			task.run();
//			task.close();
//			engine.destroy();
//			System.out.println("生成完成,路径为：E:\\birtR\\output\\测试.docx");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			Platform.shutdown();
//		}
//	}
//}
