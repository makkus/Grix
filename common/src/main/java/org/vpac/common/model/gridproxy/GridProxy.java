package org.vpac.common.model.gridproxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.util.Util;
import org.vpac.common.exceptions.MissingPrerequisitesException;

/**
 * The abstract class GridProxy describes an object that includs a X509 proxy in some way or another.
 * It's also a wrapper for the cog-kit class globusCredential and provides methods to easily access 
 * fields of such a proxy.
 * 
 * @author Markus Binsteiner
 *
 */
abstract public class GridProxy {

	static final Logger myLogger = Logger.getLogger(GridProxy.class.getName());
	
	private Vector listeners;
	
	public static final int INITIALIZING = 4;

	public static final int MISSING_PREREQUISITES = 3;

	public static final int NOT_INITIALIZED = 2;

	public static final int EXPIRED = 1;

	public static final int INITIALIZED = 0;

	//private static GridProxy defaultProxy = null;

	protected GlobusCredential globusCredential = null;
	
	protected File proxyFile = null;

	protected int status = -1;
	
	public GridProxy(File proxyFile){
		this.proxyFile = proxyFile;
		checkStatus();		
	}
	
	public GridProxy(File proxyFile, GlobusCredential globusCred) throws IOException {
		this.proxyFile = proxyFile;
		this.globusCredential = globusCred;
		writeToDisk();
		checkStatus();
	}
	
	/**
	 * This one allows to check whether all prerequisites for creating a certain proxy are there (e.g. certificate and
	 * private key, permission to write in the desired folder...).
	 * 
	 * @throws MissingPrerequisitesException
	 */
	abstract protected void checkPrerequisites() throws MissingPrerequisitesException;
	//TODO throws Exception no good here?
	
	/**
	 * Every class that extends GridProxy has to implement the creation of the proxy by itself. For example:
	 * the VomsProxy has to contact a VOMS server before creation and then build the proxy.
	 * @param passphrase the passphrase of the private key
	 * @param lifetime_in_ms the lifetime of the proxy in milliseconds
	 * @return
	 * @throws Exception
	 */
	abstract protected GlobusCredential createProxy(char[] passphrase, long lifetime_in_ms) throws Exception;
	/**
	 * Info about this proxy that can be displayed to the user. Not really important if you don't need to display
	 * proxy information to the user in a nice way.
	 * @return Information about the proxy
	 */
	abstract protected ArrayList<String> proxyInfo();
	
	/**
	 * Displays standart information about the X509 part of the proxy like subject, issuer,...
	 * 
	 * @return formatted information about the proxy
	 */
	public String[] info(){
		
		ArrayList<String> info = new ArrayList<String>();
		
		info.add("subject\t: "+globusCredential.getSubject());
		info.add("issuer\t: "+globusCredential.getIssuer());
		info.add("identity\t :"+globusCredential.getIdentity());
		info.add("type\t: "+globusCredential.getProxyType());
		info.add("strength\t: "+globusCredential.getStrength()+" bits");
		info.add("path\t: "+proxyFile.toString());
		info.add("time left\t: "+this.getFormatedTime());
		
		info.addAll(proxyInfo());
		
		return info.toArray(new String[info.size()]);
	}
	
	/**
	 * Checks wether this proxy is (still) valid.
	 * @return true if valid, false if not
	 */
	public boolean isValid(){
		try {
			globusCredential.verify();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Same functionality as the commandline "grid-proxy-init" command. Creates
	 * a proxy on the local machine (e.g. with Linux under
	 * /tmp/x509up_u&lt;uid&gt;). If there is already a grid proxy on that machine,
	 * it will be destroyed before the new one is created.
	 * 
	 * @param passphrase
	 *            The passphrase for the private key
	 * @return the status of the GridProxy
	 * @throws MissingPrerequisitesException
	 *             if either the Userkey or the Usercert or both are not
	 *             found/readable.
	 * @throws IOException if the grid proxy could not be saved
	 * @throws GeneralSecurityException 
	 * @throws GlobusCredentialException 
	 */
	public int init(char[] passphrase, long lifetime_in_ms) throws MissingPrerequisitesException, IOException, GeneralSecurityException, Exception {
		//status = INITIALIZING;
		//fireStatusChanged();
		if ( proxyFile.exists() ) destroy();
		checkPrerequisites();
		try {
			globusCredential = createProxy(passphrase, lifetime_in_ms);
			Arrays.fill(passphrase, 'x');
			globusCredential.verify();
			//status = INITIALIZED;
			writeToDisk();
		} catch (Exception e) {
			myLogger.debug("Grid proxy not initialized.\nError: "
					+ e.getMessage());
			//throw new GeneralSecurityException("Wrong password.");
			throw e;
			//e.printStackTrace();
		} finally {
			checkStatus();
		}
		return status;
	}

	/**
	 * Checks the status of the grid-proxy currently on this machine
	 * 
	 * @return either INITIALIZED, EXPIRED, NOT_INITIALIZED
	 */
	public void checkStatus() {

		int old_status = status;
		String old_dn = null;
		String new_dn = null;
		if ( globusCredential != null ) {
			old_dn = globusCredential.getIdentity();
			myLogger.debug("old dn: "+old_dn);
		}
		try {
			globusCredential = new GlobusCredential(proxyFile.toString());
			globusCredential.verify();
			status = INITIALIZED;
			new_dn = globusCredential.getIdentity();
			if ( ! new_dn.equals(old_dn ) ) {
				fireStatusChanged();
				return;
			}
		} catch (GlobusCredentialException e) {
			if (e.getErrorCode() == GlobusCredentialException.EXPIRED)
				status = EXPIRED;
			else
				status = NOT_INITIALIZED;
			myLogger.debug("Grid proxy not initialized.");
			//globusCredential = null;
			if ( status != old_status ) fireStatusChanged();
		} 
		if ( status != old_status ) fireStatusChanged();
		
	}

	/**
	 * This one tries to write the proxy to the earlier specified file.
	 * 
	 * @throws IOException if the write process fails
	 */
	public void writeToDisk() throws IOException {

		OutputStream out = null;
		myLogger.debug("Save proxy file: " + proxyFile);
		try {
			out = new FileOutputStream(proxyFile);
			Util.setFilePermissions(proxyFile.toString(), 600);
			globusCredential.save(out);
		} catch (FileNotFoundException e) {
			throw new IOException(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					myLogger.error(e);
					//e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Destroys the proxy (deletes the proxy file).
	 * @return the status of the proxy (hopefully NOT_INITIALIZED, can be different because of file permission problems).
	 */
	public int destroy() {
		
		if ( proxyFile.exists() ){
		Util.destroy(proxyFile);
		this.globusCredential = null;
		checkStatus();
		//status = NOT_INITIALIZED;
		//fireStatusChanged();
		myLogger.debug("Grid-proxy destroyed");
		} else {
			this.globusCredential = null;
			checkStatus();
		}
		return status;
	}

	/**
	 * Just a getter for the proxy status.
	 * @return The status of the proxy.
	 */
	public int getStatus() {
		checkStatus();
		return status;
	}



	public static void main(String[] args) {

		try {
			LocalProxy.createPlainGlobusProxy(args[0].toCharArray(), 3600000);
		} catch (MissingPrerequisitesException e) {
			myLogger.error(e);
			//e.printStackTrace();
		} catch (IOException e) {
			myLogger.error(e);
			//e.printStackTrace();
		} catch (GeneralSecurityException e) {
			myLogger.error(e);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			myLogger.error(e);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}


	

	
	public long getTimeLeft(){
		
		long timeLeft = 0;
		
		try {
			timeLeft = globusCredential.getTimeLeft();
		} catch (NullPointerException npe){
			timeLeft = 0;
		}
		
		return timeLeft;
	}
	
	public String getFormatedTime(){
		String time = 	
			getHoursLeft() + "h, " +
			getMinutesLeft() + "min, " +
			getSecondsLeft() + "sec" ;
		return time;
	}
	public String getFormatedTimeWithoutSeconds(){
		String time = 	
			getHoursLeft() + "h, " +
			getMinutesLeft() + "m";
		return time;
	}	
	

	public long getHoursLeft(){
		
		return getTimeLeft()/(60*60);
	}
	
	public long getMinutesLeft(){
		return (getTimeLeft()-getHoursLeft()*60*60)/(60);
	}

	public long getSecondsLeft(){
		return (getTimeLeft()-getHoursLeft()*60*60-getMinutesLeft()*60);
	}

	/**
	 * Returns the wrapped GlobusCredential. I have to change that to GSSCredential someday.
	 * @return the GlobusCredential or null if not present
	 */
	public GlobusCredential getGlobusCredential() {
		//TODO maybe throw exception?
		if ( getStatus() != GridProxy.INITIALIZED ) return null;
		else return globusCredential;
	}

	/**
	 * This one returns the file where the proxy is stored or should be stored.
	 * @return the file where the proxy is stored
	 */
	public File getProxyFile() {
		return proxyFile;
	}
	
	
//	 --------------------------------------------------------------------
//		 here comes the listeners section, just copy&pasted from the StatusSource absract class
			
			public void fireStatusChanged(){
			    // if we have no listeners, do nothing...
			    if (listeners != null && !listeners.isEmpty()) {
			      // create the event object to send
			      GridProxyEvent event = 
			        new GridProxyEvent(this, status);

			      // make a copy of the listener list in case
			      //   anyone adds/removes listeners
			      Vector targets;
			      synchronized (this) {
			        targets = (Vector) listeners.clone();
			      }

			      // walk through the listener list and
			      //   call the gridproxychanged method in each
			      Enumeration e = targets.elements();
			      while (e.hasMoreElements()) {
			        GridProxyListener l = (GridProxyListener) e.nextElement();
			        l.gridProxyStatusChanged(event);
			      }
			    }
			}	
		
		  /** Register a listener for GridProxyEvents */
		  synchronized public void addStatusListener(GridProxyListener l) {
		    if (listeners == null)
		      listeners = new Vector();
		    listeners.addElement(l);
		  }  

		  /** Remove a listener for GridProxyEvents */
		  synchronized public void removeStatusListener(GridProxyListener l) {
		    if (listeners == null){
		      listeners = new Vector();
		    }
		    listeners.removeElement(l);
		  }
		  
		  



}
