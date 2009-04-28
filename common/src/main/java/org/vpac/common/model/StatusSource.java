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

package org.vpac.common.model;

import java.util.Enumeration;
import java.util.Vector;

/**
 * The status source is the place where events occur to an object.
 * 
 * @author Markus Binsteiner
 *
 */
public abstract class StatusSource implements StatusSourceInterface{
	
	protected Vector<StatusListener> listeners;
	
	protected int status;
	
	public void fireStatusChanged(int status, String[] message){
	    // if we have no listeners, do nothing...
	    if (listeners != null && !listeners.isEmpty()) {
	      // create the event object to send
	      StatusEvent event = 
	        new StatusEvent(this, status, message);

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
	        StatusListener l = (StatusListener) e.nextElement();
	        l.statusChanged(event);
	      }
	    }
	}
	
	  /** Register a listener for GridProxyEvents */
	  synchronized public void addStatusListener(StatusListener l) {
	    if (listeners == null)
	      listeners = new Vector();
	    listeners.addElement(l);
	  }  

	  /** Remove a listener for GridProxyEvents */
	  synchronized public void removeStatusListener(StatusListener l) {
	    if (listeners == null){
	      listeners = new Vector();
	    }
	    listeners.removeElement(l);
	  }
	
}
