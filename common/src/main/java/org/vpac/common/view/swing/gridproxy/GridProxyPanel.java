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

package org.vpac.common.view.swing.gridproxy;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.GridProxyEvent;
import org.vpac.common.model.gridproxy.GridProxyListener;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;

/**
 * A panel that lets the user manage the LocalProxy via a Swing GUI. It provides
 * an Init and a Destroy button and displays the remaining time of the current
 * proxy.
 * 
 * @author Markus Binsteiner
 * 
 */
public class GridProxyPanel extends JPanel implements GridProxyListener {

	private static ResourceBundle messages = ResourceBundle.getBundle(
			"GridProxyPanelMessageBundle", java.util.Locale.getDefault());

	private static final long serialVersionUID = 1L;

	private static final long EXPIREDPROXYWARNING = 1800;

	private JLabel imageLabel = null;

	private JLabel timeLeftLabel = null;

	private JTextField timeLeftTextField = null;

	private JButton toggleButton = null;

	private JPasswordField passphraseField = null;

	private JLabel passphraseLabel = null;

	private JLabel titleLabel = null;

	private Timer updatePanel = null;

	private ImageIcon currentIcon = null;

	private ImageIcon redLight = null;

	private ImageIcon yellowLight = null;

	private ImageIcon greenLight = null;

	private ImageIcon greyLight = null;

	private Timer blinkTimer = null;

	private Action blinkingAction = null; // @jve:decl-index=0:

	private Action updatePanelAction = null; // @jve:decl-index=0:

	private MessagePanel statusPanel = null;

	private Frame dialog = null;

	private JDialog closeParent = null;

	private boolean warning = false;

	/**
	 * This is the default constructor
	 */
	public GridProxyPanel(Frame parent) {
		super();
		this.dialog = parent;
		LocalProxy.addStatusListener(this);
		initTimer();
		initialize();
		// LocalProxy.getDefaultProxy().fireStatusChanged();
		this.gridProxyStatusChanged(new GridProxyEvent(this, LocalProxy
				.getStatus()));
	}

	/**
	 * Use this constructor if the parent dialog should be closed with every
	 * button click
	 */
	public GridProxyPanel(Frame parent, JDialog closeParent) {
		super();
		this.dialog = parent;
		this.closeParent = closeParent;
		LocalProxy.addStatusListener(this);
		initTimer();
		initialize();
		// LocalProxy.getDefaultProxy().fireStatusChanged();
		this.gridProxyStatusChanged(new GridProxyEvent(this, LocalProxy
				.getStatus()));
	}

	public void initTimer() {

		redLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/redLight.png"));
		yellowLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/yellowLight.png"));
		greenLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/greenLight.png"));
		greyLight = new ImageIcon(getClass().getResource(
				"/org/vpac/grix/images/greyLight.png"));

		currentIcon = redLight;

		updatePanelAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				getTimeLeftTextField().setText(
						LocalProxy.getDefaultProxy().getHoursLeft() + "h, "
								+ LocalProxy.getDefaultProxy().getMinutesLeft()
								+ "min, "
								+ LocalProxy.getDefaultProxy().getSecondsLeft()
								+ "sec");
				if (!warning
						&& LocalProxy.getDefaultProxy().getTimeLeft() < EXPIREDPROXYWARNING) {
					setCurrentIcon(yellowLight);
					blinkTimer.start();
					getStatusPanel().setDocument(
							getMessages()
									.getString("status.proxyAboutToExpire"));
					warning = true;
				}
			}
		};

		blinkingAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;
			boolean shouldDraw = false;

			public void actionPerformed(ActionEvent e) {
				if (shouldDraw = !shouldDraw) {
					colorLight();
				} else {
					greyLight();
				}
			}
		};
		blinkTimer = new Timer(600, blinkingAction);
		updatePanel = new Timer(1000, updatePanelAction);

	}

	private void colorLight() {
		this.imageLabel.setIcon(currentIcon);
	}

	private void greyLight() {
		this.imageLabel.setIcon(greyLight);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints statusPanelConstraints = new GridBagConstraints();
		statusPanelConstraints.gridx = 0;
		statusPanelConstraints.gridwidth = 4;
		statusPanelConstraints.weightx = 1.0;
		statusPanelConstraints.weighty = 1.0;
		statusPanelConstraints.insets = new Insets(0, 20, 20, 20);
		statusPanelConstraints.gridy = 6;
		statusPanelConstraints.fill = GridBagConstraints.BOTH;
		GridBagConstraints titleLabelConstraints = new GridBagConstraints();
		titleLabelConstraints.gridx = 0;
		titleLabelConstraints.gridwidth = 4;
		titleLabelConstraints.insets = new Insets(0, 20, 0, 0);
		titleLabelConstraints.weightx = 1.0;
		titleLabelConstraints.anchor = GridBagConstraints.WEST;
		titleLabelConstraints.gridy = 0;
		titleLabel = new JLabel();
		titleLabel.setText(getMessages().getString("title"));
		GridBagConstraints passphraseLabelConstraints = new GridBagConstraints();
		passphraseLabelConstraints.gridx = 0;
		passphraseLabelConstraints.insets = new Insets(10, 20, 0, 0);
		passphraseLabelConstraints.gridy = 4;
		passphraseLabel = new JLabel();
		passphraseLabel.setText(getMessages().getString("passphrase"));
		GridBagConstraints passphraseFieldConstraints = new GridBagConstraints();
		passphraseFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		passphraseFieldConstraints.gridy = 5;
		passphraseFieldConstraints.weightx = 1.0;
		passphraseFieldConstraints.insets = new Insets(0, 20, 15, 0);
		passphraseFieldConstraints.gridwidth = 2;
		passphraseFieldConstraints.weighty = 1.0;
		passphraseFieldConstraints.anchor = GridBagConstraints.CENTER;
		passphraseFieldConstraints.gridx = 0;
		GridBagConstraints toggleButtonConstraints = new GridBagConstraints();
		toggleButtonConstraints.gridx = 3;
		toggleButtonConstraints.anchor = GridBagConstraints.EAST;
		toggleButtonConstraints.insets = new Insets(0, 20, 15, 20);
		toggleButtonConstraints.weighty = 1.0;
		toggleButtonConstraints.gridy = 5;
		GridBagConstraints timeLeftTextFieldConstraints = new GridBagConstraints();
		timeLeftTextFieldConstraints.fill = GridBagConstraints.BOTH;
		timeLeftTextFieldConstraints.gridy = 2;
		timeLeftTextFieldConstraints.weightx = 1.0;
		timeLeftTextFieldConstraints.insets = new Insets(0, 20, 30, 20);
		timeLeftTextFieldConstraints.gridwidth = 3;
		timeLeftTextFieldConstraints.gridheight = 1;
		timeLeftTextFieldConstraints.gridx = 1;
		GridBagConstraints timeLeftLabelConstraints = new GridBagConstraints();
		timeLeftLabelConstraints.gridx = 0;
		timeLeftLabelConstraints.anchor = GridBagConstraints.EAST;
		timeLeftLabelConstraints.insets = new Insets(0, 20, 30, 0);
		timeLeftLabelConstraints.gridy = 2;
		timeLeftLabel = new JLabel();
		timeLeftLabel.setText(getMessages().getString("timeleft"));
		GridBagConstraints imageLabelConstraints = new GridBagConstraints();
		imageLabelConstraints.gridx = 3;
		imageLabelConstraints.anchor = GridBagConstraints.NORTHEAST;
		imageLabelConstraints.insets = new Insets(20, 0, 30, 20);
		imageLabelConstraints.gridy = 0;
		imageLabel = new JLabel();
		this.setSize(412, 370);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(412, 294));
		this.setName("panel");
		this.add(imageLabel, imageLabelConstraints);
		this.add(timeLeftLabel, timeLeftLabelConstraints);
		this.add(getTimeLeftTextField(), timeLeftTextFieldConstraints);
		this.add(getToggleButton(), toggleButtonConstraints);
		this.add(getPassphraseField(), passphraseFieldConstraints);
		this.add(passphraseLabel, passphraseLabelConstraints);
		this.add(titleLabel, titleLabelConstraints);
		this.add((JPanel) getStatusPanel(), statusPanelConstraints);
	}

	public void gridProxyStatusChanged(GridProxyEvent e) {

		warning = false;

		if (GridProxy.INITIALIZED == e.getStatus()) {
			getTimeLeftTextField().setText(
					LocalProxy.getDefaultProxy().getFormatedTime());
			getToggleButton().setEnabled(true);
			getToggleButton().setText(
					getMessages().getString("gridProxyDestroy"));
			getPassphraseField().setEditable(false);
			setCurrentIcon(greenLight);
			blinkTimer.stop();
			updatePanel.start();
			getStatusPanel().setDocument(
					getMessages().getString("status.initialized"));
		}

		else if (GridProxy.INITIALIZING == e.getStatus()) {
			getToggleButton().setEnabled(false);
			getStatusPanel().setDocument(
					getMessages().getString("status.initializing"));
			setCurrentIcon(yellowLight);
			updatePanel.stop();
			blinkTimer.start();
		}

		else if (GridProxy.MISSING_PREREQUISITES == e.getStatus()) {
			getToggleButton().setText(getMessages().getString("gridProxyInit"));
			getToggleButton().setEnabled(true);
			getStatusPanel().setDocument(
					getMessages().getString("status.missingPrerequisites"));
			setCurrentIcon(redLight);
			updatePanel.stop();
			blinkTimer.start();
		}

		else {
			updatePanel.stop();
			blinkTimer.stop();
			getTimeLeftTextField().setText(getMessages().getString("noProxy"));
			getToggleButton().setText(getMessages().getString("gridProxyInit"));
			getToggleButton().setEnabled(true);
			getStatusPanel().setDocument(
					getMessages().getString("status.notInitialized"));
			getPassphraseField().setEditable(true);
			setCurrentIcon(redLight);
		}

	}

	public void setVisible(boolean visible) {

		if (visible) {
			LocalProxy.checkStatus();
		}
		super.setVisible(visible);

	}

	private void setCurrentIcon(ImageIcon icon) {
		currentIcon = icon;
		imageLabel.setIcon(icon);
	}

	/**
	 * This method initializes timeLeftTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTimeLeftTextField() {
		if (timeLeftTextField == null) {
			timeLeftTextField = new JTextField();
			timeLeftTextField.setEditable(false);
			timeLeftTextField.setHorizontalAlignment(JTextField.CENTER);
			timeLeftTextField.setPreferredSize(new Dimension(4, 35));
			timeLeftTextField.setBackground(Color.white);
		}
		return timeLeftTextField;
	}

	/**
	 * This method initializes toggleButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getToggleButton() {
		if (toggleButton == null) {
			toggleButton = new JButton();
			toggleButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					new Thread() {

						public void run() {

							Cursor hourglassCursor = new Cursor(
									Cursor.WAIT_CURSOR);
							setCursor(hourglassCursor);
							int oldStatus = LocalProxy.getStatus();

							if (oldStatus != GridProxy.INITIALIZED) {
								try {
									LocalProxy.destroy();
									LocalProxy.createPlainGlobusProxy(
											getPassphraseField().getPassword(),
											43200000);
								} catch (MissingPrerequisitesException e1) {
									JOptionPane
											.showMessageDialog(
													getParent(),
													"<html><body><p>"
															+ getMessages()
																	.getString(
																			"error.init.missingPrerequisites")
															+ "</p><p>"
															+ e1.getMessage()
															+ "</p></body></html>",
													getMessages().getString(
															"error.init.title"),
													JOptionPane.ERROR_MESSAGE);
								} catch (IOException e1) {
									JOptionPane
											.showMessageDialog(
													getParent(),
													"<html><body><p>"
															+ getMessages()
																	.getString(
																			"error.init.ioexception")
															+ "</p><p>"
															+ e1.getMessage()
															+ "</p></body></html>",
													getMessages().getString(
															"error.init.title"),
													JOptionPane.ERROR_MESSAGE);
								} catch (GeneralSecurityException e1) {
									JOptionPane
											.showMessageDialog(
													getParent(),
													"<html><body><p>"
															+ getMessages()
																	.getString(
																			"error.init.generalSecurityException")
															+ "</p><p>"
															+ e1.getMessage()
															+ "</p></body></html>",
													getMessages().getString(
															"error.init.title"),
													JOptionPane.ERROR_MESSAGE);
								} catch (Exception e) {
									JOptionPane
											.showMessageDialog(
													getParent(),
													"<html><body><p>"
															+ getMessages()
																	.getString(
																			"error.init.exception")
															+ "</p><p>"
															+ e.getMessage()
															+ "</p></body></html>",
													getMessages().getString(
															"error.init.title"),
													JOptionPane.ERROR_MESSAGE);
								} finally {
									getPassphraseField().setText("");
								}
							} else {
								LocalProxy.destroy();
							}
							if (oldStatus != LocalProxy.getStatus()
									&& closeParent != null) {
								// status changed, close window
								closeParent.setVisible(false);
							}
							Cursor normalCursor = new Cursor(
									Cursor.DEFAULT_CURSOR);
							setCursor(normalCursor);

						}
					}.start();

				}
			});
		}
		return toggleButton;
	}

	/**
	 * This method initializes passphraseField
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getPassphraseField() {
		if (passphraseField == null) {
			passphraseField = new JPasswordField();
		}
		return passphraseField;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private MessagePanel getStatusPanel() {
		if (statusPanel == null) {
			statusPanel = new SimpleMessagePanel();
			statusPanel.setPreferredSize(new Dimension(300, 100));
		}
		return statusPanel;
	}

	public static ResourceBundle getMessages() {
		return messages;
	}

	public Frame getDialog() {
		return dialog;
	}

}
