/* This class is a partly a rewrite of the classes VomsProxyInfo 
 *
 * Gidon Moont from
 * Imperial College London
 *
 * wrote. I did not change the functionality, just some things like
 * logging to use it better with grix. 
 * So: all the credit goes to Gidon.
 */


package org.vpac.voms.model.proxy;

import gridpp.portal.voms.VOMSAttributeCertificate;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.globus.gsi.GlobusCredential;
import org.globus.tools.proxy.DefaultGridProxyModel;
import org.vpac.common.exceptions.MissingPrerequisitesException;
import org.vpac.common.model.gridproxy.GridProxy;
import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.model.VO;

/**
 * A grid proxy that includes an AttributeCertificate that holds VO information.
 * 
 * @author Gidon Moont
 *
 */
public class VomsProxy extends GridProxy{
	
	static final Logger myLogger = Logger.getLogger(VomsProxy.class.getName());
	
	public static final File DEFAULT_DIR = new File(System.getProperty("user.home")+File.separator+".glite"+File.separator+"tmp");

	
	public static final String DEFAULT_ROLE = "TestRole";
	
	private VomsProxyCredential vomsProxyCredential = null;
	private AttributeCertificate ac = null;
	private VOMSAttributeCertificate vomsac = null;
	private VO vo = null;
	private String command = null;
	private String order = null;
	
	/**
	 * Creates a VomsProxy by parsing a file that contains a voms-enabled X509 proxy.
	 * 
	 * @param proxyFile the proxy file
	 * @throws NoVomsProxyException if the proxy is not a voms proxy
	 */
	public VomsProxy(File proxyFile) throws NoVomsProxyException{
		super(proxyFile);
		if ( proxyFile.exists() ) ac = extractAC();
		if ( ac == null ) {
			throw new NoVomsProxyException("Could not parse proxy. Most likely this is not a voms proxy.");
		}
		vomsac = new VOMSAttributeCertificate( ac ) ;
	}
	
	/**
	 * Creates an empty proxy. The creation is implemented in the createProxy method which is called with the init() method
	 * of the superclass.
	 * 
	 * @param proxyFile
	 * @param vo
	 * @param command
	 */
	public VomsProxy(File proxyFile, VO vo, String command, String order){
		super(proxyFile);
		this.vo = vo;
		this.command = command;
		this.order = order;
	}
	
	private AttributeCertificate extractAC(){
		
		ArrayList<AttributeCertificate> acs = VomsProxyCredential.extractVOMSACs(globusCredential) ;

		if ( acs == null || acs.size() == 0 )
			return null;
		
		else if ( acs.size() > 1) {
			myLogger.warn("More than one AttributeCertificates in the voms proxy. This is not implemented yet. Using the first one.");
		}
		
		return acs.get(0);
	}

	protected void checkPrerequisites() throws MissingPrerequisitesException {

		//TODO check this
		ArrayList<File> missingFiles = new ArrayList<File>();

		if (missingFiles.size() != 0)
			throw new MissingPrerequisitesException(missingFiles);
	}	
	
	protected GlobusCredential createProxy(char[] passphrase, long lifetime_in_ms) throws Exception{

		GlobusCredential globusProxy = null;
		DefaultGridProxyModel model = new DefaultGridProxyModel();
		model.getProperties().setProxyLifeTime(new Long((lifetime_in_ms)/(1000*3600)).intValue());
		File tmp_proxy = new File(this.proxyFile.toString()+"_tmp");
		myLogger.debug("Temp proxy: "+tmp_proxy.toString());
		model.getProperties().setProxyFile(tmp_proxy.toString());
		globusProxy = model.createProxy(new String(passphrase));
		
		vomsProxyCredential = new VomsProxyCredential(globusProxy, vo, command, order, new Long(lifetime_in_ms/1000).intValue());
		
		//tmp_proxy.delete();

		return vomsProxyCredential.getVomsProxy();
	}
	
	public static void main (String args[]){
		
//		GridProxy proxy = new VomsProxy(LocalProxy.getProxyFile(), VO.getDefaultVO(), "b/Markus/G2/G2S1:TestRole");
//		GridProxy proxy = new VomsProxy(LocalProxy.getProxyFile(), VO.getDefaultVO(), "B/Chris/G2/G2S1:TestRole");
//		GridProxy proxy = new VomsProxy(LocalProxy.getProxyFile(), VO.getDefaultVO(), "A", null);
		GridProxy proxy = null;
		try {
			proxy.init("xxx".toCharArray(), 3600000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the voms-enabled proxy credential
	 */
	public VomsProxyCredential getVomsProxyCredential() {
		return vomsProxyCredential;
	}

	/**
	 * @return the AttributeCertificate that contains VOMS information
	 */
	public VOMSAttributeCertificate getVomsac() {
		if ( vomsac == null ) {
			if ( proxyFile.exists() ) {
				ac = extractAC();
			if ( ac == null ) {
				return null;
			}
			vomsac = new VOMSAttributeCertificate( ac ) ;
			} else return null;
		}
		return vomsac;
	}

	@Override
	protected ArrayList<String> proxyInfo() {
		ArrayList<String> info = new ArrayList<String>();
		info.add("=== VO extension information ===");
		try {
		 info.add("issuer\t: " + vomsac.getIssuer() );
	       boolean checked = vomsac.verify() ;
	       if( checked )
	       {
	          info.add( "validity\t: ... signature is valid" ) ;
	       } else {
	          info.add( "validity\t: WARNING - Unable to validate the signature of this AC - DO NOT TRUST!" ) ;
	       }
	       long milliseconds = vomsac.getTime() ;
	        if( milliseconds > 0 )
	        {
	          int hours = new Long(milliseconds/(1000*3600)).intValue();
	          int minutes = new Long((milliseconds - hours*1000*3600)/(1000*60)).intValue();
	          int seconds = new Long( (milliseconds - (hours*1000*3600+minutes*1000*60))/1000 ).intValue();
	          info.add( "time left\t: " + hours  +":" + minutes + ":" + seconds ) ;
	        } else {
	          info.add( "WARNING - this AC is not within its valid time - DO NOT TRUST!" ) ;
	        }
	        info.add( "holder\t: " + vomsac.getHolder() ) ;
	        info.add( "version\t: " + vomsac.getVersion() ) ;
	        info.add( "algorithm\t: " + vomsac.getAlgorithmIdentifier() ) ;
	        info.add( "serialNumber\t: " + vomsac.getSerialNumberIntValue() ) ;
	        for ( String line : vomsac.getVOMSFQANs() ){
	        	info.add( "attribute\t: "+line);
	        }
	        //info.addAll(vomsac.getVOMSFQANs());
		 
		} catch (Exception e){
			//e.printStackTrace();
			myLogger.error(e);
			return null;
		}
		return info;
	}
}
