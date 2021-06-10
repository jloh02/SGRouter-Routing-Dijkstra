package com.jonathan.sgrouter.routing;

import com.jonathan.sgrouter.routing.config.Config;
import com.jonathan.sgrouter.routing.pathfinder.PathfinderExecutor;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RoutingApplication {
  public static Config config;
  Random r = new Random();

  @Autowired private Config cfgImport;

  public static void main(String[] args) {
    SpringApplication.run(RoutingApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void doSomethingAfterStartup() {
    config = new Config(cfgImport);

    // ? Testing Code
    for (int i = 0; i < 1; i++)
      PathfinderExecutor.route(randLat(), randLon(), randLat(), randLon());
  }

  double randLat() {
    return 1.310824 + (1.415272 - 1.310824) * r.nextDouble();
  }

  double randLon() {
    return 103.750295 + (103.897093 - 103.750295) * r.nextDouble();
  }
}
