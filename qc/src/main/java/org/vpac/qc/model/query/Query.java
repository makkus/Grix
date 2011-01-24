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

import java.net.NoRouteToHostException;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.vpac.qc.model.clients.GenericClient;
import org.vpac.qc.model.clients.QueryException;

/**
 * This is, beneath {@link GenericClient} and
 * {@link org.vpac.qc.control.retrievers.QcRetriever} the most important class
 * in the qc library.
 * <p>
 * It represents one query to a query service, which can be everything from
 * getting information to subscribing to some service or executing a remote
 * command.
 * <p>
 * I started working on qc when I had to programm an application that needed to
 * help the user subscribing to a VOMRS
 * (http://osg.ivdgl.org/bin/view/Integration/VOMRS) server via web services.
 * The user has to provide his name, phone no., email as well as a set of
 * personal information that can differ for every VOMRS server. Because it is
 * difficult to build a gui form if you don't know exactly what kind of
 * information the user has to provide I thought it would be good to have some
 * kind of library that works like this:
 * <p>
 * <ol>
 * <li>There is a query service that provides functionality you need to access
 * and that is ready to use.
 * <li>Most of the times, this query service will be a web service, but it can
 * be everything you can imagine, like a simple java class, a web page form,...
 * To contact this query service, you need to write a query client. Such a
 * client has to extend the GenericClient abstract class. This client knows:
 * <ol>
 * <li>How to make a connection to the query service
 * <li>Which queries the query service provides (this can be either through a
 * static list within the client or, if it is supported, by asking the query
 * service)
 * <li>For every query: what are the arguments to execute every query (again
 * either through a static list or by asking the service). That's why I needed
 * this in the first place, see above.
 * <li>What the answer to a query means (or what format/class it is in)
 * </ol>
 * <li>There is an optional xml file that allows to configure the query you are
 * about to submit. For example, you can set default values for certain query
 * arguments or get a set of values for the user to choose from (e.g. through a
 * combo-box). These values/preselctions can be either set statically in the xml
 * file or the xml file configures a so-called retriever to get it. Such a
 * receiver has to extend the class QcRetriever. There are a few existing
 * receivers already included with qc. For exampe one that parses java-style
 * property files, one that can call a java class and execute a method on it and
 * a very usefull one that can use a GenericClient to get values/preselections.
 * <li>For the user input you need to write a class that extends the UserInput
 * abstract class. This class is the user interface and displays a form with all
 * necessary query arguments that the user has to provide values for. qc comes
 * with two already implemented ui's, one for commandline input and one that
 * creates a JPanel with a submit and cancel button.
 * </ol>
 * <p>
 * The workflow of submitting a query is:
 * <p>
 * <ol>
 * <li>Creating a GenericClient for the specific query service.<br>
 * {@code GenericClient client = new MathServiceClient(new File("queries.xml"));}
 * <li>Either let the GenericClient display a list of all available queries and
 * let the user pick the one he wants to use or, more likely, you already know
 * the name of the query and you can hardcode it in your code.<br>
 * {@code UserInputQuery currentQuery = client.createUserInputQuery("add");}
 * <li>Init the query (this calls the initializeClient() method in the client
 * and basically creates the connection to the service).<br>
 * {@code currentQuery.init();}
 * <li>Now you have to create a {@link org.vpac.qc.model.query.UserInput} object
 * to ask for, uhm, user input.<br>
 * {@code Voc voc = new Voc();} (go here for information about
 * {@link org.vpac.qc.view.cli.Voc}
 * <li>After that, connect the UserInput object with the query (which internally
 * calls the connect() method of the Query object and the init() method of the
 * UserInput object).<br>
 * {@code voc.connect(currentQuery);}
 * <li>Now is the time to actually ask the user all the things we need to know:<br>
 * {@code currentQuery.fillUserInput();}
 * <li>And then submit the query:<br>
 * {@code currentQuery.submit();}
 * <li>Finally, get the result (if the query provides one). At the moment you
 * have to know which format the result has (e.g. an Array of Strings):<br>
 * 
 * <pre>
 * {@code  Object[] result = currentQuery.getResult();
 * for ( Object part : result ){
 *    System.out.println("Result: "+(String)part);
 * }
 * }
 * 
 * <pre>
 * </ol>
 * <p>
 * The xml file for this example could look like this:
 * <p>
 * 
 * <pre>
 * {@code 
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE QueryArguments >
 * <QueryArguments>
 * 		<QueryArgument name="Parameter a" prettyName="The first value" description="The first value of this calculation.">
 * 			<type name="userinput">
 * 			</type>
 * 		</QueryArgument>
 * 		<QueryArgument name="Parameter b" prettyName="The second number" description="The second value of this calculation.">
 * 			<type name="userinput">
 * 			</type>
 * 		</QueryArgument>
 * </QueryArguments>}
 * </pre>
 * <p>
 * Or, if you want to change the displayed name of the second parameter
 * according to the query name you could:
 * <p>
 * 
 * <pre>
 * {@code 
 * <QueryArgument name="Parameter b" prettyName="The second number" description="The second value of this calculation.">
 * 		<type name="userinput">
 * 		</type>
 * 		<query name="add" prettyName="The value to add to the first value." >
 * 			<type name="userinput">
 * 			</type>
 * 		</query>
 * 		<query name="substract" prettyName="The value to substract from the first value." >
 * 			<type name="userinput">
 * 			</type>
 * 		</query>
 * </QueryArgument>
 * }
 * </pre>
 * <p>
 * If you want to have the same value for the first parameter for every time,
 * you can have this in the xml file:
 * <p>
 * 
 * <pre>
 * {@code
 * <QueryArgument name="Parameter a" prettyName="The first value" description="The first value of this calculation.">
 * 		<type name="default">
 * 				<method name="Default">
 * 						<method_arg object="java.lang.String">33</method_arg>
 * 				</method>
 * 	</type>
 * </QueryArgument>
 * }
 * </pre>
 * <p>
 * Note: At the moment, it is safest to use Strings as parameters as this is
 * tested. It should work with other classes as well but I did not really need
 * it until now. Also there is not much testing whether a parameter is allowed
 * or not...
 * <p>
 * Here is a diagram of how it works:
 * <p>
 * <img src="http://www.vpac.org/~markus/qc/qc.png">
 * 
 * 
 * 
 * 
 * @author Markus Binsteiner
 * 
 */
public abstract class Query {

	protected String context = null;

	protected Object[] result = null;

	protected Exception exception = null;

	protected String name = null;

	protected QueryArgument[] arguments = null;

	protected String[] queryResultNames = null;

	protected GenericClient client = null;

	static final Logger myLogger = Logger.getLogger(UserInputQuery.class
			.getName());

	public Query(String name, GenericClient client, String context) {

		this.name = name;
		this.client = client;
		this.context = context;

	}

	private void init(String[] arguments) throws JDOMException,
			ArgumentsException {
		setArguments(arguments);
		prepare();
	}

	public void init() throws JDOMException, ArgumentsException {
		init(setArguments());
	}

	private String[] setArguments() throws JDOMException, ArgumentsException {
		String[] argNames;
		try {
			argNames = client.getQueryArgumentNames(this, context);
		} catch (NoRouteToHostException nrthe) {
			// TODO
			throw new ArgumentsException(
					"Could not set arguments for userInputQuery \"" + this.name
							+ "\": " + nrthe.getMessage());
		} catch (Exception e) {
			throw new ArgumentsException(
					"Could not set arguments for userInputQuery \"" + this.name
							+ "\": " + e.getMessage());
		}
		return argNames;
	}

	public GenericClient getClient() {
		return client;
	}

	public void submit() {

		try {
			this.result = client.execute(this.name,
					client.formatArgumentValueArray(getArgumentValues()));
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			this.exception = e;
		}

	}

	public Object[] getArgumentValues() {

		Object[] result = new Object[arguments.length];

		for (int i = 0; i < arguments.length; i++) {
			result[i] = arguments[i].getValue()[0];
		}

		return result;
	}

	public String getName() {
		return name;
	}

	public QueryArgument[] getArguments() {
		return arguments;
	}

	public Object[] getResult() {
		return result;
	}

	abstract protected void setArguments(String[] arguments)
			throws JDOMException, ArgumentsException;

	abstract public void prepare() throws ArgumentsException;

	public String[] getQueryResultNames() throws QueryException {
		if (queryResultNames == null) {
			// TODO the new Object[]{} part
			queryResultNames = client.getQueryResultNames(this, context,
					new Object[] {});
		}
		return queryResultNames;
	}

	public Exception getException() {
		return exception;
	}

}
