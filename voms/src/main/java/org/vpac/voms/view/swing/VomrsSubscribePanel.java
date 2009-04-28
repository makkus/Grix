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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.vomrs.control.Vomrs_Utils;
import org.vpac.vomrs.model.VomrsClient;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.VomsStatusEvent;
import org.vpac.voms.control.VomsesStatusListener;

public class VomrsSubscribePanel extends JPanel implements VomsesStatusListener {
	
	static final Logger myLogger = Logger
	.getLogger(VomrsSubscribePanel.class.getName());  //  @jve:decl-index=0:

	private static final long serialVersionUID = 1L;

	private JComboBox groupsComboBox = null;

	private JLabel groupsLabel = null;

	private JButton applyButton = null;

	private VomrsClient client = null;

	private DefaultComboBoxModel comboBoxModel = null;

	private StringBuffer groupsToSubscribe = null;  //  @jve:decl-index=0:

	private String[] allMyGroups = null;

	private Voms voms = null;
	private Color lighterColor = null;

	/**
	 * This is the default constructor
	 */
	// public VomrsSubscribePanel(VomrsClient client) {
	public VomrsSubscribePanel(Voms voms) {
		super();
		this.voms = voms;
		this.client = voms.getClient();
		this.comboBoxModel = new DefaultComboBoxModel();
		initialize();
		LocalVomses.addStatusListener(this);
	}
	
	public VomrsSubscribePanel(Voms voms, Color lighterColor) {
		super();
		this.lighterColor = lighterColor;
		this.voms = voms;
		this.client = voms.getClient();
		this.comboBoxModel = new DefaultComboBoxModel();
		initialize();
		LocalVomses.addStatusListener(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.insets = new Insets(20, 0, 20, 20);
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new Insets(20, 20, 0, 0);
		gridBagConstraints1.gridy = 0;
		groupsLabel = new JLabel();
		groupsLabel.setText("Group");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(20, 20, 0, 20);
		gridBagConstraints.gridx = 1;
		this.setSize(628, 110);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0), "Apply for membership in a group", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 14), new Color(51, 51, 51)));
		if ( lighterColor != null )	this.setBackground(lighterColor);
		this.setLayout(new GridBagLayout());
		this.add(getGroupsComboBox(), gridBagConstraints);
		this.add(groupsLabel, gridBagConstraints1);
		this.add(getApplyButton(), gridBagConstraints2);
	}

	/**
	 * This method initializes groupsComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getGroupsComboBox() {
		if (groupsComboBox == null) {
			fillGroups();
			groupsComboBox = new JComboBox(comboBoxModel);
		}
		return groupsComboBox;
	}

	/**
	 * This method initializes applyButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getApplyButton() {
		if (applyButton == null) {
			applyButton = new JButton();
			applyButton.setText("Apply");
			applyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						
						StringBuffer message = new StringBuffer();
						//TODO outsource messages...
						message.append("<p>You are about to subscribe to the group <b>"+(String)getGroupsComboBox().getSelectedItem()+"</b></p>");
						message.append("<p>Do you want to do that?</p>");
						
						Object[] options = new Object[]{"Yes", "No"};
						MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
						messagePanel.setDocument(message.toString(), true);
						messagePanel.setPreferredSize(new Dimension(250, 150));
						int n = JOptionPane.showOptionDialog(VomrsSubscribePanel.this, messagePanel, "Please confirm", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						myLogger.debug("User chose: "+ n);
						
						if ( n == 0 ) {
							// subscribe to all parent groups as well...
							StringBuffer currentGroup = new StringBuffer();
							
							for ( String group : ((String)getGroupsComboBox().getSelectedItem()).substring(1).split("/") ){
								currentGroup.append("/"+group);
								if ( groupsToSubscribe.indexOf("<"+currentGroup+">") != -1 ) {
									myLogger.debug("Subscribing to group: "+currentGroup);	
									Object[] output = client.execute("addMbrToGroup",
											new Object[] { currentGroup.toString() });
								}
							}
						
						LocalVomses.updateVoms(voms);
						}
						
					} catch (Exception e1) {
						StringBuffer errormessage = new StringBuffer();
						//TODO outsource messages...
						errormessage.append("<p>Could not subscribe to group <b>"+(String)getGroupsComboBox().getSelectedItem()+"</b></p>");
						if ( e1.getMessage() != null ){
							errormessage.append("<p>Reason:</p>");
							errormessage.append("<p>"+e1.getMessage()+"</p>");
						} else {
							errormessage.append("<br>One possible reason could be that you tried to subscribe to a subgroup whitout being in the parentgroup of this subgroup.");
						}
						Object[] options = new Object[]{"Yes", "No"};
						MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
						messagePanel.setDocument(errormessage.toString(), true);
						messagePanel.setPreferredSize(new Dimension(300, 200));
						JOptionPane.showMessageDialog(VomrsSubscribePanel.this, messagePanel, "Error when subscribing to group", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return applyButton;
	}

	private void fillGroups() {

		comboBoxModel.removeAllElements();

		allMyGroups = VomrsClient.parseGroups((String) ((voms
				.getInfoQuery().getResult())[14]));
		
		String[] allVOGroups = client.getAllGroups();
		
		groupsToSubscribe = new StringBuffer();
		
		boolean noMemberYet = true;
		boolean sure = false;

		// check whether subscribed to this group
		for (String group : allVOGroups) {
			
			for ( String mygroup : allMyGroups ){
				
				myLogger.debug("Current group: "+group+"/"+Vomrs_Utils.ROLE);
				myLogger.debug("Current mygroup: "+mygroup);
				
				if ( (( mygroup.indexOf(group+"/"+Vomrs_Utils.ROLE) != -1 ) && // listed in mbrInfo
					 ( mygroup.indexOf("STATUS:Approved") == -1 && mygroup.indexOf("STATUS:New") == -1 )) // not approved or new (but: denied)
				) { 
					noMemberYet = true;
					sure = true;
				} else if ( mygroup.indexOf(group+"/"+Vomrs_Utils.ROLE) == -1 ){ // not listed in mbrInfo
					noMemberYet = true;
					sure = false;
				} else {
					noMemberYet = false;
					sure = true;
				}

				if ( sure ) {
					break;
				}

			}
			// if not in this group or applied already
			if ( noMemberYet ) {
				String newGroup = group;
				comboBoxModel.addElement(newGroup);
				groupsToSubscribe.append("<"+newGroup+">");
			}
		}

		 if ( comboBoxModel.getSize() == 0 ) {
		 comboBoxModel.addElement(new String("No groups left to subscribe to."));
		 getApplyButton().setEnabled(false);
		 this.setVisible(false);
		 } else {
		 getApplyButton().setEnabled(true);
		 this.setVisible(true);
		 }
		 

	}

	public void vomsStatusChanged(VomsStatusEvent event) {

		if (((Voms) event.getSource()).equals(voms) && event.getAction() != VomsStatusEvent.REMOVED_VOMS ) {
			//if (event.getAction() == VomsStatusEvent.INFO_CHANGED) {
				fillGroups();
			//}
		}

	}

} // @jve:decl-index=0:visual-constraint="10,10"
