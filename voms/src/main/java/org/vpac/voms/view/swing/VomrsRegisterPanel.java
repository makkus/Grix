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
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.vpac.common.view.swing.messagePanel.InfoPagePanel;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.QueryArgument;
import org.vpac.qc.model.query.UserInputQuery;
import org.vpac.qc.view.swing.OneQueryPanel;
import org.vpac.qc.view.swing.OneQueryPanelParent;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.utils.VomsProperty;

public class VomrsRegisterPanel extends JPanel implements OneQueryPanelParent {

	static final Logger myLogger = Logger.getLogger(VomrsRegisterPanel.class
			.getName()); // @jve:decl-index=0:

	private static final long serialVersionUID = 1L;

	private OneQueryPanel oneQueryPanel = null;

	private UserInputQuery userInputQuery = null;

	private Voms voms = null;

	/**
	 * This is the default constructor
	 * 
	 * @throws ArgumentsException
	 * @throws JDOMException
	 */
	public VomrsRegisterPanel(Voms voms, Color bg) throws JDOMException,
			ArgumentsException {
		super();
		this.voms = voms;
		userInputQuery = new UserInputQuery("registerMember", voms.getClient(),
				"Visitor");
		userInputQuery.init();
		initialize();
		getOneQueryPanel().setBackground(bg);
	}

	public VomrsRegisterPanel(Voms voms) throws JDOMException,
			ArgumentsException {
		super();
		this.voms = voms;
		userInputQuery = new UserInputQuery("registerMember", voms.getClient(),
				"Visitor");
		userInputQuery.init();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		// GridBagConstraints oneQueryPanelConstraints = new
		// GridBagConstraints();
		// oneQueryPanelConstraints.gridx = 0;
		// oneQueryPanelConstraints.gridy = 0;
		// oneQueryPanelConstraints.weightx = 1;
		// oneQueryPanelConstraints.weighty = 1;
		// oneQueryPanelConstraints.fill = GridBagConstraints.BOTH;
		// GridBagConstraints infoPagePanelConstraints = new
		// GridBagConstraints();
		// infoPagePanelConstraints.gridx = 0;
		// infoPagePanelConstraints.gridy = 1;
		// infoPagePanelConstraints.fill = GridBagConstraints.BOTH;
		// infoPagePanelConstraints.weightx = 1;
		// infoPagePanelConstraints.weighty = 0;
		// this.setSize(300, 200);
		BorderLayout layout = new BorderLayout();
		layout.setVgap(20);
		this.setLayout(layout);
		this.add(getOneQueryPanel(), BorderLayout.CENTER);
		this
				.add(new InfoPagePanel("register", Color.white),
						BorderLayout.SOUTH);
	}

	/**
	 * This method initializes oneQueryPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private OneQueryPanel getOneQueryPanel() {
		if (oneQueryPanel == null) {
			oneQueryPanel = new OneQueryPanel(this, "Register to VO \""
					+ voms.toString() + "\"", "Apply", false);
		}
		return oneQueryPanel;
	}

	public void cancel() {
		// TODO Auto-generated method stub

	}

	public UserInputQuery getQuery() {
		return userInputQuery;
	}

	public void submitted(boolean success) {
		Object temp = userInputQuery.getResult();
		Exception e = userInputQuery.getException();
		if (temp == null && e == null) {
			// success. VOMRS does not give anything back if successfull
			MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
			messagePanel
					.setDocument(
							"<html><body><p>You have successfully applied for membership in the "
									+ voms.toString()
									+ " VO.</p>"
									+ "<p>Now you have to wait for an email from the VO administrator. Follow the instructions in this email to become"
									+ " an approved member of the VO.</p></body></html>",
							false);
			messagePanel.setPreferredSize(new Dimension(300, 200));
			JOptionPane.showMessageDialog(this, messagePanel);
			LocalVomses.updateVoms(voms);

		} else if (e != null) {
			MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
			messagePanel
					.setDocument(
							"<html><body><p>Your application for membership in the "
									+ voms.toString()
									+ " VO was not successful.</p>"
									+ "<p>This should not happen and is most likely a problem with the VOMRS server. Please contact your systems administrator or <a href=\"mailto:"
									+ VomsProperty.getString("help.email.address")
									+ "\">"+ VomsProperty.getString("help.email.address")
									+ "</a>."
									+ "</p></body></html>", false);
			messagePanel.setPreferredSize(new Dimension(300, 200));
			JOptionPane.showMessageDialog(this, messagePanel,
					"Application not successful.", JOptionPane.ERROR_MESSAGE);
			LocalVomses.updateVoms(voms);
		}
	}

	public boolean tellUser() {

		StringBuffer message = new StringBuffer();
		// TODO outsource messages...
		message
				.append("<p>Are you sure you want to apply for VO membership with the following details?</p><br><br>");
		message
				.append("<table style=\"text-align: left; width: 100%;\" border=\"0\" cellpadding=\"2\" cellspacing=\"2\"><tbody>");
		boolean switch_color = true;
		for (QueryArgument argument : userInputQuery.getArguments()) {
			message.append("<tr");
			if (switch_color) {
				switch_color = false;
				message
						.append(" style=\"background-color: rgb(245, 245, 245);\"");
			} else {
				switch_color = true;
			}
			message.append("><td style=\"font-weight: bold;\">"
					+ argument.getPrettyName() + "</td><td><font size=\"-1\">"
					+ argument.getValue()[0] + "</font></td></tr>");
		}
		message.append("</tbody></table><br>");

		Object[] options = new Object[] { "Yes", "No" };
		MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
		messagePanel.setDocument(message.toString(), true);
		messagePanel.setPreferredSize(new Dimension(450, 350));
		int n = JOptionPane.showOptionDialog(this, messagePanel,
				"Please confirm your input.", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		myLogger.debug("User chose: " + n);

		if (n == 0)
			return true;
		else
			return false;

	}

}
