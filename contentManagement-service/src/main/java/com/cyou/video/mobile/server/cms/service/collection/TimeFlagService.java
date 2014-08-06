package com.cyou.video.mobile.server.cms.service.collection;

import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;

/**
 * 
 * @author lusi
 * 
 */
public interface TimeFlagService {

	/**
	 * 异常恢复
	 * 
	 * @param oldLastUpdateTime
	 */
	public void recover(PushTagLastUpdateTime oldLastUpdateTime);

	/**
	 * 设置时间戳
	 * 
	 * @param query
	 * @return
	 */
	public PushTagLastUpdateTime setTimestamp(Query query,String cname,String dname) ;
}
