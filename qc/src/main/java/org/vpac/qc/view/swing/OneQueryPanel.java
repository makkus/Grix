/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
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

package org.vpac.qc.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.vpac.common.control.Helpers;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.Query;
import org.vpac.qc.model.query.QueryArgument;
import org.vpac.qc.model.query.UserInputQuery;

/**
 * If you want to use a JPanel to ask the user about {@link QueryArgument}s for a {@link UserInputQuery} than you have to implement
 * the {@link OneQueryPanelParent} interface and add a {@link OneQueryPanel} to your swing application.
 * <p>
 * This class provides a panel that has got a Submit button, an optional Cancel button and a title to describe the {@link Query}.
 *  
 * @author Markus Binsteiner
 *
 */
public class OneQueryPanel extends JPanel {
	
	static final Logger myLogger = Logger.getLogger(OneQueryPanel.class.getName());

	private static final long serialVersionUID = 1L;

	private UserInputPanelWrapper userInputPanel = null; // @jve:decl-index=0:

	private JButton cancelButton = null;

	private JButton submitButton = null;

	private OneQueryPanelParent oneQueryParent = null;

	private boolean cancel_button = true;

	private String button_text = null;

	private String title = null;
	
	private boolean clipboardSupport = false;

	/**
	 * This is the default constructor
	 */
	public OneQueryPanel(OneQueryPanelParent window, String title,
			String button_text, boolean cancel_button, boolean clipboardSupport) {
		super();
		this.title = title;
		this.cancel_button = cancel_button;
		this.button_text = button_text;
		this.clipboardSupport = clipboardSupport;
		this.oneQueryParent = window;
		if (this.oneQueryParent.getQuery() != null) {
			getUserInputPanel().connect(this.oneQueryParent.getQuery());
			initialize();
		} else {
			setUninitialized();
		}
	}
	
	public OneQueryPanel(OneQueryPanelParent window, String title,
			String button_text, boolean cancel_button) {
		
		this(window, title, button_text, cancel_button, false);
	}

	private void setUninitialized() {
		SimpleMessagePanel mp = new SimpleMessagePanel(Color.white);
		mp.setDocument(mp.getMessages().getString("queryNotInitialized"));
		this.setLayout(new BorderLayout());
		this.add(mp, BorderLayout.CENTER);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		JLabel titleLabel = null;
		GridBagConstraints titleConstraints = new GridBagConstraints();
		if (title != null && ! "".equals(title)) {
			titleLabel = new JLabel(title);
			titleConstraints = new GridBagConstraints();
			titleConstraints.gridx = 0;
			titleConstraints.gridy = 0;
			titleConstraints.insets = new Insets(20, 20, 5, 20);
			titleConstraints.anchor = GridBagConstraints.WEST;
		}
		GridBagConstraints submitButtonConstraints = new GridBagConstraints();
		submitButtonConstraints.gridx = 2;
		submitButtonConstraints.insets = new Insets(0, 0, 20, 20);
		submitButtonConstraints.anchor = GridBagConstraints.EAST;
		submitButtonConstraints.gridy = 2;
		GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
		cancelButtonConstraints.gridx = 1;
		cancelButtonConstraints.weightx = 1.0;
		cancelButtonConstraints.anchor = GridBagConstraints.EAST;
		cancelButtonConstraints.insets = new Insets(0, 0, 20, 20);
		cancelButtonConstraints.gridy = 2;
		GridBagConstraints userInputPanelConstraints = new GridBagConstraints();
		userInputPanelConstraints.gridx = 0;
		userInputPanelConstraints.weightx = 1.0;
		userInputPanelConstraints.weighty = 1.0;
		userInputPanelConstraints.fill = GridBagConstraints.BOTH;
		userInputPanelConstraints.gridwidth = 3;
		userInputPanelConstraints.insets = new Insets(20, 20, 20, 20);
		userInputPanelConstraints.gridy = 1;
		this.setSize(467, 345);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(245, 245, 245));
		if ( titleLabel != null )
			this.add(titleLabel, titleConstraints);
		this.add(getUserInputPanel().getPanel(), userInputPanelConstraints);
		if (cancel_button)
			this.add(getCancelButton(), cancelButtonConstraints);
		this.add(getSubmitButton(), submitButtonConstraints);
	}

	/**
	 * This method initializes userInputPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public UserInputPanelWrapper getUserInputPanel() {
		if (userInputPanel == null) {
			userInputPanel = new UserInputPanelWrapper(clipboardSupport);
		}
		return userInputPanel;
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
					oneQueryParent.cancel();
				}

			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes submitButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSubmitButton() {
		if (submitButton == null) {
			submitButton = new JButton();
			submitButton.setText(button_text);
			//submitButton.setPreferredSize(new Dimension(75, 25));
			submitButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						oneQueryParent.getQuery().fillUserInput();
					} catch (ArgumentsException e1) {
						oneQueryParent.submitted(false);
					}
					if (oneQueryParent.tellUser()) {
						try {
							oneQueryParent.getQuery().submit();
						} catch (Exception re) {
							oneQueryParent.submitted(false);
						}
						oneQueryParent.submitted(true);
					} else {
						try {
							oneQueryParent.getQuery().init();
						} catch (ArgumentsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							myLogger.error(e1);
						} catch (JDOMException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							myLogger.error(e1);
						}
						// do nothing
					}
				}
			});
		}
		return submitButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
