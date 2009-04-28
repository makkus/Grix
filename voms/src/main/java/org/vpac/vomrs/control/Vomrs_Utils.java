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

package org.vpac.vomrs.control;

import java.util.ArrayList;

public class Vomrs_Utils {
	
	public static final String TYPE = "TYPE:Active";
	public static final String STATUS = "STATUS:Approved";
	public static final String DEFAULT_ROLE = "Member";
	public static final String ROLE = "Role="+DEFAULT_ROLE;

		
	public static ArrayList<String> getGroups(ArrayList<String> groups_long){
		
		ArrayList<String> result = new ArrayList<String>();
		
		for ( String group_long : groups_long ) {
			String group_short = group_long.substring(group_long.indexOf("/"), group_long.indexOf("/Role="));
			result.add(group_short);
		}
		
		return result;
	}
	
	/**
	 * Filters out the groups of the answer of the MbrInfo query to the VOMRS web service.
	 * 
	 * @param mbrInfoResult the result of a MbrInfo query to a VOMRS web service
	 * @returnall the groups a user is member of in the order VOMRS returns it
	 */
	public static String[] getGroupRoles(String[] mbrInfoResult){
		
		String group_string = mbrInfoResult[14].substring(1,mbrInfoResult[14].length()-1);
		
		String [] groups = group_string.split("\\]\\[");
		
		ArrayList<String> activeGroups = new ArrayList<String>();
		for ( String group : groups ){
			if ( group.indexOf(ROLE) != -1 && group.indexOf(STATUS) != -1 && group.indexOf(TYPE) != -1 ){
				activeGroups.add(group.substring(0, group.indexOf(ROLE)-1));
			}
		}
		
		return activeGroups.toArray(new String[activeGroups.size()]);
	}

}
