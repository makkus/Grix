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

package org.vpac.grix.view.swing.proxy;

import gridpp.portal.voms.VOMSAttributeCertificate;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.GridProxyEvent;
import org.vpac.common.model.gridproxy.GridProxyListener;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.vomrs.model.VomrsClient;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.VomsStatusEvent;
import org.vpac.voms.control.VomsesStatusListener;
import org.vpac.voms.model.VO;
import org.vpac.voms.model.proxy.VomsProxy;

public class VomsProxyInitPanel extends JPanel implements VomsesStatusListener,
		GridProxyListener {

	private static final long serialVersionUID = 1L;

	private static ResourceBundle messages = ResourceBundle.getBundle(
			"GridProxyPanelMessageBundle", java.util.Locale.getDefault()); // @jve:decl-index=0:

	static final Logger myLogger = Logger.getLogger(VomsProxyInitPanel.class
			.getName());

	public static final Integer[] PROXY_LIFETIMES = new Integer[] { 1, 2, 3, 4,
			5, 6, 7, 14, 21 };

	// public static final Integer DEFAULT_PROXY_LIFETIME = new Integer(7);

	private static final long EXPIREDPROXYWARNING = 1800;

	private JLabel voLabel = null;

	private JComboBox voComboBox = null;

	private JLabel gridPassphraseLabel = null;

	private JPasswordField gridPassphraseField = null;

	private JButton initButton = null;

	private JLabel validLabel = null;

	private JComboBox validComboBox = null;

	private JCheckBox vomsCheckBox = null;

	private DefaultComboBoxModel voComboBoxModel = null;

	private DefaultComboBoxModel groupComboBoxModel = null;

	private DefaultComboBoxModel validProxyComboBoxModel = null;

	private JPanel voAttributePanel = null;

	private JPanel gridProxyPanel = null;

	private JLabel groupLabel = null;

	private JComboBox groupComboBox = null;

	private Color lighterColor = null;

	private JPanel localProxyInfoPanel = null;

	private JLabel voInfoLabel = null;

	private JTextField dnTextField = null;

	private JLabel timeLeftLabel = null;

	private JTextField timeLeftTextField = null;

	private JButton destroyButton = null;

	private JLabel pathLabel = null;

	private JTextField pathTextField = null;

	private ImageIcon currentIcon = null;

	private ImageIcon redLight = null;

	private ImageIcon yellowLight = null;

	private ImageIcon greenLight = null;

	private ImageIcon greyLight = null;

	private Action updatePanelAction = null;

	private Action blinkingAction = null;

	private Timer blinkTimer = null;

	private boolean warning = false;

	private JLabel imageLabel = null;

	private Timer updatePanel = null;

	// /**
	// * This is the default constructor
	// */
	// public VomsProxyInitPanel() {
	// super();
	// initialize();
	// LocalVomses.getLocalVomses().addStatusListener(this);
	// LocalProxy.getDefaultProxy().addStatusListener(this);
	// }

	public VomsProxyInitPanel(Color lighterColor) {
		super();
		this.lighterColor = lighterColor;
		initTimer();
		initialize();
		LocalVomses.getLocalVomses().addStatusListener(this);
		LocalProxy.addStatusListener(this);
		this.gridProxyStatusChanged(new GridProxyEvent(this, LocalProxy
				.getStatus()));

	}

	public void initTimer() {

		redLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/redLight.png"));
		yellowLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/yellowLight.png"));
		greenLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/greenLight.png"));
		greyLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/greyLight.png"));

		currentIcon = redLight;

		updatePanelAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {

				Long hours = LocalProxy.getDefaultProxy().getHoursLeft();
				String days_string = "";
				if (hours > 23) {
					if (hours > 47)
						days_string = hours / 24 + " days, ";
					else
						days_string = "1 day, ";
				}

				getTimeLeftTextField().setText(
						days_string
								+ LocalProxy.getDefaultProxy().getHoursLeft()
								% 24 + "h, "
								+ LocalProxy.getDefaultProxy().getMinutesLeft()
								+ "min, "
								+ LocalProxy.getDefaultProxy().getSecondsLeft()
								+ "sec");
				if (!warning
						&& LocalProxy.getDefaultProxy().getTimeLeft() < EXPIREDPROXYWARNING) {
					setCurrentIcon(yellowLight);
					blinkTimer.start();
					warning = true;
				}
			}
		};

		blinkingAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			boolean shouldDraw = false;

			public void actionPerformed(ActionEvent e) {
				if (shouldDraw = !shouldDraw) {
					colorLight();
				} else {
					greyLight();
				}
			}
		};
		blinkTimer = new Timer(600, blinkingAction);
		updatePanel = new Timer(1000, updatePanelAction);

	}

	private void colorLight() {
		this.imageLabel.setIcon(currentIcon);
	}

	private void greyLight() {
		this.imageLabel.setIcon(greyLight);
	}

	private void setCurrentIcon(ImageIcon icon) {
		currentIcon = icon;
		imageLabel.setIcon(icon);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(20, 20, 20, 20);
		gridBagConstraints.gridy = 0;
		GridBagConstraints gridProxyPanelConstraints = new GridBagConstraints();
		gridProxyPanelConstraints.gridx = 0;
		gridProxyPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridProxyPanelConstraints.weightx = 1.0;
		gridProxyPanelConstraints.weighty = 1.0;
		gridProxyPanelConstraints.insets = new Insets(0, 20, 20, 20);
		gridProxyPanelConstraints.anchor = GridBagConstraints.NORTH;
		gridProxyPanelConstraints.gridy = 1;
		this.setSize(688, 606);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(245, 245, 245));
		this.add(getGridProxyPanel(), gridProxyPanelConstraints);
		this.add(getLocalProxyInfoPanel(), gridBagConstraints);
		deactivateVOMSPanel();
		gridProxyStatusChanged(new GridProxyEvent(this, LocalProxy.getStatus()));
	}

	/**
	 * This method initializes voComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getVoComboBox() {
		if (voComboBox == null) {
			voComboBoxModel = new DefaultComboBoxModel();
			voComboBox = new JComboBox(voComboBoxModel);
			voComboBox.setPreferredSize(new Dimension(32, 24));
			voComboBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fillGroupsComboBox();
				}
			});
		}
		return voComboBox;
	}

	private void fillGroupsComboBox() {
		groupComboBoxModel.removeAllElements();
		if (voComboBoxModel.getSize() > 0) {

			Voms voms_temp = (Voms) (voComboBoxModel.getSelectedItem());

			if (voms_temp == null)
				return;

			if (voms_temp.getAc() != null) {
				// means non-vomrs voms
				ArrayList<String> fqans;
				try {
					fqans = new VOMSAttributeCertificate(voms_temp.getAc())
							.getVOMSFQANs();
				} catch (Exception e) {
					myLogger.error(e);
					return;
				}
				for (String fqan : fqans) {
					String substring = fqan
							.substring(0, fqan.indexOf("/Role="));
					if (groupComboBoxModel.getIndexOf(substring) == -1)
						groupComboBoxModel.addElement(substring);
				}
			} else {

				String[] groups = VomrsClient.parseGroups((String) ((voms_temp
						.getInfoQuery().getResult())[14]));
				for (String group : groups) {
					if (group.indexOf("STATUS:Approved") != -1
							&& group.indexOf("Role=Member") != -1) {
						groupComboBoxModel.addElement(group.substring(0,
								group.indexOf("/Role=")));
					}
				}
			}
		}

	}

	/**
	 * This method initializes gridPassphraseField
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getGridPassphraseField() {
		if (gridPassphraseField == null) {
			gridPassphraseField = new JPasswordField();
		}
		return gridPassphraseField;
	}

	private void initProxy() {

		try {
			// if ( LocalProxy.getDefaultProxy() != null )
			// LocalProxy.getDefaultProxy().destroy();

			Long proxyLength = new Long(1000 * 3600 * 12);
			try {
				Long comboBoxInt = new Long(((String) getValidComboBox()
						.getSelectedItem()));
				proxyLength = 1000 * 3600 * 24 * comboBoxInt;
				// proxyLength = 1000*comboBoxInt;
			} catch (NumberFormatException e3) {
				JOptionPane.showMessageDialog(getParent(), "<html><body><p>"
						+ getMessages().getString("error.init.notAnumber")
						+ "</p></body></html>",
						getMessages().getString("error.init.title"),
						JOptionPane.ERROR_MESSAGE);
				return;
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			if (getVomsCheckBox().isSelected() && voComboBoxModel.getSize() > 0) {

				VO selVO = ((Voms) voComboBoxModel.getSelectedItem()).getVo();
				String selGroup = ((String) getGroupComboBox()
						.getSelectedItem());

				VomsProxy vomsProxy = new VomsProxy(LocalProxy.getProxyFile(),
						selVO, "G" + selGroup, selGroup);

				vomsProxy.init(getGridPassphraseField().getPassword(),
						proxyLength);
				// vomsProxy.writeToDisk();
				LocalProxy.setDefaultProxy(vomsProxy);
				fillLocalGridProxyInfo();

			} else {
				LocalProxy.createPlainGlobusProxy(getGridPassphraseField()
						.getPassword(), proxyLength);
				fillLocalGridProxyInfo();
			}

		} catch (MissingPrerequisitesException e1) {
			JOptionPane.showMessageDialog(getParent(), "<html><body><p>"
					+ getMessages()
							.getString("error.init.missingPrerequisites")
					+ "</p><p>" + e1.getMessage() + "</p></body></html>",
					getMessages().getString("error.init.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(getParent(), "<html><body><p>"
					+ getMessages().getString("error.init.ioexception")
					+ "</p><p>" + e1.getMessage() + "</p></body></html>",
					getMessages().getString("error.init.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (GeneralSecurityException e1) {
			JOptionPane.showMessageDialog(
					getParent(),
					"<html><body><p>"
							+ getMessages().getString(
									"error.init.generalSecurityException")
							+ "</p><p>" + e1.getMessage()
							+ "</p></body></html>",
					getMessages().getString("error.init.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getParent(), "<html><body><p>"
					+ getMessages().getString("error.init.exception")
					+ "</p><p>" + e.getMessage() + "</p></body></html>",
					getMessages().getString("error.init.title"),
					JOptionPane.ERROR_MESSAGE);
		} finally {
			getGridPassphraseField().setText("");
			getValidComboBox().setSelectedIndex(0);
		}
		Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(normalCursor);

	}

	/**
	 * This method initializes initButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getInitButton() {
		if (initButton == null) {
			initButton = new JButton();
			initButton.setText("Init");
			initButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new Thread() {

						public void run() {
							Cursor hourglassCursor = new Cursor(
									Cursor.WAIT_CURSOR);
							setCursor(hourglassCursor);
							getInitButton().setEnabled(false);
							initProxy();
							getInitButton().setEnabled(true);
							Cursor normalCursor = new Cursor(
									Cursor.DEFAULT_CURSOR);
							setCursor(normalCursor);

						}
					}.start();
				}
			});

		}
		return initButton;
	}

	/**
	 * This method initializes validComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getValidComboBox() {
		if (validComboBox == null) {
			validProxyComboBoxModel = new DefaultComboBoxModel(GrixProperty
					.getString("default.local.proxy.lifetimes").split(","));
			validProxyComboBoxModel.setSelectedItem(GrixProperty
					.getString("default.local.proxy.lifetime"));
			validComboBox = new JComboBox(this.validProxyComboBoxModel);
			// validComboBox.
			validComboBox.setPreferredSize(new Dimension(44, 24));
			validComboBox.setEditable(true);
		}
		return validComboBox;
	}

	/**
	 * This method initializes vomsCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getVomsCheckBox() {
		if (vomsCheckBox == null) {
			vomsCheckBox = new JCheckBox();
			vomsCheckBox.setOpaque(false);
			vomsCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
			vomsCheckBox.setText("Include VOMS Attribute Certificate");

			vomsCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						deactivateVOMSPanel();
					} else {
						activateVOMSPanel();
					}
				}

			});
		}
		return vomsCheckBox;
	}

	private void deactivateVOMSPanel() {
		myLogger.debug("VO attributes deselected");
		getVoComboBox().setEnabled(false);
		getGroupComboBox().setEnabled(false);
		getVoComboBox().setVisible(false);
		getGroupComboBox().setVisible(false);
		groupLabel.setVisible(false);
		voLabel.setVisible(false);
	}

	private void activateVOMSPanel() {
		myLogger.debug("VO attributes selected");
		getVoComboBox().setEnabled(true);
		getGroupComboBox().setEnabled(true);
		getVoComboBox().setVisible(true);
		getGroupComboBox().setVisible(true);
		groupLabel.setVisible(true);
		voLabel.setVisible(true);
	}

	public void vomsStatusChanged(VomsStatusEvent event) {
		if (event.getAction() == VomsStatusEvent.NEW_VOMS
				|| event.getAction() == VomsStatusEvent.STATUS_CHANGED) {
			if ((((Voms) event.getSource()).getStatus() == Voms.MEMBER || ((Voms) event
					.getSource()).getStatus() == Voms.NON_VOMRS_MEMBER)
					&& (voComboBoxModel.getIndexOf(((Voms) event.getSource())) == -1))
				voComboBoxModel.addElement((Voms) event.getSource());
		} else if (event.getAction() == VomsStatusEvent.REMOVED_VOMS_MEMBERSHIP) {
			voComboBoxModel.removeElement(((Voms) event.getSource()));

		} else if (event.getAction() == VomsStatusEvent.INFO_CHANGED) {
			fillGroupsComboBox();
		}

	}

	/**
	 * This method initializes voAttributePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getVoAttributePanel() {
		if (voAttributePanel == null) {
			GridBagConstraints groupComboBoxConstraints = new GridBagConstraints();
			groupComboBoxConstraints.fill = GridBagConstraints.BOTH;
			groupComboBoxConstraints.gridy = 3;
			groupComboBoxConstraints.weightx = 1.0;
			groupComboBoxConstraints.insets = new Insets(0, 0, 20, 20);
			groupComboBoxConstraints.gridx = 1;
			GridBagConstraints groupLabelConstraints = new GridBagConstraints();
			groupLabelConstraints.gridx = 0;
			groupLabelConstraints.insets = new Insets(0, 15, 20, 20);
			groupLabelConstraints.anchor = GridBagConstraints.EAST;
			groupLabelConstraints.gridy = 3;
			groupLabel = new JLabel();
			groupLabel.setText("Group");
			GridBagConstraints voComboBoxConstraints = new GridBagConstraints();
			voComboBoxConstraints.fill = GridBagConstraints.BOTH;
			voComboBoxConstraints.gridwidth = 1;
			voComboBoxConstraints.gridx = 1;
			voComboBoxConstraints.gridy = 2;
			voComboBoxConstraints.weightx = 1.0;
			voComboBoxConstraints.insets = new Insets(0, 0, 15, 20);
			GridBagConstraints voLabelConstraints = new GridBagConstraints();
			voLabelConstraints.anchor = GridBagConstraints.EAST;
			voLabelConstraints.gridx = 0;
			voLabelConstraints.gridy = 2;
			voLabelConstraints.insets = new Insets(0, 15, 15, 20);
			GridBagConstraints vomsCheckBoxConstraints = new GridBagConstraints();
			vomsCheckBoxConstraints.anchor = GridBagConstraints.NORTHWEST;
			vomsCheckBoxConstraints.gridwidth = 2;
			vomsCheckBoxConstraints.gridx = 0;
			vomsCheckBoxConstraints.gridy = 0;
			vomsCheckBoxConstraints.weightx = 1.0;
			vomsCheckBoxConstraints.insets = new Insets(10, 10, 15, 0);
			voAttributePanel = new JPanel();
			if (lighterColor != null)
				voAttributePanel.setBackground(lighterColor);
			voAttributePanel.setLayout(new GridBagLayout());
			// voAttributePanel.setBackground(new Color(245, 245, 245));
			voAttributePanel.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			voAttributePanel.add(getVomsCheckBox(), vomsCheckBoxConstraints);
			voAttributePanel.add(voLabel, voLabelConstraints);
			voAttributePanel.add(getVoComboBox(), voComboBoxConstraints);
			voAttributePanel.add(groupLabel, groupLabelConstraints);
			voAttributePanel.add(getGroupComboBox(), groupComboBoxConstraints);
		}
		return voAttributePanel;
	}

	/**
	 * This method initializes gridProxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGridProxyPanel() {
		if (gridProxyPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints8.gridx = 4;
			gridBagConstraints8.gridy = 5;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.insets = new Insets(0, 0, 20, 20);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.NORTH;
			gridBagConstraints7.insets = new Insets(0, 20, 20, 20);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 4;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.weighty = 0.0;
			gridBagConstraints7.gridwidth = 5;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridProxyPanel = new JPanel();
			if (lighterColor != null)
				gridProxyPanel.setBackground(lighterColor);
			GridBagConstraints validComboBoxConstraints = new GridBagConstraints();
			validComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
			validComboBoxConstraints.gridy = 3;
			validComboBoxConstraints.weightx = 0.0;
			validComboBoxConstraints.insets = new Insets(0, 0, 20, 20);
			validComboBoxConstraints.gridwidth = 1;
			validComboBoxConstraints.gridx = 4;
			GridBagConstraints validLabelConstraints = new GridBagConstraints();
			validLabelConstraints.gridx = 4;
			validLabelConstraints.insets = new Insets(0, 0, 5, 20);
			validLabelConstraints.anchor = GridBagConstraints.SOUTHEAST;
			validLabelConstraints.gridy = 1;
			validLabel = new JLabel();
			validLabel.setText("Valid (days)");
			GridBagConstraints gridPassphraseFieldConstraints = new GridBagConstraints();
			gridPassphraseFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridPassphraseFieldConstraints.gridy = 3;
			gridPassphraseFieldConstraints.weightx = 1.0;
			gridPassphraseFieldConstraints.insets = new Insets(0, 0, 20, 20);
			gridPassphraseFieldConstraints.gridwidth = 3;
			gridPassphraseFieldConstraints.gridx = 1;
			GridBagConstraints gridPassphraseLabelConstraints = new GridBagConstraints();
			gridPassphraseLabelConstraints.gridx = 0;
			gridPassphraseLabelConstraints.insets = new Insets(0, 20, 20, 20);
			gridPassphraseLabelConstraints.anchor = GridBagConstraints.EAST;
			gridPassphraseLabelConstraints.gridy = 3;
			gridPassphraseLabel = new JLabel();
			gridPassphraseLabel.setText("Key passphrase");
			GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
			usernameLabelConstraints.gridx = 0;
			usernameLabelConstraints.insets = new Insets(10, 20, 15, 20);
			usernameLabelConstraints.anchor = GridBagConstraints.EAST;
			usernameLabelConstraints.gridy = 1;
			voLabel = new JLabel();
			voLabel.setText("VO");
			gridProxyPanel.setLayout(new GridBagLayout());
			// gridProxyPanel.setBackground(new Color(245, 245, 245));
			gridProxyPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEmptyBorder(15, 0, 0, 0), "Proxy init",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 14), new Color(51, 51, 51)));
			gridProxyPanel.add(gridPassphraseLabel,
					gridPassphraseLabelConstraints);
			gridProxyPanel.add(getGridPassphraseField(),
					gridPassphraseFieldConstraints);
			gridProxyPanel.add(validLabel, validLabelConstraints);
			gridProxyPanel.add(getValidComboBox(), validComboBoxConstraints);
			gridProxyPanel.add(getVoAttributePanel(), gridBagConstraints7);
			gridProxyPanel.add(getInitButton(), gridBagConstraints8);
		}
		return gridProxyPanel;
	}

	/**
	 * This method initializes groupComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getGroupComboBox() {
		if (groupComboBox == null) {
			groupComboBoxModel = new DefaultComboBoxModel();
			groupComboBox = new JComboBox(groupComboBoxModel);
			groupComboBox.setPreferredSize(new Dimension(32, 24));

			if (voComboBoxModel.getSize() > 0) {
				String[] groups = VomrsClient
						.parseGroups((String) ((((Voms) (voComboBoxModel
								.getSelectedItem())).getInfoQuery().getResult())[14]));
				for (String group : groups) {
					if (group.indexOf("Role=Member") != -1) {
						groupComboBoxModel.addElement(group.substring(0,
								group.indexOf("/Role=")));
					}
				}
			}
		}
		return groupComboBox;
	}

	public void gridProxyStatusChanged(GridProxyEvent e) {

		fillLocalGridProxyInfo();

		// if (e.getStatus() != GridProxy.INITIALIZED) {
		// } else {
		// getGroupComboBox().setEnabled(true);
		// getVoComboBox().setEnabled(true);
		// getVomsCheckBox().setEnabled(true);
		// }

		if (GridProxy.INITIALIZED == e.getStatus()) {
			getGroupComboBox().setEnabled(true);
			getVoComboBox().setEnabled(true);
			getVomsCheckBox().setEnabled(true);
			getTimeLeftTextField().setText(
					LocalProxy.getDefaultProxy().getFormatedTime());
			getDestroyButton().setEnabled(true);
			setCurrentIcon(greenLight);
			blinkTimer.stop();
			updatePanel.start();
		}

		else if (GridProxy.INITIALIZING == e.getStatus()) {
			setCurrentIcon(yellowLight);
			updatePanel.stop();
			blinkTimer.start();
		}

		else if (GridProxy.MISSING_PREREQUISITES == e.getStatus()) {

			getInitButton().setEnabled(true);
			getDestroyButton().setEnabled(false);
			setCurrentIcon(redLight);
			updatePanel.stop();
			blinkTimer.start();
		}

		else {
			updatePanel.stop();
			blinkTimer.stop();
			getTimeLeftTextField().setText("no local grid proxy");
			getDestroyButton().setEnabled(false);
			getInitButton().setEnabled(true);
			// getGridPassphraseField().setEditable(true);
			setCurrentIcon(redLight);
			groupComboBoxModel.removeAllElements();
			voComboBoxModel.removeAllElements();
			getGroupComboBox().setEnabled(false);
			getVoComboBox().setEnabled(false);
			getVomsCheckBox().setEnabled(false);
			getVomsCheckBox().setSelected(false);
			deactivateVOMSPanel();
		}

	}

	private void fillLocalGridProxyInfo() {

		if (LocalProxy.isValid()) {
			if (LocalProxy.getDefaultProxy() instanceof org.vpac.voms.model.proxy.VomsProxy) {
				try {
					VomsProxy vomsProxy = (VomsProxy) LocalProxy
							.getDefaultProxy();

					String group = ((VomsProxy) LocalProxy.getDefaultProxy())
							.getVomsac().getVOMSFQANs().get(0);

					if (group.indexOf("/Role=") >= 0) {
						getVoInfoTextField().setText(
								group.substring(0, group.indexOf("/Role=")));
					} else {
						getVoInfoTextField().setText(group);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					myLogger.error(e);
					getVoInfoTextField().setText(
							"Error reading VO information.");
				}
			} else {
				getVoInfoTextField().setText("This proxy is not a voms proxy.");
			}

			getPathTextField().setText(
					LocalProxy.getDefaultProxy().getProxyFile().toString());
		} else {
			getVoInfoTextField().setText("n/a");
			getPathTextField().setText("n/a");
		}

	}

	/**
	 * This method initializes localProxyInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLocalProxyInfoPanel() {
		if (localProxyInfoPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints3.gridy = 4;
			imageLabel = new JLabel();
			imageLabel.setText("");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(0, 20, 15, 20);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new Insets(0, 20, 15, 0);
			gridBagConstraints10.anchor = GridBagConstraints.EAST;
			gridBagConstraints10.gridy = 1;
			pathLabel = new JLabel();
			pathLabel.setText("Path:");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.anchor = GridBagConstraints.EAST;
			gridBagConstraints9.insets = new Insets(0, 0, 20, 20);
			gridBagConstraints9.gridy = 4;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.insets = new Insets(0, 20, 20, 20);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.insets = new Insets(0, 20, 20, 0);
			gridBagConstraints5.gridy = 3;
			timeLeftLabel = new JLabel();
			timeLeftLabel.setText("Time left:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(10, 20, 15, 20);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(10, 20, 15, 0);
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.gridy = 0;
			voInfoLabel = new JLabel();
			voInfoLabel.setText("VO info:");
			localProxyInfoPanel = new JPanel();
			if (lighterColor != null)
				localProxyInfoPanel.setBackground(lighterColor);

			localProxyInfoPanel.setLayout(new GridBagLayout());
			localProxyInfoPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEmptyBorder(15, 0, 0, 0),
					"Local proxy details", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 14), new Color(51, 51, 51)));
			localProxyInfoPanel.add(voInfoLabel, gridBagConstraints1);
			localProxyInfoPanel.add(getVoInfoTextField(), gridBagConstraints2);
			localProxyInfoPanel.add(timeLeftLabel, gridBagConstraints5);
			localProxyInfoPanel
					.add(getTimeLeftTextField(), gridBagConstraints6);
			localProxyInfoPanel.add(getDestroyButton(), gridBagConstraints9);
			localProxyInfoPanel.add(pathLabel, gridBagConstraints10);
			localProxyInfoPanel.add(getPathTextField(), gridBagConstraints11);
			localProxyInfoPanel.add(imageLabel, gridBagConstraints3);
		}
		return localProxyInfoPanel;
	}

	/**
	 * This method initializes dnTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getVoInfoTextField() {
		if (dnTextField == null) {
			dnTextField = new JTextField();
			dnTextField.setEditable(false);
			dnTextField.setHorizontalAlignment(JTextField.CENTER);
		}
		return dnTextField;
	}

	/**
	 * This method initializes timeLeftTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTimeLeftTextField() {
		if (timeLeftTextField == null) {
			timeLeftTextField = new JTextField();
			timeLeftTextField.setEditable(false);
			timeLeftTextField.setHorizontalAlignment(JTextField.CENTER);
		}
		return timeLeftTextField;
	}

	/**
	 * This method initializes destroyButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDestroyButton() {
		if (destroyButton == null) {
			destroyButton = new JButton();
			destroyButton.setText("Destroy");
			destroyButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							LocalProxy.destroy();
						}
					});
		}
		return destroyButton;
	}

	/**
	 * This method initializes pathTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getPathTextField() {
		if (pathTextField == null) {
			pathTextField = new JTextField();
			pathTextField.setEditable(false);
			pathTextField.setHorizontalAlignment(JTextField.CENTER);
		}
		return pathTextField;
	}

	public static ResourceBundle getMessages() {
		return messages;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
