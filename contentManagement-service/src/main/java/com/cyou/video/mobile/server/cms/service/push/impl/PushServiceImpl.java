package com.cyou.video.mobile.server.cms.service.push.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_JOB_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_TYPE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.VerifyException;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.cyou.video.mobile.server.cms.service.push.PushService;
import com.cyou.video.mobile.server.cms.service.sys.SystemConfigService;
import com.cyou.video.mobile.server.cms.service.utils.DateUtils;
import com.cyou.video.mobile.server.cms.service.utils.HttpUtils;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.HttpUtil;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;

/**
 * 意见反馈业务实现
 * 
 * @author jyz
 */
@Service("pushService")
public class PushServiceImpl implements PushService {

  private Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);

  @Autowired
  private SystemConfigService systemConfigService;

  @Autowired
  PushInterface xingePush;

  @Autowired
  private MongoOperations mongoTemplate;

  @Override
  public String createPush(Push push) {
    try {
      if(push == null) {
        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
            Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_push");
      }
      mongoTemplate.insert(push);
      return push.getId();
    }
    catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public String savePush(Push push) {
    try {
      if(push == null) {
        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
            Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_push");
      }
      mongoTemplate.save(push);
      return push.getId();
    }
    catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public int updatePush(Push push) {
    try {
      if(push == null) {
        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),
            Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_push");
      }
      mongoTemplate.updateFirst(
          new Query().addCriteria(new Criteria("id").is(push.getId())),
          new Update().set("title", push.getTitle()).set("content", push.getContent()).set("tags", push.getTags())
              .set("clientType", push.getClientType()).set("userScope", push.getUserScope()), Push.class);
      // pushDao.updatePush(push);
      return 1;
    }
    catch(Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public void readPush(int id) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public Push getPushById(String id) throws Exception {
    Push p = mongoTemplate.findOne(new Query().addCriteria(new Criteria("_id").is(id)), Push.class);
    if(p.getPushType() == Consts.PUSH_TYPE.TIMING) {
      try {
        Map<String, String> map = this.postGetTriggerInfo(p.getId());
        if(map != null) {
          p.setTirggerName(map.get("name"));
          p.setStartTime(map.get("startTime"));
          p.setPreviousFireTime(map.get("previousFireTime"));
          p.setNextFireTime(map.get("nextFireTime"));
          p.setCronExpression(map.get("cronExpression"));
        }
      }
      catch(Exception e) {
        e.printStackTrace();
        logger.error("get job info failed :" + e.getMessage());

      }
    }
    return p;
  }

  @Override
  public Push getPushByPreId(int preId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * 调用cms-job创建新quartz任务
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Override
  public JSONObject postNewJob(String pushId, String cronExpress) throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("expression", cronExpress);
    params.put("pushId", pushId);
    String str = HttpUtil.syncPost(systemConfigService.getByKey("job_url") + "/job/push/newJob", params, null);
    return new JSONObject(str);
  }

  /**
   * 调用cms-job删除quartz任务
   * 
   * @param pushId
   *          push编号
   * @return
   * @throws Exception
   */
  @Override
  public JSONObject deleteJob(String pushId) throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("pushId", pushId + "");
    String str = HttpUtil.syncPost(systemConfigService.getByKey("job_url") + "/job/push/deleteJob", params, null);
    return new JSONObject(str);
  }

  /**
   * 调用cms-job停用quartz任务
   * 
   * @param pushId
   *          push编号
   * @return
   * @throws Exception
   */
  private String pauseJob(String pushId) throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("pushId", pushId + "");
    return HttpUtil.syncPost(systemConfigService.getByKey("job_url") + "/job/push/pauseJob", params, null);
  }

  /**
   * 调用cms-job启用quartz任务
   * 
   * @param pushId
   *          push编号
   * @return
   * @throws Exception
   */
  private String resumeJob(String pushId) throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("pushId", pushId + "");
    return HttpUtil.syncPost(systemConfigService.getByKey("job_url") + "/job/push/resumeJob", params, null);
  }

  /**
   * 调用cms-job修改quartz任务的cron表达式
   * 
   * @param pushId
   *          push编号
   * @param expression
   *          cron表达式
   * @return
   * @throws Exception
   */
  @Override
  public JSONObject modifyJob(String pushId, String expression) throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("pushId", pushId + "");
    params.put("expression", expression + "");
    String str = HttpUtil.syncPost(systemConfigService.getByKey("job_url") + "/job/push/updateTrigger", params, null);
    return new JSONObject(str);
  }

  /**
   * 调用cms-job获得quartz任务信息
   * 
   * @param pushId
   *          push编号
   * @return
   * @throws Exception
   */
  public Map<String, String> postGetTriggerInfo(String pushId) throws Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("pushId", pushId + "");
    String result = HttpUtil.syncPost(systemConfigService.getByKey("job_url") + "/job/push/getTriggerInfo", params,
        null);
    if(StringUtils.isEmpty(result)) {
      logger.info("job return msg is blank push id is " + pushId);
      return null;
    }
    else {
      @SuppressWarnings("unchecked")
      Map<String, String> map = JacksonUtil.getJsonMapper().readValue(result, Map.class);
      return map;
    }
  }

  @Override
  public Pagination listPush(Map<String, Object> params) throws Exception {
    Pagination pagination = new Pagination();
    int curPage = Integer.parseInt(params.get("start").toString());
    int pageSize = Integer.parseInt(params.get("limit").toString());
    params.put("curPage", curPage);
    params.put("pageSize", pageSize);
    pagination.setRowCount((int) mongoTemplate.count(this.getQuery(params), Push.class));
    List<Push> pushs = mongoTemplate.find(this.getQuery(params), Push.class);
    // List<Push> result = new ArrayList<Push>();
    pagination.setContent(pushs);
    // if(org.springframework.util.StringUtils.isEmpty(params.get("pushType"))
    // || Consts.PUSH_TYPE.valueOf(params.get("pushType").toString()) ==
    // Consts.PUSH_TYPE.IMMEDIATE) {
    // pagination.setContent(pushs);
    // }
    // else {
    // int flag = 0;
    // for(Iterator<Push> iterator = pushs.iterator(); iterator.hasNext();) {
    // Push push = iterator.next();
    // if(Consts.PUSH_TYPE.valueOf(params.get("pushType").toString()) ==
    // Consts.PUSH_TYPE.TIMING) {
    // try {
    // Map<String, String> map = this.postGetTriggerInfo(push.getId());
    // if(map != null) {
    // push.setTirggerName(map.get("name"));
    // push.setStartTime(map.get("startTime"));
    // push.setPreviousFireTime(map.get("previousFireTime"));
    // push.setNextFireTime(map.get("nextFireTime"));
    // if(org.springframework.util.StringUtils.isEmpty(map.get("nextFireTime"))){
    // push.setJobState(PUSH_JOB_STATE.DISABLE);
    // push.setJobState(PUSH_JOB_STATE.DISABLE);
    // this.updateJobStateById(push);
    // }
    // push.setCronExpression(map.get("cronExpression"));
    // }
    // }
    // catch(Exception e) {
    // flag = 1;
    // logger.error("get job info failed :" + e.getMessage());
    // break;
    // }
    // }
    // result.add(push);
    // }
    // if(flag == 1) {
    // pagination.setContent(pushs);
    // }
    // else {
    // pagination.setContent(result);
    // }
    // }
    return pagination;
  }

  public Query getQuery(Map<String, Object> params) throws Exception {
    Query query = new Query();
    if(!StringUtils.isEmpty((String) params.get("platForm"))) {
      query.addCriteria(new Criteria("platForm").is(params.get("platForm")));
    }
    if(params.get("appId") != null) {
      query.addCriteria(new Criteria("appId").is((Integer)params.get("appId")));
    }
    if(!StringUtils.isEmpty((String) params.get("title"))) {
      Pattern pattern = Pattern.compile("^.*" + params.get("title").toString() + ".*$");
      query.addCriteria(Criteria.where("title").regex(pattern));
    }
    if(!StringUtils.isEmpty((String) params.get("clientType"))) {
      query.addCriteria(new Criteria("clientType").is(params.get("clientType")));
    }
    if(!StringUtils.isEmpty((String) params.get("jobState"))) {
      query.addCriteria(new Criteria("jobState").is(params.get("jobState")));
    }
    if(!StringUtils.isEmpty((String) params.get("pushType"))) {
      query.addCriteria(new Criteria("pushType").is(params.get("pushType")));
    }
    if(!StringUtils.isEmpty((String) params.get("sendState"))) {
      query.addCriteria(new Criteria("sendState").is(params.get("sendState")));
    }
    if(!StringUtils.isEmpty((String) params.get("userScope"))) {
      query.addCriteria(new Criteria("userScope").is(params.get("userScope")));
    }
    if(!org.springframework.util.StringUtils.isEmpty(params.get("curPage"))) {
      query.limit((Integer) params.get("pageSize"));
      query.skip((Integer) params.get("curPage"));
    }
    query.with(new Sort(Sort.Direction.DESC, "sendDate"));
    return query;
  }

  @Override
  public void modifyStateById(String id, PUSH_JOB_STATE state) throws Exception {
    if(state == PUSH_JOB_STATE.DISABLE) {
      if(pauseJob(id).indexOf("true") >= 0)
        mongoTemplate.updateFirst(new Query().addCriteria(new Criteria("id").is(id)),
            new Update().set("jobState", state), Push.class);
    }
    else {
      if(resumeJob(id).indexOf("true") >= 0)
        mongoTemplate.updateFirst(new Query().addCriteria(new Criteria("id").is(id)),
            new Update().set("jobState", state), Push.class);
    }
  }

  @Override
  public void modifyAutoPushStateById(int id, PUSH_JOB_STATE state) throws Exception {
    // pushDao.updateStateById(id, state);
  }

  @Override
  public void modifyAutoPushStateById(String id, PUSH_JOB_STATE state) throws Exception {
    mongoTemplate.updateFirst(new Query().addCriteria(new Criteria("_id").is(id)), new Update().set("jobState", state),
        Push.class);
  }

  @Override
  public void updateSendStateById(Push push) {
    try {
      mongoTemplate.updateFirst(new Query().addCriteria(new Criteria("id").is(push.getId())),
          new Update().set("sendState", push.getSendState()).set("sentLogs", push.getSentLogs()), Push.class);
    }
    catch(Exception e) {
      logger.error("update send state erro" + e.getMessage());
    }
  }

  @Override
  public void updateJobStateById(Push push) {
    try {
      // mongoTemplate.updateFirst(new Query().addCriteria(new
      // Criteria("id").is(push.getId())),
      // new Update().set("jobState", push.getJobState()), Push.class);
      mongoTemplate.save(push);
    }
    catch(Exception e) {
      logger.error("update send state erro" + e.getMessage());
    }
  }

  @Override
  public void deletePush(String id) throws Exception {
    Push push = new Push();
    push.setId(id);
    mongoTemplate.remove(push);
  }

  @Override
  public void autoPush(Push push) throws Exception {
    push.setPushType(PUSH_TYPE.AUTO);
    xingePush.pushTag(push);
  }

  public void autoPush(String title, String content, CLIENT_TYPE clientType, String gameCode, String serviceId)
      throws Exception {
    Push push = new Push();
    push.setTitle(title);
    push.setClientType(clientType);
    push.setContent(content);
    push.setPushType(PUSH_TYPE.AUTO);
    this.autoPush(push);
  }

  @Override
  public Push pushInfo(Push push) throws Exception {
    switch(push.getUserScope()) {
      case ALL :
        return xingePush.pushAll(push);
      case SINGLE :
        break;
      case TAG :
        return xingePush.pushTag(push);
      default :
        break;
    }
    return push;
  }

}
