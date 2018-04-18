/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月13日
 * @version 1.0
 **/
public class IpReader {

	private static Logger logger = LoggerFactory.getLogger(IpReader.class);

	private static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		logger.debug(osName);
		if (osName.toLowerCase().indexOf("windows") > -1)
			isWindowsOS = true;
		return isWindowsOS;
	}

	public static String getLocalIP() {
		String localIP = "127.0.0.1";
		try {
			if (isWindowsOS()) {
				localIP = InetAddress.getLocalHost().getHostAddress();
			} else {
				boolean lookupIP = false;
				Enumeration<NetworkInterface> netInterfaces = 
						(Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
				while(netInterfaces.hasMoreElements()) {
					if(lookupIP)
						break;
					NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while(ips.hasMoreElements()) {
						InetAddress ip = ips.nextElement();
						if(ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
							localIP = ip.getHostAddress();
							lookupIP = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionHelper.trace2String(e));
		}
		return localIP;
	}
	
	/**
	 * 获取访问者IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip))
			return ip;
		
		ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)){
			//多次反向代理会出现过个IP,第一个为真是IP
			int index = ip.indexOf(",");
			return (index != -1) ? ip.substring(0, index) : ip;
		}else{
			return request.getRemoteAddr();
		}
	}
}
