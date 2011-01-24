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

package org.vpac.grix.view.swing.certificate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.grix.control.utils.UserProperty;
import org.vpac.grix.control.utils.Utils;
import org.vpac.grix.model.certificate.Certificate;
import org.vpac.grix.view.swing.Grix;

public class RequestUserCertificatePanel extends JPanel {

	private JLabel surnameLabel;
	static final Logger myLogger = Logger
			.getLogger(RequestUserCertificatePanel.class.getName());

	private static final Dimension STANDARD_DIMENSION = new Dimension(4, 24);

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JLabel jLabel6 = null;
	private JTextField firstNameTextField = null;
	private JTextField emailTextField = null;
	private JTextField organisationTextField = null;
	private JTextField countryTextField = null;
	private JComboBox ouComboBox = null;
	private DefaultComboBoxModel ouComboBoxModel = null;
	private JPasswordField passphrase1Field = null;
	private JPasswordField passphrase2Field = null;

	private JLabel jLabel7 = null;

	private JTextField telephoneTextField = null;

	private JTextField lastNameTextField = null;

	/**
	 * This is the default constructor
	 */
	public RequestUserCertificatePanel() {
		super();
		initialize();
	}

	public RequestUserCertificatePanel(Certificate cert) {
		super();
		initialize();
		prefill(cert);
	}

	public void prefill(Certificate cert) {
		// if ( UserProperty.getProperty("FIRST_NAME") != null &&
		// UserProperty.getProperty("LAST_NAME") != null ){
		// getFirstNameTextField().setText(UserProperty.getProperty("FIRST_NAME"));
		// getLastNameTextField().setText(UserProperty.getProperty("LAST_NAME"));
		// } else {
		String[] name = Utils.parseNameField(cert.getCn());
		getFirstNameTextField().setText(name[0]);
		getLastNameTextField().setText(name[1]);
		// }
		if (cert.getEmail() != null)
			getEmailTextField().setText(cert.getEmail());
		if (UserProperty.getProperty("PHONE") != null)
			getTelephoneTextField().setText(UserProperty.getProperty("PHONE"));
		if (cert.getOu() != null) {
			if (ouComboBoxModel.getIndexOf(cert.getOu()) != -1)
				ouComboBoxModel.setSelectedItem(cert.getOu());
			else {
				ouComboBoxModel.addElement(cert.getOu());
				ouComboBoxModel.setSelectedItem(cert.getOu());
			}
		}
		if (cert.getO() != null)
			getOrganisationTextField().setText(cert.getO());
		if (cert.getC() != null)
			getCountryTextField().setText(cert.getC());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints31.gridy = 1;
		gridBagConstraints31.weightx = 1.0;
		gridBagConstraints31.insets = new Insets(0, 0, 15, 20);
		gridBagConstraints31.gridx = 3;
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints22.gridy = 3;
		gridBagConstraints22.weightx = 1.0;
		gridBagConstraints22.insets = new Insets(0, 20, 15, 20);
		gridBagConstraints22.gridwidth = 3;
		gridBagConstraints22.gridx = 1;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.anchor = GridBagConstraints.EAST;
		gridBagConstraints13.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints13.gridy = 3;
		jLabel7 = new JLabel();
		jLabel7.setText("Telephone:");
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.gridy = 8;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.insets = new Insets(0, 20, 0, 20);
		gridBagConstraints21.gridwidth = 3;
		gridBagConstraints21.gridx = 1;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints12.gridy = 7;
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.insets = new Insets(0, 20, 15, 20);
		gridBagConstraints12.gridwidth = 3;
		gridBagConstraints12.gridx = 1;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.fill = GridBagConstraints.BOTH;
		gridBagConstraints9.gridy = 4;
		gridBagConstraints9.weightx = 1.0;
		gridBagConstraints9.insets = new Insets(0, 20, 15, 20);
		gridBagConstraints9.gridwidth = 3;
		gridBagConstraints9.gridx = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 6;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(0, 20, 35, 20);
		gridBagConstraints11.gridwidth = 3;
		gridBagConstraints11.gridx = 1;
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.fill = GridBagConstraints.BOTH;
		gridBagConstraints10.gridy = 5;
		gridBagConstraints10.weightx = 1.0;
		gridBagConstraints10.insets = new Insets(0, 20, 15, 20);
		gridBagConstraints10.gridwidth = 3;
		gridBagConstraints10.gridx = 1;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.fill = GridBagConstraints.BOTH;
		gridBagConstraints8.gridy = 2;
		gridBagConstraints8.weightx = 1.0;
		gridBagConstraints8.insets = new Insets(0, 20, 15, 20);
		gridBagConstraints8.gridwidth = 3;
		gridBagConstraints8.gridx = 1;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.fill = GridBagConstraints.BOTH;
		gridBagConstraints7.gridy = 1;
		gridBagConstraints7.weightx = 1.0;
		gridBagConstraints7.insets = new Insets(0, 20, 15, 10);
		gridBagConstraints7.gridx = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.EAST;
		gridBagConstraints6.insets = new Insets(0, 20, 0, 0);
		gridBagConstraints6.gridy = 8;
		jLabel6 = new JLabel();
		jLabel6.setText("confirm:");
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.EAST;
		gridBagConstraints5.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints5.gridy = 7;
		jLabel5 = new JLabel();
		jLabel5.setText("Passphrase:");
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.insets = new Insets(0, 20, 35, 0);
		gridBagConstraints4.gridy = 6;
		jLabel4 = new JLabel();
		jLabel4.setText("Country:");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.anchor = GridBagConstraints.EAST;
		gridBagConstraints3.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints3.gridy = 5;
		jLabel3 = new JLabel();
		jLabel3.setText("Organisation:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints2.gridy = 4;
		jLabel2 = new JLabel();
		jLabel2.setText("Organisation Unit:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		gridBagConstraints1.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints1.gridy = 2;
		jLabel1 = new JLabel();
		jLabel1.setText("Email:");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints.gridy = 1;
		jLabel = new JLabel();
		jLabel.setText("Given name:");
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 7 };
		this.setLayout(gridBagLayout);
		this.setBackground(new Color(245, 245, 245));
		this.setSize(new Dimension(477, 497));
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEmptyBorder(15, 0, 0, 0),
				"Please provide your personal information",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 14), new Color(51, 51, 51)));
		this.add(jLabel, gridBagConstraints);
		this.add(jLabel1, gridBagConstraints1);
		this.add(jLabel2, gridBagConstraints2);
		this.add(jLabel3, gridBagConstraints3);
		this.add(jLabel4, gridBagConstraints4);
		this.add(jLabel5, gridBagConstraints5);
		this.add(jLabel6, gridBagConstraints6);
		this.add(getFirstNameTextField(), gridBagConstraints7);
		this.add(getEmailTextField(), gridBagConstraints8);
		this.add(getOrganisationTextField(), gridBagConstraints10);
		this.add(getCountryTextField(), gridBagConstraints11);
		this.add(getOuComboBox(), gridBagConstraints9);
		this.add(getPassphrase1Field(), gridBagConstraints12);
		this.add(getPassphrase2Field(), gridBagConstraints21);
		this.add(jLabel7, gridBagConstraints13);
		this.add(getTelephoneTextField(), gridBagConstraints22);
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(0, 0, 15, 10);
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.gridx = 2;
		add(getSurnameLabel(), gridBagConstraints_1);
		this.add(getLastNameTextField(), gridBagConstraints31);
	}

	/**
	 * This method initializes nameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getFirstNameTextField() {
		if (firstNameTextField == null) {
			firstNameTextField = new JTextField();
			firstNameTextField.setPreferredSize(STANDARD_DIMENSION);
			firstNameTextField.setToolTipText(Grix.getMessages().getString(
					"tooltip.firstNameTextField"));
		}
		return firstNameTextField;
	}

	/**
	 * This method initializes emailTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getEmailTextField() {
		if (emailTextField == null) {
			emailTextField = new JTextField();
			emailTextField.setPreferredSize(STANDARD_DIMENSION);
			emailTextField
					.addCaretListener(new javax.swing.event.CaretListener() {

						public void caretUpdate(javax.swing.event.CaretEvent e) {

							// check for default organisation unit
							String domain = emailTextField.getText();
							if (domain.indexOf("@") == -1)
								return;
							myLogger.debug("Trying to get default ou for substring: "
									+ domain.substring(domain.indexOf("@") + 1));
							String index = Utils.defaultOrganizationUnit(domain
									.substring(domain.indexOf("@") + 1));

							if (index != null)
								ouComboBoxModel.setSelectedItem(index);

						}
					});
			emailTextField.setToolTipText(Grix.getMessages().getString(
					"tooltip.emailTextField"));

		}
		return emailTextField;
	}

	/**
	 * This method initializes organisationTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOrganisationTextField() {
		if (organisationTextField == null) {
			organisationTextField = new JTextField();
			organisationTextField.setPreferredSize(STANDARD_DIMENSION);
			organisationTextField.setText(GrixProperty
					.getString("default.organisation"));
			if (!"yes".equals(GrixProperty.getString(
					"allow.change.organisation").toLowerCase())) {
				organisationTextField.setEditable(false);
				organisationTextField.setFocusable(false);
			}
			organisationTextField.setToolTipText(Grix.getMessages().getString(
					"tooltip.organisationTextField"));
		}
		return organisationTextField;
	}

	/**
	 * This method initializes countryTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCountryTextField() {
		if (countryTextField == null) {
			countryTextField = new JTextField();
			countryTextField.setPreferredSize(STANDARD_DIMENSION);
			countryTextField.setText(GrixProperty.getString("default.country"));
			countryTextField.setToolTipText(Grix.getMessages().getString(
					"tooltip.countryTextField"));
			if (!"yes".equals(GrixProperty.getString("allow.change.country")
					.toLowerCase())) {
				countryTextField.setEditable(false);
				countryTextField.setFocusable(false);
			}
		}
		return countryTextField;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getOuComboBox() {
		if (ouComboBox == null) {
			ouComboBox = new JComboBox();
			ouComboBox.setPreferredSize(STANDARD_DIMENSION);
			String[] sites = { "Your site" };

			// fill with properties of resource.properties file if possible
			sites = GrixProperty.getString("default.organisation.units").split(
					",");

			ouComboBoxModel = new DefaultComboBoxModel(sites);

			ouComboBox = new JComboBox(ouComboBoxModel);
			try {
				if (GrixProperty.getString("default.organisation.unit") == null
						|| "no".equals(GrixProperty
								.getString("default.organisation.unit"))) {
					ouComboBoxModel.insertElementAt("", 0);
					ouComboBoxModel.setSelectedItem("");
				} else {
					ouComboBoxModel.setSelectedItem(GrixProperty
							.getString("default.organisation.unit"));
				}
			} catch (IllegalArgumentException iae) {
				// does not matter
			}
			if (!"yes".equals(GrixProperty.getString(
					"allow.change.organisation.unit").toLowerCase())) {
				ouComboBox.setEnabled(false);
				ouComboBox.setFocusable(false);
			}
			ouComboBox.setBackground(Color.white);
			ouComboBox.setEditable(true);
			ouComboBox.setToolTipText(Grix.getMessages().getString(
					"tooltip.ouComboBox"));
		}
		return ouComboBox;
	}

	/**
	 * This method initializes passphrase1Field
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getPassphrase1Field() {
		if (passphrase1Field == null) {
			passphrase1Field = new JPasswordField();
			passphrase1Field.setPreferredSize(STANDARD_DIMENSION);
			passphrase1Field.setToolTipText(Grix.getMessages().getString(
					"tooltip.passphrase1Field"));
		}
		return passphrase1Field;
	}

	/**
	 * This method initializes passphrase2Field
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getPassphrase2Field() {
		if (passphrase2Field == null) {
			passphrase2Field = new JPasswordField();
			passphrase2Field.setPreferredSize(STANDARD_DIMENSION);
			passphrase2Field.setToolTipText(Grix.getMessages().getString(
					"tooltip.passphrase2Field"));
		}
		return passphrase2Field;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTelephoneTextField() {
		if (telephoneTextField == null) {
			telephoneTextField = new JTextField();
			telephoneTextField.setPreferredSize(STANDARD_DIMENSION);
			telephoneTextField.setToolTipText(Grix.getMessages().getString(
					"tooltip.telephoneTextField"));
		}
		return telephoneTextField;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getLastNameTextField() {
		if (lastNameTextField == null) {
			lastNameTextField = new JTextField();
			lastNameTextField.setPreferredSize(STANDARD_DIMENSION);
			lastNameTextField.setToolTipText(Grix.getMessages().getString(
					"tooltip.lastNameTextField"));
		}
		return lastNameTextField;
	}

	public String getC() {
		return getCountryTextField().getText().trim();
	}

	public String getCN() {
		return (getFirstNameTextField().getText() + " " + getLastNameTextField()
				.getText()).trim();
	}

	public String getFirstName() {
		return getFirstNameTextField().getText().trim();
	}

	public String getLastName() {
		return getLastNameTextField().getText().trim();
	}

	public String getEmail() {
		return getEmailTextField().getText().trim();
	}

	public String getPhone() {
		return getTelephoneTextField().getText().trim();
	}

	public String getO() {
		return getOrganisationTextField().getText().trim();
	}

	public String getOU() {
		return getOuComboBox().getSelectedItem().toString().trim();
	}

	public char[] getPassphrase() {
		return getPassphrase1Field().getPassword();
	}

	public char[] getPassphrase2() {
		return getPassphrase2Field().getPassword();
	}

	public void clearPassphrases() {
		getPassphrase1Field().setText("");
		getPassphrase2Field().setText("");
	}

	public void lockInput() {
		getCountryTextField().setEnabled(false);
		getOrganisationTextField().setEnabled(false);
		getOuComboBox().setEnabled(false);
		getPassphrase1Field().setEnabled(false);
		getPassphrase2Field().setEnabled(false);
		getTelephoneTextField().setEnabled(false);
		getFirstNameTextField().setEnabled(false);
		getLastNameTextField().setEnabled(false);
		getEmailTextField().setEnabled(false);

	}

	public void unlockInput() {

		if ("yes".equals(GrixProperty.getString("allow.change.country")
				.toLowerCase()))
			countryTextField.setEditable(true);
		if ("yes".equals(GrixProperty.getString("allow.change.organisation")
				.toLowerCase()))
			getOrganisationTextField().setEnabled(true);
		if ("yes".equals(GrixProperty.getString(
				"allow.change.organisation.unit").toLowerCase()))
			getOuComboBox().setEnabled(true);
		getCountryTextField().setEnabled(true);
		getPassphrase1Field().setEnabled(true);
		getPassphrase2Field().setEnabled(true);
		getTelephoneTextField().setEnabled(true);
		getFirstNameTextField().setEnabled(true);
		getLastNameTextField().setEnabled(true);
		getEmailTextField().setEnabled(true);
	}

	/**
	 * @return
	 */
	protected JLabel getSurnameLabel() {
		if (surnameLabel == null) {
			surnameLabel = new JLabel();
			surnameLabel.setText("Surname:");
		}
		return surnameLabel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
