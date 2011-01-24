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

package org.vpac.qc.model.clients;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.vpac.qc.model.query.InfoQuery;
import org.vpac.qc.model.query.Query;
import org.vpac.qc.model.query.UserInputQuery;

/**
 * This class is a generic client which can submit one/more {@link Query} (s) to
 * a query service. A query service can be everything from a web service to a
 * website to a class...
 * <p>
 * If you want to implement a Generic client, first you have to implement an
 * initializeClient method which initializes your client. This can be an empty
 * method if nothing needs to be initialized.
 * <p>
 * After that, there has to be a getQueryArgumentNames method (actually two: one
 * with parameters and a default one) which asks the query service about the
 * amount and the names of the arguments. This method has to return an array of
 * Strings.
 * <p>
 * The formatArgumentNames method takes all required argument values and formats
 * them so that the query service understands them.
 * 
 * @author Markus Binsteiner
 * 
 */
public abstract class GenericClient {

	static final Logger myLogger = Logger.getLogger(GenericClient.class
			.getName());

	protected Object stub = null;

	protected String defaultContext = null;

	private Element rootElement = null;

	/**
	 * This method is needed to initialize the client. This can be something
	 * like authenticating and connecting to a WebService or loading a webpage
	 * into memory or whatever is needed. In the end the "stub"-Object should be
	 * an Object of some type (and therefore non-null, because otherwise an
	 * exception is thrown) for/through which the queries are executed.
	 * 
	 * @param args
	 *            The array that contains all parameters to initialize the
	 *            GenericClient
	 * @throws ClientNotInitializedException
	 *             if for some reason the client can't be initialized
	 */
	abstract protected void initializeClient(Object[] args)
			throws ClientNotInitializedException;

	/**
	 * This one returns all possible queries of this query service. You can
	 * implement it as a static String-array (e.g. prepare it in the
	 * initializeClient() method) or dynamicaly query a web service or
	 * whatever...
	 * 
	 * @return all possible queries of the query service this client is written
	 *         for
	 */
	abstract public String[] getQueries();

	/**
	 * Returns the names of all QueryArguments (and therefor all parameters of
	 * this Query. These names are then used to look in the xml-document for
	 * this query/client and to create a QueryArgument for this argument name.
	 * The default context (as returned by the determineDefaultContext method
	 * for a GenericClient) is used.
	 * 
	 * @param query
	 *            The Query you want to know the QueryArguments for.
	 * @return The names of all QueryArguments for this Query.
	 * @throws Exception
	 */
	abstract public String[] getQueryArgumentNames(Query query)
			throws Exception;

	/**
	 * The same as above with the difference that the Query is called for a
	 * specific context.
	 * 
	 * @param query
	 *            The Query you want to know the QueryArguments for.
	 * @param context
	 *            The context in which the Query is called.
	 * @return The names of all QueryArguments for this Query in this context.
	 * @throws Exception
	 */
	abstract public String[] getQueryArgumentNames(Query query, String context)
			throws Exception;

	/**
	 * Analog to the getQueryArgumentNames method this method returns the names
	 * of the QueryResults.
	 * 
	 * @param query
	 *            he Query you want to know the QueryResults for.
	 * @param context
	 *            The context in which the Query is called.
	 * @param arguments
	 *            The arguments used to execute the Query.
	 * @return The names of all QueryResults for this Query.
	 * @throws QueryException
	 */
	abstract public String[] getQueryResultNames(Query query, String context,
			Object[] arguments) throws QueryException;

	/**
	 * This one is a bit tricky to explain. I had to add it because VOMRS has a
	 * return String for a getQueryArguementNames type query like: INSTITUTION
	 * REPDN REPCA RIGHTS EMAIL First name,<First name>,Last name,<Last
	 * name>,Phone,<Phone> This method put's the computed/user-inputted values
	 * of the QueryArguments back in the right format for the Query service(e.g.
	 * substitutes the <First name> substring with "Markus" without changing the
	 * "First name" substring.
	 * 
	 * @param argumentValues
	 *            All the values of all the QueryArguments in the order the
	 *            getQueryArgumentNames method returns.
	 * @return All the values in the right format and order for the Query to be
	 *         submitted.
	 * @throws ClassCastException
	 */
	abstract public Object[] formatArgumentValueArray(Object[] argumentValues)
			throws ClassCastException;

	/**
	 * A context can be something like a context the one who wants to submit a
	 * query. Depending on the context the Query returns different results (e.g.
	 * asking for permissions to do something is different for normal users or
	 * admins). This method In some (most) cases contexts are not used at all.
	 * In such cases just return null. returns all contexts within this Query
	 * service.
	 * 
	 * @return all contexts within this Query service
	 */
	abstract public String[] getAllContexts();

	/**
	 * This method returns all contexts for the current user/environment. In
	 * some (most) cases contexts are not used at all. In such cases just return
	 * null.
	 * 
	 * @return all contexts for the current user/environment
	 */
	abstract public String[] getMyContexts();

	/**
	 * Saves the default context for this query service in the defaultContext
	 * varible. Is called within the constructor. In some (most) cases contexts
	 * are not used at all. In such cases just ignore this method.
	 */
	abstract protected void determineDefaultContext();

	/**
	 * This class provides a method to call a method on an object with a String,
	 * like: genericClientObject.execute("getService", new Object("argument1",
	 * 1, new OtherObject("test")); I wrote it to have a wrapper for
	 * webservices, but it should work for other purposes as well.
	 * 
	 * If you write a class that extends this class, be sure to implement and
	 * call initStub(Object[] args) before you call execute(...).
	 * 
	 * @param args
	 *            The arguments that are needed to initialize the stub
	 * @throws ClientNotInitializedException
	 * @throws GeneralSecurityException
	 *             if there is a security realted problem
	 * @throws StubNotInitializedException
	 *             if there is another problem
	 * @throws IOException
	 * @throws JDOMException
	 */
	public GenericClient(Object[] args, Document doc)
			throws ClientNotInitializedException {

		rootElement = doc.getRootElement();

		initializeClient(args);
		if (stub == null)
			throw new ClientNotInitializedException(
					"Stub is null. Something's wrong with the initializeClient method of this class.");
		determineDefaultContext();
	}

	public GenericClient(Object[] args, File xmlConfigFile)
			throws ClientNotInitializedException {

		SAXBuilder builder = new SAXBuilder();
		Document doc = null;

		try {
			doc = builder.build(xmlConfigFile);
		} catch (Exception e) {
			throw new ClientNotInitializedException(e);
		}

		rootElement = doc.getRootElement();

		initializeClient(args);
		if (stub == null)
			throw new ClientNotInitializedException(
					"Stub is null. Something's wrong with the initializeClient method of this class.");
		determineDefaultContext();
	}

	/**
	 * This executes a query service method with arguments
	 * 
	 * @param method_name
	 *            The name of the method (e.g. "getServiceArguments")
	 * @param arguments
	 *            The arguments (e.g. {"registerMember", "Visitor"})
	 * @return the answer from the server
	 * @throws Exception
	 */
	public Object[] execute(String method_name, Object[] arguments)
			throws Exception {

		Class[] argClasses = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			argClasses[i] = arguments[i].getClass();
		}

		Method method = null;
		try {
			method = stub.getClass().getMethod(method_name, argClasses);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		}
		myLogger.debug("Executing: " + method.getName());
		for (Class cls : method.getParameterTypes()) {
			myLogger.debug("Argument: " + cls.getName());
		}

		Object[] result = null;

		try {
			result = (Object[]) method.invoke(stub, arguments);
		} catch (ClassCastException e) {
			// if the result is not an array
			result = new Object[] { method.invoke(stub, arguments) };
		}

		return result;
	}

	/**
	 * Returns a UserInputQuery for the query service of the according client.
	 * Uses the default context of this query service.
	 * 
	 * @param name
	 *            The name of the query
	 * @return a UserInputQuery
	 */
	public UserInputQuery createUserInputQuery(String name) {
		return new UserInputQuery(name, this, this.getDefaultContext());
	}

	public UserInputQuery createUserInputQuery(String name, String context) {
		// TODO check for valid context
		return new UserInputQuery(name, this, context);
	}

	public InfoQuery createInfoQuery(String name) {
		return new InfoQuery(name, this, this.getDefaultContext());
	}

	public InfoQuery createInfoQuery(String name, String context) {
		// TODO check for valid context
		return new InfoQuery(name, this, context);
	}

	public Element getRootElement() {
		return rootElement;
	}

	public String getDefaultContext() {
		return defaultContext;
	}

}
