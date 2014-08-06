package com.cyou.video.mobile.server.cms.service.security;

import java.util.List;

import com.cyou.video.mobile.server.cms.model.security.Operation;

/**
 * CMS操作项业务接口
 * @author jyz
 */
public interface OperationService {

  /**
   * 获取操作项列表
   * @return 操作项列表
   * @throws Exception
   */
  public List<Operation> listOperation() throws Exception;

  /**
   * 创建操作项
   * @param operation 操作项
   * @return 操作项id
   * @throws Exception
   */
  public void createOperation(Operation operation) throws Exception;

  /**
   * 更新操作项
   * @param operation 操作项
   * @throws Exception
   */
  public void updateOperation(Operation operation) throws Exception;

  /**
   * 删除操作项
   * @param id 操作项id
   * @throws Exception
   */
  public void deleteOperation(int id) throws Exception;

  /***
   * 添加角色
   * @param manageItemId
   * @param orderId
   * @param roleId
   * @throws Exception
   */
  void addRole(String manageItemId, int orderId, String roleId) throws Exception;

  /**
   * 角色权限
   * @param roleId
   * @return
   * @throws Exception
   */
  List<Operation> listOperationOfRole(String roleId) throws Exception;

  /**
   * 权限以外的菜单项
   * @param roleId
   * @return
   * @throws Exception
   */
  List<Operation> listOperationExcludeRole(String roleId) throws Exception;
}
