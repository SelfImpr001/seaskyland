package com.cntest.fxpt.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSH连接--cmd命令执行帮助类
 * @author Chenyou
 * @param command String类型的   cmd命令 
 * @throws IOException
 * @throws JSchException
 */
public class SSHHelper {

/**
 * 默认配置执行cmd（System.properties）
 * @param command
 * @throws IOException
 * @throws JSchException
 */
public static void startCMD(String command) throws IOException,JSchException {
	
		String host = SystemConfig.newInstance().getValue("SSH.host");
		int port = 22;
		String user = SystemConfig.newInstance().getValue("SSH.user");
		String password = SystemConfig.newInstance().getValue("SSH.password");
		exeCommand(host,port,user,password,command);
		
	}
	
/**
 * 自定义配置连接SSH，执行CMD
 * @param host  主机名称
 * @param port  主机端口
 * @param user  用户名
 * @param password  密码
 * @param command   CMD 命令
 * @return
 * @throws JSchException
 * @throws IOException
 */
public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {
        
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        //java.util.Properties config = new java.util.Properties();
        //config.put("StrictHostKeyChecking", "no");
        
        session.setPassword(password);
        session.connect();
        ChannelExec channelExec = (ChannelExec) 

        session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");
        
        channelExec.disconnect();
        session.disconnect();
        
        return out;
    }

}