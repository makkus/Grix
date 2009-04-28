/* Copyright 2006 VPAC
 * 
 * This file is part of voms.
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

package org.vpac.voms.control;

import java.util.EventObject;

public class VomsStatusEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	
	public static final int NEW_VOMS = 0;
	public static final int REMOVED_VOMS = 1;
	public static final int STATUS_CHANGED = 2;
	public static final int REMOVED_VOMS_MEMBERSHIP = 3;
	public static final int INFO_CHANGED = 4;
	
	private int action = -1;
	private String[] message = null;
	
	public VomsStatusEvent(Voms voms, int action) {
		super(voms);
		this.action = action;

	}
	
	public VomsStatusEvent(Voms voms, int action, String[] message){
		super(voms);
		this.action = action;
		this.message = message;
	}

	public String[] getMessage() {
		return message;
	}

	public int getAction() {
		return action;
	}

}
