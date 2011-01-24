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

import org.apache.log4j.Logger;

/**
 * There are 4 different kind of arguments:
 * 
 * DEFAULT, which fills the QueryArgument with a default value. PRESELECTION,
 * which fills the QueryArgument with an array of choices for the user
 * USERINPUT, which needs user input and INPUT_DEPENDANT, which uses the user
 * input to calculate a value.
 * 
 * @author Markus Binsteiner
 * 
 */
public class ArgumentType {

	static final Logger myLogger = Logger.getLogger(ArgumentType.class
			.getName());

	public static final String DEFAULT = "default";

	public static final String PRESELECTION = "preselection";

	public static final String USERINPUT = "userinput";

	public static final String INPUT_DEPENDANT = "input_dependant";

}
