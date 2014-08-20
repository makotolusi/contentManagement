package com.cyou.video.mobile.server.cms.web.controller.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.model.security.ManageItem;
import com.cyou.video.mobile.server.cms.service.security.ManageItemService;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;

/**
 * CMS管理项controller
 * 
 * @author jyz
 */
@Controller
@RequestMapping("/web/manageItem")
public class ManageItemController {

  private Logger logger = LoggerFactory.getLogger(ManageItemController.class);

  @Autowired
  private ManageItemService manageItemService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listManageItem(ModelMap model) {
    try {
      List<ManageItem> itemList = manageItemService.listManageItem();
      model.addAttribute("children", itemList);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listManageItem()] Get manage item list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap createManageItem(@RequestBody
  ManageItem manageItem, ModelMap model) {
    try {
      logger.info("[method: createManageItem()] Create manage item by params : "
          + JacksonUtil.getJsonMapper().writeValueAsString(manageItem));
      manageItemService.createManageItem(manageItem);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: createManageItem()] Create manage item  : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "{id}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap updateManageItem(@PathVariable("id")
  int manageItemId, @RequestBody
  ManageItem manageItem, ModelMap model) {
    try {
//      manageItem.setId(manageItemId);
      logger.info("[method: updateManageItem()] Update manage item by params : "
          + JacksonUtil.getJsonMapper().writeValueAsString(manageItem));
//      manageItemService.updateManageItem(manageItem);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: updateManageItem()] Update manage item : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
  @ResponseBody
  public ModelMap deleteManageItem(@PathVariable("id")
  int manageItemId, ModelMap model) {
    try {
      logger.info("[method: deleteManageItem()] Delete manage item by params : {manageItemId=" + manageItemId + "}");
//      manageItemService.deleteManageItem(manageItemId);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: deleteManageItem()] Delete manage item : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "{id}/status", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap updateStatus(@PathVariable("id")
  int manageItemId, @RequestParam("status")
  int status, ModelMap model) {
    try {
      logger.info("[method: updateStatus()] Update manage item status by params : {manageItemId=" + manageItemId
          + ",status=" + status + "}");
//      manageItemService.updateStatus(manageItemId, status);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: updateStatus()] Update manage item status : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/order", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap updateOrder(@RequestBody
  Map<String, Integer> map, ModelMap model) {
    try {
      logger.info("[method: updateOrder()] Update manage Item order by params : "
          + JacksonUtil.getJsonMapper().writeValueAsString(map));
//      manageItemService.updateOrder(map);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: updateOrder()] Update manage Item order : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
}
