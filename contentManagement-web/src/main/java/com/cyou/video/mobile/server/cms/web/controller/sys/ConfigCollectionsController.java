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

import com.cyou.video.mobile.server.cms.model.sys.ConfigCollections;
import com.cyou.video.mobile.server.cms.service.sys.ConfigCollectionsService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 应用维护controller
 * 
 * @author lusi
 */
@Controller
@RequestMapping("/web/configcollections")
public class ConfigCollectionsController {
  private Logger logger = LoggerFactory.getLogger(ConfigCollectionsController.class);

  @Autowired
  private ConfigCollectionsService configCollectionsService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap list(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      model.addAttribute("page", configCollectionsService.list(params));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: list()] Get contentType log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap create(@RequestBody
  ConfigCollections configCollections, ModelMap model) {
    try {
      configCollectionsService.save(configCollections);
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
  ConfigCollections configCollections, ModelMap model) {
    try {
      configCollectionsService.delete(configCollections);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: del ()] del contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

}
