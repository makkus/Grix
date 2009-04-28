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

package org.vpac.grix.view.swing.proxy;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class VomsProxyPanelHolder extends JPanel {

	private static final long serialVersionUID = 1L;

	private VomsProxyInitPanel vomsProxyInitPanel = null;
	
	private Color base_color = null;
	private Color lighter_color = null;
	

	/**
	 * This is the default constructor
	 */
	public VomsProxyPanelHolder() {
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
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 0;
		this.setSize(629, 559);
		this.setLayout(new GridBagLayout());
		this.add(getProxyInitPanel(), gridBagConstraints);
	}

	/**
	 * This method initializes proxyInitPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private VomsProxyInitPanel getProxyInitPanel() {
		if (vomsProxyInitPanel == null) {
			vomsProxyInitPanel = new VomsProxyInitPanel(getLighterColor());
			vomsProxyInitPanel.setBackground(getBaseColor());
		}
		return vomsProxyInitPanel;
	}
	
	public Color getBaseColor() {
		if ( base_color == null ) {
			base_color = this.getBackground();
		}
		return base_color;
	}
	
	public Color getLighterColor() {
		if ( lighter_color == null ){
			int red = getBaseColor().getRed()+10;
			int green = getBaseColor().getGreen()+10;
			int blue = getBaseColor().getBlue()+10;
			lighter_color = new Color(red, green, blue);
		}
		return lighter_color;
	}
	


}  //  @jve:decl-index=0:visual-constraint="10,10"

