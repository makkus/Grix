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

package org.vpac.grix.control.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

/**
 * This class tries to ease the pain sun causes with its "Unlimited Strength
 * Policy files" which are not shipped with a default java installation but have
 * to be downloaded and installed by the user. Since we are not allowed to
 * distribute the files, we have to try to help the user installing them.
 * 
 * @author Markus Binsteiner
 * 
 */
public class UnlimitedStrengthPolicyManager {

	static final Logger myLogger = Logger
			.getLogger(UnlimitedStrengthPolicyManager.class.getName());

	public static final String LOCAL_POLICY_FILENAME = "local_policy.jar";

	public static final String US_EXPORT_POLICY_FILENAME = "US_export_policy.jar";

	public static final String SUN_POLICY_ZIPFILENAME = "jce_policy-1_5_0.zip";

	private static final File PATH = new File(System.getProperty("java.home")
			+ File.separator + "lib" + File.separator + "security");

	// private static final File PATH = new File("/home/markus/Desktop/test");
	private static final File LOCAL_POLICY_FILE = new File(PATH,
			LOCAL_POLICY_FILENAME);

	private static final File US_EXPORT_POLICY_FILE = new File(PATH,
			US_EXPORT_POLICY_FILENAME);

	private static final File SUN_POLICY_ZIPFILE = new File(new File(
			System.getProperty("user.home")), SUN_POLICY_ZIPFILENAME);

	/**
	 * Tests whether Unlimited Strength Encryption is installed.
	 * 
	 * @return true if installed, false if not
	 */
	// public static boolean isUnlimited() {
	//
	// try {
	// if ( Cipher.getMaxAllowedKeyLength( "RC4" ) == Integer.MAX_VALUE ) return
	// true;
	// else return false;
	// } catch (Exception e) {
	// // TODO does not matter
	// //e.printStackTrace();
	// myLogger.error(e);
	// }
	// return false;
	// }

	/**
	 * Tests the max rsa keysize.
	 * 
	 * @return true if installed, false if not
	 */
	// public static int maxRSAKeysize() {
	//
	// try {
	// myLogger.debug( "Max RSA Keysize: "+Cipher.getMaxAllowedKeyLength( "RSA"
	// ) );
	// return Cipher.getMaxAllowedKeyLength( "RSA" );
	// } catch (NoSuchAlgorithmException e) {
	// // TODO Auto-generated catch block
	// //e.printStackTrace();
	// myLogger.error(e);
	// }
	// return -1;
	//
	// }

	/**
	 * Tests whether the current user is allowed to install the policy files
	 * (write access to the jre directory) or not.
	 * 
	 * @return true if allowed, false if not
	 */
	public static boolean allowedToWritePolicyFiles() {

		if (PATH.canWrite()) {
			myLogger.debug("Can write to path.");
			if (US_EXPORT_POLICY_FILE.canWrite()) {
				myLogger.debug("Can write: " + US_EXPORT_POLICY_FILE.toString());
				if (LOCAL_POLICY_FILE.canWrite()) {
					myLogger.debug("Can write: " + LOCAL_POLICY_FILE.toString());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * You could use this to download the policy file from a static URL.
	 * Unfortunately, SUN does not offer one. And as it is not allowed to
	 * distribute the file this function is pretty useless.
	 * 
	 * @return whether it worked (true) or not (false)
	 */
	public static boolean downloadPolicyFile() {

		String policyFile = "http://www.vpac.org/~markus/jce_policy-1_5_0.zip";

		OutputStream out = null;
		URLConnection conn = null;
		InputStream in = null;
		try {

			URL url = new URL(policyFile);
			out = new BufferedOutputStream(new FileOutputStream(
					SUN_POLICY_ZIPFILE));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			myLogger.debug("downloaded: " + policyFile.toString() + "\t"
					+ "size: " + numWritten);
			return true;
		} catch (Exception exception) {
			// exception.printStackTrace();
			myLogger.error(exception);
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				// ioe.printStackTrace();
				myLogger.error(ioe);
				return false;
			}
		}
	}

	/**
	 * This function tries to install an already downloaded jce_policy file.
	 * 
	 * @param zipfile
	 *            the jce_policy file
	 * @return true if successful, false if not
	 */
	public static boolean install(ZipFile zipfile) {

		// backup old files
		if (LOCAL_POLICY_FILE.exists()) {
			LOCAL_POLICY_FILE.renameTo(new File(LOCAL_POLICY_FILE.toString()
					+ "." + GrixProperty.getString("backup.file.extension")));
		}
		if (US_EXPORT_POLICY_FILE.exists()) {
			US_EXPORT_POLICY_FILE.renameTo(new File(US_EXPORT_POLICY_FILE
					.toString()
					+ "."
					+ GrixProperty.getString("backup.file.extension")));
		}

		Enumeration entries = zipfile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			myLogger.debug("ZipEntry: " + entry.getName());
			try {
				if (entry.getName().endsWith(LOCAL_POLICY_FILENAME)) {
					myLogger.debug("Extracting: " + entry.getName());
					BufferedInputStream in = new BufferedInputStream(
							zipfile.getInputStream(entry));
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(LOCAL_POLICY_FILE));

					byte[] b = new byte[512];
					int len = 0;
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
					}
					out.close();
					in.close();
				}
				if (entry.getName().endsWith(US_EXPORT_POLICY_FILENAME)) {
					myLogger.debug("Extracting: " + entry.getName());
					BufferedInputStream in = new BufferedInputStream(
							zipfile.getInputStream(entry));
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(US_EXPORT_POLICY_FILE));

					byte[] b = new byte[512];
					int len = 0;
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
					}
					out.close();
					in.close();
				}

			} catch (Exception e) {
				// e.printStackTrace();
				myLogger.error(e);
				// TODO
				return false;
			}
		}

		return true;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ZipException
	 */
	public static void main(String[] args) throws ZipException, IOException {

		// TODO Auto-generated method stub

		// JFileChooser fc = new JFileChooser();
		// fc.showDialog(null, "OPPENN");
		// File file = fc.getSelectedFile();

		// install(new ZipFile(file));

		downloadPolicyFile();

	}

}
