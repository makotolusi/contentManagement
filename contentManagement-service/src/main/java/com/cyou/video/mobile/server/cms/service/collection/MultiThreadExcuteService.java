package com.cyou.video.mobile.server.cms.service.collection;

import java.util.Map;

import org.springframework.ui.ModelMap;


/**
 * collection and pv
 * 
 * @author lusi
 * 
 */
public interface MultiThreadExcuteService {

	/**
	 * 多线程发标签
	 * @param model
	 * @return
	 */
	public ModelMap sendPushTags( Map<String, Object> params,ModelMap model);

	/**
	 * 多线程制作标签
	 * @param model
	 * @return
	 */
	ModelMap makeTag(ModelMap model);

	/**
	 * 补充已有数据
	 * @param model
	 * @return
	 */
	ModelMap updateLogInfo(Map<String, Object> params,ModelMap model);

	/**
	 * 发组合标签
	 * @param model
	 * @return
	 */
	ModelMap sendCombinationTags(Map<String, Object> params,ModelMap model);

	/**
	 * 渠道标签
	 * @param model
	 * @return
	 */
	ModelMap sendPushTagsChannel(Map<String, Object> params,ModelMap model);
	
	/**
	 * 组合标签数量
	 * @param name
	 * @return
	 */
	public long countCombinationTags(String name);

	/**
	 * 杀掉进程
	 */
  void shutDown();

  /**
   * 最强攻略手机收集
   * @param params
   * @param model
   * @return
   */
  ModelMap sendWalkThroughAppTags(Map<String, Object> params, ModelMap model);

  /**
   * 同步应用 app
   */
  void syncApp();
}
