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

package org.vpac.grix.view.swing.vomsproxy;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.vpac.common.view.swing.ElementsPanel;

public class MyVomsProxiesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ElementsPanel proxiesPanel = null;

	/**
	 * This is the default constructor
	 */
	public MyVomsProxiesPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getProxiesPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public ElementsPanel getProxiesPanel() {
		if (proxiesPanel == null) {
			proxiesPanel = new ElementsPanel();
			proxiesPanel.setLayout(new GridBagLayout());
		}
		return proxiesPanel;
	}

}
