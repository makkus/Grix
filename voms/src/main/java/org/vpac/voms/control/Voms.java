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

package org.vpac.voms.control;

import gridpp.portal.voms.VOMSAttributeCertificate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.globus.common.CoGProperties;
import org.globus.gsi.GlobusCredential;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.vpac.qc.model.clients.ClientNotInitializedException;
import org.vpac.qc.model.query.ArgumentsException;
import org.vpac.qc.model.query.InfoQuery;
import org.vpac.vomrs.model.VomrsClient;
import org.vpac.voms.model.VO;
import org.vpac.voms.model.proxy.VomsProxyCredential;

/**
 * A voms is the combination of VO and the information about the membership of
 * the current user (like status) in this VO.
 * 
 * @author Markus Binsteiner
 * 
 */
public class Voms {

	static final Logger myLogger = Logger.getLogger(Voms.class.getName());

	public final static File VOMSES = new File(System.getProperty("user.home")
			+ File.separator + ".glite" + File.separator + "vomses");

	public static final int MEMBER = 0;

	public static final int APPLICANT = 1;

	public static final int CANDIDATE = 2;

	public static final int NO_MEMBER = 3;

	public static final int NON_VOMRS_MEMBER = 4;

	public static final int NO_CONNECTION_TO_VOMRS = -1;

	private VO vo = null;

	private VomrsClient client = null;

	private InfoQuery infoQuery = null;
	private AttributeCertificate ac = null;

	private int status = -2;

	public String toString() {
		return this.vo.toString();
	}

	/**
	 * Refresh the membership information within this voms.
	 * 
	 * @return the status of the member in the VO
	 */
	public int updateVoms() {

		try {
			infoQuery = new InfoQuery("getMbrInfo", client, "Member");
			infoQuery.init();
			infoQuery.submit();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		} catch (ArgumentsException e) {
			// TODO That does not really belong here!!!
			myLogger.debug("Exception in query: " + e.getMessage());
			status = NO_CONNECTION_TO_VOMRS;
		}

		if (Arrays.binarySearch(client.getMyContexts(), "Member") >= 0) {
			status = MEMBER;
		} else if (Arrays.binarySearch(client.getMyContexts(), "Applicant") >= 0) {
			status = APPLICANT;
		} else if (Arrays.binarySearch(client.getMyContexts(), "Candidate") >= 0) {
			status = CANDIDATE;
		} else if (Arrays.binarySearch(client.getMyContexts(), "Visitor") >= 0) {
			status = NO_MEMBER;
		} else {
			status = -1;
		}

		return status;
	}

	/**
	 * The constructor contacts the VOMS server/VO with the current grid proxy
	 * and retrieves the membership information of this DN/user to build the
	 * voms. In the curse of this a VomrsClient is created to retrieve
	 * membership information for this VO from the according VOMRS server.
	 * 
	 * @param vo
	 *            the VO
	 */
	public Voms(VO vo) {
		this.vo = vo;
		try {

			// if (true ) throw new ArgumentsException("test");

			InputStream in = Voms.class
					.getResourceAsStream("/org/vpac/voms/control/queries.xml");

			SAXBuilder builder = new SAXBuilder();
			Document doc = null;
			doc = builder.build(in);

			this.client = VomrsClient.getVomrsClient(vo, doc);
			infoQuery = new InfoQuery("getMbrInfo", client, "Member");
			infoQuery.init();
			infoQuery.submit();
		} catch (ClientNotInitializedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			myLogger.error(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			status = -1;
			return;
		} catch (ArgumentsException e) {
			// TODO That does not really belong here!!!
			myLogger.debug("Exception in query: " + e.getMessage());
			status = NO_CONNECTION_TO_VOMRS;

			GlobusCredential globusCredential = null;
			try {
				globusCredential = new GlobusCredential(CoGProperties
						.getDefault().getProxyFile());

				ac = VomsProxyCredential.getAC(globusCredential, vo, "A", null,
						1);

				if (ac != null) {
					status = NON_VOMRS_MEMBER;
					return;
				}

			} catch (Exception e1) {
				myLogger.error(e1);
				status = NO_CONNECTION_TO_VOMRS;
				return;
			}
			return;
		}
		if (Arrays.binarySearch(client.getMyContexts(), "Member") >= 0) {
			status = MEMBER;
		} else if (Arrays.binarySearch(client.getMyContexts(), "Applicant") >= 0) {
			status = APPLICANT;
		} else if (Arrays.binarySearch(client.getMyContexts(), "Candidate") >= 0) {
			status = CANDIDATE;
		} else if (Arrays.binarySearch(client.getMyContexts(), "Visitor") >= 0) {
			status = NO_MEMBER;
		} else {
			status = -1;
		}

	}

	/**
	 * Takes the line (as explained in the LocalVomses class) that describes
	 * this VO and parses it to extract information to be able to create a VO.
	 * 
	 * @param line
	 *            the first line of a vomses file
	 * @return the according VO
	 */
	public static VO parseVomsesLine(String line) {

		int start = line.indexOf("\"") + 1;
		int end = line.indexOf("\"", start + 1);

		if (start < 0 || end < 0)
			return null;

		String name = line.substring(start, end);

		start = line.indexOf("\"", end + 1) + 1;
		end = line.indexOf("\"", start + 1);

		if (start < 0 || end < 0)
			return null;

		String host = line.substring(start, end);

		start = line.indexOf("\"", end + 1) + 1;
		end = line.indexOf("\"", start + 1);

		if (start < 0 || end < 0)
			return null;

		int port = -1;
		try {
			port = Integer.parseInt(line.substring(start, end));
		} catch (NumberFormatException e) {
			return null;
		}
		if (port < 1)
			return null;

		start = line.indexOf("\"", end + 1) + 1;
		end = line.indexOf("\"", start + 1);

		if (start < 0 || end < 0)
			return null;

		String hostDN = line.substring(start, end);

		start = line.indexOf("\"", end + 1) + 1;
		end = line.indexOf("\"", start + 1);

		// no use for that
		String stupidName = line.substring(start, end);

		start = line.indexOf("\"", end + 1) + 1;
		end = line.indexOf("\"", start + 1);

		if (start < 1) {
			myLogger.debug(name + " " + host + " " + port + " " + hostDN);

			return new VO(name, host, port, hostDN);
		}

		String vomrsUrl = line.substring(start, end);

		myLogger.debug(name + " " + host + " " + port + " " + hostDN + vomrsUrl);

		return new VO(name, host, port, hostDN, vomrsUrl);
	}

	public boolean equals(Voms other) {

		if (other == null)
			return false;

		if (other.toString().equals(this.toString()))
			return true;
		else
			return false;
	}

	public VomrsClient getClient() {
		return client;
	}

	public int getStatus() {
		return status;
	}

	public VO getVo() {
		return vo;
	}

	/**
	 * The InfoQuery that is used to retrieve membership information with the
	 * VomrsClient.
	 * 
	 * @return the InfoQuery
	 */
	public InfoQuery getInfoQuery() {
		return infoQuery;
	}

	public String getVomsWebURL() {
		return "https://" + vo.getHost() + ":8443/voms/" + vo.getVoName();
	}

	public AttributeCertificate getAc() {
		return ac;
	}

	public boolean equals(Object other) {

		if (other == null)
			return false;

		Voms othervoms = null;

		try {
			othervoms = (Voms) other;
		} catch (ClassCastException e) {
			return false;
		}

		return equals(othervoms);
	}

}
