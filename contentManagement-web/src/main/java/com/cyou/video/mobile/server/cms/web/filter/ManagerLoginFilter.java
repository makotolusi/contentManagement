package com.cyou.video.mobile.server.cms.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cyou.video.mobile.server.cms.common.Consts;

/**
 * 管理后台登录filter
 * @author jyz
 */
public class ManagerLoginFilter implements Filter {

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpSession session = request.getSession();
    String uri = request.getRequestURI().replaceAll("/$", "");
//    if(uri.equals(request.getContextPath() + "/web") || uri.equals(request.getContextPath() + "/web/manager/login") || uri.equals(request.getContextPath() + "/web/manager/logout")) {
//      filterChain.doFilter(request, response);
//      return;
//    }
//    if(session.getAttribute(Consts.SESSION_MANAGER) == null) {
//      response.sendRedirect(request.getContextPath() + "/web/manager/logout");
//      return;
//    }
    filterChain.doFilter(request, response);
    return;
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
  }

}
