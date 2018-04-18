/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.util;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.exception.SystemException;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2013年12月24日
 * @version 1.0
 **/
public class CloseableHelper {
	private static final Logger logger = LoggerFactory.getLogger(CloseableHelper.class);
	public static void close(Closeable closeable) {
		try {
			if(closeable != null)
				closeable.close();
		}catch(IOException e) {
			logger.error(ExceptionHelper.trace2String(e));
			throw new SystemException(e);
		}
	}
}

