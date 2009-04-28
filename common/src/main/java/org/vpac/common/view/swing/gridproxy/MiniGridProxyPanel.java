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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.GridProxyEvent;
import org.vpac.common.model.gridproxy.GridProxyListener;
import org.vpac.common.model.gridproxy.LocalProxy;
import java.awt.Font;

/**
 * This one is the same as the GridProxyPanel, just smaller.
 * 
 * @author Markus Binsteiner
 *
 */
public class MiniGridProxyPanel extends JPanel implements GridProxyListener {

	private static ResourceBundle messages = ResourceBundle.getBundle(
			"GridProxyPanelMessageBundle", java.util.Locale.getDefault());  //  @jve:decl-index=0:

	private static final long serialVersionUID = 1L;
	
	private static final long expiredProxyWarning = 1800;

	private JTextField timeLeftTextField = null;

	private JButton toggleButton = null;

	private JPasswordField passphraseField = null;

	private Timer updatePanel = null;

	private ImageIcon currentIcon = null;

	private ImageIcon redLight = null;  //  @jve:decl-index=0:

	private ImageIcon yellowLight = null;

	private ImageIcon greenLight = null;  //  @jve:decl-index=0:

	private ImageIcon greyLight = null;

	private Timer blinkTimer = null;

	private Action blinkingAction = null;  //  @jve:decl-index=0:

	private Action updatePanelAction = null;  //  @jve:decl-index=0:

	private JLabel passphraseLabel = null;

	private JLabel imageLabel = null;
	
	private boolean warning = false;

	/**
	 * This is the default constructor
	 */
	public MiniGridProxyPanel() {
		super();
		LocalProxy.addStatusListener(this);
		initTimer();
		initialize();
		LocalProxy.getDefaultProxy().fireStatusChanged();
	}

	public void initTimer() {

		redLight = new ImageIcon(getClass().getResource(
				"/org/vpac/common/view/swing/gridproxy/redLight_small.png"));
		yellowLight = new ImageIcon(getClass().getResource(
				"/org/vpac/common/view/swing/gridproxy/yellowLight_small.png"));
		greenLight = new ImageIcon(getClass().getResource(
				"/org/vpac/common/view/swing/gridproxy/greenLight_small.png"));
		greyLight = new ImageIcon(getClass().getResource("/org/vpac/common/view/swing/gridproxy/greyLight_small.png"));

		currentIcon = redLight;

		updatePanelAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				getTimeLeftTextField().setText(
						LocalProxy.getDefaultProxy().getFormatedTimeWithoutSeconds());
				if ( !warning && LocalProxy.getDefaultProxy().getTimeLeft() < expiredProxyWarning ){
					setCurrentIcon(yellowLight);
					blinkTimer.start();
					MiniGridProxyPanel.this.setToolTipText(getMessages().getString("status.proxyAboutToExpire"));
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
		GridBagConstraints imageLabelconstraints = new GridBagConstraints();
		imageLabelconstraints.gridx = 2;
		imageLabelconstraints.anchor = GridBagConstraints.EAST;
		imageLabelconstraints.fill = GridBagConstraints.NONE;
		imageLabelconstraints.weightx = 0.0;
		imageLabelconstraints.insets = new Insets(0, 0, 0, 10);
		imageLabelconstraints.gridwidth = 1;
		imageLabelconstraints.gridy = 0;
		imageLabel = new JLabel();
		//imageLabel.setText("tt");
		GridBagConstraints passphraseLabelConstraints = new GridBagConstraints();
		passphraseLabelConstraints.gridx = 2;
		passphraseLabelConstraints.insets = new Insets(0, 10, 0, 10);
		passphraseLabelConstraints.anchor = GridBagConstraints.NORTHEAST;
		passphraseLabelConstraints.weightx = 1.0;
		passphraseLabelConstraints.gridwidth = 2;
		passphraseLabelConstraints.gridy = 2;
		passphraseLabel = new JLabel();
		passphraseLabel.setText("Private key passphrase");
		passphraseLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		GridBagConstraints toggleButtonConstraints = new GridBagConstraints();
		toggleButtonConstraints.gridx = 1;
		toggleButtonConstraints.insets = new Insets(15, 10, 10, 10);
		toggleButtonConstraints.gridwidth = 2;
		toggleButtonConstraints.weighty = 1.0;
		toggleButtonConstraints.anchor = GridBagConstraints.NORTHEAST;
		toggleButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
		toggleButtonConstraints.gridheight = 1;
		toggleButtonConstraints.weightx = 1.0;
		toggleButtonConstraints.gridy = 3;
		GridBagConstraints passphraseFieldConstraints = new GridBagConstraints();
		passphraseFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		passphraseFieldConstraints.gridy = 1;
		passphraseFieldConstraints.weightx = 1.0;
		passphraseFieldConstraints.insets = new Insets(0, 10, 4, 10);
		passphraseFieldConstraints.gridwidth = 4;
		passphraseFieldConstraints.weighty = 1.0;
		passphraseFieldConstraints.anchor = GridBagConstraints.CENTER;
		passphraseFieldConstraints.gridx = 0;
		GridBagConstraints timeLeftTextFieldConstraints = new GridBagConstraints();
		timeLeftTextFieldConstraints.fill = GridBagConstraints.BOTH;
		timeLeftTextFieldConstraints.gridy = 0;
		timeLeftTextFieldConstraints.weightx = 1.0;
		timeLeftTextFieldConstraints.insets = new Insets(20, 10, 20, 32);
		timeLeftTextFieldConstraints.gridwidth = 3;
		timeLeftTextFieldConstraints.gridheight = 1;
		timeLeftTextFieldConstraints.gridx = 0;
		this.setSize(207, 173);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(412, 294));
		this.setBackground(new Color(245, 245, 245));
		this.setBorder(BorderFactory.createTitledBorder(null, "Local proxy", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		this.setName("panel");
		this.add(getTimeLeftTextField(), timeLeftTextFieldConstraints);
		this.add(getPassphraseField(), passphraseFieldConstraints);
		this.add(getToggleButton(), toggleButtonConstraints);
		this.add(passphraseLabel, passphraseLabelConstraints);
		this.add(imageLabel, imageLabelconstraints);
	}

	public void gridProxyStatusChanged(GridProxyEvent e) {
		
		warning = false;

		if (GridProxy.INITIALIZED == e.getStatus()) {
			getTimeLeftTextField().setText(
					LocalProxy.getDefaultProxy().getFormatedTimeWithoutSeconds());
			getTimeLeftTextField().setToolTipText(messages.getString("tooltip.timeleft"));
			getToggleButton().setToolTipText(messages.getString("tooltip.button.destroy"));
			getToggleButton().setEnabled(true);
			getToggleButton().setText("Destroy");
			//getPassphraseField().setEnabled(false);
			getPassphraseField().setEditable(false);
			getPassphraseField().setText("");
			setCurrentIcon(greenLight);
			blinkTimer.stop();
			updatePanel.start();
		}

		else if (GridProxy.INITIALIZING == e.getStatus()) {
			getToggleButton().setEnabled(false);
			setCurrentIcon(yellowLight);
			updatePanel.stop();
			blinkTimer.start();
		}

		else if (GridProxy.MISSING_PREREQUISITES == e.getStatus()) {
			getTimeLeftTextField().setToolTipText(messages.getString("tooltip.timeleft.missingPrerequisites"));
			getToggleButton().setEnabled(false);
			setCurrentIcon(greyLight);
			updatePanel.stop();
			blinkTimer.start();
		}

		else {
			getTimeLeftTextField().setText(getMessages().getString("noProxyShort"));
			getTimeLeftTextField().setToolTipText(messages.getString("tooltip.timeleft"));
			getToggleButton().setToolTipText(messages.getString("tooltip.button.init"));
			getToggleButton().setEnabled(true);
			getToggleButton().setText("Init");
			//getPassphraseField().setEnabled(true);
			getPassphraseField().setEditable(true);
			setCurrentIcon(redLight);
			updatePanel.stop();
			
			blinkTimer.stop();
		}

	}

	private void setCurrentIcon(ImageIcon icon) {
		currentIcon = icon;
		this.imageLabel.setIcon(icon);
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
			toggleButton.setMinimumSize(new Dimension(110,25));
			//toggleButton.setText("OK");
			//toggleButton.setIcon(new ImageIcon(getClass().getResource("/org/vpac/common/view/swing/gridproxy/greyLight_small.png")));
			toggleButton.setBackground(new Color(245, 245, 245));
			toggleButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					
					new Thread() {

						public void run() {

							Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
							setCursor(hourglassCursor);

					if (LocalProxy.getStatus() != GridProxy.INITIALIZED) {
						try {
							LocalProxy.destroy();
							LocalProxy.createPlainGlobusProxy(
									getPassphraseField().getPassword(), 43200000);
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
						} catch (Exception e2) {
							JOptionPane
							.showMessageDialog(
									getParent(),
									"<html><body><p>"
											+ getMessages()
													.getString(
															"error.init.exception")
											+ "</p><p>"
											+ e2.getMessage()
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
							Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
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
			passphraseField.setToolTipText(messages.getString("tooltip.passphrase"));
		}
		return passphraseField;
	}

	public static ResourceBundle getMessages() {
		return messages;
	}



}  //  @jve:decl-index=0:visual-constraint="10,10" 
