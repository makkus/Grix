/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
 * qc is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.

 * qc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with qc; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.vpac.qc.control.common;

import java.io.File;
import java.security.cert.CertificateException;

import org.apache.log4j.Logger;
import org.vpac.common.model.GlobusLocations;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.LocalProxy;

public class VOMRS_utils {

	static final Logger myLogger = Logger
			.getLogger(VOMRS_utils.class.getName());

	// TODO make sure this is the right directory
	// final public static String CERTIFICATE_DIRECTORY =
	// System.getProperty("user.home")
	// + File.separator + ".globus" + File.separator + "grix.certificates";

	private static boolean isInitialized = false;

	/**
	 * This initializes the VOMRS web service connection by setting some
	 * environment variables. Do it before you connect to a VOMRS web service.
	 * 
	 * @throws CertificateException
	 *             if the grid proxy is expired or not present
	 */
	public static void initVomrsWS() throws CertificateException {

		if (!isInitialized) {
			System.setProperty("axis.socketSecureFactory",
					"org.glite.security.trustmanager.axis.AXISSocketFactory");
			if (LocalProxy.getStatus() != GridProxy.INITIALIZED) {
				myLogger.error("Could not find valid proxy, Proxy status: "
						+ LocalProxy.getStatus());
				throw new CertificateException("No credentials found.");
			}
			System.setProperty("gridProxyFile", LocalProxy.getProxyFile()
					.toString());
			// TODO check out whether this is always true:
			// String sslCAFiles = GlobusLocations.defaultLocations()
			// .getCertificatesDirectories()[0]
			// + File.separator + "*.0";
			String sslCAFiles = null;
			if (new File("/etc/grid-security/certificates").exists())
				sslCAFiles = "/etc/grid-security/certificates";

			if (sslCAFiles == null)
				sslCAFiles = System.getProperty("user.home") + File.separator
						+ ".globus" + File.separator + "certificates";
			myLogger.debug("Setting \"sslCAFiles\" system property to: "
					+ sslCAFiles);
			System.setProperty("sslCAFiles", sslCAFiles);
			// System.setProperty("sslCAFilese", CERTIFICATE_DIRECTORY +
			// File.separator + "*.0");
			myLogger.debug("cacert_dir: " + System.getProperty("sslCAFiles"));
			// System.setProperty("crlFiles", GlobusLocations.defaultLocations()
			// .getCertificatesDirectories()[0]
			// + "*.r0");
			// System.setProperty("crlFiles", CERTIFICATE_DIRECTORY +
			// File.separator + "*.r0");
			System.setProperty("crlEnabled", "false");
			isInitialized = true;
		}

	}

	// public static void initVomrsWsWithHostCert() throws CertificateException
	// {
	//
	// if (! isInitialized ){
	// System.setProperty("axis.socketSecureFactory",
	// "org.glite.security.trustmanager.axis.AXISSocketFactory");
	// System.setProperty("sslCertFile", "/etc/grid-security/hostcert.pem");
	// System.setProperty("sslKey", "/etc/grid-security/hostkey.pem");
	// System.setProperty("sslKeyPasswd", "");
	// //System.setProperty("gridProxyFile",
	// LocalProxy.getProxyFile().toString());
	// // TODO check out whether this is always true:
	// System.setProperty("sslCAFiles", "/etc/grid-security/certificates/*.0");
	// //System.setProperty("sslCAFilese", CERTIFICATE_DIRECTORY +
	// File.separator + "*.0");
	// // myLogger.debug("cacert_dir: " + System.getProperty("sslCAFiles"));
	// // System.setProperty("crlFiles", GlobusLocations.defaultLocations()
	// // .getCertificatesDirectories()[0]
	// // + "*.r0");
	// //System.setProperty("crlFiles", CERTIFICATE_DIRECTORY + File.separator +
	// "*.r0");
	// System.setProperty("crlEnabled", "false");
	// isInitialized = true;
	//
	//
	// // * -DRGMA_HOME=$RGMA_HOME
	// // * -DX509_USER_PROXY=$X509_USER_PROXY
	// // *
	// -Daxis.socketSecureFactory=org.glite.security.trustmanager.axis.AXISSocketFactory
	// // * -DsslCAFiles=/etc/grid-security/certificates/*.0
	// // * -DsslCertFile=_dir_/usercert.pem
	// // * -DsslKey=_dir_/userkey.pem
	// // * -DsslKeyPasswd=_pass_
	// }
	// }

}
