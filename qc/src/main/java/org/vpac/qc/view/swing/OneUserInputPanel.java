/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
 * qc is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.

 * qc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with qc; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.vpac.qc.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.vpac.common.view.swing.ElementsPanel;
import org.vpac.common.view.swing.OneElementPanel;
import org.vpac.qc.model.query.UserInterfaceArgument;

/**
 * One QueryArgument description/input panel.
 * 
 * @author Markus Binsteiner
 *
 */
public class OneUserInputPanel extends JPanel implements OneElementPanel, OneUserInputPanelInterface{

	private static final long serialVersionUID = 1L;
	
	private UserInterfaceArgument argument = null;

	private JLabel nameLabel = null;

	private JTextField userInputTextField = null;

	private JComboBox userInputComboBox = null;
	
	private ElementsPanel elementsPanel = null;
	

	/**
	 * This is the default constructor
	 */
	public OneUserInputPanel(ElementsPanel ep, UserInterfaceArgument argument) {
		super();
		this.elementsPanel = ep;
		this.argument = argument;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints textFieldConstraints = new GridBagConstraints();
		textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldConstraints.gridy = 1;
		textFieldConstraints.weightx = 1.0;
		textFieldConstraints.insets = new Insets(5, 15, 0, 15);
		textFieldConstraints.anchor = GridBagConstraints.EAST;
		textFieldConstraints.gridx = 0;
		GridBagConstraints nameLabelConstraints = new GridBagConstraints();
		nameLabelConstraints.gridx = 0;
		nameLabelConstraints.anchor = GridBagConstraints.WEST;
		nameLabelConstraints.weightx = 1.0;
		nameLabelConstraints.insets = new Insets(0, 15, 0, 15);
		nameLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		nameLabelConstraints.gridy = 0;
		nameLabel = new JLabel();
		if ( argument.getPrettyName() == null || "".equals(argument.getPrettyName()) )
			nameLabel.setText(argument.getName());
		else nameLabel.setText(argument.getPrettyName() );
		nameLabel.setPreferredSize(new Dimension(150, 19));
		this.setSize(434, 62);
		this.setLayout(new GridBagLayout());
		this.add(nameLabel, nameLabelConstraints);
		if ( argument.isPreselection() ) {
			this.add(getUserInputComboBox(), textFieldConstraints);
		} else { 
			if ( argument.getPreselection() == null || argument.getPreselection().size() < 2 ){
				this.add(getUserInputTextField(), textFieldConstraints);
			} else {
				this.add(getUserInputComboBox(), textFieldConstraints);
			}
		}
	}

	/**
	 * This method initializes userInputTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUserInputTextField() {
		if (userInputTextField == null) {
			userInputTextField = new JTextField();
			if ( argument.getDescription() != null && ! "".equals(argument.getDescription()) )
				userInputTextField.setToolTipText(argument.getDescription());
				userInputTextField.setPreferredSize(new Dimension(300, 24));
				if ( argument.getPreselection() != null && argument.getPreselection().size() == 1 ){
					userInputTextField.setText(argument.getPreselectionAsString()[0]);
				}
		}
		return userInputTextField;
	}

	/**
	 * This method initializes userInputComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getUserInputComboBox() {
		if (userInputComboBox == null) {
			userInputComboBox = new JComboBox(argument.getPreselectionAsString());
			userInputComboBox.setBackground(Color.white);
			if ( ! argument.isPreselection() ) {
				userInputComboBox.setEditable(true);
			}
			userInputComboBox.setPreferredSize(new Dimension(300, 24));
			if ( argument.getDescription() != null && ! "".equals(argument.getDescription()) )
				userInputComboBox.setToolTipText(argument.getDescription());
		}
		return userInputComboBox;
	}
	
	public Object getUserInput(){
		if ( argument.isPreselection() ){
			return argument.getPreselection().get(getUserInputComboBox().getSelectedIndex());
		} else {
			if ( argument.getPreselection() == null || argument.getPreselection().size() < 2 ) {
				return getUserInputTextField().getText();
			} else {
				return getUserInputComboBox().getSelectedItem();
			}
		}
	}

	public ElementsPanel getElementsPanel() {
		return this.elementsPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
