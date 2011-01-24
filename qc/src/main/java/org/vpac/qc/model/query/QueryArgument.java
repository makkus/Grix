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

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.vpac.qc.control.retrievers.QcRetriever;
import org.vpac.qc.model.clients.GenericClient;

/**
 * A QueryArgument has got a name which is the same as the {@link GenericClient}
 * returns with the getQueryArgumentNames() method. The name can also be found
 * in the xml config file for a Query. The xml file is parsed, and if the name
 * is found the QueryArgument will be processed according to the parameters in
 * the xml file.
 * <p>
 * If the QueryArgument name is not found, the type is set to USERINPUT
 * automatically.
 * 
 * @author Markus Binsteiner
 * 
 */
public class QueryArgument {

	static final Logger myLogger = Logger.getLogger(QueryArgument.class
			.getName());

	GenericClient client = null;

	String name = null;

	Element type = null;

	String prettyName = null;

	String description = null;

	Object[] value = null;

	Query query = null;

	private QueryArgument(Query query, String name, Element type,
			String prettyName, String description, Object[] value) {
		this.query = query;
		this.name = name;
		this.type = type;
		this.prettyName = prettyName;
		this.description = description;
		this.value = value;
	}

	public QueryArgument(Query query, String name) {
		this.query = query;
		this.name = name;
		this.type = new Element("type").setAttribute("name", "userinput");
		this.prettyName = name;
		this.description = name;
		this.value = null;
	}

	public static QueryArgument argumentFactory(Query query, String name)
			throws JDOMException {

		myLogger.debug("Adding element: " + name + " to userInputQuery: "
				+ query.getName());

		Element type = null;
		String prettyName = null;
		String description = null;

		Object[] value = null;

		String searchString = "/QueryArguments/QueryArgument[@name=\"" + name
				+ "\"]";
		XPath x = XPath.newInstance(searchString);
		Element rootElement = query.getClient().getRootElement();

		List list = x.selectNodes(rootElement);

		if (!list.isEmpty()) {

			// take the first element of the list and ignore possible other
			// matches
			Element element = (Element) list.get(0);
			// check whether this element contains a query element with the name
			// of
			// the Query
			searchString = searchString + "/query[@name=\"" + query.getName()
					+ "\"]";
			// searchString = "/QueryArguments/QueryArgument/query";
			x = XPath.newInstance(searchString);
			list = x.selectNodes(rootElement);

			Element queryElement = null;
			// if something is found, again, take the first result
			if (list.size() > 0)
				queryElement = (Element) list.get(0);

			// check query specific elements
			if (queryElement != null) {
				try {
					type = queryElement.getChild("type");
				} catch (NoSuchElementException nsee) {
					// does not matter
				}
				prettyName = queryElement.getAttributeValue("prettyName");
				description = queryElement.getAttributeValue("description");
			}
			try {
				if (type == null)
					type = element.getChild("type");
			} catch (NoSuchElementException nsee) {
				throw new JDOMException(
						"Could not find type of the QueryArgument with the name: "
								+ name);
			}

			if (prettyName == null)
				prettyName = element.getAttributeValue("prettyName");
			if (description == null)
				description = element.getAttributeValue("description");

			// if still null...
			if (prettyName == null)
				prettyName = name;
			if (description == null)
				description = name;

			return new QueryArgument(query, name, type, prettyName,
					description, value);
		} else {
			// not found in xml description file
			QueryArgument arg = new QueryArgument(query, name);
			return arg;
		}

	}

	public boolean equals(QueryArgument other) {
		if (other.getName().equals(this.getName()))
			return true;
		else
			return false;
	}

	/**
	 * This method calls the class specified in the xml file to fill the value
	 * field of the QueryArgument with an array for preselection
	 * 
	 * @throws Throwable
	 */
	public void retrieveValue() throws ArgumentsException {

		Element element = type.getChild("method");

		String method_name = element.getAttributeValue("name");
		QcRetriever retreiver = null;
		try {
			Class plugin = Class.forName("org.vpac.qc.control.retrievers."
					+ method_name);

			// Constructor
			Constructor call_classConstructor = null;
			Class[] call_classConstructorClasses = new Class[] { QueryArgument.class };
			Object[] call_classConstructorObjects = new Object[] { this };

			call_classConstructor = plugin
					.getConstructor(call_classConstructorClasses);
			retreiver = (QcRetriever) call_classConstructor
					.newInstance(call_classConstructorObjects);
		} catch (ClassCastException cce) {
			// should not happen
			throw new ArgumentsException("Could not cast retreiver plugin: "
					+ cce.getMessage());
		} catch (Exception ae) {
			throw (ArgumentsException) ae;
		}

		try {
			this.value = retreiver.retrieveValue();

		} catch (Exception e) {
			myLogger.warn("Error when invoking method: " + method_name + ": "
					+ e.getMessage());
			throw new ArgumentsException(
					"Could not retreive data with retreiver " + retreiver
							+ ": " + e.getMessage());
		}

	}

	public Element getType() {
		return type;
	}

	public Object[] getValue() {
		return value;
	}

	protected void setValue(Object[] value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public Query getQuery() {
		return query;
	}

	public void changeType(String type) throws ArgumentsException {

		if (ArgumentType.DEFAULT.equals(type))
			this.type = new Element("type").setAttribute("name", "default");
		else if (ArgumentType.PRESELECTION.equals(type))
			this.type = new Element("type")
					.setAttribute("name", "preselection");
		else if (ArgumentType.INPUT_DEPENDANT.equals(type))
			this.type = new Element("type").setAttribute("name",
					"input_dependant");
		else if (ArgumentType.USERINPUT.equals(type))
			this.type = new Element("type").setAttribute("name", "userinput");
		else
			throw new ArgumentsException("Unknown ArgumentType: " + type);
	}

}
