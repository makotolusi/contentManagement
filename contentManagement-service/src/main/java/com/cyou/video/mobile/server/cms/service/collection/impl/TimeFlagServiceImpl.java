package com.cyou.video.mobile.server.cms.service.collection.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cyou.video.mobile.server.cms.model.collection.PushTagLastUpdateTime;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.cms.service.collection.TimeFlagService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;

/**
 * 
 * @author lusi
 * 
 */
@Service("timeFlagService")
public class TimeFlagServiceImpl implements TimeFlagService {

  private Logger logger = LoggerFactory.getLogger(TimeFlagServiceImpl.class);

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @Autowired
  PushTagService pushTagService;

  @Autowired
  ThreadPoolTaskExecutor taskExecutor;

  @Override
  public void recover(PushTagLastUpdateTime oldLastUpdateTime) {
    clientLogCollectionService.updatePushTagLastUpdateTime(oldLastUpdateTime);
  }

  @Override
  public PushTagLastUpdateTime setTimestamp(Query query, String cname, String dname) {
    // 设置时间戳
    // 取得最新记录时间
    PushTagLastUpdateTime lastUpdateTime = clientLogCollectionService.getPushTagLastUpdateTime(cname);
    PushTagLastUpdateTime oldLastUpdateTime = lastUpdateTime;
    // 记录更新时间
    if(lastUpdateTime != null) {// 已经有更新时间
      if(query != null) query.addCriteria(Criteria.where(dname).gt(lastUpdateTime.getLastUpdateTime())); // 增量

    }
    lastUpdateTime = new PushTagLastUpdateTime(cname, new Date());
    clientLogCollectionService.updatePushTagLastUpdateTime(lastUpdateTime);
    return oldLastUpdateTime;
  }
}
