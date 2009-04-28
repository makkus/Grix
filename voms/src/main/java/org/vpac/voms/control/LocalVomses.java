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

package org.vpac.voms.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.vpac.common.control.Helpers;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.GridProxyEvent;
import org.vpac.common.model.gridproxy.GridProxyListener;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.model.VO;

/**
 * Manages all VOMS servers that are of interest for the user. Information about
 * these servers can be found in the $HOME/.glite/vomses directory. Every file
 * which contains a line like<br>
 * <br>
 * "Chris" "vomrsdev.vpac.org" "15003"
 * "/C=AU/O=APACGrid/OU=VPAC/CN=vomrsdev.vpac.org" "Chris" <br>
 * <br>
 * describes one VO: "Chris" is the name of the VO, followed by the hostname of
 * the VOMS server and the port the VO is mapped to. After that the DN of the
 * host of the VOMS server and I don't know what the last "Chris" means.
 * 
 * <br>
 * <br>
 * At the moment the endpoint of a VOMRS server is constructed with this
 * information as well which is very dodgy and I hope to change that in the
 * future with a better model.
 * 
 * @author Markus Binsteiner
 * 
 */
public class LocalVomses implements GridProxyListener {
	
	static final Logger myLogger = Logger.getLogger(LocalVomses.class.getName());

	private static LocalVomses localVomses = null;

	private Vector<Voms> userVomses = null;

	private boolean stop_thread = false;

	private Thread contactLocalVomses = null;

	/**
	 * List of all VO's that have vomses files in $HOME/.glite/vomses
	 * 
	 * @return all available VO's
	 */
	public Vector<VO> getVOs() {

		Vector<VO> vos = new Vector<VO>();
		File[] files = Voms.VOMSES.listFiles();
		
		if ( files != null ){

		for (File file : files) {
			BufferedReader f = null;
			try {
				f = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				continue;
			}
			String line = null;
			try {
				while ((line = f.readLine()) != null) {
					VO new_vo = Voms.parseVomsesLine(line);
					if (new_vo == null)
						continue;
					vos.add(new_vo);
				}
			} catch (IOException e) {
				continue;
			}

		}
		}

		return vos;
	}

	/**
	 * Returns the VO with the specified name.
	 * 
	 * @param voms
	 *            the name of the VO
	 * @return the VO
	 */
	public VO getVO(String voms) {

		for (VO vo : getVOs()) {
			if (voms.equals(vo.getVoName()))
				return vo;
		}
		return null;
	}

	/**
	 * 
	 * @return all VO's the current user is a member in
	 */
	public Vector<Voms> getUserSubscribedVomses() {

		Vector<Voms> userSubscribedVomses = new Vector<Voms>();
		for (Voms voms : this.getVomses()) {
			if (voms.getStatus() == Voms.MEMBER)
				userSubscribedVomses.add(voms);
		}
		return userSubscribedVomses;
	}

	/**
	 * @return all Vomses
	 */
	public synchronized Vector<Voms> getVomses() {

		if (this.userVomses == null) {
			if (contactLocalVomses == null || !contactLocalVomses.isAlive()) {
				this.contactLocalVomses = new Thread(new Runnable() {
					public void run() {
						LocalVomses.this.userVomses = new Vector<Voms>();
						for (VO vo : getVOs()) {
							Voms voms = null;

							voms = new Voms(vo);
							LocalVomses.this.userVomses.add(voms);
							try {
								fireStatusChanged(voms,
										VomsStatusEvent.NEW_VOMS,
										new String[] { "Contacted Voms: "
												+ voms.toString() });
							} catch (RuntimeException ae) {
								// does not matter
								//ae.printStackTrace();
								myLogger.error(ae);
							}
							if (stop_thread) {
								stop_thread = false;
								for (Voms voms_rem : LocalVomses.this.userVomses) {
									fireStatusChanged(voms_rem,
											VomsStatusEvent.REMOVED_VOMS,
											new String[] { "Removed Voms: "
													+ voms.toString() });
								}
								LocalVomses.this.userVomses = null;
								// localVomses = null;
								return;
							}
						}
					}
				});
				this.contactLocalVomses.start();

			} else if (contactLocalVomses.isAlive()) {
				try {
					contactLocalVomses.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (contactLocalVomses.isAlive()) {
			try {
				contactLocalVomses.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.userVomses;
	}

	/**
	 * The static object for all local vomses.
	 * 
	 * @return all vomses
	 */
	public static synchronized LocalVomses getLocalVomses() {
		if (localVomses == null) {
			localVomses = new LocalVomses();
		}
		return localVomses;
	}

	public static void refreshLocalVomses() {
		if ( LocalProxy.isValid() ) {
			if ( getLocalVomses().userVomses != null ) {

			if (getLocalVomses().contactLocalVomses.isAlive()) {
				try {
					getLocalVomses().contactLocalVomses.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					myLogger.error(e);
				}
			}
			for (Voms voms : getLocalVomses().userVomses) {
				fireStatusChanged(voms, VomsStatusEvent.REMOVED_VOMS,
						new String[] { "Removed Voms: " + voms.toString() });
			}
			getLocalVomses().userVomses = null;
			}

			getLocalVomses().getVomses();
		}
	}

	/**
	 * This method updates the membership information of the current user within
	 * this VO and fires an event if something has changed.
	 * 
	 * @param voms
	 *            the voms to check
	 */
	public static void updateVoms(Voms voms) {
		// maybe this has to go into the Voms class.
		int old_status = voms.getStatus();
		String[] info = (String[]) voms.getInfoQuery().getResult();
		voms.updateVoms();
		if ((old_status != Voms.NO_CONNECTION_TO_VOMRS || old_status != Voms.NO_MEMBER)
				&& (voms.getStatus() == Voms.NO_MEMBER))
			fireStatusChanged(voms, VomsStatusEvent.REMOVED_VOMS_MEMBERSHIP,
					new String[] { "Removed Voms: " + voms.toString()
							+ " changed to:" + voms.getStatus() });
		else if (old_status != voms.getStatus())
			fireStatusChanged(voms, VomsStatusEvent.STATUS_CHANGED,
					new String[] { "Status of Voms: " + voms.toString()
							+ " changed to:" + voms.getStatus() });
		else if (voms.getStatus() == Voms.MEMBER
				&& old_status == voms.getStatus()) {
			// test whether info changed
			String[] newInfo = (String[]) voms.getInfoQuery().getResult();
			if (info.length != newInfo.length) {
				fireStatusChanged(voms, VomsStatusEvent.INFO_CHANGED,
						new String[] { "Info of Voms: " + voms.toString()
								+ " changed." });
			}

			boolean changed = false;
			for (int i = 0; i < info.length; i++) {
				if (info[i].equals(newInfo[i]))
					continue;
				else {
					changed = true;
					break;
				}
			}
			if (changed)
				fireStatusChanged(voms, VomsStatusEvent.INFO_CHANGED,
						new String[] { "Info of Voms: " + voms.toString()
								+ " changed." });
		}

	}

	// ------------- Status source
	// ---------------------------------------------------
	private static Vector<VomsesStatusListener> vomsesListeners;

	private static void fireStatusChanged(Voms voms, int action, String[] message) {
		// if we have no vomsesListeners, do nothing...
		if (vomsesListeners != null && !vomsesListeners.isEmpty()) {
			// create the event object to send
			VomsStatusEvent event = new VomsStatusEvent(voms, action, message);

			// make a copy of the listener list in case
			// anyone adds/removes vomsesListeners
			Vector targets;
			synchronized (voms) {
				targets = (Vector) vomsesListeners.clone();
			}

			// walk through the listener list and
			// call the gridproxychanged method in each
			Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				VomsesStatusListener l = (VomsesStatusListener) e.nextElement();
				l.vomsStatusChanged(event);
			}
		}
	}

	/** Register a listener for GridProxyEvents */
	synchronized public static void addStatusListener(VomsesStatusListener l) {
		if (vomsesListeners == null)
			vomsesListeners = new Vector();
		vomsesListeners.addElement(l);
	}

	/** Remove a listener for GridProxyEvents */
	synchronized public static void removeStatusListener(VomsesStatusListener l) {
		if (vomsesListeners == null) {
			vomsesListeners = new Vector();
		}
		vomsesListeners.removeElement(l);
	}

	public void gridProxyStatusChanged(GridProxyEvent e) {

		if (e.getStatus() == GridProxy.INITIALIZED) {
			refreshLocalVomses();
		} else {
			if (contactLocalVomses.isAlive()) {
				stop_thread = true;
			} else {
				for (Voms voms : userVomses) {
					fireStatusChanged(voms, VomsStatusEvent.REMOVED_VOMS,
							new String[] { "Removed Voms: " + voms.toString() });
				}
				userVomses = null;
			}
		}

	}

}
