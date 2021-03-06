/* Copyright 2006 VPAC
 * 
 * This file is part of grix-proxy.
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

package org.vpac.voms.model;

/**
 * A VO consist of the name, a host the management software (VOMS/VOMRS) is
 * hosted, the port to contact the VOMS server (for voms proxy creation) and the
 * DN of the host. Also the url for the endpoint of the VOMRS webservice. At the
 * moment this url is created throught String concatenation in a dodgy way. So,
 * unless your VOMRS server is set up in a way to accomodate that, don't rely on
 * it.
 * 
 * @author Markus Binsteiner
 * 
 */
public class VO {

	private String voName = null;
	private String host = null;
	private int port = -1;
	private String hostDN = null;

	private String vomrs_url = null;

	/**
	 * The default constructor. As said above, the vomrs_url is created in a
	 * dodgy way, so beware.
	 * 
	 * @param voName
	 *            the name of the VO
	 * @param host
	 *            the host of the VOMS/VOMRS server of this VO
	 * @param port
	 *            the port of this VO on the VOMS server
	 * @param hostDN
	 *            the host dn
	 */
	public VO(String voName, String host, int port, String hostDN) {
		this.voName = voName;
		this.host = host;
		this.port = port;
		this.hostDN = hostDN;
		this.vomrs_url = "https://" + host + ":8443/vo/" + voName
				+ "/services/VOMRS?wsdl";
	}

	public VO(String voName, String host, int port, String hostDn,
			String vomrsURL) {
		this.voName = voName;
		this.host = host;
		this.port = port;
		this.hostDN = hostDn;
		this.vomrs_url = vomrsURL;
	}

	/**
	 * Just for testing. Don't use.
	 * 
	 * @return
	 */
	// public static VO getDefaultVO(){
	// //TODO
	// return new VO("Chris", "vomrsdev.vpac.org", 15003,
	// "/C=AU/O=APACGrid/OU=VPAC/CN=vomrsdev.vpac.org");
	// }

	public String getHost() {
		return host;
	}

	public String getHostDN() {
		return hostDN;
	}

	public int getPort() {
		return port;
	}

	public String getVoName() {
		return voName;
	}

	public String toString() {
		return getVoName();
	}

	public String getVomrs_url() {
		return vomrs_url;
		// return "https://"+host+":8443/vo/"+voName+"/services/VOMRS?wsdl";
	}

	public String getVOMSURL() {
		return null; // TODO
	}

}
