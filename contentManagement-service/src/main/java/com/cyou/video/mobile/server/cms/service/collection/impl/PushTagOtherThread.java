package com.cyou.video.mobile.server.cms.service.collection.impl;


public class PushTagOtherThread extends PThread {
//  private Logger logger = LoggerFactory.getLogger(PushTagOtherThread.class);
//
////  PushTagService pushTagService;
//  PushTagXinGeService pushTagXinGeService;
//  Query q;
//  Map<String, Object> params;
//
//  public PushTagOtherThread(PushTagService p, PushTagXinGeService pushTagXinGeService,Map<String, Object> params) {
//    super();
//    this.pushTagService = p;
//    this.pushTagXinGeService = pushTagXinGeService;
//    this.q = params.get("query") == null ? null : (Query) params.get("query");
//    this.params=params;
//  }
//
//  @Override
//  public void run() {
//    try {
//      getThreadService();
//    }
//    catch(Exception e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//  }
//
//  private void getThreadService() throws Exception {
//    int total=0;
//    PThread pThread = null;
//    switch((COLLECTION_PUSH_TAG_JOB_NAME)params.get("jobType")) {
//      case COMBINATION_TAG :
//        PushTagCombination combination = (PushTagCombination) params.get("combination");
//        total = pushTagService.sendPushCombinationTags((int) start, (int) end, q, name, combination);
//        break;
//      case USER_CHANNEL_TAG :
//        total = pushTagService.sendPushTagsChannel((int) start, (int) end, (Date) params.get("lastModifyDate"));
//        break;
//      case COLLECTION_UPATE :
//        // pThread = new ServiceNameThread(clientLogCollectionService);
//        break;
//       case WALKTHROUGH_APP_GAME_TAG:
//         total = pushTagXinGeService.sendBestWalkThroughInstalledGameTags((int) start, (int) end, q, name);
//       break;
//      case USER_REDUCE_TAG :
//         total = pushTagService.sendPushTags((int) start, (int) end, q, name);
//        break;
//
//      default :
//        break;
//    }
//    
//    logger.info(" this tread end =========================================================================");
//    synchronized(pushTagService) {
//      pushTagService.successLogEnd(name, total);
//    }
//  }
}
