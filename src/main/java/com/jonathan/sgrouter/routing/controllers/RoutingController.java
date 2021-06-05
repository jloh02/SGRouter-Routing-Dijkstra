package com.jonathan.sgrouter.routing.controllers;

import com.jonathan.sgrouter.routing.utils.CloudStorageHandler;
import com.jonathan.sgrouter.routing.utils.SQLiteHandler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingController {
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	@GetMapping("/route")
	public String route(){
		SQLiteHandler sqh = new SQLiteHandler(); //!IMPORANT ensure this line is before download
		//CloudStorageHandler.downloadDB();  //TODO uncomment this line
		System.out.println(sqh.getNodes());
		System.out.println(sqh.getVertices("CC9","CC10"));

		sqh.close();
		return "";
	}
}
