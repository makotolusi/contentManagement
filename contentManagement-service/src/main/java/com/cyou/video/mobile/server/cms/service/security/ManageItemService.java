package com.cyou.video.mobile.server.cms.service.security;

import java.util.List;
import java.util.Map;

import com.cyou.video.mobile.server.cms.model.security.ManageItem;

/**
 * CMS管理项业务接口
 * @author jyz
 */
public interface ManageItemService {

  /**
   * 获取管理项列表
   * @return 管理项列表
   * @throws Exception
   */
  public List<ManageItem> listManageItem() throws Exception;

  /**
   * 创建管理项
   * @param manageItem 管理项
   * @return 管理项id
   * @throws Exception
   */
  public void createManageItem(ManageItem manageItem) throws Exception;

  /**
   * 更新管理项
   * @param manageItem 管理项
   * @throws Exception
   */
  public void updateManageItem(ManageItem manageItem) throws Exception;

  /**
   * 删除管理项
   * @param id 管理项id
   * @throws Exception
   */
  public void deleteManageItem(int id) throws Exception;

  /**
   * 更新管理项状态
   * @param manageItemId 管理项id
   * @param status 管理项状态
   * @throws Exception
   */
  public void updateStatus(int manageItemId, int status) throws Exception;

  /**
   * 更新管理项排序
   * @param orderMap 排序信息
   * @throws Exception
   */
  public void updateOrder(Map<String, Integer> orderMap) throws Exception;
}
