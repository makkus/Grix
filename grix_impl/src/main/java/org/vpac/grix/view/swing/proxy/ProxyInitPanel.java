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
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.globus.tools.proxy.DefaultGridProxyModel;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.GridProxyEvent;
import org.vpac.common.model.gridproxy.GridProxyListener;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleInfoDialog;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.grix.control.utils.UserProperty;
import org.vpac.grix.model.myproxy.MyProxyCred;
import org.vpac.vomrs.model.VomrsClient;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.VomsStatusEvent;
import org.vpac.voms.control.VomsesStatusListener;
import org.vpac.voms.model.proxy.VomsProxyCredential;

public class ProxyInitPanel extends JPanel implements VomsesStatusListener,
		GridProxyListener {

	private JButton button;
	private static final long serialVersionUID = 1L;

	private static ResourceBundle messages = ResourceBundle.getBundle(
			"GridProxyPanelMessageBundle", java.util.Locale.getDefault());

	static final Logger myLogger = Logger.getLogger(ProxyInitPanel.class
			.getName());

	public static final Integer[] PROXY_LIFETIMES = new Integer[] { 1, 2, 3, 4,
			5, 6, 7, 14, 21 };
	public static final Integer DEFAULT_PROXY_LIFETIME = new Integer(7);

	private JLabel voLabel = null;

	private JComboBox voComboBox = null;

	private JLabel usernameLabel = null;

	private JLabel proxyPassphraseLabel = null;

	private JLabel gridPassphraseLabel = null;

	private JTextField usernameTextField = null;

	private JPasswordField proxyPassphraseField = null;

	private JPasswordField gridPassphraseField = null;

	private JButton initButton = null;

	private JLabel validLabel = null;

	private JComboBox validComboBox = null;

	private JCheckBox vomsCheckBox = null;

	private DefaultComboBoxModel voComboBoxModel = null;

	private DefaultComboBoxModel groupComboBoxModel = null;

	private DefaultComboBoxModel validProxyComboBoxModel = null;

	private DefaultComboBoxModel delegatedProxyComboBoxModel = null;

	private JPanel voAttributePanel = null;

	private JPanel myProxyPanel = null;

	private JLabel groupLabel = null;

	private JComboBox groupComboBox = null;

	private JLabel proxyNameLabel = null;

	private JTextField proxyNameTextField = null;

	private boolean user_typed = false;

	private JPanel advancedPanel = null;

	private JCheckBox advancedCheckBox = null;

	private JLabel delegatedCredsLabel2 = null;

	private JComboBox delegatedCredsComboBox = null;

	private JCheckBox anonymousCheckBox = null;

	private Color lighterColor = null;

	private String preferredVO = null;
	private String preferredGroup = null;

	/**
	 * This is the default constructor
	 */
	public ProxyInitPanel() {
		super();
		initialize();
		LocalVomses.getLocalVomses().addStatusListener(this);
		LocalProxy.addStatusListener(this);
	}

	public ProxyInitPanel(Color lighterColor) {
		super();
		this.lighterColor = lighterColor;
		initialize();
		LocalVomses.getLocalVomses().addStatusListener(this);
		LocalProxy.addStatusListener(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridwidth = 2;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(0, 20, 20, 20);
		gridBagConstraints11.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 20, 20);
		GridBagConstraints myProxyPanelConstraints = new GridBagConstraints();
		myProxyPanelConstraints.gridwidth = 2;
		myProxyPanelConstraints.gridx = 0;
		myProxyPanelConstraints.fill = GridBagConstraints.BOTH;
		myProxyPanelConstraints.weightx = 1.0;
		myProxyPanelConstraints.weighty = 0.0;
		myProxyPanelConstraints.insets = new Insets(20, 20, 20, 20);
		myProxyPanelConstraints.anchor = GridBagConstraints.NORTH;
		myProxyPanelConstraints.gridy = 0;
		GridBagConstraints voAttributePanelConstraints = new GridBagConstraints();
		voAttributePanelConstraints.gridwidth = 2;
		voAttributePanelConstraints.gridx = 0;
		voAttributePanelConstraints.fill = GridBagConstraints.BOTH;
		voAttributePanelConstraints.insets = new Insets(0, 20, 20, 20);
		voAttributePanelConstraints.weightx = 1.0;
		voAttributePanelConstraints.weighty = 0.0;
		voAttributePanelConstraints.anchor = GridBagConstraints.NORTH;
		voAttributePanelConstraints.gridy = 2;

		this.setSize(686, 542);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 7 };
		this.setLayout(gridBagLayout);
		this.setBackground(new Color(245, 245, 245));
		this.add(getMyProxyPanel(), myProxyPanelConstraints);
		this.add(getVoAttributePanel(), voAttributePanelConstraints);
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints_1.insets = new Insets(0, 20, 20, 0);
		gridBagConstraints_1.gridy = 3;
		gridBagConstraints_1.gridx = 0;
		add(getButton(), gridBagConstraints_1);
		this.add(getInitButton(), gridBagConstraints);
		this.add(getAdvancedPanel(), gridBagConstraints11);
		deactivateVOMSPanel();
		deactivateAdvancedPanel();
		gridProxyStatusChanged(new GridProxyEvent(this, LocalProxy.getStatus()));

		// change to remembered settings (if applicable)
		if ("yes".equals(UserProperty.getProperty("REMEMBER_MYPROXY_SETTINGS"))) {

			getUsernameTextField().setText(
					UserProperty.getProperty("MYPROXY_USERNAME_TEXTFIELD"));

			if ("yes".equals(UserProperty
					.getProperty("MYPROXY_ADVANCED_CHECKBOX"))) {
				activateAdvancedPanel();
				getProxyNameTextField()
						.setText(
								UserProperty
										.getProperty("MYPROXY_PROXYNAME_TEXTFIELD"));
				try {
					getDelegatedCredsComboBox()
							.setSelectedItem(
									UserProperty
											.getProperty("MYPROXY_DELEGATED_LIFETIME_COMBOBOX"));
				} catch (NumberFormatException nfe) {
					myLogger.error(nfe);
				}
				getAnonymousCheckBox()
						.setSelected(
								"yes".equals(UserProperty
										.getProperty("MYPROXY_DONT_ALLOW_ANONYMOUS_CHECKBOX")));
			}

			getValidComboBox().setSelectedItem(
					UserProperty.getProperty("MYPROXY_VALID_COMBOBOX"));

			if ("yes".equals(UserProperty.getProperty("MYPROXY_VO_CHECKBOX"))) {
				activateVOMSPanel();
			}
			this.preferredVO = UserProperty.getProperty("MYPROXY_SELECTED_VO");
			this.preferredGroup = UserProperty
					.getProperty("MYPROXY_SELECTED_GROUP");
			getVoComboBox().setSelectedItem(this.preferredVO);
			getGroupComboBox().setSelectedItem(this.preferredGroup);
		}

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
		if (getAdvancedCheckBox().isSelected() && !user_typed) {
			if ("yes"
					.equals(UserProperty.getProperty("AUTOSUGGEST_PROXYNAMES")))
				suggestProxyName();
		}
	}

	private void suggestProxyName() {
		if ("yes".equals(UserProperty.getProperty("AUTOSUGGEST_PROXYNAMES"))) {
			try {
				getProxyNameTextField().setText(
						((String) groupComboBoxModel.getSelectedItem())
								.substring(1).replaceAll("/", "_")
								.toLowerCase());
			} catch (RuntimeException e1) {
				// does not matter
			}
		}
	}

	/**
	 * This method initializes usernameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUsernameTextField() {
		if (usernameTextField == null) {
			usernameTextField = new JTextField();
			usernameTextField.setText(System.getProperty("user.name"));
			// try {
			// usernameTextField.setText(new
			// Certificate(GlobusLocations.defaultLocations()
			// .getUserCert()).getCn());
			// } catch (IOException e) {
			// // does not really matter
			// //e.printStackTrace();
			// } catch (GeneralSecurityException e) {
			// // does not really matter
			// //e.printStackTrace();
			// }
		}
		return usernameTextField;
	}

	/**
	 * This method initializes proxyPassphraseField
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getProxyPassphraseField() {
		if (proxyPassphraseField == null) {
			proxyPassphraseField = new JPasswordField();
			proxyPassphraseField
					.setToolTipText("This is a password that will protect your\nnewly created proxy in the MyProxy server.");
		}
		return proxyPassphraseField;
	}

	/**
	 * This method initializes gridPassphraseField
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getGridPassphraseField() {
		if (gridPassphraseField == null) {
			gridPassphraseField = new JPasswordField();
			gridPassphraseField
					.setToolTipText("This is the passphrase protecting your private key on this computer.");
		}
		return gridPassphraseField;
	}

	private void initProxy() {

		// check whether proxy lifetimes can be parsed to integer
		try {
			new Integer(
					((String) getDelegatedCredsComboBox().getSelectedItem()));
		} catch (NumberFormatException e3) {
			JOptionPane.showMessageDialog(getParent(), "<html><body><p>"
					+ getMessages().getString("error.init.notAnumber")
					+ "</p></body></html>",
					getMessages().getString("error.init.title"),
					JOptionPane.ERROR_MESSAGE);
			getDelegatedCredsComboBox()
					.setSelectedItem(
							GrixProperty
									.getString("default.myproxy.delegated.proxy.lifetimes"));
			getProxyPassphraseField().setText("");
			getGridPassphraseField().setText("");
			return;
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		try {
			new Integer(((String) getValidComboBox().getSelectedItem()));
		} catch (NumberFormatException e3) {
			JOptionPane.showMessageDialog(getParent(), "<html><body><p>"
					+ getMessages().getString("error.init.notAnumber")
					+ "</p></body></html>",
					getMessages().getString("error.init.title"),
					JOptionPane.ERROR_MESSAGE);
			validProxyComboBoxModel.setSelectedItem(GrixProperty
					.getString("default.local.proxy.lifetime"));
			getProxyPassphraseField().setText("");
			getGridPassphraseField().setText("");
			return;
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		MyProxyCred newCred = null;

		newCred = new MyProxyCred(getUsernameTextField().getText());

		try {

			DefaultGridProxyModel model = new DefaultGridProxyModel();
			model.getProperties().setProxyLifeTime(
					Integer.parseInt((String) getValidComboBox()
							.getSelectedItem()) * 24);
			GlobusCredential globusProxy = model.createProxy(new String(
					getGridPassphraseField().getPassword()));

			int delegated_creds_lifetime = 12;
			String credential_name = "";
			if (getAdvancedCheckBox().isSelected()) {
				delegated_creds_lifetime = Integer
						.parseInt((String) getDelegatedCredsComboBox()
								.getSelectedItem());
				credential_name = getProxyNameTextField().getText();
				if (!"".equals(credential_name.trim())) {
					newCred.setCredentialName(credential_name);
				}
			}

			boolean anonymous = !getAnonymousCheckBox().isSelected();

			if (getVomsCheckBox().isSelected() && voComboBoxModel.getSize() > 0) {

				// TODO check whether there is a local grid-proxy

				String selGroup = ((String) getGroupComboBox()
						.getSelectedItem());

				VomsProxyCredential vomsProxy = new VomsProxyCredential(
						globusProxy,
						((Voms) voComboBoxModel.getSelectedItem()).getVo(), "G"
								+ selGroup, selGroup,
						Integer.parseInt((String) getValidComboBox()
								.getSelectedItem()) * 24);

				newCred.init(vomsProxy.getVomsProxy(), anonymous,
						getProxyPassphraseField().getPassword(), Integer
								.parseInt((String) getDelegatedCredsComboBox()
										.getSelectedItem()) * 24);
			} else {
				newCred.init(globusProxy, anonymous, getProxyPassphraseField()
						.getPassword(), Integer
						.parseInt((String) getDelegatedCredsComboBox()
								.getSelectedItem()) * 24);
			}
			StringBuffer successmessage = new StringBuffer();
			// TODO outsource messages...
			successmessage
					.append("<p>Successfully created proxy and delegated it to the myproxy server.</p>");
			successmessage.append("<p><b>Details:</b><p>");
			successmessage.append("<b>Username:</b> "
					+ getUsernameTextField().getText() + "<br>");
			successmessage.append("<b>Proxy name:</b> ");
			if (getProxyNameTextField().getText().trim().length() != 0) {
				successmessage.append(getProxyNameTextField().getText().trim());
			} else {
				successmessage.append("No proxy name/default.");
			}
			// TODO maybe more details

			MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
			messagePanel.setDocument(successmessage.toString(), true);
			messagePanel.setPreferredSize(new Dimension(300, 200));
			JOptionPane.showMessageDialog(ProxyInitPanel.this, messagePanel,
					"Created and delegated proxy.",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			StringBuffer errormessage = new StringBuffer();
			// TODO outsource messages...
			errormessage.append("<p>Could not create voms proxy.</p>");
			if (e.getMessage() != null) {
				errormessage.append("<p>Reason:</p>");
				errormessage.append("<p>" + e.getMessage() + "</p>");
			}

			MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
			messagePanel.setDocument(errormessage.toString(), true);
			messagePanel.setPreferredSize(new Dimension(300, 200));
			JOptionPane.showMessageDialog(ProxyInitPanel.this, messagePanel,
					"Error when creating voms proxy.",
					JOptionPane.ERROR_MESSAGE);

			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		} finally {
			getProxyPassphraseField().setText("");
			getGridPassphraseField().setText("");
		}
	}

	public static ResourceBundle getMessages() {
		return messages;
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

							// save defaults if user wants that
							if ("yes".equals(UserProperty
									.getProperty("REMEMBER_MYPROXY_SETTINGS"))) {

								UserProperty.setProperty(
										"MYPROXY_USERNAME_TEXTFIELD",
										getUsernameTextField().getText());

								if (getAdvancedCheckBox().isSelected()) {
									UserProperty.setProperty(
											"MYPROXY_ADVANCED_CHECKBOX", "yes");
								} else
									UserProperty.setProperty(
											"MYPROXY_ADVANCED_CHECKBOX", "no");

								UserProperty.setProperty(
										"MYPROXY_PROXYNAME_TEXTFIELD",
										getProxyNameTextField().getText());

								UserProperty.setProperty(
										"MYPROXY_DELEGATED_LIFETIME_COMBOBOX",
										getDelegatedCredsComboBox()
												.getSelectedItem().toString());

								if (getAnonymousCheckBox().isSelected())
									UserProperty
											.setProperty(
													"MYPROXY_DONT_ALLOW_ANONYMOUS_CHECKBOX",
													"yes");
								else
									UserProperty
											.setProperty(
													"MYPROXY_DONT_ALLOW_ANONYMOUS_CHECKBOX",
													"no");

								UserProperty.setProperty(
										"MYPROXY_VALID_COMBOBOX",
										getValidComboBox().getSelectedItem()
												.toString());

								if (getVomsCheckBox().isSelected()) {
									UserProperty.setProperty(
											"MYPROXY_VO_CHECKBOX", "yes");

									UserProperty.setProperty(
											"MYPROXY_SELECTED_VO",
											getVoComboBox().getSelectedItem()
													.toString());
									UserProperty.setProperty(
											"MYPROXY_SELECTED_GROUP",
											getGroupComboBox()
													.getSelectedItem()
													.toString());

								} else
									UserProperty.setProperty(
											"MYPROXY_VO_CHECKBOX", "no");

							}

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
					.getString("default.myproxy.proxy.lifetimes").split(","));
			validProxyComboBoxModel.setSelectedItem(GrixProperty
					.getString("default.myproxy.proxy.lifetime"));
			validComboBox = new JComboBox(this.validProxyComboBoxModel);
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
		if (getVomsCheckBox().isSelected()) {
			getVomsCheckBox().setSelected(false);
		}
		getVoComboBox().setEnabled(false);
		getGroupComboBox().setEnabled(false);
		getVoComboBox().setVisible(false);
		getGroupComboBox().setVisible(false);
		groupLabel.setVisible(false);
		voLabel.setVisible(false);
	}

	private void activateVOMSPanel() {
		if (!getVomsCheckBox().isSelected()) {
			getVomsCheckBox().setSelected(true);
		}
		myLogger.debug("VO attributes selected");
		getVoComboBox().setEnabled(true);
		getGroupComboBox().setEnabled(true);
		getVoComboBox().setVisible(true);
		getGroupComboBox().setVisible(true);
		groupLabel.setVisible(true);
		voLabel.setVisible(true);
	}

	private void deactivateAdvancedPanel() {
		if (getAdvancedCheckBox().isSelected()) {
			getAdvancedCheckBox().setSelected(false);
		}
		myLogger.debug("Advanced options deselected");
		getProxyNameTextField().setEnabled(false);
		getProxyNameTextField().setVisible(false);
		getAnonymousCheckBox().setVisible(false);
		this.getDelegatedCredsComboBox().setEnabled(false);
		this.getDelegatedCredsComboBox().setVisible(false);
		this.getProxyNameTextField().setText("");
		proxyNameLabel.setVisible(false);
		this.delegatedCredsLabel2.setVisible(false);
	}

	private void activateAdvancedPanel() {
		if (!getAdvancedCheckBox().isSelected()) {
			getAdvancedCheckBox().setSelected(true);
		}
		myLogger.debug("Advanced options selected");
		getProxyNameTextField().setEnabled(true);
		getProxyNameTextField().setVisible(true);
		getAnonymousCheckBox().setVisible(true);
		this.getDelegatedCredsComboBox().setEnabled(true);
		this.getDelegatedCredsComboBox().setVisible(true);
		proxyNameLabel.setVisible(true);
		this.delegatedCredsLabel2.setVisible(true);
	}

	public void vomsStatusChanged(VomsStatusEvent event) {
		if (event.getAction() == VomsStatusEvent.NEW_VOMS
				|| event.getAction() == VomsStatusEvent.STATUS_CHANGED) {
			if ((((Voms) event.getSource()).getStatus() == Voms.MEMBER || ((Voms) event
					.getSource()).getStatus() == Voms.NON_VOMRS_MEMBER)
					&& (voComboBoxModel.getIndexOf(((Voms) event.getSource())) == -1))
				voComboBoxModel.addElement((Voms) event.getSource());

			// set preferred VO & group
			if (this.preferredVO != null
					&& voComboBoxModel.getIndexOf(this.preferredVO) != -1) {
				getVoComboBox().setSelectedItem(this.preferredVO);
			}
			if (this.preferredGroup != null
					&& groupComboBoxModel.getIndexOf(this.preferredGroup) != -1) {
				getGroupComboBox().setSelectedItem(this.preferredGroup);
			}
		} else if (event.getAction() == VomsStatusEvent.REMOVED_VOMS_MEMBERSHIP) {
			voComboBoxModel.removeElement(((Voms) event.getSource()));
			if (voComboBoxModel.getSize() == 0) {
				getProxyNameTextField().setText("");
			}

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
			proxyNameLabel = new JLabel();
			proxyNameLabel.setText("Proxy name");
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
			voAttributePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
					0));
			voAttributePanel.add(getVomsCheckBox(), vomsCheckBoxConstraints);
			voAttributePanel.add(voLabel, voLabelConstraints);
			voAttributePanel.add(getVoComboBox(), voComboBoxConstraints);
			voAttributePanel.add(groupLabel, groupLabelConstraints);
			voAttributePanel.add(getGroupComboBox(), groupComboBoxConstraints);
		}
		return voAttributePanel;
	}

	/**
	 * This method initializes myProxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMyProxyPanel() {
		if (myProxyPanel == null) {
			myProxyPanel = new JPanel();
			if (lighterColor != null)
				myProxyPanel.setBackground(lighterColor);
			GridBagConstraints validComboBoxConstraints = new GridBagConstraints();
			validComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
			validComboBoxConstraints.gridy = 2;
			validComboBoxConstraints.weightx = 0.0;
			validComboBoxConstraints.insets = new Insets(0, 0, 15, 20);
			validComboBoxConstraints.gridwidth = 1;
			validComboBoxConstraints.gridx = 2;
			GridBagConstraints validLabelConstraints = new GridBagConstraints();
			validLabelConstraints.gridx = 2;
			validLabelConstraints.insets = new Insets(20, 0, 5, 20);
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
			GridBagConstraints proxyPassphraseFieldConstraints = new GridBagConstraints();
			proxyPassphraseFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
			proxyPassphraseFieldConstraints.gridy = 2;
			proxyPassphraseFieldConstraints.weightx = 1.0;
			proxyPassphraseFieldConstraints.insets = new Insets(0, 0, 15, 20);
			proxyPassphraseFieldConstraints.gridwidth = 1;
			proxyPassphraseFieldConstraints.gridx = 1;
			GridBagConstraints usernameTextFieldConstraints = new GridBagConstraints();
			usernameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
			usernameTextFieldConstraints.gridy = 1;
			usernameTextFieldConstraints.weightx = 1.0;
			usernameTextFieldConstraints.insets = new Insets(10, 0, 15, 20);
			usernameTextFieldConstraints.gridx = 1;
			GridBagConstraints gridPassphraseLabelConstraints = new GridBagConstraints();
			gridPassphraseLabelConstraints.gridx = 0;
			gridPassphraseLabelConstraints.insets = new Insets(0, 20, 20, 20);
			gridPassphraseLabelConstraints.anchor = GridBagConstraints.EAST;
			gridPassphraseLabelConstraints.gridy = 3;
			gridPassphraseLabel = new JLabel();
			gridPassphraseLabel.setText("Key passphrase");
			GridBagConstraints proxyPassphraseLabelConstraints = new GridBagConstraints();
			proxyPassphraseLabelConstraints.gridx = 0;
			proxyPassphraseLabelConstraints.insets = new Insets(0, 20, 15, 20);
			proxyPassphraseLabelConstraints.anchor = GridBagConstraints.EAST;
			proxyPassphraseLabelConstraints.gridy = 2;
			proxyPassphraseLabel = new JLabel();
			proxyPassphraseLabel.setText("MyProxy password");
			GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
			usernameLabelConstraints.gridx = 0;
			usernameLabelConstraints.insets = new Insets(10, 20, 15, 20);
			usernameLabelConstraints.anchor = GridBagConstraints.EAST;
			usernameLabelConstraints.gridy = 1;
			usernameLabel = new JLabel();
			usernameLabel.setText("Username");
			voLabel = new JLabel();
			voLabel.setText("VO");
			myProxyPanel.setLayout(new GridBagLayout());
			// myProxyPanel.setBackground(new Color(245, 245, 245));
			myProxyPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEmptyBorder(15, 0, 0, 0),
					"Proxy details", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 14), null));
			myProxyPanel.add(usernameLabel, usernameLabelConstraints);
			myProxyPanel.add(proxyPassphraseLabel,
					proxyPassphraseLabelConstraints);
			myProxyPanel.add(gridPassphraseLabel,
					gridPassphraseLabelConstraints);
			myProxyPanel.add(getUsernameTextField(),
					usernameTextFieldConstraints);
			myProxyPanel.add(getProxyPassphraseField(),
					proxyPassphraseFieldConstraints);
			myProxyPanel.add(getGridPassphraseField(),
					gridPassphraseFieldConstraints);
			myProxyPanel.add(validLabel, validLabelConstraints);
			myProxyPanel.add(getValidComboBox(), validComboBoxConstraints);
		}
		return myProxyPanel;
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
			groupComboBox
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							myLogger.debug("Action command: "
									+ e.getActionCommand());
							if (getAdvancedCheckBox().isSelected()
									&& !user_typed
									&& groupComboBoxModel.getSelectedItem() != null) {

								// String selected = (String) groupComboBoxModel
								// .getSelectedItem();
								// String parsed = selected.substring(1);
								// String replaced = parsed.replace('/', '_');
								// getProxyNameTextField().setText(replaced);
								suggestProxyName();
							}
						}
					});
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

	/**
	 * This method initializes proxyNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getProxyNameTextField() {
		if (proxyNameTextField == null) {
			proxyNameTextField = new JTextField();
			proxyNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					user_typed = true;
				}
			});
		}
		return proxyNameTextField;
	}

	/**
	 * This method initializes advancedPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAdvancedPanel() {
		if (advancedPanel == null) {
			GridBagConstraints anonymousCheckBoxConstraints = new GridBagConstraints();
			anonymousCheckBoxConstraints.gridx = 2;
			anonymousCheckBoxConstraints.gridwidth = 1;
			anonymousCheckBoxConstraints.anchor = GridBagConstraints.EAST;
			anonymousCheckBoxConstraints.insets = new Insets(0, 0, 10, 20);
			anonymousCheckBoxConstraints.weightx = 1.0;
			anonymousCheckBoxConstraints.gridy = 3;
			GridBagConstraints delegatedCredsComboBoxConstraints = new GridBagConstraints();
			delegatedCredsComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
			delegatedCredsComboBoxConstraints.gridy = 3;
			delegatedCredsComboBoxConstraints.weightx = 1.0;
			delegatedCredsComboBoxConstraints.insets = new Insets(0, 0, 20, 20);
			delegatedCredsComboBoxConstraints.gridx = 1;
			GridBagConstraints delegatedCredsLabel2Constraints = new GridBagConstraints();
			delegatedCredsLabel2Constraints.gridx = 0;
			delegatedCredsLabel2Constraints.insets = new Insets(0, 20, 20, 20);
			delegatedCredsLabel2Constraints.gridwidth = 1;
			delegatedCredsLabel2Constraints.gridy = 3;
			delegatedCredsLabel2 = new JLabel();
			delegatedCredsLabel2
					.setText("Max. delegated proxy lifetime (days)");
			GridBagConstraints proxyNameTextFieldConstraints = new GridBagConstraints();
			proxyNameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
			proxyNameTextFieldConstraints.gridx = 1;
			proxyNameTextFieldConstraints.gridy = 2;
			proxyNameTextFieldConstraints.weightx = 1.0;
			proxyNameTextFieldConstraints.gridwidth = 3;
			proxyNameTextFieldConstraints.insets = new Insets(0, 0, 15, 20);
			GridBagConstraints proxyNameLabelConstraints = new GridBagConstraints();
			proxyNameLabelConstraints.insets = new Insets(0, 20, 15, 20);
			proxyNameLabelConstraints.gridy = 2;
			proxyNameLabelConstraints.anchor = GridBagConstraints.WEST;
			proxyNameLabelConstraints.gridx = 0;
			GridBagConstraints advancedCheckBoxConstraints = new GridBagConstraints();
			advancedCheckBoxConstraints.anchor = GridBagConstraints.NORTHWEST;
			advancedCheckBoxConstraints.insets = new Insets(10, 10, 15, 0);
			advancedCheckBoxConstraints.gridwidth = 2;
			advancedCheckBoxConstraints.gridx = 0;
			advancedCheckBoxConstraints.gridy = 0;
			advancedCheckBoxConstraints.weighty = 1.0;
			advancedCheckBoxConstraints.weightx = 1.0;
			advancedPanel = new JPanel();
			if (lighterColor != null)
				advancedPanel.setBackground(lighterColor);
			advancedPanel.setLayout(new GridBagLayout());
			// advancedPanel.setBackground(new Color(245, 245, 245));
			advancedPanel
					.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			advancedPanel.add(getAdvancedCheckBox(),
					advancedCheckBoxConstraints);
			advancedPanel.add(proxyNameLabel, proxyNameLabelConstraints);
			advancedPanel.add(getProxyNameTextField(),
					proxyNameTextFieldConstraints);
			advancedPanel.add(delegatedCredsLabel2,
					delegatedCredsLabel2Constraints);
			advancedPanel.add(getDelegatedCredsComboBox(),
					delegatedCredsComboBoxConstraints);
			advancedPanel.add(getAnonymousCheckBox(),
					anonymousCheckBoxConstraints);
		}
		return advancedPanel;
	}

	/**
	 * This method initializes advancedCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getAdvancedCheckBox() {
		if (advancedCheckBox == null) {
			advancedCheckBox = new JCheckBox();
			advancedCheckBox.setText("Advanced MyProxy options");
			advancedCheckBox.setFont(new Font("Dialog", Font.BOLD, 12));
			advancedCheckBox.setOpaque(false);
			advancedCheckBox.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						deactivateAdvancedPanel();
					} else {
						activateAdvancedPanel();
					}
				}

			});
		}
		return advancedCheckBox;
	}

	/**
	 * This method initializes delegatedCredsComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDelegatedCredsComboBox() {
		if (delegatedCredsComboBox == null) {
			delegatedProxyComboBoxModel = new DefaultComboBoxModel(GrixProperty
					.getString("default.myproxy.delegated.proxy.lifetimes")
					.split(","));
			delegatedProxyComboBoxModel.setSelectedItem(GrixProperty
					.getString("default.myproxy.delegated.proxy.lifetime"));
			delegatedCredsComboBox = new JComboBox(delegatedProxyComboBoxModel);
			delegatedCredsComboBox.setEditable(true);
		}
		return delegatedCredsComboBox;
	}

	public void gridProxyStatusChanged(GridProxyEvent e) {

		if (e.getStatus() != GridProxy.INITIALIZED) {
			groupComboBoxModel.removeAllElements();
			voComboBoxModel.removeAllElements();
			getGroupComboBox().setEnabled(false);
			getVoComboBox().setEnabled(false);
			getVomsCheckBox().setEnabled(false);
		} else {
			getGroupComboBox().setEnabled(true);
			getVoComboBox().setEnabled(true);
			getVomsCheckBox().setEnabled(true);
		}

	}

	/**
	 * This method initializes anonymousCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getAnonymousCheckBox() {
		if (anonymousCheckBox == null) {
			anonymousCheckBox = new JCheckBox();
			anonymousCheckBox.setOpaque(false);
			anonymousCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			anonymousCheckBox
					.setActionCommand("Do not allow anonymous retrieval");
			anonymousCheckBox.setText("Do not allow anonymous retrieval");
		}
		return anonymousCheckBox;
	}

	/**
	 * @return
	 */
	protected JButton getButton() {
		if (button == null) {
			button = new JButton();
			button.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					SimpleInfoDialog dialog = new SimpleInfoDialog(null,
							"myproxy_info", Color.white);
					dialog.setVisible(true);
				}
			});
			button.setText("Help");
		}
		return button;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
