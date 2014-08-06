package com.cyou.video.mobile.server.cms.service.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cyou.video.mobile.server.cms.model.security.Role;

/**
 * CMS角色业务接口
 * @author jyz
 */
public interface RoleService {

  /**
   * 获取角色列表
   * @param info 执行信息
   * @param request 请求信息
   * @return 角色列表
   * @throws Exception
   */
  public List<Role> listRole(HttpServletRequest request) throws Exception;

  /**
   * 创建角色
   * @param info 执行信息
   * @param role 角色对象
   * @return 角色id
   * @throws Exception
   */
  public void createRole(Role role) throws Exception;

  /**
   * 更新角色
   * @param info 执行信息
   * @param role 角色对象
   * @throws Exception
   */
  public void updateRole(Role role) throws Exception;

  /**
   * 删除角色
   * @param info 执行信息
   * @param id 角色id
   * @throws Exception
   */
  public void deleteRole(int id) throws Exception;

  /**
   * 更新角色操作项关联
   * @param info 执行信息
   * @param roleId 角色id
   * @param operationId 操作项id列表
   * @throws Exception
   */
  public void saveRoleOperationRela(int roleId, List<String> operationId) throws Exception;
}
