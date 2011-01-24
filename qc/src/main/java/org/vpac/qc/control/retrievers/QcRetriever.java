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

package org.vpac.qc.control.retrievers;

import org.jdom.Element;
import org.vpac.qc.model.query.QueryArgument;

/**
 * A retriever is a class which is able to get default values or Arrays of
 * preselection values for QueryArguments. Specify the parameters you need in
 * the xml file and parse them in the constructor to initialize the retriever.
 * <p>
 * If you want to implement a retriever plugin for qc you have to extend this
 * class. Actually, this is more like an interface really. You need to implement
 * a constructor which takes a {@link QueryArgument} and initializes everything
 * for the sake of splitting init and the actual call to retrieve the value(s).
 * You have to call the super constructor which stores the method-node from the
 * xml document into the method_element field.
 * <p>
 * You could do the init in the retrieveValue method as well but that is not
 * recommendend. The retrieveValue method you implement has to return an array
 * of Objects.
 * 
 * @author Markus Binsteiner
 * 
 */
public abstract class QcRetriever {

	// QueryArgument arg = null;
	Element method_element = null;

	/**
	 * This constructor should be called by the implementing class. It stores
	 * the "method" node of the xml file into a field caled method_element.
	 * 
	 * @param arg
	 *            the QueryArgument
	 */
	public QcRetriever(QueryArgument arg) {
		this.method_element = arg.getType().getChild("method");
	}

	/**
	 * This method should retrieve the value for the QueryArgument as an array
	 * of Objects. If you only return one value, create a new Object[] for it
	 * and wrap it around your result.
	 * 
	 * @return the value of the QueryArgument
	 * @throws Exception
	 *             if something's gone wrong
	 */
	abstract public Object[] retrieveValue() throws Exception;

}
