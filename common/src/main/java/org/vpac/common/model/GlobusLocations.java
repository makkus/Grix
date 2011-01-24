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

package org.vpac.common.model;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.globus.common.CoGProperties;

/**
 * This class is a utility class to return all globus-relevant files. The files
 * are: the [user]/.globus directory, private key, the certificate_request and
 * the certificate. I know this class is pretty useless because I could use
 * CoGProperties.getDefault for all of the fields. Don't know why I did not do
 * that in the first place. Maybe I refactor grix to use the cogproperties
 * directly when I have time.
 * 
 * See [@link org.globus.common.CoGProperties] for more documentation.
 * 
 * @author Markus Binsteiner
 * 
 */
public class GlobusLocations {

	static final Logger myLogger = Logger.getLogger(GlobusLocations.class
			.getName());

	static ResourceBundle messages = ResourceBundle.getBundle(
			"ModelMessagesBundle", java.util.Locale.getDefault());

	static final public String GLOBUS_DIR_NAME = ".globus";
	static final public String CERT_REQUEST_FILENAME = "usercert_request.pem";
	static final public String CERT_PKCS12_FILENAME = "usercert.p12";
	static final public String RENEW_EXTENSION = ".new";

	private CoGProperties cog = CoGProperties.getDefault();
	private File userCert = null;
	private File renewUserCert = null;
	private File userKey = null;
	private File renewUserKey = null;
	private File globusDirectory = null;
	private File userCertRequest = null;
	private File renewUserCertRequest = null;
	private File userCertPKCS12 = null;
	private File[] certificatesDirectories = null;

	static private GlobusLocations defaultGlobusLocations = null;

	private GlobusLocations() {
		String certDirName = System.getProperty("x509_cert_dir");
		this.cog = CoGProperties.getDefault();
		if (certDirName == null || "".equals(certDirName)) {
			myLogger.debug("Could not find x509_cert_dir property. using cog defaults for cert location.");
			this.userCert = new File(cog.getUserCertFile());
			this.renewUserCert = new File(cog.getUserCertFile()
					+ RENEW_EXTENSION);
			this.userKey = new File(cog.getUserKeyFile());
			this.renewUserKey = new File(cog.getUserKeyFile() + RENEW_EXTENSION);
			this.globusDirectory = new File(System.getProperty("user.home"),
					GLOBUS_DIR_NAME);
		} else {
			myLogger.debug("Found x509_cert_dir property: " + certDirName
					+ ". Using it for cert location and grix properties file.");
			this.globusDirectory = new File(certDirName);
			if (!this.globusDirectory.exists()) {
				this.globusDirectory.mkdirs();
			}

			if (!this.globusDirectory.exists()
					|| !this.globusDirectory.canWrite()) {
				myLogger.warn("Can't write to certificate directory "
						+ this.globusDirectory.toString()
						+ ". Using default location in $HOME/.globus.");
				this.globusDirectory = new File(
						System.getProperty("user.home"), GLOBUS_DIR_NAME);
			}
			this.userCert = new File(this.globusDirectory, "usercert.pem");
			this.renewUserCert = new File(this.userCert.toString()
					+ RENEW_EXTENSION);
			this.userKey = new File(this.globusDirectory, "userkey.pem");
			this.renewUserKey = new File(this.userKey.toString()
					+ RENEW_EXTENSION);
		}
		this.userCertRequest = new File(this.globusDirectory,
				CERT_REQUEST_FILENAME);
		this.renewUserCertRequest = new File(this.globusDirectory,
				CERT_REQUEST_FILENAME + RENEW_EXTENSION);
		this.userCertPKCS12 = new File(this.globusDirectory,
				CERT_PKCS12_FILENAME);
		String[] dirs = null;
		try {
			// dirs =
			// CoGProperties.getDefault().getCaCertLocations().split(",");
			// this.certificatesDirectories = new File[dirs.length];
			// for ( int i = 0; i<dirs.length; i++ ){
			// this.certificatesDirectories[i] = new File(dirs[i]);
			// }
			// File dir = new
			// File(CoGProperties.getDefault().getCaCertLocations().split(",")[0]).getParentFile();

			File dir = null;
			if (new File("/etc/grid-security/certificates").exists()) {
				dir = new File("/etc/grid-security/certificates");
			} else {
				dir = new File(this.globusDirectory, "certificates");
			}

			this.certificatesDirectories = new File[] { dir };
		} catch (NullPointerException npe) {
			myLogger.error(npe.getLocalizedMessage());
		}

		myLogger.debug("GlobusLocations:\n-------------------------"
				+ "Globus directory: " + this.globusDirectory.toString()
				+ "\nCertificate: " + this.userCert.toString()
				+ "\nCertificate Request: " + this.userCertRequest.toString()
				+ "\nUserkey: " + this.userKey.toString()
				+ "\n-------------------------");
	}

	public static GlobusLocations defaultLocations() {
		if (defaultGlobusLocations == null)
			defaultGlobusLocations = new GlobusLocations();
		return defaultGlobusLocations;
	}

	public File createGlobusDirectory() throws IOException {
		if (!globusDirectoryExists()) {
			if (!globusDirectory.mkdirs())
				throw new IOException(
						messages.getString("error.cannotCreateDirectory") + " "
								+ globusDirectory.toString());
		}
		return globusDirectory;
	}

	public File getGlobusDirectory() {
		return globusDirectory;
	}

	public boolean globusDirectoryExists() {
		return globusDirectory.exists();
	}

	public File getUserCert() {
		return userCert;
	}

	public File getRenewUserCert() {
		return renewUserCert;
	}

	public boolean userCertExists() {
		return userCert.exists();
	}

	public File getUserCertRequest() {
		return userCertRequest;
	}

	public File getRenewUserCertRequest() {
		return renewUserCertRequest;
	}

	public boolean userCertRequestExists() {
		return userCertRequest.exists();
	}

	public File getUserKey() {
		return userKey;
	}

	public File getRenewUserKey() {
		return renewUserKey;
	}

	public boolean userKeyExists() {
		return userKey.exists();
	}

	public File getUserCertPKCS12() {
		return userCertPKCS12;
	}

	public boolean userCertPKCS12Exists() {
		return userCertPKCS12.exists();
	}

	public File[] getCertificatesDirectories() {
		return certificatesDirectories;
	}
}
