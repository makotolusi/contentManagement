package com.cyou.video.mobile.server.cms.web.controller.push;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.pattern.IntegerPatternConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.CONTENT_SOURCE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo2;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.push.PushTagCombination;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.collection.MultiThreadExcuteService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 
 * 推送一些手动设置项 只有管理员可以看到的页面
 * 
 * @author LUSI
 */
@Controller
@RequestMapping("/web/activity/tagmanager")
public class TagManagerController {

  private Logger logger = LoggerFactory.getLogger(TagManagerController.class);

//  @Autowired
//  private ChannelService channelService;

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @Autowired
  PushTagService pushTagService;

  @Autowired
  ThreadPoolTaskExecutor taskExecutor;

  @Autowired
  MultiThreadExcuteService multiThreadExcuteService;

//  @Autowired
//  private AppVersionService appVersionService;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView pushListPage(ModelMap model) {
    model.put("itemType", COLLECTION_ITEM_TYPE.values());
    model.put("operatorType", COLLECTION_OPERATOR_TYPE.values());
    int to = pushTagService.getThreadTotal();
    model.put("threadTotal", to);
    List<PushTagExcuteStateInfo> l = pushTagService.getThreadNumList();
    int total = 0;
    for(Iterator iterator = l.iterator(); iterator.hasNext();) {
      PushTagExcuteStateInfo pushTagExcuteStateInfo = (PushTagExcuteStateInfo) iterator.next();
      total += pushTagExcuteStateInfo.getThreadNum();
    }
    if(total > to) total = to;
    model.put("threadNumList", total);
    return new ModelAndView("/activity/tagmanager", "type", model);

  }

  @RequestMapping(value = "/makePushTagCombination", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap makePushTagCombination(@RequestBody
  PushTagCombination params, ModelMap model) {
    try {

      if(pushTagService.existPushTagCombination(params)) {
        model.addAttribute("message", "组合标签重名了！！");
        return model;
      }
      if(pushTagService.makePushTagCombination(params))
        model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
      else
        model.addAttribute("message", "insert failed!!");
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/findPushTagCombination", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap findPushTagCombination(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {

      Pagination pagination = pushTagService.findPushTagCombination(params);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
      model.addAttribute("page", pagination);

      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/countPushTagCombination", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap countPushTagCombination(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      String name = params.get("name").toString();
      model.addAttribute("size", multiThreadExcuteService.countCombinationTags(name));
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: countPushTagCombination()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", "数据异常" + e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/sentSetPushTagCombination", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap sentSetPushTagCombination(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      if(pushTagService.existRunningThread())
        model.addAttribute("message", "有其它标签线程正在运行！");
      else
        multiThreadExcuteService.sendCombinationTags(params, model);

    }
    catch(Exception e) {
      logger.error("[method: countPushTagCombination()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", "数据异常" + e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listChannelTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listChannelTag(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      if(params.get("channelName") == null) params.put("channelName", params.get("serviceName"));
//      Pagination pagination = channelService.listChannel(params);
//      List<PushTagCollection> pushs = new ArrayList<PushTagCollection>();
//      for(Iterator iterator = pagination.getContent().iterator(); iterator.hasNext();) {
//        Channel c = (Channel) iterator.next();
//        PushTagCollection p = new PushTagCollection();
//        p.setTagName(c.getChannelName());
//        p.setTagId(c.getChannelId());
//        pushs.add(p);
//      }
//      pagination.setContent(pushs);
//      model.addAttribute("page", pagination);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listVersionTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listVersion(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      String versionName = "";
      if(params.get("serviceName") == null)
        versionName = params.get("versionName") == null ? null : params.get("versionName").toString();
      else
        versionName = params.get("serviceName").toString();
      int curPage = Integer.parseInt(params.get("curPage").toString());
      int pageSize = Pagination.PAGESIZE;
//      if(params.get("pageSize") != null) pageSize = Integer.parseInt(params.get("pageSize").toString());
//      Pagination pagination = appVersionService.listAppVersionByName(curPage, pageSize, versionName);
//
//      List<PushTagCollection> pushs = new ArrayList<PushTagCollection>();
//      for(Iterator iterator = pagination.getContent().iterator(); iterator.hasNext();) {
//        AppVersion c = (AppVersion) iterator.next();
//        PushTagCollection p = new PushTagCollection();
//        // if(c.getPlat()==6)
//        // p.setTagName("IOS_"+c.getVersion());
//        // else
//        // p.setTagName("ANDROID_"+c.getVersion());
//        p.setTagName(c.getVersion());
//        p.setTagId(c.getVersion());
//        pushs.add(p);
//      }
//      pagination.setContent(pushs);
//      model.addAttribute("page", pagination);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listVersion()] Get application version list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
    }
    return model;
  }

  @RequestMapping(value = "/listJiongTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listJiongTag(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.getJiong());
      model.addAttribute("page", pagination);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listJiongTag()] Get list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
    }
    return model;
  }

  @RequestMapping(value = "/listGameCateTag/{code}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listGameCateTag(@PathVariable("code")
  int code, ModelMap model) {

    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.getGameCategory(code));
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listRankTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listRankTag(ModelMap model) {

    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.getRankTag());
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listGamePlatFormTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listGamePlatFormTag(ModelMap model) {

    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.getGamePlatFormTag());
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listGameTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listGameTag(@RequestBody
  Map<String, Object> params, ModelMap model) {

    try {

      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.listGameTag(
          params.get("serviceName") == null ? null : params.get("serviceName").toString(),
          Integer.parseInt(params.get("curPage").toString()), Integer.parseInt(params.get("pageSize").toString()),
          COLLECTION_ITEM_TYPE.WALKTHROUGH));
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listLiveTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listLiveTag(@RequestBody
  Map<String, Object> params, ModelMap model) {

    try {

      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.listGameTag(
          params.get("serviceName") == null ? null : params.get("serviceName").toString(),
          Integer.parseInt(params.get("curPage").toString()), Pagination.PAGESIZE, COLLECTION_ITEM_TYPE.LIVE));
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listGiftTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listGiftTag(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.listGameTag(
          params.get("serviceName") == null ? null : params.get("serviceName").toString(),
          Integer.parseInt(params.get("curPage").toString()), Pagination.PAGESIZE, COLLECTION_ITEM_TYPE.GIFT));
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listActivityTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listActivityTag(@RequestBody
  Map<String, Object> params, ModelMap model) {

    try {
      params.put("itemType", "21");
      params.put("collectionName", Consts.COLLECTION_ITEM_PV_NAME);
      List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
      Pagination pagination = clientLogCollectionService.getPVByName(params);
      List<UserItemOperatePvMongo2> list = (List<UserItemOperatePvMongo2>) pagination.getContent();
      PushTagCollection p1 = new PushTagCollection();
      p1.setTagId(COLLECTION_ITEM_TYPE.ACT_CENTER.name());
      p1.setTagName("活动用户(所有参加过活动的用户)");
      tags.add(p1);
      for(Iterator iterator = list.iterator(); iterator.hasNext();) {
        UserItemOperatePvMongo2 u = (UserItemOperatePvMongo2) iterator.next();
        PushTagCollection p = new PushTagCollection();
        p.setTagId(u.getValue().getServiceName());
        p.setTagName(u.getValue().getServiceName());
        tags.add(p);
      }
      pagination.setContent(tags);
      model.addAttribute("total", clientLogCollectionService.getTotalNum(Consts.COLLECTION_ITEM_PV_NAME));
      model.addAttribute("page", pagination);

      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listContent/{source}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listContent(@RequestBody
  Map<String, Object> params, @PathVariable("source")
  CONTENT_SOURCE source, ModelMap model) {
    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.listContent(params.get("name") == null ? null : params.get("name")
          .toString(), 1, Pagination.PAGESIZE, COLLECTION_ITEM_TYPE.values()[Integer.parseInt(params.get("type")
          .toString())], source));
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listAppTag", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listAppTag(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      Pagination pagination = new Pagination();
      pagination.setContent(pushTagService.listAppTag(params));
      model.put("page", pagination);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      model.put("message", Constants.CUSTOM_ERROR_CODE.FAILED.toString());
      e.printStackTrace();
    }
    return model;
  }

}
