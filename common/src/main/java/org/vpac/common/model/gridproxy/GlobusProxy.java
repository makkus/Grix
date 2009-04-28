/* Copyright 2006 VPAC
 * 
 * This file is part of common.
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

package org.vpac.common.model.gridproxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.globus.gsi.GlobusCredential;
import org.globus.tools.proxy.DefaultGridProxyModel;
import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.common.model.GlobusLocations;

/**
 * A GlobusProxy is the default X509 proxy that is used with Globus. It is normally stored under 
 *  /tmp/x509up_u<uid>. 
 * @author Markus Binsteiner
 *
 */
public class GlobusProxy extends GridProxy {
	
	private DefaultGridProxyModel model = null;
	
	public GlobusProxy(File proxyFile){
		super(proxyFile);
		model = new DefaultGridProxyModel();
		model.getProperties().setUserCertFile(GlobusLocations.defaultLocations().getUserCert().toString());
		model.getProperties().setUserKeyFile(GlobusLocations.defaultLocations().getUserKey().toString());
	}
	
	protected void checkPrerequisites() throws MissingPrerequisitesException {

		ArrayList<File> missingFiles = new ArrayList<File>();
		if (!GlobusLocations.defaultLocations().userCertExists()) {
			missingFiles.add(GlobusLocations.defaultLocations().getUserCert());
		}
		if (!GlobusLocations.defaultLocations().userKeyExists()) {
			missingFiles.add(GlobusLocations.defaultLocations().getUserKey());
		}

		if (missingFiles.size() != 0)
			throw new MissingPrerequisitesException(missingFiles);
	}	
	
	protected GlobusCredential createProxy(char[] passphrase, long lifetime_in_ms) throws Exception{
		model.getProperties().setProxyLifeTime(new Long((lifetime_in_ms)/(1000*3600)).intValue());
		return model.createProxy(new String(passphrase));
	}

	@Override
	protected ArrayList<String> proxyInfo() {
		//TODO ?
		return new ArrayList<String>();
	}
	
}
