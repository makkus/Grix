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

package org.vpac.qc.model.clients;

import java.io.File;

import org.jdom.Document;
import org.vpac.qc.examples.MathService.MathService;
import org.vpac.qc.model.query.Query;

/**
 * This is just an example client for the {@link org.vpac.qc.examples.MathService.MathService} query service.
 * 
 * @author Markus Binsteiner
 *
 */
public class MathServiceClient extends GenericClient {
	
	public MathServiceClient(Document doc) throws ClientNotInitializedException{
		super(new Object[]{}, doc);
	}
	
	public MathServiceClient(File configFile) throws ClientNotInitializedException{
		super(new Object[]{}, configFile);
	}

	@Override
	protected void determineDefaultContext() {
		//do nothing

	}

	@Override
	public Object[] formatArgumentValueArray(Object[] argumentValues)
			throws ClassCastException {
		

		return argumentValues;
	}

	@Override
	public String[] getAllContexts() {
		return null;
	}

	@Override
	public String[] getMyContexts() {
		return null;
	}

	@Override
	public String[] getQueries() {
		return new String[]{"add", "substract"};
	}

	@Override
	public String[] getQueryArgumentNames(Query query) throws Exception {
		if ( "add".equals(query.getName()) ) return new String[]{"Parameter a", "Parameter b"};
		else if ( "substract".equals(query.getName()) ) return new String[]{"Parameter a", "Parameter b"};
		else throw new Exception("No such Query: "+query.getName()+" for the math query service.");
	}

	@Override
	public String[] getQueryArgumentNames(Query query, String context)
			throws Exception {
		return getQueryArgumentNames(query);
	}

	@Override
	public String[] getQueryResultNames(Query query, String context,
			Object[] arguments) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initializeClient(Object[] args)
			throws ClientNotInitializedException {
		stub = new MathService();
	}

}
