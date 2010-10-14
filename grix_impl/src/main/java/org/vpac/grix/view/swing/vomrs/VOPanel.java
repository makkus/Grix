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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.GridProxyEvent;
import org.vpac.common.model.gridproxy.GridProxyListener;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.common.view.swing.gridproxy.MiniGridProxyPanel;
import org.vpac.common.view.swing.messagePanel.InfoPagePanel;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.grix.control.utils.UserProperty;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.voms.control.LocalVomses;
import org.vpac.voms.control.Voms;
import org.vpac.voms.control.VomsStatusEvent;
import org.vpac.voms.control.VomsesStatusListener;
import org.vpac.voms.view.swing.VomrsRegisterPanel;
import org.vpac.voms.view.swing.VomrsRegisterPanelPhaseII;
import org.vpac.voms.view.swing.VomsesManagementDialog;

public class VOPanel extends JPanel implements VomsesStatusListener,
		GridProxyListener {

	static final Logger myLogger = Logger.getLogger(VOPanel.class.getName()); // @jve:decl-index=0:

	private static final long serialVersionUID = 1L;

	private JPanel voPanel = null;

	private JPanel contentPanel = null;

	private JList voList = null;

	DefaultListModel voListModel = null;

	private JScrollPane jScrollPane = null;

	GridBagConstraints contentPanelConstraints = null;

	private MiniGridProxyPanel miniGridProxyPanel = null;

	private Color base_color = null;
	private Color lighter_color = null;

	private JButton jButton = null;

	/**
	 * This is the default constructor
	 */
	public VOPanel() {
		super();
		initialize();
		LocalProxy.addStatusListener(this);
		LocalVomses.getLocalVomses().addStatusListener(this);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints miniGridPanelConstraints = new GridBagConstraints();
		miniGridPanelConstraints.gridx = 0;
		miniGridPanelConstraints.insets = new Insets(0, 20, 20, 20);
		miniGridPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		miniGridPanelConstraints.weightx = 0.0;
		miniGridPanelConstraints.gridy = 1;
		contentPanelConstraints = new GridBagConstraints();
		contentPanelConstraints.gridx = 1;
		contentPanelConstraints.gridy = 0;
		contentPanelConstraints.fill = GridBagConstraints.BOTH;
		contentPanelConstraints.weighty = 1.0;
		contentPanelConstraints.gridheight = 2;
		contentPanelConstraints.insets = new Insets(20, 0, 20, 20);
		contentPanelConstraints.weightx = 1.0;
		GridBagConstraints voPanelConstraints = new GridBagConstraints();
		voPanelConstraints.gridx = 0;
		voPanelConstraints.fill = GridBagConstraints.BOTH;
		voPanelConstraints.weightx = 0.0;
		voPanelConstraints.weighty = 1.0;
		voPanelConstraints.insets = new Insets(20, 20, 20, 20);
		voPanelConstraints.gridy = 0;
		this.setSize(680, 439);
		this.setLayout(new GridBagLayout());
		this.add(getVoPanel(), voPanelConstraints);
		this.add(getContentPanel(), contentPanelConstraints);
		if ( ! ( "top".equals(UserProperty.getProperty("SHOW_GRID_PROXY_STATUS_PANEL"))  ||
				"bottom".equals(UserProperty.getProperty("SHOW_GRID_PROXY_STATUS_PANEL")) ))
			this.add(getMiniGridProxyPanel(), miniGridPanelConstraints);
		revalidate();
	}

	/**
	 * This method initializes voPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getVoPanel() {
		if (voPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(15, 10, 10, 10);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
//			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
//			gridBagConstraints4.fill = GridBagConstraints.BOTH;
//			gridBagConstraints4.gridy = 0;
//			gridBagConstraints4.weightx = 1.0;
//			gridBagConstraints4.weighty = 1.0;
//			gridBagConstraints4.insets = new Insets(20, 20, 20, 20);
//			gridBagConstraints4.gridx = 0;
			voPanel = new JPanel();
			voPanel.setLayout(new GridBagLayout());
			voPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0), "VO\'s", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 14), null));
			voPanel.setBackground(getLighterColor());
			voPanel.add(getJScrollPane(), gridBagConstraints1);
			//			voPanel.add(getJButton(), gridBagConstraints);
		}
		return voPanel;
	}

	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new CardLayout());
			contentPanel.add(new InfoPagePanel("loadingInfo", Color.white), "loadingInfo");
			contentPanel.add(new InfoPagePanel("noGridProxy", Color.white), "noGridProxy");
			contentPanel.add(new InfoPagePanel("noVO", Color.white), "noVO");
			CardLayout cl = (CardLayout) (getContentPanel().getLayout());
			if (GridProxy.INITIALIZED == LocalProxy.getStatus()) {
				if ( LocalVomses.getLocalVomses().getVOs().size() == 0 )
					cl.show(getContentPanel(), "noVO");
				else
					cl.show(contentPanel, "loadingInfo");
			} else {
				cl.show(contentPanel, "noGridProxy");
			}
			this.revalidate();

		}
		return contentPanel;
	}

	/**
	 * This method initializes voList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getVoList() {
		if (voList == null) {
			voListModel = new DefaultListModel();
			voList = new JList(voListModel);
			voList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			voList.setFont(new Font("Dialog", Font.PLAIN, 12));
			voList.setPreferredSize(new Dimension(70, 50));
			voList.setSelectedIndex(0);
			voList
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {

							//TODOif ( e.getSource() ){}
							if (voListModel.getSize() > 0) {
								CardLayout cl = (CardLayout) (getContentPanel()
										.getLayout());
								try {
									cl.show(getContentPanel(), ((Voms) voList
											.getSelectedValue()).toString());
								} catch (NullPointerException npe) {
									// does not matter
								}
							}
						}
					});
		}
		return voList;
	}

	private void addVoms(Voms voms, boolean add_to_model) {
		
		if (add_to_model) {
			voListModel.addElement(voms);
		}
		VomrsInfoPanel newPanel = null;
		// test whether vomrs server...
		if (voms.getStatus() == Voms.NO_CONNECTION_TO_VOMRS) {
			// no connection (most likely only voms, not vomrs
			try {
				SimpleMessagePanel mp = new SimpleMessagePanel(MessagePanel
						.getHTML("no_vomrs_server")+"<p><a href=\""+voms.getVomsWebURL()+"\">"
						+voms.getVomsWebURL()+"</a></p></li></ul></div>", Color.white);
				mp.setMargins(new Insets(40,40,40,40));
				contentPanel.add(mp, voms.toString());
			} catch (Exception ex) {
				myLogger.error(ex);
				//ex.printStackTrace();
			}

		} else if ( voms.getStatus() == Voms.CANDIDATE ) {
			VomrsRegisterPanelPhaseII regIIPanel = null;
			try {
				regIIPanel = new VomrsRegisterPanelPhaseII(voms, getLighterColor());
				//regPanel.setBackground(getLighterColor());
				contentPanel.add(regIIPanel, voms.toString());
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				myLogger.error(e);
			} catch (ArgumentsException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				myLogger.error(e);
			}
		}else if (voms.getStatus() == Voms.NO_MEMBER) {
			// test whether already registered...
			VomrsRegisterPanel regPanel = null;
			try {
				regPanel = new VomrsRegisterPanel(voms, getLighterColor());
				//regPanel.setBackground(getLighterColor());
				contentPanel.add(regPanel, voms.toString());
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ArgumentsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ( voms.getStatus() == Voms.NON_VOMRS_MEMBER ) { 
			// NO connection to vomrs => no detailed member info
			try {
				SimpleMessagePanel mp = new SimpleMessagePanel(MessagePanel
						.getHTML("no_vomrs_server_but_voms")+"<p><a href=\""+voms.getVomsWebURL()+"\">"
						+voms.getVomsWebURL()+"</a></p></div>", Color.white);
				mp.setMargins(new Insets(40,40,40,40));
				contentPanel.add(mp, voms.toString());
			} catch (Exception ex) {
				myLogger.error(ex);
				//ex.printStackTrace();
			}
		} else if (voms.getStatus() != Voms.NO_MEMBER) {
			newPanel = new VomrsInfoPanel(voms, getLighterColor());
			contentPanel.add(newPanel, voms.toString());
		}
	}

	private void removeVoms(Voms voms, boolean remove_from_model) {

		if (remove_from_model) {
			int index = voListModel.indexOf(voms);
			if (index > -1) {
				voListModel.remove(voListModel.indexOf(voms));
			}
		}
		for (Component panel : contentPanel.getComponents()) {
			try {
				myLogger.debug("This panel is of Class: " + panel.getClass());
				if (panel.getClass() == this
						.getClass()
						.getClassLoader()
						.loadClass(
								"org.vpac.grix.view.swing.vomrs.VomrsInfoPanel")) {
					if (voms.toString().equals(
							((VomrsInfoPanel) panel).getVoms().toString())) {
						contentPanel.remove(panel);
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				myLogger.error(e);
			}
		}
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(70, 60));
			jScrollPane.setViewportView(getVoList());
		}
		return jScrollPane;
	}

	public void gridProxyStatusChanged(GridProxyEvent e) {

		int status = e.getStatus();
		CardLayout cl = (CardLayout) (getContentPanel().getLayout());
		if (status == GridProxy.INITIALIZED) {
			if ( LocalVomses.getLocalVomses().getVOs().size() == 0 )
				cl.show(getContentPanel(), "noVO");
			else
				cl.show(getContentPanel(), "loadingInfo");
		} else {
//			for ( int i = 0; i < voListModel.getSize(); i++ ) {
//				removeVoms((Voms)voListModel.get(i), false);
//			}
//			voListModel.removeAllElements();
			cl.show(getContentPanel(), "noGridProxy");
		}
		this.revalidate();
	}

	/**
	 * This method initializes miniGridProxyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private MiniGridProxyPanel getMiniGridProxyPanel() {
		if (miniGridProxyPanel == null) {
			miniGridProxyPanel = new MiniGridProxyPanel();
			miniGridProxyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0), "Grid proxy", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 14), new Color(51, 51, 51)));
			miniGridProxyPanel.setBackground(getLighterColor());

		}
		return miniGridProxyPanel;
	}

	public void vomsStatusChanged(VomsStatusEvent event) {

		if (event.getAction() == VomsStatusEvent.STATUS_CHANGED || event.getAction() == VomsStatusEvent.REMOVED_VOMS_MEMBERSHIP ) {
			
			//TODO maybe give a message to the user that a vo membership was removed
			
			final CardLayout cl = (CardLayout) (getContentPanel().getLayout());
			cl.show(contentPanel, "loadingInfo");
			final Voms changed_voms = (Voms)event.getSource();
			new Thread() {
				public void run() {
					removeVoms(changed_voms, false);
					addVoms(changed_voms, false);
					cl.show(getContentPanel(), changed_voms.toString());
				}
			}.start();
		} else if (event.getAction() == VomsStatusEvent.NEW_VOMS) {
			addVoms((Voms) event.getSource(), true);
			//TODO somehow this is buggy but I don't know why
			if (getVoList().getSelectedIndex() == -1) {
				getVoList().setSelectedIndex(0);
			}
		} else if (event.getAction() == VomsStatusEvent.REMOVED_VOMS) {
			removeVoms((Voms) event.getSource(), true);
			if ( voListModel.size() == 0 && LocalProxy.isValid() ){
				final CardLayout cl = (CardLayout) (getContentPanel().getLayout());
				cl.show(contentPanel, "noVO");
			}
		}

		this.revalidate();
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

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Manage VOMS");
			jButton.setToolTipText("Manage the VOs you want to connect to.");
			//jButton.setEnabled(false);
			//jButton.setVisible(false);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					VomsesManagementDialog vmd = new VomsesManagementDialog(null);
					vmd.setVisible(true);
				}
			});
		}
		return jButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
