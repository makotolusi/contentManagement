package com.cyou.video.mobile.server.cms.job.push;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.cyou.video.mobile.server.cms.service.push.PushService;
import com.cyou.video.mobile.server.cms.service.push.impl.BaiduPush;
import com.cyou.video.mobile.server.cms.service.push.impl.XinGePush;

@Component("pushJob")
public class PushJob implements Job {

  private Logger logger = LoggerFactory.getLogger(PushJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    logger.info("push job start =============== ");
    try {
      SchedulerContext schedulerContext = context.getScheduler().getContext();
      ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get("applicationContextKey");
      PushInterface baiduPush = (BaiduPush) applicationContext.getBean("baiduPush");
      PushInterface xingePush = (XinGePush) applicationContext.getBean("xingePush");
      PushService pushService = (PushService) applicationContext.getBean("pushService");
      String pushId = context.getJobDetail().getJobDataMap().get("pushId").toString();
      logger.info("push job start ===============  push id is " + pushId);
      Push push = pushService.getPushById(pushId);
      switch(push.getUserScope()) {
        case ALL :
          if(Consts.PUSH_PLATFORM_TYPE.BAIDU.equals(push.getPlatForm())) {
            push = baiduPush.pushAll(push);
          }
          if(Consts.PUSH_PLATFORM_TYPE.XINGE.equals(push.getPlatForm())) {
            push = xingePush.pushAll(push);
          }
          break;
        case TAG :
          if(Consts.PUSH_PLATFORM_TYPE.BAIDU.equals(push.getPlatForm())) {
            push = baiduPush.pushTag(push);
          }
          if(Consts.PUSH_PLATFORM_TYPE.XINGE.equals(push.getPlatForm())) {
            push = xingePush.pushTag(push);
          }
          break;
        default :
          break;
      }
      pushService.updateSendStateById(push);
    }
    catch(Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      logger.error("not get push object by id : " + e.getMessage());
    }

  }

}
