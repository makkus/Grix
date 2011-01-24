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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.vpac.common.view.swing.ElementsPanel;
import org.vpac.common.view.swing.OneElementPanel;
import org.vpac.voms.control.Voms_Utils;
import org.vpac.voms.model.proxy.VomsProxy;
import org.vpac.voms.model.proxy.VomsProxyCredential;

public class OneProxyPanel extends JPanel implements OneElementPanel {

	private static final long serialVersionUID = 1L;

	static final Logger myLogger = Logger.getLogger(OneProxyPanel.class
			.getName());

	private JLabel voLabel = null;

	private VomsProxy proxy = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JTextField groupTextField = null;
	private JTextField roleTextField = null;
	private JPanel myProxyPanel = null; // @jve:decl-index=0:visual-constraint="370,341"
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JTextField myproxyUsernameTextField = null;
	private JButton jButton = null;
	private JPanel timePanel = null;
	private JPanel infoPanel = null;
	private JTextField expiresTextField = null;
	private JButton jButton1 = null;

	private String default_fqan = null;
	private JLabel jLabel4 = null;
	private JButton jButton2 = null;
	private JLabel jLabel5 = null;
	private JTextField jTextField = null;

	private ElementsPanel elementsPanel = null;

	/**
	 * This is the default constructor
	 */
	public OneProxyPanel(ElementsPanel elementsPanel, VomsProxy proxy) {
		super();
		this.elementsPanel = elementsPanel;
		this.proxy = proxy;
		default_fqan = Voms_Utils.getDefaultFQAN(Voms_Utils
				.getFQANs(VomsProxyCredential.extractVOMSACs(
						proxy.getGlobusCredential()).get(0)));
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
		gridBagConstraints81.fill = GridBagConstraints.BOTH;
		gridBagConstraints81.weighty = 1.0;
		gridBagConstraints81.gridx = 0;
		gridBagConstraints81.gridy = 0;
		gridBagConstraints81.insets = new Insets(15, 15, 15, 15);
		gridBagConstraints81.weightx = 1.0;
		GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
		gridBagConstraints71.gridx = 0;
		gridBagConstraints71.fill = GridBagConstraints.BOTH;
		gridBagConstraints71.weightx = 1.0;
		gridBagConstraints71.weighty = 1.0;
		gridBagConstraints71.gridwidth = 2;
		gridBagConstraints71.insets = new Insets(0, 15, 15, 15);
		gridBagConstraints71.gridy = 3;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 1;
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.weightx = 0.0;
		gridBagConstraints6.weighty = 0.0;
		gridBagConstraints6.insets = new Insets(15, 15, 15, 15);
		gridBagConstraints6.gridheight = 3;
		gridBagConstraints6.gridy = 0;
		jLabel1 = new JLabel();
		jLabel1.setText("Group:");
		jLabel1.setHorizontalTextPosition(SwingConstants.RIGHT);
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setPreferredSize(new Dimension(50, 15));
		jLabel = new JLabel();
		jLabel.setText("Role:");
		voLabel = new JLabel();
		voLabel.setText("Information in the voms proxy:");
		this.setSize(522, 201);
		this.setLayout(new GridBagLayout());
		// this.add(getMyProxyPanel(), gridBagConstraints6);
		this.setBackground(Color.white);
		// this.add(getTimePanel(), gridBagConstraints71);
		this.add(getInfoPanel(), gridBagConstraints81);
	}

	/**
	 * This method initializes groupTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGroupTextField() {
		if (groupTextField == null) {
			groupTextField = new JTextField();
			try {
				groupTextField.setText(Voms_Utils.getGroup(default_fqan));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				myLogger.error(e);
			}
		}
		return groupTextField;
	}

	/**
	 * This method initializes roleTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRoleTextField() {
		if (roleTextField == null) {
			roleTextField = new JTextField();
			roleTextField.setText(Voms_Utils.getRole(default_fqan));
		}
		return roleTextField;
	}

	/**
	 * This method initializes myProxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMyProxyPanel() {
		if (myProxyPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints10.weighty = 0.0;
			gridBagConstraints10.insets = new Insets(0, 20, 0, 10);
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new Insets(0, 20, 10, 10);
			gridBagConstraints9.anchor = GridBagConstraints.NORTH;
			gridBagConstraints9.weighty = 0.0;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints8.insets = new Insets(0, 10, 10, 0);
			gridBagConstraints8.weighty = 0.0;
			gridBagConstraints8.gridy = 1;
			jLabel3 = new JLabel();
			jLabel3.setText("Username:");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.insets = new Insets(15, 10, 15, 0);
			gridBagConstraints7.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints7.weighty = 0.0;
			gridBagConstraints7.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("MyProxy");
			myProxyPanel = new JPanel();
			myProxyPanel.setLayout(new GridBagLayout());
			myProxyPanel.setSize(new Dimension(270, 166));
			myProxyPanel.add(jLabel2, gridBagConstraints7);
			myProxyPanel.add(jLabel3, gridBagConstraints8);
			myProxyPanel
					.add(getMyproxyUsernameTextField(), gridBagConstraints9);
			myProxyPanel.add(getJButton(), gridBagConstraints10);
		}
		return myProxyPanel;
	}

	/**
	 * This method initializes myproxyUsernameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getMyproxyUsernameTextField() {
		if (myproxyUsernameTextField == null) {
			myproxyUsernameTextField = new JTextField();
			myproxyUsernameTextField.setPreferredSize(new Dimension(100, 19));
		}
		return myproxyUsernameTextField;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Upload");
			jButton.setPreferredSize(new Dimension(78, 19));
		}
		return jButton;
	}

	/**
	 * This method initializes timePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTimePanel() {
		if (timePanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.insets = new Insets(0, 20, 0, 20);
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new Insets(0, 20, 0, 0);
			gridBagConstraints12.gridy = 0;
			jLabel5 = new JLabel();
			jLabel5.setText("MyProxy status:");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.weightx = 0.0;
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 20);
			gridBagConstraints4.gridy = 0;
			timePanel = new JPanel();
			timePanel.setLayout(new GridBagLayout());
			timePanel.add(getJButton2(), gridBagConstraints4);
			timePanel.add(jLabel5, gridBagConstraints12);
			timePanel.add(getJTextField(), gridBagConstraints13);
		}
		return timePanel;
	}

	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.anchor = GridBagConstraints.EAST;
			gridBagConstraints31.gridx = 3;
			gridBagConstraints31.gridy = 0;
			gridBagConstraints31.insets = new Insets(0, 0, 0, 20);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 2;
			gridBagConstraints21.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints21.gridy = 2;
			jLabel4 = new JLabel();
			jLabel4.setText("Time left:");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridx = 3;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(0, 20, 10, 20);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.CENTER;
			gridBagConstraints5.insets = new Insets(0, 20, 10, 20);
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.NORTH;
			gridBagConstraints3.insets = new Insets(0, 20, 15, 20);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridwidth = 3;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.insets = new Insets(0, 20, 15, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.insets = new Insets(0, 20, 10, 0);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.insets = new Insets(15, 20, 15, 0);

			infoPanel = new JPanel();
			infoPanel.setLayout(new GridBagLayout());
			infoPanel.setBackground(Color.white);
			infoPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			infoPanel.add(voLabel, gridBagConstraints);
			infoPanel.add(jLabel, gridBagConstraints1);
			infoPanel.add(jLabel1, gridBagConstraints2);
			infoPanel.add(getGroupTextField(), gridBagConstraints3);
			infoPanel.add(getRoleTextField(), gridBagConstraints5);
			infoPanel.add(getExpiresTextField(), gridBagConstraints11);
			infoPanel.add(jLabel4, gridBagConstraints21);
			infoPanel.add(getJButton1(), gridBagConstraints31);
		}
		return infoPanel;
	}

	/**
	 * This method initializes expiresTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExpiresTextField() {
		if (expiresTextField == null) {
			expiresTextField = new JTextField();
			expiresTextField.setText(proxy.getFormatedTimeWithoutSeconds());
		}
		return expiresTextField;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Destroy");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					OneProxyPanel.this.getElementsPanel()
							.removeOneElementPanel(OneProxyPanel.this);
					proxy.destroy();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("Upload");
		}
		return jButton2;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}

	public ElementsPanel getElementsPanel() {
		return this.elementsPanel;
	}

} // @jve:decl-index=0:visual-constraint="10,81"
