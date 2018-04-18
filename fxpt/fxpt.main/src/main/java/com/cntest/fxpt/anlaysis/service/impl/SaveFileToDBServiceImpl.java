/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.SvaeFileToDBServiceImpl.java	1.0 2014年12月10日:下午5:43:40
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.OS;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.SaveFileTask;
import com.cntest.fxpt.anlaysis.service.ISaveFileToDBService;
import com.cntest.fxpt.anlaysis.uitl.DBInfo;
import com.cntest.fxpt.anlaysis.uitl.Util;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月10日 下午5:43:40
 * @version 1.0
 */
public class SaveFileToDBServiceImpl implements ISaveFileToDBService {
	private final Logger log = LoggerFactory
			.getLogger(SaveFileToDBServiceImpl.class);

	@Override
	public void save(SaveFileTask event) throws Exception {
		String cmdDir = Util.getThridProcessDir();
		String script = cmdDir + "mysqlExec";

		String dataDir = Util.getDataDir();
		DBInfo db = new DBInfo();
		CommandLine cmd = new CommandLine(resolveScriptForOS(script));
		cmd.addArgument("-u" + db.getUser());
		cmd.addArgument("-p" + db.getPassword());
		cmd.addArgument("-h" + db.getHost());
		cmd.addArgument("-P" + db.getPort());
		cmd.addArgument("-D" + db.getDbName());
		cmd.addArgument(event.getFile().getName());

		Executor exec = new DefaultExecutor();
		exec.setWorkingDirectory(new File(dataDir));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(out, err);
		exec.setStreamHandler(pumpStreamHandler);
		HashMap<String, String> ev = new HashMap<>();
		ev.put("PATH", cmdDir);
		try {
			int exitValue = exec.execute(cmd, ev);
		} catch (Exception e) {
			log.error(new String(err.toByteArray()));
			throw e;
		} finally {
			log.debug(new String(out.toByteArray()));
		}

	}

	private File resolveScriptForOS(final String script) {
		if (OS.isFamilyWindows()) {
			return new File(script + ".bat");
		} else if (OS.isFamilyUnix()) {
			return new File(script + ".sh");
		} else if (OS.isFamilyOpenVms()) {
			return new File(script + ".dcl");
		} else {
			throw new RuntimeException("Test not supported for this OS");
		}
	}
}
