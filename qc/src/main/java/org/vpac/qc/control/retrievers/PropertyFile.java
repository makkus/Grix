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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.QueryArgument;

/**
 * This class retrieves values from property files. If you provide a seperator
 * attribute in the xml description than it uses it to split the result with it
 * and return the resulting array.
 * 
 * <p>
 * The xml code looks like this:
 * <p>
 * 
 * <pre>
 * {@code
 * <type name="userinput">
 *        <method name="PropertyFile">
 *               <property application="grix" key="FIRST_NAME"></property>
 *        </method>
 * </type>}
 * </pre>
 * <p>
 * At the moment, the <b>application</b> tag is used to load an application
 * specific property file. Which file is hardcoded in this retriever (and "grix"
 * is the only one at the moment). If the <b>application</b> tag is not present,
 * the retriever looks for a tag called <b>file</b> and loads the content of the
 * specified path/file which has to be in the usual java properties format
 * (value=key). After loading the property file the value of the key, specified
 * with the <b>key</b> tag is read and returned. If there is no key with this
 * name, an exception is thrown.
 * 
 * @author Markus Binsteiner
 * 
 */
public class PropertyFile extends QcRetriever {

	static final Logger myLogger = Logger.getLogger(PropertyFile.class
			.getName());

	List properties = null;
	ArrayList<String> values_temp = null;

	public PropertyFile(QueryArgument arg) throws ArgumentsException {
		super(arg);

		try {

			properties = method_element.getChildren("property");

			values_temp = new ArrayList<String>();

			Iterator i = properties.iterator();

			while (i.hasNext()) {
				Element prop = (Element) i.next();
				Properties properties_from_file = new Properties();
				String application = prop.getAttributeValue("application");
				// first try to get application specific propertyfile
				if (application != null && !"".equals(application)) {
					if ("grix".equals(application)) {
						properties_from_file.load(new FileInputStream(new File(
								System.getProperty("user.home")
										+ File.separator + ".globus"
										+ File.separator + "grix.properties")));
					}
				}
				if (properties_from_file.isEmpty()) {
					properties_from_file.load(new FileInputStream(prop
							.getAttributeValue("file")));
				}
				String key = prop.getAttributeValue("key");
				String seperator = prop.getAttributeValue("seperator");
				if (seperator != null) {
					String[] values_prop = null;
					String value = properties_from_file.getProperty(key);
					if (value == null)
						throw new ArgumentsException("Could not find key: "
								+ key + " in property file: "
								+ prop.getAttributeValue("file"));
					values_prop = value.split(seperator);
					// fill in the fields
					for (String val : values_prop) {
						values_temp.add(val);
					}
				} else {
					values_temp.add(properties_from_file.getProperty(key));
				}
			}
		} catch (IOException e) {
			throw new ArgumentsException(
					"Could not init PropertyFile retreiver: " + e.getMessage());
		}

	}

	public Object[] retrieveValue() throws Exception {

		if (values_temp.size() == 0)
			throw new ArgumentsException(
					"Problems retrieving values from property file. If you see this message, there is most likely a bug in the code. Inform the developer.");
		if (values_temp.size() == 1)
			return new Object[] { values_temp.get(0) };
		else
			return values_temp.toArray();

	}
}
