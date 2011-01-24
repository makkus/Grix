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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.vpac.common.exceptions.MissingInformationException;
import org.vpac.common.model.GlobusLocations;
import org.vpac.common.view.swing.messagePanel.InfoPagePanel;
import org.vpac.common.view.swing.messagePanel.MessagePanel;
import org.vpac.common.view.swing.messagePanel.SimpleMessagePanel;
import org.vpac.grix.control.certificate.CreateRequestInterface;
import org.vpac.grix.control.certificate.ManageCertificate;
import org.vpac.grix.control.utils.DateHelper;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.grix.control.utils.UserProperty;
import org.vpac.grix.exceptions.certificate.UnableToDownloadCertificateException;
import org.vpac.grix.exceptions.certificate.UnableToUploadCertificationRequestException;
import org.vpac.grix.model.certificate.Certificate;
import org.vpac.grix.model.certificate.CertificationRequest;
import org.vpac.grix.model.certificate.PKCS12Certificate;
import org.vpac.grix.view.swing.Grix;

public class CertificatePanel extends JPanel implements
		CertificateStatusListener, CreateRequestInterface {

	static final Logger myLogger = Logger.getLogger(CertificatePanel.class
			.getName()); // @jve:decl-index=0:

	private static final long serialVersionUID = 1L;

	private JPanel buttonPanel = null;

	private JPanel currentInfoPanel = null;

	private Certificate cert = null;

	private CertificateInfoPanel certificateInfoPanel = null;

	private CertificateStatusPanel certificateStatusPanel = null;

	private JButton createButton = null;

	private JButton retrieveButton = null;

	private JButton exportButton = null;

	private InfoPagePanel certificateRequestedPanel = null;

	private RequestUserCertificatePanel requestUserCertificatePanel = null;

	private boolean toggle_button_renew = true;

	private String currentStatus = null;

	private Color base_color = null; // @jve:decl-index=0:

	private Color lighter_color = null; // @jve:decl-index=0:

	private Vector<CertificateStatusListener> listeners = null; // @jve:decl-index=0:

	private GridBagConstraints infoOrRequestPanelConstraints = null;

	public final static String UPLOADING_FAILED = "CertificateStatus.upload.failed";

	public final static String RENEW_REQUESTED = "CertificateStatus.renew.requested.notReady";

	public final static String RENEW_READY_ON_CA_SERVER = "CertificateStatus.renew.requested.ready"; // @jve:decl-index=0:

	public final static String RENEW_REQUEST_CREATED = "CertificateStatus.renew.requested.notUploaded";

	public final static String NO_CERT = "CertificateStatus.noCertificate";

	public final static String UPLOADING = "CertificateStatus.uploading";

	public final static String REQUESTED = "CertificateStatus.requested.notReady"; // @jve:decl-index=0:

	public final static String REQUEST_CREATED = "CertificateStatus.requested.notUploaded"; // @jve:decl-index=0:

	public final static String QUERYING_CA_SERVER = "CertificateStatus.queryingCAServer";

	public final static String READY_ON_CA_SERVER = "CertificateStatus.requested.ready"; // @jve:decl-index=0:

	public final static String DOWNLOADING_FROM_CA_SERVER = "CertificateStatus.downloading";

	public final static String CERT_PRESENT_EXPORTED = "CertificateStatus.ready.exported";

	public final static String CERT_PRESENT_NOT_EXPORTED = "CertificateStatus.ready.notExported";

	public final static String CERT_PRESENT_READ_ERROR = "CertificateStatus.ready.readError";

	public final static String CERT_EXPIRED = "CertificateStatus.expired";

	public final static String CERT_EXPIRING = "CertificateStatus.expiring";

	private JButton renewButton = null;

	// private CertificateStatusListener parentListener = null;

	/**
	 * This is the default constructor
	 */
	public CertificatePanel(CertificateStatusListener parentListener) {
		super();
		listeners = new Vector<CertificateStatusListener>();
		initCert();
		initialize();
		if (parentListener != null) {
			this.addStatusListener(parentListener);
		}
		this.fireStatusChanged();
	}

	private void initCert() {

		try {
			cert = new Certificate(GlobusLocations.defaultLocations()
					.getUserCert());
			// currentStatus = ;
		} catch (IOException e) {
			cert = new Certificate();
		} catch (GeneralSecurityException e) {
			cert = new Certificate();
		}

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints statusPanelConstraints = new GridBagConstraints();
		statusPanelConstraints.gridx = 0;
		statusPanelConstraints.weightx = 1.0;
		statusPanelConstraints.gridwidth = 2;
		statusPanelConstraints.fill = GridBagConstraints.BOTH;
		statusPanelConstraints.insets = new Insets(15, 15, 15, 15);
		statusPanelConstraints.weighty = 0.6;
		statusPanelConstraints.gridy = 1;
		infoOrRequestPanelConstraints = new GridBagConstraints();
		infoOrRequestPanelConstraints.gridx = 1;
		infoOrRequestPanelConstraints.fill = GridBagConstraints.BOTH;
		infoOrRequestPanelConstraints.weightx = 1.0;
		infoOrRequestPanelConstraints.weighty = 0.6;
		infoOrRequestPanelConstraints.insets = new Insets(15, 15, 15, 15);
		infoOrRequestPanelConstraints.gridy = 0;
		GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
		buttonPanelConstraints.gridx = 0;
		buttonPanelConstraints.fill = GridBagConstraints.BOTH;
		buttonPanelConstraints.weightx = 0.0;
		buttonPanelConstraints.weighty = 1.0;
		buttonPanelConstraints.insets = new Insets(15, 15, 15, 15);
		buttonPanelConstraints.gridy = 0;
		this.setSize(589, 417);
		this.setLayout(new GridBagLayout());

		// check whether openca feature is disabled
		if ("yes".equals(GrixProperty
				.getString("disable.certification.request"))) {
			if (!(GlobusLocations.defaultLocations().userKeyExists() && GlobusLocations
					.defaultLocations().userCertExists())) {
				JPanel infopanel = new InfoPagePanel(
						"cert_request_not_available");
				this.setLayout(new BorderLayout());
				this.add(infopanel, BorderLayout.CENTER);
				return;
			}
		}

		this.add(getButtonPanel(), buttonPanelConstraints);

		this.add(getCertificateStatusPanel(), statusPanelConstraints);
		this.addStatusListener(this);
		this.addStatusListener(getCertificateStatusPanel());

	}

	public void statusChanged(CertificateEvent statusEvent) {

		String status = statusEvent.getStatus();

		// if RENEW or present
		if (RENEW_REQUESTED.equals(status)
				|| RENEW_READY_ON_CA_SERVER.equals(status)
				|| RENEW_REQUEST_CREATED.equals(status)) {
			this.getRenewButton().setText("Renew");
			this.toggle_button_renew = true;
			if (currentInfoPanel != null)
				this.remove(currentInfoPanel);
			this.add(getCertificateInfoPanel(), infoOrRequestPanelConstraints);
			this.currentInfoPanel = getCertificateInfoPanel();
			if (!RENEW_REQUEST_CREATED.equals(status))
				this.getRenewButton().setEnabled(false);
			this.getCreateButton().setEnabled(false);
			this.getRetrieveButton().setEnabled(true);
			this.getExportButton().setEnabled(false);
		}
		// if PRESENT
		else if (CERT_PRESENT_EXPORTED.equals(status)
				|| CERT_PRESENT_NOT_EXPORTED.equals(status)
				|| CERT_EXPIRED.equals(status) || CERT_EXPIRING.equals(status)) {

			if (currentInfoPanel != null)
				this.remove(currentInfoPanel);
			this.add(getCertificateInfoPanel(), infoOrRequestPanelConstraints);
			this.currentInfoPanel = getCertificateInfoPanel();

			this.getCreateButton().setEnabled(false);
			this.getRetrieveButton().setEnabled(false);
			// if (CERT_PRESENT_EXPORTED.equals(status))
			// this.getExportButton().setEnabled(false);
			// else
			this.getExportButton().setEnabled(true);
			this.getRenewButton().setText("Renew");
			this.toggle_button_renew = true;
			this.getRenewButton().setEnabled(true);
		}
		// if REQUESTED
		else if (REQUESTED.equals(status) || REQUEST_CREATED.equals(status)
				|| READY_ON_CA_SERVER.equals(status)) {

			if (currentInfoPanel != null)
				this.remove(currentInfoPanel);
			this.add(getCertificateRequestedPanel(),
					infoOrRequestPanelConstraints);
			this.currentInfoPanel = getCertificateRequestedPanel();
			this.getCreateButton().setEnabled(false);
			this.getExportButton().setEnabled(false);
			this.getRetrieveButton().setEnabled(true);
			this.toggle_button_renew = true;
			this.getRenewButton().setText("Renew");
			this.getRenewButton().setEnabled(false);
		}
		// // if READY_ON_CA_SERVER
		// else if (READY_ON_CA_SERVER.equals(status)) {
		//
		// if (currentInfoPanel != null)
		// this.remove(currentInfoPanel);
		// this.add(getCertificateInfoPanel(), infoOrRequestPanelConstraints);
		// this.currentInfoPanel = getCertificateInfoPanel();
		// this.getCreateButton().setEnabled(false);
		// this.getExportButton().setEnabled(false);
		// this.getRetrieveButton().setEnabled(true);
		// this.toggle_button_renew = true;
		// this.getRenewButton().setText("Renew");
		// this.getRenewButton().setEnabled(false);
		// }

		// if NO_CERT
		else if (NO_CERT.equals(status)) {

			if (currentInfoPanel != null)
				this.remove(currentInfoPanel);
			if (!"yes".equals(GrixProperty
					.getString("disable.certification.request"))) {
				this.add(getRequestUserCertificatePanel(),
						infoOrRequestPanelConstraints);
				this.currentInfoPanel = getRequestUserCertificatePanel();
				this.getRetrieveButton().setEnabled(false);
				this.getExportButton().setEnabled(false);
				this.getCreateButton().setEnabled(true);
				this.toggle_button_renew = true;
				this.getRenewButton().setText("Renew");
				this.getRenewButton().setEnabled(false);
			}
		}
		revalidate();

	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {

		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(0, 15, 15, 15);
			gridBagConstraints.gridy = 3;
			GridBagConstraints exportConstraints = new GridBagConstraints();
			exportConstraints.gridx = 0;
			exportConstraints.anchor = GridBagConstraints.NORTH;
			exportConstraints.insets = new Insets(15, 15, 0, 15);
			exportConstraints.fill = GridBagConstraints.HORIZONTAL;
			exportConstraints.weightx = 0.0;
			exportConstraints.weighty = 1.0;
			exportConstraints.gridy = 2;
			GridBagConstraints retrieveConstraints = new GridBagConstraints();
			retrieveConstraints.gridx = 0;
			retrieveConstraints.fill = GridBagConstraints.HORIZONTAL;
			retrieveConstraints.anchor = GridBagConstraints.NORTH;
			retrieveConstraints.insets = new Insets(15, 15, 0, 15);
			retrieveConstraints.weightx = 0.0;
			retrieveConstraints.weighty = 0.0;
			retrieveConstraints.gridy = 1;
			GridBagConstraints createConstraints = new GridBagConstraints();
			createConstraints.gridx = 0;
			createConstraints.fill = GridBagConstraints.HORIZONTAL;
			createConstraints.anchor = GridBagConstraints.NORTH;
			createConstraints.insets = new Insets(15, 15, 0, 15);
			createConstraints.weighty = 0.0;
			createConstraints.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.setBackground(getLighterColor());
			buttonPanel.setBorder(BorderFactory.createTitledBorder(null, "",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null));
			if (!"yes".equals(GrixProperty
					.getString("disable.certification.request"))) {
				buttonPanel.add(getCreateButton(), createConstraints);
				buttonPanel.add(getRetrieveButton(), retrieveConstraints);
				buttonPanel.add(getRenewButton(), gridBagConstraints);
			}
			buttonPanel.add(getExportButton(), exportConstraints);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes certificateInfoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificateInfoPanel getCertificateInfoPanel() {
		if (certificateInfoPanel == null) {
			certificateInfoPanel = new CertificateInfoPanel(cert);
			certificateInfoPanel.setBackground(getLighterColor());
		}
		return certificateInfoPanel;
	}

	/**
	 * This method initializes requestUserCertificatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private RequestUserCertificatePanel getRequestUserCertificatePanel() {
		if (requestUserCertificatePanel == null) {
			requestUserCertificatePanel = new RequestUserCertificatePanel();
			requestUserCertificatePanel.setBackground(getLighterColor());
		}
		return requestUserCertificatePanel;
	}

	/**
	 * This method initializes certificateRequestedPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private InfoPagePanel getCertificateRequestedPanel() {
		if (certificateRequestedPanel == null) {
			certificateRequestedPanel = new InfoPagePanel("requested",
					Color.white);
		}
		return certificateRequestedPanel;
	}

	/**
	 * This method initializes certificateStatusPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private CertificateStatusPanel getCertificateStatusPanel() {
		if (certificateStatusPanel == null) {
			certificateStatusPanel = new CertificateStatusPanel();
			certificateStatusPanel.setBackground(getLighterColor());
			listeners.add(certificateStatusPanel);
		}
		return certificateStatusPanel;
	}

	private void requestCertificate(CertificationRequest cert_req,
			File logFile, boolean renewal) {

		if (cert_req == null) {
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);
			getCertificateStatusPanel().setCursor(normalCursor);
			myLogger.debug("Could not create certification request. Please try again.");
			UserProperty.setProperty("REQUESTED_RENEWED_CERTIFICATE", "no");
			return;
		}

		int choice = JOptionPane
				.showConfirmDialog(this, messagePane(Grix.getMessages()
						.getString("CertificationRequest.uploadingMessage")),
						"Uploading certification request",
						JOptionPane.OK_CANCEL_OPTION);

		if (choice == JOptionPane.CANCEL_OPTION) {
			UserProperty.setProperty("REQUEST_SERIAL", "manual");
			if (renewal)
				UserProperty
						.setProperty("REQUESTED_RENEWED_CERTIFICATE", "yes");
			fireStatusChanged();
			return;
		}
		fireStatusChanged(CertificatePanel.UPLOADING);

		try {
			String[] answer = ManageCertificate.uploadCertificationRequest(
					cert_req, false, cert_req.getCn(), cert_req.getEmail(), "",
					getPhone());

			Writer fw = null;

			try {

				fw = new FileWriter(logFile);
				fw.write(answer[1]);
			} catch (IOException e) {
				myLogger.warn("Could not write certification request log file...");
			} finally {
				if (fw != null)
					try {
						fw.close();
					} catch (IOException e) {
						myLogger.error(e);
						// e.printStackTrace();
					}
			}
			// TODO parse xhtml
			choice = JOptionPane.showOptionDialog(
					this,
					messagePane(Grix.getMessages().getString(
							"CertificationRequest.uploadSuccessful")),
					answer[0], JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, new String[] { "OK",
							"Details" }, 1);
			// "OK" }, 0);
			if (choice == 0) {
				return;
			}
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			getCertificateStatusPanel().setCursor(hourglassCursor);
			getCertificateRequestedPanel().setCursor(hourglassCursor);

			MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
			messagePanel.setDocument(logFile);
			messagePanel.setPreferredSize(new Dimension(600, 400));
			JOptionPane.showMessageDialog(this, messagePanel);

		} catch (UnableToUploadCertificationRequestException e) {
			Writer fw = null;

			try {

				fw = new FileWriter(logFile);
				fw.write(e.getException().getMessage());
			} catch (Exception e1) {
				myLogger.warn("Could not write certification request log file...");
			} finally {
				if (fw != null)
					try {
						fw.close();
					} catch (IOException e1) {
						myLogger.error(e1);
						// e.printStackTrace();
					}
			}

			myLogger.error("Could not upload certification request: "
					+ e.getMessage());
			int choiceNotSuccessful = JOptionPane.showOptionDialog(
					this,
					messagePane(Grix.getMessages().getString(
							"CertificationRequest.uploadNotSuccessful")),
					"Upload not successful", JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE, null, new String[] { "OK",
							"Details" }, 1);
			if (choiceNotSuccessful != 0) {
				Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(hourglassCursor);
				getCertificateStatusPanel().setCursor(hourglassCursor);
				getCertificateRequestedPanel().setCursor(hourglassCursor);

				MessagePanel messagePanel = new SimpleMessagePanel(Color.white);
				messagePanel.setDocument(logFile);
				messagePanel.setPreferredSize(new Dimension(600, 400));
				JOptionPane.showMessageDialog(this, messagePanel);
			}
			GlobusLocations.defaultLocations().getUserKey().delete();
			GlobusLocations.defaultLocations().getUserCertRequest().delete();
			UserProperty.setProperty("REQUEST_SERIAL", "manual");
			UserProperty.setProperty("REQUESTED_RENEWED_CERTIFICATE", "no");
			clearPassphrases();
		}

	}

	private boolean retrieveCertificate(CertificationRequest cert_req,
			File locationForCert, boolean overwrite) {

		if (!overwrite) {
			if (locationForCert.exists()) {
				message((Grix.getMessages()
						.getString("CertificationRequest.downloading.alreadyPresent"))
						+ ": " + locationForCert);
				return false;
			}
		}

		Certificate cert = null;
		try {
			cert = ManageCertificate.downloadCertificate(cert_req.getC(),
					cert_req.getO(), cert_req.getOu(), cert_req.getCn(),
					cert_req.getEmail());
		} catch (GeneralSecurityException e) {
			message(Grix.getMessages().getString(
					"CertificationRequest.downloading.cantConvertServerAnswer")
					+ "<p>" + e.getMessage() + "</p>");
			return false;
		} catch (UnableToDownloadCertificateException e) {
			message(Grix.getMessages().getString(
					"CertificationRequest.downloading.cantDownloadCertificate")
					+ "<p>" + e.getMessage() + "</p>");
			return false;
		}

		if (cert == null) {
			message(Grix.getMessages().getString(
					"CertificateStatus.requested.notReadyOnCAServer"));
			return false;
		}

		try {
			cert.writeToFile(locationForCert);
		} catch (IOException e) {
			message(Grix.getMessages().getString(
					"CertificateStatus.cantWriteCertificate")
					+ locationForCert);
			return false;
		}
		message(Grix.getMessages().getString(
				"CertificateStatus.downloadSuccessfull")
				+ locationForCert);
		try {
			cert = new Certificate(GlobusLocations.defaultLocations()
					.getUserCert());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			myLogger.error(e);
			// e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		}
		getCertificateInfoPanel().refresh();
		return true;
	}

	/**
	 * This method initializes createButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCreateButton() {
		if (createButton == null) {
			createButton = new JButton();
			createButton.setText("Request");
			createButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					if (toggle_button_renew == true) {
						// create a new certificate request (no renewal)
						myLogger.debug("Create button pressed. Trying to create certification request.");
						if (GlobusLocations.defaultLocations()
								.userCertRequestExists()) {
							myLogger.debug(("There is already a certification request in the globus directory. Remove this file if you want to retrieve a new one: " + GlobusLocations
									.defaultLocations().getUserCertRequest()
									.toString()));
							return;
						}

						new Thread() {

							public void run() {

								Cursor hourglassCursor = new Cursor(
										Cursor.WAIT_CURSOR);
								setCursor(hourglassCursor);
								getCertificateStatusPanel().setCursor(
										hourglassCursor);

								CertificationRequest cert_req = null;

								cert_req = ManageCertificate
										.createAndStoreCertificationRequest(
												CertificatePanel.this,
												false,
												GrixProperty
														.getInt("default.keysize"),
												GrixProperty
														.getString("signature.algorithm"),
												null, null, false);

								String cert_req_log_file = GrixProperty
										.getString("openca.server.answer.log");
								if (cert_req_log_file == null)
									cert_req_log_file = "cert_req_log.html";
								File logFile = new File(GlobusLocations
										.defaultLocations()
										.getGlobusDirectory(),
										cert_req_log_file);
								requestCertificate(cert_req, logFile, false);
								fireStatusChanged();

								Cursor normalCursor = new Cursor(
										Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
								getCertificateStatusPanel().setCursor(
										normalCursor);
								currentInfoPanel.setCursor(normalCursor);

							}
						}.start();
					} else {
						// createa a renewal certificate request
						new Thread() {

							public void run() {

								Cursor hourglassCursor = new Cursor(
										Cursor.WAIT_CURSOR);
								setCursor(hourglassCursor);
								getCertificateStatusPanel().setCursor(
										hourglassCursor);

								CertificationRequest cert_req = null;

								cert_req = ManageCertificate
										.createAndStoreCertificationRequest(
												CertificatePanel.this,
												false,
												GrixProperty
														.getInt("default.keysize"),
												GrixProperty
														.getString("signature.algorithm"),
												GlobusLocations
														.defaultLocations()
														.getRenewUserCertRequest(),
												GlobusLocations
														.defaultLocations()
														.getRenewUserKey(),
												true);

								if (cert_req == null) {
									Cursor normalCursor = new Cursor(
											Cursor.DEFAULT_CURSOR);
									setCursor(normalCursor);
									getCertificateStatusPanel().setCursor(
											normalCursor);
									currentInfoPanel.setCursor(normalCursor);
									getCreateButton().setEnabled(true);
									clearPassphrases();
									UserProperty.setProperty(
											"REQUESTED_RENEWED_CERTIFICATE",
											"no");
									return;
								}

								myLogger.debug("New dn: "
										+ cert_req.getDNwithoutEmail());
								myLogger.debug("Old dn: " + cert.getDn());

								if (!cert_req.getDNwithoutEmail().equals(
										cert.getDn())) {
									// old dn different to new one
									int choice = JOptionPane
											.showConfirmDialog(
													CertificatePanel.this,
													messagePane(Grix
															.getMessages()
															.getString(
																	"CertificationRequest.differentDNs")),
													"Different DNs",
													JOptionPane.OK_CANCEL_OPTION);

									if (choice == JOptionPane.CANCEL_OPTION) {
										Cursor normalCursor = new Cursor(
												Cursor.DEFAULT_CURSOR);
										setCursor(normalCursor);
										getCertificateStatusPanel().setCursor(
												normalCursor);
										getCreateButton().setEnabled(true);
										currentInfoPanel
												.setCursor(normalCursor);
										UserProperty
												.setProperty(
														"REQUESTED_RENEWED_CERTIFICATE",
														"no");
										clearPassphrases();
										fireStatusChanged();
										return;
									}
								}

								String cert_req_log_file = GrixProperty
										.getString("openca.server.answer.log")
										+ ".new";
								if (cert_req_log_file == null)
									cert_req_log_file = "cert_req_log.html.new";
								File logFile = new File(GlobusLocations
										.defaultLocations()
										.getGlobusDirectory(),
										cert_req_log_file);

								requestCertificate(cert_req, logFile, true);
								UserProperty.setProperty(
										"REQUESTED_RENEWED_CERTIFICATE", "yes");
								fireStatusChanged();

								Cursor normalCursor = new Cursor(
										Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
								getCertificateStatusPanel().setCursor(
										normalCursor);
								currentInfoPanel.setCursor(normalCursor);

							}
						}.start();
					}

				}
			});

		}
		return createButton;
	}

	/**
	 * This method initializes retrieveButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRetrieveButton() {
		if (retrieveButton == null) {
			retrieveButton = new JButton();
			retrieveButton.setText("Retrieve");
			retrieveButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {

							if (!"yes".equals(UserProperty
									.getProperty("REQUESTED_RENEWED_CERTIFICATE"))) {
								// retrieve a new certificate
								Cursor hourglassCursor = new Cursor(
										Cursor.WAIT_CURSOR);
								setCursor(hourglassCursor);
								getCertificateStatusPanel().setCursor(
										hourglassCursor);

								CertificationRequest cert_req = null;
								try {
									cert_req = new CertificationRequest(
											GlobusLocations.defaultLocations()
													.getUserCertRequest(),
											GrixProperty
													.getString("signature.algorithm"),
											false);
								} catch (IOException e1) {
									message((Grix.getMessages()
											.getString("CertificationRequest.downloading.cantReadRequest"))
											+ ": "
											+ GlobusLocations
													.defaultLocations()
													.getUserCertRequest()
													.toString());
									return;
								} catch (MissingInformationException e2) {

									StringBuffer message = new StringBuffer(
											Grix.getMessages()
													.getString(
															"CertificationRequest.downloading.missingInformation")
													+ "\n\n");
									for (String info : e2
											.getMissingInformation()) {
										message.append("<br>" + info);
									}
									message(message.toString());
									return;
								}

								retrieveCertificate(cert_req, GlobusLocations
										.defaultLocations().getUserCert(),
										false);
								initCert();
								fireStatusChanged();

								Cursor normalCursor = new Cursor(
										Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
								getCertificateStatusPanel().setCursor(
										normalCursor);

							} else {
								// renewed certificate, means backup the old one
								// and get the new one
								Cursor hourglassCursor = new Cursor(
										Cursor.WAIT_CURSOR);
								setCursor(hourglassCursor);
								getCertificateStatusPanel().setCursor(
										hourglassCursor);

								CertificationRequest cert_req = null;
								try {
									cert_req = new CertificationRequest(
											GlobusLocations.defaultLocations()
													.getRenewUserCertRequest(),
											GrixProperty
													.getString("signature.algorithm"),
											false);
								} catch (IOException e1) {
									message((Grix.getMessages()
											.getString("CertificationRequest.downloading.cantReadRequest"))
											+ ": "
											+ GlobusLocations
													.defaultLocations()
													.getUserCertRequest()
													.toString());
									return;
								} catch (MissingInformationException e2) {

									StringBuffer message = new StringBuffer(
											Grix.getMessages()
													.getString(
															"CertificationRequest.downloading.missingInformation")
													+ "\n\n");
									for (String info : e2
											.getMissingInformation()) {
										message.append("<br>" + info);
									}
									message(message.toString());
									return;
								}

								boolean successRetrieve = retrieveCertificate(
										cert_req, GlobusLocations
												.defaultLocations()
												.getRenewUserCert(), true);

								if (successRetrieve) {
									int choice = JOptionPane
											.showConfirmDialog(
													CertificatePanel.this,
													messagePane(Grix
															.getMessages()
															.getString(
																	"CertificationRenewal.swapMessage")),
													"Swapping your old certificate with the renewed one.",
													JOptionPane.OK_CANCEL_OPTION);

									if (choice == JOptionPane.CANCEL_OPTION) {
										fireStatusChanged(CertificatePanel.RENEW_READY_ON_CA_SERVER);
										Cursor normalCursor = new Cursor(
												Cursor.DEFAULT_CURSOR);
										setCursor(normalCursor);
										getCertificateStatusPanel().setCursor(
												normalCursor);
										return;
									}

									// swap old cert with new one
									ManageCertificate
											.exchangeRenewedCertificateFiles();
									initCert();
									getCertificateInfoPanel().refresh();

								}

								fireStatusChanged();

								Cursor normalCursor = new Cursor(
										Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
								getCertificateStatusPanel().setCursor(
										normalCursor);

							}
						}
					});
		}
		return retrieveButton;
	}

	private MessagePanel messagePane(String message) {
		SimpleMessagePanel panel = new SimpleMessagePanel(message, Color.white);
		panel.setPreferredSize(new Dimension(350, 150));
		return panel;
	}

	private void exportCertificate() {

		// if (GlobusLocations.defaultLocations().userCertPKCS12Exists()) {
		// message(Grix.getMessages().getString(
		// "Certificate.export.alreadyPKCS12CertificatePresent")
		// + "<p>"
		// + GlobusLocations.defaultLocations().getUserCertPKCS12()
		// .toString() + "</p>");
		// return;
		// }

		PKCS12Certificate p12cert = null;
		try {
			p12cert = new PKCS12Certificate(GlobusLocations.defaultLocations()
					.getUserKey().toString(), GlobusLocations
					.defaultLocations().getUserCert().toString(),
					new Certificate(GlobusLocations.defaultLocations()
							.getUserCert()).getCn());
		} catch (IOException e) {
			message(Grix.getMessages().getString(
					"Certificate.export.cantReadCertificate")
					+ "<p>"
					+ GlobusLocations.defaultLocations().getUserCert()
							.toString() + "</p>");
			GlobusLocations.defaultLocations().getUserCertPKCS12().delete();
			return;
		} catch (GeneralSecurityException e) {
			message(Grix.getMessages().getString(
					"Certificate.export.cantParseCertificate")
					+ "<p>"
					+ GlobusLocations.defaultLocations().getUserCert()
							.toString() + "</p>");
			GlobusLocations.defaultLocations().getUserCertPKCS12().delete();
			return;
		}

		try {
			JPasswordField pwd = new JPasswordField();
			char[] passphrase = null;
			SimpleMessagePanel messagePanel = new SimpleMessagePanel(
					Color.white);
			messagePanel.setDocument(Grix.getMessages().getString(
					"Certificate.export.askForPassphrase"));
			messagePanel.setPreferredSize(new Dimension(300, 250));
			Object[] message = { messagePanel, pwd };
			int resp = JOptionPane.showConfirmDialog(this, message,
					"Passphrase", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (resp == JOptionPane.OK_OPTION) {
				passphrase = pwd.getPassword();
				boolean success = p12cert.writeToP12File(passphrase,
						GlobusLocations.defaultLocations().getUserCertPKCS12()
								.toString());
				Arrays.fill(passphrase, 'x');
				if (!success) {
					message(Grix
							.getMessages()
							.getString(
									"Certificate.export.couldNotStorePKCS12Certificate"));
				} else {
					message(Grix.getMessages().getString(
							"Certificate.export.success")
							+ GlobusLocations.defaultLocations()
									.getUserCertPKCS12().toString());
					fireStatusChanged();
				}
			}
		} catch (InvalidKeyException e) {
			message(Grix.getMessages().getString(
					"Certificate.export.wrongPassphrase"));
			if (GlobusLocations.defaultLocations().getUserCertPKCS12().length() == 0) {
				GlobusLocations.defaultLocations().getUserCertPKCS12().delete();
			}
			return;
		}

	}

	/**
	 * This method initializes exportButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportButton() {
		if (exportButton == null) {
			exportButton = new JButton();
			exportButton.setText("Export for browser");
			exportButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					exportCertificate();

				}
			});
		}
		return exportButton;
	}

	public String getCurrentCertificateStatus() {

		// test for RENEW (that means that an old cert has to be present, if
		// not, well, pray
		if ("yes".equals(UserProperty
				.getProperty("REQUESTED_RENEWED_CERTIFICATE"))) {
			if (UserProperty.getProperty("REQUEST_SERIAL") != null
					&& "manual".equals(UserProperty
							.getProperty("REQUEST_SERIAL"))) {
				return RENEW_REQUEST_CREATED;
			} else if (UserProperty.getProperty("REQUEST_SERIAL") != null) {
				// if (ManageCertificate.readyToDownload())
				// return RENEW_READY_ON_CA_SERVER;
				// else
				return RENEW_REQUESTED;
			} else {
				return RENEW_REQUEST_CREATED;
			}
		}

		// test for CERT_PRESENT
		if (GlobusLocations.defaultLocations().getUserCert().exists()) {
			try {
				Certificate cert = new Certificate(GlobusLocations
						.defaultLocations().getUserCert());

				DateFormat df = DateHelper.getDateFormat();
				Date enddate = df.parse(cert.getEnddate());

				if (enddate.before(new Date())) {
					return CERT_EXPIRED;
				}
				Calendar rightNow = Calendar.getInstance();
				rightNow.add(Calendar.MONTH, 1);
				Date inOneMonth = rightNow.getTime();
				if (enddate.before(inOneMonth)) {
					return CERT_EXPIRING;
				}

				if (GlobusLocations.defaultLocations().getUserCertPKCS12()
						.exists()
						&& GlobusLocations.defaultLocations()
								.getUserCertPKCS12().length() != 0)
					return CERT_PRESENT_EXPORTED;
				else
					return CERT_PRESENT_NOT_EXPORTED;
			} catch (Exception e) {
				currentStatus = CERT_PRESENT_READ_ERROR;
			}
		} else if (GlobusLocations.defaultLocations().getUserCertRequest()
				.exists()) {

			// test for REQUESTED with/without serial number
			if (UserProperty.getProperty("REQUEST_SERIAL") != null) {
				if ("manual".equals(UserProperty.getProperty("REQUEST_SERIAL"))) {
					// no serial number
					myLogger.debug("Manual request upload. ");
					// cert = ManageCertificate.retrieveDefaultCertificate();
					// if (cert == null)
					// return REQUESTED;
					// else {
					// cert = null;
					// return READY_ON_CA_SERVER;
					// }
					return REQUESTED;
				} else {
					// serial number
					// if (!ManageCertificate.readyToDownload()) {
					// return REQUESTED;
					// } else {
					// return READY_ON_CA_SERVER;
					// }
					return REQUESTED;
				}

			} else {
				// no REQUEST_SERIAL in the grix.properties file. This should
				// not happen anymore.
				myLogger.error("No REQUEST_SERIAL property in the grix.properties file. Please contact markus@vpac.org for debugging purposes.");
				// cert = ManageCertificate.retrieveDefaultCertificate();
				// if (cert == null)
				// return REQUESTED;
				// else {
				// cert = null;
				// return READY_ON_CA_SERVER;
				// }
				return REQUESTED;
			}
		}

		return NO_CERT;
	}

	public void fireStatusChanged() {
		// if we have no listeners, do nothing...
		if (listeners != null && !listeners.isEmpty()) {
			// create the event object to send
			CertificateEvent event = new CertificateEvent(this,
					getCurrentCertificateStatus());

			// make a copy of the listener list in case
			// anyone adds/removes listeners
			Vector targets;
			synchronized (this) {
				targets = (Vector) listeners.clone();
			}

			// walk through the listener list and
			// call the sunMoved method in each
			Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				CertificateStatusListener l = (CertificateStatusListener) e
						.nextElement();
				l.statusChanged(event);
			}
		}
	}

	public void fireStatusChanged(String status) {
		// if we have no listeners, do nothing...
		if (listeners != null && !listeners.isEmpty()) {
			// create the event object to send
			CertificateEvent event = new CertificateEvent(this, status);

			// make a copy of the listener list in case
			// anyone adds/removes listeners
			Vector targets;
			synchronized (this) {
				targets = (Vector) listeners.clone();
			}

			// walk through the listener list and
			// call the sunMoved method in each
			Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				CertificateStatusListener l = (CertificateStatusListener) e
						.nextElement();
				l.statusChanged(event);
			}
		}
	}

	/** Register a listener for CertificateEvents */
	synchronized public void addStatusListener(CertificateStatusListener l) {
		if (listeners == null)
			listeners = new Vector();
		listeners.addElement(l);
	}

	/** Remove a listener for CertificateEvents */
	synchronized public void removeStatusListener(CertificateStatusListener l) {
		if (listeners == null) {
			listeners = new Vector();
		}
		listeners.removeElement(l);
	}

	public String getC() {
		return getRequestUserCertificatePanel().getC();
	}

	public String getCN() {
		return getRequestUserCertificatePanel().getCN();
	}

	public String getEmail() {
		return getRequestUserCertificatePanel().getEmail();
	}

	public String getPhone() {
		return getRequestUserCertificatePanel().getPhone();
	}

	public String getO() {
		return getRequestUserCertificatePanel().getO();
	}

	public String getOU() {
		return getRequestUserCertificatePanel().getOU();
	}

	public char[] getPassphrase() {
		return getRequestUserCertificatePanel().getPassphrase();
	}

	public char[] getPassphrase2() {
		return getRequestUserCertificatePanel().getPassphrase2();
	}

	public void message(String string) {
		JOptionPane.showMessageDialog(this, messagePane(string),
				"Certification request", JOptionPane.WARNING_MESSAGE);
	}

	public void setStatus(String string) {
		getCertificateStatusPanel().setMessage(string);
	}

	public void clearPassphrases() {
		getRequestUserCertificatePanel().clearPassphrases();

	}

	public void lockInput() {

		getRequestUserCertificatePanel().lockInput();
		getCreateButton().setEnabled(false);
		getRetrieveButton().setEnabled(false);
		getExportButton().setEnabled(false);
	}

	public void unlockInput() {
		getRequestUserCertificatePanel().unlockInput();
		// fireStatusChanged();
	}

	public String getFirstName() {
		return getRequestUserCertificatePanel().getFirstName();
	}

	public String getLastName() {
		return getRequestUserCertificatePanel().getLastName();
	}

	public Color getBaseColor() {
		if (base_color == null) {
			base_color = this.getBackground();
		}
		return base_color;
	}

	public Color getLighterColor() {
		if (lighter_color == null) {
			int red = getBaseColor().getRed() + 10;
			int green = getBaseColor().getGreen() + 10;
			int blue = getBaseColor().getBlue() + 10;
			lighter_color = new Color(red, green, blue);
		}
		return lighter_color;
	}

	/**
	 * This method initializes renewButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRenewButton() {
		if (renewButton == null) {
			renewButton = new JButton();
			renewButton.setText("Renew");
			renewButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					myLogger.debug("Renew button pressed.");
					// if
					// (GlobusLocations.defaultLocations().userCertRequestExists()
					// ){
					// myLogger.debug(("There is already a certification request
					// in the globus
					// directory. Remove this file if you want to retrieve a new
					// one:
					// "+GlobusLocations.defaultLocations().getUserCertRequest().toString()));
					// return;
					// }

					if (toggle_button_renew) {
						// start the renewal process
						setStatus(Grix.getMessages().getString(
								"CertificationRenewal.info"));
						toggle_button_renew = false;
						getRenewButton().setText("Cancel");
						getCreateButton().setEnabled(true);
						getExportButton().setEnabled(false);
						getRetrieveButton().setEnabled(false);

						new Thread() {

							public void run() {

								Cursor hourglassCursor = new Cursor(
										Cursor.WAIT_CURSOR);
								setCursor(hourglassCursor);
								getCertificateStatusPanel().setCursor(
										hourglassCursor);

								if (currentInfoPanel != null)
									CertificatePanel.this
											.remove(currentInfoPanel);
								CertificatePanel.this.add(
										getRequestUserCertificatePanel(),
										infoOrRequestPanelConstraints);
								CertificatePanel.this.currentInfoPanel = getRequestUserCertificatePanel();
								getRequestUserCertificatePanel().prefill(cert);
								revalidate();

								// fireStatusChanged();

								Cursor normalCursor = new Cursor(
										Cursor.DEFAULT_CURSOR);
								setCursor(normalCursor);
								getCertificateStatusPanel().setCursor(
										normalCursor);

							}
						}.start();
					} else {
						// cancel the renewal process
						toggle_button_renew = true;
						getRenewButton().setText("Renew");
						requestUserCertificatePanel = null;
						fireStatusChanged();
					}

				}
			});
		}
		return renewButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"

