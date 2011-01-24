/* Copyright 2006 VPAC
 * 
 * This file is part of voms.
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

package org.vpac.voms.model.proxy;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERSequence;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.X509ExtensionSet;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.globus.gsi.bc.BouncyCastleX509Extension;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.myproxy.InitParams;
import org.globus.myproxy.MyProxy;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.vpac.common.model.GlobusLocations;

/**
 * Dummy. Don't use. Just example code.
 * 
 * @author Markus Binsteiner
 * 
 */
public class VomsMyProxy {

	static final Logger myLogger = Logger
			.getLogger(VomsMyProxy.class.getName());

	public static void dummy() {
		try {
			// Testing from here on:
			CertUtil.init();
			BouncyCastleCertProcessingFactory factory = BouncyCastleCertProcessingFactory
					.getDefault();
			GlobusCredential globusCred = null;

			X509Certificate cert = null;
			OpenSSLKey key = null;

			InputStream inCert;

			inCert = new FileInputStream(GlobusLocations.defaultLocations()
					.getUserCert());
			InputStream inKey = new FileInputStream(GlobusLocations
					.defaultLocations().getUserKey());
			cert = CertUtil.loadCertificate(inCert);
			key = new BouncyCastleOpenSSLKey(inKey);

			key.decrypt(new String("xxx"));
			myLogger.debug("Decrypted key.");

			myLogger.debug("Could not decrypt private key. Most likely wrong myProxyPassphrase.");

			// Arrays.fill(key_passphrase, 'x');

			globusCred = factory.createCredential(
					new X509Certificate[] { cert }, key.getPrivateKey(), 512,
					7 * 60 * 60 * 24, GSIConstants.GSI_3_IMPERSONATION_PROXY);
			// lifetime, GSIConstants.GSI_3_IMPERSONATION_PROXY);
			myLogger.debug("Created credentials.");

			// Extension 1
			// DERSequence seqac = new
			// DERSequence(vomsProxyCredential.getAttributeCertificate());
			DERSequence seqac = new DERSequence();
			DERSequence seqacwrap = new DERSequence(seqac);
			BouncyCastleX509Extension ace = new BouncyCastleX509Extension(
					"1.3.6.1.4.1.8005.100.100.5", seqacwrap);

			// Extension 2null
			// KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature
			// | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment);
			// BouncyCastleX509Extension kue = new BouncyCastleX509Extension(
			// "2.5.29.15", keyUsage.getDERObject());

			// Extension Set
			X509ExtensionSet globusExtensionSet = new X509ExtensionSet();
			globusExtensionSet.add(ace);
			// globusExtensionSet.add(kue);

			// generate new VOMS proxy
			BouncyCastleCertProcessingFactory factory2 = BouncyCastleCertProcessingFactory
					.getDefault();
			GlobusCredential vomsProxy = factory2.createCredential(
					globusCred.getCertificateChain(),
					globusCred.getPrivateKey(), globusCred.getStrength(),
					(int) globusCred.getTimeLeft(),
					GSIConstants.DELEGATION_FULL, globusExtensionSet);

			GSSCredential gssCred = null;
			try {
				// gssCred = new
				// GlobusGSSCredentialImpl(vomsProxyCredential.getVomsProxy(),
				gssCred = new GlobusGSSCredentialImpl(vomsProxy,
						GSSCredential.INITIATE_AND_ACCEPT);
				myLogger.debug("Created gss_credentials.");
			} catch (GSSException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
				// return null;
			}

			InitParams initRequest = new InitParams();
			initRequest.setUserName("markus_voms2");
			// this is the lifetime of credentials after a user downloads them
			initRequest.setLifetime(60 * 60 * 1);
			// TODO what are the following things good for?
			// initRequest.setCredentialDescription( "createdWithGrix" );
			// initRequest.setCredentialName( "createdWithGrix" );

			if (true) {
				initRequest.setRenewer("*");

				initRequest.setRetriever("*");
				initRequest.setCredentialName("test");
			}

			initRequest.setPassphrase(new String("nixenixe25"));

			MyProxy myProxy = new MyProxy("myproxy.arcs.org.au", 443);
			myLogger.debug("Created myproxy.");

			// myProxy.put( gssCred, myProxyUsername, new
			// String(myProxyPassphrase),
			// lifetime );
			myProxy.put(gssCred, initRequest);
			//
			// ManagePropertyFile.addToList("MYPROXY_USERNAMES",
			// myProxyUsername);
			myLogger.debug("Put myproxy credentials on server.");

			try {
				gssCred.dispose();
				myLogger.debug("Disposed gss_credentials.");
			} catch (GSSException e2) {
				myLogger.debug("GSSException: " + e2.getMessage());
				// TODO Auto-generated catch block
				e2.printStackTrace();
				// return null;
			} // very important to dispose the long-live credential after
				// storage!

			// MyProxyCred cred = new MyProxyCred(myProxyUsername, "DUMMY_DUMMY"
			// .toCharArray());
			// cred.getInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
