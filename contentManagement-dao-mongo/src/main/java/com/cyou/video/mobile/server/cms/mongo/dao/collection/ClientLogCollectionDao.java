package com.cyou.video.mobile.server.cms.mongo.dao.collection;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogBestWalkthroughCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo2;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.mongodb.AggregationOutput;


/**
 * 
 * @author lusi
 *
 */
public interface ClientLogCollectionDao {

	/**
	 * 
	 * 测试 待删除
	 * @param collections
	 * @return
	 */
	public int insertBatch(List collections);

	/**
   * 
   * 批量插入日志 
   * @param collections
   * @return
   */
	public int insertBatchS(List<LinkedHashMap<String, String>> collections);
	 
	/**
	 * 统计pv
	 * @return
	 */
	void statisticsPv();

	List<ClientLogCollection> getClientCollection(
			ClientLogCollection collections, int page, int size,Map<String, Object> params);

	List getPVByName(String collectionName, int page,
			int size,Map<String, Object> params);

	/**
	 * total count
	 * @param collectionName
	 * @return
	 */
	long getPVCountByName(String collectionName,Map<String, Object> params,String tag);

	/**
	 * pv最新日期
	 * @param collectionName
	 * @param params
	 * @return
	 */
	 List<StatisticJobLastUpdateTime> getPVLastUpdate(String collectionName,
			Map<String, Object> params);

	int updateBatch(ClientLogCollection id, String collecitonName,int state);

	int insertPushTags(List<PushTagCollection> collections);

	int removeAllPushTags();

	/**
	 * 得到tag列表
	 * @param page
	 * @param size
	 * @param params
	 * @return
	 */
	List<PushTagCollection> getTagCollection(int page, int size,
			Map<String, Object> params);

	long getTagCollectionCount(int page, int size, Map<String, Object> params);

	/**
	 * 
	 * @param collectionName
	 * @param page
	 * @param size
	 * @param params
	 * @return
	 */
	List<UserItemOperatePvMongo2> getUserItemNotEqual1(String collectionName,
			int page, int size, Map<String, Object> params);

	List<ClientLogCollection> getClientCollectionBaiduIdNull(int page, int size,
			Map<String, Object> params);

	/**
	 * 更新收集来的数据 之前 没有 中文title baiduid
	 * @param id
	 * @return
	 */
	int updateClientLogCollection(ClientLogCollection id);

	/**
	 * 得到所有serviceID
	 * @return
	 */
	AggregationOutput distinctServiceId();

	/**
	 * 批量更新一批serviceID数据
	 * 
	 * @param id
	 * @return
	 */
	int updateServiceNameMulti(ClientLogCollection id);

	void itemPv();

	/**
	 * 取得pv最新更新时间
	 * @return
	 */
	List<StatisticJobLastUpdateTime> getStatisticJobLastUpdateTime();

	/**
	 * 得到集合总大小
	 * @param name
	 * @return
	 */
	long getCount(String name);

	/**
	 * 得到用户订阅
	 * @param page
	 * @param size
	 * @return
	 */
	List<UserItemOperatePvMongo2> getUserItemOperatePvMongo2(int page, int size,	Query query,String name );

	/**
	 * 增加pushtag时间戳
	 * @param tagLastUpdateTime
	 * @return
	 */
	int updatePushTagLastUpdateTime(PushTagLastUpdateTime tagLastUpdateTime);

	/**
	 * 取得推送时间戳
	 * @param cellectionName
	 * @return
	 */
	PushTagLastUpdateTime getPushTagLastUpdateTime(String cellectionName);

	/**
	 * mongo insert method
	 * @param o
	 * @return
	 */
	int insert(Object o);

	/**
	 * 查找某个对象根据某个值
	 * @param o
	 * @param field
	 * @param value
	 * @return
	 */
	Object findByField(Class o, String field, String value);

	/**
	 * 查找所有根据query
	 * @param o
	 * @param query
	 * @return
	 */
	List<Object> findByCondition(Class o, Query query);

	/**
	 * count 2.0
	 * @param name
	 * @return
	 */
	long getCount(Class name);

	/**
	 * count by 
	 * @param o
	 * @param query
	 * @return
	 */
	long countByCondition(Class o, Query query);

	/**
	 * 根据查询count
	 * @param query
	 * @param name
	 * @return
	 */
	long getCountByQuery(Query query, String name);

	void remove(Object o);

	/**
	 * 移除查询的
	 * @param o
	 * @param query
	 */
	public void remove(Class o,Query query);
	/**
	 * 记录更新
	 * @param o
	 */
	void save(Object o);

	/**
	 * 找到数据 字段++
	 * @param incField
	 * @param q
	 * @param cla
	 * @return
	 */
	Object updateInc(String incField, Query q, Class cla);

	/**
	 * 收集攻略手机app
	 * @param page
	 * @param size
	 * @param query
	 * @param name
	 * @return
	 */
  List<ClientLogBestWalkthroughCollection> getClientLogWalkthroughCollection(int page, int size, Query query, String name);

}
