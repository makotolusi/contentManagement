package com.cyou.video.mobile.server.cms.web.controller.sys;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
import com.cyou.video.mobile.server.cms.model.sys.ContentTypeActionAndTag;
import com.cyou.video.mobile.server.cms.service.sys.ContentTypeService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 类型维护controller
 * 
 * @author lusi
 */
@Controller
@RequestMapping("/web/contentType")
public class ContentTypeController {
  private Logger logger = LoggerFactory.getLogger(ContentTypeController.class);

  @Autowired
  private ContentTypeService contentTypeService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap list(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      model.addAttribute("page", contentTypeService.listContentType(params));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
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
  public ModelMap create(@RequestBody ContentType contentType, ModelMap model) {
    try {
      contentTypeService.createContentType(contentType);
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
  public ModelMap delete(@RequestBody ContentType contentType, ModelMap model) {
    try {
      contentTypeService.deleteContentType(contentType);
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
  public ModelMap update(@RequestBody ContentType contentType, ModelMap model) {
    try {
      contentTypeService.updateContentType(contentType);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: update ()] update contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
  
  @RequestMapping(value = "/listAction", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listAction(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      model.addAttribute("page", contentTypeService.listContentTypeAction(params));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: list()] Get contentType log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
  @RequestMapping(value = "/saveAction", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap saveAction(@RequestBody ContentTypeActionAndTag cta, ModelMap model) {
    try {
      if(StringUtils.isEmpty(cta.getId())){
        contentTypeService.createContentTypeAction(cta);
      }else{
        contentTypeService.updateContentTypeAction(cta);
      }
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: update ()] update contentType : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
  
  @RequestMapping(value = "/deleteAction", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap deleteAction(@RequestBody ContentTypeActionAndTag contentType, ModelMap model) {
    try {
      contentTypeService.deleteAction(contentType);
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
