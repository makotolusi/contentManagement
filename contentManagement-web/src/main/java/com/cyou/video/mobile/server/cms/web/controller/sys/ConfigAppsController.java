package com.cyou.video.mobile.server.cms.web.controller.sys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.model.security.Manager;
import com.cyou.video.mobile.server.cms.model.sys.ConfigApps;
import com.cyou.video.mobile.server.cms.service.sys.ConfigAppsService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 应用维护controller
 * 
 * @author lusi
 */
@Controller
@RequestMapping("/web/configapps")
public class ConfigAppsController {
  private Logger logger = LoggerFactory.getLogger(ConfigAppsController.class);

  @Autowired
  private ConfigAppsService configAppsService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap list(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      model.addAttribute("page", configAppsService.listContentType(params));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: list()] Get contentType log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/findByAppid", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap findByAppid(@RequestBody
  Map<String, Object> params, ModelMap model, HttpServletRequest request) {
    try {
      Manager manager = (Manager) request.getSession().getAttribute(Consts.SESSION_MANAGER);
      if(manager == null) {
        model.put("SUC", false);
        model.put("message", "您尚未登录系统，请重新登录！");
      }else{
        model.addAttribute("app", configAppsService.findByAppid(manager.getAppId()));
        model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
      }
    }
    catch(Exception e) {
      logger.error("[method: list()] Get contentType log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap create(@RequestBody
  ConfigApps configApps, ModelMap model) {
    try {
      configAppsService.createContentType(configApps);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method:create ()] create contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap delete(@RequestBody
  ConfigApps configApps, ModelMap model) {
    try {
      configAppsService.deleteContentType(configApps);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: del ()] del contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap update(@RequestBody
  ConfigApps configApps, ModelMap model) {
    try {
      configAppsService.updateContentType(configApps);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: update ()] update contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
}
