package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.CONTENT_SOURCE;
import com.cyou.video.mobile.server.cms.common.Consts.GAME_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_TAG_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_TAG_SETTING_STATE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogBestWalkthroughCollection;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo2;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.push.PushTagCombination;
import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.mongo.dao.collection.ClientLogCollectionDao;
import com.cyou.video.mobile.server.cms.mongo.dao.collection.PushTagLogDao;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.HttpUtil;

/**
 * 意见反馈业务实现
 * 
 * @author jyz
 */
@Service("pushTagService")
public class PushTagServiceImpl implements PushTagService {

  private Logger logger = LoggerFactory.getLogger(PushTagServiceImpl.class);

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  private ClientLogCollectionDao clientLogCollectionDao;


  @Autowired
  PushInterface baiduPush;

  private PushTagLogDao pushTagLogDao;

  @Value("${game.mobile}")
  private String game_cate_mobile;

  @Value("${game.pc}")
  private String game_cate_pc;

  @Value("${jiongtu}")
  private String jiongtu;

  @Value("${mobile.app.shouyou}")
  private String mobile_app_shouyou;


  @Override
  public String getUidByToken(String token) throws Exception {
//    UserToken n = userTokenService.getToken(token);
//    if(n == null) return null;
//    return userTokenService.getBaiduUserIdByTokenId(n.getId());
	  return null;
  }

  /**
   * 组装tag
   * 
   * @param clientLogCollection
   * @return
   */
  @Override
  public String makeUserTagId(ClientLogCollection clientLogCollection) {
    StringBuffer buffer = new StringBuffer();
    // if (clientLogCollection.getServiceId() == null)
    // buffer.append(clientLogCollection.getServiceName());
    // else
    buffer.append(clientLogCollection.getServiceId());
    buffer.append("_");
    buffer.append(clientLogCollection.getItemTypeE().getIndex());
    buffer.append("_");
    buffer.append(clientLogCollection.getOperatorTypeE().getIndex());
    return buffer.toString();
  }

  @Override
  public String makeUserTagId(ClientLogCollection id, ClientLogCollection value) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(id.getServiceId());
    buffer.append("_");
    buffer.append(value.getItemTypeE().getIndex());
    buffer.append("_");
    if(id.getOperatorTypeE() == null)
      buffer.append(COLLECTION_OPERATOR_TYPE.VIEW.getIndex());
    else
      buffer.append(id.getOperatorTypeE().getIndex());

    return buffer.toString();
  }

  /**
   * 订阅指定收藏
   * 
   * @param clientLogCollection
   * @return
   */
  private String makeUserTagSubscribeFavorateTop(UserItemOperatePvMongo2 u) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(u.getId().getServiceId());
    buffer.append("_");
    buffer.append(u.getValue().getItemType());
    buffer.append("_");
    buffer.append(u.getValue().getOperatorType());
    return buffer.toString();
  }

  /**
   * 大标签 gamecode
   * 
   * @param clientLogCollection
   * @return
   */
  private String makeBigTag(UserItemOperatePvMongo2 u) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(u.getValue().getGameCode());
    return buffer.toString();
  }

  /**
   * 搜索标签
   * 
   * @param clientLogCollection
   * @return
   */
  private String makeSearchTag(UserItemOperatePvMongo2 u) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(u.getId().getKeyWord());
    return buffer.toString();
  }

  @Override
  public String makeUserTagName(ClientLogCollection id, ClientLogCollection v) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(v.getServiceName());
    buffer.append("_");
    buffer.append(v.getItemTypeE());
    buffer.append("_");
    if(id.getOperatorTypeE() == null)
      buffer.append(COLLECTION_OPERATOR_TYPE.VIEW);
    else
      buffer.append(id.getOperatorTypeE());
    return buffer.toString();
  }

  @Override
  public String makeUserTagName(ClientLogCollection clientLogCollection) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(clientLogCollection.getServiceName());
    buffer.append("_");
    buffer.append(clientLogCollection.getItemTypeE());
    buffer.append("_");
    buffer.append(clientLogCollection.getOperatorTypeE());
    return buffer.toString();
  }

  private boolean updateTagStateInMongoBatch(ClientLogCollection ids, int state, String name) {
    clientLogCollectionDao.updateBatch(ids, name, state);
    logger.debug(" tag state is " + state);
    return true;
  }

  @Override
  public boolean removeAllPushTags() {
    return clientLogCollectionDao.removeAllPushTags() == 1 ? true : false;
  }

  // @Override
  // public void makeTagCollection(int s, int end, Query query, String name) {
  // Map<String, Object> params = new HashMap<String, Object>();
  // int size = 1000;
  // int times = 0;
  //
  // List<UserItemOperatePvMongo2> userTag = new
  // ArrayList<UserItemOperatePvMongo2>();
  // query.with(new Sort(Direction.DESC, "value.operatorDate"));
  // userTag = clientLogCollectionDao.getUserItemOperatePvMongo2(times,
  // size, query, name);
  //
  // while (!userTag.isEmpty()) {
  //
  // List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
  // for (Iterator iterator = userTag.iterator(); iterator.hasNext();) {
  // ClientLogCollection clc = null;
  // com.cyou.video.mobile.server.cms.model.collection.Value v = null;
  // try {
  // UserItemOperatePvMongo2 uTag = (UserItemOperatePvMongo2) iterator
  // .next();
  //
  // clc = uTag.getId();
  // v = uTag.getValue();
  // if (v.getItemType() != null)
  // clc.setItemType(v.getItemType());
  // if (Consts.COLLECTION_KEYWORD_PV_NAME.equals(name)) {
  // clc.setServiceName(clc.getKeyWord());
  // clc.setItemType(COLLECTION_ITEM_TYPE.SEARCH.index + "");
  // clc.setOperatorType(COLLECTION_OPERATOR_TYPE.LEAVE_COMMENTS.index
  // + "");
  // v.setServiceName(clc.getServiceName());
  // }
  // PushTagCollection tag = new PushTagCollection();
  // tag.setTagId(this.makeUserTagId(clc));
  // tag.setTagName(this.makeUserTagName(clc, v));
  // // tag.setPv(v.getPv());
  // tag.setTagType(Integer.parseInt(clc.getItemType()));
  // tags.add(tag);
  // } catch (Exception e) {
  // e.printStackTrace();
  // logger.info("set tag exception is " + e.getMessage());
  // }
  // }
  //
  // clientLogCollectionDao.insertPushTags(tags);
  //
  // int start = (++times) * size;
  // userTag = clientLogCollectionDao.getPVByName(
  // Consts.COLLECTION_ITEM_PV_NAME, start, size, params);
  //
  // logger.info("times is" + start);
  // }
  //
  // }

  @Override
  public boolean makePushTagCombination(PushTagCombination com) {
    return clientLogCollectionDao.insert(com) == 1 ? true : false;
  }

  @Override
  public void savePushTagLog(PushTagExcuteStateInfo com) {
    clientLogCollectionDao.save(com);
  }

  @Override
  public void incThreadNum(String name) {
    Query query = new Query();
    query.addCriteria(new Criteria().where("name").is("name"));
    clientLogCollectionDao.updateInc("threadNum", query, PushTagExcuteStateInfo.class);
  }

  @Override
  public void savePushTagCombination(PushTagCombination com) {
    clientLogCollectionDao.save(com);
  }

  @Override
  public void removePushTagLog(PushTagExcuteStateInfo com) {
    clientLogCollectionDao.remove(com);
  }

  @Override
  public void removePushTagLogByName(String name) {
    clientLogCollectionDao.remove(PushTagExcuteStateInfo.class, new Query().addCriteria(new Criteria("name").is(name)));
  }

  @Override
  public List<PushTagExcuteStateInfo> getPushTagLog(String name) {
    return (List) clientLogCollectionDao.findByCondition(PushTagExcuteStateInfo.class,
        new Query().addCriteria(new Criteria("name").is(name)));
  }

  @Override
  public boolean existPushTagCombination(PushTagCombination com) {
    return clientLogCollectionDao.findByField(PushTagCombination.class, "tagName", com.getTagName()) == null ? false
        : true;
  }

  @Override
  public Pagination findPushTagCombination(Map<String, Object> params) {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("curPage").toString());
    pagination.setCurPage(curPage);
    pagination.setPageSize(Pagination.PAGESIZE);
    curPage = (curPage - 1) * Pagination.PAGESIZE;
    Query q = new Query();
    if(params.get("serviceName") != null) {

      Pattern pattern = Pattern.compile("^.*" + params.get("serviceName").toString() + ".*$");
      q.addCriteria(Criteria.where("tagName").regex(pattern));
    }
    q.limit(Pagination.PAGESIZE);
    q.skip(curPage);
    q.with(new Sort(Sort.Direction.DESC, "updateTime"));
    List list = clientLogCollectionDao.findByCondition(PushTagCombination.class, q);
    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      PushTagCombination p = (PushTagCombination) iterator.next();
      List<PushTagExcuteStateInfo> l = this.getPushTagLog(p.getTagName());
      if(l == null || l.isEmpty()) {
        p.setProcess("0/" + p.getThreadNum());
        p.setState(PUSH_SEND_TAG_STATE.WAITING);
      }
      else {
        PushTagExcuteStateInfo excuteStateInfo = l.get(0);
        p.setProcess(excuteStateInfo.getFinishThreadNum() + "/" + p.getThreadNum());
        p.setSetTotal((int) excuteStateInfo.getSize());
        p.setState(excuteStateInfo.getState());
      }
      int total = 0;

    }
    long count = clientLogCollectionDao.countByCondition(PushTagCombination.class, q);
    pagination.setRowCount((int) count);
    pagination.setContent(list);
    return pagination;
  }

  @Override
  public PushTagCombination findTagCombinationOne(String name) {
    return (PushTagCombination) clientLogCollectionDao.findByField(PushTagCombination.class, "tagName", name);
  }

  @Override
  public int sendPushTags(int s, int end, Query query, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    logger.info("<" + threadName + "> start is " + s + " end is " + end);
    long cha = end - s;
    // multi thread
    int size = 10000;
    if(size > end) {
      size = (int) cha;
    }
    int start = (int) s;

    List<UserItemOperatePvMongo2> userSub = clientLogCollectionDao.getUserItemOperatePvMongo2(start, size, query, name);
    // logger.info("userTag list is " + userSub);
    // log
    int i = 0;
    int total = 0;
    while(!userSub.isEmpty()) {
      int result = sendPushTagCoreOther(userSub, name);
      i++;
      total += result;
      // multi thread
      start = start + size;
      if(start >= end) break;

      if(start + size > end) {
        size = (int) end - start;
      }
      userSub = clientLogCollectionDao.getUserItemOperatePvMongo2(start, size, query, name);
      // log
      if(i % 50 == 0 || i == cha) {
        logger.info(threadName + "  end(" + end + ") between start(" + s + ") number " + cha + " exucute number " + i);
      }
    }
    return total;
  }

  @Override
  public int sendPushWalkThroughTags(int s, int end, Query query, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    logger.info("<" + threadName + "> start is " + s + " end is " + end);
    long cha = end - s;
    // multi thread
    int size = 10000;
    if(size > end) {
      size = (int) cha;
    }
    int start = (int) s;
    List<ClientLogBestWalkthroughCollection> userSub = clientLogCollectionDao.getClientLogWalkthroughCollection(start,
        size, query, name);
    int i = 0;
    int total = 0;
    while(!userSub.isEmpty()) {
      int result = sendPushWalkThroughTagCoreOther(userSub, name);
      i++;
      total += result;
      // multi thread
      start = start + size;
      if(start >= end) break;

      if(start + size > end) {
        size = (int) end - start;
      }
      userSub = clientLogCollectionDao.getClientLogWalkthroughCollection(start, size, query, name);
      // log
      if(i % 50 == 0 || i == cha) {
        logger.info(threadName + "  end(" + end + ") between start(" + s + ") number " + cha + " exucute number " + i);
      }
    }
    return total;
  }

  @Override
  public int sendPushCombinationTags(int s, int end, Query query, String name, PushTagCombination combination) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    logger.info("<" + threadName + ">  start is " + s + " end is " + end);
    long cha = end - s;
    // multi thread
    int size = 10000;
    if(size > end) {
      size = (int) cha;
    }
    int start = (int) s;

    List<UserItemOperatePvMongo2> userSub = clientLogCollectionDao.getUserItemOperatePvMongo2(start, size, query, name);
    int totalSetNum = 0;
    // log
    int i = 0;
    while(!userSub.isEmpty()) {
      int setNum = sendPushCombinationTagCore(userSub, name, combination);
      totalSetNum += setNum;
      // multi thread
      start = start + size;
      if(start >= end) break;

      if(start + size > end) size = (int) end - start;
      userSub = clientLogCollectionDao.getUserItemOperatePvMongo2(start, size, query, name);
      // log
      if(i % 50 == 0 || i == cha) {

        logger.info(threadName + "  end(" + end + ") between start(" + s + ") number " + cha + " exucute number " + i);

      }

      i++;
    }
    return totalSetNum;
  }

  @Override
  public int sendPushTagsSubscribe(int s, int end, Query query, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    logger.info("<" + threadName + "> start is " + s + " end is " + end);
    long cha = end - s;
    // multi thread
    int size = 10000;
    if(size > end) {
      size = (int) cha;
    }
    int start = (int) s;

    logger.info("defaut usertag size is " + size);
    List<UserItemOperatePvMongo2> userSub = new ArrayList<UserItemOperatePvMongo2>();
    userSub = clientLogCollectionDao.getUserItemOperatePvMongo2(start, size, query, name);
    // log
    int i = 0;
    int total = 0;
    while(!userSub.isEmpty()) {

      int result = sendPushTagCore(userSub);
      total += result;
      // multi thread
      start = start + size;
      if(start >= end) break;

      if(start + size > end) size = (int) end - start;
      // logger.info(threadName
      // +
      // " =================================================================================== "
      // + result);

      logger.info(threadName
          + " =================================================================================== end(" + end
          + ") between start(" + s + ") number " + cha + " exucute number " + i);

      userSub = clientLogCollectionDao.getUserItemOperatePvMongo2(start, size, query, name);
      // log
      // if (i % 5 == 0 || i == cha) {

      // }
      i++;

    }
    return total;
  }

  private int sendPushTagCore(List<UserItemOperatePvMongo2> userTag) {
    int setNum = 0;
    int delNum = 0;
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      ClientLogCollection clc = null;
      String baiduId = null;
      String tag = null;
      try {
        UserItemOperatePvMongo2 uTag = (UserItemOperatePvMongo2) iterator.next();
        clc = uTag.getId();

        if(clc.getUid() == null)// 没有token
          continue;
        else {
          // set 百度 id
          baiduId = this.getUidByToken(clc.getUid());
          logger.info("baiduId is " + baiduId + " user token is " + clc.getUid());

          if(baiduId == null) {// 没有baidu id
            continue;
          }
          else
            clc.setPushToken(baiduId);

          tag = this.makeUserTagSubscribeFavorateTop(uTag);
          CLIENT_TYPE clientType = null;
          if(clc.getUid().length() > 20)
            clientType = CLIENT_TYPE.IOS;
          else
            clientType = CLIENT_TYPE.ANDROID;

          if(isSetTag(uTag)) {// set tag
            baiduPush.setTag(baiduId, tag);
            logger.info("Sent tag to baidu . tagid is " + tag + " uid is " + baiduId);
            setNum++;
          }
          if(isDelTag(uTag)) {
            baiduPush.deleteTag(baiduId, tag);
            // logger.info("Delete tag to baidu . tagid is " +
            // tag+" uid is "+baiduId);
            delNum++;
          }
        }
      }
      catch(Exception e) {

        logger.info("Exception sent tag to baidu . tagid is " + tag + " uid is " + baiduId + " exception is "
            + e.getMessage());
        // e.printStackTrace();
      }
    }
    return (setNum + +delNum);
  }

  private int sendPushTagCoreOther(List<UserItemOperatePvMongo2> userTag, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    int setNum = 0;
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      inc(threadName, setNum);
      setNum++;
      ClientLogCollection id = null;
      com.cyou.video.mobile.server.cms.model.collection.Value value = null;
      try {
        UserItemOperatePvMongo2 uTag = (UserItemOperatePvMongo2) iterator.next();
        id = uTag.getId();
        value = uTag.getValue();

        if(id.getUid() == null) {// 没有token
          // this.updateTagStateInMongoBatch(id,
          // PUSH_TAG_SETTING_STATE.NTOKEN.getIndex(), name);
          continue;
        }
        else {
          // id.setUid("49762622979690");
          // set 百度 id
//          UserToken n = userTokenService.getToken(id.getUid());
//          if(n == null) {
//            continue;
//          }
          String baiduId = null;//userTokenService.getBaiduUserIdByTokenId(n.getId());
          logger.debug("baiduId is " + baiduId + " user token is " + id.getUid());

          if(baiduId == null) {// 没有baidu id
            // this.updateTagStateInMongoBatch(id,
            // PUSH_TAG_SETTING_STATE.NUID.getIndex(), name);
            continue;
          }
          else
            id.setPushToken(baiduId);
          List<Boolean> suc = new ArrayList<Boolean>();
          // client type
          if(Consts.COLLECTION_USER_GAME_PV.equals(name)) {
            suc.add(hits(id, value, baiduId));
          }
          else {
            suc.add(jiong(id, value, baiduId));

            news(id, value, baiduId);

            suc.add(live(id, value, baiduId));

            suc.add(video(id, value, baiduId));

            suc.add(activity(id, value, baiduId));
            //
            suc.add(bySearch(id, value, baiduId));

            suc.add(byRank(id, value, baiduId));
            // 订阅
            subscribe(id, value, baiduId);
            // 订阅
            top(id, value, baiduId);

            suc.add(desktop(id, value, baiduId));
            // app
            app(id, value, baiduId);
          }
          if(suc.contains(true)) this.updateTagStateInMongoBatch(id, PUSH_TAG_SETTING_STATE.SUC.getIndex(), name);
        }
      }
      catch(Exception e) {
        logger.error("set tag exception is " + e.getMessage());
      }
    }
    return setNum;
  }

  private int sendPushWalkThroughTagCoreOther(List<ClientLogBestWalkthroughCollection> userTag, String name) {
    String threadName = String.valueOf(Thread.currentThread().getName());
    int setNum = 0;
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      inc(threadName, setNum);
      setNum++;
      com.cyou.video.mobile.server.cms.model.collection.Value value = null;
      try {
        ClientLogBestWalkthroughCollection uTag = (ClientLogBestWalkthroughCollection) iterator.next();

        if(uTag.getUid() == null) {// 没有token
          continue;
        }
        else {
          // id.setUid("49762622979690");
          // set 百度 id
          UserToken n =null;// userTokenService.getToken(uTag.getUid());
          if(n == null) {
            continue;
          }
          String baiduId = null;// userTokenService.getBaiduUserIdByTokenId(n.getId());
          logger.debug("baiduId is " + baiduId + " user token is " + uTag.getUid());
          if(baiduId == null) {// 没有baidu id
            continue;
          }
          else
            uTag.setPushToken(baiduId);
          // app
          app(uTag, uTag, baiduId);

        }
      }
      catch(Exception e) {
        logger.error("set tag exception is " + e.getMessage());
      }
    }
    return setNum;
  }

  private void inc(String threadName, int setNum) {
    int incc = 50;
    if(setNum != 0 && setNum % incc == 0) pushTagLogDao.inc(threadName, incc);
  }

  private CLIENT_TYPE getPlatForm(UserToken n) {
    CLIENT_TYPE clientType = null;
    if(n.getPlat() == 6) {
      clientType = CLIENT_TYPE.IOS;
    }
    else if(n.getPlat() == 7) {
      clientType = CLIENT_TYPE.ANDROID;
    }
    return clientType;
  }

  /**
   * 或观看30s以上
   * 
   * @param id
   * @param value
   * @param baiduId
   * @param clientType
   * @throws Exception
   */
  private boolean video(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.VIDEO) {
      // if (id.getOperatorTypeE() ==
      // COLLECTION_OPERATOR_TYPE.LEAVE_COMMENTS
      // || id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SHARE
      // || id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.DOWNLOAD) {
      // setGameTag(value, baiduId, clientType);
      // }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
        if(value.getKeyWord() != null && !"".equals(value.getKeyWord())) {
          long sec = Long.parseLong(value.getKeyWord().trim()) / 1000;
          if(sec > 30) {
            setGameTag(value, baiduId);
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean live(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.LIVE) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
        if(value.getKeyWord() != null && !"".equals(value.getKeyWord())) {
          long sec = Long.parseLong(value.getKeyWord().trim()) / 1000;
          if(sec > 30) {
            setGameTag(value, baiduId);
            baiduPush.setTag(baiduId, value.getGameCode() + "_" + COLLECTION_ITEM_TYPE.LIVE.index);// XX游戏直播
            return true;
          }
        }

      }
    }
    return false;
  }

  private void news(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.NEWS) {
      // if (id.getOperatorTypeE() ==
      // COLLECTION_OPERATOR_TYPE.LEAVE_COMMENTS
      // || id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SHARE) {
      // setGameTag(value, baiduId, clientType);
      // }
    }
  }

  private boolean activity(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.ACT_CENTER) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
        if(!StringUtils.isEmpty(value.getServiceName())) baiduPush.setTag(baiduId, value.getServiceName());
        baiduPush.setTag(baiduId, "ACT_CENTER");
        return true;
      }
    }
    return false;
  }

  private boolean bySearch(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(id.getOtherWay() == 5) {
      if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH || value.getItemTypeE() == COLLECTION_ITEM_TYPE.VIDEO
          || value.getItemTypeE() == COLLECTION_ITEM_TYPE.NEWS) {
        if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
          return setGameTag(value, baiduId);
        }
        if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SUBSCRIBE) {
          return setGiftTag(value, baiduId);
        }

      }
      if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.GIFT) {
        if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
          return setGiftTag(value, baiduId);
        }

      }
    }
    return false;
  }

  private boolean byRank(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(id.getOtherWay() == 15) {
      setGameTag(value, baiduId);
      if(!StringUtils.isEmpty(value.getKeyWord())) {
        String rankTag = value.getKeyWord() + "_" + COLLECTION_ITEM_TYPE.RANK.getIndex();
        baiduPush.setTag(baiduId, rankTag);
        return true;
      }
    }
    return false;
  }

  private boolean jiong(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.PIC) {
      if(!StringUtils.isEmpty(value.getKeyWord()))
        baiduPush.setTag(baiduId, value.getKeyWord() + "_" + COLLECTION_ITEM_TYPE.JIONG.index);
      return true;
    }
    return false;
  }

  /**
   * 访问量 礼包标签
   * 
   * @param id
   * @param value
   * @param baiduId
   * @param clientType
   * @throws Exception
   */
  private boolean hits(ClientLogCollection id, com.cyou.video.mobile.server.cms.model.collection.Value value,
      String baiduId) throws Exception {
    if(value.getPv() >= 3) {
      return setGameTag(value, baiduId);
    }
    return false;
  }

  private void subscribe(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SUBSCRIBE) {
        setGameTag(value, baiduId);
      }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.CANCEL_SUBSCRIBE) {
        delGameTag(value, baiduId);
      }
    }
  }

  private void top(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.TOP) {
        setGiftTag(value, baiduId);
      }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.CANCEL_TOP) {
        delGiftTag(value, baiduId);
      }
    }
  }

  private boolean desktop(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.DESKTOP) {
        return setGiftTag(value, baiduId);
      }
    }
    return false;
  }

  private void app(ClientLogCollection id, ClientLogCollection value, String baiduId) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.APP) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.INSTALL) {
        setGameTag(value, baiduId);
      }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.UNINSTALL) {
        delGameTag(value, baiduId);
      }
    }
  }

  private boolean setGiftTag(ClientLogCollection value, String baiduId) throws Exception {
    setGameTag(value, baiduId);
    if(!StringUtils.isEmpty(value.getGameCode()))
      baiduPush.setTag(baiduId, value.getGameCode() + "_" + COLLECTION_ITEM_TYPE.GIFT.index);// gaem
    return true;
  }

  private void delGiftTag(ClientLogCollection value, String baiduId) throws Exception {
    delGameTag(value, baiduId);
    baiduPush.deleteTag(baiduId, value.getGameCode() + "_" + COLLECTION_ITEM_TYPE.GIFT.index);// gaem
                                                                                              // platform
  }

  private void delGameTag(ClientLogCollection value, String baiduId) throws Exception {
    try {
      if(!StringUtils.isEmpty(value.getGameCode())) {
        baiduPush.deleteTag(baiduId, value.getGameCode());// game
        baiduPush.deleteTag(baiduId, value.getGameType() + "");// game
        baiduPush.deleteTag(baiduId, value.getGameStatus() + "");// game
        baiduPush.deleteTag(baiduId, value.getGamePlatForm() + "");// gaem
        // platform
      }
    }
    catch(Exception e) {
      // TODO: handle exception
    }
  }

  private boolean setGameTag(ClientLogCollection value, String baiduId) throws Exception {
    baiduPush.setTag(baiduId, value.getGameCode());// game
    sendMultiStr(value.getGameType(), baiduId); // code
    sendMultiStr(value.getGameStatus(), baiduId);
    if(value.getGamePlatForm() != -1) {
      baiduPush.setTag(baiduId, value.getGamePlatForm() + "_P");// game
    }
    return true;
  }

  private void sendMultiStr(String str, String baiduId) throws Exception {
    if(str.indexOf(",") >= 0) {
      String[] s = str.split(",");
      for(int i = 0; i < s.length; i++) {
        baiduPush.setTag(baiduId, s[i]);// game
      }
    }
    else {
      baiduPush.setTag(baiduId, str);// game
    }
  }

  private boolean isBySearchView(ClientLogCollection id) {
    return id.getOtherWay() == 5;
  }

  private int sendPushCombinationTagCore(List<UserItemOperatePvMongo2> userTag, String name,
      PushTagCombination combination) {
    int setNum = 0;
    // log
    String threadName = String.valueOf(Thread.currentThread().getName());
    for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
      inc(threadName, setNum);
      setNum++;
      ClientLogCollection id = null;
      ClientLogCollection value = null;
      try {
        UserItemOperatePvMongo2 uTag = (UserItemOperatePvMongo2) iterator.next();
        id = uTag.getId();
        value = uTag.getValue();

        if(id.getUid() == null)// 没有token
          continue;
        else {
          // token
          UserToken n = null;// userTokenService.getToken(id.getUid());
          if(n == null) {
            continue;
          }
          // baidu id
          String baiduId = null;// userTokenService.getBaiduUserIdByTokenId(n.getId());
          logger.debug("baiduId is " + baiduId);
          if(baiduId == null) {// 没有baidu id
            continue;
          }
          else
            id.setPushToken(baiduId);
          // client type
          CLIENT_TYPE clientType = getPlatForm(n);
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
            baiduPush.setTag(baiduId, tag);
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

  private boolean isSetTag(UserItemOperatePvMongo2 uTag) {
    return (Consts.COLLECTION_OPERATOR_TYPE.SUBSCRIBE.index + "").equals(uTag.getValue().getOperatorType())
        || (Consts.COLLECTION_OPERATOR_TYPE.TOP.index + "").equals(uTag.getValue().getOperatorType())
        || (Consts.COLLECTION_OPERATOR_TYPE.FAVORITE.index + "").equals(uTag.getValue().getOperatorType())
        || (Consts.COLLECTION_OPERATOR_TYPE.INSTALL.index + "").equals(uTag.getValue().getOperatorType());
  }

  private boolean isDelTag(UserItemOperatePvMongo2 uTag) {
    return (Consts.COLLECTION_OPERATOR_TYPE.CANCEL_SUBSCRIBE.index + "").equals(uTag.getValue().getOperatorType())
        || (Consts.COLLECTION_OPERATOR_TYPE.CANCEL_TOP.index + "").equals(uTag.getValue().getOperatorType())
        || (Consts.COLLECTION_OPERATOR_TYPE.CANCEL_FAVORITE.index + "").equals(uTag.getValue().getOperatorType())
        || (Consts.COLLECTION_OPERATOR_TYPE.UNINSTALL.index + "").equals(uTag.getValue().getOperatorType());
  }

  @Override
  public int sendPushTagsChannel(int s, int end, Date lastModifyDate) throws Exception {
    // log
    String threadName = String.valueOf(Thread.currentThread().getName());
    long cha = end - s;
    logger.info(threadName + " start(" + s + ")  end (" + end + ") number " + cha);
    // multi thread
    int size = 10000;
    if(size > end) {
      size = (int) cha;
    }
    int start = (int) s;
    logger.info("defaut usertag size is " + size);
    List<UserToken> userTag = null;// userTokenService.getTokenBaiduUidChannelInfo(start, size, lastModifyDate);
    logger.info("userTag list is " + userTag);
    int i = 0;
    while(userTag != null && !userTag.isEmpty()) {
      UserToken user = null;
      for(Iterator iterator = userTag.iterator(); iterator.hasNext();) {
        try {
          inc(threadName, i);
          i++;
          user = (UserToken) iterator.next();
          String tag = user.getChannel();
          String version = user.getCurUsedVersion();
          logger.info("tag is " + tag);
          if(!StringUtils.isEmpty(tag)) baiduPush.setTag(user.getBaiduUserID(), tag);
          if(!StringUtils.isEmpty(version)) baiduPush.setTag(user.getBaiduUserID(), version);
        }
        catch(Exception e) {
          logger.error("set tag exception is " + e.getMessage() + " uid is " + user.getBaiduUserID());
          e.printStackTrace();
        }

      }
      logger.info(threadName + " total-----------(" + i + ") ");

      // multi thread
      start = start + size;
      if(start >= end) break;

      if(start + size > end) size = (int) end - start;
      userTag =null;// userTokenService.getTokenBaiduUidChannelInfo(start, size, lastModifyDate);

    }

    return i;
  }

  /**
   * query tag
   * 
   * @param tag
   * @param clientType
   * @throws ChannelServerException
   * @throws ChannelClientException
   */
  @Override
  public ModelMap queryUserTag(String token, ModelMap model) throws Exception {
    String baiduId = "";
    UserToken n = null;// userTokenService.getToken(token);
    if(n == null) {
      model.put("message", "token not found");
    }
    else {
      baiduId = null;// userTokenService.getBaiduUserIdByTokenId(n.getId());
      model.put("uid", baiduId);
      model.put("userTag", baiduPush.queryUserTag(baiduId));
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    return model;
  }

  @Override
  public ModelMap deleteUserTag(String token, ModelMap model) throws Exception {
    String baiduId = "";
    UserToken n = null;//userTokenService.getToken(token);
    if(n == null) {
      model.put("message", "token not found");
    }
    else {
      baiduId = null;// userTokenService.getBaiduUserIdByTokenId(n.getId());
      model.put("uid", baiduId);
      baiduPush.deleteUserTag(baiduId);
      model.put("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    return model;
  }

  @Override
  public void successLogEnd(String c, int total) {
    List<PushTagExcuteStateInfo> o1 = this.getPushTagLog(c);
    // if (o1 == null||o1.isEmpty()) {
    // PushTagExcuteStateInfo o = new PushTagExcuteStateInfo(c,
    // new Date(), 0);
    // String threadName = String
    // .valueOf(Thread.currentThread().getName());
    // o.setThreadName(threadName);
    // o.setSize(total);
    // pushTagService.savePushTagLog(o);
    // } else {
    PushTagExcuteStateInfo excuteStateInfo = o1.get(0);
    // System.out.println("======================== "
    // + excuteStateInfo.getFinishThreadNum());
    excuteStateInfo.setFinishThreadNum(excuteStateInfo.getFinishThreadNum() + 1);
    excuteStateInfo.setSize(excuteStateInfo.getSize() + total);
    excuteStateInfo.setLastUpdate(new Date());
    if(excuteStateInfo.getThreadNum() == excuteStateInfo.getFinishThreadNum())
      excuteStateInfo.setState(PUSH_SEND_TAG_STATE.WAITING);
    this.savePushTagLog(excuteStateInfo);
    // }
  }

  @Override
  public void successLogStart(int threadNum, String c, long size) {
    PushTagExcuteStateInfo o = new PushTagExcuteStateInfo(c, new Date(), threadNum);
    o.setSize(size);
    o.setState(PUSH_SEND_TAG_STATE.RUNNING);
    this.savePushTagLog(o);
  }

  @Override
  public boolean existRunningThread() {
    Query query = new Query();
    query.addCriteria(new Criteria().where("state").is(PUSH_SEND_TAG_STATE.RUNNING.name()));
    List l = clientLogCollectionDao.findByCondition(PushTagExcuteStateInfo.class, query);
    if(l == null || l.isEmpty())
      return false;
    else
      return true;
  }

  @Override
  public boolean isRunningMapReduce() {
    StatisticJobLastUpdateTime op = (StatisticJobLastUpdateTime) clientLogCollectionDao.findByField(
        StatisticJobLastUpdateTime.class, "statisicJobName", Consts.COLLECTION_ITEM_OPERATE_PV_NAME);
    StatisticJobLastUpdateTime uop = (StatisticJobLastUpdateTime) clientLogCollectionDao.findByField(
        StatisticJobLastUpdateTime.class, "statisicJobName", Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME);
    Calendar uopc = Calendar.getInstance();
    uopc.setTime(uop.getLastUpdateTime());
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(op.getLastUpdateTime());
    if(cal1.compareTo(uopc) == -1)
      return true;
    else
      return false;
  }

  @Override
  public List getJiong() throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
    String result = HttpUtil.syncGet(jiongtu + "/api/section/list", params, "UTF-8", "UTF-8");
    JSONObject obj = new JSONObject(result);
    JSONArray arr = obj.getJSONObject("Data").getJSONArray("List");
    for(int i = 0; i < arr.length(); i++) {
      JSONObject o = arr.getJSONObject(i);
      PushTagCollection p = new PushTagCollection();
      p.setTagId(o.getString("ID") + "_" + COLLECTION_ITEM_TYPE.JIONG.index);
      p.setTagName(o.getString("Title"));
      tags.add(p);
    }
    return tags;
  }

  @Override
  public List getRankTag() throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    List<PopGameTopCate> ranks = popGameTopService.listTopCate(1);
//    for(Iterator iterator = ranks.iterator(); iterator.hasNext();) {
//      PopGameTopCate o = (PopGameTopCate) iterator.next();
//      PushTagCollection p = new PushTagCollection();
//      p.setTagId(o.getId() + "_" + COLLECTION_ITEM_TYPE.RANK.index);
//      p.setTagName(o.getName());
//      tags.add(p);
//    }
    return tags;
  }

  @Override
  public List getGamePlatFormTag() throws Exception {
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
    PushTagCollection p = new PushTagCollection();
    p.setTagId(Consts.SHOUYOU + "_P");
    p.setTagName("手游");
    tags.add(p);
    PushTagCollection p1 = new PushTagCollection();
    p1.setTagId(Consts.DUANYOU + "_P");
    p1.setTagName("端游");
    tags.add(p1);
    return tags;
  }

  @Override
  public List listGameTag(String name, int cur, int page, COLLECTION_ITEM_TYPE type) throws Exception {
//    Pagination pagination = strategyService.listByName(name, 3, cur, page);
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    List content = pagination.getContent();
//    for(Iterator iterator = content.iterator(); iterator.hasNext();) {
//      Strategy str = (Strategy) iterator.next();
//      PushTagCollection p = new PushTagCollection();
//
//      switch(type) {
//        case LIVE :
//          p.setTagId(str.getGameCode() + "_" + COLLECTION_ITEM_TYPE.LIVE.getIndex());
//          p.setTagName(str.getName() + COLLECTION_ITEM_TYPE.LIVE);
//          break;
//        case GIFT :
//          p.setTagId(str.getGameCode() + "_" + COLLECTION_ITEM_TYPE.GIFT.getIndex());
//          p.setTagName(str.getName() + COLLECTION_ITEM_TYPE.GIFT);
//          break;
//        default :
//          p.setTagId(str.getGameCode() + "");
//          p.setTagName(str.getName());
//          break;
//      }
//      tags.add(p);
//    }
    return tags;
  }

  @Override
  public List listAppTag(Map<String, Object> params) throws Exception {
    List<UserItemOperatePvMongo2> list = clientLogCollectionDao
        .getPVByName("ItemAppPv", 0, 20, params);
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      UserItemOperatePvMongo2 str = (UserItemOperatePvMongo2) iterator.next();
      PushTagCollection p = new PushTagCollection();
      p.setTagId(str.getId().getServiceId());
      p.setTagName(str.getValue().getServiceName());
      tags.add(p);
    }
    return tags;
  }

  @Override
  public List listContent(String name, int cur, int page, COLLECTION_ITEM_TYPE type, CONTENT_SOURCE source)
      throws Exception {
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    switch(type) {
//      case GIFT :
//        selectListGift(name, tags);
//        break;
//      case LIVE :
////        List<VideoLive> lives = videoLiveDao.findLiveListLikeName(name, 0, 20);
//        for(Iterator iterator = lives.iterator(); iterator.hasNext();) {
//          VideoLive li = (VideoLive) iterator.next();
//          PushTagCollection p = new PushTagCollection();
//          p.setTagName(li.getLiveTitle());
//          p.setTagId(li.getLiveRoomId() + "");
//          tags.add(p);
//        }
//        break;
//      case WALKTHROUGH :
//        Pagination pag = strategyService.listToWeb(-1, 3, name, 1, 20);
//        List str = pag.getContent();
//        for(Iterator iterator = str.iterator(); iterator.hasNext();) {
//          Strategy li = (Strategy) iterator.next();
//          PushTagCollection p = new PushTagCollection();
//          p.setTagName(li.getName());
//          p.setTagId(li.getId() + "");
//          tags.add(p);
//        }
//        break; 
//      case NEWS :
//        switch(source) {
//          case STRATEGY_NEWS :
//            List strN = strategyNewsService.listNewsLikeName(name, 1, 20).getContent();
//            for(Iterator iterator = strN.iterator(); iterator.hasNext();) {
//              StrategyNews li = (StrategyNews) iterator.next();
//              PushTagCollection p = new PushTagCollection();
//              p.setTagName(li.getTitle());
//              p.setTagId(li.getId() + "");
//              tags.add(p);
//            }
//            break;
//          case COLUMN_NEWS :
//            List colN = newsService.listNewsLikeTitle(name, 1, 20).getContent();
//            for(Iterator iterator = colN.iterator(); iterator.hasNext();) {
//              News li = (News) iterator.next();
//              PushTagCollection p = new PushTagCollection();
//              p.setTagName(li.getTitle());
//              p.setTagId(li.getId() + "");
//              tags.add(p);
//            }
//            break;
//          case POP_GAME_NEWS :
//            List popN = popGameNewsService.listPopGameNewsLikeTitle(name, 1, 20).getContent();
//            for(Iterator iterator = popN.iterator(); iterator.hasNext();) {
//              PopGameNews li = (PopGameNews) iterator.next();
//              PushTagCollection p = new PushTagCollection();
//              p.setTagName(li.getTitle());
//              p.setTagId(li.getId() + "");
//              tags.add(p);
//            }
//            break;
//          default :
//            break;
//        }
//        break;
//      case VIDEO :
//        switch(source) {
//          case STRATEGY_VIDEO :
//            List strV = strategyVideoService.listVideoLikeTitle(name, 1, 20).getContent();
//            for(Iterator iterator = strV.iterator(); iterator.hasNext();) {
//              StrategyVideo li = (StrategyVideo) iterator.next();
//              PushTagCollection p = new PushTagCollection();
//              p.setTagName(li.getTitle());
//              p.setTagId(li.getId() + "");
//              tags.add(p);
//            }
//            break;
//          case COLUMN_VIDEO :
//            List colV = videoService.listVideoLikeTitle(name, 1, 20).getContent();
//            for(Iterator iterator = colV.iterator(); iterator.hasNext();) {
//              Video li = (Video) iterator.next();
//              PushTagCollection p = new PushTagCollection();
//              p.setTagName(li.getTitle());
//              p.setTagId(li.getId() + "");
//              tags.add(p);
//            }
//            break;
//          case POP_GAME_VIDEO :
//            List popV = popGameVideoService.listPopGameVideoLikeTitle(name, 1, 20).getContent();
//            for(Iterator iterator = popV.iterator(); iterator.hasNext();) {
//              PopGameVideo li = (PopGameVideo) iterator.next();
//              PushTagCollection p = new PushTagCollection();
//              p.setTagName(li.getTitle());
//              p.setTagId(li.getId() + "");
//              tags.add(p);
//            }
//            break;
//          default :
//            break;
//        }
//
//        break;
//      case JIONG :
//        selectListJiong(name, tags);
//        break;
//      case GAME :
//        tags=listGameTag(name, cur, 20, type);
//        break;
//      default :
//        break;
//    }
    return tags;
  }

  private void selectListGift(String name, List<PushTagCollection> tags) throws Exception, JSONException {
    Map<String, String> params = new HashMap<String, String>();
    String result = null;
    if(StringUtils.isEmpty(name)) {
      result = HttpUtil.syncPost(mobile_app_shouyou+"/gift/qianglist?gameType=0&osType=0&typeId=0&page=1&pageSize=20",
          params, null);
    }
    else {
      result = HttpUtil.syncPost(mobile_app_shouyou+"/gift/searchgift?key="+name+"&gameType=0", params, null);
    }
    JSONObject obj = new JSONObject(result);
    JSONArray arr = obj.getJSONObject("data").getJSONArray("list");
    for(int i = 0; i < arr.length(); i++) {
      JSONObject jo = (JSONObject) arr.get(i);
     if("0".equals(jo.getString("qiangShow")))
         continue;
      PushTagCollection p = new PushTagCollection();
      p.setTagName(jo.getString("giftName"));
      p.setTagId(jo.getString("giftId"));
      tags.add(p);
    }
  }

  private void selectListJiong(String name, List<PushTagCollection> tags) throws Exception, JSONException {
    Map<String, String> params = new HashMap<String, String>();
    String url=jiongtu+"/api/backopt/photoslist";
//     url="http://10.6.212.29:8080/jiongtu/api/backopt/photoslist";
    params.put("pageIndex", "0");
    params.put("pageSize", "20");
    if(!StringUtils.isEmpty(name))
      params.put("photosTitle", name);
//    String result = HttpUtil.syncPost(url, params, "gb2312");
    String result = HttpUtil.syncGet(url, params, "UTF-8","UTF-8");
    JSONObject obj = new JSONObject(result);
    JSONArray arr = obj.getJSONArray("result");
    for(int i = 0; i < arr.length(); i++) {
      JSONObject jo = (JSONObject) arr.get(i);
      PushTagCollection p = new PushTagCollection();
      p.setTagName(jo.getString("photosName"));
      p.setTagId(jo.getString("photosId"));
      tags.add(p);
    }
  }
  @Override
  public List getGameCategory(int code) throws Exception {
    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
    Map<String, String> obj = null;
    switch(code) {
      case 1001 :
        obj = getMobileType("info_status");
        break;
      case 1002 :
        obj = getMobileType("info_type");
        break;
      case 1003 :
        obj = getPCType("5");
        break;
      case 1004 :
        obj = getPCType("2");
        break;
      default :
        break;
    }
    iteratorConvert2Tag(tags, obj, code + "");
    return tags;
  }

  @Override
  // @Cacheable(value = "videoMobileCMSCache", key = "'mobileType_' + #type")
  public Map<String, String> getMobileType(String type) throws Exception, JSONException {
    Map<String, String> mobileType = new HashMap<String, String>();
    Map<String, String> p = new HashMap<String, String>();
    String url = game_cate_mobile + "/apis/category/list.html?class_type=" + type;
    String str = HttpUtil.syncPost(url, p, null);
    JSONObject obj = new JSONObject(str).getJSONObject("data");
    Iterator it = obj.keys();
    while(it.hasNext()) {
      String key = (String) it.next();
      JSONObject value = obj.getJSONObject(key);
      mobileType.put(key, value.getString("class_name"));
    }
    return mobileType;
  }

  @Override
  // @Cacheable(value = "videoMobileCMSCache", key = "'pcType_' + #type")
  public Map<String, String> getPCType(String type) throws Exception, JSONException {
    String url = game_cate_pc + "/game/category?game_class=1";
    Map<String, String> pctype = new HashMap<String, String>();
    Map<String, String> p = new HashMap<String, String>();
    String str = HttpUtil.syncPost(url, p, null);
    JSONObject obj = new JSONObject(str).getJSONObject("data").getJSONObject(type).getJSONObject("children");
    Iterator it = obj.keys();
    while(it.hasNext()) {
      String key = (String) it.next();
      JSONObject value = obj.getJSONObject(key);
      pctype.put(key, value.getString("name"));
    }
    return pctype;
  }

  private void iteratorConvert2Tag(List<PushTagCollection> tags, Map<String, String> type, String prifix)
      throws JSONException {
    Iterator iter = type.keySet().iterator();
    while(iter.hasNext()) {
      String id = iter.next().toString();
      String name = type.get(id);
      PushTagCollection tag = new PushTagCollection();
      tag.setTagId(name);
      tag.setTagName(name);
      tags.add(tag);
    }
  }

  /**
   * // @CacheEvict(value = "videoMobileCMSCache", key =
   * "'typeStatus' + #gameCode")
   */
  @Override
  @Cacheable(value = "videoMobileCMSCache", key = "'gameInfo_X_' + #gameCode ")
  public Map<String, String> getGameCodeTypeAndStatus(String gameCode, GAME_PLATFORM_TYPE type) {
    Map<String, String> typeStatus = null;
    if(type == null) {
      typeStatus = mobile(gameCode);
      if(typeStatus != null) {
        typeStatus.put("platForm", "1");
        return typeStatus;
      }
      typeStatus = pc(gameCode);
      if(typeStatus != null) {
        typeStatus.put("platForm", "2");
        return typeStatus;
      }
    }
    else {
      if(type == GAME_PLATFORM_TYPE.MOBILE) {
        return mobile(gameCode);
      }
      else if(type == GAME_PLATFORM_TYPE.PC) {
        return pc(gameCode);
      }
    }
    return typeStatus;
  }

  private Map<String, String> pc(String gameCode) {
    try {
      Map<String, String> p = new HashMap<String, String>();
      Map<String, String> typeStatus;
      typeStatus = new HashMap<String, String>();
      String url = game_cate_pc + "/game/info?game_code=" + gameCode;
      String str = HttpUtil.syncPost(url, p, null);
      if(StringUtils.isEmpty(str)) return null;
      JSONObject obj = new JSONObject(str).getJSONObject("data");
      JSONArray info_status = obj.getJSONArray("game_feature");
      typeStatus.put("name", obj.getString("game_name"));
      JSONArray info_type = obj.getJSONArray("game_type");
      typeStatus.put("status", getStrs(info_status).toString());
      typeStatus.put("type", getStrs(info_type).toString());
      typeStatus.put("gameCode", gameCode);
      return typeStatus;
    }
    catch(Exception e) {
      // e.printStackTrace();
      return null;
    }
  }

  private Map<String, String> mobile(String gameCode) {
    try {
      Map<String, String> p = new HashMap<String, String>();
      Map<String, String> typeStatus;
      typeStatus = new HashMap<String, String>();
      String url = game_cate_mobile + "/apis/game/info?game_code=" + gameCode;
      String str = HttpUtil.syncPost(url, p, null);
      if(StringUtils.isEmpty(str)) return null;
      JSONObject obj = new JSONObject(str).getJSONObject("data").getJSONObject(gameCode);
      JSONArray info_status = obj.getJSONArray("info_status");
      StringBuffer status = getStrs(info_status);
      JSONObject info_type = obj.getJSONObject("info_type");
      typeStatus.put("type", info_type.getString("name"));
      typeStatus.put("status", status.toString());
      typeStatus.put("pkg", obj.getString("info_package"));
      typeStatus.put("name", obj.getString("info_chname"));
      typeStatus.put("gameCode", gameCode);
      return typeStatus;
    }
    catch(Exception e) {
      // e.printStackTrace();
      return null;
    }
  }

  private StringBuffer getStrs(JSONArray info_status) throws JSONException {
    Map<String, String> p = new HashMap<String, String>();
    StringBuffer status = new StringBuffer();
    for(int i = 0; i < info_status.length(); i++) {
      status.append(info_status.getJSONObject(i).getString("name")).append(",");
    }
    if(status.length() != 0) status = status.deleteCharAt(status.length() - 1);
    return status;
  }

  @Override
  public void setThreadTotal(String total) {
    pushTagLogDao.setThreadTotal(Integer.parseInt(total));
  }

  @Override
  public void setSysThreadNum(int num) {
    pushTagLogDao.setSysThreadNum(num);
  }

  @Override
  public void updateWaiting() {
    pushTagLogDao.updateWaiting();
  }

  @Override
  public PushTagExcuteStateInfo getSysThreadNum() {
    return pushTagLogDao.getSysThreadNum();
  }

  @Override
  public Integer getThreadTotal() {
    return pushTagLogDao.getThreadTotal();
  }

  @Override
  public List<PushTagExcuteStateInfo> getThreadNumList() {
    try {
      return pushTagLogDao.getThreadNum("taskExecutor");
    }
    catch(Exception e) {
      return null;
    }
  }

  @Override
  public void delThreadNumList() {
    try {
      pushTagLogDao.removeThreadLog();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

}
