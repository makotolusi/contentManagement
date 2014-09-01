package com.cyou.video.mobile.server.cms.service.security.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
  public List<Operation> listOperationOfRole(List<String> roleId, int out) throws Exception {
    Query q = null;
    if(out == 1)
      q = new Query(new Criteria("roleIds").nin(roleId));
    else if(out == 0) {
      if(StringUtils.isEmpty(roleId))
        return new ArrayList<Operation>();
      else
        q = new Query(new Criteria("roleIds").all(roleId));
    }
    else if(out == 3) q = new Query();
    return mongoTemplate.find(q, Operation.class);
  }

  @Override
  public boolean containsOperationOfRoles(List<String> roleId, String operation) throws Exception {
    Query q = new Query(new Criteria("roleIds").all(roleId).and("url").is(operation));
    List l= mongoTemplate.find(q, Operation.class);
    if(l==null||l.isEmpty()){
      return false;
    }else{
      return true;
    }
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
    mongoTemplate.insert(operation);
    List<Operation> ops = manageItem.getOperations();
    Operation rel = new Operation();
    rel.setId(operation.getId());
    if(ops == null) {
      ops = new ArrayList<Operation>();
    }
    ops.add(rel);
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
  public void deleteOperation(Operation op) throws Exception {
    if(op == null) {
      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),
          Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_operation.id");
    }
    mongoTemplate.remove(op);
    ManageItem manageItem = mongoTemplate.findOne(new Query(new Criteria("id").is(op.getManageItemId())),
        ManageItem.class);
    List<Operation> ops = manageItem.getOperations();
    for(int i = 0; i < ops.size(); i++) {
      Operation o = (Operation) ops.get(i);
      if(o == null) {
        ops.remove(o);
        i--;
      }
    }
    manageItem.setOperations(ops);
    mongoTemplate.save(manageItem);
  }

  @Override
  public void addRole(String[] ids, String roleId) throws Exception {
    List<Operation> ops = mongoTemplate.find(new Query(new Criteria("_id").in(ids)), Operation.class);
    for(Iterator iterator = ops.iterator(); iterator.hasNext();) {
      Operation op = (Operation) iterator.next();
      List<String> roleIds = op.getRoleIds();
      if(roleIds == null) roleIds = new ArrayList<String>();
      roleIds.add(roleId);
      op.setRoleIds(roleIds);
      mongoTemplate.save(op);
    }
  }
}
