package com.cyou.video.mobile.server.cms.mongo.dao.collection;

import java.util.List;

import com.cyou.video.mobile.server.cms.model.collection.PushTagExcuteStateInfo;

public interface PushTagLogDao {

  /**
   * push tag thread number inc
   * @param threadName
   */
  void inc(String threadName,int n);

  /**
   * 取得thread info
   * @param threadName
   * @return
   */
  List<PushTagExcuteStateInfo> getThreadNum(String threadName);

  /**
   * 移除 所有log
   * @param threadName
   */
  void removeThreadLog();

  /**
   * 设置total
   * @param total
   */
  void setThreadTotal(int total);

  /**
   * 取得执行数据总量
   */
  int getThreadTotal();

  /**
   * 系统自动标签线程数量
   * @param num
   */
  void setSysThreadNum(int num);

  /**
   * 得到系统参数
   * @return
   */
  PushTagExcuteStateInfo getSysThreadNum();

  /**
   * 所有任务恢复 等待状态
   */
  void updateWaiting();

}
