package com.cyou.video.mobile.server.cms.service.collection.impl;

public class PThread implements Runnable {

  protected long start;

  protected long end;

  protected String name;

  public long getStart() {
    return start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public long getEnd() {
    return end;
  }

  public void setEnd(long end) {
    this.end = end;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub

  }

}
