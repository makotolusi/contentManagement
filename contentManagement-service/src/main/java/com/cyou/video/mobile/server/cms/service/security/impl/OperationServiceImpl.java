package com.cyou.video.mobile.server.cms.service.security.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.model.VerifyException;
import com.cyou.video.mobile.server.cms.model.security.ManageItem;
import com.cyou.video.mobile.server.cms.model.security.Operation;
import com.cyou.video.mobile.server.cms.service.security.OperationService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * CMS操作项业务实现
 * 
 * @author jyz
 */
@Service("operationService")
public class OperationServiceImpl implements OperationService {

  @Autowired
  private MongoOperations mongoTemplate;

  @Override
  public List<Operation> listOperation() throws Exception {
    List<Operation> all = new ArrayList<Operation>();
    List<ManageItem> manageItems = mongoTemplate.findAll(ManageItem.class);
    for(Iterator iterator = manageItems.iterator(); iterator.hasNext();) {
      ManageItem manageItem = (ManageItem) iterator.next();
      List<Operation> ops = manageItem.getOperations();
      if(ops != null) all.addAll(ops);
    }
    return all;
  }

  @Override
  public List<Operation> listOperationOfRole(String roleId) throws Exception {
    List<Operation> all = new ArrayList<Operation>();
    Aggregation agg = Aggregation.newAggregation(Aggregation.match(new Criteria("operations.roleIds").all(roleId)),
        Aggregation.unwind("operations"));
    AggregationResults<ManageItem> results = mongoTemplate.aggregate(agg, ManageItem.COLLECTION_NAME, ManageItem.class);
    List<ManageItem> manageItems = results.getMappedResults();
    for(Iterator iterator = manageItems.iterator(); iterator.hasNext();) {
      ManageItem manageItem = (ManageItem) iterator.next();
      List<Operation> ops = manageItem.getOperations();
      if(ops != null) 
        all.addAll(ops);
    }
    return all;
  }

  @Override
  public List<Operation> listOperationExcludeRole(String roleId) throws Exception {
    List<ManageItem> manageItems = mongoTemplate.find(new Query(new Criteria("operations.roleIds").all(roleId)),
        ManageItem.class);
    List<Operation> all = new ArrayList<Operation>();
    for(Iterator iterator = manageItems.iterator(); iterator.hasNext();) {
      ManageItem manageItem = (ManageItem) iterator.next();
      List<Operation> ops = manageItem.getOperations();
      if(ops != null) all.addAll(ops);
    }
    return all;
  }

  @Override
  public void createOperation(Operation operation) throws Exception {
    if(operation == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_operation");
    }
    // relation
    ManageItem manageItem = mongoTemplate.findOne(new Query(new Criteria("id").is(operation.getManageItemId())),
        ManageItem.class);
    List<Operation> ops = manageItem.getOperations();
    if(ops == null) {
      ops = new ArrayList<Operation>();
    }
    int order = ops.size();
    operation.setOrderId(order + 1);
    ops.add(operation);
    manageItem.setOperations(ops);
    mongoTemplate.save(manageItem);// 更新菜单下的功能列表
  }

  @Override
  public void updateOperation(Operation operation) throws Exception {
    if(operation == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_operation");
    }
    mongoTemplate.save(operation);
  }

  @Override
  public void deleteOperation(int id) throws Exception {
    if(id < 1) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_operation.id");
    }
    // roleOperationRelaDao.deleteRoleOperationRelaByOperation(id);
    // //删除每一个操作项都要先删除与角色的关联关系
    // operationDao.deleteOperation(id);
  }

  @Override
  public void addRole(String manageItemId, int orderId, String roleId) throws Exception {
    ManageItem manageItem = mongoTemplate.findOne(new Query(new Criteria("operations.manageItemId").is(manageItemId)
        .and("operations.orderId").is(orderId)), ManageItem.class);
    List<String> roleIds = manageItem.getOperations().get(0).getRoleIds();
    if(roleIds == null) roleIds = new ArrayList<String>();
    roleIds.add(roleId);
    manageItem.getOperations().get(0).setRoleIds(roleIds);
    mongoTemplate.save(manageItem);
  }
}
