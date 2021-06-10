package com.jonathan.sgrouter.routing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties
public class Config {
  public boolean appengineDeployment;

  public Config() {}

  public Config(Config config) {}
}
