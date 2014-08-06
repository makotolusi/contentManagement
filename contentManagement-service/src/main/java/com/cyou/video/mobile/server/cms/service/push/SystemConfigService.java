package com.cyou.video.mobile.server.cms.service.push;

import com.cyou.video.mobile.server.cms.model.collection.SystemConfig;
import com.cyou.video.mobile.server.cms.service.push.impl.DataNotFoundException;

public interface SystemConfigService {

  /**
   * 查找系统配置
   * 
   * @param configKey
   * @return
   */
  String getSystemConfigByConfigKey(String configKey) throws DataNotFoundException;

  /**
   * 更新配置项
   * @param systemConfig
   * @return
   * @throws DataNotFoundException
   */
  void updateSystemConfigByConfigValue(SystemConfig systemConfig);

}
