/**
 * 
 */
package com.cyou.video.mobile.server.cms.service.push;

import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;

/**
 * 推送历史业务接口
 * 
 * @author kevin Wang
 * 
 */
public interface PushHistoryService {
	/**
	 * 获取推送历史列表
	 * 
	 * @param params
	 *            查询参数
	 * @param curPage
	 *            当前页码
	 * @param pageSize
	 *            每页数量
	 * @return 推送历史列表
	 * @throws Exception
	 */
	public Pagination listPushHistory(Map<String, Object> params)
			throws Exception;
}
