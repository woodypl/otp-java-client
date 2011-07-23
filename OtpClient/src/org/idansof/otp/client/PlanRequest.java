package org.idansof.otp.client;

import java.util.Date;

public class PlanRequest {

	private Location from,to;
	private Date date;
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
	
	
	
	
}
