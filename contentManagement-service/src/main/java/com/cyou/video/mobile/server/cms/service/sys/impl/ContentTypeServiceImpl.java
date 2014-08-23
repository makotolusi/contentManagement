package com.cyou.video.mobile.server.cms.service.sys.impl;

import java.util.Iterator;
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
import com.cyou.video.mobile.server.cms.model.security.ManageItem;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
import com.cyou.video.mobile.server.cms.model.sys.ContentTypeActionAndTag;
import com.cyou.video.mobile.server.cms.service.sys.ContentTypeService;

/**
 * 推送类型维护
 * 
 * @author lusi
 */
@Service("contentTypeService")
public class ContentTypeServiceImpl implements ContentTypeService {

  @Autowired
  private MongoOperations mongoTemplate;

  @Override
  public Pagination listContentType(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize = Integer.parseInt(params.get("limit").toString());
    params.put("curPage", curPage);
    params.put("pageSize", pageSize);
    Query q = new Query();
    q.with(new Sort(Direction.ASC, "index"));
    pagination.setRowCount((int) mongoTemplate.count(new Query(), ContentType.class));
    List<ContentType> conTypes = mongoTemplate.find(q, ContentType.class);
    pagination.setContent(conTypes);
    return pagination;
  }

  @Override
  public ContentType getByIndex(String index) throws Exception {
    return mongoTemplate.findOne(new Query(new Criteria("index").is(index)), ContentType.class);
  }

  // @Override
  public ContentType getById(String id) throws Exception {
    return mongoTemplate.findOne(new Query(new Criteria("id").is(id)), ContentType.class);
  }

  @Override
  public void createContentType(ContentType contentType) throws Exception {
    mongoTemplate.save(contentType);
  }

  @Override
  public void deleteContentType(ContentType contentType) throws Exception {
    mongoTemplate.remove(contentType);
  }

  @Override
  public void deleteAction(ContentTypeActionAndTag contentType) throws Exception {
    mongoTemplate.remove(contentType);
  }

  @Override
  public void updateContentType(ContentType contentType) throws Exception {
    // ContentType ct = mongoTemplate.findOne(new Query(new
    // Criteria("id").is(contentType.getId())), ContentType.class);
    // List<ContentType> cts = ct.getSubContentType();
    // if(cts != null) {
    // cts.add(contentType);
    // ct.setSubContentType(cts);
    // }
    mongoTemplate.save(contentType);
  }

  @Override
  public Pagination listContentTypeAction(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize = Integer.parseInt(params.get("limit").toString());
    params.put("curPage", curPage);
    params.put("pageSize", pageSize);
    Query q = new Query();
    q.with(new Sort(Direction.ASC, "index"));
    pagination.setRowCount((int) mongoTemplate.count(new Query(), ContentTypeActionAndTag.class));
    List<ContentTypeActionAndTag> conTypes = mongoTemplate.find(q, ContentTypeActionAndTag.class);
    for(Iterator iterator = conTypes.iterator(); iterator.hasNext();) {
      ContentTypeActionAndTag contentTypeActionAndTag = (ContentTypeActionAndTag) iterator.next();
      List<ContentType> actions = contentTypeActionAndTag.getAction();
      fullActionAndTag(actions);
      List<ContentType> tags = contentTypeActionAndTag.getTags();
      fullActionAndTag(tags);
    }
    pagination.setContent(conTypes);
    return pagination;
  }

  @Override
  public void fullActionAndTag( List<ContentType> ct) throws Exception {
    for(Iterator iterator2 = ct.iterator(); iterator2.hasNext();) {
      ContentType contentType = (ContentType) iterator2.next();
      ContentType newContentType = this.getById(contentType.getId());
      contentType.setName(newContentType.getName());
      contentType.setIndex(newContentType.getIndex());
      contentType.setTag(newContentType.getTag());
    }
  }

  @Override
  public void createContentTypeAction(ContentTypeActionAndTag action) throws Exception {
    mongoTemplate.save(action);
  }

  @Override
  public void updateContentTypeAction(ContentTypeActionAndTag action) throws Exception {
    // ContentTypeActionAndTag ac = mongoTemplate.findOne(new Query(new
    // Criteria("id").is(action.getId())),
    // ContentTypeActionAndTag.class);
    // ac.setAction(action.getAction());
    // ac.setTags(action.getTags());
    mongoTemplate.save(action);
  }
}
