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
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.vpac.common.control.Helpers;
import org.vpac.common.model.GlobusLocations;
import org.vpac.grix.model.certificate.Certificate;

public class CertificateInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Certificate cert = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JLabel jLabel3 = null;

	private JLabel jLabel4 = null;

	private JLabel jLabel5 = null;

	private JTextField countryTextField = null;

	private JTextField organisationTextField = null;

	private JTextField organisationUnitTextField = null;

	private JTextField nameTextField = null;

	private JTextField emailTextField = null;

	private JLabel jLabel6 = null;

	private JTextField expiresTextField = null;

	private JLabel jLabel7 = null;

	private JTextField dnTextField = null;

	private JPopupMenu jPopupMenu = null; // @jve:decl-index=0:visual-constraint="263,437"

	private JMenuItem jMenuItem = null;

	/**
	 * This is the default constructor
	 */
	public CertificateInfoPanel(Certificate cert) {
		super();
		this.cert = cert;
		initialize();
		fillInformation();
	}

	private void fillInformation() {
		if (cert != null) {
			this.getCountryTextField().setText(this.cert.getC());
			this.getOrganisationTextField().setText(this.cert.getO());
			this.getOrganisationUnitTextField().setText(this.cert.getOu());
			this.getNameTextField().setText(cert.getCn());
			this.getEmailTextField().setText(cert.getEmail());
			this.getExpiresTextField().setText(cert.getEnddate());
			// this.getIssuerTextField().setText(cert.getIssuer());
			this.getDnTextField().setText(cert.getDn());
			this.revalidate();
		}
	}

	public void refresh() {
		try {
			cert = new Certificate(GlobusLocations.defaultLocations()
					.getUserCert());
		} catch (IOException e) {
			// TODO ignore for now
		} catch (GeneralSecurityException e) {
			// TODO ignore for now
		}
		fillInformation();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.fill = GridBagConstraints.BOTH;
		gridBagConstraints8.gridy = 8;
		gridBagConstraints8.weightx = 1.0;
		gridBagConstraints8.gridwidth = 2;
		gridBagConstraints8.insets = new Insets(0, 20, 0, 20);
		gridBagConstraints8.gridx = 0;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.anchor = GridBagConstraints.EAST;
		gridBagConstraints7.insets = new Insets(0, 20, 10, 0);
		gridBagConstraints7.gridy = 7;
		jLabel7 = new JLabel();
		jLabel7.setText("complete DN");
		GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
		gridBagConstraints61.fill = GridBagConstraints.BOTH;
		gridBagConstraints61.gridy = 6;
		gridBagConstraints61.weightx = 1.0;
		gridBagConstraints61.insets = new Insets(10, 15, 15, 20);
		gridBagConstraints61.gridx = 1;
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 0;
		gridBagConstraints51.anchor = GridBagConstraints.EAST;
		gridBagConstraints51.insets = new Insets(10, 20, 15, 0);
		gridBagConstraints51.gridy = 6;
		jLabel6 = new JLabel();
		jLabel6.setText("expires");
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.fill = GridBagConstraints.BOTH;
		gridBagConstraints41.gridy = 5;
		gridBagConstraints41.weightx = 1.0;
		gridBagConstraints41.insets = new Insets(0, 15, 15, 20);
		gridBagConstraints41.gridx = 1;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.fill = GridBagConstraints.BOTH;
		gridBagConstraints31.gridy = 4;
		gridBagConstraints31.weightx = 1.0;
		gridBagConstraints31.insets = new Insets(0, 15, 15, 20);
		gridBagConstraints31.gridx = 1;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridy = 3;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.insets = new Insets(0, 15, 15, 20);
		gridBagConstraints21.gridx = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 2;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(0, 15, 15, 20);
		gridBagConstraints11.gridx = 1;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.gridy = 1;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.insets = new Insets(0, 15, 15, 20);
		gridBagConstraints6.gridx = 1;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.EAST;
		gridBagConstraints5.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints5.gridy = 5;
		jLabel5 = new JLabel();
		jLabel5.setText("Email");
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.EAST;
		gridBagConstraints4.insets = new Insets(0, 15, 15, 0);
		gridBagConstraints4.gridy = 4;
		jLabel4 = new JLabel();
		jLabel4.setText("Name");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.anchor = GridBagConstraints.EAST;
		gridBagConstraints3.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints3.gridy = 3;
		jLabel3 = new JLabel();
		jLabel3.setText("Organisation Unit");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints2.gridy = 2;
		jLabel2 = new JLabel();
		jLabel2.setText("Organisation");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.EAST;
		gridBagConstraints1.insets = new Insets(0, 20, 15, 0);
		gridBagConstraints1.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("Country");
		this.setSize(424, 410);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(245, 245, 245));
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEmptyBorder(15, 0, 0, 0),
				"Information in your current certificate",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 14), new Color(51, 51, 51)));
		this.setPreferredSize(new Dimension(424, 410));
		this.add(jLabel1, gridBagConstraints1);
		this.add(jLabel2, gridBagConstraints2);
		this.add(jLabel3, gridBagConstraints3);
		this.add(jLabel4, gridBagConstraints4);
		this.add(jLabel5, gridBagConstraints5);
		this.add(getCountryTextField(), gridBagConstraints6);
		this.add(getOrganisationTextField(), gridBagConstraints11);
		this.add(getOrganisationUnitTextField(), gridBagConstraints21);
		this.add(getNameTextField(), gridBagConstraints31);
		this.add(getEmailTextField(), gridBagConstraints41);
		this.add(jLabel6, gridBagConstraints51);
		this.add(getExpiresTextField(), gridBagConstraints61);
		this.add(jLabel7, gridBagConstraints7);
		this.add(getDnTextField(), gridBagConstraints8);
	}

	/**
	 * This method initializes countryTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCountryTextField() {
		if (countryTextField == null) {
			countryTextField = new JTextField();
			countryTextField.setHorizontalAlignment(JTextField.CENTER);
			countryTextField.setBackground(Color.white);
			countryTextField.setEditable(false);
		}
		return countryTextField;
	}

	/**
	 * This method initializes organisationTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOrganisationTextField() {
		if (organisationTextField == null) {
			organisationTextField = new JTextField();
			organisationTextField.setEditable(false);
			organisationTextField.setBackground(Color.white);
			organisationTextField.setHorizontalAlignment(JTextField.CENTER);
		}
		return organisationTextField;
	}

	/**
	 * This method initializes organisationUnitTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOrganisationUnitTextField() {
		if (organisationUnitTextField == null) {
			organisationUnitTextField = new JTextField();
			organisationUnitTextField.setHorizontalAlignment(JTextField.CENTER);
			organisationUnitTextField.setBackground(Color.white);
			organisationUnitTextField.setEditable(false);
		}
		return organisationUnitTextField;
	}

	/**
	 * This method initializes nameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNameTextField() {
		if (nameTextField == null) {
			nameTextField = new JTextField();
			nameTextField.setEditable(false);
			nameTextField.setHorizontalAlignment(JTextField.CENTER);
			nameTextField.setBackground(Color.white);
		}
		return nameTextField;
	}

	/**
	 * This method initializes emailTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getEmailTextField() {
		if (emailTextField == null) {
			emailTextField = new JTextField();
			emailTextField.setHorizontalAlignment(JTextField.CENTER);
			emailTextField.setBackground(Color.white);
			emailTextField.setEditable(false);
		}
		return emailTextField;
	}

	/**
	 * This method initializes expiresTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExpiresTextField() {
		if (expiresTextField == null) {
			expiresTextField = new JTextField();
			expiresTextField.setEditable(false);
			expiresTextField.setHorizontalAlignment(JTextField.CENTER);
			expiresTextField.setBackground(Color.white);
		}
		return expiresTextField;
	}

	/**
	 * This method initializes dnTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDnTextField() {
		if (dnTextField == null) {
			dnTextField = new JTextField();
			dnTextField.setHorizontalAlignment(JTextField.CENTER);
			dnTextField.addMouseListener(new java.awt.event.MouseListener() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (SwingUtilities.isRightMouseButton(e)) {
						getJPopupMenu().show(e.getComponent(), e.getX(),
								e.getY());
					}
				}

				public void mousePressed(java.awt.event.MouseEvent e) {
				}

				public void mouseReleased(java.awt.event.MouseEvent e) {
				}

				public void mouseEntered(java.awt.event.MouseEvent e) {
				}

				public void mouseExited(java.awt.event.MouseEvent e) {
				}
			});
			dnTextField.setBackground(Color.white);

		}
		return dnTextField;
	}

	/**
	 * This method initializes jPopupMenu
	 * 
	 * @return javax.swing.JPopupMenu
	 */
	private JPopupMenu getJPopupMenu() {
		if (jPopupMenu == null) {
			jPopupMenu = new JPopupMenu();
			jPopupMenu.add(getJMenuItem());
		}
		return jPopupMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Copy DN to clipboard");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Helpers.setClipboard(cert.getDn());
				}
			});
		}
		return jMenuItem;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
