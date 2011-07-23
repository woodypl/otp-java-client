package org.idansof.otp.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripPlan implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4836197045216079886L;
	private Date date;
	private Location from,to;
	private List<Itinerary> itineraries = new ArrayList<Itinerary>();
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the from
	 */
	public Location getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(Location from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public Location getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(Location to) {
		this.to = to;
	}
	/**
	 * @return the itineraries
	 */
	public List<Itinerary> getItineraries() {
		return itineraries;
	}
	
	

}
