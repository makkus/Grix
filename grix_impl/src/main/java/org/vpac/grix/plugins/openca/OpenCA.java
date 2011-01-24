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

package org.vpac.grix.plugins.openca;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.grix.control.utils.UserProperty;
import org.vpac.grix.exceptions.certificate.UnableToUploadCertificationRequestException;
import org.vpac.grix.external.ClientHttpRequest;
import org.vpac.grix.model.certificate.CertificationRequest;

/**
 * This class is used to download a certificate from an openca server via
 * http(s).
 * 
 * @author Markus Binsteiner
 * 
 */
public class OpenCA {

	static final Logger myLogger = Logger.getLogger(OpenCA.class.getName());

	private static void installTrustManager() {
		// TODO try better way to create https connection. maybe using
		// hostname verifier

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {

				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {

			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {

			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}

	/**
	 * Initializes this plugin.
	 */
	// public static void init() {
	//
	// if (UnlimitedStrengthPolicyManager.maxRSAKeysize() >= GrixProperty
	// .getInt("OPENCA_SERVER_KEYSIZE")) {
	// myLogger
	// .debug("Able to communicate with the openca server. That's good.");
	// UserProperty.setProperty("ABLE_TO_COMMUNICATE_WITH_OPENCA_SERVER",
	// "yes");
	// } else {
	// myLogger
	// .debug("Not able to communicate with the openca server because of java policy restrictions. That's not good.");
	// UserProperty.setProperty("ABLE_TO_COMMUNICATE_WITH_OPENCA_SERVER",
	// "no");
	// }
	//
	// }

	/**
	 * Checks whether the certification request with the specified
	 * request_serial number is processed by the CA.
	 * 
	 * @param request_serial
	 *            the request serial
	 * @return true if ready to download, false if not
	 */
	public static boolean checkStatusOfRequest(String request_serial) {

		installTrustManager();
		URL url = null;
		try {
			url = new URL(GrixProperty.getString("openca.base.url")
					+ "?cmd=lists;action=newReqs");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block/ throw exception
			// e.printStackTrace();
			myLogger.error(e);
		}

		String result = readPage(url);
		if (result == null) {
			myLogger.debug("Could not read OpenCA page... returning \"ready\" nontheless.");
			// TODO throw exception
			return true;
		}

		if (result.indexOf(request_serial) != -1) {
			// found, means not processed yet
			myLogger.debug("Request serial found, this means the request is not processed yet.");
			return false;
		} else {
			while ((url = containsNextPage(result)) != null) {

				myLogger.debug("Next page url: " + url.toString());
				result = readPage(url);
				if (result == null) {
					myLogger.debug("Could not read OpenCA page... returning \"ready\" nontheless.");
					return true; // TODO throw exception
				}

				if (result.indexOf(request_serial) != -1) {
					myLogger.debug("Request serial found on this page, this means the request is not processed yet.");
					return false; // still on list => not processed
				}

			}

			myLogger.debug("Request processed.");

			return true;
		}
	}

	private static String readPage(URL url) {

		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
				result.append(System.getProperty("line.separator"));
			}
		} catch (IOException ex) {
			myLogger.debug("Cannot retrieve contents of: " + url);
			return null;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				myLogger.error(e);
				return null;
			}
		}
		return result.toString();

	}

	private static URL containsNextPage(String page_body) {
		if (page_body.indexOf("&gt;&gt;") != -1) {
			int startLink = page_body
					.lastIndexOf("Extra References&nbsp; <a href=") + 32;
			int endLink = page_body.indexOf(">&gt;</a> &nbsp;", startLink) - 1;
			String url_string = page_body.substring(startLink, endLink);
			if ((startLink = url_string.lastIndexOf("&lt;</a>")) != -1) {
				startLink = url_string.indexOf("<a href=", startLink) + 9;
				url_string = url_string.substring(startLink);
			}
			URL url = null;
			try {
				url = new URL(GrixProperty.getString("openca.base.url")
						+ url_string);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				myLogger.error(e);
				return null;
			}
			return url;
		} else
			return null;
	}

	/**
	 * Download the certificate via https. This installs a trust-manager that
	 * does not validate certificate chains. This may be altered in the future,
	 * although I don't think it is really a security concern in our case.
	 * 
	 * @return The base64 encoded certificate as a string.
	 * @throws Exception
	 */
	public static String downloadCertificate(String c, String o, String ou,
			String cn, String email) throws Exception {

		if (email == null)
			email = "*";
		// TODO check this:
		// cn = "*";
		String value4 = "*";

		Object[] post_options_pre = {
				// "value_1", cn,
				"value_1", "*", "value_2", email, "value_3", "*CN=" + cn +
				// "*OU="+ou+ //because of possible change of organisation on ca
				// side
						"*O=" + o + "*C=" + c + "*", "value_4", value4,
				"value_5", "", "name_3", "DN", "name_5", "KEY", "name_1", "CN",
				"cmd", "search", "dataType", "CERTIFICATE", "name_4", "ROLE",
				"pcounter", "5", "name_2", "EMAILADDRESS" };

		// Object[] post_options_pre = {
		// "value_1", cn,
		// "value_2", email,
		// "value_3", "*",
		// "value_4", "*",
		// "value_5", "",
		// "name_1", "CN",
		// "name_2", "EMAILADDRESS",
		// "name_3", "DN",
		// "name_4", "ROLE",
		// "cmd", "search",
		// "pcounter", "5"
		// };

		String certificate = null;

		installTrustManager();

		URL url_pre = new URL("https://ca.apac.edu.au/cgi-bin/pub/pki");

		InputStream serverinput_pre = ClientHttpRequest.post(url_pre,
				post_options_pre);

		BufferedInputStream bufin_pre = new BufferedInputStream(serverinput_pre);

		myLogger.debug("parsing html...");

		BufferedReader br_pre = new BufferedReader(new InputStreamReader(
				bufin_pre));

		String line_pre;
		int serial = -1;
		StringBuffer result_pre = new StringBuffer();
		myLogger.debug("Search answer: ");

		// search for the serial number of the certificate if there are multiple
		// then use the last one found
		while ((line_pre = br_pre.readLine()) != null) {
			// result_pre = result_pre.append( line_pre );
			// result_pre = result_pre.append( "\n" );
			myLogger.debug(line_pre);

			String searchString = GrixProperty
					.getString("openca.server.keyword.start");
			String searchString_end = GrixProperty
					.getString("openca.server.keyword.end");
			int pos = line_pre.indexOf(searchString);

			if (pos != -1) {
				serial = new Integer(line_pre.subSequence(
						pos + searchString.length(),
						line_pre.indexOf(searchString_end, pos + 1)).toString());
			}

		}

		bufin_pre.close();

		if (serial == -1) {
			myLogger.debug("Serial not found. Can't download certificate.");
			return null;
		}

		myLogger.debug("Serial found: " + serial);

		URL url = new URL(GrixProperty.getString("openca.base.url"));

		Object[] post_options = { "format_sendcert", "pem", "Submit",
				"Download", "GET_PARAMS_CMD", "", "cmd", "sendcert",
				"dataType", "VALID_CERTIFICATE", "name", "PUBLIC", "key",
				serial, "HIDDEN_key", serial, "passwd", "", "signature", "",
				"format", "", "text", "", "new_dn", "", "dn", ""

		};

		InputStream serverinput = ClientHttpRequest.post(url, post_options);

		BufferedInputStream bufin = new BufferedInputStream(serverinput);

		myLogger.debug("pasing xhtml...");

		BufferedReader br = new BufferedReader(new InputStreamReader(bufin));

		String line;
		StringBuffer result = new StringBuffer();

		while ((line = br.readLine()) != null) {
			result = result.append(line);
			result = result.append("\n");

		}

		certificate = result.toString();

		bufin.close();

		myLogger.debug("Answer from server: \n\n" + certificate);

		return certificate;

	}

	/**
	 * Uploads a user certification request onto an OpenCA server via http(s)
	 * post.
	 * 
	 * @param cert_req
	 *            The certificationRequest
	 * @return a String array consisting of string[0]=short message,
	 *         string[1]=long message(e.g. server answer)
	 * @throws Exception
	 */
	public static String[] uploadCertificationRequest(
			CertificationRequest cert_req, String department, String telephone)
			throws Exception {
		return uploadCertificationRequest(cert_req, false, cert_req.getCn(),
				cert_req.getEmail(), department, telephone);
	}

	/**
	 * Uploads a certification request onto an OpenCA server via http(s) post.
	 * 
	 * @param cert_req
	 *            The certificationRequest
	 * @param email
	 *            The email address of the user (used for hostcerts, because
	 *            these don't contain email addresses)
	 * @param hostCert
	 *            whether this request is for a hostcert or not
	 * @return a String array consisting of string[0]=short message,
	 *         string[1]=long message(e.g. server answer)
	 * @throws Exception
	 */

	public static String[] uploadCertificationRequest(
			CertificationRequest cert_req, boolean hostCert, String name,
			String email, String department, String telephone) throws Exception {

		String result = null;

		String additional_attribute_department = department;
		String loa = "";
		String request = cert_req.getEncodedRequest();
		String additional_attribute_telephone = telephone;
		String cmd = "pkcs10_req";
		String passwd1 = "";
		String passwd2 = "";
		String additional_attribute_email = email; // cert_req.getEmail();
		String ra = cert_req.getOu(); // TODO translate
		String operation = "server-confirmed-form";
		String role = "Web Server";
		if (!hostCert)
			role = "User";
		String additional_attribute_requestercn = name;// cert_req.getCn();

		Object[] post_options = {
				"ADDITIONAL_ATTRIBUTE_DEPARTMENT",
				additional_attribute_department,
				"loa",
				loa,
				"request",
				request,
				// "request", "-----BEGIN CERTIFICATE
				// REQUEST-----\nMIIBuDCCASECAQAwXXXeDELMAkGA1UEBhMCQVUxETAPBgNVBAoTCEFQQUNHcmlkMQ0w\nCwYDVQQLEwRWUEFDMScwJQYDVQQDEx5QbGVhc2UgZGVsZXRlIG1lIHN0cmFpZ2h0\nIGF3YXkxHjAcBgkqhkiG9w0BCQEWD21hcmt1c0B2cGFjLm9yZzCBnzANBgkqhkiG\n9w0BAQEFAAOBjQAwgYkCgYEAtqPqSqO3fHUs/3b95musvjeIYZ1zTFk9R1KF5MfL\n1ica/Uf/XGqzPfNA2WeRHE1cE2wRjImRO8Z2IYFwo1Wu5aSGPiOodi/Qymuw29NP\n20sO4USWJk/womBRhQATUPmAgQdJp4WMJPvditfdkzyrWnYemOXKot4Xiidj01/R\n2XkCAwEAAaAAMA0GCSqGSIb3DQEBBAUAA4GBAGO4C/8ybRLZ9rDTIE3+QmthiSGs\n4rewkiiTLar2thR/+rrpOA6RwjmGZEotZBY195wSmpmAPpTypmvehDqRgiXCqLWe\nFqrbRZKFsHRPf4iSZTPiw4zyNi3lOOZX6IyI+PadR6BNXJeFK0K1XLogmzRNZmtA\nRLc2sqwMIglFNIzJ\n-----END
				// CERTIFICATE REQUEST-----",
				"ADDITIONAL_ATTRIBUTE_TELEPHONE",
				additional_attribute_telephone, "cmd", cmd, "passwd1", passwd1,
				"ADDITIONAL_ATTRIBUTE_EMAIL", additional_attribute_email, "ra",
				ra, "passwd2", passwd2, "operation", operation, "role", role,
				"ADDITIONAL_ATTRIBUTE_REQUESTERCN",
				additional_attribute_requestercn };

		installTrustManager();

		URL url = new URL(GrixProperty.getString("openca.base.url"));

		InputStream serverinput = ClientHttpRequest.post(url, post_options);

		// BufferedInputStream bufin = new BufferedInputStream( serverinput );

		myLogger.debug("parsing xhtml...");
		// Don't know whether clean html or not
		// result = Utils.getCleanXHTML( bufin );

		StringBuffer answer = new StringBuffer();
		BufferedReader buff = new BufferedReader(new InputStreamReader(
				serverinput));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = buff.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			answer.append(readData);
			buf = new char[1024];
		}

		buff.close();
		serverinput.close();

		// TODO dodgy but I don't know how else to do it.
		try {
			int index = answer.indexOf("serial") + 7;
			if (index == -1 || answer.indexOf("error") != -1
					|| answer.indexOf("Error") != -1) {
				// means: not successful
				myLogger.error("Could not upload certification request.");
				throw new UnableToUploadCertificationRequestException("OpenCA",
						new Exception(answer.toString()));
			}
			int index_end = answer.indexOf(" ", index);
			String req_serial = answer.substring(index, index_end);

			UserProperty.setProperty("REQUEST_SERIAL", req_serial);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
			throw e;
		}

		myLogger.debug("Answer from server: \n\n" + answer);

		return new String[] { "Upload successful", answer.toString() };

	}

}
