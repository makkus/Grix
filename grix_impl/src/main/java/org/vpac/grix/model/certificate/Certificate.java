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
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Collection;
import java.util.List;

import org.globus.gsi.CertUtil;
import org.vpac.grix.control.utils.DateHelper;

/**
 * This class resembles a certificate. It's used as a wrapper around the
 * X509Certificate class from the java api.
 * 
 * @author Markus Binsteiner
 * 
 */
public class Certificate {

	public static final String PEM_HEADER = "-----BEGIN CERTIFICATE-----";
	public static final String PEM_FOOTER = "-----END CERTIFICATE-----";

	private String issuer;

	private String c;

	private String cn;

	private String email;

	private String o;

	private String ou;

	private String dn;

	private String startdate;

	private String enddate;

	private String whole_certificate;

	private X509Certificate x509certificate;

	/**
	 * This default constructor fills all fields with the string "N/A".
	 */
	public Certificate() {

		this.issuer = "N/A";
		this.c = "N/A";
		this.cn = "N/A";
		this.email = "N/A";
		this.o = "N/A";
		this.ou = "N/A";
		this.dn = "N/A";
		this.startdate = "N/A";
		this.enddate = "N/A";
	}

	/**
	 * This constructor tries to initialize the Certificate with values from the
	 * given file.
	 * 
	 * @param file
	 *            the certificate file
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public Certificate(File file) throws IOException, GeneralSecurityException {

		this.loadCert(CertUtil.loadCertificate(file.toString()));

		StringBuffer sb = new StringBuffer(1024);
		BufferedReader reader = new BufferedReader(new FileReader(
				file.getAbsolutePath()));

		char[] chars = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(chars)) > -1) {
			sb.append(String.valueOf(chars));
		}

		reader.close();

		this.whole_certificate = sb.toString();

	}

	/**
	 * This takes the certificate as a string and parses it
	 * 
	 * @param cert
	 *            The whole certificate as String.
	 * @throws GeneralSecurityException
	 */
	public Certificate(String cert) throws GeneralSecurityException {

		this.whole_certificate = cert;

		this.loadCert(CertUtil.loadCertificate(new ByteArrayInputStream(cert
				.getBytes())));

	}

	public Certificate(X509Certificate cert) {

		loadCert(cert);

	}

	/**
	 * Writes the certificate to the specified file
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 */
	public void writeToFile(File file) throws IOException {

		FileWriter writer = new FileWriter(file);

		BufferedWriter buffWriter = new BufferedWriter(writer);

		buffWriter.write(this.whole_certificate);
		buffWriter.close();

		return;
	}

	/**
	 * Loads the values from a x509Certificate into the object.
	 * 
	 * @param file
	 *            the certificate file
	 * @throws LoadCertificateFileException
	 */
	public void loadCert(X509Certificate cert) {

		this.x509certificate = cert;

		this.dn = x509certificate.getSubjectDN().getName();

		this.fillDNcomponents();

		this.issuer = x509certificate.getIssuerDN().getName();

		DateFormat df = DateHelper.getDateFormat();

		this.startdate = df.format(x509certificate.getNotBefore());
		this.enddate = df.format(x509certificate.getNotAfter());

		try {
			this.email = this.getEmail(x509certificate
					.getSubjectAlternativeNames());
		} catch (CertificateParsingException cpe) {
			this.email = "N/A";
		}

	}

	/**
	 * Parses the dn and fills all the components in the apropriate fields.
	 * 
	 */
	private void fillDNcomponents() {

		// TODO exceptionhandling host name certificate??
		// System.out.println(dn);
		int start = this.dn.indexOf("C=");
		int end = this.dn.indexOf(",");
		this.c = this.dn.substring(start + 2, end);
		start = this.dn.indexOf("O=");
		end = this.dn.indexOf(",", start);
		this.o = this.dn.substring(start + 2, end);
		start = this.dn.indexOf("OU=");
		end = this.dn.indexOf(",", start);
		this.ou = this.dn.substring(start + 3, end);
		start = this.dn.indexOf("CN=");
		this.cn = this.dn.substring(start + 3);
	}

	/**
	 * This method tries to parse a collection returned by
	 * getSubjectAlternativeNames() and return the email address of the
	 * certificate owner.
	 * 
	 * @param coll
	 *            the collection of SubjectAlternativeNames
	 * @return the email address of the certificate owner or, if not found, the
	 *         string "N/A"
	 */
	private String getEmail(Collection coll) {

		// return "markus@vpac.org";
		String email = "N/A";
		if (coll == null)
			return email;
		for (Object item : coll) {
			Integer type = (Integer) ((List) item).get(0);
			if (type.intValue() == 1) {
				email = (String) ((List) item).get(1);
				break;
			}
		}
		return email;
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
	public String getDn() {

		return dn;
	}

	/**
	 * @param dn
	 *            the dn to set
	 */
	public void setDn(String dn) {

		this.dn = dn;
		fillDNcomponents();
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
	 * @return the enddate
	 */
	public String getEnddate() {

		return enddate;
	}

	/**
	 * @param enddate
	 *            the enddate to set
	 */
	public void setEnddate(String enddate) {

		this.enddate = enddate;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {

		return issuer;
	}

	/**
	 * @param issuer
	 *            the issuer to set
	 */
	public void setIssuer(String issuer) {

		this.issuer = issuer;
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

	/**
	 * @return the startdate
	 */
	public String getStartdate() {

		return startdate;
	}

	/**
	 * @param startdate
	 *            the startdate to set
	 */
	public void setStartdate(String startdate) {

		this.startdate = startdate;
	}

	/**
	 * @return the whole_certificate
	 */
	public String getWholeCertificate() {

		return whole_certificate;
	}

	/**
	 * @param whole_certificate
	 *            the whole_certificate to set
	 */
	public void setWholeCertificate(String whole_certificate) {

		this.whole_certificate = whole_certificate;
	}

}
