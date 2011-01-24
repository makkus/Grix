/* This class is a rewrite of the classes VOMSClient and
 * MyGloubsCredentialUtils 
 *
 * Gidon Moont from
 * Imperial College London
 *
 * wrote. I did not change the functionality, just some things like
 * logging to use it better with grix. 
 * So: all the credit goes to Gidon.
 */

package org.vpac.voms.model.proxy;

import gridpp.portal.voms.VincenzoBase64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.X509ExtensionSet;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.bc.BouncyCastleX509Extension;
import org.globus.gsi.gssapi.GSSConstants;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.gssapi.GlobusGSSManagerImpl;
import org.globus.gsi.gssapi.auth.Authorization;
import org.globus.gsi.gssapi.auth.IdentityAuthorization;
import org.globus.gsi.gssapi.net.GssSocket;
import org.globus.gsi.gssapi.net.GssSocketFactory;
import org.gridforum.jgss.ExtendedGSSContext;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.model.VO;

/**
 * The actual credential that is build of a GlobusCredential and an
 * AttributeCertificate. The AttributeCertificate is sent by the VOMS server
 * after sending one of this commands:
 * 
 * <br>
 * A - this means get everything the server knows about you <br>
 * G/group - This means get group informations. /group should be /vo-name. <br>
 * This is the default request used by voms-proxy-init <br>
 * Rrole - This means grant me the specified role, in all groups in which <br>
 * you can grant it. <br>
 * Bgroup:role - This means grant me the specified role in the specified group.
 * 
 * @author Markus Binsteiner
 * 
 */
public class VomsProxyCredential {

	static final Logger myLogger = Logger.getLogger(VomsProxyCredential.class
			.getName());

	private GlobusCredential gridProxy = null;

	private GlobusCredential vomsProxy = null;

	private AttributeCertificate ac = null;

	private String command = null;
	private String order = null;
	private int lifetime = -1;

	private VO vo = null;

	/**
	 * Don't use this.
	 * 
	 * @throws Exception
	 */
	// public VomsProxyCredential() throws Exception {
	// this(LocalProxy.getDefaultProxy().getGlobusCredential(), VO
	// .getDefaultVO(), "G/" + VO.getDefaultVO().getVoName(), 10000);
	// }

	/**
	 * The default constructor. Assembles a VomsProxyCredential.
	 * 
	 * @param gridProxy
	 *            a X509 proxy (can be the local proxy or a myproxy proxy
	 *            credential.
	 * @param vo
	 *            the VO
	 * @param command
	 *            the command to send to the VOMS server
	 * @param lifetime_in_hours
	 *            the lifetime of the proxy in hours
	 * @throws Exception
	 *             if something fails, obviously
	 */
	public VomsProxyCredential(GlobusCredential gridProxy, VO vo,
			String command, String order, int lifetime_in_hours)
			throws Exception {
		this.gridProxy = gridProxy;
		this.vo = vo;
		this.command = command;
		this.order = order;
		this.lifetime = lifetime_in_hours * 3600;
		getAC();
		generateProxy();
	}

	public static AttributeCertificate getAllInfoAttributeCertificate(
			GlobusCredential gridProxy, VO vo, int lifetime_in_hours) {

		return null;
	}

	public AttributeCertificate getAttributeCertificate() {
		return ac;
	}

	/**
	 * Contacts the VOMS server to get an AttributeCertificate
	 * 
	 * @return true if successful, false if not
	 * @throws GSSException
	 * @throws IOException
	 */
	protected boolean getAC() {

		try {
			ac = getAC(gridProxy, vo, command, order, lifetime);
		} catch (Exception e) {
			return false;
		}

		if (ac != null)
			return true;
		else
			return false;
	}

	/**
	 * Contacts the voms server and retrieves an AC
	 * 
	 * @param credential
	 *            the credential to authenticate against the voms server
	 * @param virtOrg
	 *            the virtual organisation you want the ac for
	 * @param command_for_voms_server
	 *            "A" for all groups "G/Group" for spec. group
	 * @param lifetime_in_hours
	 *            the lifetime in ...hours
	 * @return the AttributeCertifiate with all the requested information in it
	 * @throws GSSException
	 *             if something's wrong with the credential
	 * @throws IOException
	 *             if the connection with the voms server fails
	 */
	public static AttributeCertificate getAC(GlobusCredential credential,
			VO virtOrg, String command_for_voms_server, String order_for_fqans,
			int lifetime_in_hours) throws GSSException, IOException {

		int server = 0;

		myLogger.debug("Contacting VOMS server [" + virtOrg.getHost() + "]...");

		GSSManager manager = new GlobusGSSManagerImpl();

		Authorization authorization = new IdentityAuthorization(
				virtOrg.getHostDN());

		GSSCredential clientCreds = (GSSCredential) new GlobusGSSCredentialImpl(
				credential, GSSCredential.INITIATE_ONLY);

		ExtendedGSSContext context = (ExtendedGSSContext) manager
				.createContext(null, GSSConstants.MECH_OID, clientCreds,
						GSSContext.DEFAULT_LIFETIME);

		context.requestMutualAuth(true);
		context.requestCredDeleg(false);
		context.requestConf(true);
		context.requestAnonymity(false);

		context.setOption(GSSConstants.GSS_MODE, GSIConstants.MODE_GSI);
		context.setOption(GSSConstants.REJECT_LIMITED_PROXY, new Boolean(false));

		GssSocket socket = (GssSocket) GssSocketFactory.getDefault()
				.createSocket(virtOrg.getHost(), virtOrg.getPort(), context);

		socket.setWrapMode(GssSocket.GSI_MODE);
		socket.setAuthorization(authorization);

		OutputStream out = ((Socket) socket).getOutputStream();
		InputStream in = ((Socket) socket).getInputStream();

		String msg = null;

		if (order_for_fqans == null || "".equals(order_for_fqans)) {
			msg = new String(
					"<?xml version=\"1.0\" encoding = \"US-ASCII\"?><voms><command>"
							+ command_for_voms_server + "</command><lifetime>"
							+ lifetime_in_hours + "</lifetime></voms>");
		} else {
			msg = new String(
					"<?xml version=\"1.0\" encoding = \"US-ASCII\"?><voms><command>"
							+ command_for_voms_server + "</command><order>"
							+ order_for_fqans + "</order><lifetime>"
							+ lifetime_in_hours + "</lifetime></voms>");
		}
		byte[] outToken = msg.getBytes();

		out.write(outToken);
		out.flush();

		StringBuffer voms_server_answer = new StringBuffer();

		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = buff.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			voms_server_answer.append(readData);
			buf = new char[1024];
		}

		// String answer = buff.readLine();

		out.close();
		in.close();
		buff.close();

		String answer = voms_server_answer.toString();

		if (answer.indexOf("<error>") > 1) {
			String errormsg = answer.substring(answer.indexOf("<message>") + 9,
					answer.indexOf("</message>"));
			myLogger.warn("VOMS server returned an error => " + errormsg);
			server++;
		}

		String encoded;
		try {
			encoded = answer.substring(answer.indexOf("<ac>") + 4,
					answer.indexOf("</ac>"));
		} catch (IndexOutOfBoundsException e) {
			myLogger.warn("Could not find encoded voms proxy in server answer.");
			throw e;
		}

		System.out.println(" success");

		byte[] payload = VincenzoBase64.decode(encoded);

		ByteArrayInputStream is = new ByteArrayInputStream(payload);
		ASN1InputStream asnInStream = new ASN1InputStream(is);
		ASN1Sequence acseq = (ASN1Sequence) asnInStream.readObject();

		return new AttributeCertificate(acseq);

	}

	/**
	 * Returns all included AttributesCertificates of a GlobusCredential. In
	 * general we are only interested in the first one.
	 * 
	 * @param vomsProxy
	 *            the voms enabled proxy credential
	 * @return all AttributeCertificates
	 */
	public static ArrayList<AttributeCertificate> extractVOMSACs(
			GlobusCredential vomsProxy) {

		// the aim of this is to retrieve all VOMS ACs
		ArrayList<AttributeCertificate> acArrayList = new ArrayList<AttributeCertificate>();

		try {

			X509Certificate[] x509s = vomsProxy.getCertificateChain();

			for (int x = 0; x < x509s.length; x++) {

				try {

					byte[] payload = x509s[x]
							.getExtensionValue("1.3.6.1.4.1.8005.100.100.5");

					// Octet String encapsulation - see RFC 3280 section 4.1
					payload = ((ASN1OctetString) new ASN1InputStream(
							new ByteArrayInputStream(payload)).readObject())
							.getOctets();

					ASN1Sequence acSequence = (ASN1Sequence) new ASN1InputStream(
							new ByteArrayInputStream(payload)).readObject();

					for (Enumeration e1 = acSequence.getObjects(); e1
							.hasMoreElements();) {

						ASN1Sequence seq2 = (ASN1Sequence) e1.nextElement();

						for (Enumeration e2 = seq2.getObjects(); e2
								.hasMoreElements();) {

							AttributeCertificate ac = new AttributeCertificate(
									(ASN1Sequence) e2.nextElement());

							acArrayList.add(ac);

						}
					}

				} catch (Exception pe) {
					// System.out.println( "This part of the chain has no AC" )
					// ;
				}

			}

		} catch (Exception e) {
			// e.printStackTrace();
			myLogger.error(e);
		}

		return acArrayList;
	}

	private void generateProxy() throws GeneralSecurityException {

		// Extension 1
		DERSequence seqac = new DERSequence(this.ac);
		DERSequence seqacwrap = new DERSequence(seqac);
		BouncyCastleX509Extension ace = new BouncyCastleX509Extension(
				"1.3.6.1.4.1.8005.100.100.5", seqacwrap);

		// Extension 2
		KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature
				| KeyUsage.keyEncipherment | KeyUsage.dataEncipherment);
		BouncyCastleX509Extension kue = new BouncyCastleX509Extension(
				"2.5.29.15", keyUsage.getDERObject());

		// Extension Set
		X509ExtensionSet globusExtensionSet = new X509ExtensionSet();
		globusExtensionSet.add(ace);
		globusExtensionSet.add(kue);

		// generate new VOMS proxy
		BouncyCastleCertProcessingFactory factory = BouncyCastleCertProcessingFactory
				.getDefault();
		vomsProxy = factory.createCredential(gridProxy.getCertificateChain(),
				gridProxy.getPrivateKey(), gridProxy.getStrength(),
				(int) gridProxy.getTimeLeft(), GSIConstants.DELEGATION_FULL,
				globusExtensionSet);

	}

	/**
	 * @return the voms enabled proxy
	 */
	public GlobusCredential getVomsProxy() {
		return vomsProxy;
	}

}
