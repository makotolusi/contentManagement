package com.cyou.video.mobile.server.cms.rest.controller.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.model.sys.SysProperty;
import com.cyou.video.mobile.server.cms.rest.common.PrivateUtil;
import com.cyou.video.mobile.server.cms.service.sys.SysPropertyService;
import com.cyou.video.mobile.server.common.Constants;

@Controller
@RequestMapping("/rest/sys/sysProperty")
public class SysPropertyController {
  
  @Autowired
  private SysPropertyService sysPropertyservice;

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listSysProperty(@RequestBody
  Map<String, String> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    String ver = "1.0";
    if(request.getHeader("v") != null) {
      ver = request.getHeader("v");
    }
    int plat = Integer.parseInt(String.valueOf(request.getAttribute("plat")));
    int appType = Integer.parseInt(String.valueOf(request.getAttribute("appType")));
    String appName = String.valueOf(request.getAttribute("appName"));
    String common = PrivateUtil.getMainChannel(plat, (appType == Consts.APP_TYPE.GAMETOPLINE.getValue() ? "MOBILEME_YXKK":"ZQSTATEGY"));
    if(StringUtils.isBlank(common)){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return model;
    }
    String channel = String.valueOf(request.getAttribute("channel"));
    if(StringUtils.isBlank(channel)) {
      channel = common;
    }
    try {
      SysProperty syspro = new SysProperty();
      syspro.setPlat(plat);
      syspro.setType(Constants.STATUS.ON.getValue());
      syspro.setStatus(Constants.STATUS.ON.getValue());
      syspro.setVersion(ver);
      syspro.setAppType(appType);
      List<SysProperty> list = sysPropertyservice.restListSysProperty(syspro);
      Map<String,SysProperty> map = new HashMap<String, SysProperty>();
      List<SysProperty> result = new ArrayList<SysProperty>();
      for(SysProperty sys : list) {
        if(!StringUtils.equals(common, sys.getChannel()) && !StringUtils.equals(channel, sys.getChannel())){
          continue;
        }
        if(appType == Consts.APP_TYPE.ZQSTRATEGY.getValue()){//如果是最强攻略的，过滤掉不属于当前app的系统参数
          List<String> applist = new ArrayList<String>(Arrays.asList(sys.getAppSx().split(",")));
          if(!applist.contains(appName)){
            continue;
          }
        }
        if(map.containsKey(sys.getKey())){
          SysProperty sysp = (SysProperty) map.get(sys.getKey());
          if(StringUtils.equals(channel, sys.getChannel())){
            map.remove(sysp.getKey());
            map.put(sys.getKey(), sys);
          }
        }else{
          map.put(sys.getKey(), sys);
        }
      }
      //map转list
      result.addAll(map.values());
      if(result != null && result.size() > 0) {
        model.addAttribute("result", result);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
      }
      else {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      }
    }
    catch(Exception e) {
      response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      e.printStackTrace();
    }
    return model;
  }
  
  @RequestMapping(value = "{key}", method = RequestMethod.GET)
  @ResponseBody
  public ModelMap getValueByKey(@PathVariable("key") String key, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      String ver = "1.0";
      if(request.getHeader("v") != null) {
        ver = request.getHeader("v");
      }
      int plat = Integer.parseInt(String.valueOf(request.getAttribute("plat")));
      int appType = Integer.parseInt(String.valueOf(request.getAttribute("appType")));
      String appName = String.valueOf(request.getAttribute("appName"));
      String channel = String.valueOf(request.getAttribute("channel"));
      List<SysProperty> sysList = sysPropertyservice.listSysPropertyByKey(key,Constants.STATUS.ON.getValue(),plat,ver,appType);
      String common = PrivateUtil.getMainChannel(plat, (appType == Consts.APP_TYPE.GAMETOPLINE.getValue() ? "MOBILEME_YXKK":"ZQSTATEGY"));
      if(!StringUtils.isBlank(common) && StringUtils.isNotBlank(ver) && StringUtils.isNotBlank(key)){
        if(StringUtils.isBlank(channel)) {
          channel = common;
        }
        SysProperty sysProperty = null;
        SysProperty commSys = null;
        if(sysList != null && sysList.size() > 0) {
          for(SysProperty sys : sysList) {
            if(!StringUtils.equals(key, sys.getKey())){
              continue;
            }
            if(!StringUtils.equals(common, sys.getChannel()) && !StringUtils.equals(channel, sys.getChannel())){
              continue;
            }
            if(appType == Consts.APP_TYPE.ZQSTRATEGY.getValue()){//如果是最强攻略的，过滤掉不属于当前app的系统参数
              List<String> applist = new ArrayList<String>(Arrays.asList(sys.getAppSx().split(",")));
              if(!applist.contains(appName)){
                continue;
              }
            }
            if(StringUtils.equals(channel, sys.getChannel())){
              sysProperty = sys;
            }else if(StringUtils.equals(common, sys.getChannel())){
              commSys = sys;
            }
          }
          if(sysProperty == null){
            sysProperty = commSys;
          }
          if(sysProperty != null){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            model.addAttribute("result", sysProperty);
          }else{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
          }
        }else{
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      }else{
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
    catch(Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
    return model;
  }
}
