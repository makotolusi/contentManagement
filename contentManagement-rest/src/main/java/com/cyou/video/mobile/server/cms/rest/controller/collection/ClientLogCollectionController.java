package com.cyou.video.mobile.server.cms.rest.controller.collection;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.GAME_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogBestWalkthroughCollection;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.rest.common.PrivateUtil;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;

/**
 * 数据收集 and pv
 * 
 * @author lusi
 * 
 */
@Controller
@RequestMapping("/rest/collection")
public class ClientLogCollectionController {

  private Logger LOGGER = LoggerFactory.getLogger(ClientLogCollectionController.class);

  @Autowired
  private ClientLogCollectionService clientLogCollectionService;

  @Autowired
  private PushTagService pushTagService;

  /**
   * 数据收集接口直接存入mongo
   * 
   * @param params
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/collectLogInfo", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap collectLogInfo(@RequestBody
  Map<String, ArrayList<LinkedHashMap<String, String>>> params, HttpServletRequest request,
      HttpServletResponse response, ModelMap model) {
    if(params == null) {
      LOGGER.error("params is null ");
      response.setStatus(HttpServletResponse.SC_OK);
      return model;
    }
    String token = PrivateUtil.decryptToken(request);
    if(StringUtils.isEmpty(token)) {
      LOGGER.info("token is null bind baidu id failed!!!!! " + token);
      response.setStatus(HttpServletResponse.SC_OK);
      return model;
    }
    else {
      List<ClientLogCollection> result = new ArrayList<ClientLogCollection>();
      // 为了 set user token
      ArrayList<LinkedHashMap<String, String>> clientLogCollections = params.get("logs");
      for(int i = 0; i < clientLogCollections.size(); i++) {
        try {
          ClientLogCollection collection = new ClientLogCollection();
          LinkedHashMap<String, String> obj = clientLogCollections.get(i);
          collection.setServiceName(obj.get("serviceName"));
          if(obj.get("gameCode") == null) continue;
          collection.setGameCode(obj.get("gameCode"));
          collection.setGamePlatForm(getIntegerData(obj.get("gamePlatForm")));
          if(!"".equals(obj.get("gameCode"))) {
            GAME_PLATFORM_TYPE p = null;
            if(collection.getGamePlatForm() != -1) {
              p = GAME_PLATFORM_TYPE.values()[collection.getGamePlatForm() - 1];
            }
            Map<String, String> typeSt = pushTagService.getGameCodeTypeAndStatus(collection.getGameCode(), p);
            if(typeSt != null) {
              if(typeSt.containsKey("platForm")) {
                collection.setGamePlatForm(getIntegerData(typeSt.get("platForm")));
              }
              collection.setGameType(typeSt.get("type"));
              collection.setGameStatus(typeSt.get("status"));
              if(StringUtils.isEmpty(obj.get("serviceName"))) collection.setServiceName(typeSt.get("name"));
            }
          }
          collection.setServiceId(obj.get("serviceId") == null ? "" : obj.get("serviceId"));
          collection.setOtherWay(getIntegerData(obj.get("otherWay")));
          collection.setUid(token);
          collection.setItemType(obj.get("itemType") == null ? "" : obj.get("itemType"));
          collection.setOperatorType(obj.get("operatorType") == null ? "" : obj.get("operatorType"));
          collection.setKeyWord(obj.get("keyWord") == null ? "" : obj.get("keyWord").toString());
          collection.setKeyWord2(obj.get("keyWord2") == null ? "" : obj.get("keyWord2").toString());
          if(obj.get("operatorDate") != null) {
            Date date = new Date(Long.parseLong(obj.get("operatorDate")));
            collection.setOperatorDate(date);
          }

          result.add(collection);
        }
        catch(Exception e) {
          // e.printStackTrace();
          LOGGER.error("ClientLogCollection exception is " + e.getMessage());
        }
      }
      try {
        clientLogCollectionService.collectLogInfo(result);
        response.setStatus(HttpServletResponse.SC_OK);
      }
      catch(Exception e) {
        // e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_OK);
        LOGGER.error("collectLogInfo exception is " + e.getMessage());
      }
    }
    return model;
  }

  private Integer getIntegerData(String key) {
    return key == null || "".equals(key) ? -1 : Integer.parseInt(key);
  }

  /**
   * 收集游戏
   * 
   * @param params
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/collectGameInfo", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap collectGameInfo(@RequestBody
  Map<String, ArrayList<LinkedHashMap<String, String>>> params, HttpServletRequest request,
      HttpServletResponse response, ModelMap model) {
    List<ClientLogCollection> result = toClientLogCollection(params, request, response);
    if(result == null) {
      LOGGER.error("params is null ");
      response.setStatus(HttpServletResponse.SC_OK);
      return model;
    }
    try {
      if(!result.isEmpty()) clientLogCollectionService.collectLogInfo(result);
      {
        response.setStatus(HttpServletResponse.SC_OK);
      }
    }
    catch(Exception e) {
      response.setStatus(HttpServletResponse.SC_OK);
      LOGGER.error("collectLogInfo exception is " + e.getMessage());
    }
    return model;
  }

  /**
   * 收集最强攻略游戏
   * 
   * @param params
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/walkThroughApp/collectGameInfo", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap collectWalkThroughApp(@RequestBody
  Map<String, ArrayList<LinkedHashMap<String, String>>> params, HttpServletRequest request,
      HttpServletResponse response, ModelMap model) {
    String appId = params.get("appId") + "";
    List<ClientLogCollection> result = toClientLogCollection(params, request, response);
    if(result == null || result.isEmpty()) {
      LOGGER.error("result is empty ");
      response.setStatus(HttpServletResponse.SC_OK);
      return model;
    }
    List<ClientLogBestWalkthroughCollection> wta = new ArrayList<ClientLogBestWalkthroughCollection>();
    for(Iterator iterator = result.iterator(); iterator.hasNext();) {
      ClientLogCollection c = (ClientLogCollection) iterator.next();
      ClientLogBestWalkthroughCollection w = new ClientLogBestWalkthroughCollection();
      // if(c.getUid().indexOf("_") >= 0) {
      // w.setUid(c.getUid().split("_")[0]);
      // }
      // else {
      w.setUid(c.getUid());
      // }
      w.setServiceId(c.getServiceId());
      w.setServiceName(c.getServiceName());
      w.setGameCode(c.getGameCode());
      w.setGameStatus(c.getGameStatus());
      w.setGameType(c.getGameType());
      w.setGamePlatForm(c.getGamePlatForm());
      w.setItemType(c.getItemType());
      w.setOperatorType(c.getOperatorType());
      w.setOperatorDate(c.getOperatorDate());
      w.setUploadDate(c.getUploadDate());
      w.setAppId(appId);
      wta.add(w);
    }
    try {
      if(!result.isEmpty()) {
        clientLogCollectionService.collectLogInfo(wta);
        response.setStatus(HttpServletResponse.SC_OK);
      }
    }
    catch(Exception e) {
      response.setStatus(HttpServletResponse.SC_OK);
      LOGGER.error("collectLogInfo exception is " + e.getMessage());
    }
    return model;
  }

  public List<ClientLogCollection> toClientLogCollection(Map<String, ArrayList<LinkedHashMap<String, String>>> params,
      HttpServletRequest request, HttpServletResponse response) {
    if(params == null) {
      LOGGER.error("params is null ");
      response.setStatus(HttpServletResponse.SC_OK);
      return null;
    }
    LOGGER.debug("collectLogInfo ========== " + params);
    String token = PrivateUtil.decryptToken(request);
    if(StringUtils.isEmpty(token)) {
      LOGGER.info("token is null bind baidu id failed!!!!! " + token);
      response.setStatus(HttpServletResponse.SC_OK);
      return null;
    }
    else {
      List<ClientLogCollection> result = new ArrayList<ClientLogCollection>();
      // 为了 set user token
      ArrayList<LinkedHashMap<String, String>> clientLogCollections = params.get("logs");
      for(int i = 0; i < clientLogCollections.size(); i++) {
        try {
          LinkedHashMap<String, String> obj = clientLogCollections.get(i);
          String pkg = obj.get("package");
          if(!StringUtils.isEmpty(pkg)) {
            if(obj.get("operatorType") != null && !"".equals(obj.get("operatorType"))) {
              COLLECTION_OPERATOR_TYPE opType = COLLECTION_OPERATOR_TYPE.values()[Integer.parseInt(obj
                  .get("operatorType"))];
              List<ClientLogCollection> games = clientLogCollectionService.gameAppFilter(pkg, opType, token);
              result.addAll(games);
            }
          }
        }
        catch(Exception e) {
          LOGGER.error("ClientLogCollection exception is " + e.getMessage());
          e.printStackTrace();
          return null;
        }
      }
      return result;
    }
  }

  /**
   * 绑定百度id
   * 
   * @param params
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/bindUserToken/xinge", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap bindUserId(@RequestBody
  Map<String, String> params, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    String token = PrivateUtil.decryptToken(request);
    params.put("token-self", token);
    try {
      clientLogCollectionService.bindUserId(params);
      response.setStatus(HttpServletResponse.SC_OK);
    }
    catch(Exception e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      LOGGER.error(" bind baidu id exception !!!!! " + e.getMessage());
    }
    return model;

  }

}
