package com.cyou.video.mobile.server.cms.model;

import java.io.Externalizable;

/**
 * CMS Model基类
 * @author jyz
 */
public abstract class BaseModel implements Externalizable, Cloneable {

  private static final long serialVersionUID = -6179944476640423659L;

  public abstract String toString();

  public abstract boolean equals(final Object other);

  public abstract int hashCode();
}
