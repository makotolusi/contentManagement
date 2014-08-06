package com.cyou.video.mobile.server.cms.service.sys;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.sys.MailInfo;

/**
 * 邮件信息业务接口
 * @author jyz
 */
public interface MailInfoService {

  /**
   * 创建邮件信息
   * @param mailInfo 邮件信息
   * @return 邮件信息id
   * @throws Exception
   */
  public int createMailInfo(MailInfo mailInfo) throws Exception;

  /**
   * 获取邮件信息
   * @param id 邮件信息id
   * @return 邮件信息
   * @throws Exception
   */
  public MailInfo getMailInfo(int id) throws Exception;

  /**
   * 删除邮件信息
   * @param id 邮件信息id
   * @throws Exception
   */
  public void deleteMailInfo(int id) throws Exception;

  /**
   * 更新邮件信息
   * @param mailInfo 邮件信息
   * @throws Exception
   */
  public void updateMailInfo(MailInfo mailInfo) throws Exception;

  /**
   * 获取邮件信息分页列表
   * @param status 邮件信息状态
   * @param project 应用项目
   * @param curPage 当前页码
   * @param pageSize 每页数量
   * @return 邮件信息分页列表
   * @throws Exception
   */
  public Pagination listMailInfo(int status, String project, int curPage, int pageSize) throws Exception;
}
