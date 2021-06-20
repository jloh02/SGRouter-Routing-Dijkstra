package com.jonathan.sgrouter.routing.controllers;

import com.jonathan.sgrouter.routing.RoutingApplication;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class Filter implements javax.servlet.Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;

    String appengineHeader = req.getHeader("X-Appengine-Country");

    RoutingApplication.appengineDeployment = appengineHeader != null;

    if (!RoutingApplication.appengineDeployment) AnsiOutput.setEnabled(Enabled.ALWAYS);
    log.debug("App Engine Deployment: {}", RoutingApplication.appengineDeployment);

    chain.doFilter(request, response);
  }
}
