package com.cyou.video.mobile.server.cms.web.controller.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyou.video.mobile.server.cms.model.security.Role;
import com.cyou.video.mobile.server.cms.service.security.RoleService;
import com.cyou.video.mobile.server.cms.web.aspect.LogAnno;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;

/**
 * CMS角色controller
 * @author jyz
 */
@Controller
@RequestMapping("/web/role")
public class RoleController {
  
  private Logger logger=LoggerFactory.getLogger(RoleController.class);

  @Autowired
  private RoleService roleService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listRole(HttpServletRequest request, ModelMap model) {
    try {
      List<Role> list = roleService.listRole(request);
      model.addAttribute("data", list);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
      logger.info("[method: listRole()] Get role list");
    }
    catch(Exception e) {
      logger.error("[method: listRole()] Get role list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @LogAnno(type = Constants.LOG_TYPE_ADD, desc = "创建新角色", modelName = "角色管理")
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap createRole(@RequestBody Role role, ModelMap model) {
    try {
      logger.info("[method: createRole()] Create role by params : " + JacksonUtil.getJsonMapper().writeValueAsString(role));
      roleService.createRole(role);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: createRole()] Create role : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @LogAnno(type = Constants.LOG_TYPE_UPDATE, desc = "更新指定角色信息", modelName = "角色管理")
  @RequestMapping(value = "{id}", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap updateRole(@PathVariable("id") int roleId, @RequestBody Role role, ModelMap model) {
    try {
//      role.setId(roleId);
      logger.info("[method: updateRole()] Update role by params : " + JacksonUtil.getJsonMapper().writeValueAsString(role));
      roleService.updateRole(role);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: updateRole()] Update role : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @LogAnno(type = Constants.LOG_TYPE_DELETE, desc = "删除指定角色以及其关联的所有管理项", modelName = "角色管理")
  @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
  @ResponseBody
  public ModelMap deleteManageItem(@PathVariable("id") int roleId, ModelMap model) {
    try {
      logger.info("[method: deleteManageItem()] Delete manage item by params : {roleId = " + roleId + "}");
      roleService.deleteRole(roleId);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: deleteManageItem()] Delete manage item : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "{id}/operationRela", method = RequestMethod.GET)
  @ResponseBody
  public ModelMap getRoleOperationRela(@PathVariable("id") int roleId, ModelMap model) {
    try {
      logger.info("[method: getRoleOperationRela()] Get the relation between role and operation item by params : {roleId = " + roleId + "}");
//      List<RoleOperationRela> list = roleService.listRoleOperationRela(roleId);
//      model.addAttribute("list", list);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: getRoleOperationRela()] Get the relation between role and operation item : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @LogAnno(type = Constants.LOG_TYPE_ADD, desc = "保持角色与操作项的关联关系", modelName = "角色管理")
  @RequestMapping(value = "{id}/operationRela", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap saveRoleOperationRela(@PathVariable("id") int roleId, @RequestBody List<String> operationId, ModelMap model) {
    try {
      logger.info("[method: saveRoleOperationRela()] Save the relation between role and operation item : {roleId=" + roleId +",operationId=" + JacksonUtil.getJsonMapper().writeValueAsString(operationId) + "}");
      roleService.saveRoleOperationRela(roleId, operationId);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: saveRoleOperationRela()] Save the relation between role and operation item : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
}
