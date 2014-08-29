package com.cyou.video.mobile.server.cms.service.collection.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

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
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.common.RedisTemplate;
import com.cyou.video.mobile.server.cms.service.push.impl.PushTagXinGe173APPApi;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.HttpUtil;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;

/**
 * 
 * @author lusi
 * 
 */
@Service("clientLogCollectionService")
public class ClientLogCollectionServiceImpl implements ClientLogCollectionService {

  @Resource(name = "redisTemplate")
  private RedisTemplate redisTemplate;

  @Value("${jiongtu}")
  private String jiongtu;

  private Logger LOGGER = LoggerFactory.getLogger(ClientLogCollectionServiceImpl.class);

  @Value("${game.mobile}")
  private String game_cate_mobile;

  @Autowired
  private MongoOperations mongoTemplate;

  @Autowired
  PushTagXinGe173APPApi pushTagXinGe173APPApi;

  /**
   * 测试类暂时使用
   */
  @Override
  public int collectLogInfo(List collections) {
    mongoTemplate.insertAll(collections);
    return collections.size();
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
        gameInfo(opType, token, result, code + "", pk.get(j));
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
            ClientLogCollection c = gameInfo(opType, token, result, code, sb.toString());
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
      String code, String pkg) {
    ClientLogCollection c = new ClientLogCollection();
    try {
      c.setGameCode(code);
      // 类型
      Map<String, String> typeSt = pushTagXinGe173APPApi.getGameCodeTypeAndStatus(c.getGameCode(), GAME_PLATFORM_TYPE.MOBILE);
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
    return mongoTemplate.find(new Query(), StatisticJobLastUpdateTime.class);
  }

  @Override
  public List getPushTagExcuteStateInfo() {
    Query q = new Query();
    q.with(new Sort(Direction.DESC, "lastUpdate"));
    q.limit(100);
    return mongoTemplate.find(q, PushTagExcuteStateInfo.class);
  }

  @Override
  public Pagination getClientLogCollection(Map<String, Object> params) throws Exception {
    Pagination pagination = null;
    pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize =Integer.parseInt(params.get("limit").toString());
    pagination.setCurPage(curPage);
    pagination.setPageSize(pageSize);
    List<ClientLogCollection> list = getClientCollection(null, curPage, pageSize, params);
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

  private List<ClientLogCollection> getClientCollection(ClientLogCollection collections, int page, int size,
      Map<String, Object> params) {
    Query query = new Query();
    query.limit(size);
    query.skip(page);
    query.with(new Sort(Sort.Direction.DESC, "uploadDate"));
    query(params, query, "");
    return mongoTemplate.find(query, ClientLogCollection.class);
  }

  private void query(Map<String, Object> params, Query query, String tag) {
    if(params.get("dateFrom") != null) {
      if(params.get("dateTo") == null) {
        query.addCriteria(Criteria.where("operatorDate").lte(new Date())
            .gte(Constants.parseDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), params.get("dateFrom").toString())));

      }
      else
        query.addCriteria(Criteria.where("operatorDate")
            .lte(Constants.parseDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), params.get("dateTo").toString()))
            .gte(Constants.parseDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), params.get("dateFrom").toString())));
    }
    if(params.get("dFrom") != null) {
      if(params.get("dTo") == null) {
        query.addCriteria(Criteria.where("uploadDate").lte(new Date())
            .gte(Constants.parseDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), params.get("dFrom").toString())));

      }
      else
        query.addCriteria(Criteria.where("uploadDate")
            .lte(Constants.parseDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), params.get("dTo").toString()))
            .gte(Constants.parseDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), params.get("dFrom").toString())));
    }
    if(params.get("userToken") != null)
      query.addCriteria(Criteria.where(tag + "uid").is(params.get("userToken").toString()));

    if(params.get("itemType") != null && !"999".equals(params.get("itemType"))) {
      if("id.".equals(tag)) tag = "value.";
      query.addCriteria(Criteria.where(tag + "itemType").is(params.get("itemType").toString()));
    }

    if(params.get("operatorType") != null && !"999".equals(params.get("operatorType"))) {
      // if ("id.".equals(tag))
      // tag = "value.";
      query.addCriteria(Criteria.where(tag + "operatorType").is(params.get("operatorType").toString()));
    }

    if(params.get("serviceId") != null) {
      Pattern pattern = Pattern.compile("^.*" + params.get("serviceId").toString() + ".*$");
      query.addCriteria(Criteria.where(tag + "serviceId").regex(pattern));
    }
    else if(params.get("serviceId2") != null)
      query.addCriteria(Criteria.where(tag + "serviceId").is(params.get("serviceId2").toString()));

    if(params.get("serviceName") != null) {
      if("id.".equals(tag)) tag = "value.";
      Pattern pattern = Pattern.compile("^.*" + params.get("serviceName").toString() + ".*$");
      query.addCriteria(Criteria.where(tag + "serviceName").regex(pattern));
    }
    else if(params.get("serviceName2") != null) {
      if("id.".equals(tag)) tag = "value.";
      query.addCriteria(Criteria.where(tag + "serviceName").is(params.get("serviceName2").toString()));

    }
    if(params.get("gameCode") != null) {
      query.addCriteria(Criteria.where("value.gameCode").is(params.get("gameCode").toString()));
    }

    if(params.get("keyWord") != null) {
      Pattern pattern = Pattern.compile("^.*" + params.get("keyWord").toString() + ".*$");
      if(Consts.COLLECTION_KEYWORD_PV_NAME.equals(params.get("cname"))) tag = "id.";
      query.addCriteria(Criteria.where(tag + "keyWord").regex(pattern));
    }

    if(params.get("otherWay") != null && !"999".equals(params.get("otherWay"))) {
      query.addCriteria(Criteria.where("otherWay").is(Integer.parseInt(params.get("otherWay").toString())));
    }

  }

  @Override
  public int getTotalNum(String collectionName) throws Exception {
    Map<String, Object> params = new HashMap<String, Object>();
    Query query = new Query();
    query(params, query, "");
    return (int) mongoTemplate.count(query, collectionName);
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
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize =Integer.parseInt(params.get("limit").toString());
    pagination.setCurPage(curPage);
    String collectionName = params.get("collectionName").toString();
    List<UserItemOperatePvMongo2> list = getPVByName(collectionName, curPage, pageSize, params);
    List<ClientLogCollection> result=new ArrayList<ClientLogCollection>();
    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      UserItemOperatePvMongo2 userItemOperatePvMongo2 = (UserItemOperatePvMongo2) iterator.next();
      ClientLogCollection clientLogCollection = userItemOperatePvMongo2.getValue();
      ClientLogCollection id = userItemOperatePvMongo2.getId();
      if(clientLogCollection.getGamePlatForm() != null && clientLogCollection.getGamePlatForm() > 0) {
        setGameType(clientLogCollection);
      }
      if(id.getOtherWay() != null && id.getOtherWay() != -1)
        id.setOtherWayE(COLLECTION_ITEM_TYPE.values()[id.getOtherWay()].getName());
      clientLogCollection.setServiceId(id.getServiceId());
      clientLogCollection.setOperatorType(clientLogCollection.getOperatorType()==null?id.getOperatorType():clientLogCollection.getOperatorType());
      clientLogCollection.setOtherWay(clientLogCollection.getOtherWay()==null?id.getOtherWay():clientLogCollection.getOtherWay());
      clientLogCollection.setUid(clientLogCollection.getUid()==null?id.getUid():clientLogCollection.getUid());
      clientLogCollection.setKeyWord(clientLogCollection.getKeyWord()==null?id.getKeyWord():clientLogCollection.getKeyWord());
      result.add(clientLogCollection);
    }
    pagination.setRowCount(9999);
    pagination.setContent(result);
    return pagination;
  }

  private List getPVByName(String collectionName, int page, int size, Map<String, Object> params) {
    Query query = new Query();
    query.limit(size);
    query.skip(page);
    query.with(new Sort(Sort.Direction.DESC, "value.pv"));

    query(params, query, "id.");
    if(params.get("tagType") != null && "1".equals(params.get("tagType")))
      query.addCriteria(new Criteria().where("_id.operatorType").ne("0"));
    return mongoTemplate.find(query, UserItemOperatePvMongo2.class, collectionName);
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
    List<UserItemOperatePvMongo2> list = getPVByName(collectionName, curPage, pageSize, params);

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
        // p.setTagId(pushTagService.makeUserTagId(userItemOperatePvMongo2.getId(),
        // userItemOperatePvMongo2.getValue()));
        // p.setTagName(pushTagService.makeUserTagName(userItemOperatePvMongo2.getId(),
        // userItemOperatePvMongo2.getValue()));
      }

      pushs.add(p);
    }
    pagination.setRowCount(1000);

    pagination.setContent(pushs);
    return pagination;
  }

  @Override
  public List<StatisticJobLastUpdateTime> getPVLastUpdateTime(Map<String, Object> params) throws Exception {
    return mongoTemplate.find(new Query(), StatisticJobLastUpdateTime.class);
  }

  @Override
  public Long getCount(String name) {
    return mongoTemplate.count(new Query(), name);
  }

  @Override
  public Long getCount(Query query, String name) {
    return mongoTemplate.count(new Query(), name);
  }

  @Override
  public int updatePushTagLastUpdateTime(PushTagLastUpdateTime tagLastUpdateTime) {
    try {
      mongoTemplate.remove(new Query(Criteria.where("name").is(tagLastUpdateTime.getName())),
          PushTagLastUpdateTime.class);
      mongoTemplate.insert(tagLastUpdateTime);
      return 1;
    }
    catch(Exception e) {
      return 0;
    }
  }

  @Override
  public PushTagLastUpdateTime getPushTagLastUpdateTime(String collectionName) {
    PushTagLastUpdateTime lastUpdateTimeBean = mongoTemplate.findOne(new Query(Criteria.where("name")
        .is(collectionName)), PushTagLastUpdateTime.class);
    return lastUpdateTimeBean;
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
      UserToken userToken = null;// userTokenService.getToken(params.get("token-self"));
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
        if(xinge.size() <= 0) mongoTemplate.insert(bindXinge);
      }
    }
  }

}
