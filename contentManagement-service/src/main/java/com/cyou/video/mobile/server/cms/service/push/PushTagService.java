package com.cyou.video.mobile.server.cms.service.push;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ui.ModelMap;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.CONTENT_SOURCE;
import com.cyou.video.mobile.server.cms.common.Consts.GAME_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;
import com.cyou.video.mobile.server.cms.model.push.PushTagCombination;

/**
 * 意见反馈业务接口
 * 
 * @author jyz
 */
public interface PushTagService {

  // /**
  // * 生成tag列表 方便以后发百度 tag
  // */
  // void importPushTagCollectinos();

  /**
   * 设置tag的job service
   */
  int sendPushTags(int s, int end, Query query, String name);

  /**
   * 通过itempv 生成 tag列表
   */
  // void makeTagCollection(int s, int end, Query query, String name);

  /**
   * 根据token 取 uid baidu
   * 
   * @param token
   * @return
   * @throws Exception
   */
  String getUidByToken(String token) throws Exception;

  /**
   * 清除所有标签
   * 
   * @param ids
   * @param state
   * @return
   */
  boolean removeAllPushTags();

  /**
   * 发送渠道的标签
   */
  int sendPushTagsChannel(int s, int end, Date lastModifyDate) throws Exception;

  /**
   * 订阅标签
   * 
   * @param s
   * @param e
   */
  int sendPushTagsSubscribe(int s, int e, Query query, String name);

  /**
   * 查询用户tag
   * 
   * @param uid
   * @param clientType
   * @return
   */
  ModelMap queryUserTag(String uid, ModelMap model) throws Exception;

  /**
   * 拼tag中文名 统计结果用
   * 
   * @param clientLogCollection
   * @param v
   * @return
   */
  String makeUserTagName(ClientLogCollection clientLogCollection, ClientLogCollection v);

  /**
   * 拼tag中文名
   * 
   * @param clientLogCollection
   * @param v
   * @return
   */
  String makeUserTagName(ClientLogCollection clientLogCollection);

  /**
   * 拼tag id 名称
   * 
   * @param clientLogCollection
   * @return
   */
  String makeUserTagId(ClientLogCollection clientLogCollection);

  /**
   * 制作tag id
   * 
   * @param id
   * @param value
   * @return
   */
  String makeUserTagId(ClientLogCollection id, ClientLogCollection value);

  /**
   * 插入组合
   * 
   * @param com
   * @return
   */
  boolean makePushTagCombination(PushTagCombination com);

  /**
   * 组合标签是否已经存在
   * 
   * @param com
   * @return
   */
  boolean existPushTagCombination(PushTagCombination com);

  /**
   * find all c tag
   * 
   * @param params
   * @return
   */
  Pagination findPushTagCombination(Map<String, Object> params);

  /**
   * 记录发送标签日志
   * 
   * @param com
   * @return
   */
  void savePushTagLog(PushTagExcuteStateInfo com);

  /**
   * 得到一个 组合标签
   * 
   * @param name
   * @return
   */
  PushTagCombination findTagCombinationOne(String name);

  /**
   * 组合标签
   * 
   * @param s
   * @param end
   * @param query
   * @param name
   * @param combinationTagId
   */
  int sendPushCombinationTags(int s, int end, Query query, String name, PushTagCombination combination);

  /**
   * 根据名字找log
   * 
   * @param name
   * @return
   */
  List<PushTagExcuteStateInfo> getPushTagLog(String name);

  /**
   * 移除
   * 
   * @param com
   */
  void removePushTagLog(PushTagExcuteStateInfo com);

  /**
   * 移除
   * 
   * @param name
   */
  public void removePushTagLogByName(String name);

  /**
   * save push combination
   * 
   * @param com
   */
  void savePushTagCombination(PushTagCombination com);

  /**
   * 增加log线程 完成数
   * 
   * @param name
   */
  void incThreadNum(String name);

  /**
   * 线程结束标志
   * 
   * @param c
   * @param total
   */
  void successLogEnd(String c, int total);

  /**
   * 线程开始
   * 
   * @param threadNum
   * @param c
   * @param size
   */
  void successLogStart(int threadNum, String c, long size);

  /**
   * 是否存在正在运行 线程
   * 
   * @return
   */
  boolean existRunningThread();

  /***
   * 得到游戏分类标签
   * 
   * @param code
   *          类别
   * @return
   * @throws Exception
   */
  List getGameCategory(int code) throws Exception;

  /**
   * 得到jiong标签
   * 
   * @return
   */
  public List getJiong() throws Exception;

  /**
   * 得到排行榜标签
   * 
   * @return
   * @throws Exception
   */
  List getRankTag() throws Exception;

  /**
   * 手游和端游tag
   * 
   * @return
   * @throws Exception
   */
  List getGamePlatFormTag() throws Exception;

  /***
   * 列出所有游戏标签
   * 
   * @return
   * @throws Exception
   */
  List listGameTag(String name, int cur, int page, COLLECTION_ITEM_TYPE type) throws Exception;

  /**
   * 得到手游 类型 json
   * 
   * @param type
   * @return
   * @throws Exception
   * @throws JSONException
   */
  Map<String, String> getMobileType(String type) throws Exception, JSONException;

  /**
   * 得到pc 类型json
   * 
   * @param url
   * @param type
   * @return
   * @throws Exception
   * @throws JSONException
   */
  Map<String, String> getPCType(String type) throws Exception, JSONException;

  /**
   * 收集时得到 gamecode 类型 特征
   * 
   * @param gameCode
   * @param type
   * @return
   */
  Map<String, String> getGameCodeTypeAndStatus(String gameCode, GAME_PLATFORM_TYPE type);

  /**
   * 线程执行总量
   * 
   * @param total
   */
  void setThreadTotal(String total);

  /**
   * 得到线程总数
   * 
   * @return
   */
  Integer getThreadTotal();

  /**
   * 取得多个线程值
   * 
   * @return
   */
  List<PushTagExcuteStateInfo> getThreadNumList();

  /**
   * 删除 线程计数
   */
  void delThreadNumList();

  /**
   * 删除tag
   * 
   * @param uid
   * @param clientType
   */
  public ModelMap deleteUserTag(String token, ModelMap model) throws Exception;

  /**
   * 系统自动标签线程数量
   * @param num
   */
  void setSysThreadNum(int num);

  /**
   * 得到系统参数
   * @return
   */
  PushTagExcuteStateInfo getSysThreadNum();

  /**
   * 更新状态
   */
  void updateWaiting();

  /**
   * map reduce 是否执行完成
   * @return
   */
  boolean isRunningMapReduce();

  /**
   * 推送内容列表
   * @param name
   * @param cur
   * @param page
   * @param type
   * @param source
   * @return
   * @throws Exception
   */
  List listContent(String name, int cur, int page, COLLECTION_ITEM_TYPE type, CONTENT_SOURCE source) throws Exception;

  /**
   * 最强攻略收集
   * @param s
   * @param end
   * @param query
   * @param name
   * @return
   */
  int sendPushWalkThroughTags(int s, int end, Query query, String name);

  /**
   * 最强攻略 app标签
   * @param params
   * @param cur
   * @param page
   * @param type
   * @return
   * @throws Exception
   */
  List listAppTag(Map<String, Object> params) throws Exception;

}
