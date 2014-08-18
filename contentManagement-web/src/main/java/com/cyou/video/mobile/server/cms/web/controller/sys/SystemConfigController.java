package com.cyou.video.mobile.server.cms.web.controller.sys;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.model.collection.SystemConfig;
import com.cyou.video.mobile.server.cms.service.sys.SystemConfigService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 类型维护controller
 * 
 * @author lusi
 */
@Controller
@RequestMapping("/web/systemconfig")
public class SystemConfigController {
  private Logger logger = LoggerFactory.getLogger(SystemConfigController.class);

  @Autowired
  private SystemConfigService systemConfigService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap list(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      model.addAttribute("page", systemConfigService.list(params));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: list()] Get contentType log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap delete(@RequestBody SystemConfig systemConfig, ModelMap model) {
    try {
      systemConfigService.delete(systemConfig);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: del ()] del contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
  
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap update(@RequestBody SystemConfig systemConfig, ModelMap model) {
    try {
      systemConfigService.save(systemConfig);
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
