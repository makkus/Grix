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

package org.vpac.grix.view.swing.certificate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.vpac.common.model.GlobusLocations;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.grix.view.swing.Grix;

public class CertificateStatusPanel extends JPanel implements CertificateStatusListener{

	private static final long serialVersionUID = 1L;
	private JLabel imageLabel = null;
	private MessagePanel messagePanel = null;  //  @jve:decl-index=0:
	
	private ImageIcon redLight = null;
	private ImageIcon yellowLight = null;
	private ImageIcon greenLight = null;  //  @jve:decl-index=0:
	private ImageIcon greyLight = null;  //  @jve:decl-index=0:
	
	private ImageIcon currentIcon = null;
	
	private Timer timer = null;
	private Action blinkerAction = null;  //  @jve:decl-index=0:

	/**
	 * This is the default constructor
	 */
	public CertificateStatusPanel() {
		super();
		initTimer();
		initialize();
	}
	
	public void initTimer(){
		
		redLight = new ImageIcon(getClass().getResource("/org/vpac/grix/images/redLight.png"));
		yellowLight = new ImageIcon(getClass().getResource("/org/vpac/grix/images/yellowLight.png"));
		greenLight = new ImageIcon(getClass().getResource("/org/vpac/grix/images/greenLight.png"));
		greyLight = new ImageIcon(getClass().getResource("/org/vpac/grix/images/greyLight.png"));
		
		currentIcon = redLight;
		
		blinkerAction = new AbstractAction() {
		    boolean shouldDraw = false;  //  @jve:decl-index=0:
		    public void actionPerformed(ActionEvent e) {
			if (shouldDraw = !shouldDraw) {
			    colorLight();
			} else {
			    greyLight();
			}
		    }
		};
		timer = new Timer(600, blinkerAction);
		
	}
	
//	public CertificateStatusPanel(){
//		super();
//		initialize();
//		initTimer();
//	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.insets = new Insets(15, 15, 15, 15);
		gridBagConstraints2.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.insets = new Insets(10, 10, 10, 11);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 0;
		imageLabel = new JLabel();
		imageLabel.setText("");
		imageLabel.setIcon(currentIcon);
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(245, 245, 245));
		this.setPreferredSize(new Dimension(360, 76));
		this.setSize(new Dimension(360, 76));
		this.add(imageLabel, gridBagConstraints);
		this.add((JPanel)getMessagePanel(), gridBagConstraints2);
	}

	/**
	 * This method initializes messagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public MessagePanel getMessagePanel() {
		if (messagePanel == null) {
			//messagePanel = new XHTMLMessagePanel();
			messagePanel = new SimpleMessagePanel(Color.white);
		}
		return messagePanel;
	}
	
	public void setMessage(String message){
		messagePanel.setDocument(message);
	}
	
	private void colorLight(){
		this.imageLabel.setIcon(currentIcon);
	}
	private void greyLight(){
		this.imageLabel.setIcon(greyLight);
	}

	public void statusChanged(CertificateEvent event) {
		
		String status = event.getStatus();

		setMessage(Grix.getMessages().getString(status));
		
		if ( CertificatePanel.CERT_EXPIRED.equals(status) ) {
			timer.start();
			setCurrentIcon(redLight);
		} else if ( CertificatePanel.CERT_EXPIRING.equals(status) ) {
			timer.start();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.CERT_PRESENT_EXPORTED.equals(status) ){
			timer.stop();
			setCurrentIcon(greenLight);
			setMessage(Grix.getMessages().getString(status)+"<p><a href=\""+GlobusLocations.defaultLocations().getUserCertPKCS12().toString()+
						"\">"+GlobusLocations.defaultLocations().getUserCertPKCS12().toString()+"</a><p>"+
						Grix.getMessages().getString(status+".firefoxHelp")+"</p>");
		} else if ( CertificatePanel.CERT_PRESENT_NOT_EXPORTED.equals(status) ) {
			timer.stop();
			setCurrentIcon(greenLight);
		}else if ( CertificatePanel.READY_ON_CA_SERVER.equals(status)){
			timer.start();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.REQUESTED.equals(status) ){
			timer.stop();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.REQUEST_CREATED.equals(status) ){
			timer.start();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.DOWNLOADING_FROM_CA_SERVER.equals(status) ){
			timer.start();
			setCurrentIcon(greenLight);
		} else if ( CertificatePanel.QUERYING_CA_SERVER.equals(status) ){
			timer.start();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.UPLOADING.equals(status) ){
			timer.start();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.CERT_PRESENT_READ_ERROR.equals(status) ){
			timer.stop();
			setCurrentIcon(redLight);
		} else if ( CertificatePanel.NO_CERT.equals(status) ){
			timer.stop();
			setCurrentIcon(redLight);
		} else if ( CertificatePanel.RENEW_REQUESTED.equals(status) || CertificatePanel.RENEW_REQUESTED.equals(status) ) {
			timer.start();
			setCurrentIcon(yellowLight);
		} else if ( CertificatePanel.RENEW_READY_ON_CA_SERVER.equals(status) ){
			timer.start();
			setCurrentIcon(greenLight);
		} else if ( CertificatePanel.UPLOADING_FAILED.equals(status) ) {
			timer.start();
			setCurrentIcon(redLight);
		}
		
	}

	private void setCurrentIcon(ImageIcon icon) {
		currentIcon = icon;
		imageLabel.setIcon(icon);
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
