package com.cyou.video.mobile.server.cms.rest.controller.collection;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.rest.common.PrivateUtil;
import com.cyou.video.mobile.server.cms.service.push.AutoPushService;
import com.cyou.video.mobile.server.cms.service.push.PushService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 数据收集 and pv
 * 
 * @author lusi
 * 
 */
@Controller
@RequestMapping("/rest/push")
public class PushAutoController {

  private Logger LOGGER = LoggerFactory.getLogger(PushAutoController.class);

  @Autowired
  AutoPushService autoPushServiceImpl;

  @Autowired
  PushTagService pushTagService;

  @Autowired
  PushService pushService;
  
  /**
   * 新建直播推送
   * 
   * @param id
   *          推送任务编号
   * @param model
   * @return
   */
  @RequestMapping(value = "/auto/live", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap autoLive(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      LOGGER.info("自动推送直播被调用");
      autoPushServiceImpl.autoPush(params.get("gameCode").toString(), params.get("roomId").toString(),
          params.get("title").toString(), COLLECTION_ITEM_TYPE.LIVE, null);
      model.addAttribute("message", "SUCCESS");
    }
    catch(Exception e) {
      LOGGER.debug("delete job failed " + e.getMessage());
      model.addAttribute("message", e.getMessage());
    }

    return model;
  }

  /**
   * 新建礼包推送
   * 
   * @param id
   *          推送任务编号
   * @param model
   * @return
   */
  @RequestMapping(value = "/auto/gift", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap autoGift(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      LOGGER.info("自动推送礼包被调用");
      autoPushServiceImpl.autoPush(params.get("gameCode").toString(), params.get("id").toString(), params.get("title")
          .toString(), COLLECTION_ITEM_TYPE.GIFT, null);
      model.addAttribute("message", "SUCCESS");
    }
    catch(Exception e) {
      LOGGER.debug("delete job failed " + e.getMessage());
      model.addAttribute("message", e.getMessage());
    }

    return model;
  }

  /**
   * 单播推送接口
   * 
   * @param id
   *          推送任务编号
   * @param model
   * @return
   */
  @RequestMapping(value = "/auto/pushOne", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap pushOne(@RequestBody
  Map<String, String> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    try {
      String token = params.get("token");
      if(StringUtils.isBlank(token)) {
        LOGGER.info("token is null bind baidu id failed!!!!! " + token);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return model;
      }
      else {
        LOGGER.info("单播推送调用");
        autoPushServiceImpl.pushFeedBack(token, params.get("reply"), params.get("reply"),
            COLLECTION_ITEM_TYPE.ENCY, params.get("appId"));
        model.addAttribute("message", "SUCCESS");
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      LOGGER.debug("delete job failed " + e.getMessage());
      model.addAttribute("message", e.getMessage());
    }

    return model;
  }

  /**
   * 游戏中心需要的推送列表
   * @param params
   * @param model
   * @return
   */
  @RequestMapping(value = "/listPush", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap list(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      Pagination pagination = pushService.listPush(params);
      model.addAttribute("page", pagination);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      LOGGER.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }

    return model;
  }
}
