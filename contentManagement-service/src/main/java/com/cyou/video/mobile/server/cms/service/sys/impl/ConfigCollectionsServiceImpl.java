package com.cyou.video.mobile.server.cms.service.sys.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.ConfigApps;
import com.cyou.video.mobile.server.cms.model.sys.ConfigCollections;
import com.cyou.video.mobile.server.cms.service.sys.ConfigCollectionsService;

/**
 * 推送类型维护
 * 
 * @author zs
 */
@Service("configCollectionsService")
public class ConfigCollectionsServiceImpl implements ConfigCollectionsService {

  @Autowired
  private MongoOperations mongoTemplate;

  @Override
  public Pagination list(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize = Integer.parseInt(params.get("limit").toString());
    params.put("curPage", curPage);
    params.put("pageSize", pageSize);
    Query q = new Query();
    q.with(new Sort(Direction.ASC, "index"));
    pagination.setRowCount((int) mongoTemplate.count(new Query(), ConfigCollections.class));
    List<ConfigCollections> conTypes = mongoTemplate.find(q, ConfigCollections.class);
    pagination.setContent(conTypes);
    return pagination;
  }

  @Override
  public ConfigApps findByAppid(Integer appId) throws Exception {
    return mongoTemplate.findOne(new Query(new Criteria("appIds").is(appId)), ConfigApps.class);
  }

  @Override
  public void save(ConfigCollections configCollections) throws Exception {
    mongoTemplate.save(configCollections);
  }

  @Override
  public void delete(ConfigCollections configCollections) throws Exception {
    mongoTemplate.remove(configCollections);
  }

}
