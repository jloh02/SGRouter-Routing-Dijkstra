package com.jonathan.sgrouter.routing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties
public class Config {
	public boolean appengineDeployment;

	public Config() {
	}

	public Config(Config config) {
	}
}
