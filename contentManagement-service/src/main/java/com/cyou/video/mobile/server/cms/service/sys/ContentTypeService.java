package com.cyou.video.mobile.server.cms.service.sys;

import java.util.List;
import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
import com.cyou.video.mobile.server.cms.model.sys.ContentTypeActionAndTag;


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

  /***
   * list
   * @param params
   * @return
   * @throws Exception
   */
  Pagination listContentTypeAction(Map<String, Object> params) throws Exception;

  /**
   * create
   * @param action
   * @throws Exception
   */
  void createContentTypeAction(ContentTypeActionAndTag action) throws Exception;

  /**
   * update
   * @param action
   * @throws Exception
   */
  void updateContentTypeAction(ContentTypeActionAndTag action) throws Exception;

  /**
   * 删除
   * @param contentType
   * @throws Exception
   */
  void deleteAction(ContentTypeActionAndTag contentType) throws Exception;

  /**
   * 填充行为和标签
   * @param ct
   * @throws Exception
   */
  void fullActionAndTag(List<ContentType> ct) throws Exception;
}
