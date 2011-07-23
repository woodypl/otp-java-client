package org.idansof.otp.client;

import java.util.List;

public class GeocoderResult {
	
	private List<Location> locations;
	
	
	public GeocoderResult(List<Location> locations) {
		super();
		this.locations = locations;
	}
	
	public List<Location> getLocations() {
		return locations;
	}

}
