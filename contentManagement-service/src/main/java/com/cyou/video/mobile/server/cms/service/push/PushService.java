package com.cyou.video.mobile.server.cms.service.push;

import java.util.Map;

import com.cyou.video.mobile.server.cms.common.Consts.PUSH_JOB_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_STATE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.push.Push;

/**
 * 意见反馈业务接口
 * 
 * @author jyz
 */
public interface PushService {

	/**
	 * 创建意见反馈
	 * 
	 * @param feedback
	 *            意见反馈
	 * @return 意见反馈id
	 * @throws Exception
	 */
	public String createPush(Push push);

	/**
	 * 更新意见反馈状态为已读
	 * 
	 * @param id
	 *            意见反馈id
	 * @throws Exception
	 */
	public void readPush(int id) throws Exception;

	/**
	 * 获取意见反馈
	 * 
	 * @param id
	 *            意见反馈id
	 * @return 意见反馈
	 * @throws Exception
	 */
	public Push getPushById(String id) throws Exception;

	/**
	 * 获取子意见反馈
	 * 
	 * @param id
	 *            父意见反馈id
	 * @return 子意见反馈
	 * @throws Exception
	 */
	public Push getPushByPreId(int preId) throws Exception;

	/**
	 * 获取意见反馈列表
	 * 
	 * @param params
	 *            查询参数
	 * @return 意见反馈列表
	 * @throws Exception
	 */
	public Pagination listPush(Map<String, Object> params) throws Exception;

	/**
	 * 更新推送任务
	 * 
	 * @param id
	 *            推送任务编号
	 * @param state
	 *            任务状态
	 * @throws Exception
	 */
	public void modifyStateById(String id, PUSH_JOB_STATE state) throws Exception;

//	void fetchTag();

	/**
	 * 新的任务
	 * 
	 * @param pushId
	 * @param cronExpress
	 * @throws Exception
	 */
	public void postNewJob(String pushId, String cronExpress) throws Exception;

	/**
	 * 删除任务
	 * 
	 * @param id
	 * @throws Exception
	 */
	void deletePush(String id,int type) throws Exception;

	/**
	 * update job
	 * 
	 * @param push
	 * @throws Exception
	 */
	void modifyPush(Push push) throws Exception;

	/**
	 * update send state
	 * @param id
	 * @param state
	 * @throws Exception
	 */
	void updateSendStateById(Push push);

	/**
	 * 自动推送任务
	 * @param push
	 * @throws Exception
	 */
	void autoPush(Push push) throws Exception;

	public Push pushInfo(Push push);

	/**
	 * 自动推送任务启停 
	 * @param id
	 * @param state
	 * @throws Exception
	 */
	void modifyAutoPushStateById(String id, PUSH_JOB_STATE state)
			throws Exception;

	/**
	 * 自动推送启停
	 * @param id
	 * @param state
	 * @throws Exception
	 */
  void modifyAutoPushStateById(int id, PUSH_JOB_STATE state) throws Exception;

  /**
   * 更新push
   * @param push
   * @return
   */
  int updatePush(Push push);
}
