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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.QueryArgument;

/**
 * <p>
 * This retriever plugin is able to call a class with empty or non-empty
 * constructor and gives back the value of a called method. This could be a
 * getter or another method.
 * </p>
 * 
 * <p>
 * The xml code for this plugin looks like:
 * </p>
 * 
 * <pre>
 * {@code
 * <method name="CallClass" object="org.vpac.voc.model.clients.TestClass" object_property="addToConstructor">
 *   		<constructor_arg object="java.lang.Integer">11</constructor_arg>
 *   		<method_arg object="java.lang.Integer">10</method_arg>
 * </method>}
 * </pre>
 * 
 * , where <b>object</b> is the class to call and <b>object_property</b> is the
 * method to use. If <b>method_arg</b> tags are present, these will be used to
 * call the method (e.g. addToConstructor). If there is a <b>constructor_arg</b>
 * (even an empty on), the the constructor of this class is called before
 * executing the method object_property. If not, the method should be static
 * (not implemented yet, I think).
 * <p>
 * 
 * @author Markus Binsteiner
 * 
 */
public class CallClass extends QcRetriever {

	static final Logger myLogger = Logger.getLogger(CallClass.class.getName());

	Class classToCall = null;
	List constructorArguments = null;
	String methodToCall = null;
	List arguments = null;

	public CallClass(QueryArgument arg) throws ArgumentsException {
		super(arg);
		try {
			classToCall = Class.forName(method_element
					.getAttributeValue("object"));
			constructorArguments = method_element
					.getChildren("constructor_arg");
			methodToCall = method_element.getAttributeValue("object_property");
			arguments = method_element.getChildren("method_arg");

		} catch (ClassNotFoundException cnfe) {
			throw new ArgumentsException("Could not init CallClass retreiver: "
					+ cnfe.getMessage());
		}
	}

	public Object[] retrieveValue() throws Exception {

		Object classObject = null;

		// if a constructor has to be called
		if (constructorArguments.size() != 0) {
			// check if empty constructor
			if (((Element) constructorArguments.get(0))
					.getAttributeValue("object") == null) {
				// empty constructor
				classObject = classToCall.newInstance();
			} else {
				// non-empty constructor
				Constructor call_classConstructor = null;
				Class[] call_classConstructorClasses = new Class[constructorArguments
						.size()];
				Object[] call_classConstructorObjects = new Object[constructorArguments
						.size()];

				for (int i = 0; i < constructorArguments.size(); i++) {

					Constructor classConstructor = null;
					Class[] argsClasses = new Class[] { String.class };

					Element conArgElement = (Element) constructorArguments
							.get(i);
					Class classOfConArg = Class.forName(conArgElement
							.getAttributeValue("object"));
					String conArgConstructorValue = conArgElement.getValue();
					classConstructor = classOfConArg
							.getConstructor(argsClasses);
					call_classConstructorClasses[i] = classOfConArg;
					call_classConstructorObjects[i] = classConstructor
							.newInstance(new Object[] { conArgConstructorValue });
				}

				// call constructor
				call_classConstructor = classToCall
						.getConstructor(call_classConstructorClasses);
				classObject = call_classConstructor
						.newInstance(call_classConstructorObjects);
			}
		}

		Class[] argumentClasses = new Class[arguments.size()];
		Object[] argumentObjects = new Object[arguments.size()];

		for (int i = 0; i < arguments.size(); i++) {

			Constructor classConstructor = null;
			Class[] argsClasses = new Class[] { String.class }; // the
																// constructor
																// argument(s)
			// TODO later maybe not only one-String constructor

			// getting the element
			Element argElement = (Element) arguments.get(i);
			// this is the class of the current argument
			Class classOfArg = Class.forName(argElement
					.getAttributeValue("object"));
			// this is the constructor argument of the current argument
			String argConstructorValue = argElement.getValue();
			// initializing the constructor
			classConstructor = classOfArg.getConstructor(argsClasses);
			// fill the argumentClasses array
			argumentClasses[i] = classOfArg;
			// now we create the argument object with the appropriate class
			argumentObjects[i] = classConstructor
					.newInstance(new Object[] { argConstructorValue });
		}

		Method method = classToCall.getMethod(methodToCall, argumentClasses);

		return new Object[] { method.invoke(classObject, argumentObjects) };

	}

}
