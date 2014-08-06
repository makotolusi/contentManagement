package com.cyou.video.mobile.server.cms.service.collection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.ClientLogCollection;
import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.model.collection.UserItemOperatePvMongo;

/**
 * collection and pv
 * 
 * @author lusi
 * 
 */
public interface ClientLogCollectionService {

	/**
	 * 终端日志收集
	 * 
	 * @param collections
	 *            日志
	 * @return
	 */
	public int collectLogInfo(List collections);

	public int collectLogInfoS(List<LinkedHashMap<String, String>> collections);

	/**
	 * 统计pv
	 * 
	 * @return
	 */
	void statisticsPv();


	/**
	 * 
	 * @param params
	 * @return
	 */
	Pagination getClientLogCollection(Map<String, Object> params)throws Exception ;


	Pagination getPVByName(Map<String, Object> params)throws Exception ;

	/**
	 *  最新更新日期
	 * @param params
	 * @return
	 * @throws Exception
	 */
	 List<StatisticJobLastUpdateTime> getPVLastUpdateTime(
			Map<String, Object> params) throws Exception;

	/**
	 * 得到tag的name
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Pagination getTagNameAndPV(Map<String, Object> params) throws Exception;

	void getTagName(ClientLogCollection clientLogCollection) throws Exception;

	/**
	 * get userid of tag
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<UserItemOperatePvMongo> getUserIdByTag(Map<String, Object> params)
			throws Exception;

	/**
	 * 获得最新统计时间
	 * @return
	 */
	List<StatisticJobLastUpdateTime> getStatisticJobLastUpdateTime();

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
	int updatePushTagLastUpdateTime(PushTagLastUpdateTime tagLastUpdateTime);

	/**
	 * 得到推送时间戳
	 * @param collectionName
	 * @return
	 */
	PushTagLastUpdateTime getPushTagLastUpdateTime(String collectionName);

	/**
	 * 根据查询count
	 * @param query
	 * @param name
	 * @return
	 */
	Long getCount(Query query, String name);

	
	/**
	 * 得到tag发送日志
	 * @return
	 */
	List getPushTagExcuteStateInfo();

	/**
	 * 得到数据总数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	
	int getTotalNum(String collectionName) throws Exception;

	/**
	 * 包名转json
	 * @param pkgs
	 * @return
	 */
  List<ClientLogCollection> gameAppFilter(String pkg,COLLECTION_OPERATOR_TYPE opType,String token);

  /***
   * 绑定信鸽
   * @param params
   * @throws Exception
   */
  void bindUserId(Map<String, String> params) throws Exception;
}
