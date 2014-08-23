package com.cyou.video.mobile.server.cms.service.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.service.push.impl.DataNotFoundException;
import com.cyou.video.mobile.server.cms.service.sys.SystemConfigService;

@Component("httpRequestProvider")
public class HttpRequestProvider {

  @Autowired
  private SystemConfigService systemConfigService;
  
  public UserToken getToken(String token) throws DataNotFoundException {
    Map<String, String> params=new HashMap<String, String>();
    String str= HttpUtils.post(systemConfigService.getByKey("cms_token_uri")+"?token="+token, params);
    if(StringUtils.isEmpty(str))
      return null;
    JSONObject obj = new JSONObject(str).getJSONObject("token");
    UserToken t=new UserToken();
    t.setId(obj.getInt("id"));
    t.setPlat(obj.getInt("plat"));
    t.setToken(obj.getString("token"));
    t.setChannel(obj.getString("channel"));
    t.setCurUsedVersion(obj.getString("curUsedVersion"));
    return t;
  }
}
