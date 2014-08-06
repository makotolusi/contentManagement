package com.cyou.video.mobile.server.cms.rest.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ranger
 * 
 */
public class HeaderFilter implements javax.servlet.Filter {

    private static final Logger logger = LoggerFactory.getLogger(HeaderFilter.class);

    private final static String headerName = "Content-Type";
    private final static String headerValue = "application/json;charset=UTF-8";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String contentType = req.getHeader(headerName);
        if (contentType != null && contentType.indexOf(",") != -1) {
            logger.error("error Content-Type :" + contentType);
        }
        chain.doFilter(new HeaderRequestHandler(req), response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    final class HeaderRequestHandler extends HttpServletRequestWrapper {
        public HeaderRequestHandler(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if (headerName.equalsIgnoreCase(name)) {
                return headerValue;
            }

            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return super.getHeaderNames();
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> list = new ArrayList<String>(1);
            list.add(headerValue);
            if (headerName.equalsIgnoreCase(name)) {
                return Collections.enumeration(list);
            }
            return super.getHeaders(name);
        }
    }

    public void destroy() {

    }

}
