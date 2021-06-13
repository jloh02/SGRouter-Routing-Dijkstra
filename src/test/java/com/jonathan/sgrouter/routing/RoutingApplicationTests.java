package com.jonathan.sgrouter.routing;

import com.jonathan.sgrouter.routing.pathfinder.PathfinderExecutor;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoutingApplicationTests {

  Random r = new Random();

  @Test
  void randLatLonTest() {
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
