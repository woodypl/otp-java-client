/*
 * Copyright 2010, 2011 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.idansof.otp.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Itinerary  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3037199620313568428L;
	private long duration;
	private Date startTime;
	private Date endTime;
	private long walkTime;
	private long transitTime;
	private long waitingTime;
	private double walkDistance;
	private int transfers;
	private List<Leg> legs = new ArrayList<Leg>();
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the walkTime
	 */
	public long getWalkTime() {
		return walkTime;
	}
	/**
	 * @param walkTime the walkTime to set
	 */
	public void setWalkTime(long walkTime) {
		this.walkTime = walkTime;
	}
	/**
	 * @return the transitTime
	 */
	public long getTransitTime() {
		return transitTime;
	}
	/**
	 * @param transitTime the transitTime to set
	 */
	public void setTransitTime(long transitTime) {
		this.transitTime = transitTime;
	}
	/**
	 * @return the waitingTime
	 */
	public long getWaitingTime() {
		return waitingTime;
	}
	/**
	 * @param waitingTime the waitingTime to set
	 */
	public void setWaitingTime(long waitingTime) {
		this.waitingTime = waitingTime;
	}
	/**
	 * @return the walkDistance
	 */
	public double getWalkDistance() {
		return walkDistance;
	}
	/**
	 * @param walkDistance the walkDistance to set
	 */
	public void setWalkDistance(double walkDistance) {
		this.walkDistance = walkDistance;
	}
	/**
	 * @return the transfers
	 */
	public int getTransfers() {
		return transfers;
	}
	/**
	 * @param transfers the transfers to set
	 */
	public void setTransfers(int transfers) {
		this.transfers = transfers;
	}
	/**
	 * @return the legs
	 */
	public List<Leg> getLegs() {
		return legs;
	}
	
	
	
	

}
