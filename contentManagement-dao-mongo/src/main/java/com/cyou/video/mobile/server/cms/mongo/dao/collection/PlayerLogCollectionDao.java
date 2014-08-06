package com.cyou.video.mobile.server.cms.mongo.dao.collection;


import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.model.collection.PlayerAdv;
import com.cyou.video.mobile.server.cms.model.collection.PlayerApps;
import com.cyou.video.mobile.server.cms.model.collection.PlayerAvList;
import com.cyou.video.mobile.server.cms.model.collection.PlayerLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.PlayerPvList;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;


/**
 * 
 * @author duxiaona
 *
 */
public interface PlayerLogCollectionDao {

	/**
	 * 
	 * 插入日志 
	 * @param collections
	 * @return
	 */
	public int insertBatch(List<PlayerLogCollection> collections);


	List<PlayerLogCollection> getPlayerCollection(
			PlayerLogCollection collections, int page, int size,Map<String, Object> params);

	List<PlayerPvList> getPV(PlayerPvList collections, int page,
			int size,Map<String, Object> params);
	
	List<PlayerAvList> getAV(PlayerAvList collections, int page,
			int size,Map<String, Object> params);
	
	List<PlayerAdv> listAdv(PlayerAdv collections, int page,
			int size,Map<String, Object> params);
	
	List<PlayerApps> listApps(PlayerApps collections, int page,
			int size,Map<String, Object> params);
	/**
	 * 获取广告代码
	 * @return
	 */
	String getAdvCode();

	/**
	 * 获取广告状态
	 * @return
	 */
	String getAdvState(String appid);
	
	PlayerAdv getAdvertisingById(String id);
	
	/**
	 * 停用广告
	 * @return
	 */
	int disableAdvertising(String id);
	
	/**
	 * 启用广告
	 * @return
	 */
	int enableAdvertising(String id);
	
	
	/**
	 * 更新app表中的广告开关
	 * @param islocal
	 * @param state
	 * @return
	 */
	int updateAdvStatePlayerApps(String islocal,String state);
	
	
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


	int updateApps(PlayerApps playerApps);
	
	int updateAdv(PlayerAdv playerAdv);



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

}
