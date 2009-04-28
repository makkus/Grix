/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
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

package org.vpac.qc.model.query;

import java.io.IOException;

import org.jdom.JDOMException;
import org.vpac.qc.model.clients.GenericClient;

/**
 * An InfoQuery is a {@link Query} that does not need any user input/interaction. If a value for {@link QueryArgument} 
 * of the {@link Query} can not be determined, an {@link ArgumentsException} is thrown.
 * <p>
 * The workflow is the same as for a {@link UserInputQuery}, except you don't need to execute the query.fillUserInput() method.
 * 
 * @author Markus Binsteiner
 *
 */
public class InfoQuery extends Query{
	
	public InfoQuery(String name, GenericClient client, String context) {

		super(name, client, context);
	}	

	@Override
	public void prepare() throws ArgumentsException {
		
		fillArguments();
		
	}

	@Override
	protected void setArguments(String[] arguments) throws JDOMException, ArgumentsException {

		this.arguments = new QueryArgument[arguments.length];
		
		for (int i = 0; i < arguments.length; i++) {

			this.arguments[i] = QueryArgument
					.argumentFactory(this, arguments[i]);
			String typeString = this.arguments[i].getType().getAttributeValue(
					"name");
			if (! ArgumentType.DEFAULT.equals(typeString))
				throw new ArgumentsException("Argument: "
						+ this.arguments[i].name + "is not of type \"default\". Can't process InfoQuery.");

		}		
		
	}
	
	public void fillArguments() throws ArgumentsException {

		for (QueryArgument arg : arguments) {

			try {
				arg.retrieveValue();
			} catch (Throwable e) {
				myLogger.warn("Could not fill argument \"" + arg.getName()
						+ "\"with default value. Cause: " + e.getMessage());
				throw new ArgumentsException("No value for argument: "+arg.getName()+". Cause"+e.getMessage());
				
			}
			if (arg.getValue() == null) {
				throw new ArgumentsException("No value for argument: "+arg.getName());
			}

		}
	}	


	
}
