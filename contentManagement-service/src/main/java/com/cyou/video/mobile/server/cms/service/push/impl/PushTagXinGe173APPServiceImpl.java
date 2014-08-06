package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_TAG_SETTING_STATE;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo2;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.push.PushTagCombination;
import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.service.push.PushTagXinGe173APPService;

/**
 * 意见反馈业务实现
 * 
 * @author jyz
 */
@Service("pushTagXinGe173APPService")
public class PushTagXinGe173APPServiceImpl implements PushTagXinGe173APPService {

  private Logger logger = LoggerFactory.getLogger(PushTagXinGe173APPServiceImpl.class);

  @Autowired
  PushTagXinGe173APPApi pushTagXinGe173APPApi;

  @Override
  public int sendPushTags(int s, int end, Query query, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    // tag的集合
    pushTagXinGe173APPApi.newTagResult();
    logger.info("<" + threadName + "> start is " + s + " end is " + end);
    pushTagXinGe173APPApi.initStartAndSize(s, end);
    List<UserItemOperatePvMongo2> userSub = pushTagXinGe173APPApi.getUserItemOperatePvMongo2(query, name);
    int total = 0;
    while(!userSub.isEmpty()) {
      int result = sendPushTagCoreOther(userSub, name);
      total += result;
      // multi thread
      if(pushTagXinGe173APPApi.continueStartAndSize(end) == false) break;
      userSub = pushTagXinGe173APPApi.getUserItemOperatePvMongo2(query, name);
    }
    try {
      pushTagXinGe173APPApi.batchTags();
    }
    catch(Exception e) {
      logger.error("信鸽批量tag时候出错 " + e.getMessage());
    }// 批量执行tag
    return total;
  }

  private int sendPushTagCoreOther(List<UserItemOperatePvMongo2> userTag, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    int setNum = 0;
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      pushTagXinGe173APPApi.inc(threadName, setNum);
      setNum++;
      try {
        UserItemOperatePvMongo2 uTag = (UserItemOperatePvMongo2) iterator.next();
        if(uTag.getId().getUid() == null) {// 没有token
          continue;
        }
        else {
          // id.setUid("49762622979690");
          // set 百度 id
          UserToken n = null;//userTokenService.getToken(uTag.getId().getUid());
          if(n == null) {
            continue;
          }
          String xingeToken = pushTagXinGe173APPApi.getXGToken(n);
          if(xingeToken == null) {// 没有xinge id
            continue;
          }
          else {
            uTag.getId().setPushToken(xingeToken);
            uTag.getValue().setPushToken(xingeToken);
          }
          pushTagXinGe173APPApi.initClientType(n);
          // 成功数
          List<Boolean> suc = new ArrayList<Boolean>();
          // client type
          if(Consts.COLLECTION_USER_GAME_PV.equals(name)) {
            suc.add(pushTagXinGe173APPApi.hits(uTag.getId(), uTag.getValue()));
          }
          else {
            suc.add(pushTagXinGe173APPApi.jiong(uTag.getId(), uTag.getValue()));

            pushTagXinGe173APPApi.news(uTag.getId(), uTag.getValue());

            suc.add(pushTagXinGe173APPApi.live(uTag.getId(), uTag.getValue()));

            suc.add(pushTagXinGe173APPApi.video(uTag.getId(), uTag.getValue()));

            suc.add(pushTagXinGe173APPApi.activity(uTag.getId(), uTag.getValue()));
            //
            suc.add(pushTagXinGe173APPApi.bySearch(uTag.getId(), uTag.getValue()));

            suc.add(pushTagXinGe173APPApi.byRank(uTag.getId(), uTag.getValue()));
            // 订阅
            pushTagXinGe173APPApi.subscribe(uTag.getId(), uTag.getValue());
            // 订阅
            pushTagXinGe173APPApi.top(uTag.getId(), uTag.getValue());

            suc.add(pushTagXinGe173APPApi.desktop(uTag.getId(), uTag.getValue()));
            // app
            pushTagXinGe173APPApi.app(uTag.getId(), uTag.getValue());
          }
          if(suc.contains(true))
            pushTagXinGe173APPApi.updateTagStateInMongoBatch(uTag.getId(), PUSH_TAG_SETTING_STATE.SUC.getIndex(), name);
        }
      }
      catch(Exception e) {
        logger.error("set tag exception is " + e.getMessage());
      }
    }
    return setNum;
  }

  @Override
  public int sendPushTagsChannel(int s, int end, Date lastModifyDate) throws Exception {
    String threadName = String.valueOf(Thread.currentThread().getName());
//    pushTagXinGe173APPApi.newTagResult();
//    logger.info("<" + threadName + "> start is " + s + " end is " + end);
//    pushTagXinGe173APPApi.initStartAndSize(s, end);
//    List<UserToken> userTag = userTokenDao.getTokenBaiduUidChannelInfoForXinGe(pushTagXinGe173APPApi.getStart(),
//        pushTagXinGe173APPApi.getSize(), lastModifyDate);
//    logger.info("userTag list is " + userTag);
//    int setNum = 0;
//    while(userTag != null && !userTag.isEmpty()) {
//      for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
//        try {
//          pushTagXinGe173APPApi.inc(threadName, setNum);
//          setNum++;
//          UserToken user = (UserToken) iterator.next();
//          String channel = user.getChannel();
//          String version = user.getCurUsedVersion();
//          String xingeToken = pushTagXinGe173APPApi.getXGToken(user);
//          if(xingeToken == null) {// 没有xinge id
//            continue;
//          }
//          pushTagXinGe173APPApi.initClientType(user);
//          if(!StringUtils.isEmpty(channel)) 
//            pushTagXinGe173APPApi.setTag(xingeToken, channel);
//          if(!StringUtils.isEmpty(version)) 
//            pushTagXinGe173APPApi.setTag(xingeToken, version);
//        }
//        catch(Exception e) {
//          logger.error("set tag exception is " + e.getMessage());
//          e.printStackTrace();
//        }
//      }
//      // multi thread
//      if(pushTagXinGe173APPApi.continueStartAndSize(end) == false) break;
//      userTag = userTokenService.getTokenBaiduUidChannelInfo(pushTagXinGe173APPApi.getStart(),
//          pushTagXinGe173APPApi.getSize(), lastModifyDate);
//    }
//    try {
//      pushTagXinGe173APPApi.batchTags();
//    }
//    catch(Exception e) {
//      logger.error("信鸽批量tag时候出错 " + e.getMessage());
//    }// 批量执行tag
    return 0;
  }

  @Override
  public int sendPushCombinationTags(int s, int end, Query query, String name, PushTagCombination combination) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    pushTagXinGe173APPApi.newTagResult();
    logger.info("<" + threadName + "> start is " + s + " end is " + end);
    pushTagXinGe173APPApi.initStartAndSize(s, end);
    List<UserItemOperatePvMongo2> userSub = pushTagXinGe173APPApi.getUserItemOperatePvMongo2(query, name);
    int totalSetNum = 0;
    // log
    int i = 0;
    while(!userSub.isEmpty()) {
      int setNum = sendPushCombinationTagCore(userSub, name, combination);
      totalSetNum += setNum;
      // multi thread
      if(pushTagXinGe173APPApi.continueStartAndSize(end) == false) break;
      userSub = pushTagXinGe173APPApi.getUserItemOperatePvMongo2(query, name);
      i++;
    }
    return totalSetNum;
  }
  

  private int sendPushCombinationTagCore(List<UserItemOperatePvMongo2> userTag, String name,
      PushTagCombination combination) {
    int setNum = 0;
    // log
    String threadName = String.valueOf(Thread.currentThread().getName());
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      pushTagXinGe173APPApi.inc(threadName, setNum);
      setNum++;
      try {
        UserItemOperatePvMongo2 uTag = (UserItemOperatePvMongo2) iterator.next();
        if(uTag.getId().getUid() == null)// 没有token
          continue;
        else {
          // token
          UserToken n = null;//userTokenService.getToken(uTag.getId().getUid());
          if(n == null) {
            continue;
          }
          String xingeToken = pushTagXinGe173APPApi.getXGToken(n);
          if(xingeToken == null) {// 没有xinge id
            continue;
          }
          else {
            uTag.getId().setPushToken(xingeToken);
            uTag.getValue().setPushToken(xingeToken);
          }
          // client type
          pushTagXinGe173APPApi.initClientType(n);
          // c and v
          boolean cjump = true;
          boolean vjump = true;
          if(n.getChannel() != null) {
            // 渠道
            if(combination.getCtags() == null || combination.getCtags().isEmpty()) {
              cjump = false;
            }
            else {
              for(Iterator iterator2 = combination.getCtags().iterator(); iterator2.hasNext();) {
                PushTagCollection pushTagCollection = (PushTagCollection) iterator2.next();

                String cid = pushTagCollection.getTagId().trim();
                if(cid.equals(n.getChannel().trim())) cjump = false;
              }
            }
          }
          // 版本
          if(n.getCurUsedVersion() != null && !"".equals(n.getCurUsedVersion()))
            if(combination.getVtags() == null || combination.getVtags().isEmpty()) {
              vjump = false;
            }
            else {
              for(Iterator iterator2 = combination.getVtags().iterator(); iterator2.hasNext();) {
                PushTagCollection pushTagCollection = (PushTagCollection) iterator2.next();
                String cid = pushTagCollection.getTagId().trim();
                // cid = cid.split("_")[1];
                if(cid.equals(n.getCurUsedVersion().trim())) vjump = false;
              }
            }
          if("OR".equals(combination.getCvtagsop())) {
            if(cjump == true && vjump == true) continue;
          }
          else if("AND".equals(combination.getCvtagsop())) {
            if(cjump == true || vjump == true) continue;
          }
          String tag = combination.getTagId();
          if(!StringUtils.isEmpty(tag)) {
            pushTagXinGe173APPApi.setTag(xingeToken, tag);
          }
        }
      }
      catch(Exception e) {
        logger.error("set tag exception is " + e.getMessage());
        e.printStackTrace();
      }
    }
    return setNum;
  }
  
  @Override
  public void successLogEnd(String c, int total) {
    pushTagXinGe173APPApi.successLogEnd(c, total);

  }
}
