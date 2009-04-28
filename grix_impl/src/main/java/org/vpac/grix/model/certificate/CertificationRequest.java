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


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.globus.gsi.CertUtil;
import org.globus.util.PEMUtils;
import org.vpac.common.exceptions.MissingInformationException;
import org.vpac.grix.control.utils.GrixProperty;


/**
 * A certification request similar/compatible with the one created by openssl
 * e.g.: openssl req -newkey rsa:1024 -config ./apacgrid-openssl.cnf -out
 * usercert-request.pem
 * 
 * @author Markus Binsteiner
 * 
 */
public class CertificationRequest {
	
	static final Logger myLogger = Logger.getLogger(CertificationRequest.class.getName());

	private final String signatureAlgorithm;

	private GlobusKeyPair keypair;

	private String c = "";

	private String o = "";

	private String ou = "";

	private String cn = "";

	private String email = "";

	private X509Name dn = null;

	/**
	 * Creates a certification request with the specified Subject. Generates a
	 * RSA keypair. The private key has to be stored by the user manually.
	 * 
	 * @param c
	 *            the country
	 * @param o
	 *            the organization
	 * @param ou
	 *            the organization unit
	 * @param cn
	 *            the name of the applicant
	 * @param email
	 *            the email address of the applicant
	 * @param passphrase
	 *            the passphrase to encrypt the private key
	 * @throws GeneralSecurityException 
	 * @throws MissingInformationException 
	 * @throws  
	 */
	public CertificationRequest(String c, String o, String ou, String cn,
			String email, int keysize, String signature_algorithm, boolean hostcert) throws GeneralSecurityException, MissingInformationException  {

		this.c = c;
		this.o = o;
		this.ou = ou;
		this.cn = cn;
		this.email = email;

		createDN(hostcert); 

		this.keypair = GlobusKeyPair.globusKeyPairFactory(keysize);
		this.signatureAlgorithm = signature_algorithm;
	}
	
	/**
	 * Creates a certification request with the specified Subject. Generates a
	 * RSA keypair. The private key has to be stored by the user manually.
	 * 
	 * @param c
	 *            the country
	 * @param o
	 *            the organization
	 * @param ou
	 *            the organization unit
	 * @param cn
	 *            the name of the applicant
	 * @param passphrase
	 *            the passphrase to encrypt the private key
	 * @throws GeneralSecurityException 
	 * @throws MissingInformationException 
	 * @throws  
	 */
	public CertificationRequest(String c, String o, String ou, String cn,
			int keysize, String signature_algorithm, boolean hostcert) throws GeneralSecurityException, MissingInformationException  {

		this.c = c;
		this.o = o;
		this.ou = ou;
		this.cn = cn;

		createDN(hostcert); 

		this.keypair = GlobusKeyPair.globusKeyPairFactory(keysize);
		this.signatureAlgorithm = signature_algorithm;
	}	

	/**
	 * Creates a certification request without user details. Only sets the
	 * signature algorithm and creates a keypair.
	 * @throws GeneralSecurityException 
	 * @throws  
	 * 
	 * @throws SecurityProviderException
	 */
	public CertificationRequest(int keysize, String signature_algorithm) throws GeneralSecurityException {

		this.keypair = GlobusKeyPair.globusKeyPairFactory(keysize);
		this.signatureAlgorithm = signature_algorithm;
	}

	/**
	 * Broken!!! This constructor does not read the public key in the .pem file
	 * It just parses the subject line and fills the dn fields.
	 * 
	 * @param file the certificate request with a subject line containing the DN
	 * @param signature_algorithm the algorithm for the private key. Use GrixProperty.getString("signature.algorithm");
	 * @param hostcert whether the certification request was for a hostcert or not
	 * @throws IOException 
	 * @throws MissingInformationException 
	 */
	public CertificationRequest(File file, String signature_algorithm, boolean hostcert) throws IOException, MissingInformationException  {
		
		this.signatureAlgorithm = signature_algorithm;

		BufferedReader input = null;
		try {
			input = new BufferedReader( new FileReader( file ) );
			String line = null; // not declared within while loop

			while ((line = input.readLine()) != null) {
				if ( line.startsWith( "/C=" ) ) {
					parseDN(line, hostcert);
				}
			}
		} finally {
			try {
				if ( input != null ) {
					// flush and close both "input" and its underlying
					// FileReader
					input.close();
				}
			} catch (Exception e) {
				//e.printStackTrace();
				myLogger.error(e);
			}
		}
	}

	private void parseDN(String line, boolean hostcert) throws MissingInformationException{

		this.c = line.substring( line.indexOf( "/C=" )+3, line.indexOf( "/O=" ) );
		this.o = line.substring( line.indexOf( "/O=" )+3, line.indexOf( "/OU=" ) );
		this.ou = line.substring( line.indexOf( "/OU=" )+4, line.indexOf( "/CN=" ) );
//		if (!hostcert){
		if ( line.indexOf("/Email") != -1 ){
			this.cn = line.substring( line.indexOf( "/CN=" )+4, line.indexOf( "/Email=" ) );
			this.email = line.substring( line.indexOf("/Email=" )+7 );
		} else {
			this.cn = line.substring( line.indexOf( "/CN=" )+4, line.indexOf( "/E=" ) );
			this.email = line.substring( line.indexOf("/E=" )+3 );
		}
//		} else {
//			this.cn = line.substring( line.indexOf( "/CN=")+4 );
//		}
		createDN(hostcert);
		
	}

	/**
	 * Concatenates all the user information to a DN and stores the DN into the
	 * X509Name field dn.
	 * @param hostcert whether this is for a hostcert or not
	 * @throws MissingInformationException 
	 */
	public void createDN(boolean hostcert) throws MissingInformationException {

		ArrayList<String> missingInformation = new ArrayList<String>();
		
		if ( this.c.equals("") ) missingInformation.add("C");
		if ( this.o.equals("") ) missingInformation.add("O");
		if ( this.ou.equals("") ) missingInformation.add("OU");
		if ( this.cn.equals("") ) missingInformation.add("CN");
		if ( this.email.equals("") ) missingInformation.add("EMAIL");

		
		if ( missingInformation.size() == 0 ) {
			if ( !hostcert ) {
			//user certificate
			this.dn = new X509Name( "C=" + c + ", O=" + o + ", OU=" + ou + ", CN="
					+ cn + ", emailAddress=" + email );
			} else {
				//host certificate
				this.dn = new X509Name( "C=" + c + ", O=" + o + ", OU=" + ou + ", CN="+ cn + ", emailAddress=" + email );

		}
		} else {
			
			throw new MissingInformationException(
			"Could not create DN: not enough information available", missingInformation );
		}
	}

	/**
	 * Creates a certification request with the specified//
	 * PEMUtils.writeBase64(ps, "-----BEGIN CERTIFICATE REQUEST-----", //
	 * encodedData, "-----END CERTIFICATE REQUEST-----"); Subject.
	 * 
	 * @param dn
	 *            the subject (e.g. "C=AU, O=APACGrid, OU=VPAC, ..."
	 * @param keypair
	 *            the GlobusKeyPair to use for the certification request.
	 * @throws NoSuchPropertyException
	 */
	public CertificationRequest(GlobusKeyPair keypair) {

		this.signatureAlgorithm = GrixProperty
				.getString( "signature.algorithm" );
		this.keypair = keypair;
	}

	/**
	 * creates a encoded representation of the certification request.
	 * 
	 * @return the certification request.
	 */
	public String getEncodedRequest() {

		String request_string = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// stolen from jglobus.jar:

		// nothing in it
		DERSet derset = new DERSet();
		PublicKey pubkey = this.keypair.getPublicKey();
		PrivateKey privkey = this.keypair.getPrivateKey().getPrivateKey();

		PKCS10CertificationRequest request = null;

		try {
			// using the already implemented .getEncoded() function of
			// PKCS10CertificationRequest
			request = new PKCS10CertificationRequest( this.signatureAlgorithm,
					this.dn, pubkey, derset, privkey );

			PEMUtils.writeBase64( out, "-----BEGIN CERTIFICATE REQUEST-----",
					Base64.encode( request.getEncoded() ),
					"-----END CERTIFICATE REQUEST-----" );

			request_string = out.toString();
			out.close();
		} catch (Exception e) {
			// TODO throw new SecurityProviderException("Could not write
			// certification request to file.");
			//e.printStackTrace();
			myLogger.error(e);
		}

		return request_string;
	}

	/**
	 * Writes the certification request to a PEM encoded file
	 * 
	 * @param certReqFile
	 *            the file
	 * @throws IOException 
	 * @throws Exception
	 */
	public void writeToPEMFile(File certReqFile) throws IOException { 
		
		if ( !certReqFile.getParentFile().exists() )
			if (!certReqFile.getParentFile().mkdirs()) throw new IOException("Could not create directory: "+certReqFile.getParent());
		
		//if ( !certReqFile.canWrite() ) throw new IOException("Can not write file: "+certReqFile.toString());

		PrintStream ps = null;

		try {
			ps = new PrintStream( new FileOutputStream( certReqFile ) );

			ps
					.print( "\n\n"
							+ "Please mail the following certificate request to "
							+ GrixProperty
									.getString( "CA_EMAIL_ADDRESS" )
							+ "\n"
							+ "\n"
							+ "==================================================================\n"
							+ "\n"
							+ "Certificate Subject:\n"
							+ "\n"
							+ CertUtil.toGlobusID( this.dn.toString() )
							+ "\n"
							+ "\n"
							+ "The above string is known as your user certificate subject, and it \n"
							+ "uniquely identifies this user.\n"
							+ "\n"
							+ "To install this user certificate, please save this e-mail message\n"
							+ "into the following file.\n"
							+ "\n"
							+ "\n"
							+ certReqFile.getAbsolutePath()
							+ "\n"
							+ "\n"
							+ "\n"
							+ "      You need not edit this message in any way. Simply \n"
							+ "      save this e-mail message to the file.\n"
							+ "\n"
							+ "\n"
							+ "If you have any questions about the certificate contact\n"
							+ "the Certificate Authority at "
							+ GrixProperty
									.getString( "CA_EMAIL_ADDRESS" )
							+ "\n" + "\n" + getEncodedRequest() );
		} finally {
			if ( ps != null ) {
				ps.close();
			}
		}

	}

	/**
	 * @return the keypair
	 */
	public GlobusKeyPair getKeypair() {

		return keypair;
	}

	/**
	 * @param keypair
	 *            the keypair to set
	 */
	public void setKeypair(GlobusKeyPair keypair) {

		this.keypair = keypair;
	}

	/**
	 * @return the c
	 */
	public String getC() {

		return c;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(String c) {

		this.c = c;
	}

	/**
	 * @return the cn
	 */
	public String getCn() {

		return cn;
	}

	/**
	 * @param cn
	 *            the cn to set
	 */
	public void setCn(String cn) {

		this.cn = cn;
	}

	/**
	 * @return the dn
	 */
	public X509Name getDn() {

		return dn;
	}
	
	public String getDNwithoutEmail() {
		
		int index = dn.toString().indexOf(",E=");
		if ( index == -1 )
			index = dn.toString().indexOf(",Email=");
		return dn.toString().substring(0, index);
	}

	/**
	 * @param dn
	 *            the dn to set
	 */
	public void setDn(X509Name dn) {

		this.dn = dn;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {

		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * @return the o
	 */
	public String getO() {

		return o;
	}

	/**
	 * @param o
	 *            the o to set
	 */
	public void setO(String o) {

		this.o = o;
	}

	/**
	 * @return the ou
	 */
	public String getOu() {

		return ou;
	}

	/**
	 * @param ou
	 *            the ou to set
	 */
	public void setOu(String ou) {

		this.ou = ou;
	}

}
