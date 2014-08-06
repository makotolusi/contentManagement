package com.cyou.video.mobile.server.cms.service.collection;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.PlayerAdv;
import com.cyou.video.mobile.server.cms.model.collection.PlayerApps;
import com.cyou.video.mobile.server.cms.model.collection.PlayerLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
/**
 * collection and pv
 * 
 * @author lusi
 * 
 */
public interface PlayerLogCollectionService {

	/**
	 * 终端日志收集
	 * 
	 * @param collections
	 *            日志
	 * @return
	 */
	public int collectLogInfo(List<PlayerLogCollection> collections);

	
	/**
	 * 获取广告代码
	 * 
	 * @return
	 */
	public String getAdvCode();
	
	
	/**
	 * 获取广告开关
	 * 
	 * @return
	 */
	public String getAdvState(String appid);


	/**
	 * 停用广告
	 * @param id
	 * @throws Exception
	 */
	int disableAdvertising(String id) throws Exception;

	/**
	 * 启用广告
	 * @param id
	 * @throws Exception
	 */
	int enableAdvertising(String id) throws Exception;
	
	PlayerAdv getAdvertisingById(String id) throws Exception;

	/**
	 * 
	 * @param params
	 * @return
	 */
	Pagination getPlayerLogCollection(Map<String, Object> params)throws Exception ;


	Pagination getPV(Map<String, Object> params)throws Exception ;
	
	Pagination getAV(Map<String, Object> params)throws Exception ;
	
	Pagination listAdv(Map<String, Object> params)throws Exception ;
	
	Pagination listApps(Map<String, Object> params)throws Exception ;
	
	/**
	 *  最新更新日期
	 * @param params
	 * @return
	 * @throws Exception
	 */
	 List<StatisticJobLastUpdateTime> getPVLastUpdateTime(
			Map<String, Object> params) throws Exception;

	
	/**
	 * 得到集合count
	 * @param name
	 * @return
	 */
	Long getCount(String name);

	/**
	 * 得到标签时间戳
	 * @param tagLastUpdateTime
	 * @return
	 */
	
	Long getCount(Query query, String name);

	
		
	int getTotalNum(String collectionName) throws Exception;
	
	int updateApps(PlayerApps playerApps) throws Exception;
	
	int updateAdv(PlayerAdv playerAdv) throws Exception;

}
