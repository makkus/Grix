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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.vpac.common.external.ArrayListTransferHandler;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.voms.control.LocalVomses;

public class VomsesManagementDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	static final Logger myLogger = Logger
			.getLogger(VomsesManagementDialog.class.getName());

	public final String available_vomses_dir = System.getProperty("user.home")
			+ File.separator + ".glite" + File.separator + "vomses_available";

	public final String active_vomses_dir = System.getProperty("user.home")
			+ File.separator + ".glite" + File.separator + "vomses"; // @jve:decl-index=0:

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JList availableList = null;

	private JList activeList = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private DefaultListModel availableModel = null;
	private DefaultListModel activeModel = null;
	private ArrayListTransferHandler listTransferHandler = null;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private boolean vomses_changed = false;

	private JPanel jPanel1 = null;

	private SimpleMessagePanel infoPanel = null;

	/**
	 * @param owner
	 */
	public VomsesManagementDialog(Frame owner) {
		super(owner);

		this.activeModel = new DefaultListModel();
		for (File file : new File(active_vomses_dir).listFiles()) {
			this.activeModel.addElement(file.getName());
		}

		this.availableModel = new DefaultListModel();
		for (File file : new File(available_vomses_dir).listFiles()) {
			boolean in_active = false;
			for (Object active : activeModel.toArray()) {
				if (((String) active).equals(file.getName())) {
					in_active = true;
					break;
				}
			}
			if (!in_active)
				this.availableModel.addElement(file.getName());
		}

		listTransferHandler = new ArrayListTransferHandler();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(474, 294);
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
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridwidth = 3;
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 3;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 3;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.insets = new Insets(0, 0, 15, 15);
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new Insets(0, 0, 15, 15);
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = GridBagConstraints.BOTH;
			gridBagConstraints31.weighty = 1.0;
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.gridy = 1;
			gridBagConstraints31.insets = new Insets(15, 15, 15, 15);
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.weightx = 1.0;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.weighty = 1.0;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.insets = new Insets(15, 15, 15, 15);
			gridBagConstraints21.weightx = 1.0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(10, 15, 0, 0);
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("Active groups");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(10, 15, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Unused groups:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(15, 0, 15, 15);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(15, 15, 15, 15);
			gridBagConstraints.gridx = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, gridBagConstraints2);
			jPanel.add(jLabel1, gridBagConstraints3);
			jPanel.add(getJScrollPane(), gridBagConstraints21);
			jPanel.add(getJScrollPane1(), gridBagConstraints31);
			jPanel.add(getJButton(), gridBagConstraints4);
			jPanel.add(getJButton1(), gridBagConstraints5);
			jPanel.add(getInfoPanel(), gridBagConstraints12);
		}
		return jPanel;
	}

	/**
	 * This method initializes availableList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getAvailableList() {
		if (availableList == null) {
			availableList = new JList(availableModel);
			availableList.setDragEnabled(true);
			availableList
					.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			availableList.setTransferHandler(listTransferHandler);
		}
		return availableList;
	}

	/**
	 * This method initializes activeList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getActiveList() {
		if (activeList == null) {
			activeList = new JList(activeModel);
			activeList.setDragEnabled(true);
			activeList
					.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			activeList.setTransferHandler(listTransferHandler);
		}
		return activeList;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setPreferredSize(new Dimension(120, 3));
			jScrollPane.setViewportView(getAvailableList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane1.setPreferredSize(new Dimension(120, 3));
			jScrollPane1.setViewportView(getActiveList());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Cancel");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					VomsesManagementDialog.this.dispose();
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Apply");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!new File(active_vomses_dir).exists())
						new File(active_vomses_dir).mkdirs();
					for (Object filename : activeModel.toArray()) {
						try {
							if (!new File(active_vomses_dir, (String) filename)
									.exists()) {
								FileUtils.copyFile(
										new File(available_vomses_dir,
												(String) filename), new File(
												active_vomses_dir,
												(String) filename));
								vomses_changed = true;
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							// e1.printStackTrace();
							myLogger.error(e1);
						}
					}

					for (File file : new File(active_vomses_dir).listFiles()) {
						boolean inList = false;
						for (Object filename : activeModel.toArray()) {
							if (((String) filename).equals(file.getName())) {
								inList = true;
								break;
							}
						}
						if (!inList) {
							if (!new File(available_vomses_dir, file.getName())
									.exists()) {
								try {
									FileUtils.copyFile(
											file,
											new File(available_vomses_dir, file
													.getName()));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									// e1.printStackTrace();
									myLogger.error(e1);
								}
							}
							file.delete();
							vomses_changed = true;
						}

					}
					if (vomses_changed) {
						LocalVomses.refreshLocalVomses();
					}
					VomsesManagementDialog.this.dispose();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private SimpleMessagePanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new SimpleMessagePanel(
					"Drag & Drop groups you want to contact to the \"Active Groups\" panel.");
		}
		return infoPanel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
