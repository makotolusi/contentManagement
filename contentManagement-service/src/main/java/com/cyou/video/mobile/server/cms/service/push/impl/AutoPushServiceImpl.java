package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_JOB_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_USER_SCOPE;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.model.push.PushApp;
import com.cyou.video.mobile.server.cms.model.push.PushAuto;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.model.user.UserTokenBindBaidu;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.cms.service.push.AutoPushService;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.cyou.video.mobile.server.cms.service.push.PushService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;

/**
 * 自动推送
 * 
 * @author lusi
 */
@Service("autoPushServiceImpl")
public class AutoPushServiceImpl implements AutoPushService {

  private Logger logger = LoggerFactory.getLogger(AutoPushServiceImpl.class);

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @Autowired
  PushService pushService;


  @Autowired
  PushInterface baiduPush;

  @Autowired
  XinGePush xingePush;

  @Autowired
  private MongoOperations mongoTemplate;

  @Autowired
  PushTagService pushTagService;

  @Autowired
  AppSelectService appSelectService;

  @Autowired
  PushTagXinGe173APPApi pushTagXinGe173APPApi;

  @Override
  public boolean autoPush(String gameCode, String id, String title, COLLECTION_ITEM_TYPE itemType, CLIENT_TYPE ct) {
    try {
      Push pushAuto = this.getAutoPushByType(itemType);
      PUSH_JOB_STATE state = pushAuto.getJobState();
      if(state == PUSH_JOB_STATE.DISABLE) return false;// 停用
      if(isCreatedWalkThrough(id, itemType)) // 该攻略已经启用过
        return false;
      Push push = new Push();
      CLIENT_TYPE[] ctype = null;
      if(ct == null) {
        if(pushAuto.getClientType() == CLIENT_TYPE.ALL)
          ctype = new CLIENT_TYPE[]{CLIENT_TYPE.ANDROID, CLIENT_TYPE.IOS};
        else
          ctype = new CLIENT_TYPE[]{pushAuto.getClientType()};
      }
      else {
        if(ct == CLIENT_TYPE.ALL)
          ctype = new CLIENT_TYPE[]{CLIENT_TYPE.ANDROID, CLIENT_TYPE.IOS};
        else
          ctype = new CLIENT_TYPE[]{ct};
      }
      push.setSendState(PUSH_SEND_STATE.FAIL);
      String gameName = null;
      Map<String, String> typeSt = pushTagService.getGameCodeTypeAndStatus(gameCode, null);
      if(typeSt != null && !StringUtils.isEmpty(typeSt.get("name"))) {
        gameName = typeSt.get("name");
        push.setTitle(pushAuto.getTitle().replaceAll("#gameName#", typeSt.get("name")));
        push.setContent(pushAuto.getContent().replaceAll("#gameName#", typeSt.get("name")));
      }
      else {
        push.setTitle(pushAuto.getTitle().replaceAll("#gameName#", ""));
        push.setContent(pushAuto.getContent().replaceAll("#gameName#", ""));
      }
      if(StringUtils.isEmpty(gameName)) {
        gameName = title;
      }
      switch(itemType) {
        case WALKTHROUGH :
          setTag(gameCode, gameName, push, pushAuto);
          break;
        case LIVE :
          setTag(gameCode + "_" + COLLECTION_ITEM_TYPE.LIVE.index, gameName, push, pushAuto);
          break;
        case GIFT :
          setTag(gameCode + "_" + COLLECTION_ITEM_TYPE.GIFT.index, gameName, push, pushAuto);
          break;
        case TOOL :
          setTag(gameCode, gameName, push, pushAuto);
          break;
        default :
          break;
      }
      push.setUserScope(PUSH_USER_SCOPE.TAG);
      Map<String, String> keyValue = new HashMap<String, String>();
      keyValue.put("i", id);
      keyValue.put("t", title);
      if(itemType == COLLECTION_ITEM_TYPE.TOOL)
        keyValue.put("p", COLLECTION_ITEM_TYPE.WALKTHROUGH.index + "");
      else
        keyValue.put("p", itemType.index + "");
      keyValue.put("s", "999");
      push.setKeyValue(keyValue);
      push.setContentType(itemType);
      push.setPlatForm(PUSH_PLATFORM_TYPE.BAIDU);
      push.setAppId(pushAuto.getAppId());
      push.setPushType(PUSH_TYPE.AUTO_HISTORY);
      for(int i = 0; i < ctype.length; i++) {
        push.setId(null);
        push.setClientType(ctype[i]);
        String pushId = pushService.createPush(push);
        if(pushId == null) {
          logger.error("insert push object failed!!");
        }
        else {
          push = baiduPush.pushTag(push);
          if(push.getSendState() == PUSH_SEND_STATE.FAIL)
            logger.error("auto push failed!!");
          else
            pushService.updateSendStateById(push);
        }

      }

    }
    catch(Exception e) {
      e.printStackTrace();
      logger.error("auto push exception is {}", e.getMessage());
      return false;
    }
    return true;
  }

  private boolean isCreatedWalkThrough(String id, COLLECTION_ITEM_TYPE itemType) {
    if(itemType == COLLECTION_ITEM_TYPE.WALKTHROUGH)// 新建攻略只能推送一次
    {
      Query q = new Query();
      q.addCriteria(new Criteria("contentType").is(COLLECTION_ITEM_TYPE.WALKTHROUGH.name()).and("keyValue.i").is(id));
      List<Push> l = mongoTemplate.find(q, Push.class);
      if(l == null || l.isEmpty())
        return false;
      else
        return true;
    }
    return false;
  }

  private void setTag(String gameCode, String gameName, Push push, Push pushAuto) {
    List<PushTagCollection> tags = pushAuto.getTags();
    PushTagCollection tag = new PushTagCollection();
    tag.setTagId(gameCode);
    tag.setTagName(gameName);
    tags.add(tag);
    push.setTags(tags);
  }

  @Override
  public boolean pushFeedBack(String token, String title, String content, COLLECTION_ITEM_TYPE type, String appId)
      throws Exception {
    Push push = new Push();
    PushApp app = appSelectService.getAppById(Integer.parseInt(appId));
    Push pushAuto = this.getAutoPushByType(type);
    PUSH_JOB_STATE state = pushAuto.getJobState();
    if(state == PUSH_JOB_STATE.DISABLE) return false;// 停用
    UserToken n =null;// userTokenService.getToken(token);
    String pushToke = "";
    if(n == null) {
      return false;
    }
    if(app.getPushPlatForm() == PUSH_PLATFORM_TYPE.BAIDU) {
      List<UserTokenBindBaidu> baiduId =null;// userTokenService.getBaidu2TokenList(n.getId());
      if(baiduId == null || baiduId.isEmpty()) {
        return false;
      }
      else {
        UserTokenBindBaidu baidu = baiduId.get(0);
        push.setChannelId(baidu.getBaiduChannelId());
        push.setUserId(baidu.getBaiduUserId());
      }
    }
    else if(app.getPushPlatForm() == PUSH_PLATFORM_TYPE.XINGE) {
      String xingeToken = pushTagXinGe173APPApi.getXGToken(n);
      if(xingeToken == null) {// 没有xinge id
        return false;
      }
      push.setUserId(xingeToken);
    }
    if(n.getPlat() == 6)
      push.setClientType(CLIENT_TYPE.IOS);
    else
      push.setClientType(CLIENT_TYPE.ANDROID);
    push.setContentType(type);
    push.setAppId(Integer.parseInt(appId));
    push.setPushType(PUSH_TYPE.AUTO_HISTORY);
    String reply = pushAuto.getContent().replaceAll("#feedback#", content);
    push.setTitle(reply);
    push.setContent(reply);
    push.setPlatForm(pushAuto.getPlatForm());
    Map<String, String> keyValue = new HashMap<String, String>();
    keyValue.put("i", "");
    keyValue.put("t", "title");
    keyValue.put("p", type.index + "");
    keyValue.put("s", "999");
    push.setKeyValue(keyValue);
    push.setSendState(PUSH_SEND_STATE.FAIL);
    String pushId = pushService.createPush(push);
    if(pushId == null) {
      logger.error("insert push object failed!!");
    }
    else {
      if(app.getPushPlatForm() == PUSH_PLATFORM_TYPE.BAIDU)
        push = baiduPush.pushOne(push);
      else if(app.getPushPlatForm() == PUSH_PLATFORM_TYPE.XINGE) push = xingePush.pushOne(push);
      if(push.getSendState() == PUSH_SEND_STATE.FAIL)
        logger.error("auto push failed!!");
      else
        pushService.updateSendStateById(push);
    }

    return true;

  }

  @Override
  public List<PushAuto> listAutoPush() {
    return mongoTemplate.findAll(PushAuto.class);
  }

  // @Override
  public Push getAutoPushByType(COLLECTION_ITEM_TYPE itemType) throws Exception {
    Query query = new Query();
    query.addCriteria(new Criteria("pushType").is(PUSH_TYPE.AUTO).and("contentType").is(itemType));
    return mongoTemplate.findOne(query, Push.class);
  }

}
