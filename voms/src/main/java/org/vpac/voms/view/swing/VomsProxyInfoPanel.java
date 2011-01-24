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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.vpac.common.model.StatusEvent;
import org.vpac.common.model.StatusListener;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;

public class VomsProxyInfoPanel extends JPanel implements StatusListener {

	private static final long serialVersionUID = 1L;
	private SimpleMessagePanel messagePanel = null;

	/**
	 * This is the default constructor
	 */
	public VomsProxyInfoPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getMessagePanel(), gridBagConstraints);
	}

	/**
	 * This method initializes messagePanel
	 * 
	 * @return javax.swing.JEditorPane
	 */
	private SimpleMessagePanel getMessagePanel() {
		if (messagePanel == null) {
			messagePanel = new SimpleMessagePanel(Color.white);
		}
		return messagePanel;
	}

	public void statusChanged(StatusEvent event) {

		if (GridProxy.INITIALIZED == event.getStatus()) {

			StringBuffer formattedMessage = new StringBuffer();
			for (String line : event.getMessage()) {
				formattedMessage.append(line + "<br>");
			}

			this.messagePanel.setDocument(formattedMessage.toString());
		} else {
			this.messagePanel
					.setDocument("No voms proxy on the local machine.");
		}
	}

}
