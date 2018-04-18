/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.OAuthResponse.OAuthResponseBuilder;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.security.UserDetails;
import com.cntest.util.ExceptionHelper;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2015年3月19日
 * @version 1.0
 **/
//@Controller
//@RequestMapping("/oss")
public class OSSServerController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(OSSServerController.class);

	private static final String INVALID_CLIENT_DESCRIPTION = "客户端验证失败，如错误的client_id/client_secret。";

	private static OAuthService oAuthService;
	@Autowired(required = false)
	private UserService userService;
	@Autowired
	private URLResourceService resourceService;

	@Autowired
	private CacheManager cacheManager;

	public OSSServerController() {
		init();
	}

	@RequestMapping("/authorize")
	public ResponseEntity authorize(ServletRequest request, ServletResponse response) throws URISyntaxException, OAuthSystemException {

		try {
			// 构建OAuth 授权请求
			HttpServletRequest httpRequest = WebUtils.toHttp(request);
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(httpRequest);
			String clientId = oauthRequest.getClientId();

			// 检查提交的客户端id是否正确
			ResponseEntity errResponse = validateClient(httpRequest, clientId);
			if (errResponse != null) {
				return errResponse;
			}

			UserDetails user = null;
			try {
				user = userService.getCurrentLoginedUser();
			} catch (Exception e) {
				logger.error(ExceptionHelper.trace2String(e));
			}
			// String username = (String) subject.getPrincipal();
			// 生成授权码
			String authorizationCode = null;
			// responseType目前仅支持CODE，另外还有TOKEN
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				authorizationCode = oauthIssuerImpl.authorizationCode();
				oAuthService.addAuthCode(authorizationCode, user.getUserName());
			}

			// 进行OAuth响应构建
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(httpRequest,
					HttpServletResponse.SC_FOUND);
			// 设置授权码
			builder.setCode(authorizationCode);
			// 得到到客户端重定向地址
			String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
			builder.location(redirectURI);
			// 构建响应
			final OAuthResponse oAuthResponse = buildOAuthResponse(httpRequest, builder);

			// 根据OAuthResponse返回ResponseEntity响应
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(oAuthResponse.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));

		} catch (OAuthProblemException e) {

			// 出错处理
			String redirectUri = e.getRedirectUri();
			if (OAuthUtils.isEmpty(redirectUri)) {
				// 告诉客户端没有传入redirectUri直接报错
				return new ResponseEntity("找不到客户端回调地址！", HttpStatus.NOT_FOUND);
			}

			// 返回错误消息（如?error=）
			final OAuthResponse thisResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e).location(redirectUri)
					.buildQueryMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(thisResponse.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(thisResponse.getResponseStatus()));
		}
	}

	@RequestMapping("/accessToken")
	public HttpEntity token(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
		init();
		try {
			// 构建OAuth请求
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
			HttpServletRequest httpRequest = WebUtils.toHttp(request);
			String clientId = oauthRequest.getClientId();
			// 检查提交的客户端id是否正确
			ResponseEntity errResponse = validateClient(httpRequest, clientId);
			if (errResponse != null) {
				return errResponse;
			}

			// 检查客户端安全KEY是否正确
			if (!oAuthService.checkClientSecret(oauthRequest.getClientSecret())) {
				OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT).setErrorDescription(INVALID_CLIENT_DESCRIPTION).buildJSONMessage();
				return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}

			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
			// 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				if (!oAuthService.checkAuthCode(authCode)) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT).setErrorDescription("错误的授权码").buildJSONMessage();
					return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
				}
			}

			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			oAuthService.addAccessToken(accessToken, oAuthService.getUsernameByAuthCode(authCode));

			// 生成OAuth响应
			OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(accessToken)
					.setExpiresIn(String.valueOf(oAuthService.getExpireIn())).buildJSONMessage();

			// 根据OAuthResponse生成ResponseEntity
			return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));

		} catch (OAuthProblemException e) {
			// 构建错误响应
			OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e).buildJSONMessage();
			return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
		}

	}

	private ResponseEntity validateClient(HttpServletRequest httpRequest, String clientId) throws OAuthSystemException {
		if (!resourceService.appExists(clientId)) {
			OAuthResponseBuilder builder = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError(OAuthError.TokenResponse.INVALID_CLIENT).setErrorDescription(INVALID_CLIENT_DESCRIPTION);

			OAuthResponse oAuthresponse = buildOAuthResponse(httpRequest, builder);
			return new ResponseEntity(oAuthresponse.getBody(), HttpStatus.valueOf(oAuthresponse.getResponseStatus()));
		}
		return null;
	}

	private OAuthResponse buildOAuthResponse(HttpServletRequest httpRequest, OAuthResponseBuilder builder) throws OAuthSystemException {
		if (isReponsedJson(httpRequest)) {
			return builder.buildJSONMessage();
		}
		return builder.buildQueryMessage();
	}

	private boolean isReponsedJson(HttpServletRequest httpRequest) {
		String contentType = httpRequest.getHeader("Content-Type");
		if (contentType == null || contentType.length() < 1)
			return false;
		if (contentType.toLowerCase().contains("application/json")) {
			return true;
		}

		return false;
	}

	private void init() {
		if (oAuthService == null) {
			oAuthService = new OAuthService(cacheManager.getCache("code-cache"));
		}
	}

	private class OAuthService {
		private Cache cache;

		public OAuthService(Cache cache) {
			this.cache = cache;
		}

		public void addAuthCode(String authCode, String username) {
			cache.put(authCode, username);
		}

		public void addAccessToken(String accessToken, String username) {
			cache.put(accessToken, username);
		}

		public String getUsernameByAuthCode(String authCode) {
			return (String) cache.get(authCode).get();
		}

		public String getUsernameByAccessToken(String accessToken) {
			return (String) cache.get(accessToken).get();
		}

		public boolean checkAuthCode(String authCode) {
			return cache.get(authCode) != null;
		}

		public boolean checkAccessToken(String accessToken) {
			return true;
			// return cache.get(accessToken) != null;
		}

		public boolean checkClientSecret(String clientSecret) {
			return true;
			// return cache.get(clientSecret) != null;
			// return clientService.findByClientSecret(clientSecret) != null;
		}

		public long getExpireIn() {
			return 3600L;
		}
	}
}
