package com.cyou.video.mobile.server.cms.service.sys;

import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;


/**
 * CMS系统参数业务接口
 * @author lusi
 */
public interface ContentTypeService {

  /**
   * 类型列表
   * @return
   * @throws Exception
   */
  Pagination listContentType(Map<String, Object> params) throws Exception;

  /**
   * 新增
   * @param contentType
   * @throws Exception
   */
  void createContentType(ContentType contentType) throws Exception;
	
  /**
   * 更新
   * @param contentType
   * @throws Exception
   */
  void updateContentType(ContentType contentType) throws Exception;

  /**
   * 删除
   * @param contentType
   * @throws Exception
   */
  void deleteContentType(ContentType contentType) throws Exception;

  /**
   * 根据index取类型
   * @param index
   * @return
   * @throws Exception
   */
  ContentType getByIndex(String index) throws Exception;
}
