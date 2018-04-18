/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-9-10
 * @version 1.0
 **/
public class XmlUtil {
	private final static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	
	public static String getNodeText(Node node ,String xpath) {
		return null;
	}
	
	public static  Document getXmlDocument(String path) {
		try {  
            SAXReader reader = new SAXReader();  
            InputStream in = new FileInputStream(path);
            Document doc = reader.read(in);  
            return doc; 
        } catch (DocumentException e) {  
            logger.error(ExceptionHelper.trace2String(e));
        } catch (FileNotFoundException e) {
        	logger.error(ExceptionHelper.trace2String(e));
		}  
		return null;
	}
}

