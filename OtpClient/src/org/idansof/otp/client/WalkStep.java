package org.idansof.otp.client;

import java.io.Serializable;

public class WalkStep implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6436808745713888517L;
	public enum RelativeDirection { HARD_LEFT, LEFT, SLIGHTLY_LEFT, CONTINUE, SLIGHTLY_RIGHT, RIGHT, CIRCLE_CLOCKWISE, CIRCLE_COUNTERCLOCKWISE }	
	public enum AbsoluteDirection { NORTH,NORTHEAST,EAST,SOUTHEAST,SOUTH,SOUTHWEST,WEST,NORTHWEST}	
	private double distance;
	private Location location;
	private RelativeDirection relativeDirection;
	private AbsoluteDirection absoluteDirection;
	private String exit;
	
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
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	/**
	 * @return the relativeDirection
	 */
	public RelativeDirection getRelativeDirection() {
		return relativeDirection;
	}
	/**
	 * @param relativeDirection the relativeDirection to set
	 */
	public void setRelativeDirection(RelativeDirection relativeDirection) {
		this.relativeDirection = relativeDirection;
	}
	/**
	 * @return the absoluteDirection
	 */
	public AbsoluteDirection getAbsoluteDirection() {
		return absoluteDirection;
	}
	/**
	 * @param absoluteDirection the absoluteDirection to set
	 */
	public void setAbsoluteDirection(AbsoluteDirection absoluteDirection) {
		this.absoluteDirection = absoluteDirection;
	}
	/**
	 * @return the exit
	 */
	public String getExit() {
		return exit;
	}
	/**
	 * @param exit the exit to set
	 */
	public void setExit(String exit) {
		this.exit = exit;
	}
	
}
