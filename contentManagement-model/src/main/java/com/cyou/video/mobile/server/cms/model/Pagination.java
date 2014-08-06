package com.cyou.video.mobile.server.cms.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 分页封装类
 * @author jyz
 */
public class Pagination extends BaseModel {

  public static final int PAGESIZE = 10; //默认每页10条记录

  private int curPage = 1; //当前页码

  private int pageSize = PAGESIZE; //每页条数

  private List<?> content; //记录集

  private int rowCount = 0; //记录数

  public int getCurPage() {
    return curPage;
  }

  public void setCurPage(int curPage) {
    this.curPage = curPage;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public List<?> getContent() {
    return content;
  }

  public void setContent(List<?> content) {
    this.content = content;
  }

  public int getRowCount() {
    return rowCount;
  }

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }

  public int getPageCount() {
    if(rowCount == 0) {
      return 0;
    }
    else if(curPage < 0) {
      if(content != null && content.size() > 0) {
        return 1;
      }
      else {
        return 0;
      }
    }
    else {
      return rowCount / pageSize + (rowCount % pageSize > 0 ? 1 : 0);
    }
  }

  public int getStartRow() {
    if(content != null && content.size() > 0) {
      if(curPage < 0) {
        return 1;
      }
      else {
        return (curPage - 1) * pageSize + 1;
      }
    }
    else {
      return 0;
    }
  }

  public int getEndRow() {
    if(content != null && content.size() > 0) {
      return getStartRow() + content.size();
    }
    else {
      return 0;
    }
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(curPage);
    out.writeInt(pageSize);
    out.writeObject(content);
    out.writeInt(rowCount);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    curPage = in.readInt();
    pageSize = in.readInt();
    content = (List<?>) in.readObject();
    rowCount = in.readInt();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("curPage", curPage);
    builder.append("pageSize", pageSize);
    builder.append("content", content);
    builder.append("rowCount", rowCount);
    return builder.toString();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof Pagination) {
      if(this == other) {
        equals = true;
      }
      else {
        Pagination cast = (Pagination) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(curPage, cast.curPage);
        builder.append(pageSize, cast.pageSize);
        builder.append(content, cast.content);
        builder.append(rowCount, cast.rowCount);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(curPage);
    builder.append(pageSize);
    builder.append(content);
    builder.append(rowCount);
    return builder.toHashCode();
  }
}
