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
