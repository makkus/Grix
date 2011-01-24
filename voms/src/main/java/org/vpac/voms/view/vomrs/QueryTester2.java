/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
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

package org.vpac.voms.view.vomrs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.vpac.common.model.gridproxy.GlobusProxy;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.qc.model.clients.ClientNotInitializedException;
import org.vpac.qc.model.clients.GenericClient;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.UserInputQuery;
import org.vpac.qc.view.swing.OneQueryPanel;
import org.vpac.qc.view.swing.OneQueryPanelParent;
import org.vpac.vomrs.model.VomrsClient;

/**
 * This is a demonstration of what qc is capable of. Also it can be used to
 * control VOMRS via a Swing-GUI by using VOMRS's web service interface.
 * 
 * @author Markus Binsteiner
 * 
 */
public class QueryTester2 implements OneQueryPanelParent {

	static final Logger myLogger = Logger.getLogger(QueryTester2.class
			.getName()); // @jve:decl-index=0:

	private JFrame jFrame = null; // @jve:decl-index=0:visual-constraint="10,10"

	private JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;

	private JMenu fileMenu = null;

	private JMenu editMenu = null;

	private JMenu helpMenu = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem aboutMenuItem = null;

	private JMenuItem cutMenuItem = null;

	private JMenuItem copyMenuItem = null;

	private JMenuItem pasteMenuItem = null;

	private JMenuItem saveMenuItem = null;

	private JDialog aboutDialog = null; // @jve:decl-index=0:visual-constraint="601,42"

	private JPanel aboutContentPane = null;

	private JLabel aboutVersionLabel = null;

	private JPanel controlPanel = null;

	private JButton querySelectButton = null;

	private JComboBox queriesComboBox = null;

	private GenericClient client = null;

	private UserInputQuery currentQuery = null;

	private JComboBox myRolesComboBox = null;

	private OneQueryPanel inputPanel = null; // @jve:decl-index=0:

	private GridBagConstraints inputPanelConstraints = null;

	public QueryTester2(String url, Document doc) {

		try {
			this.client = new VomrsClient(url, doc);
		} catch (ClientNotInitializedException e) {
			JOptionPane
					.showMessageDialog(this.getJFrame(), "<html><body><p>"
							+ "Could not initialize VomrsClient: " + "</p><p>"
							+ e.getMessage() + "</p></body></html>",
							"Error initializing VomrsClient",
							JOptionPane.ERROR_MESSAGE);
			myLogger.debug(e.getStackTrace());
		}

		currentQuery = new UserInputQuery("getServiceArguments", client,
				"Visitor");
		try {
			currentQuery.init();
		} catch (JDOMException e) {
			JOptionPane.showMessageDialog(this.getJFrame(), "<html><body><p>"
					+ "Could not parse xml config file for VomrsClient: "
					+ "</p><p>" + e.getMessage() + "</p></body></html>",
					"Error parsing xml config file", JOptionPane.ERROR_MESSAGE);
			myLogger.debug(e.getStackTrace());
		} catch (ArgumentsException e) {
			JOptionPane
					.showMessageDialog(this.getJFrame(),
							"<html><body><p>" + "Could not parse init Query: "
									+ "</p><p>" + e.getMessage()
									+ "</p></body></html>", "Error init Query",
							JOptionPane.ERROR_MESSAGE);
			myLogger.debug(e.getStackTrace());
		}
	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(808, 430);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Query Tester");
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			inputPanelConstraints = new GridBagConstraints();
			inputPanelConstraints.gridx = 0;
			inputPanelConstraints.fill = GridBagConstraints.BOTH;
			inputPanelConstraints.weighty = 1.0;
			inputPanelConstraints.weightx = 1.0;
			inputPanelConstraints.weighty = 1.0;
			inputPanelConstraints.gridy = 1;
			GridBagConstraints controlPanelConstraints = new GridBagConstraints();
			controlPanelConstraints.gridx = 0;
			controlPanelConstraints.fill = GridBagConstraints.BOTH;
			controlPanelConstraints.gridy = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getControlPanel(), controlPanelConstraints);
			jContentPane.add(getInputPanel(), inputPanelConstraints);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes aboutDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new JDialog(getJFrame(), true);
			aboutDialog.setTitle("About");
			aboutDialog.setContentPane(getAboutContentPane());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane() {
		if (aboutContentPane == null) {
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutVersionLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getAboutVersionLabel() {
		if (aboutVersionLabel == null) {
			aboutVersionLabel = new JLabel();
			aboutVersionLabel.setText("Version 1.0");
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}

	/**
	 * This method initializes controlPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			GridBagConstraints myRolesComboBoxConstraints = new GridBagConstraints();
			myRolesComboBoxConstraints.fill = GridBagConstraints.NONE;
			myRolesComboBoxConstraints.gridy = 0;
			myRolesComboBoxConstraints.weightx = 1.0;
			myRolesComboBoxConstraints.insets = new Insets(15, 10, 15, 10);
			myRolesComboBoxConstraints.gridx = 1;
			GridBagConstraints querySelectButtonConstraints = new GridBagConstraints();
			querySelectButtonConstraints.gridx = 2;
			querySelectButtonConstraints.insets = new Insets(15, 15, 15, 15);
			GridBagConstraints queriesComboBoxConstraints = new GridBagConstraints();
			queriesComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
			queriesComboBoxConstraints.gridx = 0;
			queriesComboBoxConstraints.gridy = 0;
			queriesComboBoxConstraints.insets = new Insets(15, 15, 15, 15);
			queriesComboBoxConstraints.weightx = 1.0;
			controlPanel = new JPanel();
			controlPanel.setLayout(new GridBagLayout());
			controlPanel.add(getQuerySelectButton(),
					querySelectButtonConstraints);
			controlPanel.add(getQueriesComboBox(), queriesComboBoxConstraints);
			controlPanel.add(getMyRolesComboBox(), myRolesComboBoxConstraints);
		}
		return controlPanel;
	}

	/**
	 * This method initializes querySelectButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getQuerySelectButton() {
		if (querySelectButton == null) {
			querySelectButton = new JButton();
			querySelectButton.setText("Select");
			querySelectButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							currentQuery = new UserInputQuery(
									(String) getQueriesComboBox()
											.getSelectedItem(), client,
									(String) getMyRolesComboBox()
											.getSelectedItem());
							try {
								currentQuery.init();
							} catch (JDOMException e1) {
								JOptionPane.showMessageDialog(
										QueryTester2.this.getJFrame(),
										"<html><body><p>"
												+ "Could not parse xml config file for VomrsClient: "
												+ "</p><p>" + e1.getMessage()
												+ "</p></body></html>",
										"Error parsing xml config file",
										JOptionPane.ERROR_MESSAGE);
								myLogger.debug(e1.getStackTrace());
								currentQuery = null;
							} catch (ArgumentsException e1) {
								JOptionPane.showMessageDialog(
										QueryTester2.this.getJFrame(),
										"<html><body><p>"
												+ "Error with argument: "
												+ "</p><p>" + e1.getMessage()
												+ "</p></body></html>",
										"Argument error",
										JOptionPane.ERROR_MESSAGE);
								myLogger.debug(e1.getStackTrace());
								currentQuery = null;
							} finally {
								getJContentPane().remove(inputPanel);
								inputPanel = null;
								getJContentPane().add(getInputPanel(),
										inputPanelConstraints);
								QueryTester2.this.getJContentPane()
										.revalidate();
							}
						}
					});
		}
		return querySelectButton;
	}

	/**
	 * This method initializes queriesComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getQueriesComboBox() {
		if (queriesComboBox == null) {
			queriesComboBox = new JComboBox(client.getQueries());
		}
		return queriesComboBox;
	}

	/**
	 * This method initializes myRolesComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getMyRolesComboBox() {
		if (myRolesComboBox == null) {
			myRolesComboBox = new JComboBox(client.getMyContexts());
		}
		return myRolesComboBox;
	}

	/**
	 * This method initializes inputPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private OneQueryPanel getInputPanel() {
		if (inputPanel == null) {
			inputPanel = new OneQueryPanel(this, null, "OK", true);
		}
		return inputPanel;
	}

	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				try {
					LocalProxy.setDefaultProxy(new GlobusProxy(new File(
							"/tmp/x509up_u1000")));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				SAXBuilder builder = new SAXBuilder();

				Document doc = null;

				try {
					doc = builder
							.build(new File(
									"/home/markus/workspace/qc//src/main/java/org/vpac/qc/model/clients/queries.xml"));
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					myLogger.error(e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					myLogger.error(e);
				}

				QueryTester2 application = new QueryTester2(
						"https://vomrsdev.vpac.org:8443/vo/Chris/services/VOMRS?wsdl",
						doc);
				// QueryTester2 application = new QueryTester2(
				// "https://vomrs.apac.edu.au:8443/vo/NGAdmin/services/VOMRS?wsdl",
				// new File("/home/markus/workspace/qc/queries_test.xml"));
				application.getJFrame().setVisible(true);
			}
		});
	}

	public void cancel() {
		currentQuery = null;
		getJContentPane().remove(inputPanel);
		inputPanel = null;
		getJContentPane().add(getInputPanel(), inputPanelConstraints);
		QueryTester2.this.getJContentPane().revalidate();
	}

	public UserInputQuery getQuery() {
		return currentQuery;
	}

	public void submitted(boolean success) {
		SimpleMessagePanel mp = new SimpleMessagePanel(Color.white);
		if (!success) {
			mp.setDocument("<p>An error occured:</p><p>"
					+ getQuery().getException().getMessage() + "</p>");
		} else {
			Object[] result = currentQuery.getResult();
			myLogger.debug("Result: \n");
			StringBuffer display_result = new StringBuffer();
			for (Object part : result) {
				display_result.append(part.toString() + "<br>");
				myLogger.debug(part.toString());
			}
			mp.setDocument("<p>This is the answer the VOMRS server returned:</p><p>"
					+ display_result.toString() + "</p>");
		}
		JOptionPane.showMessageDialog(this.getJFrame(), mp,
				"Query successfull", JOptionPane.PLAIN_MESSAGE);
	}

	public boolean tellUser() {
		// nothing to tell
		return true;
	}
}
