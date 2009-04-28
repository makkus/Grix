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

package org.vpac.grix.view.swing.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ItemEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.vpac.grix.control.utils.UserProperty;
import org.vpac.grix.view.swing.Grix;

public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JCheckBox proxyAtStartupCheckBox = null;

	private JLabel startupTabLabel = null;

	private JRadioButton certificateRadioButton = null;

	private JRadioButton proxyRadioButton = null;

	private JRadioButton voRadioButton = null;

	private JButton saveButton = null;

	private JButton cancelButton = null;
	
	private String selectedStartupTab = null;
	
	ButtonGroup tabGroup = null;  //  @jve:decl-index=0:
	ButtonGroup statusPanelGroup = null;

	private JCheckBox showStatusPanelCheckBox = null;

	private JRadioButton topRadioButton = null;

	private JRadioButton bottomRadioButton = null;

	private JRadioButton localProxyRadioButton = null;

	private JCheckBox rememberProxySettingsCheckBox = null;

	private JCheckBox suggestProxyNamesCheckBox = null;

	/**
	 * @param owner
	 */
	public OptionsDialog(Frame owner) {
		super(owner);
		if ( UserProperty.getProperty("TAB_AT_STARTUP") != null ){
			selectedStartupTab = UserProperty.getProperty("TAB_AT_STARTUP");
		} else {
			selectedStartupTab = "LocalProxy";
		}
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(531, 513);
		Point p = new Point();
		p = this.getOwner().getLocation();
		this.setLocation(p.x+150, p.y+150);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.insets = new Insets(0, 20, 10, 0);
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.gridy = 10;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.insets = new Insets(15, 20, 10, 0);
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 9;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints12.gridy = 4;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridy = 8;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints21.gridy = 7;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints11.insets = new Insets(15, 20, 10, 0);
			gridBagConstraints11.weighty = 0.0;
			gridBagConstraints11.gridy = 6;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.SOUTHEAST;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(0, 0, 20, 15);
			gridBagConstraints6.gridy = 11;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.insets = new Insets(0, 0, 20, 15);
			gridBagConstraints5.anchor = GridBagConstraints.SOUTHEAST;
			gridBagConstraints5.gridy = 11;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints4.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints4.weighty = 0.0;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints3.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.gridy = 5;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.weighty = 0.0;
			gridBagConstraints1.weightx = 0.0;
			gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints1.insets = new Insets(0, 20, 10, 0);
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.gridy = 1;
			startupTabLabel = new JLabel();
			startupTabLabel.setText(Grix.getMessages().getString("Options.startup.tab"));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 0.0;
			gridBagConstraints.insets = new Insets(20, 20, 15, 0);
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setPreferredSize(new Dimension(400, 450));
			jPanel.add(getProxyAtStartupCheckBox(), gridBagConstraints);
			jPanel.add(startupTabLabel, gridBagConstraints1);
			jPanel.add(getCertificateRadioButton(), gridBagConstraints2);
			jPanel.add(getProxyRadioButton(), gridBagConstraints3);
			jPanel.add(getVoRadioButton(), gridBagConstraints4);
			jPanel.add(getSaveButton(), gridBagConstraints5);
			jPanel.add(getCancelButton(), gridBagConstraints6);
			jPanel.add(getShowStatusPanelCheckBox(), gridBagConstraints11);
			jPanel.add(getTopRadioButton(), gridBagConstraints21);
			jPanel.add(getBottomRadioButton(), gridBagConstraints31);
			jPanel.add(getLocalProxyRadioButton(), gridBagConstraints12);
			jPanel.add(getRememberProxySettingsCheckBox(), gridBagConstraints13);
			jPanel.add(getSuggestProxyNamesCheckBox(), gridBagConstraints22);
			tabGroup = new ButtonGroup();
			tabGroup.add(getCertificateRadioButton());
			tabGroup.add(getProxyRadioButton());
			tabGroup.add(getVoRadioButton());
			tabGroup.add(getLocalProxyRadioButton());
			statusPanelGroup = new ButtonGroup();
			statusPanelGroup.add(getTopRadioButton());
			statusPanelGroup.add(getBottomRadioButton());
			if ( "top".equals(UserProperty.getProperty("SHOW_GRID_PROXY_STATUS_PANEL")) ) {
				getShowStatusPanelCheckBox().setSelected(true);
				getTopRadioButton().setSelected(true);
			} else if ( "bottom".equals(UserProperty.getProperty("SHOW_GRID_PROXY_STATUS_PANEL")) ) {
				getShowStatusPanelCheckBox().setSelected(true);
				getBottomRadioButton().setSelected(true);
			} else {
				getShowStatusPanelCheckBox().setSelected(false);
			}
		}
		return jPanel;
	}

	/**
	 * This method initializes proxyAtStartupCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getProxyAtStartupCheckBox() {
		if (proxyAtStartupCheckBox == null) {
			proxyAtStartupCheckBox = new JCheckBox();
			if ( "yes".equals(UserProperty.getProperty("CREATE_PROXY_AT_STARTUP")) )
				proxyAtStartupCheckBox.setSelected(true);
			proxyAtStartupCheckBox.setText(Grix.getMessages().getString("Options.startup.checkLocalProxy"));
		}
		return proxyAtStartupCheckBox;
	}

	/**
	 * This method initializes certificateRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCertificateRadioButton() {
		if (certificateRadioButton == null) {
			certificateRadioButton = new JRadioButton();
			if ( "Certificate".equals(selectedStartupTab) ) 
				certificateRadioButton.setSelected(true);
			certificateRadioButton.setText("Certificate");
			certificateRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedStartupTab = "Certificate";
				}
			});
		}
		return certificateRadioButton;
	}

	/**
	 * This method initializes proxyRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getProxyRadioButton() {
		if (proxyRadioButton == null) {
			proxyRadioButton = new JRadioButton();
			proxyRadioButton.setText("MyProxy");
			if ( "Proxy".equals(selectedStartupTab) ) 
				proxyRadioButton.setSelected(true);
			proxyRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedStartupTab = "Proxy";
			}
		});
		}
		return proxyRadioButton;
	}

	/**
	 * This method initializes voRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getVoRadioButton() {
		if (voRadioButton == null) {
			voRadioButton = new JRadioButton();
			voRadioButton.setText("VO");
			if ( "VO".equals(selectedStartupTab) ) 
				voRadioButton.setSelected(true);
			voRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedStartupTab = "VO";
			}
		});
		}
		return voRadioButton;
	}
	
	/**
	 * This method initializes localProxyRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLocalProxyRadioButton() {
		if (localProxyRadioButton == null) {
			localProxyRadioButton = new JRadioButton();
			localProxyRadioButton.setText("LocalProxy");
			if ( "LocalProxy".equals(selectedStartupTab) ) 
				localProxyRadioButton.setSelected(true);
			localProxyRadioButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectedStartupTab = "LocalProxy";
			}
		});
		}
		return localProxyRadioButton;
	}

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// save the preferences
					if ( getProxyAtStartupCheckBox().isSelected() ) {
						UserProperty.setProperty("CREATE_PROXY_AT_STARTUP", "yes");
					} else {
						UserProperty.setProperty("CREATE_PROXY_AT_STARTUP", "no");
					}
					UserProperty.setProperty("TAB_AT_STARTUP", selectedStartupTab);
					
					if ( getShowStatusPanelCheckBox().isSelected() ) {
						if ( getBottomRadioButton().isSelected() ) 
							UserProperty.setProperty("SHOW_GRID_PROXY_STATUS_PANEL", "bottom");
						else
							UserProperty.setProperty("SHOW_GRID_PROXY_STATUS_PANEL", "top");
					} else {
						UserProperty.setProperty("SHOW_GRID_PROXY_STATUS_PANEL", "no");
					}
					
					if ( getRememberProxySettingsCheckBox().isSelected() ) 
						UserProperty.setProperty("REMEMBER_MYPROXY_SETTINGS", "yes");
					else
						UserProperty.setProperty("REMEMBER_MYPROXY_SETTINGS", "no");
					
					if ( getSuggestProxyNamesCheckBox().isSelected() )
						UserProperty.setProperty("AUTOSUGGEST_PROXYNAMES", "yes");
					else UserProperty.setProperty("AUTOSUGGEST_PROXYNAMES", "no");
					
					OptionsDialog.this.getOwner().repaint();
					OptionsDialog.this.dispose();
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					OptionsDialog.this.dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes showStatusPanelCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getShowStatusPanelCheckBox() {
		if (showStatusPanelCheckBox == null) {
			showStatusPanelCheckBox = new JCheckBox();
			showStatusPanelCheckBox.setText(Grix.getMessages().getString("Options.showGridProxyStatusPanel"));
				showStatusPanelCheckBox.setSelected(true);
				showStatusPanelCheckBox.addItemListener(new java.awt.event.ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if ( e.getStateChange() == ItemEvent.SELECTED  ) {
							getTopRadioButton().setEnabled(true);
							getBottomRadioButton().setEnabled(true);
						} else {
							getTopRadioButton().setEnabled(false);
							getBottomRadioButton().setEnabled(false);
						}
					}
				});
			}
		return showStatusPanelCheckBox;
	}

	/**
	 * This method initializes topRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getTopRadioButton() {
		if (topRadioButton == null) {
			topRadioButton = new JRadioButton();
			topRadioButton.setText("...on the top");
			topRadioButton.setSelected(true);
		}
		return topRadioButton;
	}

	/**
	 * This method initializes bottomRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getBottomRadioButton() {
		if (bottomRadioButton == null) {
			bottomRadioButton = new JRadioButton();
			bottomRadioButton.setText("...on the bottom");
		}
		return bottomRadioButton;
	}

	/**
	 * This method initializes rememberProxySettingsCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getRememberProxySettingsCheckBox() {
		if (rememberProxySettingsCheckBox == null) {
			rememberProxySettingsCheckBox = new JCheckBox();
			rememberProxySettingsCheckBox.setText("Remember MyProxy settings");
			if ( "yes".equals(UserProperty.getProperty("REMEMBER_MYPROXY_SETTINGS")) )
				rememberProxySettingsCheckBox.setSelected(true);
		}
		return rememberProxySettingsCheckBox;
	}

	/**
	 * This method initializes suggestProxyNamesCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSuggestProxyNamesCheckBox() {
		if (suggestProxyNamesCheckBox == null) {
			suggestProxyNamesCheckBox = new JCheckBox();
			suggestProxyNamesCheckBox.setText("Auto-suggest proxy names");
			if ( "yes".equals(UserProperty.getProperty("AUTOSUGGEST_PROXYNAMES")) )
				suggestProxyNamesCheckBox.setSelected(true);
		}
		return suggestProxyNamesCheckBox;
	}



}  //  @jve:decl-index=0:visual-constraint="10,10"
