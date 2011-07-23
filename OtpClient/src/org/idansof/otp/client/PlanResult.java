package org.idansof.otp.client;

import java.io.Serializable;

public class PlanResult implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6470606620376435953L;
	private TripPlan tripPlan;
		
	public PlanResult(TripPlan tripPlan) {
		super();
		this.tripPlan = tripPlan;
	}



	public TripPlan getTripPlan() {
		return tripPlan;
	}
	

}
