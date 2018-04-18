package com.cntest.foura.application.permission;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;


/** 
 * <pre>
 * 凭证（密码，证书等等）验证
 * </pre>
 *  
 * @author 李贵庆2014年6月10日
 * @version 1.0
 **/
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;
    
    private int retryTotal = 5;//密码重试次数

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

//    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
//        //passwordRetryCache = cacheManager.getObject().getCache("passwordRetryCache");//cacheManager.getCache("passwordRetryCache");
//    	net.sf.ehcache.Cache cache = cacheManager.getCache("passwordRetryCache");
//    	passwordRetryCache = new EhCache<String, AtomicInteger>(cache);
//    }
    
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        //暂时不启用重试次数限制
//        if(retryCount == null) {
//            retryCount = new AtomicInteger(0);
//            passwordRetryCache.put(username, retryCount);
//        }
//        if(retryCount.incrementAndGet() > retryTotal) {
//            //if retry count > 5 throw
//            throw new ExcessiveAttemptsException();
//        }

        
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }

	public int getRetryTotal() {
		return retryTotal;
	}

	public void setRetryTotal(int retryTotal) {
		this.retryTotal = retryTotal;
	}
    
    
}
