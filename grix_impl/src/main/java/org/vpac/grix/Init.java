package org.vpac.grix;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Class that includes static init methods for certain parts of Grix.
 * 
 * @author Markus Binsteiner
 *
 */
public class Init {

	static final Logger myLogger = Logger.getLogger(Init.class.getName());

	final public static String CERTIFICATE_DIRECTORY = System.getProperty("user.home")
			+ File.separator + ".globus" + File.separator + "certificates";

	final static File CERT_DIR = new File(CERTIFICATE_DIRECTORY);
	
	final static File GLOBAL_CERT_DIR = new File("/etc/grid-security/certificates");
	
	final static String VOMSES_DIRECTORY = System.getProperty("user.home")
	+ File.separator + ".glite" + File.separator + "vomses_available";
	
	final static String ACTIVE_VOMSES_DIRECTORY = System.getProperty("user.home")
	+ File.separator + ".glite" + File.separator + "vomses";

	final static File VOMSES_DIR = new File(VOMSES_DIRECTORY);

	final static File ACTIVE_VOMSES_DIR = new File(ACTIVE_VOMSES_DIRECTORY);

	/**
	 * Adds the BouncyCastle provider to the current Java environment. Stops application
	 * if the provider can't be found.
	 */
	public static void initBouncyCastle() {

		// bouncy castle
		if (Security.addProvider(new BouncyCastleProvider()) == -1) {
			myLogger
					.error("Could not load BouncyCastleProvider. Makes no sense to continue...");
			System.exit(-1);
		}
	}

	/**
	 * Writes details about the unlimeted policy situation for the current JRE into the grix property file.
	 */
//	public static void initUnlimitedStrengthPolicy() {
//
//		// check Unlimited Strength Policy files
//		if (!UnlimitedStrengthPolicyManager.isUnlimited()) {
//			myLogger
//					.warn("Unlimited strength policy files are not installed. This limits the funcitonality of this program. Take a look at "
//							+ GrixProperty.getString("POLICY_HOWTO_URL")
//							+ " to find out how to install them.");
//			UserProperty.setProperty("UNLIMITED_STRENGTH", "no");
//
//			if (!UnlimitedStrengthPolicyManager.allowedToWritePolicyFiles()) {
//				myLogger
//						.warn("You don't have write access to the directory where the policy files are installed. Most likely you have "
//								+ "to contact an administrator and ask him to install them for you.");
//				UserProperty.setProperty("WRITE_ACCESS_POLICY_FILE", "no");
//			} else {
//				myLogger
//						.debug("User has write access to policy files folder. Thats good.");
//				UserProperty.setProperty("WRITE_ACCESS_POLICY_FILE", "yes");
//			}
//
//		} else {
//			myLogger.debug("Unlimted strength policy files in place.");
//			UserProperty.setProperty("UNLIMITED_STRENGTH", "yes");
//		}
//	}
	
	/**
	 * Extracts the files in the vomses.zip file in the directory $HOME/.glite/vomses
	 * These files are pointing Grix to the voms/vomrs server(s) the APACGrid is using.
	 */
	public static void copyVomses() {


		if (!ACTIVE_VOMSES_DIR.exists()) {
			if (!ACTIVE_VOMSES_DIR.mkdirs())
				myLogger.error("Could not write in .glite directory. Please check permissions.");
		}
		
		if (!VOMSES_DIR.exists()) {
			if (!VOMSES_DIR.mkdirs())
				myLogger
						.error("Could not write in .glite directory. Please check permissions.");
		}

		int BUFFER_SIZE = 8192;
		int count;
		byte data[] = new byte[BUFFER_SIZE];

		InputStream in = Init.class.getResourceAsStream("/vomses.zip");
		ZipInputStream vomsStream = new ZipInputStream(in);

		BufferedOutputStream dest = null;

		try {

			ZipEntry voms = null;

			while ((voms = vomsStream.getNextEntry()) != null) {

				if (!voms.isDirectory()) {

					myLogger.debug("Vomses name: " + voms.getName());
					File vomses_file = new File(VOMSES_DIR, voms.getName());

					if (!vomses_file.exists()) {

						// Write the file to the file system
						FileOutputStream fos = new FileOutputStream(vomses_file);
						dest = new BufferedOutputStream(fos, BUFFER_SIZE);
						while ((count = vomsStream.read(data, 0, BUFFER_SIZE)) != -1) {
							dest.write(data, 0, count);
						}
						dest.flush();
						dest.close();
					}

				}
			}
			
			if ( ! new File(ACTIVE_VOMSES_DIR, "ARCS").exists() ) {
				FileUtils.copyFile(new File(VOMSES_DIR, "ARCS"), new File(ACTIVE_VOMSES_DIR, "ARCS"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myLogger.error(e);
		}
	}


	/**
	 * This one copies the CA certificates (in the certificates.zip file) into the .globus/certificates
	 * directory if they are not already there...
	 */
	public static void copyCACerts() {

		if ( !CERT_DIR.exists() && !GLOBAL_CERT_DIR.exists() ) {
			if (!CERT_DIR.mkdirs())
				myLogger
						.error("Could not write in .globus directory. Please check permissions.");
		}

		int BUFFER_SIZE = 8192;
		int count;
		byte data[] = new byte[BUFFER_SIZE];

		InputStream in = Init.class.getResourceAsStream("/certificates.zip");
		ZipInputStream certStream = new ZipInputStream(in);

		BufferedOutputStream dest = null;

		try {

			ZipEntry cert = null;

			while ((cert = certStream.getNextEntry()) != null) {

				if (!cert.isDirectory()) {

					myLogger.debug("Certificate name: " + cert.getName());
					File cert_file = new File(CERT_DIR, cert.getName());

					// exception for the apacgrid cert
					if (!cert_file.exists() || cert_file.getName().startsWith("1e12d831")) {

						// Write the file to the file system
						FileOutputStream fos = new FileOutputStream(cert_file);
						dest = new BufferedOutputStream(fos, BUFFER_SIZE);
						while ((count = certStream.read(data, 0, BUFFER_SIZE)) != -1) {
							dest.write(data, 0, count);
						}
						dest.flush();
						dest.close();
					}

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myLogger.error(e);
		}

		// boolean cacertExists = false;
		// final String CACERTS_DIR_NAME = "/cacerts/";
		//		
		// URL cert0 = Init.class.getResource(CACERTS_DIR_NAME
		// + GrixProperty.getString("CACERT_FILE"));
		//		
		// URL cert_signing_policy = Init.class
		// .getResource(CACERTS_DIR_NAME
		// + GrixProperty
		// .getString("CACERT_POLICY_FILE"));
		//
		// try {
		// String[] cacertDirectories = CoGProperties.getDefault()
		// .getCaCertLocations().split(",");
		//
		// if (cacertDirectories.length < 0) {
		//
		// for (String dir : cacertDirectories) {
		// myLogger.debug("cacertDirectory: " + dir);
		// // TODO check for .policy file?
		// if (new File(dir, GrixProperty
		// .getString("CACERT_FILE")).exists()) {
		// cacertExists = true;
		// // break;
		// }
		// }
		// }
		// } catch (NullPointerException npe) {
		// cacertExists = false;
		// }
		//
		// if (cacertExists == false) {
		// // copy cacert to <home user>/.globus/certificates
		// File caCertFile = new
		// File(GlobusLocations.defaultLocations().getGlobusDirectory().toString()
		// + File.separator
		// + GrixProperty.getString("CERTIFICATES_DIRECTORY_NAME")
		// + File.separator
		// + GrixProperty.getString("CACERT_FILE"));
		// File caCertPolicyFile = new
		// File(GlobusLocations.defaultLocations().getGlobusDirectory().toString()
		// + File.separator
		// + GrixProperty.getString("CERTIFICATES_DIRECTORY_NAME")
		// + File.separator
		// + GrixProperty.getString("CACERT_POLICY_FILE"));
		//
		// // copy cacert to .globus/certificates
		// if (!caCertFile.exists()) {
		// if (!caCertFile.getParentFile().exists())
		// caCertFile.getParentFile().mkdirs();
		// InputStream cert0_in = null;
		// OutputStream out = null;
		// try {
		// cert0_in = cert0.openStream();
		//
		// out = new FileOutputStream(caCertFile);
		// int read;
		// byte[] buffer = new byte[1024];
		//
		// while ((read = cert0_in.read(buffer, 0, 1024)) != -1)
		// out.write(buffer, 0, read);
		// } catch (Exception e) {
		// // TODO
		// e.printStackTrace();
		// } finally {
		// try {
		// out.close();
		// cert0_in.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		//
		// // copy cacert to .globus/certificatesarg0
		// if (!caCertPolicyFile.exists()) {
		// if (!caCertPolicyFile.getParentFile().exists())
		// caCertPolicyFile.getParentFile().mkdirs();
		// InputStream cert_signing_policy_in = null;
		// OutputStream out = null;
		// try {
		// cert_signing_policy_in = cert_signing_policy.openStream();
		//
		// out = new FileOutputStream(caCertPolicyFile);
		// int read;
		// byte[] buffer = new byte[1024];
		//
		// while ((read = cert_signing_policy_in.read(buffer, 0, 1024)) != -1)
		// out.write(buffer, 0, read);
		// } catch (Exception e) {
		// // TODO
		// e.printStackTrace();
		// } finally {
		// try {
		// out.close();
		// cert_signing_policy_in.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		// }arg0
	}

//	/**
//	 * This initializes prerequistes for a secure connection to an gsi enabled
//	 * web service
//	 */
//	public static void initVOMRS() {
//		// vomrs stuff
//		org.apache.axis.AxisProperties.setProperty("axis.socketSecureFactory",
//				"org.glite.security.trustmanager.axis.AXISSocketFactory");
//		System.setProperty("GRIDPROXYFILE", CoGProperties.getDefault()
//				.getProxyFile());
//	}

}
