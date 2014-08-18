package com.cyou.video.mobile.server.cms.service.sys;

import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.SystemConfig;
import com.cyou.video.mobile.server.cms.service.push.impl.DataNotFoundException;

public interface SystemConfigService {

  /**
   * 查找系统配置
   * 
   * @param configKey
   * @return
   */
  String getByKey(String configKey) throws DataNotFoundException;

  /**
   * 更新配置项
   * @param systemConfig
   * @return
   * @throws DataNotFoundException
   */
  void save(SystemConfig systemConfig);

  /**
   * 列表
   * @param params
   * @return
   * @throws Exception
   */
  Pagination list(Map<String, Object> params) throws Exception;

  /**
   * remove
   * @param systemConfig
   */
  void delete(SystemConfig systemConfig);

}
