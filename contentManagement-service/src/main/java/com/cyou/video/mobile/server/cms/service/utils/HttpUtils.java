package com.cyou.video.mobile.server.cms.service.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.CharSet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpUtils {

  public static String post(String url, Map<String, String> params) {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    String body = null;
    HttpPost post = postForm(url, params);
    body = invoke(httpclient, post);
    httpclient.getConnectionManager().shutdown();
    return body;
  }

  public static String postJosn(String url, JSONObject json) {
    HttpParams httpParams = new BasicHttpParams(); 
    httpParams.setParameter("charset", "UTF-8"); 
    DefaultHttpClient httpclient = new DefaultHttpClient();
    String body = null;
    HttpPost post = postJSON(url, json);
    body = invoke(httpclient, post);
    httpclient.getConnectionManager().shutdown();
    return body;
  }

  private static HttpPost postForm(String url, Map<String, String> params) {

    HttpPost httpost = new HttpPost(url);
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();

    Set<String> keySet = params.keySet();
    for(String key : keySet) {
      nvps.add(new BasicNameValuePair(key, params.get(key)));
    }
    try {
      httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
      httpost.addHeader("Content-Type", "application/json");
    }
    catch(UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return httpost;
  }

  private static HttpPost postJSON(String url, JSONObject json) {
    HttpPost httpost = new HttpPost(url);
    try {
      StringEntity s = new StringEntity(json.toString(),"UTF-8");
//      s.setContentEncoding("UTF-8");
      s.setContentType("application/json");
      httpost.setEntity(s);
    }
    catch(UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return httpost;
  }

  private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {

    HttpResponse response = sendRequest(httpclient, httpost);
    String body = paseResponse(response);

    return body;
  }

  private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
    HttpResponse response = null;

    try {
      response = httpclient.execute(httpost);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  private static String paseResponse(HttpResponse response) {
    HttpEntity entity = response.getEntity();
    String body = null;
    try {
      body = EntityUtils.toString(entity);
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    return body;
  }
}
