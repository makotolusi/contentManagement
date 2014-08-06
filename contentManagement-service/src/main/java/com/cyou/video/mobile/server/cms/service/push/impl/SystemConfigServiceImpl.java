package com.cyou.video.mobile.server.cms.service.push.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.model.collection.SystemConfig;
import com.cyou.video.mobile.server.cms.service.push.SystemConfigService;

@Service("systemConfigService")
public class SystemConfigServiceImpl implements SystemConfigService{


  @Autowired
  private MongoOperations mongoTemplate;

  @Override
  public String getSystemConfigByConfigKey(String configKey) throws DataNotFoundException {
      SystemConfig systemConfig = mongoTemplate.findOne(new Query().addCriteria(new Criteria("key").is(configKey)), SystemConfig.class);
      if(systemConfig != null){
        return systemConfig.getValue();
      }
      throw new DataNotFoundException("没有找到"+configKey+"对应的系统配置信息");
  }

  @Override
  public void updateSystemConfigByConfigValue(SystemConfig systemConfig) {
     mongoTemplate.updateFirst(new Query().addCriteria(new Criteria("key").is(systemConfig.getKey())),new Update().set("value", systemConfig.getValue()), SystemConfig.class);
  }

}
