/* Copyright 2006 VPAC
 * 
 * This file is part of voms.
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

package org.vpac.voms.model;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.vpac.common.control.Helpers;
import org.vpac.qc.model.clients.ClientNotInitializedException;
import org.vpac.qc.model.clients.GenericClient;
import org.vpac.qc.model.clients.QueryException;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.InfoQuery;
import org.vpac.qc.model.query.Query;
import org.vpac.vomrs.control.Vomrs_Utils;
import org.vpac.vomrs.model.VomrsClient;

/**
 * Since a VOMS server does not provide a web service interface to get information about membership
 * this class really contacts a VOMRS server. Basically it's a wrapper of a VomrsClient and not of much use. Also, most 
 * functions are not implemented. So unless you know what you are doing...
 * 
 * @author Markus Binsteiner
 *
 */
public class VomsClient extends GenericClient {
	
	static final Logger myLogger = Logger.getLogger(VomsClient.class.getName());
	
	private VomrsClient vomrsclient = null;

	/**
	 * Default constructor 
	 * 
	 * @param url the url of the VOMRS endpoint
	 * @param doc the root element of the query xml config file
	 * @throws ClientNotInitializedException
	 */
	public VomsClient(String url, Document doc) throws ClientNotInitializedException {
		super(new Object[] { }, doc);
		vomrsclient = new VomrsClient(url, doc);
	}
	
	public static VomsClient getVomsClient(VO vo, Document doc) throws ClientNotInitializedException{
		//TODO change this once voms has it's own real client
		
		return new VomsClient(vo.getVomrs_url(), doc);
	}

	
	@Override
	protected void determineDefaultContext() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] formatArgumentValueArray(Object[] argumentValues)
			throws ClassCastException {
		// TODO Auto-generated method stub
		return new Object[]{};
	}

	@Override
	public String[] getMyContexts() {

		InfoQuery infoQuery = null;
		try {
			infoQuery = new InfoQuery("getMbrInfo", vomrsclient, "Member");
			infoQuery.init();
			infoQuery.submit();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myLogger.error(e);
		} catch (ArgumentsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myLogger.error(e);
		}
		
		return Vomrs_Utils.getGroupRoles((String[])infoQuery.getResult());
	}

	@Override
	public String[] getQueries() {
		return null;
	}

	@Override
	public String[] getQueryArgumentNames(Query query) throws Exception {
		// TODO Auto-generated method stub
		return new String[]{};
	}

	@Override
	public String[] getQueryArgumentNames(Query query, String role)
			throws Exception {
		// TODO Auto-generated method stub
		return new String[]{};
	}

	@Override
	public String[] getQueryResultNames(Query query, String role,
			Object[] arguments) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAllContexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initializeClient(Object[] args) throws ClientNotInitializedException {
		// TODO Auto-generated method stub

	}
	
//	public static void main(String[] args){
//		
//		try {
//			GenericClient client = new VomsClient("https://vomrsdev.vpac.org:8443/vo/Markus/services/VOMRS?wsdl" ,
//				new File("/home/markus/workspace/voc/queries_test.xml"));
//
//			client.getMyRoles();
//		
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		
//		
//	}

}
