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
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
import com.cyou.video.mobile.server.cms.service.sys.ConfigAppsService;
import com.cyou.video.mobile.server.cms.service.sys.ContentTypeService;

/**
 * 推送类型维护
 * 
 * @author zs
 */
@Service("configAppsService")
public class ConfigAppsServiceImpl implements ConfigAppsService {

  @Autowired
  private MongoOperations mongoTemplate;

  @Override
  public Pagination listContentType(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize = Integer.parseInt(params.get("limit").toString());
    params.put("curPage", curPage);
    params.put("pageSize", pageSize);
    Query q=new Query();
    q.with(new Sort(Direction.ASC, "index"));
    pagination.setRowCount((int) mongoTemplate.count(new Query(), ContentType.class));
    List<ConfigApps> conTypes = mongoTemplate.find(q,ConfigApps.class);
    pagination.setContent(conTypes);
    return pagination;
  }

  @Override
  public ConfigApps findByAppid(Integer appId) throws Exception {
    return mongoTemplate.findOne(new Query(new Criteria("appIds").is(appId)),ConfigApps.class);
  }
  @Override
  public void createContentType(ConfigApps configApps) throws Exception {
    mongoTemplate.save(configApps);
  }
  @Override
  public void deleteContentType(ConfigApps configApps) throws Exception {
    mongoTemplate.remove(configApps);
  }
  @Override
  public void updateContentType(ConfigApps configApps) throws Exception {
//    ContentType ct = mongoTemplate.findOne(new Query(new Criteria("id").is(contentType.getId())), ContentType.class);
//    List<ContentType> cts = ct.getSubContentType();
//    if(cts != null) {
//      cts.add(contentType);
//      ct.setSubContentType(cts);
//    }
    mongoTemplate.save(configApps);
  }

}
