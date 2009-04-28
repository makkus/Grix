/* Copyright 2006 VPAC
 * 
 * This file is part of common.
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
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Wraps a SimpleMessagePanel to be able to display a dialog.
 * 
 * @author Markus Binsteiner
 *
 */
public class SimpleMessageDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	
	private String message = null;

	private SimpleMessagePanel simpleMessagePanel = null;
	private Color background = null;

	/**
	 * @param owner
	 */
	public SimpleMessageDialog(Frame owner, String message, Color background) {
		super(owner, true);
		this.background = background;
		this.message = message;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(579, 494);
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
			jContentPane.add(getSimpleMessagePanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes simpleMessagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private SimpleMessagePanel getSimpleMessagePanel() {
		if (simpleMessagePanel == null) {
			simpleMessagePanel = new SimpleMessagePanel(background);
			simpleMessagePanel.setDocument(message);
		}
		return simpleMessagePanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
