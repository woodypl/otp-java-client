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
