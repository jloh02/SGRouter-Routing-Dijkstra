package com.jonathan.sgrouter.routing;

import com.jonathan.sgrouter.routing.config.Config;
import com.jonathan.sgrouter.routing.pathfinder.Dijkstra;
import com.jonathan.sgrouter.routing.utils.CloudStorageHandler;
import com.jonathan.sgrouter.routing.utils.SQLiteHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RoutingApplication {
	public static Config config;

	@Autowired
	private Config cfgImport;

	public static void main(String[] args) {
		SpringApplication.run(RoutingApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		config = new Config(cfgImport);

		SQLiteHandler sqh = new SQLiteHandler(); //!IMPORANT ensure this line is before download
		CloudStorageHandler.downloadDB();
		
		Dijkstra d = new Dijkstra();
		//d.route(sqh,"CC14","19089");
		d.route(sqh,"CC21","75361");

		sqh.close();
	}

}
