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

package org.vpac.voms.view.cli;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.model.VO;
import org.vpac.voms.model.proxy.NoVomsProxyException;
import org.vpac.voms.model.proxy.VomsProxy;

/**
 * Quick hack.
 * 
 * @author Markus Binsteiner
 *
 */
public class VomsProxyInit {
	
	static final Logger myLogger = Logger.getLogger(VomsProxyInit.class
			.getName());	
	
	Options options = null;
	String voms = null;
	VO vo = null;
	
	private void initOptions(){
		options = new Options();
		
		options.addOption("v", "voms", true, "Specify voms server.");
	}
	
	public void parseCommandLineArguments(String[] args) throws ParseException{
		
		if ( options == null ) initOptions();
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse( options, args);
		
		if(cmd.hasOption("v")) {
		    voms = cmd.getOptionValue("v");
		    vo = LocalVomses.getLocalVomses().getVO(voms);
		    if ( vo == null ) throw new ParseException("Can't find voms \""+voms+"\" in vomses directory.");
		    myLogger.debug("Found voms: \""+voms+"\"");
		}
		else {
		    throw new MissingOptionException("You have to provide the voms server you want to contact.");
		}
	}
	
	
	public static void main (String[] args){
		
		VomsProxyInit vpi = new VomsProxyInit();
		try {
			vpi.parseCommandLineArguments(args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("Please enter GRID passphrase: ");
		char[] passphrase = "xxx".toCharArray();
		
		File proxyFile = LocalProxy.getProxyFile();
		VomsProxy proxy = new VomsProxy( proxyFile, vpi.vo, "A", null);
		
		try {
			proxy.init( passphrase, 43200000 );
		} catch (MissingPrerequisitesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		try {
			proxy = new VomsProxy(proxyFile);
		} catch (NoVomsProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
