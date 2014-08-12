package com.cyou.video.mobile.server.cms.service.sys;

import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.ConfigApps;

/**
 * CMS系统参数业务接口
 * 
 * @author zs
 */
public interface ConfigAppsService {

  /**
   * 配置应用列表
   * 
   * @return
   * @throws Exception
   */
  Pagination listContentType(Map<String, Object> params) throws Exception;

  /**
   * 新增
   * 
   * @param contentType
   * @throws Exception
   */
  void createContentType(ConfigApps configApps) throws Exception;

  /**
   * 更新
   * 
   * @param contentType
   * @throws Exception
   */
  void updateContentType(ConfigApps configApps) throws Exception;

  /**
   * 删除
   * 
   * @param contentType
   * @throws Exception
   */
  void deleteContentType(ConfigApps configApps) throws Exception;

  /**
   * 根据appid找
   * @param appId
   * @return
   * @throws Exception
   */
  ConfigApps findByAppid(Integer appId) throws Exception;
}
