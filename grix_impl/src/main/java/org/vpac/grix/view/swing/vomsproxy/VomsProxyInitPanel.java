/* Copyright 2006 VPAC
 * 
 * This file is part of grix.
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

package org.vpac.grix.view.swing.vomsproxy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.qc.model.clients.GenericClient;
import org.vpac.voms.model.VO;
import org.vpac.voms.model.proxy.VomsProxy;
import org.vpac.voms.model.proxy.VomsProxyEvent;
import org.vpac.voms.model.proxy.VomsProxyListener;

public class VomsProxyInitPanel extends JPanel {
	
	static final Logger myLogger = Logger.getLogger(VomsProxyInitPanel.class.getName());  //  @jve:decl-index=0:

	private static final long serialVersionUID = 1L;
	
	private Vector listeners;  //  @jve:decl-index=0:
	
	private String[] groupRoles = null;

	private JComboBox groupRolesComboBox = null;

	private JComboBox timeComboBox = null;

	private JButton initButton = null;
	
	private GenericClient client = null;
	
	private VO vo = null;

	/**
	 * This is the default constructor
	 */
	public VomsProxyInitPanel(VO vo, GenericClient client) {
		super();
		this.vo = vo;
		this.client = client;
		this.groupRoles = client.getMyContexts();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints initButtonConstraints = new GridBagConstraints();
		initButtonConstraints.gridx = 2;
		initButtonConstraints.anchor = GridBagConstraints.EAST;
		initButtonConstraints.insets = new Insets(15, 20, 15, 20);
		initButtonConstraints.gridy = 0;
		GridBagConstraints timeComboBoxConstraints = new GridBagConstraints();
		timeComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeComboBoxConstraints.gridy = 0;
		timeComboBoxConstraints.insets = new Insets(15, 20, 15, 20);
		timeComboBoxConstraints.gridx = 1;
		GridBagConstraints groupRolesComboBoxConstraints = new GridBagConstraints();
		groupRolesComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
		groupRolesComboBoxConstraints.gridy = 0;
		groupRolesComboBoxConstraints.weightx = 1.0;
		groupRolesComboBoxConstraints.insets = new Insets(15, 20, 15, 20);
		groupRolesComboBoxConstraints.gridx = 0;
		this.setBorder(BorderFactory.createTitledBorder(null, "Voms proxy init", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		this.setBackground(new Color(245, 245, 245));
		this.setSize(490, 409);
		this.setLayout(new GridBagLayout());
		this.add(getGroupRolesComboBox(), groupRolesComboBoxConstraints);
		this.add(getTimeComboBox(), timeComboBoxConstraints);
		this.add(getInitButton(), initButtonConstraints);
	}

	/**
	 * This method initializes groupRolesComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getGroupRolesComboBox() {
		if (groupRolesComboBox == null) {
			groupRolesComboBox = new JComboBox(groupRoles);
		}
		return groupRolesComboBox;
	}

	/**
	 * This method initializes timeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTimeComboBox() {
		if (timeComboBox == null) {
			timeComboBox = new JComboBox(new Integer[]{1,2,3,4,5,6,7});
			timeComboBox.setPreferredSize(new Dimension(69, 24));
		}
		return timeComboBox;
	}

	/**
	 * This method initializes initButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getInitButton() {
		if (initButton == null) {
			initButton = new JButton();
			initButton.setText("Voms proxy init");
			initButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//TODO test file permissions
					File proxyFile = new File(VomsProxy.DEFAULT_DIR, "VO_"+vo.getVoName()+((String)getGroupRolesComboBox().getSelectedItem()).replaceAll("/", "_"));
					VomsProxy proxy = new VomsProxy( proxyFile, vo, "B"+getGroupRolesComboBox().getSelectedItem()+":"+VomsProxy.DEFAULT_ROLE, null);
					try {
						proxy.init("xxx".toCharArray(), (Integer)getTimeComboBox().getSelectedItem()*86400000);
					} catch (MissingPrerequisitesException e1) {
						// TODO Auto-generated catch block
						myLogger.error(e1);
						//e1.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						//e2.printStackTrace();
						myLogger.error(e2);
					} catch (GeneralSecurityException e3) {
						// TODO Auto-generated catch block
						//e3.printStackTrace();
						myLogger.error(e3);
					} catch (Exception e4) {
						// TODO Auto-generated catch block
						//e4.printStackTrace();
						myLogger.error(e4);
					} 
					fireNewVomsProxy(VomsProxyEvent.NEW_PROXY, proxy);
					
				}
			});
		}
		return initButton;
	}
	
	public void fireNewVomsProxy(String action, VomsProxy proxy){
	    // if we have no listeners, do nothing...
	    if (listeners != null && !listeners.isEmpty()) {
	      // create the event object to send
	      VomsProxyEvent event = new VomsProxyEvent(this, action, proxy);

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
	        VomsProxyListener l = (VomsProxyListener) e.nextElement();
	        l.vomsProxiesChanged(event);
	      }
	    }
	}
	

	  synchronized public void addStatusListener(VomsProxyListener l) {
	    if (listeners == null)
	      listeners = new Vector<VomsProxyListener>();
	    listeners.addElement(l);
	  }  


	  synchronized public void removeStatusListener(VomsProxyListener l) {
	    if (listeners == null){
	      listeners = new Vector();
	    }
	    listeners.removeElement(l);
	  }

}  //  @jve:decl-index=0:visual-constraint="10,10"
