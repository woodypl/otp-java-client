package org.idansof.otp.client;

import java.io.Serializable;

public class Location  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7604358504729416116L;
	private String address;
	private double latitude;
	private double longitude;
	
	public Location(String address, double latitude, double longitude) {
		super();
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	public String getCoordinateString()
	{
		return latitude + "," + longitude;
	}
	
	
	

}
