package com.cyou.video.mobile.server.cms.service.collection.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_PUSH_TAG_JOB_NAME;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.push.PushTagCombination;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.collection.MultiThreadExcuteService;
import com.cyou.video.mobile.server.cms.service.collection.TimeFlagService;
import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;
import com.cyou.video.mobile.server.cms.service.push.PushTagXinGeService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 
 * @author lusi
 * 
 */
@Service("multiThreadExcuteService")
public class MultiThreadExcuteServiceImpl implements MultiThreadExcuteService {

  private Logger logger = LoggerFactory.getLogger(MultiThreadExcuteServiceImpl.class);

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @Autowired
  PushTagService pushTagService;

  @Autowired
  PushTagXinGeService pushTagXinGeService;
  
  @Autowired
  ThreadPoolTaskExecutor taskExecutor;

  @Autowired
  TimeFlagService timeFlagService;

//  @Autowired
//  private UserTokenService userTokenService;
  
  @Autowired
  AppSelectService appSelectService;

  @Override
  public void syncApp() {
    try {
      appSelectService.syncApp();
    }
    catch(Exception e) {
      logger.error("manual syncApp exception " + e.getMessage());
    }
  }
  
  // /**
  // * 发送tag
  // */
  @Override
  public ModelMap sendCombinationTags(Map<String, Object> params, ModelMap model) {
    waiting();
    pushTagService.delThreadNumList();// del log info
    String name = params.get("name").toString();
    PushTagCombination combination = pushTagService.findTagCombinationOne(name);
    pushTagService.removePushTagLogByName(combination.getTagName());
    Query queryCombination = makeQuery(combination);
    combinationTag(params, model, queryCombination, combination);
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }

  @Override
  public long countCombinationTags(String name) {

    PushTagCombination combination = pushTagService.findTagCombinationOne(name);
    Query queryCombination = makeQuery(combination);
    return clientLogCollectionService.getCount(queryCombination, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME);
  }

  private Query makeQuery(PushTagCombination combination) {
    Query queryCombination = new Query();
    List<Criteria> cri = new ArrayList<Criteria>();
    String tagop = combination.getTagsop();
    for(Iterator iterator = combination.getTags().iterator(); iterator.hasNext();) {
      PushTagCollection tag = (PushTagCollection) iterator.next();
      if(tag.getTagType() >= 100) {
        if(tag.getTagType() == 1001 || tag.getTagType() == 1003) {// 类型
          Pattern pattern = Pattern.compile("^.*" + tag.getTagId() + ".*$");
          cri.add(new Criteria().where("value.gameStatus").regex(pattern));
        }
        else if(tag.getTagType() == 1002 || tag.getTagType() == 1004) {// 类型
          Pattern pattern = Pattern.compile("^.*" + tag.getTagId() + ".*$");
          cri.add(new Criteria().where("value.gameType").regex(pattern));
        }
        else if(tag.getTagType() == 101) {// 行为
          String[] t = tag.getTagId().split("_");
          cri.add(new Criteria().where("_id.serviceId").is(t[0]).and("value.itemType").is(t[1]).and("_id.operatorType")
              .is(t[2]));
        }
        else if(tag.getTagType() == 102)// 游戏平台
          cri.add(new Criteria().where("value.gamePlatForm").is(Integer.parseInt(tag.getTagId().split("_")[0])));
        else if(tag.getTagType() == 100)// 游戏平台
          cri.add(new Criteria().where("value.gameCode").is(tag.getTagId().split("_")[0]));
        else if(tag.getTagType() == 103) { // 活动
          if(COLLECTION_ITEM_TYPE.ACT_CENTER.name().equals(tag.getTagId())) {// 活动用户
            cri.add(new Criteria().where("value.itemType").is(COLLECTION_ITEM_TYPE.ACT_CENTER.index + ""));
          }
          else {
            cri.add(new Criteria().where("value.serviceName").is(tag.getTagId().split("_")[0]));
          }
        }
      }
      else {// jong rank
        if(tag.getTagType() == COLLECTION_ITEM_TYPE.JIONG.index || tag.getTagType() == COLLECTION_ITEM_TYPE.RANK.index)
          cri.add(new Criteria().where("_id.otherWay").is(COLLECTION_ITEM_TYPE.values()[tag.getTagType()].index)
              .and("value.keyWord").is(tag.getTagId().split("_")[0]));
      }
    }
    Criteria[] o = new Criteria[]{};
    o = cri.toArray(o);
    if(o.length != 0) {
      if("OR".equals(tagop)) queryCombination.addCriteria(new Criteria().orOperator(o));
      if("AND".equals(tagop)) queryCombination.addCriteria(new Criteria().andOperator(o));
    }
    return queryCombination;
  }

  /**
   * 发送tag
   */
  @Override
  public ModelMap sendPushTags(Map<String, Object> params, ModelMap model) {
//    //等mr执行完
//    waitingMapReduce();
    // 等其它线程执行完
    waiting();
    pushTagService.delThreadNumList();
    smallTag(params, model);
    // 等其它线程执行完
    waiting();
    pushTagService.delThreadNumList();
    gameCode(params, model);
    // waiting();
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }

  /**
   * 发送tag
   */
  @Override
  public ModelMap sendWalkThroughAppTags(Map<String, Object> params, ModelMap model) {
    waiting();
    pushTagService.delThreadNumList();
    walkThroughAppTag(params, model);
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }
  
  public void waiting() {
    while(pushTagService.existRunningThread()) {
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

  public void waitingMapReduce() {
    while(pushTagService.isRunningMapReduce()) {
      logger.info("map reduce thread is running ---------------------------------------");
      try {
        Thread.sleep(5000);
      }
      catch(InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  private void favorite(Map<String, Object> params, ModelMap model) {
    Query queryFaorite = new Query();
    queryFaorite.addCriteria(new Criteria().orOperator(
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.FAVORITE.index + ""),
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.CANCEL_FAVORITE.index + "")));
    int threadNum = Integer.parseInt(params.get("threadNum").toString());
    sendMergeTag(model, threadNum, Consts.COLLECTION_USER_FAVORITE, queryFaorite);
  }

  private void top(Map<String, Object> params, ModelMap model) {
    Query queryTop = new Query();
    queryTop.addCriteria(new Criteria().orOperator(
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.TOP.index + ""),
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.CANCEL_TOP.index + "")));
    int threadNum = Integer.parseInt(params.get("threadNum").toString());
    sendMergeTag(model, threadNum, Consts.COLLECTION_USER_TOP, queryTop);
  }

  private void install(Map<String, Object> params, ModelMap model) {
    Query queryTop = new Query();
    queryTop.addCriteria(new Criteria().orOperator(
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.INSTALL.index + ""),
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.UNINSTALL.index + "")));
    int threadNum = Integer.parseInt(params.get("threadNum").toString());
    sendMergeTag(model, threadNum, Consts.COLLECTION_USER_TOP, queryTop);
  }

  /**
   * 订阅攻略 自动 游戏库标签
   * 
   * @param params
   * @param model
   */
  private void subscribe(Map<String, Object> params, ModelMap model) {
    Query querySubscribe = new Query();
    querySubscribe.addCriteria(new Criteria().orOperator(
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.SUBSCRIBE.index + ""),
        Criteria.where("value.operatorType").is(Consts.COLLECTION_OPERATOR_TYPE.CANCEL_SUBSCRIBE.index + "")));
    int threadNum = Integer.parseInt(params.get("threadNum").toString());

    sendMergeTag(model, threadNum, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME, querySubscribe);
  }

  private void sendMergeTag(ModelMap model, int threadNum, String c, Query q) {
    pushTagService.removePushTagLogByName(c);
    pushTagService.successLogStart(threadNum, c, 0);
    PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(q, c, "operatorDate");
    try {
      // 设置时间戳

      long total = clientLogCollectionService.getCount(q, c);

      Map<String, Object> params = new HashMap<String, Object>();
      params.put("query", q);
      // 多线程
      multiTreadCore(threadNum, total, c, COLLECTION_PUSH_TAG_JOB_NAME.USER_REDUCE_TAG, params);

    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
      timeFlagService.recover(oldLastUpdateTime);
    }
  }

  /**
   * 发送tag
   */
  @Override
  public ModelMap sendPushTagsChannel(Map<String, Object> params, ModelMap model) {
    waiting();
    pushTagService.delThreadNumList();
    String c = COLLECTION_PUSH_TAG_JOB_NAME.USER_CHANNEL_TAG.name();
    // 清空之前的记录
    pushTagService.removePushTagLogByName(c);
    // 最新时间
    PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(null, c, "uploadDate");
    try {
      Date lmd = null;
      if(oldLastUpdateTime != null) {
        lmd = oldLastUpdateTime.getLastUpdateTime();
      }
      int total = 0;//userTokenService.countTokenBaiduUidChannelInfo(lmd);
      // 线程总量
      pushTagService.setThreadTotal(total + "");
      params.put("lastModifyDate", lmd);
      int threadNum = pushTagService.getSysThreadNum().getThreadNum();
      pushTagService.successLogStart(threadNum, c, 0);
      multiTreadCore(threadNum, total, c, COLLECTION_PUSH_TAG_JOB_NAME.USER_CHANNEL_TAG, params);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
      timeFlagService.recover(oldLastUpdateTime);
    }
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
  }

  private void exceptionLog(int threadNum, String c, Exception e) {
    PushTagExcuteStateInfo o = new PushTagExcuteStateInfo(c, new Date(), 0);
    o.setExceptionMsg(e.getMessage());
    pushTagService.savePushTagLog(o);
  }

  private void smallTag(Map<String, Object> params, ModelMap model) {
    Query queryOther = new Query();
    // 清空之前的记录
    pushTagService.removePushTagLogByName(Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME);
    Object[] in = new Object[]{Consts.COLLECTION_ITEM_TYPE.APP.index + "",
        Consts.COLLECTION_ITEM_TYPE.WALKTHROUGH.index + "", Consts.COLLECTION_ITEM_TYPE.GIFT.index + "",
        Consts.COLLECTION_ITEM_TYPE.VIDEO.index + "",
        Consts.COLLECTION_ITEM_TYPE.LIVE.index + "", Consts.COLLECTION_ITEM_TYPE.PIC.index + "",
        Consts.COLLECTION_ITEM_TYPE.ACT_CENTER.index + ""};
    // 其它
    queryOther.addCriteria(Criteria.where("value.itemType").in(Arrays.asList(in)));
    queryOther.addCriteria(Criteria.where("value.state").is(0));
    PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(queryOther,
        Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME, "value.uploadDate");
    long total = clientLogCollectionService.getCount(queryOther, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME);
    // 线程总量
    pushTagService.setThreadTotal(total + "");
    params.put("query", queryOther);
    try {
      int threadNum = pushTagService.getSysThreadNum().getThreadNum();
      pushTagService.successLogStart(threadNum, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME, 0);
      multiTreadCore(threadNum, total, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME,
          COLLECTION_PUSH_TAG_JOB_NAME.USER_REDUCE_TAG, params);
    }
    catch(Exception e) {
      timeFlagService.recover(oldLastUpdateTime);
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  /**
   * 攻略收集 手机app
   * @param params
   * @param model
   */
  private void walkThroughAppTag(Map<String, Object> params, ModelMap model) {
    Query queryOther = new Query();
    // 清空之前的记录
    pushTagService.removePushTagLogByName(Consts.COLLECTION_CLIENT_LOG_WALKTHROUGH_NAME);
//    // 其它
    PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(queryOther,
        Consts.COLLECTION_CLIENT_LOG_WALKTHROUGH_NAME, "uploadDate");
    long total = clientLogCollectionService.getCount(queryOther, Consts.COLLECTION_CLIENT_LOG_WALKTHROUGH_NAME);
    // 线程总量
    pushTagService.setThreadTotal(total + "");
    params.put("query", queryOther);
    try {
      int threadNum = pushTagService.getSysThreadNum().getThreadNum();
      pushTagService.successLogStart(threadNum, Consts.COLLECTION_CLIENT_LOG_WALKTHROUGH_NAME, 0);
      multiTreadCore(threadNum, total, Consts.COLLECTION_CLIENT_LOG_WALKTHROUGH_NAME,
          COLLECTION_PUSH_TAG_JOB_NAME.WALKTHROUGH_APP_GAME_TAG, params);
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void gameCode(Map<String, Object> params, ModelMap model) {
    Query queryOther = new Query();
    // 清空之前的记录
    pushTagService.removePushTagLogByName(Consts.COLLECTION_USER_GAME_PV);

    // 其它
    PushTagLastUpdateTime oldLastUpdateTime = timeFlagService.setTimestamp(queryOther, Consts.COLLECTION_USER_GAME_PV,
        "value.uploadDate");
    queryOther.addCriteria(Criteria.where("value.state").is(0));
    long total = clientLogCollectionService.getCount(queryOther, Consts.COLLECTION_USER_GAME_PV);
    // 线程总量
    pushTagService.setThreadTotal(total + "");
    params.put("query", queryOther);
    try {
      int threadNum = pushTagService.getSysThreadNum().getThreadNum();
      pushTagService.successLogStart(threadNum, Consts.COLLECTION_USER_GAME_PV, 0);
      multiTreadCore(threadNum, total, Consts.COLLECTION_USER_GAME_PV, COLLECTION_PUSH_TAG_JOB_NAME.USER_REDUCE_TAG,
          params);

    }
    catch(Exception e) {
      timeFlagService.recover(oldLastUpdateTime);
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
  }

  // private void searchTag(Map<String, Object> params,ModelMap model) {
  // Query queryOther = new Query();
  // // 清空之前的记录
  // pushTagService
  // .removePushTagLogByName(Consts.COLLECTION_USER_SEARCH_CONTENT_PV_NAME);
  // int threadNum = Integer.parseInt(params.get("threadNum").toString());
  //
  // pushTagService.successLogStart(threadNum,
  // Consts.COLLECTION_USER_SEARCH_CONTENT_PV_NAME, 0);
  // queryOther.addCriteria(Criteria.where("value.state").is(0));
  //
  // long total = clientLogCollectionService.getCount(queryOther,
  // Consts.COLLECTION_USER_SEARCH_CONTENT_PV_NAME);
  //
  // params.put("query", queryOther);
  // try {
  // // 多线程
  // multiTreadCore(threadNum, total,
  // Consts.COLLECTION_USER_SEARCH_CONTENT_PV_NAME,
  // COLLECTION_PUSH_TAG_JOB_NAME.USER_REDUCE_TAG, params);
  //
  // } catch (Exception e) {
  // model.put("message",
  // "manual updateLogInfo exception " + e.getMessage());
  // e.printStackTrace();
  // }
  // }

  private void combinationTag(Map<String, Object> params, ModelMap model, Query queryOther,
      PushTagCombination combination) {
    int threadNum = Integer.parseInt(params.get("threadNum").toString());
    combination.setThreadNum(threadNum);
    pushTagService.savePushTagCombination(combination);
    try {
      long total = clientLogCollectionService.getCount(queryOther, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME);
      pushTagService.setThreadTotal(total + "");
      pushTagService.successLogStart(threadNum, combination.getTagName(), 0);
      params.put("query", queryOther);
      params.put("combination", combination);
      // 多线程
      multiTreadCore(threadNum, total, Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME,
          COLLECTION_PUSH_TAG_JOB_NAME.USER_CHANNEL_TAG.COMBINATION_TAG, params);

    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      // e.printStackTrace();
    }
  }

  // private void bigTag(ModelMap model,Query queryOther) {
  // int threadNum = 20;
  // Date start = new Date();
  // // 其它
  // queryOther.addCriteria(Criteria.where("value.state").is(0));
  // try {
  // // 多线程
  // multiTreadCore(20, model, null, Consts.COLLECTION_ITEM_PV_NAME,
  // new PushTagOtherThread(pushTagService, queryOther));
  // // log
  // successLog(threadNum, Consts.COLLECTION_ITEM_PV_NAME, start);
  //
  // } catch (Exception e) {
  // exceptionLog(threadNum, Consts.COLLECTION_ITEM_PV_NAME, start, e);
  // model.put("message",
  // "manual updateLogInfo exception " + e.getMessage());
  // e.printStackTrace();
  // }
  // }

  @Override
  public ModelMap makeTag(ModelMap model) {
    // Query query = new Query();
    //
    // query.addCriteria(new Criteria().andOperator(
    // Criteria.where("value.operatorType").ne(
    // Consts.COLLECTION_OPERATOR_TYPE.CANCEL_FAVORITE.index
    // + ""),
    // Criteria.where("value.operatorType").ne(
    // Consts.COLLECTION_OPERATOR_TYPE.CANCEL_SUBSCRIBE.index
    // + ""),
    // Criteria.where("value.operatorType").ne(
    // Consts.COLLECTION_OPERATOR_TYPE.CANCEL_TOP.index + "")));
    // // 设置时间戳
    // PushTagLastUpdateTime oldLastUpdateTime =
    // timeFlagService.setTimestamp(
    // query, Consts.COLLECTION_JOB_MAKE_TAG, "uploadDate");
    // // 多线程
    // multiTreadCore(1, model, oldLastUpdateTime,
    // Consts.COLLECTION_ITEM_OPERATE_PV_NAME, new MakeTagThread(
    // pushTagService, query));
    //
    // model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());

    return model;
  }

  @Override
  public void shutDown() {
    // taskExecutor.
    taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
    taskExecutor.destroy();
  }

  @Override
  public ModelMap updateLogInfo(Map<String, Object> params, ModelMap model) {
    int threadNum = Integer.parseInt(params.get("threadNum").toString());
    try {
      long total = clientLogCollectionService.getCount(Consts.COLLECTION_ITEM_PV_NAME);
      // 多线程
      multiTreadCore(threadNum, total, null, COLLECTION_PUSH_TAG_JOB_NAME.COLLECTION_UPATE,
          new HashMap<String, Object>());
    }
    catch(Exception e) {
      model.put("message", "manual updateLogInfo exception " + e.getMessage());
      e.printStackTrace();
    }
    model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    return model;
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
    // if (threadN * 10 >= total) {
    // threadN = 1;
    // }
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

      PThread pThread = getThreadService(type, params);

      pThread.setName(collectionName);
      pThread.setStart(start);
      pThread.setEnd(end);
      taskExecutor.execute(pThread);
    }
    // taskExecutor.shutdown();
  }

  private PThread getThreadService(COLLECTION_PUSH_TAG_JOB_NAME type, Map<String, Object> params) {
    params.put("jobType", type);
    PThread pThread = new PushTagOtherThread(pushTagService,pushTagXinGeService, params);
//    switch(type) {
//      case COMBINATION_TAG :
//        PushTagCombination combination = (PushTagCombination) params.get("combination");
//        pThread = new PushCombinationTagThread(pushTagService, query, combination);
//        break;
//      case USER_CHANNEL_TAG :
//        pThread = new PushTagChannelThread(pushTagService, (Date) params.get("lastModifyDate"));
//        break;
//      case COLLECTION_UPATE :
//        // pThread = new ServiceNameThread(clientLogCollectionService);
//        break;
//       case WALKTHROUGH_APP_GAME_TAG:
//       pThread = new PushWalkThroughTagOtherThread(pushTagService, query);
//       break;
//      case USER_REDUCE_TAG :
//        pThread = new PushTagOtherThread(pushTagService, query);
//        break;
//
//      default :
//        break;
//    }
    return pThread;
  }

}
