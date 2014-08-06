package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_TAG_STATE;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo2;
import com.cyou.video.mobile.server.cms.model.collection.Value;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.model.user.UserTokenBindXinge;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.cyou.video.mobile.server.cms.service.push.SystemConfigService;
import com.mongodb.WriteResult;
import com.tencent.xinge.TagTokenPair;

/**
 * 意见反馈业务实现
 * 
 * @author jyz
 */
@Component("pushTagXinGe173APPApi")
public class PushTagXinGe173APPApi {

  public Logger logger = LoggerFactory.getLogger(PushTagXinGe173APPApi.class);

  private UserItemOperatePvMongo tagResult;

  private CLIENT_TYPE clientType;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private SystemConfigService systemConfigService;

  @Autowired
  private PushInterface xingePush;

  private int start;

  private int size;

  /**
   * ---------------------不同类型对应tag----------------------
   */
  /**
   * 或观看30s以上
   * 
   * @param id
   * @param value
   * @param baiduId
   * @param clientType
   * @throws Exception
   */
  public boolean video(ClientLogCollection id, ClientLogCollection value) throws Exception {
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
            setGameTag(value, id.getPushToken());
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean live(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.LIVE) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
        if(value.getKeyWord() != null && !"".equals(value.getKeyWord())) {
          long sec = Long.parseLong(value.getKeyWord().trim()) / 1000;
          if(sec > 30) {
            setGameTag(value, id.getPushToken());
            setTag(id.getPushToken(), value.getGameCode() + "_" + COLLECTION_ITEM_TYPE.LIVE.index);// XX游戏直播
            return true;
          }
        }

      }
    }
    return false;
  }

  public void news(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.NEWS) {
      // if (id.getOperatorTypeE() ==
      // COLLECTION_OPERATOR_TYPE.LEAVE_COMMENTS
      // || id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SHARE) {
      // setGameTag(value, baiduId, clientType);
      // }
    }
  }

  public boolean activity(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.ACT_CENTER) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
        if(!StringUtils.isEmpty(value.getServiceName())) setTag(id.getPushToken(), value.getServiceName());
        setTag(id.getPushToken(), "ACT_CENTER");
        return true;
      }
    }
    return false;
  }

  public boolean bySearch(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(id.getOtherWay() == 5) {
      if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH || value.getItemTypeE() == COLLECTION_ITEM_TYPE.VIDEO
          || value.getItemTypeE() == COLLECTION_ITEM_TYPE.NEWS) {
        if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
          return setGameTag(value, id.getPushToken());
        }
        if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SUBSCRIBE) {
          return setGiftTag(value, id.getPushToken());
        }

      }
      if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.GIFT) {
        if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.VIEW) {
          return setGiftTag(value, id.getPushToken());
        }

      }
    }
    return false;
  }

  public boolean byRank(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(id.getOtherWay() == 15) {
      setGameTag(value, id.getPushToken());
      if(!StringUtils.isEmpty(value.getKeyWord())) {
        String rankTag = value.getKeyWord() + "_" + COLLECTION_ITEM_TYPE.RANK.getIndex();
        setTag(id.getPushToken(), rankTag);
        return true;
      }
    }
    return false;
  }

  public boolean jiong(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.PIC) {
      if(!StringUtils.isEmpty(value.getKeyWord()))
        setTag(value.getPushToken(), value.getKeyWord() + "_" + COLLECTION_ITEM_TYPE.JIONG.index);
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
  public boolean hits(ClientLogCollection id, com.cyou.video.mobile.server.cms.model.collection.Value value,
      String baiduId) throws Exception {
    if(value.getPv() >= 3) {
      return setGameTag(value, baiduId);
    }
    return false;
  }

  public void subscribe(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.SUBSCRIBE) {
        setGameTag(value, id.getPushToken());
      }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.CANCEL_SUBSCRIBE) {
        delGameTag(value, id.getPushToken());
      }
    }
  }

  public void top(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.TOP) {
        setGiftTag(value, id.getPushToken());
      }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.CANCEL_TOP) {
        delGiftTag(value, id.getPushToken());
      }
    }
  }

  public boolean desktop(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.WALKTHROUGH) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.DESKTOP) {
        return setGiftTag(value, id.getPushToken());
      }
    }
    return false;
  }

  public void app(ClientLogCollection id, ClientLogCollection value) throws Exception {
    if(value.getItemTypeE() == COLLECTION_ITEM_TYPE.APP) {
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.INSTALL) {
        setGameTag(value, id.getPushToken());
      }
      if(id.getOperatorTypeE() == COLLECTION_OPERATOR_TYPE.UNINSTALL) {
        delGameTag(value, id.getPushToken());
      }
    }
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
  public boolean hits(ClientLogCollection id, Value value) throws Exception {
    if(value.getPv() >= 3) {
      return setGameTag(value, id.getPushToken());
    }
    return false;
  }

  /**
   * ----------------tool--------------------------------
   */
  public void initStartAndSize(int s, int e) {
    long cha = e - s;
    size = Consts.LIST_MAX_SIZE;
    if(size > e) {
      size = (int) cha;
    }
    start = (int) s;
  }

  public boolean continueStartAndSize(int e) {
    // multi thread
    start = start + size;
    if(start >= e) 
      return false;
    if(start + size > e) {
      size = (int) e - start;
    }
    return true;
  }

  public boolean setGiftTag(ClientLogCollection value, String baiduId) throws Exception {
    setGameTag(value, baiduId);
    if(!StringUtils.isEmpty(value.getGameCode()))
      setTag(baiduId, value.getGameCode() + "_" + COLLECTION_ITEM_TYPE.GIFT.index);// gaem
    return true;
  }

  public void delGiftTag(ClientLogCollection value, String baiduId) throws Exception {
    delGameTag(value, baiduId);
    deleteTag(baiduId, value.getGameCode() + "_" + COLLECTION_ITEM_TYPE.GIFT.index);// gaem
                                                                                    // platform
  }

  public void delGameTag(ClientLogCollection value, String baiduId) throws Exception {
    try {
      if(!StringUtils.isEmpty(value.getGameCode())) {
        deleteTag(baiduId, value.getGameCode());// game
        deleteTag(baiduId, value.getGameType() + "");// game
        deleteTag(baiduId, value.getGameStatus() + "");// game
        deleteTag(baiduId, value.getGamePlatForm() + "");// gaem
        // platform
      }
    }
    catch(Exception e) {
      // TODO: handle exception
    }
  }

  public boolean setGameTag(ClientLogCollection value, String baiduId) throws Exception {
    setTag(baiduId, value.getGameCode());// game
    sendMultiStr(value.getGameType(), baiduId); // code
    sendMultiStr(value.getGameStatus(), baiduId);
    if(value.getGamePlatForm() != -1) {
      setTag(baiduId, value.getGamePlatForm() + "_P");// game
    }
    return true;
  }

  public void sendMultiStr(String str, String baiduId) throws Exception {
    if(str.indexOf(",") >= 0) {
      String[] s = str.split(",");
      for(int i = 0; i < s.length; i++) {
        setTag(baiduId, s[i]);// game
      }
    }
    else {
      setTag(baiduId, str);// game
    }
  }

  public void setTag(String uid, String tag) {
    TagTokenPair t = new TagTokenPair(tag, uid);
    if(clientType == CLIENT_TYPE.IOS)
      tagResult.addIosTag(t);
    else
      tagResult.addAndroidTag(t);
  }

  public void deleteTag(String uid, String tag) {
    TagTokenPair t = new TagTokenPair(tag, uid);
    if(clientType == CLIENT_TYPE.IOS)
      tagResult.addIosDelTag(t);
    else
      tagResult.addAndroidDelTag(t);
  }

  public void inc(String threadName, int setNum) {
    int incc = 50;
    if(setNum != 0 && setNum % incc == 0) {
      mongoTemplate.findAndModify(new Query(new Criteria("threadName").is(threadName)),
          new Update().inc("threadNum", setNum), FindAndModifyOptions.options().upsert(true),
          PushTagExcuteStateInfo.class, "PushTagThreadInfo");
    }
  }

  public void successLogEnd(String c, int total) {
    List<PushTagExcuteStateInfo> o1 = (List) mongoTemplate.find(new Query().addCriteria(new Criteria("name").is(c)),
        PushTagExcuteStateInfo.class);
    PushTagExcuteStateInfo excuteStateInfo = o1.get(0);
    excuteStateInfo.setFinishThreadNum(excuteStateInfo.getFinishThreadNum() + 1);
    excuteStateInfo.setSize(excuteStateInfo.getSize() + total);
    excuteStateInfo.setLastUpdate(new Date());
    if(excuteStateInfo.getThreadNum() == excuteStateInfo.getFinishThreadNum())
      excuteStateInfo.setState(PUSH_SEND_TAG_STATE.WAITING);
    mongoTemplate.save(excuteStateInfo);
  }
  /**
   * ----------------mongotemplate------------------
   */
  
  public List<UserItemOperatePvMongo2> getUserItemOperatePvMongo2(Query query, String name) {
    query.limit(size);
    query.skip(start);
    return mongoTemplate.find(query, UserItemOperatePvMongo2.class, name);
  }
  
  public int updateTagStateInMongoBatch(ClientLogCollection id, int state, String collecitonName) {
    try {
      if(Consts.COLLECTION_USER_GAME_PV.equals(collecitonName)) {
        WriteResult wr = mongoTemplate.updateFirst(
            new Query(Criteria.where("_id.uid").is(id.getUid()).and("_id.gameCode").is(id.getGameCode())),
            Update.update("value.state", state), collecitonName);
        return wr.getN();
      }
      if(Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME.equals(collecitonName)) {
        WriteResult wr = mongoTemplate
            .updateFirst(new Query(Criteria.where("_id.uid").is(id.getUid()).and("_id.serviceId").is(id.getServiceId())
                .and("_id.operatorType").is(id.getOperatorType())), Update.update("value.state", state), collecitonName);
        return wr.getN();
      }
      return 0;
    }
    catch(Exception e) {

      return 0;
    }
  }

  /**
   * 批量收集标签发送tag给信鸽
   * 
   * @throws Exception
   */
  public void batchTags() throws Exception {
    Push p = new Push();
    String appId = systemConfigService.getSystemConfigByConfigKey("sys_173app_id");
    if(!StringUtils.isEmpty(appId)) {
      UserItemOperatePvMongo tagResult = getTagResult();
      p.setAppId(Integer.parseInt(appId));
      p.setClientType(CLIENT_TYPE.IOS);
      xingePush.setTagByXinge(tagResult.getIosTags(), p);
      xingePush.delTagByXinge(tagResult.getIosDelTags(), p);
      p.setClientType(CLIENT_TYPE.ANDROID);
      xingePush.setTagByXinge(tagResult.getAndroidTags(), p);
      xingePush.delTagByXinge(tagResult.getAndroidDelTags(), p);
    }
  }

  /**
   * 信鸽token
   * 
   * @param n
   * @return
   */
  public String getXGToken(UserToken n) {
    List<UserTokenBindXinge> xgToken = mongoTemplate.find(new Query(new Criteria("tokenId").is(n.getId())),
        UserTokenBindXinge.class);
    String xingeToken = null;
    if(!xgToken.isEmpty()) {
      xingeToken = xgToken.get(0).getXgToken();
    }
    logger.debug("xingeToken is " + xingeToken);
    return xingeToken;
  }

  public void initClientType( UserToken n) {
    if(n.getPlat()==6)
      setClientType(CLIENT_TYPE.IOS);
    else
      setClientType(CLIENT_TYPE.ANDROID);
  }

  
  /**
   * -------------------------------------------------get and
   * set------------------------------------------------------
   */
  public UserItemOperatePvMongo getTagResult() {
    return tagResult;
  }

  public void newTagResult() {
    this.tagResult = new UserItemOperatePvMongo();
  }

  public void setTagResult(UserItemOperatePvMongo tagResult) {
    this.tagResult = tagResult;
  }

  public CLIENT_TYPE getClientType() {
    return clientType;
  }

  public void setClientType(CLIENT_TYPE clientType) {
    this.clientType = clientType;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

}
