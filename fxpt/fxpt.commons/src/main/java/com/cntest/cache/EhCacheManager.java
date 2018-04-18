/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月4日
 * @version 1.0
 **/
public class EhCacheManager  implements CacheManager {

	private static final Logger logger = LoggerFactory.getLogger(EhCacheManager.class);

	private net.sf.ehcache.CacheManager cacheManager;
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {

		net.sf.ehcache.Ehcache cache = cacheManager.getEhcache(name);
		if (cache == null) {
			logger.info("Cache with name '{}' does not yet exist.  Creating now.", name);
            
            this.cacheManager.addCache(name);

            cache = this.cacheManager.getCache(name);

            logger.info("Added EhCache named [" + name + "]");
            
        } else {
        	logger.info("Using existing EHCache named [" + cache.getName() + "]");       
        }
		 return new EhCache<K, V>(cache);
	}

	public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
}

