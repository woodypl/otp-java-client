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

public class Leg  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5573418733774689997L;
	public enum Mode {  WALK, TRAM, SUBWAY, RAIL, BUS, FERRY, CABLE_CAR, GONDOLA, FUNICULAR, TRANSIT};
	
	private String route;
	private Mode mode;
	private Date startTime;
	private Date endTime;
	private double distance;
	private long duration;
	private Location from;
	private Location to;
	private List<WalkStep> walkSteps = new ArrayList<WalkStep>();
	private List<Location> geometry = new ArrayList<Location>();
	
	/**
	 * @return the route
	 */
	public String getRoute() {
		return route;
	}
	/**
	 * @param route the route to set
	 */
	public void setRoute(String route) {
		this.route = route;
	}
	/**
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
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
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
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
	 * @return the walkStep
	 */
	public List<WalkStep> getWalkSteps() {
		return walkSteps;
	}
	
	public List<Location> getGeometry() {
		return geometry;
	}
	
	
	

}
