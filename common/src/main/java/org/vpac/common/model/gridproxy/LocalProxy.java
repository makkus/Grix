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
import java.security.GeneralSecurityException;
import java.util.Vector;

import org.globus.tools.proxy.DefaultGridProxyModel;
import org.vpac.common.exceptions.MissingPrerequisitesException;

/**
 * Since it does not make sense to have several GridProxies on one machine this class
 * holds a static GridProxy (a GlobusProxy) and provides easy access to it.
 * 
 * @author Markus Binsteiner
 *
 */
public class LocalProxy {
	
	private static Vector listeners = new Vector();
	
	private static GridProxy defaultProxy = null;
	
	private static DefaultGridProxyModel model = new DefaultGridProxyModel();
	private static File proxyFile = new File(model.getProperties().getProxyFile());


	/**
	 * @return the (one and only) local GridProxy (normally under /tmp/x509up_u<uid> or null if there is none
	 */
	public static GridProxy getDefaultProxy() {
//		if (defaultProxy == null) {
//			//TODO change that so that VomsProxy is possible to
//			//defaultProxy = new GlobusProxy(new File(model.getProperties().getProxyFile()));
//		}
		return defaultProxy;
	}	
	
	public static void createPlainGlobusProxy(char[] passphrase, long lifetime_in_ms) throws MissingPrerequisitesException, IOException, GeneralSecurityException, Exception{
		GlobusProxy temp = new GlobusProxy(proxyFile);
		temp.init(passphrase, lifetime_in_ms);
		setDefaultProxy(temp);
		
	}
	
	public static GridProxy setDefaultProxy(GridProxy gridProxy) throws IOException {
		
//		if (defaultProxy != null) {
//			defaultProxy.destroy();
//		}
		defaultProxy = gridProxy;
		if ( defaultProxy.isValid() )
			defaultProxy.writeToDisk();
		for ( Object l : listeners ){
			defaultProxy.addStatusListener((GridProxyListener)l);
		}
		defaultProxy.checkStatus();
		defaultProxy.fireStatusChanged();
		return defaultProxy;
	}
	
	public static File getProxyFile() {
		return proxyFile;
	}	
	
	public static int getStatus() {
		if ( defaultProxy != null )
			return defaultProxy.getStatus();
		else return GridProxy.NOT_INITIALIZED;
	}
	
	public static void destroy(){
		if (getDefaultProxy() != null )
		getDefaultProxy().destroy();
		//defaultProxy = null;
	}
	
	public static void writeToFile() throws IOException{
		getDefaultProxy().writeToDisk();
	}
	
	public static void checkStatus(){
		//if ( defaultProxy == null ) return;
		getDefaultProxy().checkStatus();
	}
	
	public static boolean isValid(){
		if ( defaultProxy == null ) return false;
		else return defaultProxy.isValid();
	}
	
	  /** Register a listener for GridProxyEvents */
	  synchronized static public void addStatusListener(GridProxyListener l) {
	    if (listeners == null)
	      listeners = new Vector();
	    listeners.addElement(l);
	    if ( defaultProxy != null ) defaultProxy.addStatusListener(l);
	  }  

	  /** Remove a listener for GridProxyEvents */
	  synchronized static public void removeStatusListener(GridProxyListener l) {
	    if (listeners == null){
	      listeners = new Vector();
	    }
	    listeners.removeElement(l);
	    if ( defaultProxy != null ) defaultProxy.removeStatusListener(l);
	  }
}
