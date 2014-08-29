package com.cyou.video.mobile.server.cms.service.security.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.VerifyException;
import com.cyou.video.mobile.server.cms.model.security.Manager;
import com.cyou.video.mobile.server.cms.model.security.Operation;
import com.cyou.video.mobile.server.cms.model.security.Role;
import com.cyou.video.mobile.server.cms.service.security.ManagerService;
import com.cyou.video.mobile.server.cms.service.security.OperationService;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.SecurityUtil;

/**
 * CMS管理员业务实现
 * 
 * @author jyz
 */
@Service("managerService")
public class ManagerServiceImpl implements ManagerService {

  @Autowired
  private MongoOperations mongoTemplate;

  @Autowired
  OperationService operationService;
  
  @Override
  public int createManager(Manager manager) throws Exception {
    if(StringUtils.isEmpty(manager.getId())) {
      if(manager == null) {
        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
            Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager");
      }
      manager.setPassword(SecurityUtil.encryptMD5("password")); // 密码用md5加密
      manager.setStatus(Constants.STATUS.ON);
      mongoTemplate.save(manager);
    }
    else {
      Manager m = mongoTemplate.findOne(new Query(new Criteria("id").is(manager.getId())), Manager.class);
      m.setEmail(manager.getEmail());
      m.setUsername(manager.getUsername());
      m.setRoles(manager.getRoles());
      mongoTemplate.save(m);
    }
    return 0;
  }

  public Manager findOne(Manager manager) throws Exception {
     return mongoTemplate.findOne(new Query(new Criteria("username").is(manager.getUsername())), Manager.class);
  }
  @Override
  public List<Operation> login(Manager manager, HttpServletRequest request) throws Exception {
    List<Operation> list = null;
    if(manager == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager");
    }
    if(StringUtils.isEmpty(manager.getUsername())) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager.username");
    }
    if(StringUtils.isEmpty(manager.getPassword())) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manager.password");
    }
    if(request == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_request");
    }
    // 查找是否存在指定名称的管理员
    Manager reference = this.findOne(manager);
    if(reference == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(),"账号或密码有误！");
    }
    if(reference.getPassword().equals(SecurityUtil.encryptMD5(manager.getPassword()))) { // 判断登录密码是否正确
      if(reference.getStatus() == Constants.STATUS.ON) {
        // 判断管理员的状态是否可用
        List<Role> roles= reference.getRoles();
        List<String> roleids=new ArrayList<String>();
        for(Iterator iterator = roles.iterator(); iterator.hasNext();) {
          Role role = (Role) iterator.next();
          roleids.add(role.getId());
        }
        reference.setRoleids(roleids);
        list = operationService.listOperationOfRole(roleids, 0);
        // 查询当前管理员的所有操作项
        if(list != null && list.size() > 0) {
          request.getSession().setAttribute(Consts.SESSION_MANAGER, reference);
        }
        else {
          throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(),
              Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_operation");
        }
      }
      else {
        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
            Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.status");
      }
    }
    else {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_manager.password");
    }
    return list;
  }

  @Override
  public void editPassword(String password, HttpServletRequest request) throws Exception {
    if(StringUtils.isEmpty(password)) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_password");
    }
    Manager manager = (Manager) request.getSession().getAttribute(Consts.SESSION_MANAGER);
    manager.setPassword(SecurityUtil.encryptMD5(password));
    mongoTemplate.save(manager);
  }

  @Override
  public Pagination listManager(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize = Integer.parseInt(params.get("limit").toString());
    params.put("curPage", curPage);
    params.put("pageSize", pageSize);
    Query query = new Query();
    query.limit((Integer) params.get("pageSize"));
    query.skip((Integer) params.get("curPage"));
    pagination.setRowCount((int) mongoTemplate.count(query, Manager.class));
    List<Manager> managers = mongoTemplate.find(query, Manager.class);
    // if(list != null && list.size() > 0) {
    // Manager manager = (Manager)
    // request.getSession().getAttribute(Consts.SESSION_MANAGER);
    // if(manager.getId() > 1) { //当前登录的管理员不是超级管理员时，过滤掉超级管理员。
    // if(status == Constants.STATUS.ON.getValue()) {
    // list.remove(0);
    // }
    // }
    // }
    pagination.setContent(managers);
    return pagination;
  }

  @Override
  public void resetPassword(String managerId) throws Exception {
    Manager manager = mongoTemplate.findOne(new Query(new Criteria("id").is(managerId)), Manager.class);
    if(manager == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(),
          Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manager");
    }
    manager.setPassword(SecurityUtil.encryptMD5("password")); // 重置后的密码都是password
    mongoTemplate.save(manager);
  }

  @Override
  public void updateStatus(String managerId, Constants.STATUS status) throws Exception {
    status=(status==Constants.STATUS.ON?Constants.STATUS.OFF:Constants.STATUS.ON);
    Manager manager = mongoTemplate.findOne(new Query(new Criteria("id").is(managerId)), Manager.class);
    if(manager == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(),
          Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manager");
    }
    manager.setStatus(status);
    mongoTemplate.save(manager);
  }

}
