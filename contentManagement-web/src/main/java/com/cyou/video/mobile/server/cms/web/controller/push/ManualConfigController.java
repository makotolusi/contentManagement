package com.cyou.video.mobile.server.cms.web.controller.push;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.SystemConfig;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.collection.MultiThreadExcuteService;
import com.cyou.video.mobile.server.cms.service.collection.MultiThreadExcuteXinGeService;
import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.cms.service.push.AutoPushService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;
import com.cyou.video.mobile.server.cms.service.push.SystemConfigService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 
 * 推送一些手动设置项 只有管理员可以看到的页面
 * 
 * @author LUSI
 */
@Controller
@RequestMapping("/web/activity/manual")
public class ManualConfigController {

  private Logger logger = LoggerFactory.getLogger(ManualConfigController.class);

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @Autowired
  PushTagService pushTagService;

  @Autowired
  AutoPushService autoPushService;

  @Autowired
  AppSelectService appSelectService;
  
  @Autowired
  MultiThreadExcuteService multiThreadExcuteService;

  @Autowired
  MultiThreadExcuteXinGeService multiThreadExcuteXinGeService;
  
  @Autowired
  private SystemConfigService systemConfigService;
  
  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView pushListPage() {
    List<StatisticJobLastUpdateTime> list = null;
    ModelAndView m = null;
    try {
      long start = new Date().getTime();
      logger.debug("updateLogInfo start  : " + new Date());
      list = clientLogCollectionService.getStatisticJobLastUpdateTime();
      List l = clientLogCollectionService.getPushTagExcuteStateInfo();
      long time = new Date().getTime() - start;
      logger.debug("excute time : " + (time / 1000));
      m = new ModelAndView("/activity/manual", "lastPvTime", list);
      m.addObject("pushTagExcuteStateInfo", l);
      m.addObject("threadTotal", pushTagService.getThreadTotal());
      m.addObject("threadNumList", pushTagService.getThreadNumList());
      m.addObject("appId",systemConfigService.getSystemConfigByConfigKey("sys_173app_id"));
      // thread num
      PushTagExcuteStateInfo p = pushTagService.getSysThreadNum();
      if(p != null) {
        m.addObject("sysThreadN", p.getThreadNum() + "");
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      logger.error("manual updateLogInfo exception " + e.getMessage());
      return m;
    }

    return m;

  }

  @RequestMapping(value = "/syncApp", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap syncApp(@RequestBody
  Map<String, String> params, ModelMap model) {
    try {
      appSelectService.syncApp();
    }
    catch(Exception e) {
      e.printStackTrace();
      model.put("message", "syncApp  exception " + e.getMessage());
      logger.error("manual syncApp exception " + e.getMessage());
    }

    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }

  @RequestMapping(value = "/pvAll", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap mongo2SQLDBIncManual(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      clientLogCollectionService.statisticsPv();
    }
    catch(Exception e) {
      e.printStackTrace();
      logger.error("manual static exception " + e.getMessage());
    }

    return model;
  }

  /**
   * 发送百度标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sendPushTags", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sendPushTags(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    if(params.get("psw") != null && "lusizuiqiang".equals(params.get("psw"))) {
      if(pushTagService.existRunningThread()) {
        model.put("message", "other thread is running!");
        return model;
      }
      return multiThreadExcuteService.sendPushTags(params, model);
    }
    else {
      model.put("message", "请输入密码");
      return model;
    }
  }

  /**
   * 发送百度标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/oldPushData", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap oldPushData(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
      return multiThreadExcuteXinGeService.pushHistoryToMongo(params, model);
  }

  
  /**
   * 发送百度标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sendPushTagsXG", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sendPushTagsXG(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    if(params.get("psw") != null && "lusizuiqiang".equals(params.get("psw"))) {
      if(pushTagService.existRunningThread()) {
        model.put("message", "other thread is running!");
        return model;
      }
      return multiThreadExcuteXinGeService.sendPushTags(params, model);
    }
    else {
      model.put("message", "请输入密码");
      return model;
    }
  }
  
  /**
   * 发送攻略2.0app标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sendWalkThroughAppTags", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sendWalkThroughAppTags(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    if(params.get("psw") != null && "lusizuiqiang".equals(params.get("psw"))) {
      if(pushTagService.existRunningThread()) {
        model.put("message", "other thread is running!");
        return model;
      }
      return multiThreadExcuteService.sendWalkThroughAppTags(params, model);
    }
    else {
      model.put("message", "请输入密码");
      return model;
    }
  }
  
  /**
   * 发送版本 渠道 标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sendCVTags", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sendCVTags(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    if(params.get("psw") != null && "lusizuiqiang".equals(params.get("psw"))) {
      if(pushTagService.existRunningThread()) {
        model.put("message", "other thread is running!");
        return model;
      }
      return multiThreadExcuteService.sendPushTagsChannel(params, model);
    }
    else {
      model.put("message", "请输入密码");
      return model;
    }
  }

  /**
   * 发送版本 渠道 标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sendCVTagsXG", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sendCVTagsXG(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    if(params.get("psw") != null && "lusizuiqiang".equals(params.get("psw"))) {
      if(pushTagService.existRunningThread()) {
        model.put("message", "other thread is running!");
        return model;
      }
      return multiThreadExcuteXinGeService.sendPushTagsChannel(params, model);
    }
    else {
      model.put("message", "请输入密码");
      return model;
    }
  }
  
  /**
   * 发送组合标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sendCombinationTags", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sendCombinationTags(@RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    return multiThreadExcuteService.sendCombinationTags(params, model);
  }

  /**
   * 查看百度标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/queryUserTag/{token}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap queryUserTag(@PathVariable("token")
  String token, @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      model = pushTagService.queryUserTag(token, model);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      // e.printStackTrace();
    }
    return model;
  }

  /**
   * 查看百度标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/deleteUserTag/{token}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap deleteUserTag(@PathVariable("token")
  String token, @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      model = pushTagService.deleteUserTag(token, model);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      // e.printStackTrace();
    }
    return model;
  }

  /**
   * 删除log日志
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/delLog/{name}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap delLog(@PathVariable("name")
  String name, @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      pushTagService.removePushTagLogByName(name);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      // e.printStackTrace();
    }
    return model;
  }

  /**
   * 查看百度标签
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/makeTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap makeTag(String token, @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    return multiThreadExcuteService.makeTag(model);
  }

  // /**
  // * 自动推送测试
  // *
  // * @param params
  // * @param psw
  // * @param request
  // * @param response
  // * @param model
  // * @return
  // */
  // @RequestMapping(value = "/autoPush", method = RequestMethod.POST)
  // @ResponseBody
  // public ModelMap autoPush(String token, @RequestBody
  // Map<String, Object> params, HttpServletRequest request, HttpServletResponse
  // response, ModelMap model) {
  // autoPushService.pushWalkthrough("", "3607988", "xxxxxxxxxxxxxxx");
  // return model;
  // }

  /**
   * 设置系统线程参数
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/sysThread/{sysThreadN}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sysThread(@PathVariable("sysThreadN")
  int sysThreadN, @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      pushTagService.setSysThreadNum(sysThreadN);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      e.printStackTrace();
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
    }
    return model;
  }

  /**
   * 设置173appID
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/set173APPId", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap set173APPId( @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      SystemConfig sc=new SystemConfig();
      sc.setKey("appId");
      sc.setValue(params.get("appId").toString());
      systemConfigService.updateSystemConfigByConfigValue(sc);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      e.printStackTrace();
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
    }
    return model;
  }
  
  /**
   * 设置系统线程参数
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/updateWaiting", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap updateWaiting( @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      pushTagService.updateWaiting();
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      e.printStackTrace();
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
    }
    return model;
  }
  
  /**
   * 自动推送测试
   * 
   * @param params
   * @param psw
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/autoPush", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap autoPush(String token, @RequestBody
  Map<String, Object> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
      autoPushService.autoPush("4034610", "94", "qq部落守卫战", COLLECTION_ITEM_TYPE.GIFT,null);
//    autoPushService.pushFeedBack(token, title, content)
//    autoPushService.autoPush("10898", "7", "英雄联盟", COLLECTION_ITEM_TYPE.WALKTHROUGH);
    try {
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      // e.printStackTrace();
    }
    return model;
  }

}
