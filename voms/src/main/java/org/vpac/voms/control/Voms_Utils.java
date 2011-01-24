/* Most of this class is a written by
 *
 * Gidon Moont from
 * Imperial College London
 *
 * I just rearranged/moved methods so that they better fit into the Grix architecture.
 * Again: all the credit goes to Gidon.
 */

package org.vpac.voms.control;

import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERGeneralString;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.vpac.voms.model.proxy.VomsProxy;

/**
 * Helper methods
 * 
 * @author Markus Binsteiner
 * 
 */
public class Voms_Utils {

	static final Logger myLogger = Logger.getLogger(Voms_Utils.class.getName());

	public static String getDefaultFQAN(ArrayList<String> fqans) {

		for (String fqan : fqans) {
			if (fqan.indexOf("Role=NULL") == -1)
				return fqan;
		}

		return null;
	}

	public static String getRole(String fqan) {
		int start = fqan.indexOf("Role=") + 5;
		int end = fqan.indexOf("/Capability=");
		return fqan.substring(start, end);
	}

	public static String getGroup(String fqan) {
		int end = fqan.indexOf("/Role=");
		return fqan.substring(0, end);
	}

	/**
	 * Extracts the FQANs from an AttributeCertificate
	 * 
	 * @param ac
	 *            the AttributeCertificate
	 * @return all FQANs in this AttributeCertificate
	 */
	public static ArrayList<String> getFQANs(AttributeCertificate ac) {

		ArrayList<String> theseFQANs = new ArrayList<String>();

		try {

			// could have more than one AC in here...
			for (Enumeration a = ac.getAcinfo().getAttributes().getObjects(); a
					.hasMoreElements();) {

				ASN1Sequence sequence = (ASN1Sequence) a.nextElement();
				// sequence contains the OID [voms 4] (as a DERObjectIdentifier)
				// at address 0 , and an SET at address 1

				ASN1Set set = (ASN1Set) sequence.getObjectAt(1);
				// set contains only a SEQUENCE at address 0

				ASN1Sequence sequence2 = (ASN1Sequence) set.getObjectAt(0);
				// sequence2 contains a TAGGED OBJECT ad address 0 and another
				// SEQUENCE at address 1

				ASN1TaggedObject taggedObject = (ASN1TaggedObject) sequence2
						.getObjectAt(0);
				// dig down the tagged object... (undocumented?) - TagNumber
				// value is 0

				ASN1TaggedObject taggedObject2 = (ASN1TaggedObject) taggedObject
						.getObject();
				// this tagged object has TagNumber value of 6 (?)
				ASN1OctetString originOctetString = (ASN1OctetString) taggedObject2
						.getObject();
				String origin = (new DERGeneralString(
						originOctetString.getOctets())).getString();

				ASN1Sequence fqanSequence = (ASN1Sequence) sequence2
						.getObjectAt(1);
				// this is the actual sequence of FQANs

				for (int fqan = 0; fqan < fqanSequence.size(); fqan++) {
					ASN1OctetString fqanOctetString = (ASN1OctetString) fqanSequence
							.getObjectAt(fqan);
					String FQAN_Value = (new DERGeneralString(
							fqanOctetString.getOctets())).getString();
					theseFQANs.add(FQAN_Value);
				}

			}

		} catch (Exception e) {
			// e.printStackTrace();
			myLogger.error(e);
		}

		for (String fqan : theseFQANs) {
			myLogger.debug("FQAN: " + fqan);
			;
		}

		return theseFQANs;

	}

}
