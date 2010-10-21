package org.vpac.grix.model.myproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.myproxy.CredentialInfo;
import org.globus.myproxy.InitParams;
import org.globus.myproxy.MyProxy;
import org.globus.myproxy.MyProxyException;
import org.gridforum.jgss.ExtendedGSSManager;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.vpac.common.model.GlobusLocations;
import org.vpac.grix.control.utils.GrixProperty;
import org.vpac.grix.control.utils.UserProperty;

/**
 * MyProxy related tasks. Have to rewrite that.
 * 
 * @author Markus Binsteiner
 * 
 */
public class MyProxyCred {

	static final Logger myLogger = Logger
			.getLogger(MyProxyCred.class.getName());

	static public final String myProxyHost = GrixProperty
			.getString("myproxy.server");
	static public final int myProxyPort = GrixProperty.getInt("myproxy.port");
	static public final int myProxyKeylength = GrixProperty
			.getInt("myproxy.keysize");

	private String username = null;

	private char[] myProxyPassphrase = null;

	private String retrievers = null;

	private String renewers = null;

	private Date expires = null;

	private String credential_name = "";

	private File privateKey = null;

	private File certificate = null;

	private boolean isLoaded = false;

	/**
	 * This creates a MyProxyCred without a password. You can use it for getting
	 * info about the credential (lifetime, etc) if you have a valid grid proxy.
	 * 
	 * @param username
	 *            the username of the MyProxy credentials (on the MyProxy
	 *            server).
	 */
	public MyProxyCred(String username) {

		this.username = username;
		this.myProxyPassphrase = "DUMMY_DUMMY".toCharArray();
		this.privateKey = GlobusLocations.defaultLocations().getUserKey();
		this.certificate = GlobusLocations.defaultLocations().getUserCert();
		this.expires = null;
		this.renewers = "n/a";
		this.retrievers = "n/a";
		try {
			getInfo();
		} catch (MyProxyException mpe) {
			// does not matter here
			// mpe.printStackTrace();
		}
	}

	/**
	 * This is used for uploading real MyProxy credentials to the server. It
	 * uses the default certificate and private key (in .globus directory)
	 * 
	 * @param username
	 *            the username
	 * @param myProxyPassphrase
	 *            the myProxyPassphrase
	 */
	public MyProxyCred(String username, char[] myProxyPassphrase) {

		this.privateKey = GlobusLocations.defaultLocations().getUserKey();
		this.certificate = GlobusLocations.defaultLocations().getUserCert();
		this.username = username;
		this.myProxyPassphrase = myProxyPassphrase;
		this.expires = null;
		this.renewers = "n/a";
		this.retrievers = "n/a";
		try {
			getInfo();
		} catch (MyProxyException e) {
			// does not matter here
		}
		Arrays.fill(myProxyPassphrase, 'x');
	}

	public GlobusCredential createGlobusCredential(char[] key_passphrase,
			int lifetime) throws IOException, GeneralSecurityException {
		// TODO check whether proxy already exists?

		CertUtil.init();
		BouncyCastleCertProcessingFactory factory = BouncyCastleCertProcessingFactory
				.getDefault();
		GlobusCredential globusCred = null;

		X509Certificate cert = null;
		OpenSSLKey key = null;

		InputStream inCert;

		inCert = new FileInputStream(certificate);
		InputStream inKey = new FileInputStream(privateKey);
		cert = CertUtil.loadCertificate(inCert);
		key = new BouncyCastleOpenSSLKey(inKey);

		key.decrypt(new String(key_passphrase));
		myLogger.debug("Decrypted key.");

		// myLogger.debug("Could not decrypt private key. Most likely wrong myProxyPassphrase.");

		Arrays.fill(key_passphrase, 'x');

		globusCred = factory.createCredential(new X509Certificate[] { cert },
				key.getPrivateKey(), myProxyKeylength, lifetime,
				GSIConstants.GSI_3_IMPERSONATION_PROXY);
		// lifetime, GSIConstants.GSI_3_IMPERSONATION_PROXY);
		myLogger.debug("Created credentials.");

		return globusCred;
	}

	public boolean equals(MyProxyCred other) {

		if (other.getUsername().equals(this.username)) {
			return true;
		}
		return false;
	}

	public Date getExpires() {

		// System.out.println ("Expires: "+ expires.toLocaleString());
		return expires;
	}

	/**
	 * Loads information about the proxy (renewers, retrievers, lifetime)
	 * 
	 * @throws GSSException
	 *             if you don't have a credential file (/tmp/x509up_u...) to
	 *             authenticate against the myproxy server on your computer
	 * @throws MyProxyException
	 *             Not sure when this occurs.
	 */
	public void getInfo() throws MyProxyException {

		isLoaded = false;

		MyProxy myProxy = new MyProxy(myProxyHost, myProxyPort);
		GSSManager manager = ExtendedGSSManager.getInstance();
		GSSCredential cred = null;
		try {
			cred = manager.createCredential(GSSCredential.INITIATE_ONLY);
		} catch (GSSException e) {
			// should I throw an exception here?
			return;
		}

		CredentialInfo info = myProxy.info(cred, username, new String(
				myProxyPassphrase));

		expires = info.getEndTimeAsDate();
		retrievers = info.getRetrievers();
		if ("".equals(retrievers)) {
			retrievers = GrixProperty.getString("myproxy.default.retriever");
		} else if ("*".equals(retrievers)) {
			retrievers = "anonymous";
		}
		renewers = info.getRenewers();
		if ("".equals(renewers)) {
			renewers = GrixProperty.getString("myproxy.default.renewer");
		} else if ("*".equals(renewers)) {
			renewers = "anonymous";
		}

		isLoaded = true;

	}

	public String getRenewers() {

		return renewers;
	}

	public String getRetrievers() {

		return retrievers;
	}

	public String getUsername() {

		return username;
	}

	/**
	 * Uploads Proxy credentials to a MyProxy server. If there are already
	 * credentials with the same username on the server (created with the same
	 * certificate, of course) then they will be overwritten.
	 * 
	 * @param anonymous
	 *            whether you want the credentials to be anonymously
	 *            renewable/retrievable or not
	 * @param lifetime
	 *            the lifetime of the credentials
	 * @param myProxyPassphrase
	 *            the myProxyPassphrase of your private key
	 * @param myProxyPassphrase
	 *            the myProxyPassphrase for the credentials on the server
	 * @throws GeneralSecurityException
	 *             if you provide the wrong private key myProxyPassphrase
	 * @throws MyProxyException
	 *             if the credential could not be created. (maybe the
	 *             myProxyPassphrase for the MyProxy credential is too short or
	 *             not complex enough, or the username already exists for
	 *             another certificate, or...
	 * @throws IOException
	 *             if the certificate / private key are not readable
	 * @throws GSSException
	 *             I have not figured that out yet.
	 */
	public void init(GlobusCredential globusCred, boolean anonymous,
			char[] myProxyPassphrase, int credentials_lifetime_in_hours)
			throws GeneralSecurityException, MyProxyException, IOException,
			GSSException {

		if (myProxyPassphrase.length < GrixProperty
				.getInt("myproxy.minimal.password.length")) {
			throw new MyProxyException(
					"Passphrase must be at least 6 characters long.");
		}

		GSSCredential gssCred = null;

		gssCred = new GlobusGSSCredentialImpl(globusCred,
				GSSCredential.INITIATE_AND_ACCEPT);
		myLogger.debug("Created gss_credentials.");

		InitParams initRequest = new InitParams();
		initRequest.setUserName(username);
		// this is the lifetime of credentials after a user downloads them
		// TODO
		initRequest.setLifetime(60 * 60 * credentials_lifetime_in_hours);

		// TEST
		// anonymous = true;
		// initRequest.setLifetime(60);
		// TODO what are the following things good for?
		// initRequest.setCredentialDescription( "createdWithGrix" );
		if (!"".equals(credential_name)) {
			initRequest.setCredentialName(credential_name);
		}

		if (anonymous) {
			initRequest.setRenewer("*");
			initRequest.setRetriever("*");
			// initRequest.setCredentialName("test");
		} else {
			// check if properties contain server, if not, set renewer/retriever
			// accordingly. else server defaults are used.
			if (!GrixProperty.getString("myproxy.default.renewer")
					.toLowerCase().equals("server")) {
				initRequest.setRenewer(GrixProperty
						.getString("myproxy.default.renewer"));
			}
			if (!"server".equals(GrixProperty.getString(
					"myproxy.default.retriever").toLowerCase())) {
				initRequest.setRetriever(GrixProperty
						.getString("myproxy.default.retriever"));
			}
		}

		initRequest.setPassphrase(new String(myProxyPassphrase));
		Arrays.fill(myProxyPassphrase, 'x');

		MyProxy myProxy = new MyProxy(myProxyHost, myProxyPort);
		myLogger.debug("Created myproxy.");

		// myProxy.put( gssCred, myProxyUsername, new String(myProxyPassphrase),
		// lifetime );
		myProxy.put(gssCred, initRequest);
		UserProperty.addToList("MYPROXY_USERNAMES", username);
		myLogger.debug("Put myproxy credentials on server.");

		// very important to dispose the long-live credential after storage!
		gssCred.dispose();
		myLogger.debug("Disposed gss_credentials.");

		// MyProxyCred cred = new MyProxyCred(username, "DUMMY_DUMMY"
		// .toCharArray());
		// getInfo();

	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setCredentialName(String name) {
		this.credential_name = name;

	}

}
