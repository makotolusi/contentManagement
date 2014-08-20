package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogBestWalkthroughCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.cyou.video.mobile.server.cms.service.push.PushTagXinGeService;
import com.tencent.xinge.TagTokenPair;

/**
 * 意见反馈业务实现
 * 
 * @author jyz
 */
@Service("pushTagXinGeService")
public class PushTagXinGeServiceImpl implements PushTagXinGeService {

  private Logger logger = LoggerFactory.getLogger(PushTagXinGeServiceImpl.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  // @Autowired
  // private UserTokenService userTokenService;

  @Autowired
  private PushInterface xingePush;

  @Autowired
  PushTagXinGe173APPApi pushTagXinGe173APPApi;

  public List<ClientLogBestWalkthroughCollection> getBestWalkthroughInstalledGames(int s, int size, Query query,
      String name) {
    query.limit(size);
    query.skip(s);
    return mongoTemplate.find(query, ClientLogBestWalkthroughCollection.class, name);

  }

  @Override
  public int sendBestWalkThroughInstalledGameTags(int s, int end, Query query, String name) throws Exception {
    String threadName = String.valueOf(Thread.currentThread().getName());
    Map<String, UserItemOperatePvMongo> map = new HashMap<String, UserItemOperatePvMongo>();
    logger.info("<" + threadName + "> start is " + s + " end is " + end);
    long cha = end - s;
    // multi thread
    int size = 10000;
    if(size > end) {
      size = (int) cha;
    }
    int start = (int) s;
    List<ClientLogBestWalkthroughCollection> userSub = getBestWalkthroughInstalledGames(start, size, query, name);
    int i = 0;
    int total = 0;
    while(!userSub.isEmpty()) {
      int result = sendPushWalkThroughTagCoreOther(userSub, map);
      i++;
      start = start + size;
      if(start >= end) break;

      if(start + size > end) {
        size = (int) end - start;
      }
      userSub = getBestWalkthroughInstalledGames(start, size, query, name);
      // log
      if(i % 50 == 0 || i == cha) {
        logger.info(threadName + "  end(" + end + ") between start(" + s + ") number " + cha + " exucute number " + i);
      }
    }
    try {
      for(String appId : map.keySet()) {
        Push p = new Push();
        if(!StringUtils.isEmpty(appId)) {
          p.setAppId(Integer.parseInt(appId));
          UserItemOperatePvMongo u = map.get(appId);
          p.setClientType(CLIENT_TYPE.IOS);
          xingePush.setTagByXinge(u.getIosTags(), p);
          xingePush.delTagByXinge(u.getIosDelTags(), p);
          p.setClientType(CLIENT_TYPE.ANDROID);
          xingePush.setTagByXinge(u.getAndroidTags(), p);
          xingePush.delTagByXinge(u.getAndroidDelTags(), p);
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      logger.debug("xinge set tag exception may be could not find appKey by id");
    }
    return total;
  }

  private int sendPushWalkThroughTagCoreOther(List<ClientLogBestWalkthroughCollection> userTag,
      Map<String, UserItemOperatePvMongo> map) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    int setNum = 0;
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      inc(threadName, setNum);
      setNum++;
      try {
        ClientLogBestWalkthroughCollection uTag = (ClientLogBestWalkthroughCollection) iterator.next();
        if(uTag.getUid() == null) {// 没有token
          continue;
        }
        else {
          // set xinge id
          UserToken n = null;// userTokenService.getToken(uTag.getUid());
          if(n == null) {
            continue;
          }
          int plat = n.getPlat();
          String xingeToken = pushTagXinGe173APPApi.getXGToken(n);
          if(xingeToken == null) {// 没有baidu id
            continue;
          }
          else {
            app(uTag, xingeToken, plat, map);
          }
        }
      }
      catch(Exception e) {
        logger.error("set tag exception is " + e.getMessage());
      }
    }
    return setNum;
  }

  private void app(ClientLogBestWalkthroughCollection data, String xingeToken, int plat,
      Map<String, UserItemOperatePvMongo> map) throws Exception {
    TagTokenPair tag = new TagTokenPair(data.getServiceId(), xingeToken);
    UserItemOperatePvMongo u = null;
    if(map.containsKey(data.getAppId())) {
      u = map.get(data.getAppId());
    }
    else {
      u = new UserItemOperatePvMongo();
    }
    if(Integer.parseInt(data.getOperatorType()) == COLLECTION_OPERATOR_TYPE.INSTALL.index) {
      if(plat == 6)
        u.addIosTag(tag);
      else
        u.addAndroidTag(tag);
    }
    if(Integer.parseInt(data.getOperatorType()) == COLLECTION_OPERATOR_TYPE.UNINSTALL.index) {
      if(plat == 6)
        u.addIosDelTag(tag);
      else
        u.addAndroidDelTag(tag);
    }
    map.put(data.getAppId(), u);
  }

  private void inc(String threadName, int setNum) {
    int incc = 50;
    if(setNum != 0 && setNum % incc == 0) {
      mongoTemplate.findAndModify(new Query(new Criteria("threadName").is(threadName)),
          new Update().inc("threadNum", setNum), FindAndModifyOptions.options().upsert(true),
          PushTagExcuteStateInfo.class, "PushTagThreadInfo");
    }
  }
}
