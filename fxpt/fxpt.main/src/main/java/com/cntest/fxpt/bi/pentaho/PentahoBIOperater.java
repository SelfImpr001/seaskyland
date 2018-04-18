/**
 * 
 */
package com.cntest.fxpt.bi.pentaho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cntest.fxpt.bi.BIOperater;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.repository.smartBi.IBiUserDao;
import com.cntest.fxpt.util.SSHHelper;
import com.jcraft.jsch.JSchException;

/**
 * @author Administrator
 * 
 */
@Component("bi.BIOperater")
public class PentahoBIOperater implements BIOperater {

  @Autowired(required = false)
  @Qualifier("bi.biUserDao")
  private IBiUserDao dao;

  public BiUser getUser() {
    return dao.getBiUser(2);
  }

  public String createUser(String biServerUrl, String userName, String systemAdminName,
      String systemAdminPassword) throws ClientProtocolException, IOException, URISyntaxException {
    String resStr = "failure";
    String currentOperaterUrl = biServerUrl + "/api/userroledao/createUser";

    HttpPut request = new HttpPut(currentOperaterUrl);
    request.addHeader("Content-type", "application/json");
    String jsonObj = ("{ \"userName\":\"" + userName + "\", \"password\":\"test\" }");
    HttpEntity entity = new StringEntity(jsonObj, "utf-8");
    request.setEntity(entity);
    HttpResponse response = doRequest(request, systemAdminName, systemAdminPassword);
    // 在pentaho创建用户
    int statusCode = response.getStatusLine().getStatusCode();
    if (200 == statusCode) {
      // 创建成功后仅删除pentaho的用户文件夹
      deletePentahoFile(biServerUrl, userName, systemAdminName, systemAdminPassword);
      resStr = "success";
    } else if (403 == statusCode) {
      /*
       * currentOperaterUrl = biServerUrl + "/api/userroledao/userRoles?userName=" + userName;
       * HttpGet userRolesRequest = new HttpGet(currentOperaterUrl); HttpResponse userRolesResponse
       * = doRequest(userRolesRequest, systemAdminName, systemAdminPassword); statusCode =
       * userRolesResponse.getStatusLine().getStatusCode(); if(200 == statusCode) { String a =
       * IOUtils.toString(userRolesResponse.getEntity().getContent()); resStr = "success"; }
       */
      resStr = "success";
    }
    return resStr;
  }

  // 查询、解析、删除用户在pentaho的文件夹
  private void deletePentahoFile(String biServerUrl, String userName, String systemAdminName,
      String systemAdminPassword) throws ClientProtocolException, IOException, URISyntaxException {
    // 查询个人信息的xmlHttpGet
    String currentOperaterUrl = biServerUrl + "/api/repo/files/home/" + userName + "/acl";
    HttpGet request = new HttpGet(currentOperaterUrl);
    HttpResponse response = doRequest(request, systemAdminName, systemAdminPassword);
    int statusCode = response.getStatusLine().getStatusCode();
    if (200 == statusCode) {
      // 放回的流信息（需要转换并解析出Id）
      InputStream is = response.getEntity().getContent();
      String userStr = convertStreamToString(is);
      // 删除用HttpPut
      if (userStr.indexOf("<id>") > 0) {
        String[] userXML = userStr.split("id>");
        if (userXML.length > 2) {
          String id = userXML[1] != null
              ? userXML[1].toString().substring(0, userXML[1].toString().length() - 2) : "";
          try {
            SSHHelper.startCMD("curl -X PUT -u " + systemAdminName + ":" + systemAdminPassword
                + " -d '" + id + "' " + biServerUrl + "/api/repo/files/delete");

          } catch (JSchException e) {
          }
        }
      }
    }
  }

  // 流信息转换成String
  private String convertStreamToString(InputStream is) {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = "";
    try {
      while ((line = reader.readLine()) != null)
        sb.append(line + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }

  private HttpResponse doRequest(HttpRequest request, String systemAdminName,
      String systemAdminPassword) throws URISyntaxException, IOException, ClientProtocolException {


    String biServerUrl = request.getRequestLine().getUri();
    URI uri = new URI(biServerUrl);
    String hostname = uri.getHost();
    int port = uri.getPort();
    String scheme = uri.getScheme();
    HttpHost target = new HttpHost(hostname, port, scheme);

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    Credentials credentials = new UsernamePasswordCredentials(systemAdminName, systemAdminPassword);
    credentialsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
        credentials);

    AuthCache authCache = new BasicAuthCache();
    AuthScheme authScheme = new BasicScheme();
    authCache.put(target, authScheme);

    HttpClientContext context = HttpClientContext.create();
    context.setCredentialsProvider(credentialsProvider);
    context.setAuthCache(authCache);

    HttpClient httpClient =
        HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();



    HttpResponse response = httpClient.execute(target, request, context);
    return response;
  }

  @Override
  public String createUser(String userName) {
    BiUser user = getUser();
    String systemAdminName = user.getUserName();
    String systemAdminPassword = user.getUserPassword();
    String biServerUrl = user.getBiInfo().getUrl();
    String biServerName = user.getBiInfo().getName();
    if (!"pentaho".equals(biServerName)) {
      return "failure";
    }
    try {
      return createUser(biServerUrl, userName, systemAdminName, systemAdminPassword);
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return "failure";
  }

  public String removeUser(String biServerUrl, String userName, String systemAdminName,
      String systemAdminPassword) throws ClientProtocolException, IOException, URISyntaxException {
    String resStr = "failure";
    String currentOperaterUrl = biServerUrl + "/api/userroledao/deleteUsers?userNames=" + userName;


    HttpPut request = new HttpPut(currentOperaterUrl);
    HttpResponse response = doRequest(request, systemAdminName, systemAdminPassword);
    int statusCode = response.getStatusLine().getStatusCode();
    if (200 == statusCode) {
      resStr = "success";
    } else if (403 == statusCode) {
      resStr = "success";
    }
    return resStr;
  }

  @Override
  public String removeUser(String userName) {
    BiUser user = getUser();
    String systemAdminName = user.getUserName();
    String systemAdminPassword = user.getUserPassword();
    String biServerUrl = user.getBiInfo().getUrl();
    String biServerName = user.getBiInfo().getName();
    if ("pentaho".equals(biServerName)) {
      return "failure";
    }
    try {
      return removeUser(biServerUrl, userName, systemAdminName, systemAdminPassword);
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return "failure";
  }



  @Override
  public String biServerUrl(String userName) {
    BiUser user = getUser();
    String systemAdminName = user.getUserName();
    String systemAdminPassword = user.getUserPassword();
    String biServerUrl = user.getBiInfo().getUrl();
    String biServerName = user.getBiInfo().getName();
    return biServerUrl;
  }

  @Override
  public BiUser getBiUser(String userName) {
    BiUser user = getUser();

    return user;
  }

}
