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

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.vpac.qc.model.clients.GenericClient;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.QueryArgument;

/**
 * This class is able to use a GenericClient to retrieve default / preselection
 * values. That only is useful if you know the name of the method you want to
 * call and the type/amount of the arguments that are necessary to call the
 * method upfront.
 * <p>
 * 
 * Very useful e.g. if you want to present the user with a selection of
 * available institions in a VO. This always uses the client the query is
 * connected to.
 * 
 * <p>
 * The xml code for this plugin looks like:
 * </p>
 * 
 * <pre>
 * {@code
 * <type name="preselection">
 *       <method name="CallGenericClient" object="getInstitutions">
 *       </method>
 * </type>}
 * </pre>
 * <p>
 * The <b>object</b> tag specifies the query to call. If one/several
 * <b>client_args</b> tag(s) are present, the query is called with these
 * arguments.
 * 
 * @author Markus Binsteiner
 * 
 */
public class CallGenericClient extends QcRetriever {

	static final Logger myLogger = Logger.getLogger(CallGenericClient.class
			.getName());

	String methodToCall = null;
	GenericClient client = null;
	Object[] client_args = null;

	public CallGenericClient(QueryArgument arg) throws ArgumentsException {
		super(arg);

		try {
			methodToCall = method_element.getAttributeValue("object");
			List arguments = method_element.getChildren("method_arg");

			client = arg.getQuery().getClient();

			client_args = new Object[arguments.size()];

			for (int i = 0; i < arguments.size(); i++) {
				client_args[i] = ((Element) arguments.get(i)).getValue();
			}
		} catch (Exception e) {
			throw new ArgumentsException(
					"Could not init CallGenericClient retriever: "
							+ e.getMessage());
		}

	}

	public Object[] retrieveValue() throws Exception {

		return client.execute(methodToCall, client_args);

	}

}
