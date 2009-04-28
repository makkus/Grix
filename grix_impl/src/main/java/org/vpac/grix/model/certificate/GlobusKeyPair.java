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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Arrays;

import org.globus.gsi.bc.BouncyCastleOpenSSLKey;


/**
 * A special keypair class for this application. It holds a private key which is
 * compatible to an openssl-generated one and a RSA private key.
 * 
 * @author Markus Binsteiner
 * 
 */
public class GlobusKeyPair {

	private PublicKey publicKey;

	private BouncyCastleOpenSSLKey privateKey;

	private boolean encryptedPrivate = false;

	/**
	 * Constructor does nothing.
	 */
	public GlobusKeyPair() {

	}

	/**
	 * This factory function generates a pair of keys suitable for use with
	 * globus.
	 * 
	 * @return The newly created keypair.
	 * @throws NoSuchProviderException
	 * @throws GeneralSecurityException
	 * @throws NoSuchPropertyException
	 * @throws SecurityProviderException
	 */
	public static GlobusKeyPair globusKeyPairFactory(int keysize)
			throws GeneralSecurityException, NoSuchProviderException {

		GlobusKeyPair globus_pair = new GlobusKeyPair();

		SecureRandom random = new SecureRandom();
		KeyPairGenerator generator = null;

		generator = KeyPairGenerator.getInstance("RSA", "BC");
		try {
			generator.initialize(
					// initialize the generator with the your chosen Keysize and the
					// defaults recommended in X.509
					new RSAKeyGenParameterSpec(keysize, RSAKeyGenParameterSpec.F4),
					random);
		} catch (IllegalArgumentException e) {
			throw new GeneralSecurityException(e.getMessage());
		}

		KeyPair pair = generator.generateKeyPair();

		globus_pair
				.setPrivateKey(new BouncyCastleOpenSSLKey(pair.getPrivate()));
		// TODO check whether casting is ok??
		globus_pair.setPublicKey((RSAPublicKey) pair.getPublic());
		globus_pair.encryptedPrivate = false;

		return globus_pair;
	}

	/**
	 * @return the privateKey
	 */
	public BouncyCastleOpenSSLKey getPrivateKey() {

		return privateKey;
	}

	/**
	 * @param privateKey
	 *            the privateKey to set
	 */
	public void setPrivateKey(BouncyCastleOpenSSLKey privateKey) {

		this.privateKey = privateKey;
	}

	/**
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {

		return publicKey;
	}

	/**
	 * @param publicKey
	 *            the publicKey to set
	 */
	public void setPublicKey(RSAPublicKey publicKey) {

		this.publicKey = publicKey;
	}

	/**
	 * @return whether the containing private key is encrypted or not
	 */
	public boolean privateKeyisEncrypted() {

		return encryptedPrivate;
	}

	/**
	 * Encrypts the private key with the given passphrase.
	 * 
	 * @param passphrase
	 *            the passphrase to encrypt the private key
	 * @throws GeneralSecurityException 
	 */
	public void encryptPrivateKey(char[] passphrase) throws GeneralSecurityException  {

		this.privateKey.encrypt(new String(passphrase));
		//Arrays.fill(passphrase,'x');
		this.encryptedPrivate = true;
	}

	/**
	 * Decrypts the private key.
	 * 
	 * @param passphrase
	 *            the passphrase to decrypt the private key
	 * @throws GeneralSecurityException 
	 * @throws GeneralSecurityException 
	 */
	public void decryptPrivateKey(char[] passphrase) throws GeneralSecurityException {

			this.privateKey.decrypt(passphrase.toString());
			Arrays.fill(passphrase, 'x');
			this.encryptedPrivate = false;
	}

	/**
	 * Saves the private Key into a file. Does not check whether the key is
	 * encrypted or not.
	 * 
	 * @param privKeyFile
	 * @throws IOException
	 * @throws FileAlreadyExistsException
	 * @throws PathNotAvailableException
	 */
	public void savePrivateKeyToFile(File privKeyFile) throws IOException{

		if (privKeyFile.exists()) throw new IOException("File: "+privKeyFile.toString()+" already exists.");
		if (!privKeyFile.getParentFile().exists()){
			if (!privKeyFile.getParentFile().mkdirs()) throw new IOException("Can't create directory: "+privKeyFile.getParent());
		}
		if (!privKeyFile.canWrite()) throw new IOException("Can't write file: "+privKeyFile.toString());
		this.privateKey.writeTo(new FileOutputStream(privKeyFile));
	}

	/**
	 * Not implemented (yet?). Do I need it at all?
	 * 
	 * @param pubKeyFile
	 */
	public void savePublicKeyToFile(String pubKeyFile) {

		throw new RuntimeException("Not implemented.");
		// TODO save public key
	}

}
