package com.cyou.video.mobile.server.cms.job.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.cyou.video.mobile.server.common.Constants;

@Component
public class PushScheduler {

  private Logger logger = LoggerFactory.getLogger(PushJobController.class);

  @Autowired
  private SchedulerFactoryBean schedulerFactoryBean;

  private Scheduler scheduler = null;

  private static final String TRIGER = "trigger_";

  private static final String JOB_NAME = "push_job_";

  private static final String GROUP_NAME = "group_job_";

  public static void main(String[] args) throws Exception {
    PushScheduler pushScheduler = new PushScheduler();
  }

  public void startScheduler() throws Exception {
    this.initScheduler();
    JobDetail jobDetail = new JobDetail("PushJob", "PushGroup", PushJob.class);
    scheduler.start();
  }

  public Scheduler initScheduler() {
    try {
      scheduler = schedulerFactoryBean.getScheduler();
      return scheduler;
      // this.scheduler= StdSchedulerFactory.getDefaultScheduler();
    }
    catch(Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 新触发器
   * 
   * @throws org.quartz.SchedulerException
   */
  public void updateTriger(String pushId,String expression) throws Exception {
        // retrieve the trigger
        CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(JOB_NAME + TRIGER + pushId + "_0", JOB_NAME + pushId);
        oldTrigger.setCronExpression(expression);
        scheduler.rescheduleJob(JOB_NAME + TRIGER + pushId + "_0", JOB_NAME + pushId, oldTrigger);
  }

  /**
   * 新触发器状态
   * 
   * @throws org.quartz.SchedulerException
   */
  public int  getTrigerState(String pushId) throws Exception {
        return scheduler.getTriggerState(JOB_NAME + TRIGER + pushId + "_0", JOB_NAME + pushId);
  }
  
  /**
   * 新触发器
   * 
   * @throws Exception
   * @throws org.quartz.SchedulerException
   */
  public Map<String, Object> newPushJob(String pushId, String expression) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    JobDetail jobDetail = new JobDetail(JOB_NAME + pushId, GROUP_NAME, PushJob.class);
    jobDetail.getJobDataMap().put("pushId", pushId);
    Trigger trigger = new CronTrigger(JOB_NAME + TRIGER + pushId + "_0", JOB_NAME + pushId, expression);
    scheduler.scheduleJob(jobDetail, trigger);
    Trigger[] triggers = this.getTriggerByPushId(pushId);
    List<Map<String, String>> triggerList = new ArrayList<Map<String, String>>();
    for(int i = 0; i < triggers.length; i++) {
      Trigger t = triggers[i];
      Map<String, String> tri = new HashMap<String, String>();
      tri.put("name", t.getName());
      if(t.getStartTime()!=null)
      tri.put("startTime", Constants.formatDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), t.getStartTime()));
      if(t.getNextFireTime()!=null)
      tri.put("nextFireTime", Constants.formatDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), t.getNextFireTime()));
      if(t.getPreviousFireTime()!=null)
      tri.put("previousFireTime",
          Constants.formatDate(Constants.SDF.YYYYMMDDHHMMSS.toString(), t.getPreviousFireTime()));
      else
        tri.put("previousFireTime","");
      triggerList.add(tri);
    }
    result.put("isStateful", jobDetail.isStateful());
    result.put("isDurable", jobDetail.isDurable());
    result.put("isVolatile", jobDetail.isVolatile());
    result.put("triggers", triggerList);
    return result;
  }

  /**
   * 新触发器
   * 
   * @throws org.quartz.SchedulerException
   */
  public void newTriger2Job(Map<String, String> map) {
    if(!map.containsKey("pushId") || map.get("pushId") == null || !map.containsKey("expression")
        || map.get("expression") == null)
      logger.warn("push id  and expression is null ");
    else {
      try {
        String pushId = map.get("pushId");
        String[] trigerName = scheduler.getTriggerNames(JOB_NAME + pushId);
        // JobDetail jobDetail = new JobDetail(jobName, jobGroup,
        // PushJob.class);
        Trigger triger = new CronTrigger(JOB_NAME + TRIGER + pushId + "_" + trigerName.length, JOB_NAME + pushId,
            map.get("expression"));
        triger.setJobName(JOB_NAME + pushId);
        triger.setJobGroup(GROUP_NAME);
        scheduler.scheduleJob(triger);
      }
      catch(Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  /**
   * 启动定时任务
   * 
   * @throws org.quartz.SchedulerException
   */
  public void startSchedulerTask() throws SchedulerException {
    try {
      this.scheduler = StdSchedulerFactory.getDefaultScheduler();
      this.scheduler.start();
    }
    catch(SchedulerException e) {
      logger.error("GameTaskServer start error", e);
    }
  }

  /**
   * 是否启动
   * 
   * @return
   * @throws org.quartz.SchedulerException
   */
  public boolean isStartSchedulerTask() throws SchedulerException {
    return this.scheduler.isStarted();
  }

  /**
   * 是否关闭
   * 
   * @return
   * @throws org.quartz.SchedulerException
   */
  public boolean isShutDownSchedulerTask() throws SchedulerException {
    return this.scheduler.isShutdown();
  }

  /**
   * 停止作业
   * 
   * @param jobName
   * @param groupName
   */
  public void pauseJob(String pushId) throws SchedulerException {
    
      this.scheduler.pauseTrigger(JOB_NAME + TRIGER + pushId + "_0", JOB_NAME + pushId);//.pauseJob(JOB_NAME + pushId, GROUP_NAME + pushId);
    
  }

  /**
   * 恢复 job
   * 
   * @param jobName
   * @param groupName
   */
  public void resumeJob(String pushId) throws SchedulerException {
      this.scheduler.resumeJob(JOB_NAME + pushId, GROUP_NAME);
  }

  /**
   * 删除指定的job
   * 
   * @param jobName
   * @param groupName
   * @return
   * @throws org.quartz.SchedulerException
   */
  public boolean deleteJob(String pushId) throws SchedulerException {
      return this.scheduler.deleteJob(JOB_NAME + pushId, GROUP_NAME);
    
  }

  /**
   * 恢复 job
   * 
   * @param jobName
   * @param groupName
   */
  public Trigger[] getTriggerByPushId(String pushId) throws SchedulerException {
    if(pushId == null) {
      logger.warn("push id  is null , resumeJob failed ");
      return null;
    }
    else {
      return this.scheduler.getTriggersOfJob(JOB_NAME + pushId, GROUP_NAME);
    }
  }

  public JobDetail getJobByPushId(String pushId) throws SchedulerException {
      return this.scheduler.getJobDetail(JOB_NAME + pushId, GROUP_NAME);
  }

  /**
   * 停止触发器
   * 
   * @param triggerName
   * @param group
   */
  public void pauseTrigger(String triggerName, String group) {
    try {
      this.scheduler.pauseTrigger(triggerName, group);// 停止触发器
    }
    catch(SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 重启触发器
   * 
   * @param triggerName
   * @param group
   */
  public void resumeTrigger(String triggerName, String group) {
    try {
      this.scheduler.resumeTrigger(triggerName, group);// 重启触发器
    }
    catch(SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 移除触发器
   * 
   * @param triggerName
   * @param group
   * @return
   */
  public boolean removeTrigdger(String triggerName, String group) {
    try {
      this.scheduler.pauseTrigger(triggerName, group);// 停止触发器
      return this.scheduler.unscheduleJob(triggerName, group);// 移除触发器
    }
    catch(SchedulerException e) {
      throw new RuntimeException(e);
    }
  }

}