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

package org.vpac.grix.view.swing.vomrs;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.vpac.common.view.swing.messagePanel.InfoPagePanel;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.VomsStatusEvent;
import org.vpac.voms.control.VomsesStatusListener;
import org.vpac.voms.view.swing.VomrsSubscribePanel;

public class VomrsInfoPanel extends JPanel implements VomsesStatusListener {

	private static final long serialVersionUID = 1L;
	
	static final Logger myLogger = Logger.getLogger(VomrsInfoPanel.class.getName());
	
	private Voms voms = null;

	private MembershipInfoPanel membershipInfoPanel = null;

	private JPanel detailPanel = null;
	private Color lighterColor = null;

	/**
	 * This is the default constructor
	 */
	public VomrsInfoPanel(Voms voms) {
		super();
		this.voms = voms;

		if ( voms.getStatus() == Voms.NO_MEMBER ) {
			myLogger.debug("Not a member of this VO yet.");
		}
		initialize();
		LocalVomses.addStatusListener(this);
	}
	
	/**
	 * This is the default constructor
	 */
	public VomrsInfoPanel(Voms voms, Color lighterColor) {
		super();
		this.voms = voms;
		this.lighterColor = lighterColor;

		if ( voms.getStatus() == Voms.NO_MEMBER ) {
			myLogger.debug("Not a member of this VO yet.");
		}
		initialize();
		LocalVomses.addStatusListener(this);
	}	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints subscribePanelConstraints = new GridBagConstraints();
		subscribePanelConstraints.gridx = 0;
		subscribePanelConstraints.gridy = 1;
		subscribePanelConstraints.fill = GridBagConstraints.BOTH;
		subscribePanelConstraints.insets = new Insets(15,0,0,0);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 0;
		this.setSize(619, 248);
		this.setLayout(new GridBagLayout());
		this.add(getMembershipInfoPanel(), gridBagConstraints);
		this.add(getDetailPanel(), subscribePanelConstraints);
		
	}

	private JPanel getDetailPanel() {
		if (detailPanel == null) {
			detailPanel = new JPanel();
			detailPanel.setLayout(new CardLayout());
			detailPanel.add(new VomrsSubscribePanel(voms, lighterColor), "subscribePanel");
			detailPanel.add(new InfoPagePanel("infoApplicant", Color.white), "infoApplicant");
			detailPanel.add(new InfoPagePanel("infoCandidate", Color.white), "infoCandidate");
			switchToAppropriateInfoPanel();

		}
		return detailPanel;
	}
	
	private void switchToAppropriateInfoPanel(){
		CardLayout cl = (CardLayout) (detailPanel.getLayout());
		if ( voms.getStatus() == Voms.NO_MEMBER ){
			// this should not happen...
			detailPanel.add(new InfoPagePanel("noMemberAnymore", Color.white), "noMemberAnymore");
			cl.show(detailPanel, "noMemberAnymore");
		}
		if ( voms.getStatus() == Voms.APPLICANT ) {
			cl.show(detailPanel, "infoApplicant");
		} else if ( voms.getStatus() == Voms.CANDIDATE ) {
			cl.show(detailPanel, "infoCandidate");
		} else {
			cl.show(detailPanel, "subscribePanel");
		}		
	}

	/**
	 * This method initializes membershipInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private MembershipInfoPanel getMembershipInfoPanel() {
		if (membershipInfoPanel == null) {

			membershipInfoPanel = new MembershipInfoPanel(voms, lighterColor);

		}
		return membershipInfoPanel;
	}

	public Voms getVoms() {
		return voms;
	}

	public void vomsStatusChanged(VomsStatusEvent event) {
		
		if ( ((Voms)event.getSource()).equals(voms) ){
			switchToAppropriateInfoPanel();
		}
		
	}

} // @jve:decl-index=0:visual-constraint="10,10"
