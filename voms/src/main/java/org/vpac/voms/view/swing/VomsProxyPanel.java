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

package org.vpac.voms.view.swing;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.vpac.common.model.StatusEvent;
import org.vpac.common.model.StatusListener;
import org.vpac.common.model.StatusSourceInterface;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.model.proxy.NoVomsProxyException;
import org.vpac.voms.model.proxy.VomsProxy;

public class VomsProxyPanel implements StatusSourceInterface {

	private JFrame jFrame = null; // @jve:decl-index=0:visual-constraint="10,10"

	private JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;

	private JMenu helpMenu = null;

	private JMenuItem aboutMenuItem = null;

	private JDialog aboutDialog = null; // @jve:decl-index=0:visual-constraint="496,100"

	private JPanel aboutContentPane = null;

	private JLabel aboutVersionLabel = null;

	private VomsProxyInitPanel proxyInitPanel = null;

	private VomsProxyInfoPanel proxyInfoPanel = null;

	private VomsProxy vomsProxy = null;

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(487, 609);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Visual voms-proxy-init");
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 * @throws NoVomsProxyException
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getProxyInitPanel(), BorderLayout.NORTH);
			jContentPane.add(getProxyInfoPanel(), BorderLayout.CENTER);
			addStatusListener(getProxyInfoPanel());
			addStatusListener(getProxyInitPanel());
			try {
				vomsProxy = new VomsProxy(LocalProxy.getProxyFile());
				fireStatusChanged(vomsProxy.getStatus(), vomsProxy.info());
			} catch (NoVomsProxyException e) {
				fireStatusChanged(
						GridProxy.NOT_INITIALIZED,
						new String[] { "There is no proxy file on this machine." });
			}

		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes aboutDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new JDialog(getJFrame(), true);
			aboutDialog.setTitle("About");
			aboutDialog.setSize(new Dimension(162, 174));
			aboutDialog.setContentPane(getAboutContentPane());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane() {
		if (aboutContentPane == null) {
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutVersionLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getAboutVersionLabel() {
		if (aboutVersionLabel == null) {
			aboutVersionLabel = new JLabel();
			aboutVersionLabel.setText("Version 1.0");
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	/**
	 * This method initializes proxyInitPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private VomsProxyInitPanel getProxyInitPanel() {
		if (proxyInitPanel == null) {
			proxyInitPanel = new VomsProxyInitPanel(this);
		}
		return proxyInitPanel;
	}

	/**
	 * This method initializes proxyInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private VomsProxyInfoPanel getProxyInfoPanel() {
		if (proxyInfoPanel == null) {
			proxyInfoPanel = new VomsProxyInfoPanel();
		}
		return proxyInfoPanel;
	}

	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				VomsProxyPanel application = new VomsProxyPanel();
				application.getJFrame().setVisible(true);
			}
		});
	}

	// ----------------------------------------------------------------------
	// from here on: cut&paste StatusSource

	protected Vector<StatusListener> listeners;
	protected int status;

	public void fireStatusChanged(int status, String[] message) {
		// if we have no listeners, do nothing...
		if (listeners != null && !listeners.isEmpty()) {
			// create the event object to send
			StatusEvent event = new StatusEvent(this, status, message);

			// make a copy of the listener list in case
			// anyone adds/removes listeners
			Vector targets;
			synchronized (this) {
				targets = (Vector) listeners.clone();
			}

			// walk through the listener list and
			// call the gridproxychanged method in each
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
		if (listeners == null) {
			listeners = new Vector();
		}
		listeners.removeElement(l);
	}

}
