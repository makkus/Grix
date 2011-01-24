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
 * This abstract method provides an interface for a {@link UserInputQuery} to
 * connect to. All the init stuff should go (obviously) in the init() method
 * (can be empty too, though).
 * <p>
 * For retrieveal of the user input implement the getValues() method.
 * 
 * @author Markus Binsteiner
 * 
 */
public abstract class UserInput {

	protected ArrayList<UserInterfaceArgument> arguments = null;
	protected Object[] values = null;

	public void connect(UserInputQuery userInputQuery) {
		this.arguments = userInputQuery.getUserNeededArguments();
		values = new Object[arguments.size()];
		userInputQuery.connect(this);
		init();
	}

	public Object[] getUserInput() {
		return getValues();
	}

	abstract protected void init();

	abstract protected Object[] getValues();

}
