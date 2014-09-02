package com.cyou.video.mobile.server.cms.service.security.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.model.VerifyException;
import com.cyou.video.mobile.server.cms.model.security.ManageItem;
import com.cyou.video.mobile.server.cms.model.security.Manager;
import com.cyou.video.mobile.server.cms.model.security.Operation;
import com.cyou.video.mobile.server.cms.model.security.Role;
import com.cyou.video.mobile.server.cms.service.security.ManageItemService;
import com.cyou.video.mobile.server.cms.service.security.OperationService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * CMS管理项业务实现
 * 
 * @author jyz
 */
@Service("manageItemService")
public class ManageItemServiceImpl implements ManageItemService {

  @Autowired
  private MongoOperations mongoTemplate;

  @Autowired
  OperationService operationService;

  @Override
  public List<ManageItem> listByRole(Manager sessionManager) throws Exception {
    List<Operation> list = operationService.listOperationOfRole(sessionManager.getRoleids(), 0);
    Map<String, List<Operation>> itemId=new HashMap<String, List<Operation>>();
    List<ManageItem> result=new ArrayList<ManageItem>();
    for(Iterator iterator = list.iterator(); iterator.hasNext();) {
      Operation operation = (Operation) iterator.next();
      if(itemId.containsKey(operation.getManageItemId())){
        itemId.get(operation.getManageItemId()).add(operation);
      }else{
        List<Operation> op=new ArrayList<Operation>();
        op.add(operation);
        itemId.put(operation.getManageItemId(), op);
      }
    }
    Query q = new Query();
    q.with(new Sort(Direction.ASC, "orderNum"));
    List<ManageItem> all= mongoTemplate.find(q, ManageItem.class);
    for(Iterator iterator = all.iterator(); iterator.hasNext();) {
      ManageItem manageItem = (ManageItem) iterator.next();
      if(itemId.containsKey(manageItem.getId())){
        manageItem.setOperations(itemId.get(manageItem.getId()));
        result.add(manageItem);
      }else{
        continue;
      }
    }
    return result;
  }

  @Override
  public List<ManageItem> list() throws Exception {
    return mongoTemplate.find(new Query(), ManageItem.class);
  }
  //
  @Override
  public void createManageItem(ManageItem manageItem) throws Exception {
    if(manageItem == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manageItem");
    }
    List<ManageItem> list = mongoTemplate.findAll(ManageItem.class);
    manageItem.setOrderNum(list.size() + 1); // 新增加的管理项的顺序放到最后
    mongoTemplate.save(manageItem);
  }
  //
  // @Override
  // public void updateManageItem(ManageItem manageItem) throws Exception {
  // if(manageItem == null) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
  // Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_manageItem");
  // }
  // if(StringUtils.isBlank(manageItem.getName())) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
  // Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() +
  // "_manageItem.name");
  // }
  // // if(manageItem.getId() < 1) {
  // // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
  // // Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() +
  // "_manageItem.id");
  // // }
  // ManageItem reference =null;//
  // manageItemDao.getManageItemById(manageItem.getId());
  // if(reference == null) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(),
  // Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manageItem");
  // }
  // // 用名称作为管理项的唯一性限制
  // if(!reference.getName().equals(manageItem.getName())) {
  // if(manageItemDao.getManageItem(manageItem.getName()) != null) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(),
  // Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() +
  // "_manageItem.name");
  // }
  // }
  // reference.setName(manageItem.getName());
  // reference.setUrl(manageItem.getUrl());
  // manageItemDao.updateManageItem(reference);
  // }
  //
  // @Override
  // public void deleteManageItem(int id) throws Exception {
  // if(id < 1) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
  // Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() +
  // "_manageItem.id");
  // }
  // // roleOperationRelaDao.deleteRoleOperationRelaByManageItem(id);
  // operationDao.deleteOperationByManageItem(id);
  // manageItemDao.deleteManageItem(id);
  // }
  //
  // @Override
  // public void updateStatus(int manageItemId, int status) throws Exception {
  // if(manageItemId < 1) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
  // Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() +
  // "_manageItem.id");
  // }
  // if(status != Constants.STATUS.ON.getValue() && status !=
  // Constants.STATUS.OFF.getValue()) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
  // Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() +
  // "_manageItem.status");
  // }
  // ManageItem manageItem = manageItemDao.getManageItemById(manageItemId);
  // if(manageItem == null) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(),
  // Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_manageItem");
  // }
  // // manageItem.setStatus(status);
  // manageItemDao.updateManageItem(manageItem);
  // }
  //
  // @Override
  // public void updateOrder(Map<String, Integer> orderMap) throws Exception {
  // if(orderMap == null || orderMap.size() == 0) {
  // throw new
  // VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
  // Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_orderMap");
  // }
  // // 更新管理项顺序。orderMap中key是管理项id，value是顺序
  // for(Map.Entry<String, Integer> entry : orderMap.entrySet()) {
  // manageItemDao.updateManageItemOrder(Integer.parseInt(entry.getKey()),
  // entry.getValue());
  // }
  // }

}
