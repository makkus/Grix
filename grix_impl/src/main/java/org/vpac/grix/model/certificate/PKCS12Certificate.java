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

package org.vpac.grix.model.certificate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.globus.util.PEMUtils;
import org.vpac.common.model.GlobusLocations;

/**
 * This class represents a PKCS12 certificate, which contains of a private key
 * and a certificate (which is a public key signed by a ca). This class is not
 * really finished yet, so beware if you use it and read the documentation of
 * the constructors.
 * 
 * @author markus
 * 
 */
public class PKCS12Certificate {

	static final Logger myLogger = Logger.getLogger(PKCS12Certificate.class
			.getName());

	private OpenSSLKey privateKey;

	private X509Certificate x509certificate;

	private Certificate certificate;

	private String alias;

	/**
	 * This constructor reads both (PEM encoded) private key and certificate.
	 * It's usefull if you want to export a .p12 file. Beware: If created with
	 * this constructor, this object does not actually hold a certificate. Maybe
	 * it will later, but at the moment I don't have the time to clean up the
	 * code.
	 * 
	 * @param pem_privateKey
	 *            The filename of the private key
	 * @param pem_certificate
	 *            The filename of the certificate
	 * @param alias
	 *            The alias of the private key
	 * @throws GeneralSecurityException
	 * @throws IOException
	 *             If there is a problem reading a file
	 */
	public PKCS12Certificate(String pem_privateKey, String pem_certificate,
			String alias) throws IOException, GeneralSecurityException {

		// read certificate
		FileInputStream in = new FileInputStream(pem_certificate);
		CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
		x509certificate = (X509Certificate) cf.generateCertificate(in);
		in.close();

		// read private key

		privateKey = new BouncyCastleOpenSSLKey(pem_privateKey);

		this.alias = alias;

	}

	/**
	 * Parses a p12 file and loads the first certificate and private key into
	 * this object. Use this constructor if you want to export pem files out of
	 * a .p12 file.
	 * 
	 * @param p12File
	 * @param passphrase
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public PKCS12Certificate(File p12File, char[] passphrase)
			throws IOException, GeneralSecurityException {

		KeyStore ks = KeyStore.getInstance("PKCS12");
		FileInputStream pkcs12File = new FileInputStream(p12File);
		ks.load(pkcs12File, passphrase);
		pkcs12File.close();

		Enumeration<String> aliases = ks.aliases();
		// this takes the first alias
		alias = aliases.nextElement();

		// while (aliases.hasMoreElements()){
		// System.out.println("Alias: "+aliases.nextElement());
		// }

		privateKey = new BouncyCastleOpenSSLKey((PrivateKey) ks.getKey(alias,
				passphrase));
		if (privateKey == null) {
			throw new GeneralSecurityException(
					"No private key found in keystore.");
		}
		// System.out.println(privateKey.getFormat());

		java.security.cert.Certificate javaCertificate = ks
				.getCertificate(alias);
		// System.out.println(certificate.getPublicKey().getAlgorithm());
		if (javaCertificate == null) {
			Arrays.fill(passphrase, 'x');
			throw new GeneralSecurityException(
					"No certificate found in keystore");
		}

		byte[] cert = null;

		cert = javaCertificate.getEncoded();

		cert = Base64.encode(cert);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PEMUtils.writeBase64(out, Certificate.PEM_HEADER, cert,
				Certificate.PEM_FOOTER);
		out.close();

		certificate = new Certificate(out.toString());

	}

	public boolean writeToP12File(char[] passphrase, String p12file)
			throws InvalidKeyException {

		X509Certificate[] certChain = new X509Certificate[1];
		KeyStore ks = null;
		try {
			ks = KeyStore.getInstance("PKCS12", "BC");
			ks.load(null, null);
			certChain[0] = x509certificate;
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}

		try {
			privateKey.decrypt(new String(passphrase));

		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			throw new InvalidKeyException();
		}

		FileOutputStream fos = null;
		// TODO check whether password is needed here or not
		try {
			ks.setKeyEntry(alias, privateKey.getPrivateKey(), null, certChain);
			fos = new FileOutputStream(p12file);
			ks.store(fos, passphrase);

		} catch (IOException ioe) {
			GlobusLocations.defaultLocations().getUserCertPKCS12().delete();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			myLogger.debug("Could not write .p12 file");
			GlobusLocations.defaultLocations().getUserCertPKCS12().delete();
			throw new InvalidKeyException();
			// e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				myLogger.error(e);
			}
			// delete passphrase
			Arrays.fill(passphrase, 'x');
		}

		return true;
	}

	public static void main(String[] args) {
		System.out.println("Starting...");
		try {
			PKCS12Certificate cert = new PKCS12Certificate(new File(
					"/home/markus/Desktop/test.p12"), "xxx".toCharArray());

			cert.getCertificate().writeToFile(
					new File("/home/markus/usercert.pem"));
			cert.getPrivateKey().writeTo("/home/markus/userkey.pem");

		} catch (Exception e) {
			// e.printStackTrace();
			myLogger.error(e);
		}
		System.out.println("Finished.");
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public OpenSSLKey getPrivateKey() {
		return privateKey;
	}

}
