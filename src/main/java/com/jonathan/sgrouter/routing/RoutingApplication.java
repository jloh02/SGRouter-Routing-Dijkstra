package com.jonathan.sgrouter.routing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoutingApplication {
  public static boolean appengineDeployment;

  public static void main(String[] args) {
    SpringApplication.run(RoutingApplication.class, args);
  }
}
