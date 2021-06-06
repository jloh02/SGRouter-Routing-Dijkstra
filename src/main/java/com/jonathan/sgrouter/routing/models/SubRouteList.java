package com.jonathan.sgrouter.routing.models;

import java.util.ArrayList;
import java.util.Collections;

public class SubRouteList extends ArrayList<SubRoute>{
	public SubRouteList(){
		super(3);
	}
	
	@Override
	public boolean add(SubRoute e) {
		int idx = Collections.binarySearch(this, e);
        if (idx < 0) idx = ~idx; //binarySearch returns binary negative of intended position     
        super.add(idx, e);
		return true;
	}
}
