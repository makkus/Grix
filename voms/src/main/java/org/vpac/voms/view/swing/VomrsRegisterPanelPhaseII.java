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
import org.vpac.qc.model.query.InfoQuery;
import org.vpac.qc.model.query.UserInputQuery;
import org.vpac.qc.view.swing.OneQueryPanel;
import org.vpac.qc.view.swing.OneQueryPanelParent;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;

public class VomrsRegisterPanelPhaseII extends JPanel implements
		OneQueryPanelParent {

	static final Logger myLogger = Logger
			.getLogger(VomrsRegisterPanelPhaseII.class.getName()); // @jve:decl-index=0:

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
	public VomrsRegisterPanelPhaseII(Voms voms, Color bg) throws JDOMException,
			ArgumentsException {
		super();
		this.voms = voms;
		userInputQuery = new UserInputQuery("confirmMbrNotify", voms
				.getClient(), "Candidate");
		userInputQuery.init();
		initialize();
		getOneQueryPanel().setBackground(bg);
	}

	public VomrsRegisterPanelPhaseII(Voms voms) throws JDOMException,
			ArgumentsException {
		super();
		this.voms = voms;
		userInputQuery = new UserInputQuery("confirmMbrNotify", voms
				.getClient(), "Candidate");
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
				.add(new InfoPagePanel("infoCandidate", Color.white),
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
					+ voms.toString() + "\"", "Confirm", false, true);
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

			InfoQuery registerQuery = new InfoQuery("registerMember",
					userInputQuery.getClient(), "Candidate");
			try {
				registerQuery.init();
			} catch (ArgumentsException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				myLogger.error(e1);
			} catch (JDOMException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				myLogger.error(e1);
			}
			registerQuery.submit();
			temp = registerQuery.getResult();
			e = registerQuery.getException();
			if (temp == null && e == null) {
				// success. VOMRS does not give anything back if successfull
				MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
				messagePanel
						.setDocument(
								"<html><body><p>You have successfully confirmed your email address for your application to the "
										+ voms.toString()
										+ " VO.</p>"
										+ "</body></html>", false);
				messagePanel.setPreferredSize(new Dimension(300, 200));
				JOptionPane.showMessageDialog(this, messagePanel);
				LocalVomses.updateVoms(voms);
			} else if (e != null) {
				MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
				messagePanel
						.setDocument(
								"<html><body><p>There was an error with the confirmation of your email address.</p>"
										+ "<p>This is what the server answered:</p><p> "
										+ e.getCause().getMessage()
										+ "</p></body></html>", false);
				messagePanel.setPreferredSize(new Dimension(300, 200));
				JOptionPane.showMessageDialog(this, messagePanel,
						"Application not successful.",
						JOptionPane.ERROR_MESSAGE);
				LocalVomses.updateVoms(voms);
			}

		} else if (e != null) {
			MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
			messagePanel
					.setDocument(
							"<html><body><p>There was an error with the confirmation of your email address.</p>"
									+ "<p>This is what the server answered:</p><p> "
									+ e.getCause().getMessage()
									+ "</p></body></html>", false);
			messagePanel.setPreferredSize(new Dimension(300, 200));
			JOptionPane.showMessageDialog(this, messagePanel,
					"Application not successful.", JOptionPane.ERROR_MESSAGE);
			LocalVomses.updateVoms(voms);
		}
	}

	public boolean tellUser() {

		// nothing needed here
		return true;
	}

}
