package com.cyou.video.mobile.server.cms.service.security.impl;

import org.springframework.stereotype.Service;

/**
 * CMS管理员业务实现
 * @author jyz
 */
@Service("managerService")
public class ManagerServiceImpl {//implements ManagerService {
  
//  private ManagerDao managerDao;

//  private OperationDao operationDao;


//  @Override
//  public int createManager(Manager manager) throws Exception {
//    if(manager == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager");
//    }
//    if(StringUtils.isBlank(manager.getUsername())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager.username");
//    }
//    if(StringUtils.isBlank(manager.getEmail())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager.email");
//    }
////    if(manager.getId() > 0) {
////      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.id");
////    }
//    if(managerDao.getManager(manager.getUsername()) != null) { //用名称作为管理员的唯一性限制
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_manager.username");
//    }
//    manager.setPassword(SecurityUtil.encryptMD5("password")); //密码用md5加密 初始化密码都是password
////    manager.setStatus(Constants.STATUS.ON.getValue());
////    int id = managerDao.createManager(manager);
////    if(id == 1 && manager.getId() > 0) {
////      id = manager.getId();
////    }
//    return 0;
//  }
//
//  @Override
//  public List<Operation> login(Manager manager, HttpServletRequest request) throws Exception {
//    List<Operation> list = null;
//    if(manager == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager");
//    }
//    if(StringUtils.isBlank(manager.getUsername())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager.username");
//    }
//    if(StringUtils.isBlank(manager.getPassword())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager.password");
//    }
//    if(request == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_request");
//    }
//    //查找是否存在指定名称的管理员
//    Manager reference = managerDao.getManager(manager.getUsername());
//    if(reference == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manager");
//    }
////    if(reference.getPassword().equals(SecurityUtil.encryptMD5(manager.getPassword()))) { //判断登录密码是否正确
////      if(reference.getStatus() == Constants.STATUS.ON.getValue()) { //判断管理员的状态是否可用
////        list = operationDao.listOperationByManager(reference.getId()); //查询当前管理员的所有操作项
////        if(list != null && list.size() > 0) {
////          request.getSession().setAttribute(Consts.SESSION_MANAGER, reference);
////        }
////        else {
////          throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_operation");
////        }
////      }
////      else {
////        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.status");
////      }
////    }
////    else {
////      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.password");
////    }
//    return list;
//  }
//
//  @Override
//  public void editPassword(String password, HttpServletRequest request) throws Exception {
//    if(StringUtils.isBlank(password)) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_password");
//    }
//    if(request == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_request");
//    }
//    Manager manager = (Manager) request.getSession().getAttribute(Consts.SESSION_MANAGER);
//    manager.setPassword(SecurityUtil.encryptMD5(password));
//    managerDao.updateManager(manager);
//  }
//
//  @Override
//  public List<Manager> listManager(int status, HttpServletRequest request) throws Exception {
//    if(request == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_request");
//    }
//    List<Manager> list = listManager(status);
//    if(list != null && list.size() > 0) {
//      Manager manager = (Manager) request.getSession().getAttribute(Consts.SESSION_MANAGER);
////      if(manager.getId() > 1) { //当前登录的管理员不是超级管理员时，过滤掉超级管理员。
////        if(status == Constants.STATUS.ON.getValue()) {
////          list.remove(0);
////        }
////      }
//    }
//    return list;
//  }
//
//  @Override
//  public List<Manager> listManager(int status) throws Exception {
//    if(status != Constants.STATUS.ON.getValue() && status != Constants.STATUS.OFF.getValue()) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "status");
//    }
//    return managerDao.listManager(status);
//  }
//
//  @Override
//  public void resetPassword(int managerId) throws Exception {
//    if(managerId < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.id");
//    }
//    Manager manager = managerDao.getManagerById(managerId);
//    if(manager == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manager");
//    }
//    manager.setPassword(SecurityUtil.encryptMD5("password")); //重置后的密码都是password
//    managerDao.updateManager(manager);
//  }
//
//  @Override
//  public void updateStatus(int managerId, int status) throws Exception {
//    if(managerId < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.id");
//    }
//    if(status == Constants.STATUS.ON.getValue() || status == Constants.STATUS.OFF.getValue()) {
//      Manager manager = managerDao.getManagerById(managerId);
//      if(manager == null) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manager");
//      }
////      manager.setStatus(status);
//      managerDao.updateManager(manager);
//    }
//    else {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.status");
//    }
//  }



 

 
}
