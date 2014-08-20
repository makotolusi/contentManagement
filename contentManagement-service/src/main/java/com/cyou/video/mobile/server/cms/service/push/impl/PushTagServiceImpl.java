package com.cyou.video.mobile.server.cms.service.push.impl;

import org.springframework.stereotype.Service;

/**
 * 意见反馈业务实现
 * 
 * @author jyz
 */
@Service("pushTagService")
public class PushTagServiceImpl {//implements PushTagService {
//
//  private Logger logger = LoggerFactory.getLogger(PushTagServiceImpl.class);
//
//  @Autowired
//  private MongoOperations mongoTemplate;
//
//  @Override
//  public List getJiong() throws Exception {
//    Map<String, String> params = new HashMap<String, String>();
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    String result = HttpUtil.syncGet(jiongtu + "/api/section/list", params, "UTF-8", "UTF-8");
//    JSONObject obj = new JSONObject(result);
//    JSONArray arr = obj.getJSONObject("Data").getJSONArray("List");
//    for(int i = 0; i < arr.length(); i++) {
//      JSONObject o = arr.getJSONObject(i);
//      PushTagCollection p = new PushTagCollection();
//      p.setTagId(o.getString("ID") + "_" + COLLECTION_ITEM_TYPE.JIONG.index);
//      p.setTagName(o.getString("Title"));
//      tags.add(p);
//    }
//    return tags;
//  }
//
//  @Override
//  public List getRankTag() throws Exception {
//    Map<String, String> params = new HashMap<String, String>();
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    // List<PopGameTopCate> ranks = popGameTopService.listTopCate(1);
//    // for(Iterator iterator = ranks.iterator(); iterator.hasNext();) {
//    // PopGameTopCate o = (PopGameTopCate) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagId(o.getId() + "_" + COLLECTION_ITEM_TYPE.RANK.index);
//    // p.setTagName(o.getName());
//    // tags.add(p);
//    // }
//    return tags;
//  }
//
//  @Override
//  public List getGamePlatFormTag() throws Exception {
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    PushTagCollection p = new PushTagCollection();
//    p.setTagId(Consts.SHOUYOU + "_P");
//    p.setTagName("手游");
//    tags.add(p);
//    PushTagCollection p1 = new PushTagCollection();
//    p1.setTagId(Consts.DUANYOU + "_P");
//    p1.setTagName("端游");
//    tags.add(p1);
//    return tags;
//  }
//
//  @Override
//  public List listTag(String tagIndex, int cur, int page, COLLECTION_ITEM_TYPE type) throws Exception {
//    mongoTemplate.findOne(new Query(new Criteria("index").is(o)), ContentType.class);
//    // Pagination pagination = strategyService.listByName(name, 3, cur, page);
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    // List content = pagination.getContent();
//    // for(Iterator iterator = content.iterator(); iterator.hasNext();) {
//    // Strategy str = (Strategy) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    //
//    // switch(type) {
//    // case LIVE :
//    // p.setTagId(str.getGameCode() + "_" +
//    // COLLECTION_ITEM_TYPE.LIVE.getIndex());
//    // p.setTagName(str.getName() + COLLECTION_ITEM_TYPE.LIVE);
//    // break;
//    // case GIFT :
//    // p.setTagId(str.getGameCode() + "_" +
//    // COLLECTION_ITEM_TYPE.GIFT.getIndex());
//    // p.setTagName(str.getName() + COLLECTION_ITEM_TYPE.GIFT);
//    // break;
//    // default :
//    // p.setTagId(str.getGameCode() + "");
//    // p.setTagName(str.getName());
//    // break;
//    // }
//    // tags.add(p);
//    // }
//    return tags;
//  }
//
//  @Override
//  public List listAppTag(Map<String, Object> params) throws Exception {
//    List<UserItemOperatePvMongo2> list = clientLogCollectionDao.getPVByName("ItemAppPv", 0, 20, params);
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
//      UserItemOperatePvMongo2 str = (UserItemOperatePvMongo2) iterator.next();
//      PushTagCollection p = new PushTagCollection();
//      p.setTagId(str.getId().getServiceId());
//      p.setTagName(str.getValue().getServiceName());
//      tags.add(p);
//    }
//    return tags;
//  }
//
//  @Override
//  public List listContent(String name, int cur, int page, COLLECTION_ITEM_TYPE type, CONTENT_SOURCE source)
//      throws Exception {
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    // switch(type) {
//    // case GIFT :
//    // selectListGift(name, tags);
//    // break;
//    // case LIVE :
//    // // List<VideoLive> lives = videoLiveDao.findLiveListLikeName(name, 0,
//    // 20);
//    // for(Iterator iterator = lives.iterator(); iterator.hasNext();) {
//    // VideoLive li = (VideoLive) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getLiveTitle());
//    // p.setTagId(li.getLiveRoomId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // case WALKTHROUGH :
//    // Pagination pag = strategyService.listToWeb(-1, 3, name, 1, 20);
//    // List str = pag.getContent();
//    // for(Iterator iterator = str.iterator(); iterator.hasNext();) {
//    // Strategy li = (Strategy) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getName());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // case NEWS :
//    // switch(source) {
//    // case STRATEGY_NEWS :
//    // List strN = strategyNewsService.listNewsLikeName(name, 1,
//    // 20).getContent();
//    // for(Iterator iterator = strN.iterator(); iterator.hasNext();) {
//    // StrategyNews li = (StrategyNews) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getTitle());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // case COLUMN_NEWS :
//    // List colN = newsService.listNewsLikeTitle(name, 1, 20).getContent();
//    // for(Iterator iterator = colN.iterator(); iterator.hasNext();) {
//    // News li = (News) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getTitle());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // case POP_GAME_NEWS :
//    // List popN = popGameNewsService.listPopGameNewsLikeTitle(name, 1,
//    // 20).getContent();
//    // for(Iterator iterator = popN.iterator(); iterator.hasNext();) {
//    // PopGameNews li = (PopGameNews) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getTitle());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // default :
//    // break;
//    // }
//    // break;
//    // case VIDEO :
//    // switch(source) {
//    // case STRATEGY_VIDEO :
//    // List strV = strategyVideoService.listVideoLikeTitle(name, 1,
//    // 20).getContent();
//    // for(Iterator iterator = strV.iterator(); iterator.hasNext();) {
//    // StrategyVideo li = (StrategyVideo) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getTitle());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // case COLUMN_VIDEO :
//    // List colV = videoService.listVideoLikeTitle(name, 1, 20).getContent();
//    // for(Iterator iterator = colV.iterator(); iterator.hasNext();) {
//    // Video li = (Video) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getTitle());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // case POP_GAME_VIDEO :
//    // List popV = popGameVideoService.listPopGameVideoLikeTitle(name, 1,
//    // 20).getContent();
//    // for(Iterator iterator = popV.iterator(); iterator.hasNext();) {
//    // PopGameVideo li = (PopGameVideo) iterator.next();
//    // PushTagCollection p = new PushTagCollection();
//    // p.setTagName(li.getTitle());
//    // p.setTagId(li.getId() + "");
//    // tags.add(p);
//    // }
//    // break;
//    // default :
//    // break;
//    // }
//    //
//    // break;
//    // case JIONG :
//    // selectListJiong(name, tags);
//    // break;
//    // case GAME :
//    // tags=listGameTag(name, cur, 20, type);
//    // break;
//    // default :
//    // break;
//    // }
//    return tags;
//  }
//
//  private void selectListGift(String name, List<PushTagCollection> tags) throws Exception, JSONException {
//    Map<String, String> params = new HashMap<String, String>();
//    String result = null;
//    if(StringUtils.isEmpty(name)) {
//      result = HttpUtil.syncPost(
//          mobile_app_shouyou + "/gift/qianglist?gameType=0&osType=0&typeId=0&page=1&pageSize=20", params, null);
//    }
//    else {
//      result = HttpUtil.syncPost(mobile_app_shouyou + "/gift/searchgift?key=" + name + "&gameType=0", params, null);
//    }
//    JSONObject obj = new JSONObject(result);
//    JSONArray arr = obj.getJSONObject("data").getJSONArray("list");
//    for(int i = 0; i < arr.length(); i++) {
//      JSONObject jo = (JSONObject) arr.get(i);
//      if("0".equals(jo.getString("qiangShow"))) continue;
//      PushTagCollection p = new PushTagCollection();
//      p.setTagName(jo.getString("giftName"));
//      p.setTagId(jo.getString("giftId"));
//      tags.add(p);
//    }
//  }
//
//  private void selectListJiong(String name, List<PushTagCollection> tags) throws Exception, JSONException {
//    Map<String, String> params = new HashMap<String, String>();
//    String url = jiongtu + "/api/backopt/photoslist";
//    // url="http://10.6.212.29:8080/jiongtu/api/backopt/photoslist";
//    params.put("pageIndex", "0");
//    params.put("pageSize", "20");
//    if(!StringUtils.isEmpty(name)) params.put("photosTitle", name);
//    // String result = HttpUtil.syncPost(url, params, "gb2312");
//    String result = HttpUtil.syncGet(url, params, "UTF-8", "UTF-8");
//    JSONObject obj = new JSONObject(result);
//    JSONArray arr = obj.getJSONArray("result");
//    for(int i = 0; i < arr.length(); i++) {
//      JSONObject jo = (JSONObject) arr.get(i);
//      PushTagCollection p = new PushTagCollection();
//      p.setTagName(jo.getString("photosName"));
//      p.setTagId(jo.getString("photosId"));
//      tags.add(p);
//    }
//  }
//
//  @Override
//  public List getGameCategory(int code) throws Exception {
//    List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
//    Map<String, String> obj = null;
//    switch(code) {
//      case 1001 :
//        obj = getMobileType("info_status");
//        break;
//      case 1002 :
//        obj = getMobileType("info_type");
//        break;
//      case 1003 :
//        obj = getPCType("5");
//        break;
//      case 1004 :
//        obj = getPCType("2");
//        break;
//      default :
//        break;
//    }
//    iteratorConvert2Tag(tags, obj, code + "");
//    return tags;
//  }
//
//  @Override
//  // @Cacheable(value = "videoMobileCMSCache", key = "'mobileType_' + #type")
//  public Map<String, String> getMobileType(String type) throws Exception, JSONException {
//    Map<String, String> mobileType = new HashMap<String, String>();
//    Map<String, String> p = new HashMap<String, String>();
//    String url = game_cate_mobile + "/apis/category/list.html?class_type=" + type;
//    String str = HttpUtil.syncPost(url, p, null);
//    JSONObject obj = new JSONObject(str).getJSONObject("data");
//    Iterator it = obj.keys();
//    while(it.hasNext()) {
//      String key = (String) it.next();
//      JSONObject value = obj.getJSONObject(key);
//      mobileType.put(key, value.getString("class_name"));
//    }
//    return mobileType;
//  }
//
//  @Override
//  // @Cacheable(value = "videoMobileCMSCache", key = "'pcType_' + #type")
//  public Map<String, String> getPCType(String type) throws Exception, JSONException {
//    String url = game_cate_pc + "/game/category?game_class=1";
//    Map<String, String> pctype = new HashMap<String, String>();
//    Map<String, String> p = new HashMap<String, String>();
//    String str = HttpUtil.syncPost(url, p, null);
//    JSONObject obj = new JSONObject(str).getJSONObject("data").getJSONObject(type).getJSONObject("children");
//    Iterator it = obj.keys();
//    while(it.hasNext()) {
//      String key = (String) it.next();
//      JSONObject value = obj.getJSONObject(key);
//      pctype.put(key, value.getString("name"));
//    }
//    return pctype;
//  }
//
//  private void iteratorConvert2Tag(List<PushTagCollection> tags, Map<String, String> type, String prifix)
//      throws JSONException {
//    Iterator iter = type.keySet().iterator();
//    while(iter.hasNext()) {
//      String id = iter.next().toString();
//      String name = type.get(id);
//      PushTagCollection tag = new PushTagCollection();
//      tag.setTagId(name);
//      tag.setTagName(name);
//      tags.add(tag);
//    }
//  }
//
//  /**
//   * // @CacheEvict(value = "videoMobileCMSCache", key =
//   * "'typeStatus' + #gameCode")
//   */
//  @Override
//  @Cacheable(value = "videoMobileCMSCache", key = "'gameInfo_X_' + #gameCode ")
//  public Map<String, String> getGameCodeTypeAndStatus(String gameCode, GAME_PLATFORM_TYPE type) {
//    Map<String, String> typeStatus = null;
//    if(type == null) {
//      typeStatus = mobile(gameCode);
//      if(typeStatus != null) {
//        typeStatus.put("platForm", "1");
//        return typeStatus;
//      }
//      typeStatus = pc(gameCode);
//      if(typeStatus != null) {
//        typeStatus.put("platForm", "2");
//        return typeStatus;
//      }
//    }
//    else {
//      if(type == GAME_PLATFORM_TYPE.MOBILE) {
//        return mobile(gameCode);
//      }
//      else if(type == GAME_PLATFORM_TYPE.PC) {
//        return pc(gameCode);
//      }
//    }
//    return typeStatus;
//  }
//
//  private Map<String, String> pc(String gameCode) {
//    try {
//      Map<String, String> p = new HashMap<String, String>();
//      Map<String, String> typeStatus;
//      typeStatus = new HashMap<String, String>();
//      String url = game_cate_pc + "/game/info?game_code=" + gameCode;
//      String str = HttpUtil.syncPost(url, p, null);
//      if(StringUtils.isEmpty(str)) return null;
//      JSONObject obj = new JSONObject(str).getJSONObject("data");
//      JSONArray info_status = obj.getJSONArray("game_feature");
//      typeStatus.put("name", obj.getString("game_name"));
//      JSONArray info_type = obj.getJSONArray("game_type");
//      typeStatus.put("status", getStrs(info_status).toString());
//      typeStatus.put("type", getStrs(info_type).toString());
//      typeStatus.put("gameCode", gameCode);
//      return typeStatus;
//    }
//    catch(Exception e) {
//      // e.printStackTrace();
//      return null;
//    }
//  }
//
//  private Map<String, String> mobile(String gameCode) {
//    try {
//      Map<String, String> p = new HashMap<String, String>();
//      Map<String, String> typeStatus;
//      typeStatus = new HashMap<String, String>();
//      String url = game_cate_mobile + "/apis/game/info?game_code=" + gameCode;
//      String str = HttpUtil.syncPost(url, p, null);
//      if(StringUtils.isEmpty(str)) return null;
//      JSONObject obj = new JSONObject(str).getJSONObject("data").getJSONObject(gameCode);
//      JSONArray info_status = obj.getJSONArray("info_status");
//      StringBuffer status = getStrs(info_status);
//      JSONObject info_type = obj.getJSONObject("info_type");
//      typeStatus.put("type", info_type.getString("name"));
//      typeStatus.put("status", status.toString());
//      typeStatus.put("pkg", obj.getString("info_package"));
//      typeStatus.put("name", obj.getString("info_chname"));
//      typeStatus.put("gameCode", gameCode);
//      return typeStatus;
//    }
//    catch(Exception e) {
//      // e.printStackTrace();
//      return null;
//    }
//  }
//
//  private StringBuffer getStrs(JSONArray info_status) throws JSONException {
//    Map<String, String> p = new HashMap<String, String>();
//    StringBuffer status = new StringBuffer();
//    for(int i = 0; i < info_status.length(); i++) {
//      status.append(info_status.getJSONObject(i).getString("name")).append(",");
//    }
//    if(status.length() != 0) status = status.deleteCharAt(status.length() - 1);
//    return status;
//  }
//
//  @Override
//  public void setThreadTotal(String total) {
//    pushTagLogDao.setThreadTotal(Integer.parseInt(total));
//  }
//
//  @Override
//  public void setSysThreadNum(int num) {
//    pushTagLogDao.setSysThreadNum(num);
//  }
//
//  @Override
//  public void updateWaiting() {
//    pushTagLogDao.updateWaiting();
//  }
//
//  @Override
//  public PushTagExcuteStateInfo getSysThreadNum() {
//    return pushTagLogDao.getSysThreadNum();
//  }
//
//  @Override
//  public Integer getThreadTotal() {
//    return pushTagLogDao.getThreadTotal();
//  }
//
//  @Override
//  public List<PushTagExcuteStateInfo> getThreadNumList() {
//    try {
//      return pushTagLogDao.getThreadNum("taskExecutor");
//    }
//    catch(Exception e) {
//      return null;
//    }
//  }
//
//  @Override
//  public void delThreadNumList() {
//    try {
//      pushTagLogDao.removeThreadLog();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }

}
