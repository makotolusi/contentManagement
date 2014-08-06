package com.cyou.video.mobile.server.cms.service.sys;

import java.util.List;

import com.cyou.video.mobile.server.cms.model.sys.MailAddressee;

/**
 * 邮件接收者业务接口
 * @author jyz
 */
public interface MailAddresseeService {

  /**
   * 创建邮件接收者
   * @param mailAddressee 邮件接收者
   * @return 邮件接收者id
   * @throws Exception
   */
  public int createMailAddressee(MailAddressee mailAddressee) throws Exception;

  /**
   * 获取邮件接收者
   * @param id 邮件接收者id
   * @return 邮件接收者
   * @throws Exception
   */
  public MailAddressee getMailAddressee(int id) throws Exception;

  /**
   * 更新邮件接收者
   * @param mailAddressee 邮件接收者
   * @throws Exception
   */
  public void updateMailAddressee(MailAddressee mailAddressee) throws Exception;

  /**
   * 删除邮件接收者
   * @param id 邮件接收者id
   * @throws Exception
   */
  public void deleteMailAddressee(int id) throws Exception;

  /**
   * 获取邮件接收者列表
   * @param project 应用项目
   * @param type 应用标识
   * @return 邮件接收者列表
   * @throws Exception
   */
  public List<MailAddressee> getMailAddresseeList(String project, String type) throws Exception;

  /**
   * 获取应用标识列表
   * @param project 应用项目
   * @return 应用标识列表
   * @throws Exception
   */
  public List<String> getTypeListByProject(String project) throws Exception;

  /**
   * 获取应用项目列表
   * @return 应用项目列表
   * @throws Exception
   */
  public List<String> getProjectList() throws Exception;
}
