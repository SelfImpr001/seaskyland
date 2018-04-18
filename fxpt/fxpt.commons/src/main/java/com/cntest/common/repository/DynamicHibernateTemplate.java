/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.repository;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.cntest.util.ExceptionHelper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <pre>
 * 用Freemarker 构建动态Hibernate sql/hql语句
 * </pre>
 * 
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public class DynamicHibernateTemplate implements InitializingBean, ResourceLoaderAware, ApplicationContextAware {
	private static Logger logger = LoggerFactory.getLogger(DynamicHibernateTemplate.class);

	//private static WatchService watcher;

	private static ConcurrentHashMap<WatchKey, Path> files = new ConcurrentHashMap<>();

	private String encoding = "UTF-8";

	private Configuration cfg;

	private List<String> fileNames;

	private ResourceLoader resourceLoader;

	private ApplicationContext applicationContext;

	private DocumentLoader documentLoader;

	private SessionFactory sessionFactory;
	
	//private boolean watch = false;

	private StringTemplateLoader statements = new StringTemplateLoader();

	public DynamicHibernateTemplate() {
		this.cfg = new Configuration();
		this.cfg.setTemplateLoader(statements);
		this.documentLoader = new DefaultDocumentLoader();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public void setDocumentLoader(DocumentLoader documentLoader) {
		this.documentLoader = documentLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (int i = 0; i < fileNames.size(); i++) {
			String fileName = fileNames.get(i).trim();
			logger.debug(" DynamicHibernateTemplate file:{}", fileName);
			if (resourceLoader instanceof ResourcePatternResolver) {
				try {
					Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(fileName);
					buildHQLMap(resources);					
				} catch (IOException ex) {
					logger.error(ExceptionHelper.trace2String(ex));
					throw new Exception("Could not resolve sql definition resource pattern [" + fileName + "]", ex);
				}
			} else {
				Resource resource = resourceLoader.getResource(fileName);
				buildHQLMap(resource);
			}

		}

	}
//
//	private void watchFiles() {
//		initWatchService();
//		Executors.newCachedThreadPool().submit(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					while (true) {
//						WatchKey key = watcher.take();
//						for (WatchEvent<?> event : key.pollEvents()) {
//							if (event.kind() == ENTRY_MODIFY) {
//								//Fileevent.context();
//								Path file = files.get(key);
//								Resource resource = resourceLoader.getResource(file.getFileSystem().toString());
//								buildHQLMap(resource);
//							}
//						}
//						key.reset();
//					}
//				} catch (Exception e) {
//					logger.error(ExceptionHelper.trace2String(e));
//				}
//			}
//		});
//	}
//
//	private void addWatch(URI uri) {
//		if(!this.watch)
//			return ;
//		initWatchService();
//		if(files.containsValue(uri))
//			return;
//		Path path = Paths.get(uri);
//		try {
//			WatchKey key = path.register(watcher, ENTRY_MODIFY);
//			files.put(key, path);
//		} catch (IOException e) {
//			logger.error(ExceptionHelper.trace2String(e));
//		}
//	}
//
//	private void initWatchService() {
//		if(this.watch && watcher == null) {
//			try {
//				watcher = FileSystems.getDefault().newWatchService();
//			} catch (IOException e) {
//				logger.error(ExceptionHelper.trace2String(e));
//			}			
//		}
//	}
//	
	public Query createSQLQuery(String queryName, Object root) {
		String sql = getQueryString(queryName, root);
		logger.debug("create sql query for:\n {}", sql);
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		} catch (Exception e) {
			query = sessionFactory.openSession().createSQLQuery(sql);
		}
		return query;
	}
	


	public Query createHQLQuery(String queryName, Object root) {
		String hql = getQueryString(queryName, root);
		logger.debug("create hql query for:\n {}", hql);
		Query query = null;
		try {
			query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		} catch (Exception e) {
			query = sessionFactory.openSession().createSQLQuery(hql);
		}
		return query;
	}

	public String getQueryString(String queryName, Object root) {

		Writer out = null;
		try {
			Template temp = this.cfg.getTemplate(queryName, encoding);
			out = new StringWriter();
			temp.process(root, out);
			out.flush();
			String body = out.toString();
			return body;
		} catch (Exception e) {
			logger.error(ExceptionHelper.trace2String(e));
			throw new RuntimeException(e);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					logger.error(ExceptionHelper.trace2String(e));
				}
		}
	}

	protected void buildHQLMap(Resource... resources) throws Exception {
		for (int i = 0; i < resources.length; i++) {
			buildStatements(resources[i]);
		}
	}

	private void buildStatements(Resource resource) throws Exception {
		try {
			InputSource inputSource = new InputSource(resource.getInputStream());
			org.w3c.dom.Document doc = this.documentLoader.loadDocument(inputSource, null, null,
					org.springframework.util.xml.XmlValidationModeDetector.VALIDATION_NONE, false);
			Element root = doc.getDocumentElement();
			List<Element> querys = DomUtils.getChildElements(root);
			for (Element query : querys) {
				String queryName = query.getAttribute("name");
				if (StringUtils.isEmpty(queryName)) {
					continue;
				}
				if (statements.findTemplateSource(queryName) != null) {
					throw new Exception("DynamicHibernateTemplate Service : duplicated query in a <query>." + queryName);
				}
				statements.putTemplate(queryName, DomUtils.getTextValue(query));
			}
		} catch (SAXParseException e) {
			logger.error(ExceptionHelper.trace2String(e));
			throw e;
		} catch (IOException e) {
			logger.error(ExceptionHelper.trace2String(e));
			throw e;
		}
	}

}
