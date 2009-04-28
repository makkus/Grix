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

package org.vpac.common.view.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A ElementsPanel can hold several (similar) OneElementPanels and stacks them (from top to bottom).
 * Elements can be added or removed and the Element panel reacts accordingly.
 * 
 * @author Markus Binsteiner
 *
 */
public class ElementsPanel extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPanel = null;	
	private JPanel fillPanel = null;	
	
	private ArrayList<JPanel> panels = null;
	
	private static short id = 0;

	/**
	 * This is the default constructor
	 */
	public ElementsPanel() {
		super();
		this.panels = new ArrayList<JPanel>();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected void initialize() {
		this.setSize(420, 354);
		this.setViewportView(getContentPanel());

	}
	
	public void addOneElementPanel(OneElementPanel panel) {

		contentPanel.remove(getFillPanel());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		//TODO this is dodgy but otherwise I would take more effort I am willing to spend right now
		constraints.gridy = id++;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		//constraints.insets = new Insets(10, 20, 10, 20);
		constraints.ipadx = 20;
		constraints.ipady = 20;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;

		// fillpanel
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;

		contentPanel.add((JPanel)panel, constraints);
		panels.add((JPanel)panel);
		contentPanel.add(getFillPanel(), gridBagConstraints);
		repaint();
		revalidate();

	}

	public void removeOneElementPanel(JPanel panel) {

		// panel.stop();
		contentPanel.remove(panel);
		panels.remove(panel);
		repaint();
		revalidate();
	}	
	
	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridy = 0;
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			//contentPanel.setBackground(new Color(245, 245, 245));
			contentPanel.add(getFillPanel(), gridBagConstraints);
		}
		return contentPanel;
	}	
	
	public void setBackground(Color color){
		getContentPanel().setBackground(color);
		getFillPanel().setBackground(color);
	}
	
	private JPanel getFillPanel() {

		if (fillPanel == null) {
			fillPanel = new JPanel();
			//fillPanel.setBackground(new Color(245, 245, 245));
			fillPanel.setLayout(new GridBagLayout());
		}
		return fillPanel;
	}

	protected ArrayList<JPanel> getPanels() {
		return panels;
	}	

}  //  @jve:decl-index=0:visual-constraint="10,10"
