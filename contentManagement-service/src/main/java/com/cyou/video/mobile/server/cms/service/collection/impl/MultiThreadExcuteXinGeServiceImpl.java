package com.cyou.video.mobile.server.cms.service.collection.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_PUSH_TAG_JOB_NAME;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_TAG_STATE;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.sys.ConfigApps;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
import com.cyou.video.mobile.server.cms.model.sys.ContentTypeActionAndTag;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.collection.MultiThreadExcuteXinGeService;
import com.cyou.video.mobile.server.cms.service.collection.TimeFlagService;
import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.cms.service.push.PushTagXinGe173APPService;
import com.cyou.video.mobile.server.cms.service.push.PushTagXinGeService;
import com.cyou.video.mobile.server.cms.service.sys.ConfigAppsService;
import com.cyou.video.mobile.server.cms.service.sys.ContentTypeService;
import com.cyou.video.mobile.server.cms.service.sys.SystemConfigService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 
 * @author lusi
 * 
 */
@Service("multiThreadExcuteXinGeService")
public class MultiThreadExcuteXinGeServiceImpl implements MultiThreadExcuteXinGeService {

  private Logger logger = LoggerFactory.getLogger(MultiThreadExcuteXinGeServiceImpl.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @Autowired
  PushTagXinGe173APPService pushTagXinGe173APPService;

  @Autowired
  PushTagXinGeService pushTagXinGeService;

  @Autowired
  ThreadPoolTaskExecutor taskExecutor;

  @Autowired
  TimeFlagService timeFlagService;

  @Autowired
  ConfigAppsService configAppsService;

  @Autowired
  AppSelectService appSelectService;

  @Autowired
  private SystemConfigService systemConfigService;

  @Autowired
  ContentTypeService contentTypeService; 
  /**
   * 发送tag
   */
  @Override
  public ModelMap sendPushTags(Map<String, Object> params, ModelMap model) {
    // 等其它线程执行完
    waiting();
    // 删除发送log
    delThreadNumList();
    smallTag(params, model);
    // 等其它线程执行完
    // waiting();
    // delThreadNumList();
    // gameCode(params, model);
    // waiting();
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }

  private void gameCode(Map<String, Object> params, ModelMap model) {
    try {
      Query queryOther = new Query();
      // 清空之前的记录
      removePushTagLogByName(Consts.COLLECTION_USER_GAME_PV);
      // 其它
      PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(queryOther,
          Consts.COLLECTION_USER_GAME_PV, "value.uploadDate");
      queryOther.addCriteria(Criteria.where("value.state").is(0));
      long total = clientLogCollectionService.getCount(queryOther, Consts.COLLECTION_USER_GAME_PV);
      // 线程总量
      setThreadTotal((int) total);
      params.put("query", queryOther);
      int threadNum = Integer.parseInt(systemConfigService.getByKey("sys_thread_num"));
      successLogStart(threadNum, Consts.COLLECTION_USER_GAME_PV, 0);
      multiTreadCore(threadNum, total, Consts.COLLECTION_USER_GAME_PV, COLLECTION_PUSH_TAG_JOB_NAME.USER_REDUCE_TAG,
          params);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void smallTag(Map<String, Object> params, ModelMap model) {
    try {
      String collection = Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME;
      // 清空之前的记录
      removePushTagLogByName(Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME);
      String appId = systemConfigService.getByKey("sys_173app_id");
      ConfigApps configApps = configAppsService.findByAppid(Integer.parseInt(appId));
      List<ContentType> tag = configApps.getTag();
//      Map<String,Map<String, ContentTypeActionAndTag>> tagConfig=new HashMap<String,Map<String, ContentTypeActionAndTag>>();
      Map<String, ContentTypeActionAndTag> action=new HashMap<String, ContentTypeActionAndTag>();
      List in = new ArrayList();
      for(Iterator iterator = tag.iterator(); iterator.hasNext();) {
        ContentType ct = (ContentType) iterator.next();
        String service=ct.getIndex();
        in.add(ct.getIndex());
        List<ContentTypeActionAndTag> actionAndTag=ct.getAction();
        for(Iterator iterator2 = actionAndTag.iterator(); iterator2.hasNext();) {
          String key=service+"_";
          ContentTypeActionAndTag contentTypeActionAndTag = (ContentTypeActionAndTag) iterator2.next();
          List<ContentType> act=contentTypeActionAndTag.getAction();
          contentTypeService.fullActionAndTag(act);
          for(Iterator iterator3 = act.iterator(); iterator3.hasNext();) {
            ContentType contentType = (ContentType) iterator3.next();
            key+=contentType.getIndex()+"_";
          }
          List<ContentType> tt=contentTypeActionAndTag.getTags();
          contentTypeService.fullActionAndTag(tt);
          action.put(key, contentTypeActionAndTag);
        }
      }
      params.put("action", action);
      // Object[] in = new Object[]{Consts.COLLECTION_ITEM_TYPE.APP.index + "",
      // Consts.COLLECTION_ITEM_TYPE.WALKTHROUGH.index + "",
      // Consts.COLLECTION_ITEM_TYPE.GIFT.index + "",
      // Consts.COLLECTION_ITEM_TYPE.VIDEO.index + "",
      // Consts.COLLECTION_ITEM_TYPE.LIVE.index + "",
      // Consts.COLLECTION_ITEM_TYPE.PIC.index + "",
      // Consts.COLLECTION_ITEM_TYPE.ACT_CENTER.index + ""};
      // 其它
      Query queryOther = new Query();
      queryOther.addCriteria(Criteria.where("value.itemType").in(in));
      queryOther.addCriteria(Criteria.where("value.state").is(0));
      PushTagLastUpdateTime oldLastUpdateTime = timeFlagService
          .setTimestamp(queryOther, collection, "value.uploadDate");
      long total = clientLogCollectionService.getCount(queryOther, collection);
      // 线程总量
      setThreadTotal((int) total);
      params.put("query", queryOther);
      int threadNum = Integer.parseInt(systemConfigService.getByKey("sys_thread_num"));
      successLogStart(threadNum, collection, 0);
      multiTreadCore(threadNum, total, collection, COLLECTION_PUSH_TAG_JOB_NAME.USER_REDUCE_TAG, params);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public ModelMap sendPushTagsChannel(Map<String, Object> params, ModelMap model) {
    try {
      waiting();
      // 删除发送log
      delThreadNumList();
      String c = COLLECTION_PUSH_TAG_JOB_NAME.USER_CHANNEL_TAG.name();
      // 清空之前的记录
      removePushTagLogByName(c);
      // 最新时间
      PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(null, c, "uploadDate");

      Date lmd = null;
      if(oldLastUpdateTime != null) {
        lmd = oldLastUpdateTime.getLastUpdateTime();
      }
      int total = 0;// userTokenDao.countTokenBaiduUidChannelInfoForXinGe(lmd);
      // 线程总量
      setThreadTotal((int) total);
      params.put("lastModifyDate", lmd);
      int threadNum = Integer.parseInt(systemConfigService.getByKey("sys_thread_num"));
      successLogStart(threadNum, c, 0);
      multiTreadCore(threadNum, total, c, COLLECTION_PUSH_TAG_JOB_NAME.USER_CHANNEL_TAG, params);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }

  /**
   * ---------------------------------temp
   * 临时方法--------------------------------------
   * 
   **/

  @Override
  public ModelMap pushHistoryToMongo(Map<String, Object> params, ModelMap model) {
    try {
      // List<PushOld> li = pushDao.listPush(params);
      // List<Push> pushs=new ArrayList<Push>();
      // for(Iterator iterator = li.iterator(); iterator.hasNext();) {
      // PushOld pushOld = (PushOld) iterator.next();
      // Push push = new Push();
      // push.setPlatForm(PUSH_PLATFORM_TYPE.BAIDU);
      // push.setTitle(pushOld.getTitle());
      // push.setContent(pushOld.getContent());
      // push.setClientTypes(pushOld.getClientTypes());
      // push.setClientType(pushOld.getClientType());
      // push.setCronExp(pushOld.getCronExp());
      // push.setCronExpression(pushOld.getCronExpression());
      // push.setUserScope(pushOld.getUserScope());
      // push.setSendState(pushOld.getSendState());
      // push.setSendDate(pushOld.getSendDate());
      // push.setSentLogs(pushOld.getSentLogs());
      // push.setPushType(pushOld.getPushType());
      // push.setJobState(pushOld.getJobState());
      // push.setSentLogs(pushOld.getSentLogs());
      // push.setAppId(Integer.parseInt(systemConfigService.getSystemConfigByConfigKey("sys_173app_id")));
      // String kv = "{" + pushOld.getKeyValue() + "}";
      // JSONObject obj = new JSONObject(kv);
      // Iterator keys = obj.keys();
      // Map map = new HashMap();
      // while(keys.hasNext()) {
      // String key = (String) keys.next();
      // String value = obj.get(key).toString();
      // if("URL".equals(key)){
      // push.setContentType(COLLECTION_ITEM_TYPE.URL);
      // }else if("p".equals(key)){
      // push.setContentType(COLLECTION_ITEM_TYPE.values()[Integer.parseInt(value)]);
      // }
      // map.put(key, value);
      // }
      // push.setKeyValue(map);
      // String tagstr=pushOld.getTags();
      // if(!StringUtils.isEmpty(tagstr)){
      // PushTagCollection p=new PushTagCollection();
      // p.setTagName(tagstr.split(":")[0]);
      // p.setTagId(tagstr.split(":")[1]);
      // List<PushTagCollection> tags=new ArrayList<PushTagCollection>();
      // tags.add(p);
      // push.setTags(tags);
      // }
      // pushs.add(push);
      // }
      // mongoTemplate.insertAll(pushs);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return model;
  }

  /**
   * ---------------------------------tool--------------------------------------
   * 
   **/

  public void waiting() {
    while(existRunningThread()) {
      logger.info("other thread is running ---------------------------------------");
      try {
        Thread.sleep(5000);
      }
      catch(InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void successLogStart(int threadNum, String c, long size) {
    PushTagExcuteStateInfo o = new PushTagExcuteStateInfo(c, new Date(), threadNum);
    o.setSize(size);
    o.setState(PUSH_SEND_TAG_STATE.RUNNING);
    this.savePushTagLog(o);
  }

  // public PushTagExcuteStateInfo getSysThreadNum() {
  // return mongoTemplate.findOne(new Query().addCriteria(new
  // Criteria("name").is("sysThreadNum")),
  // PushTagExcuteStateInfo.class, "PushTagThreadInfo");
  // }

  public void setThreadTotal(int total) {
    mongoTemplate.save(new PushTagExcuteStateInfo("threadTotal", null, total), "PushTagThreadInfo");
  }

  public void removePushTagLogByName(String name) {
    mongoTemplate.remove(new Query().addCriteria(new Criteria("name").is(name)), PushTagExcuteStateInfo.class);
  }

  public void savePushTagLog(Object o) {
    mongoTemplate.save(o);
  }

  public void delThreadNumList() {
    try {
      mongoTemplate.remove(new Query().addCriteria(new Criteria("name").ne("sysThreadNum")), "PushTagThreadInfo");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public boolean existRunningThread() {
    Query query = new Query();
    query.addCriteria(new Criteria().where("state").is(PUSH_SEND_TAG_STATE.RUNNING.name()));
    List l = mongoTemplate.find(query, PushTagExcuteStateInfo.class);
    if(l == null || l.isEmpty())
      return false;
    else
      return true;
  }

  /**
   * 多线程
   * 
   * @param model
   * @param query
   * @param oldLastUpdateTime
   * @param cname
   * @param pthread
   */
  private void multiTreadCore(int thr, long total, String collectionName, COLLECTION_PUSH_TAG_JOB_NAME type,
      Map<String, Object> params) throws Exception {
    // 多线程开始
    int threadN = thr;
    long size = total / threadN;
    long yu = total % threadN;
    long start = 0;
    long end = size;

    for(int i = 1; i <= threadN; i++) {
      if(i != 1) {
        start = end;
        if(i == threadN)
          end += size + yu;
        else
          end += size;
      }
      params.put("start", start);
      params.put("end", end);
      params.put("name", collectionName);
      PThread pThread = getThreadService(type, params);
      pThread.setName(collectionName);
      pThread.setStart(start);
      pThread.setEnd(end);
      taskExecutor.execute(pThread);
    }
  }

  private PThread getThreadService(COLLECTION_PUSH_TAG_JOB_NAME type, Map<String, Object> params) {
    params.put("jobType", type);
    PThread pThread = new PushTagXinGeThread(pushTagXinGe173APPService, params);
    return pThread;
  }

}
