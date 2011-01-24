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

package org.vpac.grix.view.swing.common;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.vpac.common.view.swing.gridproxy.GridProxyPanel;

public class GridProxyDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private Frame parent = null;

	private JPanel gridProxyPanel = null;

	/**
	 * @param owner
	 */
	public GridProxyDialog(Frame owner) {
		super(owner);
		this.parent = owner;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(430, 399);
		Point p = new Point();
		p = this.getOwner().getLocation();
		this.setLocation(p.x + 150, p.y + 100);
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
			jContentPane.add(getGridProxyPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes gridProxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGridProxyPanel() {
		if (gridProxyPanel == null) {
			gridProxyPanel = new GridProxyPanel(parent, this);
		}
		return gridProxyPanel;
	}

	public Frame getParent() {
		return parent;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
