package com.cyou.video.mobile.server.cms.web.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface LogAnno {
  
  /**
   * 操作描述
   * @return
   */
  public String desc() default "";
  
  /**
   * 操作名称code
   * @return
   */
  public String name() default "";
  
  /**
   * 操作属于哪个模块
   * @return
   */
  public String modelName() default "";
  
  /**
   * 增删改
   * @return
   */
  public int type() default 0;
  
}
