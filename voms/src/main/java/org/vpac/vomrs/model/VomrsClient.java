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

package org.vpac.vomrs.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.cert.CertificateException;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.qc.control.common.VOMRS_utils;
import org.vpac.qc.model.clients.ClientNotInitializedException;
import org.vpac.qc.model.clients.GenericClient;
import org.vpac.qc.model.clients.QueryException;
import org.vpac.qc.model.query.Query;
import org.vpac.voms.model.VO;

import fnal.vox.vomrs.services.VOMRS;
import fnal.vox.vomrs.services.VOMRSServiceLocator;

/**
 * This class connects to a vomrs server and executes web service queries. 
 * 
 * @author Markus Binsteiner
 * 
 */
public class VomrsClient extends GenericClient {
	
	static final Logger myLogger = Logger.getLogger(VomrsClient.class.getName());

	private URL url = null;

	private String[] serviceArguments = null;

	private String[] pi = null;

	private int startPi = Integer.MAX_VALUE;

	/**
	 * Default constructor
	 * 
	 * @param url the url of the VOMRS server as a string (e.g. new VomrsClient(new String[]{"https://vomrs.apac.edu.au:8443/vo/APAC/services/VOMRS?wsdl"})
	 * @param doc the root of the xml-config file
	 * @throws ClientNotInitializedException
	 */
	public VomrsClient(String url, Document doc)
			throws ClientNotInitializedException {
		super(new Object[] { url }, doc);
	}
	
	/**
	 * Factory method of the above constructor
	 * @param vo the vo
	 * @param doc the root element of the xml config
	 * @return a VomrsClient for that certain VO
	 * @throws ClientNotInitializedException
	 */
	public static VomrsClient getVomrsClient(VO vo, Document doc) throws ClientNotInitializedException {

		return new VomrsClient(vo.getVomrs_url(), doc);
	}

	/**
	 * This inits the whole VOMRS webservice and loads all services in the
	 * methods field.
	 * 
	 * @throws CertificateException
	 * 
	 * @throws MalformedURLException
	 * @throws ServiceException
	 * @throws RemoteException
	 * @throws CertificateException
	 * @throws ServiceException
	 */
	protected void initializeClient(Object[] args) throws ClientNotInitializedException {

		if (args.length != 1)
			throw new ClientNotInitializedException("Wrong set of arguments.");

		try {
			this.url = new URL((String) args[0]);
		} catch (MalformedURLException e) {
			throw new ClientNotInitializedException(
					"Could not parse URL for VOMRS server.", e);
		}

		try {
			//VOMRS_utils.initVomrsWsWithHostCert();
			VOMRS_utils.initVomrsWS();
		} catch (CertificateException e1) {
			throw new ClientNotInitializedException(
					"Could not init VOMRS web services.", e1);
		}

		VOMRSServiceLocator service = new VOMRSServiceLocator();

		try {
			stub = service.getVOMRS(url);
		} catch (ServiceException e) {
			throw new ClientNotInitializedException(e.getMessage(), e);
		}

		myLogger
				.debug("Stub successfully initialized. Connected to vomrs web service.");

		return;
	}

	/**
	 * Returns all the representatives of the VO the VomrsClient is contacting. Not important for the 
	 * GenericClient functionality of this client.
	 * @return all the representatives of this VO
	 */
	public String[] getRepresentatives() {

		String[] representatives = null;

		try {
			representatives = (String[]) execute("getRepresentatives",
					new Object[] {});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myLogger.error(e);
		}

		return representatives;
	}

	/**
	 * Returns the information of the user whose grid proxy is used to contact the VOMRS server. Not important for the 
	 * GenericClient functionality of this client.
	 * @return information about VO membership
	 */
	public String[] getMyInfo() {
		String[] info = null;

		try {
			info = (String[]) execute("getMbrInfo", new Object[] {});
		} catch (Exception e) {
			// does not matter
		}

		return info;
	}

	public String[] getAllContexts() {

		String[] roles = null;

		try {
			roles = (String[]) execute("getRoles", new Object[] {});
		} catch (Exception e) {
			// does not matter
		}

		return roles;
	}

	public String[] getMyContexts() {

		String[] roles = null;

		try {
			roles = (String[]) execute("getMbrRoles", new Object[] {});
		} catch (Exception e) {
			// does not matter
			//e.printStackTrace();
			myLogger.error(e);
		}

		if (roles == null || roles.length == 0)
			return new String[] { "Visitor" };

		return roles;
	}

	@Override
	public String[] getQueryArgumentNames(Query userInputQuery)
			throws QueryException {

		String[] roles = getAllContexts();
		String role = null;
		try {
			roles = ((VOMRS) stub).getMbrRoles();
			role = roles[0];
			role = "VOAdmin";
		} catch (Exception e) {
			role = "Visitor";
		}

		// String highestRole = null;
		// for ( String defaultContext : roles ){
		// if ( highestRole == null ) highestRole = defaultContext;
		// else {
		// }
		//			
		// }

		// TODO
		return getQueryArgumentNames(userInputQuery, role);
	}

	/**
	 * Not implemented. Returns null.
	 * @return
	 */
	public String[] returnArgumentArray() {

		return null;
	}

	public Object[] formatArgumentValueArray(Object[] argumentValues)
			throws ClassCastException {

		// pi means personal information in this method.
		
		if (argumentValues.length == 0)
			return new Object[] {};
		Object[] result = null;
		if (startPi == Integer.MAX_VALUE) {
			result = new Object[argumentValues.length];
			for (int i = 0; i < argumentValues.length; i++) {
				result[i] = (String) argumentValues[i];
			}
		} else {
			result = new Object[startPi + 1];
			int i;
			for (i = 0; i < startPi; i++) {
				result[i] = (String) argumentValues[i];
			}
			// TODO test i
			String[] pi_result = new String[pi.length * 2];
			for (int j = 0; j < pi.length; j++) {
				pi_result[j * 2] = pi[j];
				pi_result[j * 2 + 1] = (String) (argumentValues[i + j]);

			}
			Object temp = (Object) pi_result;
			// TODO check this:
			startPi = Integer.MAX_VALUE;
			result[i] = temp;
		}
		// ((VOMRS)stub).regi
		// result = new Object[]{"VPAC", "/C=AU/O=APACGrid/OU=VPAC/CN=Chris
		// Kendrick",
		// "/C=AU/O=APACGrid/OU=CA/CN=APACGrid/Email=camanager@vpac.org",
		// "full", "markus@vpac.org", ((Object)(new String[]{"First
		// name","Markus","Last
		// name","Binsteiner","Phone","1234","TestPIParameter1","testpar"}))};
		return result;
	}

	@Override
	public String[] getQueryArgumentNames(Query userInputQuery, String role)
			throws QueryException {

		// if ( parameter.length != 1 ) throw new QueryException("Could not get
		// QueryArgument names. Wrong set of parameters for
		// "+this.getClass().getName());
		// String defaultContext = parameter[0];

		// this sets also the servicearguments, startPi and pi field
		// TODO check for not-applicable methods
		try {
			serviceArguments = ((VOMRS) stub).getServiceArguments(
					userInputQuery.getName(), role);

			if (serviceArguments.length == 0
					|| "".equals(serviceArguments[0].toString()))
				//TODO check whether I return a non array here???
				return new String[0];
			if ( pi == null )
				pi = ((VOMRS) stub).getPI();
		} catch (RemoteException e) {
			throw new QueryException("Could not get QueryArgument names with "
					+ this.getClass().getName() + ": " + e.getMessage());
		}

		for (String arg : serviceArguments) {
			myLogger.debug("QueryArguments: " + arg);
		}

		for (String arg : pi) {
			myLogger.debug("QueryArguments: " + arg);
		}

		// TODO ok. here it gets dodgy. but I can't think of a better way to
		// parse the answer string from vomrs

		int index = serviceArguments[0].indexOf(pi[0]);
		String[] arg_string_new = null;

		if (index == -1) {
			// this means, there is no pi in this service request involved
			arg_string_new = serviceArguments[0].split(" ");
		} else {
			serviceArguments = serviceArguments[0].substring(0, index - 1)
					.split(" ");
			arg_string_new = new String[serviceArguments.length + pi.length];
			for (int i = 0; i < serviceArguments.length; i++) {
				arg_string_new[i] = serviceArguments[i];
			}
			startPi = serviceArguments.length;
			for (int i = serviceArguments.length; i < arg_string_new.length; i++) {
				arg_string_new[i] = pi[i - serviceArguments.length];
			}
		}
		myLogger.debug("New userInputQuery argument names:");

		for (String arg : arg_string_new) {
			myLogger.debug("QueryArguments: " + arg);
		}

		return arg_string_new;
	}

	@Override
	public String[] getQueries() {

		String[] queries = null;
		try {
			queries = (String[]) (execute("getServices", new Object[] {}));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myLogger.error(e);
		}

		for (int i = 0; i < queries.length; i++) {
			StringBuffer temp = new StringBuffer(queries[i]);
			queries[i] = temp.replace(0, 1, temp.substring(0, 1).toLowerCase())
					.toString();
		}

		return queries;
	}

	@Override
	protected void determineDefaultContext() {

		String[] myroles = getMyContexts();
		if (myroles == null || myroles.length == 0) {
			defaultContext = "Visitor";
			return;
		} else if (myroles.length == 1) {
			defaultContext = myroles[0];
			return;
		} else {
			// TODO roles
			defaultContext = myroles[myroles.length - 1];
			return;
		}
	}
	
	/**
	 * Returns the personal information that is required within this VO. Not important for the 
	 * GenericClient functionality of this client.
	 * @return all fields of required personal information within this VO
	 */
	public String[] getPI() {
		String[] pi = null;

		try {
			pi = (String[]) execute("getPI", new Object[] {});
		} catch (Exception e) {
			// does not matter
		}

		return pi;
	}

	/**
	 * Returns the personal information of the current user. For description of which field is what use the getPI() function.
	 * Not important for the GenericClient functionality of this client.
	 * @return the personal information of the current user.
	 */
	public String[] getMyPI() {
		String[] pi = null;

		try {
			pi = (String[]) execute("getMbrPI", new Object[] {});
		} catch (Exception e) {
			// does not matter
		}

		return pi;
	}
	/**
	 * Returns all subgroups off the current VO. 
	 * Not important for the GenericClient functionality of this client. Just a convenience method
	 * because it is used more often.
	 * @return all subgroups of this VO.
	 */
	public String[] getAllGroups() {
		String[] allGroups = null;

		try {
			allGroups = (String[]) execute("getGroups", new Object[] {});
		} catch (Exception e) {
			// does not matter
		}

		return allGroups;
	}

	@Override
	public String[] getQueryResultNames(Query query, String role,
			Object[] arguments) throws QueryException {
		String[] temp = null;

		// TODO the null part
		try {
			temp = (((VOMRS) stub).getServiceReturnValues(query.getName(),
					role, ""));
			if ( pi == null )
				pi = ((VOMRS) stub).getPI();
		} catch (RemoteException e) {
			throw new QueryException("Could not get QueryResultNames with "
					+ this.getClass().getName() + ": " + e.getMessage());			
		}
		
		// TODO ok. here it gets dodgy. but I can't think of a better way to
		// parse the answer string from vomrs

		int index = temp[0].indexOf(pi[0]);
		String[] arg_string_new = null;

		if (index == -1) {
			// this means, there is no pi in this service request involved
			arg_string_new = temp[0].split(" ");
		} else {
			String temp_pi = temp[0].substring(index);
			temp = temp[0].substring(0, index - 1)
					.split(" ");
			arg_string_new = new String[temp.length + 1];
			int i;
			for (i = 0; i < temp.length; i++) {
				arg_string_new[i] = temp[i];
			}
			arg_string_new[i] = temp_pi;
		}
		myLogger.debug("New userInputQuery returnvalue names:");

		for (String arg : arg_string_new) {
			myLogger.debug("QueryReturnValue: " + arg);
		}

		return arg_string_new;
	}
	
//	public static void main(String[] args) {
//
//		try {
//			GenericClient soapClient = new VomrsClient(
//					"https://vomrsdev.vpac.org:8443/vo/Markus/services/VOMRS?wsdl" ,
//					new File("/home/markus/workspace/voc/queries_test.xml"));
//
//			// UserInputQuery userInputQuery = new
//			// UserInputQuery("registerMember", soapClient);
//			InfoQuery infoQuery = new InfoQuery("registerMember", soapClient,
//					"Visitor");
//			infoQuery.init();
//
//			for (QueryArgument qarg : infoQuery.getArguments()) {
//
//				System.out.println("Argument: " + qarg.getName());
//				System.out.println("\tType: "
//						+ qarg.getType().getAttributeValue("name"));
//				try {
//					try {
//						for (Object obj : (Object[]) qarg.getValue()) {
//							System.out.println("Object: " + obj + "\tClass: "
//									+ obj.getClass().toString());
//						}
//					} catch (ClassCastException e) {
//						System.out.println("Single object: " + qarg.getValue()
//								+ "\tClass: "
//								+ qarg.getValue().getClass().toString());
//					}
//				} catch (NullPointerException npe) {
//					System.out.println("No value for:" + qarg.getName());
//				}
//				System.out.println();
//
//			}
//			infoQuery.submit();
//
//			for (Object result : infoQuery.getResult()) {
//				System.out.println("Result: " + result.toString());
//			}
//			Object[] temp = infoQuery.getQueryResultNames();
//			for (Object result : temp) {
//				System.out.println("Result: " + result.toString());
//			}			
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}	
	
	
	/**
	 * Helper function
	 * 
	 * @param groups the groups string returned by the MbrInfo query
	 * @return all groups the user is member of
	 */
	public static String[] parseGroups(String groups){
		String [] parsed = groups.substring(1, groups.length()-1).split("\\]\\[");
		
		return parsed;
	}

}
