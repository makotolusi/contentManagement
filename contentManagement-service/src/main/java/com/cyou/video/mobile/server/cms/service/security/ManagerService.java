package com.cyou.video.mobile.server.cms.service.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cyou.video.mobile.server.cms.model.security.Manager;
import com.cyou.video.mobile.server.cms.model.security.Operation;


/**
 * CMS管理员业务接口
 * @author jyz
 */
public interface ManagerService {

  int createManager(Manager manager) throws Exception;

  List<Operation> login(Manager manager, HttpServletRequest request) throws Exception;

  void editPassword(String password, HttpServletRequest request) throws Exception;

  List<Manager> listManager(int status, HttpServletRequest request) throws Exception;

  List<Manager> listManager(int status) throws Exception;

  void resetPassword(int managerId) throws Exception;

  void updateStatus(int managerId, int status) throws Exception;

  
}
