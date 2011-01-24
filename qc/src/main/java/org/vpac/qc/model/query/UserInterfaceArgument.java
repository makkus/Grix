/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
 * qc is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.

 * qc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with qc; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.vpac.qc.model.query;

import java.util.ArrayList;

/**
 * A UserInterfaceArgument is a lighter version of a {@link QueryArgument}. It
 * only has got the name, a pretty name, a description and, if the according
 * {@link QueryArgument} is of the tye PRESELECTION, an {@link ArrayList} of
 * ojects to choose from.
 * 
 * @author Markus Binsteiner
 * 
 */
public class UserInterfaceArgument {

	private String name = null;
	private String prettyName = null;
	private String description = null;

	private boolean isPreselection = false;

	private ArrayList<Object> preselection = null;

	public UserInterfaceArgument(String name, String prettyName,
			String description, ArrayList<Object> preselection,
			boolean isPreselection) {

		this.name = name;
		this.prettyName = prettyName;
		this.description = description;
		this.isPreselection = isPreselection;
		this.preselection = preselection;

	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Object> getPreselection() {
		return preselection;
	}

	/**
	 * This returns the values of the preselection ArrayList in a possibly more
	 * human readable format. At the moment it only returns the
	 * Object.toString() value. But it could be extended to return nicer output.
	 * 
	 * @return the preselection as a String array
	 */
	public String[] getPreselectionAsString() {
		String[] strings = new String[preselection.size()];

		for (int i = 0; i < preselection.size(); i++) {
			// here we could implement a plugin interface to translate the
			// strings into something nicer...
			try {
				strings[i] = preselection.get(i).toString();
			} catch (NullPointerException e) {
				strings[i] = null;
			}
		}

		return strings;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public boolean isPreselection() {
		return isPreselection;
	}

}
