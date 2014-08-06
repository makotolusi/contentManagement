package com.cyou.video.mobile.server.cms.model.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.common.IEnumDisplay;

/**
 * 角色类
 * 
 * @author lusi
 */
@Document(collection = "Security_Role")
public class Role {

  @Id
  private String id; // 角色id

  private String name; // 角色名称

  private ROLE_TYPE roleType = ROLE_TYPE.NORMAL;

  // @DBRef
  private List<Manager> managers = new ArrayList<Manager>();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Manager> getManagers() {
    return managers;
  }

  public void setManagers(List<Manager> managers) {
    this.managers = managers;
  }

  public ROLE_TYPE getRoleType() {
    return roleType;
  }

  public void setRoleType(ROLE_TYPE roleType) {
    this.roleType = roleType;
  }

  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("name", name);
    return builder.toString();
  }

  public enum ROLE_TYPE implements IEnumDisplay {
    ADMIN("攻略视频", 1), NORMAL("攻略新闻", 2);
    public String name;

    public int index;

    private ROLE_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

}
