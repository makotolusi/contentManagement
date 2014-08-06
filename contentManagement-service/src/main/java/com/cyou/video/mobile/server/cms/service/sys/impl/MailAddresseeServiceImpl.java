package com.cyou.video.mobile.server.cms.service.sys.impl;

import org.springframework.stereotype.Service;

/**
 * 邮件接收者业务实现
 * @author jyz
 */
@Service("mailAddresseeService")
public class MailAddresseeServiceImpl {// implements MailAddresseeService {
//
////  private MailAddresseeDao mailAddresseeDao;
//
//  @Override
//  public int createMailAddressee(MailAddressee mailAddressee) throws Exception {
//    if(mailAddressee == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee");
//    }
//    if(mailAddressee.getId() > 0) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailAddressee.id");
//    }
//    if(StringUtils.isBlank(mailAddressee.getProject())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.project");
//    }
//    if(StringUtils.isBlank(mailAddressee.getType())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.type");
//    }
//    if(StringUtils.isBlank(mailAddressee.getNickname())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.nickname");
//    }
//    if(StringUtils.isBlank(mailAddressee.getEmail())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.email");
//    }
////    List<MailAddressee> list = mailAddresseeDao.getMailAddresseeList(mailAddressee.getProject(), mailAddressee.getType());
////    if(list != null && list.size() > 0) {
////      for(MailAddressee addressee : list) {
////        if(addressee.getEmail().equalsIgnoreCase(mailAddressee.getEmail())) { //检查新收件人的mail地址是否在数据库中存在
////          throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_mailAddressee.email");
////        }
////      }
////    }
////    mailAddressee.setProject(mailAddressee.getProject().toUpperCase());
////    mailAddressee.setType(mailAddressee.getType().toUpperCase());
////    int id = mailAddresseeDao.createMailAddressee(mailAddressee);
//////    if(id == 1 && mailAddressee.getId() > 0) {
//////      id = mailAddressee.getId();
//////    }
//    return 0;
//  }
//
//  @Override
//  public MailAddressee getMailAddressee(int id) throws Exception {
//    if(id < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailAddressee.id");
//    }
//    return null;//mailAddresseeDao.getMailAddressee(id);
//  }
//
//  @Override
//  public void updateMailAddressee(MailAddressee mailAddressee) throws Exception {
//    if(mailAddressee == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee");
//    }
//    if(mailAddressee.getId() < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailAddressee.id");
//    }
//    if(StringUtils.isBlank(mailAddressee.getProject())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.project");
//    }
//    if(StringUtils.isBlank(mailAddressee.getType())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.type");
//    }
//    if(StringUtils.isBlank(mailAddressee.getNickname())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.nickname");
//    }
//    if(StringUtils.isBlank(mailAddressee.getEmail())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.email");
//    }
////    List<MailAddressee> list = mailAddresseeDao.getMailAddresseeList(mailAddressee.getProject(), mailAddressee.getType());
////    if(list != null && list.size() > 0) {
////      for(MailAddressee addressee : list) {
////        if(addressee.getEmail().equalsIgnoreCase(mailAddressee.getEmail()) && addressee.getId() != mailAddressee.getId()) { //非本身修改的邮件地址不能与数据库中重复
////          throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_mailAddressee.email");
////        }
////      }
////    }
////    else {
////      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailAddressee.type");
////    }
////    mailAddresseeDao.updateMailAddressee(mailAddressee);
//  }
//
//  @Override
//  public void deleteMailAddressee(int id) throws Exception {
//    if(id < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailAddressee.id");
//    }
////    mailAddresseeDao.deleteMailAddressee(id);
//  }
//
//  @Override
//  public List<MailAddressee> getMailAddresseeList(String project, String type) throws Exception {
//    if(StringUtils.isBlank(project)) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.project");
//    }
//    if(StringUtils.isBlank(type)) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.type");
//    }
//    return null;//mailAddresseeDao.getMailAddresseeList(project, type);
//  }
//
//  @Override
//  public List<String> getTypeListByProject(String project) throws Exception {
//    if(StringUtils.isBlank(project)) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailAddressee.project");
//    }
//    return null;// mailAddresseeDao.getTypeListByProject(project);
//  }
//
//  @Override
//  public List<String> getProjectList() throws Exception {
//    return null;//mailAddresseeDao.getProjectList();
//  }

}
