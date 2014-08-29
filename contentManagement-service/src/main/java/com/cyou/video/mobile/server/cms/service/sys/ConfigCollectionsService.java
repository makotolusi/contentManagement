package com.cyou.video.mobile.server.cms.service.sys;

import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.ConfigApps;
import com.cyou.video.mobile.server.cms.model.sys.ConfigCollections;

/**
 * CMS系统参数业务接口
 * 
 * @author zs
 */
public interface ConfigCollectionsService {

  /**
   * 配置应用列表
   * 
   * @return
   * @throws Exception
   */
  Pagination list(Map<String, Object> params) throws Exception;

  /**
   * 新增
   * 
   * @param contentType
   * @throws Exception
   */
  void save(ConfigCollections configCollections) throws Exception;

 
  /**
   * 删除
   * 
   * @param contentType
   * @throws Exception
   */
  void delete(ConfigCollections configCollections) throws Exception;

  /**
   * 根据appid找
   * @param appId
   * @return
   * @throws Exception
   */
  ConfigApps findByAppid(Integer appId) throws Exception;
}
