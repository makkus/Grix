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

package org.vpac.grix.view.swing.vomrs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.vpac.common.view.swing.messagePanel.SimpleInfoDialog;
import org.vpac.common.view.swing.messagePanel.SimpleMessageDialog;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.qc.model.query.InfoQuery;
import org.vpac.vomrs.model.VomrsClient;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.VomsStatusEvent;
import org.vpac.voms.control.VomsesStatusListener;

public class MembershipInfoPanel extends JPanel implements VomsesStatusListener {

	private static final long serialVersionUID = 1L;

	static final Logger myLogger = Logger.getLogger(MembershipInfoPanel.class
			.getName());

	private String[] info = null;

	private InfoQuery infoQuery = null;

	private JLabel rolesLabel = null;

	private JTextField expiresTextfield = null;

	private JTextField rolesTextField = null;

	private JLabel expiresLabel = null;

	private JButton detailsButton = null;

	private JLabel statusLabel = null;

	private JTextField statusTextField = null;

	private SimpleMessagePanel groupsPane = null;

	private JLabel groupsLabel = null;

	private Voms voms = null;
	private Color lighterColor = null;

	private JButton refreshButton = null;

	private JButton jButton = null;

	/**
	 * This is the default constructor
	 */
	public MembershipInfoPanel(Voms voms) {
		super();
		this.voms = voms;
		info = (String[]) voms.getInfoQuery().getResult();
		initialize();
		fillInformation();
		LocalVomses.addStatusListener(this);
	}

	public MembershipInfoPanel(Voms voms, Color lighterColor) {
		super();
		this.voms = voms;
		this.lighterColor = lighterColor;
		info = (String[]) voms.getInfoQuery().getResult();
		initialize();
		fillInformation();
		LocalVomses.addStatusListener(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 1;
		gridBagConstraints13.insets = new Insets(0, 20, 0, 0);
		gridBagConstraints13.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints13.gridy = 10;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 3;
		gridBagConstraints12.weightx = 0.0;
		gridBagConstraints12.anchor = GridBagConstraints.EAST;
		gridBagConstraints12.insets = new Insets(0, 20, 20, 20);
		gridBagConstraints12.gridy = 10;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(0, 20, 0, 0);
		gridBagConstraints2.gridy = 3;
		groupsLabel = new JLabel();
		groupsLabel.setText("Groups");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 3;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.insets = new Insets(0, 20, 20, 20);
		gridBagConstraints11.gridwidth = 3;
		gridBagConstraints11.gridx = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 2;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(0, 20, 15, 20);
		gridBagConstraints1.gridwidth = 3;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(0, 0, 15, 0);
		gridBagConstraints.gridy = 2;
		statusLabel = new JLabel();
		statusLabel.setText("Status");
		this.setLayout(new GridBagLayout());
		expiresLabel = new JLabel();
		expiresLabel.setText("Expires");
		GridBagConstraints detailsButtonConstraints = new GridBagConstraints();
		detailsButtonConstraints.gridx = 2;
		detailsButtonConstraints.anchor = GridBagConstraints.EAST;
		detailsButtonConstraints.insets = new Insets(0, 0, 20, 0);
		detailsButtonConstraints.weightx = 1.0;
		detailsButtonConstraints.gridy = 10;
		GridBagConstraints rolesTextFieldConstraints = new GridBagConstraints();
		rolesTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		rolesTextFieldConstraints.gridy = 0;
		rolesTextFieldConstraints.weightx = 1.0;
		rolesTextFieldConstraints.insets = new Insets(20, 20, 10, 20);
		rolesTextFieldConstraints.gridwidth = 3;
		rolesTextFieldConstraints.gridx = 1;
		GridBagConstraints rolesLabelConstraints = new GridBagConstraints();
		rolesLabelConstraints.gridx = 0;
		rolesLabelConstraints.insets = new Insets(20, 0, 10, 0);
		rolesLabelConstraints.anchor = GridBagConstraints.EAST;
		rolesLabelConstraints.gridwidth = 1;
		rolesLabelConstraints.gridy = 0;
		rolesLabel = new JLabel();
		rolesLabel.setText("Role");
		GridBagConstraints expiresTextFieldConstraints = new GridBagConstraints();
		expiresTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		expiresTextFieldConstraints.gridy = 1;
		expiresTextFieldConstraints.weightx = 1.0;
		expiresTextFieldConstraints.insets = new Insets(0, 20, 15, 20);
		expiresTextFieldConstraints.gridwidth = 3;
		expiresTextFieldConstraints.gridx = 1;
		GridBagConstraints expiresLabelConstraints = new GridBagConstraints();
		expiresLabelConstraints.gridx = 0;
		expiresLabelConstraints.anchor = GridBagConstraints.EAST;
		expiresLabelConstraints.insets = new Insets(0, 20, 15, 0);
		expiresLabelConstraints.gridy = 1;
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEmptyBorder(15, 0, 0, 0),
				"Information about your group membership",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Dialog", Font.BOLD, 14), new Color(51, 51, 51)));
		this.setSize(new Dimension(610, 346));
		if (lighterColor != null)
			this.setBackground(lighterColor);
		this.add(expiresLabel, expiresLabelConstraints);
		this.add(getExpiresTextfield(), expiresTextFieldConstraints);
		this.add(rolesLabel, rolesLabelConstraints);
		this.add(getRolesTextField(), rolesTextFieldConstraints);
		this.add(getDetailsButton(), detailsButtonConstraints);
		this.add(statusLabel, gridBagConstraints);
		this.add(getStatusTextField(), gridBagConstraints1);
		this.add(getGroupsPanel(), gridBagConstraints11);
		this.add(groupsLabel, gridBagConstraints2);
		this.add(getRefreshButton(), gridBagConstraints12);
		this.add(getJButton(), gridBagConstraints13);
	}

	public void refresh() {
		try {
			infoQuery.submit();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			myLogger.error(e1);
		}
		info = (String[]) infoQuery.getResult();
		fillInformation();

	}

	/**
	 * This method initializes expiresTextfield
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getExpiresTextfield() {
		if (expiresTextfield == null) {
			expiresTextfield = new JTextField();
			expiresTextfield.setEditable(false);
			expiresTextfield.setHorizontalAlignment(JTextField.CENTER);
			expiresTextfield.setBackground(Color.white);
		}
		return expiresTextfield;
	}

	/**
	 * This method initializes rolesTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRolesTextField() {
		if (rolesTextField == null) {
			rolesTextField = new JTextField();
			rolesTextField.setEditable(false);
			rolesTextField.setHorizontalAlignment(JTextField.CENTER);
			rolesTextField.setBackground(Color.white);

		}
		return rolesTextField;
	}

	private void fillInformation() {
		StringBuffer roles = new StringBuffer();
		// TODO maybe take myroles from VomrsPanelSingle to save one webservices
		// query?
		String[] rolesArray = voms.getClient().getMyContexts();
		if (rolesArray.length > 1)
			rolesLabel.setText("Roles");
		for (String role : rolesArray) {
			roles.append(role + ", ");
		}
		rolesTextField.setText(roles.toString()
				.substring(0, roles.length() - 2));
		rolesTextField.setToolTipText(roles.toString().substring(0,
				roles.length() - 2));

		expiresTextfield.setText(info[10]);

		statusTextField.setText(info[6]);

		StringBuffer groups = new StringBuffer();

		groups.append("<html><body>");
		groups.append("<table style=\"text-align: left; width: 100%;\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\"><tbody>");
		boolean switch_color = true;
		for (String group : VomrsClient.parseGroups(info[14])) {
			if (group.indexOf("Role=Member") != -1
					&& group.indexOf("Denied") == -1) {
				groups.append("<tr");
				if (switch_color) {
					switch_color = false;
					groups.append(" style=\"background-color: rgb("
							+ lighterColor.getRed() + ", "
							+ lighterColor.getGreen() + ", "
							+ lighterColor.getBlue() + ");\"");
				} else {
					switch_color = true;
				}
				groups.append("><td style=\"font-weight: bold;\">"
						+ group.substring(0, group.indexOf("/Role="))
						+ "</td><td><font size=\"-1\">"
						+ group.substring(group.indexOf("STATUS"),
								group.indexOf("REASON")) + "</font></td></tr>");
			}
		}
		groups.append("</tbody></table><br></body></html>");

		// groups.append("<html><body>");
		// for ( String tabGroup : VomrsClient.parseGroups(info[14])){
		// if ( tabGroup.indexOf("Role=Member") != -1 ){
		// groups.append("<p>"+tabGroup.substring(0,
		// tabGroup.indexOf("/Role="))+"<br>");
		// groups.append(tabGroup.substring(tabGroup.indexOf("STATUS"),
		// tabGroup.indexOf("REASON"))+"</p>");
		// groups.append("<hr>");
		// }
		// }
		// groups.append("</body></html>");
		groupsPane.setDocument(groups.toString(), false);

	}

	/**
	 * This method initializes detailsButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDetailsButton() {
		if (detailsButton == null) {
			detailsButton = new JButton();
			detailsButton.setText("Details");
			detailsButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							StringBuffer info_formatted = new StringBuffer();
							for (String part : info) {
								info_formatted.append(part + "<br>");
							}
							SimpleMessageDialog dialog = new SimpleMessageDialog(
									null, info_formatted.toString(),
									Color.white);
							dialog.setVisible(true);
						}
					});
		}
		return detailsButton;
	}

	/**
	 * This method initializes statusTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getStatusTextField() {
		if (statusTextField == null) {
			statusTextField = new JTextField();
			statusTextField.setEditable(false);
			statusTextField.setHorizontalAlignment(JTextField.CENTER);
			statusTextField.setBackground(Color.white);
		}
		return statusTextField;
	}

	/**
	 * This method initializes groupsPane
	 * 
	 * @return javax.swing.JEditorPane
	 */
	private SimpleMessagePanel getGroupsPanel() {
		if (groupsPane == null) {
			groupsPane = new SimpleMessagePanel(Color.white);
		}
		return groupsPane;
	}

	/**
	 * This method initializes refreshButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = new JButton();
			refreshButton.setText("Refresh");
			refreshButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							LocalVomses.updateVoms(voms);
						}
					});
		}
		return refreshButton;
	}

	public void vomsStatusChanged(VomsStatusEvent event) {
		if (((Voms) event.getSource()).equals(voms)
				&& event.getAction() != VomsStatusEvent.REMOVED_VOMS) {
			if (voms.getStatus() != Voms.NO_CONNECTION_TO_VOMRS
					&& voms.getStatus() != Voms.NO_MEMBER) {
				info = (String[]) voms.getInfoQuery().getResult();
				fillInformation();
				// MembershipInfoPanel.this.setVisible(true);
			} else {
				// MembershipInfoPanel.this.setVisible(false);
			}

		}
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Help");
			jButton.setToolTipText("Display some information about VO and VO membership.");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SimpleInfoDialog dialog = new SimpleInfoDialog(null,
							"vo_info", Color.white);
					dialog.setVisible(true);
				}
			});
		}
		return jButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
