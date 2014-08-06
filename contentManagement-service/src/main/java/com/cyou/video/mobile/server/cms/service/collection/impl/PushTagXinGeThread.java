package com.cyou.video.mobile.server.cms.service.collection.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_PUSH_TAG_JOB_NAME;
import com.cyou.video.mobile.server.cms.service.push.PushTagXinGe173APPService;

public class PushTagXinGeThread extends PThread {
  private Logger logger = LoggerFactory.getLogger(PushTagOtherThread.class);

  PushTagXinGe173APPService pushTagXinGe173APPService;
  Query q;
  Map<String, Object> params;

  public PushTagXinGeThread(PushTagXinGe173APPService pushTagXinGe173APPService,Map<String, Object> params) {
    super();
    this.pushTagXinGe173APPService = pushTagXinGe173APPService;
    this.q = params.get("query") == null ? null : (Query) params.get("query");
    this.params=params;
  }

  @Override
  public void run() {
    try {
      getThreadService();
    }
    catch(Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void getThreadService() throws Exception {
    int total=0;
    PThread pThread = null;
    switch((COLLECTION_PUSH_TAG_JOB_NAME)params.get("jobType")) {
//      case COMBINATION_TAG :
//        PushTagCombination combination = (PushTagCombination) params.get("combination");
//        total = pushTagService.sendPushCombinationTags((int) start, (int) end, q, name, combination);
//        break;
      case USER_CHANNEL_TAG :
        total = pushTagXinGe173APPService.sendPushTagsChannel((int) start, (int) end, (Date) params.get("lastModifyDate"));
        break;
      case COLLECTION_UPATE :
        // pThread = new ServiceNameThread(clientLogCollectionService);
        break;
      case USER_REDUCE_TAG :
         total = pushTagXinGe173APPService.sendPushTags((int) start, (int) end, q, name);
        break;

      default :
        break;
    }
    
    logger.info(" this tread end =========================================================================");
    synchronized(pushTagXinGe173APPService) {
      pushTagXinGe173APPService.successLogEnd(name, total);
    }
  }
}
