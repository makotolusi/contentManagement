package com.cyou.video.mobile.server.cms.service.push;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Query;

import com.cyou.video.mobile.server.cms.model.push.PushTagCombination;

public interface PushTagXinGe173APPService {

  /**
   * 173app发送标签
   */
  int sendPushTags(int s, int end, Query query, String name);

  /**
   * 每个线程结束时记录日志
   * @param c
   * @param total
   */
  void successLogEnd(String c, int total);

  /**
   * 渠道 版本tag
   * @param s
   * @param end
   * @param lastModifyDate
   * @return
   * @throws Exception
   */
  int sendPushTagsChannel(int s, int end, Date lastModifyDate) throws Exception;

  /**
   * 组合标签 信鸽版
   * @param s
   * @param end
   * @param query
   * @param name
   * @param combination
   * @return
   */
  int sendPushCombinationTags(int s, int end, Query query, String name, PushTagCombination combination);

}
