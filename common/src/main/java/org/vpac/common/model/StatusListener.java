/* Copyright 2006 VPAC
 * 
 * This file is part of common.
 * Grix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.

 * Grix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Grix; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.vpac.common.model;

/**
 * A StatusListener reacts to StatusEvents
 * 
 * @author Markus Binsteiner
 *
 */
public interface StatusListener {

	/**
	 * This method gets called if a status of an object has changed.
	 * @param event the event that has happened
	 */
	public void statusChanged(StatusEvent event);
	
}
