/* Copyright 2006 VPAC
 * 
 * This file is part of Grix.
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

package org.vpac.grix.control.certificate;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.vpac.common.exceptions.MissingInformationException;
import org.vpac.common.model.GlobusLocations;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.grix.control.utils.UserProperty;
import org.vpac.grix.control.utils.Utils;
import org.vpac.grix.exceptions.certificate.UnableToDownloadCertificateException;
import org.vpac.grix.exceptions.certificate.UnableToUploadCertificationRequestException;
import org.vpac.grix.model.certificate.Certificate;
import org.vpac.grix.model.certificate.CertificationRequest;
import org.vpac.grix.plugins.openca.OpenCA;

/**
 * Controls the retrieval and storage of the certificate. Should be easy to
 * implement some kind of plugin architecture so that other CA software than
 * OpenCA can be used.
 * 
 * @author Markus Binsteiner
 * 
 */
public class ManageCertificate {

	static final Logger myLogger = Logger.getLogger(ManageCertificate.class
			.getName());

	static ResourceBundle messages = ResourceBundle.getBundle(
			"ControlMessagesBundle", java.util.Locale.getDefault());

	/**
	 * Get the certificate from server and store it afterwards.
	 * 
	 * @return the certificate or null (if not found on the server)
	 * @throws GeneralSecurityException
	 *             If the answer string can't be translated into a certificate
	 * @throws UnableToDownloadCertificateException
	 *             If the certificate can't be downloaded for whatever reason
	 */
	public static Certificate downloadCertificate(String c, String o,
			String ou, String cn, String email)
			throws GeneralSecurityException,
			UnableToDownloadCertificateException {

		// start downloading
		myLogger.debug("Trying to get certificate with DN: C=" + c + ", O=" + o
				+ ", OU=" + ou + ", CN=" + cn);

		if (!readyToDownload())
			return null;

		// TODO implement plugin architecture
		String cert = null;
		try {
			cert = OpenCA.downloadCertificate(c, o, ou, cn, email);
		} catch (Exception e) {
			throw new UnableToDownloadCertificateException("openca", e);
		}
		myLogger.debug("Answer from server: \n" + cert);
		if (cert != null && cert.indexOf(Certificate.PEM_HEADER) != -1) {
			Certificate newCert = new Certificate(cert);
			Certificate localCert = null;
			try {
				localCert = new Certificate(GlobusLocations.defaultLocations()
						.getUserCert());
			} catch (Exception e) {
				// can't create old cert, asuming this is the new one...
				return newCert;
			}

			if (localCert.getEnddate().equals(newCert.getEnddate())) {
				// myLogger.debug("Your certificate is not ready yet. Please try it again later.");
				return null;
			}
			// this means the certificate is ready
			return newCert;

		} else {
			return null;
		}
	}

	public static boolean exchangeRenewedCertificateFiles() {

		if (GlobusLocations.defaultLocations().getUserCert().exists()) {
			if (!GlobusLocations
					.defaultLocations()
					.getUserCert()
					.renameTo(
							Utils.backupFile(GlobusLocations.defaultLocations()
									.getUserCert())))
				return false;
		}
		if (GlobusLocations.defaultLocations().getUserKey().exists()) {
			if (!GlobusLocations
					.defaultLocations()
					.getUserKey()
					.renameTo(
							Utils.backupFile(GlobusLocations.defaultLocations()
									.getUserKey())))
				return false;
		}
		if (GlobusLocations.defaultLocations().getUserCertRequest().exists()) {
			if (!GlobusLocations
					.defaultLocations()
					.getUserCertRequest()
					.renameTo(
							Utils.backupFile(GlobusLocations.defaultLocations()
									.getUserCertRequest())))
				return false;
		}
		if (GlobusLocations.defaultLocations().getUserCertPKCS12().exists()) {
			if (!GlobusLocations
					.defaultLocations()
					.getUserCertPKCS12()
					.renameTo(
							Utils.backupFile(GlobusLocations.defaultLocations()
									.getUserCertPKCS12())))
				return false;
		}

		if (!GlobusLocations.defaultLocations().getRenewUserCert()
				.renameTo(GlobusLocations.defaultLocations().getUserCert()))
			return false;
		if (!GlobusLocations.defaultLocations().getRenewUserKey()
				.renameTo(GlobusLocations.defaultLocations().getUserKey()))
			return false;
		if (!GlobusLocations
				.defaultLocations()
				.getRenewUserCertRequest()
				.renameTo(
						GlobusLocations.defaultLocations().getUserCertRequest()))
			return false;

		UserProperty.setProperty("CERT_REQUEST", GlobusLocations
				.defaultLocations().getUserCertRequest().toString());
		UserProperty.setProperty("PRIVATE_KEY", GlobusLocations
				.defaultLocations().getUserKey().toString());

		UserProperty.setProperty("REQUESTED_RENEWED_CERTIFICATE", "no");

		return true;
	}

	/**
	 * Checks whether the certifcate is ready to download.
	 * 
	 * @return true if ready, false if not
	 */
	public static boolean readyToDownload() {

		// TODO make this better
		if (UserProperty.getProperty("REQUEST_SERIAL") == null
				|| "manual".equals(UserProperty.getProperty("REQUEST_SERIAL")))
			return true;

		return OpenCA.checkStatusOfRequest(UserProperty
				.getProperty("REQUEST_SERIAL"));
		// String cert = null;
		// try {
		// cert = OpenCA.downloadCertificate(c, o, ou, cn, email);
		// } catch (Exception e) {
		// throw new UnableToDownloadCertificateException("openca", e);
		// }
		// myLogger.debug("Answer from server: \n" + cert);
		// if (cert != null && cert.indexOf(Certificate.PEM_HEADER) != -1) {
		// Certificate newCert = new Certificate(cert);
		// Certificate localCert = null;
		// try {
		// localCert = new
		// Certificate(GlobusLocations.defaultLocations().getUserCert());
		// } catch (Exception e) {
		// // can't create old cert, asuming this is the new one...
		// return newCert;
		// }
		//
		// if ( localCert.getEnddate().equals(newCert.getEnddate()) ) {
		// //myLogger.debug("Your certificate is not ready yet. Please try it again later.");
		// return null;
		// }
		// // this means the certificate is ready
		// return newCert;

	}

	/**
	 * This is just a wrapper of the downloadCertificate method which does all
	 * the checking, parsing of usercer_request.pem ...
	 * 
	 * @return the requested and approved Certificate or null if retrieving
	 *         fails for some reason. If you want more detailed information why
	 *         it failed you have to do the downloadCertificate() steps
	 *         manually.
	 */
	public static Certificate retrieveDefaultCertificate() {

		if (GlobusLocations.defaultLocations().userCertExists()) {
			myLogger.debug("There is already a certificate in the .globus directory. Remove this file if you want to retrieve a new one: "
					+ GlobusLocations.defaultLocations().getUserCert()
							.toString());
			return null;
		}

		CertificationRequest cert_req = null;
		try {
			cert_req = new CertificationRequest(GlobusLocations
					.defaultLocations().getUserCertRequest(),
					GrixProperty.getString("signature.algorithm"), false);
		} catch (IOException e) {
			myLogger.debug("Can't read file: "
					+ GlobusLocations.defaultLocations().getUserCertRequest()
					+ ". Please check permissions.");
			return null;
		} catch (MissingInformationException e) {
			myLogger.debug("Can not read the following fields out of the certification request:\n");
			for (String info : e.getMissingInformation()) {
				myLogger.debug(info);
			}
			return null;
		}

		Certificate cert = null;
		try {
			cert = ManageCertificate.downloadCertificate(cert_req.getC(),
					cert_req.getO(), cert_req.getOu(), cert_req.getCn(),
					cert_req.getEmail());
		} catch (GeneralSecurityException e) {
			myLogger.debug("Can't convert server answer into a certificate: "
					+ e.getMessage());
			return null;
		} catch (UnableToDownloadCertificateException e) {
			myLogger.debug("Can't download certificate: " + e.getMessage());
			return null;
		}

		if (cert == null) {
			myLogger.debug("Your certificate is not ready yet. Please try it again later.");
			return null;
		}

		myLogger.debug("Successfully downloaded the new certificate");
		return cert;
	}

	/**
	 * Uploads the certification request using a plugin
	 * 
	 * @param cert_req
	 *            The [@link CertificationRequest]
	 * @return a String array with: string[0]=short message, string[1]=long
	 *         message (e.g. server answer)
	 * @throws UnableToUploadCertificationRequestException
	 *             if, for whatever reason, the upload fails
	 */
	public static String[] uploadCertificationRequest(
			CertificationRequest cert_req, boolean hostCert, String name,
			String email, String department, String telephone)
			throws UnableToUploadCertificationRequestException {

		// start uploading
		myLogger.debug("Trying to upload certificate request...");

		// TODO implement plugin architecture
		try {
			return OpenCA.uploadCertificationRequest(cert_req, hostCert, name,
					email, department, telephone);
		} catch (UnableToUploadCertificationRequestException ue) {
			throw ue;
		} catch (Exception e) {
			throw new UnableToUploadCertificationRequestException("openca", e);
		}
	}

	/**
	 * This creates a certification request and stores it (along with the
	 * private key) in the .globus folder on the user's home directory. It also
	 * stores the DN of the user in the .globus/grix.properties file
	 * 
	 * @param ui
	 *            The {@link CreateRequestInterface} is the view class that
	 *            returns values for the cert-request and informs the user about
	 *            the status of the creation
	 * @param hostCert
	 *            whether the request is for a hostcert (true) or usercert
	 *            (false)
	 * @param requestFile
	 *            the file to store the request into, if null, the CoGProperties
	 *            default is used
	 * @param the
	 *            file to store the private key into, if null, the CoGProperties
	 *            default is used
	 */
	public static CertificationRequest createAndStoreCertificationRequest(
			CreateRequestInterface ui, boolean hostCert, int keysize,
			String signature_algorithm, File requestFile, File keyFile,
			boolean overwrite) {

		ui.lockInput();

		boolean createdKeyDirectory = false;
		boolean createdRequestDirectory = false;
		boolean createdKey = false;

		String cn = ui.getCN();
		if (cn == null || "".equals(cn)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.noCN"));
			ui.unlockInput();
			return null;
		}
		String ou = ui.getOU();
		if (ou == null || "".equals(ou)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.noOU"));
			ui.unlockInput();
			return null;
		}
		String o = ui.getO();
		if (o == null || "".equals(o)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.noO"));
			ui.unlockInput();
			return null;
		}
		String c = ui.getC();
		if (c == null || "".equals(c)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.noC"));
			ui.unlockInput();
			return null;
		}
		if (c.length() != 2) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.wrongC"));
			ui.unlockInput();
			return null;
		}
		String phone = ui.getPhone();
		if (phone == null || "".equals(phone)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.noPhone"));
			ui.unlockInput();
			return null;
		}
		String email = ui.getEmail();
		if (!Utils.isValidEmail(email)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.invalidEmail"));
			ui.unlockInput();
			return null;
		}

		char[] passphrase1 = null;
		char[] passphrase2 = null;

		if (!hostCert) {

			ui.setStatus(messages.getString("status.readingPassphrase"));
			passphrase1 = ui.getPassphrase();
			if (!Utils.isValidPassphrase(passphrase1)) {
				Arrays.fill(passphrase1, 'x');
				ui.clearPassphrases();
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages.getString("message.invalidPassphrase"));
				ui.unlockInput();
				return null;
			}

			passphrase2 = ui.getPassphrase2();
			if (!Utils.samePasswords(passphrase1, passphrase2)) {
				Arrays.fill(passphrase1, 'x');
				Arrays.fill(passphrase2, 'x');
				ui.clearPassphrases();
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages.getString("message.passphrasesNotTheSame"));
				ui.unlockInput();
				return null;
			}
			Arrays.fill(passphrase2, 'x');
		}

		ui.setStatus(messages.getString("status.checkingGlobusDirectory"));

		if (hostCert && requestFile == null) {
			requestFile = new File(GlobusLocations.defaultLocations()
					.getGlobusDirectory(),
					GrixProperty.getString("hostcert_request.pem"));
		} else if (!hostCert && requestFile == null) {
			requestFile = GlobusLocations.defaultLocations()
					.getUserCertRequest();
		}

		if (keyFile == null) {
			if (hostCert) {
				keyFile = new File(GlobusLocations.defaultLocations()
						.getGlobusDirectory(),
						GrixProperty.getString("hostkey.pem"));
			} else {
				keyFile = GlobusLocations.defaultLocations().getUserKey();
			}
		}

		if (keyFile.equals(requestFile)) {
			ui.clearPassphrases();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.RequestAndPrivKeyFileSame"));
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			ui.unlockInput();
			return null;
		}

		// check file permissions
		if (!keyFile.getParentFile().exists()) {
			if (!keyFile.getParentFile().mkdir()) {
				ui.clearPassphrases();
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages
						.getString("message.cannotCreateGlobusDirectory")
						+ " "
						+ keyFile.getParentFile().toString());
				Arrays.fill(passphrase1, 'x');
				Arrays.fill(passphrase2, 'x');
				ui.unlockInput();
				return null;
			}
			createdKeyDirectory = true;
		}
		if (!keyFile.getParentFile().canWrite()) {
			if (createdKeyDirectory) {
				keyFile.getParentFile().delete();
			}
			ui.clearPassphrases();
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages
					.getString("message.globusDirectoryNotWriteable")
					+ " "
					+ keyFile.getParentFile().toString());
			ui.unlockInput();
			return null;
		}

		if (!requestFile.getParentFile().exists()) {
			if (!keyFile.getParentFile().mkdir()) {
				ui.clearPassphrases();
				Arrays.fill(passphrase1, 'x');
				Arrays.fill(passphrase2, 'x');
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages
						.getString("message.cannotCreateGlobusDirectory")
						+ " "
						+ requestFile.getParentFile().toString());
				ui.unlockInput();
				return null;
			}
			createdRequestDirectory = true;
		}
		if (!requestFile.getParentFile().canWrite()) {
			ui.clearPassphrases();
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			if (createdKeyDirectory)
				keyFile.getParentFile().delete();
			if (createdRequestDirectory
					&& !keyFile.getParentFile().equals(
							requestFile.getParentFile()))
				requestFile.getParentFile().delete();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages
					.getString("message.globusDirectoryNotWriteable")
					+ " "
					+ requestFile.toString());
			ui.unlockInput();
			return null;
		}

		// check whether key / request already exist
		if (!overwrite) {
			if (keyFile.exists()) {
				ui.clearPassphrases();
				Arrays.fill(passphrase1, 'x');
				Arrays.fill(passphrase2, 'x');
				if (createdRequestDirectory)
					requestFile.getParentFile().delete();
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages.getString("message.userKeyExistsAlready")
						+ " " + keyFile.toString());
				ui.unlockInput();
				return null;
			}

			if (requestFile.exists()) {
				ui.clearPassphrases();
				Arrays.fill(passphrase1, 'x');
				Arrays.fill(passphrase2, 'x');
				if (createdKeyDirectory)
					keyFile.getParentFile().delete();
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages.getString("message.userCertExistsAlready")
						+ " " + requestFile.toString());
				ui.unlockInput();
				return null;
			}
		}

		ui.setStatus(messages.getString("status.generatingKeypair"));
		CertificationRequest cert_req = null;

		try {
			cert_req = new CertificationRequest(keysize, signature_algorithm);
		} catch (GeneralSecurityException e) {
			ui.clearPassphrases();
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			if (createdKeyDirectory)
				keyFile.getParentFile().delete();
			if (createdRequestDirectory
					&& !keyFile.getParentFile().equals(
							requestFile.getParentFile()))
				requestFile.getParentFile().delete();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.cannotCreateRequest") + ": "
					+ e.getMessage());
			ui.unlockInput();
			return null;
		}

		ui.setStatus(messages.getString("status.readingInformation"));
		cert_req.setC(c);
		cert_req.setO(o);
		cert_req.setOu(ou);
		cert_req.setCn(cn);
		cert_req.setEmail(email);

		try {
			cert_req.createDN(hostCert);
		} catch (MissingInformationException e) {
			ui.clearPassphrases();
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			if (createdKeyDirectory)
				keyFile.getParentFile().delete();
			if (createdRequestDirectory
					&& !keyFile.getParentFile().equals(
							requestFile.getParentFile()))
				requestFile.getParentFile().delete();
			ui.setStatus(messages.getString("status.error"));
			String message = messages
					.getString("message.cannotCreateRequest.missingInformation"
							+ "\n\n");
			for (String emptyField : e.getMissingInformation()) {
				message = message + emptyField + "\n";
			}
			ui.message(message);
			ui.unlockInput();
			return null;
		}

		ui.setStatus(messages.getString("status.generatingRequest"));

		if (!hostCert) {
			ui.setStatus(messages.getString("status.encryptingPrivateKey"));

			try {
				cert_req.getKeypair().encryptPrivateKey(passphrase1);
				Arrays.fill(passphrase1, 'x');
				ui.setStatus(messages
						.getString("status.encryptingPrivateKeySuccess"));
			} catch (GeneralSecurityException e) {
				ui.clearPassphrases();
				Arrays.fill(passphrase1, 'x');
				Arrays.fill(passphrase2, 'x');
				if (createdKeyDirectory)
					keyFile.getParentFile().delete();
				if (createdRequestDirectory
						&& !keyFile.getParentFile().equals(
								requestFile.getParentFile()))
					requestFile.getParentFile().delete();
				Arrays.fill(passphrase1, 'x');
				ui.setStatus(messages.getString("status.error"));
				ui.message(messages.getString("message.errorEncryptingKey"));
				ui.unlockInput();
				return null;
			}
		}

		ui.setStatus(messages.getString("status.writingPrivateKey"));

		try {
			cert_req.getKeypair().getPrivateKey().writeTo(keyFile.toString());
			ui.setStatus(messages.getString("status.writingPrivateKeySuccess")
					+ " " + keyFile.toString());
			createdKey = true;
		} catch (IOException e) {
			ui.clearPassphrases();
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			if (createdKeyDirectory)
				keyFile.getParentFile().delete();
			if (createdRequestDirectory
					&& !keyFile.getParentFile().equals(
							requestFile.getParentFile()))
				requestFile.getParentFile().delete();
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.errorWritingKey") + " "
					+ keyFile.toString());
			ui.unlockInput();
			return null;
		}

		ui.setStatus(messages.getString("status.writingCertRequest"));

		try {
			cert_req.writeToPEMFile(requestFile);
			ui.setStatus(messages.getString("status.writingCertRequestSuccess")
					+ " " + requestFile.toString());
		} catch (IOException e) {
			ui.clearPassphrases();
			Arrays.fill(passphrase1, 'x');
			Arrays.fill(passphrase2, 'x');
			if (createdKey) {
				keyFile.delete();
			}
			if (createdKeyDirectory)
				keyFile.getParentFile().delete();
			if (createdRequestDirectory
					&& !keyFile.getParentFile().equals(
							requestFile.getParentFile()))
				requestFile.getParentFile().delete();
			if (createdKey) {
				keyFile.delete();
			}
			ui.setStatus(messages.getString("status.error"));
			ui.message(messages.getString("message.errorWritingRequest") + " "
					+ requestFile.toString());
			ui.unlockInput();
			return null;
		}

		// writing the location of the request file in the grix.properties file
		if (!hostCert) {
			UserProperty.setProperty("CERT_REQUEST", requestFile.toString());
			UserProperty.setProperty("PRIVATE_KEY", keyFile.toString());
			UserProperty.setProperty("FIRST_NAME", ui.getFirstName());
			UserProperty.setProperty("LAST_NAME", ui.getLastName());
			UserProperty.setProperty("EMAIL", email);
			UserProperty.setProperty("ORGANISATION", o);
			UserProperty.setProperty("ORGANISATION_UNIT", ou);
			UserProperty.setProperty("COUNTRY", c);
			UserProperty.setProperty("PHONE", phone);
		} else {
			UserProperty
					.addToList("HOST_CERT_REQUESTS", requestFile.toString());
			UserProperty.addToList("HOST_PRIVATE_KEYS", keyFile.toString());
		}
		ui.setStatus(messages.getString("status.success"));
		ui.message(messages.getString("message.creationCertRequestSuccess")
				+ " " + requestFile.toString() + " & " + keyFile.toString());

		ui.unlockInput();

		return cert_req;
	}

}
