package com.cyou.video.mobile.server.cms.service.security;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.security.Manager;
import com.cyou.video.mobile.server.cms.model.security.Operation;
import com.cyou.video.mobile.server.common.Constants;

/**
 * CMS管理员业务接口
 * 
 * @author lusi
 */
public interface ManagerService {

  int createManager(Manager manager) throws Exception;

  List<Operation> login(Manager manager, HttpServletRequest request) throws Exception;

  void editPassword(String password, HttpServletRequest request) throws Exception;

  // List<Manager> listManager(int status, HttpServletRequest request) throws
  // Exception;

  Pagination listManager(Map<String, Object> params) throws Exception;

  void resetPassword(String managerId) throws Exception;

  void updateStatus(String managerId, Constants.STATUS status) throws Exception;

  /**
   * 管理员的操作项目
   * @param manager
   * @return
   * @throws Exception
   */
  List<Operation> getOperationOfManager(Manager manager) throws Exception;

  /**
   * 权限里是否有这个菜单
   * @param manager
   * @param operation
   * @return
   * @throws Exception
   */
  boolean containsOperationOfRoles(Manager manager, String operation) throws Exception;

}
