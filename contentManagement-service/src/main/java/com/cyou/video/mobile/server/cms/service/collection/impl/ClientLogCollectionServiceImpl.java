package com.cyou.video.mobile.server.cms.service.collection.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.GAME_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo2;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.user.UserToken;
import com.cyou.video.mobile.server.cms.model.user.UserTokenBindXinge;
import com.cyou.video.mobile.server.cms.mongo.dao.collection.ClientLogCollectionDao;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.common.RedisTemplate;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;
import com.cyou.video.mobile.server.common.utils.HttpUtil;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;

/**
 * 
 * @author lusi
 * 
 */
@Service("clientLogCollectionService")
public class ClientLogCollectionServiceImpl implements ClientLogCollectionService {

  private ClientLogCollectionDao clientLogCollectionDao;

  @Autowired
  private PushTagService pushTagService;

//  @Autowired
//  private UserTokenService userTokenService;

  @Resource(name = "redisTemplate")
  private RedisTemplate redisTemplate;

  @Value("${jiongtu}")
  private String jiongtu;

  private Logger LOGGER = LoggerFactory.getLogger(ClientLogCollectionServiceImpl.class);

  @Value("${game.mobile}")
  private String game_cate_mobile;

  @Autowired
  private MongoOperations mongoTemplate;

  /**
   * 测试类暂时使用
   */
  @Override
  public int collectLogInfo(List collections) {
    return clientLogCollectionDao.insertBatch(collections);
  }

  @Override
  public List<ClientLogCollection> gameAppFilter(String pkg, COLLECTION_OPERATOR_TYPE opType, String token) {
    List<ClientLogCollection> result = new ArrayList<ClientLogCollection>();
    // get from redis
    List<String> pk = Arrays.asList(pkg.split(","));
    StringBuffer sb = new StringBuffer();
    for(int j = 0; j < pk.size(); j++) {
      // this.delPkgStatus(pk.get(j));
      int code = this.getPkgStatus(pk.get(j));// 缓存一层
      if(code > 1) {
        gameInfo(opType, token, result, code + "",pk.get(j));
      }
      else if(code == 0) {
        sb.append(pk.get(j)).append(",");
      }
    }
    if(sb.length() != 0) {
      sb = sb.delete(sb.length() - 1, sb.length());
      pk = new ArrayList<String>(Arrays.asList(sb.toString().split(",")));
      mobileGameByClass(opType, token, result, pk, sb, "1");
      mobileGameByClass(opType, token, result, pk, sb, "3");
    }
    return result;

  }

  private void mobileGameByClass(COLLECTION_OPERATOR_TYPE opType, String token, List<ClientLogCollection> result,
      List<String> pk, StringBuffer sb, String clas) {
    try {
      String str = getMobileGameList(sb.toString(), clas);
      if(!StringUtils.isEmpty(str)) {
        JSONObject obj = new JSONObject(str);
        if(!obj.isNull("data")) {
          JSONArray arr = obj.getJSONObject("data").getJSONArray("download_list");
          for(int i = 0; i < arr.length(); i++) {
            JSONObject o = (JSONObject) arr.get(i);
            String code = o.getString("game_code");
            // set info
            ClientLogCollection c = gameInfo(opType, token, result, code,sb.toString());
            if(StringUtils.isEmpty(c.getServiceId())) {
              c.setServiceId(o.getString("info_package"));
            }
            // 缓存
            if(c != null) {
              this.setPkgStatus(c.getServiceId(), c.getGameCode());
              // 去除已缓存
              if(pk.contains(c.getServiceId())) {
                pk.remove(c.getServiceId());
              }
            }
          }
        }
      }
      for(int i = 0; i < pk.size(); i++) {
        this.setPkgStatus(pk.get(i), "1");
      }
    }
    catch(Exception e1) {
      LOGGER.error("gameAppFilter -----------  e: " + e1.getMessage());
    }
  }

  private ClientLogCollection gameInfo(COLLECTION_OPERATOR_TYPE opType, String token, List<ClientLogCollection> result,
      String code,String pkg) {
    ClientLogCollection c = new ClientLogCollection();
    try {
      c.setGameCode(code);
      // 类型
      Map<String, String> typeSt = pushTagService.getGameCodeTypeAndStatus(c.getGameCode(), GAME_PLATFORM_TYPE.MOBILE);
      c.setGameCode(typeSt.get("gameCode"));
      if(typeSt != null) {
        c.setGameType(typeSt.get("type"));
        c.setGameStatus(typeSt.get("status"));
        c.setServiceId(pkg);
        c.setServiceName(typeSt.get("name"));
      }
      c.setGamePlatForm(GAME_PLATFORM_TYPE.MOBILE.index);
      c.setOperatorTypeE(opType);
      c.setItemTypeE(COLLECTION_ITEM_TYPE.APP);
      c.setUid(token);
      c.setKeyWord("");
      c.setKeyWord2("");
      c.setOtherWay(-1);
      c.setOperatorDate(c.getUploadDate());
      result.add(c);
    }
    catch(Exception e) {
      return null;
    }
    return c;
  }

  private String getMobileGameList(String pkg, String clas) throws Exception {
    Map<String, String> p = new HashMap<String, String>();
    p.put("pac_name", pkg);// ?pac_name=com.tencent.game.rhythmmaster
    p.put("class", clas);
    String str = HttpUtil.syncPost(game_cate_mobile + "/apis/game/downloads.html", p, null);
    return str;
  }

  @Override
  public List<StatisticJobLastUpdateTime> getStatisticJobLastUpdateTime() {

    return clientLogCollectionDao.getStatisticJobLastUpdateTime();
  }

  @Override
  public List getPushTagExcuteStateInfo() {
    Query q = new Query();
    q.with(new Sort(Direction.DESC, "lastUpdate"));
    q.limit(100);
    return clientLogCollectionDao.findByCondition(PushTagExcuteStateInfo.class, q);
  }

  @Override
  public void statisticsPv() {
    clientLogCollectionDao.statisticsPv();
  }

  @Override
  public int collectLogInfoS(List<LinkedHashMap<String, String>> collections) {
    int i = clientLogCollectionDao.insertBatchS(collections);
    return 0;
  }

  @Override
  public Pagination getClientLogCollection(Map<String, Object> params) throws Exception {
    Pagination pagination = null;
    pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("curPage").toString());
    int pageSize = Integer.parseInt(params.get("pageSize").toString());
    pagination.setCurPage(curPage);
    pagination.setPageSize(pageSize);
    curPage = (curPage - 1) * pageSize;
    params.remove("curPage");
    params.put("curPage", curPage);
    List<ClientLogCollection> list = clientLogCollectionDao.getClientCollection(null, curPage, pageSize, params);
    pagination.setRowCount(9999);
    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      ClientLogCollection clientLogCollection = (ClientLogCollection) iterator.next();
      if(clientLogCollection.getGamePlatForm() != null && clientLogCollection.getGamePlatForm() > 0) {
        clientLogCollection.setGamePlatFormE(GAME_PLATFORM_TYPE.values()[clientLogCollection.getGamePlatForm() - 1]
            .getName());
        setGameType(clientLogCollection);

      }
      if(clientLogCollection.getOtherWay() != null && clientLogCollection.getOtherWay() != -1)
        clientLogCollection.setOtherWayE(COLLECTION_ITEM_TYPE.values()[clientLogCollection.getOtherWay()].getName());
    }
    pagination.setContent(list);
    return pagination;
  }

  @Override
  public int getTotalNum(String collectionName) throws Exception {
    Map<String, Object> params = new HashMap<String, Object>();
    return (int) clientLogCollectionDao.getPVCountByName(collectionName, params, "");
  }

  /**
   * 囧途方法 如果慢需要优化
   * 
   * @param id
   * @param type
   * @param serviceName
   * @return
   * @throws Exception
   */
  private StringBuffer setJionTuName(String id, String type, StringBuffer serviceName) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("json", "[{ \"id\":\"" + id + "\",\"type\":\"" + type + "\"}]");
    String result;
    try {
      result = HttpUtil.syncGet(jiongtu + "api/backopt/photoinfo", params, "UTF-8", "UTF-8");

      Map<String, String> map = JacksonUtil.getJsonMapper().readValue(result, Map.class);
      if(map.get(id + "_" + type) != null) serviceName.append(map.get(id + "_" + type));
    }
    catch(Exception e) {
      LOGGER.info("jiong tu exception : " + e.getMessage());
      serviceName.append("囧图未取到");
    }
    return serviceName;
  }

  @Override
  public Pagination getPVByName(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("curPage").toString());
    int pageSize = Pagination.PAGESIZE;
    if(params.get("pageSize") != null) {
      pageSize = Integer.parseInt(params.get("pageSize").toString());
    }
    pagination.setCurPage(curPage);
    pagination.setPageSize(pageSize);
    curPage = (curPage - 1) * pageSize;
    params.remove("curPage");
    params.put("curPage", curPage);
    String collectionName = params.get("collectionName").toString();
    List<UserItemOperatePvMongo2> list = clientLogCollectionDao.getPVByName(collectionName, curPage, pageSize, params);
    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      UserItemOperatePvMongo2 userItemOperatePvMongo2 = (UserItemOperatePvMongo2) iterator.next();
      ClientLogCollection clientLogCollection = userItemOperatePvMongo2.getValue();
      ClientLogCollection id = userItemOperatePvMongo2.getId();
      if(clientLogCollection.getGamePlatForm() != null && clientLogCollection.getGamePlatForm() > 0) {
        setGameType(clientLogCollection);
      }
      if(id.getOtherWay() != null && id.getOtherWay() != -1)
        id.setOtherWayE(COLLECTION_ITEM_TYPE.values()[id.getOtherWay()].getName());
    }
    pagination.setRowCount(9999);
    pagination.setContent(list);
    return pagination;
  }

  private void setGameType(ClientLogCollection clientLogCollection) throws Exception, JSONException {
    clientLogCollection.setGamePlatFormE(GAME_PLATFORM_TYPE.values()[clientLogCollection.getGamePlatForm() - 1]
        .getName());
    // if (GAME_PLATFORM_TYPE.values()[clientLogCollection.getGamePlatForm()
    // - 1] == GAME_PLATFORM_TYPE.MOBILE) {
    // clientLogCollection.setGameStatus(pushTagService.getMobileType(
    // "info_status")
    // .get(clientLogCollection.getGameStatus() + ""));
    // clientLogCollection.setGameType(pushTagService.getMobileType(
    // "info_type").get(clientLogCollection.getGameType() + ""));
    // }
    // if (GAME_PLATFORM_TYPE.values()[clientLogCollection.getGamePlatForm()
    // - 1] == GAME_PLATFORM_TYPE.PC) {
    // clientLogCollection.setGameStatus(pushTagService.getPCType("5")
    // .get(clientLogCollection.getGameStatus() + ""));
    // clientLogCollection.setGameType(pushTagService.getPCType("2").get(
    // clientLogCollection.getGameType() + ""));
    // }
  }

  @Override
  public List<UserItemOperatePvMongo> getUserIdByTag(Map<String, Object> params) throws Exception {
    int curPage = Integer.parseInt(params.get("curPage").toString());
    int pageSize = Pagination.PAGESIZE;
    if(params.get("pageSize") != null) pageSize = Integer.parseInt(params.get("pageSize").toString());
    String collectionName = params.get("collectionName").toString();
    curPage = (curPage - 1) * pageSize;
    params.remove("curPage");
    params.put("curPage", curPage);
    List<UserItemOperatePvMongo> list = clientLogCollectionDao.getPVByName(collectionName, curPage, pageSize, params);

    return list;
  }

  @Override
  public Pagination getTagNameAndPV(Map<String, Object> params) throws Exception {
    Pagination pagination = null;
    pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("curPage").toString());
    int pageSize = Pagination.PAGESIZE;
    if(params.get("pageSize") != null) pageSize = Integer.parseInt(params.get("pageSize").toString());
    String collectionName = params.get("cname").toString();
    pagination.setCurPage(curPage);
    pagination.setPageSize(pageSize);
    curPage = (curPage - 1) * pageSize;
    params.remove("curPage");
    params.put("curPage", curPage);
    if(Consts.COLLECTION_KEYWORD_PV_NAME.equals(collectionName)) params.put("keyWord", params.remove("serviceName"));
    List<PushTagCollection> pushs = new ArrayList<PushTagCollection>();
    List<UserItemOperatePvMongo2> list = clientLogCollectionDao.getPVByName(collectionName, curPage, pageSize, params);

    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      UserItemOperatePvMongo2 userItemOperatePvMongo2 = (UserItemOperatePvMongo2) iterator.next();
      PushTagCollection p = new PushTagCollection();
      // p.setPv(userItemOperatePvMongo2.getValue().getPv());
      if(Consts.COLLECTION_ITEM_PV_NAME.equals(collectionName)) {
        p.setTagName(userItemOperatePvMongo2.getValue().getServiceName());
        p.setTagId(userItemOperatePvMongo2.getId().getServiceId());
      }
      else if(Consts.COLLECTION_KEYWORD_PV_NAME.equals(collectionName)) {
        p.setTagName(userItemOperatePvMongo2.getId().getKeyWord());
        p.setTagId(userItemOperatePvMongo2.getId().getKeyWord());
      }
      else {
        p.setTagId(pushTagService.makeUserTagId(userItemOperatePvMongo2.getId(), userItemOperatePvMongo2.getValue()));
        p.setTagName(pushTagService.makeUserTagName(userItemOperatePvMongo2.getId(), userItemOperatePvMongo2.getValue()));
      }

      pushs.add(p);
    }
    pagination.setRowCount(1000);

    pagination.setContent(pushs);
    return pagination;
  }

  @Override
  public void getTagName(ClientLogCollection clientLogCollection) throws Exception {
//    StringBuffer serviceName = new StringBuffer();
//    String[] serviceId = clientLogCollection.getServiceId().split(",");
//    String sid = serviceId[serviceId.length - 1].trim();
//
//    try {
//      if(Consts.COLLECTION_MY_SUBSCRIBE.equals(sid))
//        serviceName.append("我的订阅");
//      else
//        switch(clientLogCollection.getItemTypeE()) {
//          case VIDEO :
//            Video video = videoService.getVideo(Integer.parseInt(sid));
//            if(video != null)
//              serviceName.append(video.getTitle());
//            else
//              serviceName.append("未查询到");
//            break;
//
//          case COLUMN :
//            Column column = columnService.getColumn(Integer.parseInt(sid));
//            if(column != null)
//              serviceName.append(column.getName());
//            else
//              serviceName.append("未查询到");
//            break;
//          case NEWS :
//
//            News news = newsInfoService.getNews(Integer.parseInt(sid));
//            if(news != null)
//              serviceName.append(news.getTitle());
//            else
//              serviceName.append("未查询到");
//            break;
//
//          case JIONG :
//            this.setJionTuName(sid, "SECTION", serviceName);
//            break;
//          case PIC :
//            this.setJionTuName(sid, "PHOTOS", serviceName);
//            break;
//          default :
//            serviceName.append("未查询到");
//            break;
//        }
//
//    }
//    catch(Exception e) {
//      LOGGER.info("getClientLogCollection : Error getting serviceName,exception msg: " + e.getMessage());
//      serviceName.append("未查询到");
//    }
//
//    clientLogCollection.setServiceName(serviceName.toString());
  }

  @Override
  public List<StatisticJobLastUpdateTime> getPVLastUpdateTime(Map<String, Object> params) throws Exception {
    return clientLogCollectionDao.getPVLastUpdate(Consts.COLLECTION_USER_ITEM_OPERATE_PV_NAME, params);
  }

  @Override
  public Long getCount(String name) {

    return clientLogCollectionDao.getCount(name);
  }

  @Override
  public Long getCount(Query query, String name) {

    return clientLogCollectionDao.getCountByQuery(query, name);
  }

  @Override
  public int updatePushTagLastUpdateTime(PushTagLastUpdateTime tagLastUpdateTime) {
    return clientLogCollectionDao.updatePushTagLastUpdateTime(tagLastUpdateTime);

  }

  @Override
  public PushTagLastUpdateTime getPushTagLastUpdateTime(String collectionName) {
    return clientLogCollectionDao.getPushTagLastUpdateTime(collectionName);
  }

  public void setPkgStatus(String pkg, String flag) {
    Jedis jedis = null;
    try {
      jedis = redisTemplate.jedis();
      jedis.hset("android_installed_game", "pkg:" + pkg, flag + "");
    }
    catch(JedisConnectionException e) {
      if(null != jedis) {
        redisTemplate.returnBrokenResource(jedis);
        jedis = null;
      }
    }
    finally {
      if(null != jedis) {
        redisTemplate.returnResource(jedis);
      }
    }

  }

  public int getPkgStatus(String pkg) {
    Jedis jedis = null;
    try {
      jedis = redisTemplate.jedis();
      String v = jedis.hget("android_installed_game", "pkg:" + pkg);
      if(StringUtils.isEmpty(v)) {
        return 0;
      }
      else {
        return Integer.parseInt(v);
      }
    }
    catch(JedisConnectionException e) {
      if(null != jedis) {
        redisTemplate.returnBrokenResource(jedis);
        jedis = null;
      }
      return 0;
    }
    catch(Exception e) {
      return 0;
    }
    finally {
      if(null != jedis) {
        redisTemplate.returnResource(jedis);
      }
    }
  }

  @Override
  public void bindUserId(Map<String, String> params) throws Exception {
    if(StringUtils.isEmpty(params.get("token-self"))) {
      LOGGER.info("token is null bind baidu id failed!!!!! " + params.get("token-self"));
    }
    else {
      UserToken userToken = null;//userTokenService.getToken(params.get("token-self"));
      if(userToken == null) {
        LOGGER.error(" userToken is null !!!!! ");
      }
      else {
        UserTokenBindXinge bindXinge = new UserTokenBindXinge();
        bindXinge.setTokenId(userToken.getId());
        if(StringUtils.isEmpty(params.get("accessId")))
          bindXinge.setAccessId(0l);
        else
          bindXinge.setAccessId(Long.parseLong(params.get("accessId")));
        bindXinge.setDeviceId(params.get("deviceId"));
        bindXinge.setAccount(params.get("account"));
        bindXinge.setTicket(params.get("ticket"));
        bindXinge.setTicketType(params.get("ticketType"));
        bindXinge.setXgToken(params.get("token"));
        bindXinge.setAppId(params.get("appId"));
        Query query = new Query();
        query.addCriteria(new Criteria("tokenId").is(bindXinge.getTokenId()));
        List<UserTokenBindXinge> xinge = mongoTemplate.find(query, UserTokenBindXinge.class);
        if(xinge.size() <= 0) 
          mongoTemplate.insert(bindXinge);
      }
    }
  }

}
