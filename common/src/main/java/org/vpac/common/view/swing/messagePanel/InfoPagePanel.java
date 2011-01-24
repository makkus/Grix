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

package org.vpac.common.view.swing.messagePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import java.awt.Dimension;

/**
 * Helper class to display HTML-text within a JPanel easily. Basically it wraps
 * a MessagePanel and makes it easier to use.
 * 
 * @author Markus Binsteiner
 * 
 */
public class InfoPagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	static final Logger myLogger = Logger.getLogger(InfoPagePanel.class
			.getName()); // @jve:decl-index=0:

	private String infoPage = null;

	private MessagePanel messagePanel = null; // @jve:decl-index=0:

	/**
	 * This is the default constructor
	 */
	public InfoPagePanel(String infoPage, Color background) {
		super();
		this.setBackground(background);
		this.infoPage = infoPage;
		initialize();
	}

	public InfoPagePanel(String infoPage) {
		super();
		this.infoPage = infoPage;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		// GridBagConstraints gridBagConstraints = new GridBagConstraints();
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.fill = GridBagConstraints.BOTH;
		// gridBagConstraints.insets = new Insets(20, 20, 20, 20);
		// gridBagConstraints.gridy = 0;
		this.setSize(630, 689);
		this.setLayout(new BorderLayout());
		this.add((JPanel) getMessagePanel(), BorderLayout.CENTER);

	}

	/**
	 * This method initializes messagePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private MessagePanel getMessagePanel() {
		if (messagePanel == null) {
			// messagePanel = new XHTMLMessagePanel();
			messagePanel = new SimpleMessagePanel();
			messagePanel.setMargins(new Insets(20, 20, 20, 20));
			((JPanel) messagePanel).setBackground(new Color(245, 245, 245));
			try {
				messagePanel.setDocument(MessagePanel.getHTML(infoPage));
			} catch (IOException e) {
				messagePanel.setDefaultErrorPage();
			}

		}
		return messagePanel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
