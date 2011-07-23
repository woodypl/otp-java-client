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
	public enum Mode {  WALK, BUS};
	
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
