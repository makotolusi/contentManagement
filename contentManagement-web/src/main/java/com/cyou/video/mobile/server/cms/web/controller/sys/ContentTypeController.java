package com.cyou.video.mobile.server.cms.web.controller.sys;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
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

  private ContentTypeService contentTypeService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listUserLog(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      model.addAttribute("page", contentTypeService.listContentType(params));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listUserLog()] Get user log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listUserLog(@RequestBody ContentType contentType, ModelMap model) {
    try {
      contentTypeService.createContentType(contentType);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listUserLog()] Get user log list : error!" + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
}
