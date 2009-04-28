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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.vpac.common.control.Helpers;
import org.vpac.common.view.swing.ElementsPanel;
import org.vpac.common.view.swing.OneElementPanel;
import org.vpac.qc.model.query.UserInterfaceArgument;

/**
 * One QueryArgument description/input panel.
 * 
 * @author Markus Binsteiner
 *
 */
public class ClipboardOneUserInputPanel extends JPanel implements OneElementPanel, OneUserInputPanelInterface{

	private static final long serialVersionUID = 1L;
	
	private UserInterfaceArgument argument = null;

	private JLabel nameLabel = null;

	private JTextField userInputTextField = null;

	private JComboBox userInputComboBox = null;
	
	private ElementsPanel elementsPanel = null;

	private JButton pasteButton = null;
	private JButton clearButton = null;
	
	/**
	 * This is the default constructor
	 */
	public ClipboardOneUserInputPanel(ElementsPanel ep, UserInterfaceArgument argument) {
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

		GridBagConstraints pasteButtonConstraints = new GridBagConstraints();
		pasteButtonConstraints.gridx = 2;
		pasteButtonConstraints.insets = new Insets(0, 0, 0, 15);
		pasteButtonConstraints.gridy = 1;
		pasteButtonConstraints.anchor = GridBagConstraints.EAST;
		GridBagConstraints clearButtonConstraints = new GridBagConstraints();
		clearButtonConstraints.gridx = 1;
		clearButtonConstraints.insets = new Insets(0, 0, 0, 15);
		clearButtonConstraints.gridy = 1;
		clearButtonConstraints.weightx =1;
		clearButtonConstraints.anchor = GridBagConstraints.EAST;
		GridBagConstraints textFieldConstraints = new GridBagConstraints();
		textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldConstraints.gridy = 0;
		textFieldConstraints.weightx = 1.0;
		textFieldConstraints.gridwidth = 2;
		textFieldConstraints.insets = new Insets(5, 15, 5, 15);
		textFieldConstraints.anchor = GridBagConstraints.EAST;
		textFieldConstraints.gridx = 1;
		GridBagConstraints nameLabelConstraints = new GridBagConstraints();
		nameLabelConstraints.gridx = 0;
		nameLabelConstraints.anchor = GridBagConstraints.WEST;
		nameLabelConstraints.weightx = 0.0;
		nameLabelConstraints.insets = new Insets(0, 15, 0, 15);
		nameLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		nameLabelConstraints.gridy = 0;
		nameLabel = new JLabel();
		if ( argument.getPrettyName() == null || "".equals(argument.getPrettyName()) )
			nameLabel.setText(argument.getName());
		else nameLabel.setText(argument.getPrettyName() );
		nameLabel.setPreferredSize(new Dimension(100, 19));
		this.setSize(434, 62);
		this.setLayout(new GridBagLayout());
		this.add(nameLabel, nameLabelConstraints);
		if ( argument.isPreselection() ) {
			this.add(getUserInputComboBox(), textFieldConstraints);
			this.add(nameLabel, nameLabelConstraints);
		} else { 
			if ( argument.getPreselection() == null || argument.getPreselection().size() < 2 ){
				this.add(getUserInputTextField(), textFieldConstraints);
				this.add(getPasteButton(), pasteButtonConstraints);
				this.add(getClearButton(), clearButtonConstraints);
				this.add(nameLabel, nameLabelConstraints);
			} else {
				this.add(getUserInputComboBox(), textFieldConstraints);
				this.add(nameLabel, nameLabelConstraints);
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

	/**
	 * This method initializes pasteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPasteButton() {
		if (pasteButton == null) {
			pasteButton = new JButton();
			pasteButton.setText("Paste");
			pasteButton.setToolTipText("Paste the clipboard content into the textfield.");
			pasteButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String clipboard = Helpers.getClipboard();
					if ( clipboard != null && !"".equals(clipboard.trim())){
						getUserInputTextField().setText(clipboard.trim());
					}
				}
			});
		}
		return pasteButton;
	}
	
	/**
	 * This method initializes pasteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton();
			clearButton.setText("Clear");
			clearButton.setToolTipText("Clear the textfield.");
			clearButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
						getUserInputTextField().setText("");
				}
			});
		}
		return clearButton;
	}	

}  //  @jve:decl-index=0:visual-constraint="10,10"
