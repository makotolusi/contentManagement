package com.cyou.video.mobile.server.cms.service.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpringEl {

  public static Object getFieldValue(String field, Object obj) {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(field);
    return exp.getValue(obj);
  }
  public static Object condition(String field,String value, Object obj) {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(field+ " == "+value+"");
    return exp.getValue(obj);
  }
}
