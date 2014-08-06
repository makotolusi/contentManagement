package com.cyou.video.mobile.server.cms.service.sys;

import java.util.Date;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.ManagerLog;

/**
 * CMS用户日志业务接口
 * @author zs
 */
public interface ManagerLogService {
  
  /**
   * 记录用户日志
   * @param log 日志信息
   * @return id
   * @throws Exception
   */
  public int createUserLog(ManagerLog log) throws Exception;
  
  /**
   * 获取用户日志列表
   * @param type 操作类型
   * @param userName 当前用户
   * @param modelName 所属模块
   * @param from 开始时间
   * @param to 结束时间
   * @return 用户日志列表
   * @throws Exception
   */
  public Pagination listLogInfo(int curPage, int pageSize, int type, String userName, String modelName, Date from, Date to) throws Exception;
  
}
