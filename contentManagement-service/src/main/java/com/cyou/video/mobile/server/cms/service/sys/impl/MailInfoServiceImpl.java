package com.cyou.video.mobile.server.cms.service.sys.impl;

import org.springframework.stereotype.Service;

/**
 * 邮件信息业务实现
 * @author jyz
 */
@Service("mailInfoService")
public class MailInfoServiceImpl {//implements MailInfoService {

////  private MailInfoDao mailInfoDao;
//
//  @Override
//  public int createMailInfo(MailInfo mailInfo) throws Exception {
//    if(mailInfo == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo");
//    }
//    if(mailInfo.getId() > 0) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailInfo.id");
//    }
//    if(StringUtils.isBlank(mailInfo.getTitle())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo.title");
//    }
//    if(StringUtils.isBlank(mailInfo.getContent())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo.content");
//    }
//    if(mailInfo.getAddresseeMap() == null || mailInfo.getAddresseeMap().size() == 0) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo.addressee");
//    }
//    if(StringUtils.isBlank(mailInfo.getProject())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo.project");
//    }
//    if(StringUtils.isBlank(mailInfo.getType())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo.type");
//    }
//    mailInfo.setCreateDate(new Date());
//    mailInfo.setStatus(Constants.STATUS.OFF.getValue());
//    mailInfo.setTimes(0);
////    int id = mailInfoDao.createMailInfo(mailInfo);
////    if(id == 1 && mailInfo.getId() > 0) {
////      id = mailInfo.getId();
////    }
//    return 0;
//  }
//
//  @Override
//  public MailInfo getMailInfo(int id) throws Exception {
//    if(id < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailInfo.id");
//    }
//    return null;//mailInfoDao.getMailInfo(id);
//  }
//
//  @Override
//  public void deleteMailInfo(int id) throws Exception {
//    if(id < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailInfo.id");
//    }
////    mailInfoDao.deleteMailInfo(id);
//  }
//
//  @Override
//  public void updateMailInfo(MailInfo mailInfo) throws Exception {
//    if(mailInfo == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_mailInfo");
//    }
//    if(mailInfo.getId() < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_mailInfo.id");
//    }
//    MailInfo reference = mailInfoDao.getMailInfo(mailInfo.getId());
//    if(reference == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_mailInfo");
//    }
//    reference.setSendDate(new Date());
//    reference.setStatus(mailInfo.getStatus());
//    reference.setTimes(reference.getTimes() + 1);
//    mailInfoDao.updateMailInfo(reference);
//  }
//
//  @Override
//  public Pagination listMailInfo(int status, String project, int curPage, int pageSize) throws Exception {
//    Pagination pagination = null;
//    pagination = new Pagination();
//    pagination.setCurPage(curPage);
//    pagination.setPageSize(pageSize);
//    pagination.setRowCount(mailInfoDao.listMailInfoCount(status, project));
//    curPage = (curPage - 1) * pageSize;
//    pagination.setContent(mailInfoDao.listMailInfo(status, project, curPage, pageSize));
//    return pagination;
//  }

}
