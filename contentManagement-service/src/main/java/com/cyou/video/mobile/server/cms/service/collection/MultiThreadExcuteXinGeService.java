package com.cyou.video.mobile.server.cms.service.collection;

import java.util.Map;

import org.springframework.ui.ModelMap;


/**
 * 
 * @author lusi
 * 
 */
public interface MultiThreadExcuteXinGeService  {

  /**
   * 发送数据收集的所有tag
   * @param params
   * @param model
   * @return
   */
  ModelMap sendPushTags(Map<String, Object> params, ModelMap model);

  /**
   * 多线程173app渠道标签
   * @param params
   * @param model
   * @return
   */
  ModelMap sendPushTagsChannel(Map<String, Object> params, ModelMap model);

  /**
   * 临时 导入老推送数据
   * @param params
   * @param model
   * @return
   */
  ModelMap pushHistoryToMongo(Map<String, Object> params, ModelMap model);

}