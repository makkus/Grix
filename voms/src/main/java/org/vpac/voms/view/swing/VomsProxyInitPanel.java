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

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.vpac.common.model.StatusEvent;
import org.vpac.common.model.StatusListener;
import org.vpac.common.model.StatusSourceInterface;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.model.VO;
import org.vpac.voms.model.proxy.VomsProxy;

public class VomsProxyInitPanel extends JPanel implements StatusListener {

	private static final long serialVersionUID = 1L;
	
	private StatusSourceInterface ssi = null;

	private JComboBox jComboBox = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JPasswordField jPasswordField = null;

	private JButton initButton = null;

	private JLabel jLabel2 = null;

	private JButton destroyButton = null;

	/**
	 * This is the default constructor
	 */
	public VomsProxyInitPanel(StatusSourceInterface ssi) {
		super();
		this.ssi = ssi;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.anchor = GridBagConstraints.EAST;
		gridBagConstraints11.insets = new Insets(0, 0, 0, 20);
		gridBagConstraints11.gridy = 3;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridwidth = 2;
		gridBagConstraints5.insets = new Insets(20, 20, 0, 0);
		gridBagConstraints5.gridy = 0;
		jLabel2 = new JLabel();
		jLabel2.setText("Create a voms proxy");
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 2;
		gridBagConstraints4.insets = new Insets(20, 0, 20, 20);
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.insets = new Insets(15, 0, 0, 20);
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(15, 20, 0, 20);
		gridBagConstraints2.gridy = 2;
		jLabel1 = new JLabel();
		jLabel1.setText("grid passphrase");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(20, 20, 0, 20);
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		gridBagConstraints1.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText("VO");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(20, 0, 0, 20);
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridx = 1;
		this.setSize(501, 185);
		this.setLayout(new GridBagLayout());
		this.add(getJComboBox(), gridBagConstraints);
		this.add(jLabel, gridBagConstraints1);
		this.add(jLabel1, gridBagConstraints2);
		this.add(getJPasswordField(), gridBagConstraints3);
		this.add(getInitButton(), gridBagConstraints4);
		this.add(jLabel2, gridBagConstraints5);
		this.add(getDestroyButton(), gridBagConstraints11);
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox(LocalVomses.getLocalVomses().getVOs());
		}
		return jComboBox;
	}

	/**
	 * This method initializes jPasswordField
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getJPasswordField() {
		if (jPasswordField == null) {
			jPasswordField = new JPasswordField();
		}
		return jPasswordField;
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

					new Thread() {

						public void run() {

							File proxyFile = LocalProxy.getProxyFile();
							VomsProxy proxy = new VomsProxy(proxyFile,
									(VO) jComboBox.getSelectedItem(), "A", null);

							Cursor hourglassCursor = new Cursor(
									Cursor.WAIT_CURSOR);
							setCursor(hourglassCursor);

							try {
								proxy.init(jPasswordField.getPassword(), 43200000);
								proxy = new VomsProxy(proxyFile);
								ssi.fireStatusChanged(proxy.getStatus(), proxy.info());
								JOptionPane.showMessageDialog( VomsProxyInitPanel.this,
										"<html><p>Voms proxy successfully created."+
												"</p><p>It is stored as: "+proxyFile.toString()+"</p></html>", "Voms proxy created",
										JOptionPane.INFORMATION_MESSAGE );
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
								JOptionPane.showMessageDialog( VomsProxyInitPanel.this,
										"<html><p>Error when trying to create voms proxy:"+
												"</p><p>"+e.getMessage()+"</html>", "Could not create voms proxy.",
										JOptionPane.ERROR_MESSAGE );
							} finally {
								getJPasswordField().setText("");
								Cursor normalCursor = new Cursor(
										Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
							}
						}
					}.start();

				}
			});
		}
		return initButton;
	}

	/**
	 * This method initializes destroyButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDestroyButton() {
		if (destroyButton == null) {
			destroyButton = new JButton();
			destroyButton.setText("Voms proxy destroy");
			destroyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LocalProxy.destroy();
					ssi.fireStatusChanged(GridProxy.NOT_INITIALIZED, new String[]{"There is no local proxy on this machine."});
				}
			});
		}
		return destroyButton;
	}

	public void statusChanged(StatusEvent event) {
		
		if ( event.getStatus() == GridProxy.INITIALIZED ){
			getDestroyButton().setEnabled(true);
		} else {
			getDestroyButton().setEnabled(false);
		}
		
	}


} // @jve:decl-index=0:visual-constraint="10,10"
