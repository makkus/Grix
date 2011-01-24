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

package org.vpac.grix.control.utils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Some helper functions.
 * 
 * @author Markus Binsteiner
 * 
 */
public class Utils {

	static final Logger myLogger = Logger.getLogger(Utils.class.getName());

	/**
	 * Tries to split a name string into first name and surname
	 * 
	 * @param name
	 *            the name string
	 * @return a String array with index=0 -> first name & index=1 -> surname
	 */
	public static String[] parseNameField(String name) {
		String[] parsedName = new String[2];

		int index_last_space = name.lastIndexOf(" ");
		if (index_last_space != -1) {
			parsedName[0] = name.substring(0, index_last_space).trim();
			parsedName[1] = name.substring(index_last_space + 1).trim();
		} else {
			parsedName[0] = "";
			parsedName[1] = name.trim();
		}

		return parsedName;
	}

	public static File backupFile(File file) {

		File bakFile = new File(file.toString() + "."
				+ GrixProperty.getString("backup.file.extension"));
		int i = 1;
		while (bakFile.exists()) {
			bakFile = new File(file.toString() + "."
					+ GrixProperty.getString("backup.file.extension") + i);
			i = i + 1;
		}

		return bakFile;
	}

	/**
	 * Returns whether input string is a valid email address.
	 * 
	 * @param email
	 * @return true if valid, false if not
	 */
	public static boolean isValidEmail(String email) {

		int indexOfAtChar = email.indexOf("@");

		if (indexOfAtChar > 0) {
			int indexOfDotChar = email.indexOf(".", indexOfAtChar);
			if (indexOfDotChar > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// /**
	// * Uses tagsoup library to clean input-(xhtml/html)-stream
	// *
	// * @param htmlpage
	// * the html/xhtml code
	// * @return a String containing a valid xhtml page
	// */
	// public static String getCleanXHTML(BufferedInputStream htmlpage) {
	//
	// XMLReader r = null;
	// Writer w = null;
	// XMLWriter xmlw = null;
	// ByteArrayOutputStream out = null;
	//
	// StringBuffer lines = null;
	//
	// try {
	// // clean up xhtml
	// r = new Parser();
	// out = new ByteArrayOutputStream();
	// w = new OutputStreamWriter( out, "UTF-8" );
	// xmlw = new XMLWriter( w );
	// // xmlw.setOutputProperty(XMLWriter.METHOD, "html");
	// xmlw.setOutputProperty( XMLWriter.OMIT_XML_DECLARATION, "yes" );
	//
	// r.setContentHandler( xmlw );
	//
	// InputSource s = new InputSource();
	// // s.setSystemId(htmlpage.toString());
	// s.setByteStream( htmlpage );
	//
	// r.parse( s );
	// myLogger.debug( "Parsing of xhtml finished." );
	//
	// // TODO make this more clean
	// BufferedReader br = new BufferedReader( new InputStreamReader(
	// new ByteArrayInputStream( out.toByteArray() ) ) );
	// lines = new StringBuffer();
	// String line = null;
	// while ((line = br.readLine()) != null) {
	// lines.append( line );
	// }
	//
	// } catch (Exception e) {
	// // TODO exception and close streams
	// //e.printStackTrace();
	// myLogger.error(e);
	// }
	// return lines.toString();
	// }

	/**
	 * Returns the name of the organization unit(order in config.properties) to
	 * which the search string fits.
	 * 
	 * @param domain
	 *            the search string
	 * @return null if there is no match, the name of the apropriate
	 *         organization unit
	 */
	// public static String defaultOrganizationUnitName(String domain) {
	//
	// String[] org_units = GrixProperty.getString(
	// "default.organisation.units" ).split( "," );
	//
	// for ( String unit : org_units ) {
	// String[] domains = GrixProperty.getString( unit ).split(
	// "," );
	// for ( int i = 0; i < domains.length; i++ ) {
	// if ( domain.equals( domains[i] ) ) return unit;
	// }
	//
	// }
	//
	// return null;
	//
	// }

	/**
	 * Returns the index of the organization unit(order in config.properties) to
	 * which the search string fits.
	 * 
	 * @param domain
	 *            the search string
	 * @return -1 if there is no match, the index of the apropriate organization
	 *         unit
	 */
	public static String defaultOrganizationUnit(String domain) {

		String[] org_units = GrixProperty.getString(
				"default.organisation.units.autofill").split(",");

		for (int i = 0; i < org_units.length; i++) {
			String[] domains = GrixProperty.getString(
					"ou_" + org_units[i].replaceAll(" ", "_")).split(",");
			if (!domains[0].equals("")) {
				for (int j = 0; j < domains.length; j++) {
					if (domain.endsWith(domains[j]))
						return org_units[i];
				}
			}

		}

		return null;

	}

	/**
	 * Checks whether the passphrase is ok with the passphrase policy of the
	 * APACGrid
	 * 
	 * @param passphrase1
	 *            the passphrase
	 * @return true if valid, false if not
	 */
	public static boolean isValidPassphrase(char[] passphrase1) {
		if (passphrase1.length < GrixProperty
				.getInt("minimal.passphrase.lenght"))
			return false;
		else
			return true;
	}

	/**
	 * Helper method, tests whether the two provided char-arrays are equal
	 * 
	 * @param first
	 *            first passphrase
	 * @param second
	 *            second passphrase
	 * @return true if equal, false if not
	 */
	public static boolean samePasswords(char[] first, char[] second) {

		boolean equal = true;

		if (first.length != second.length)
			equal = false;
		for (int i = 0; i < first.length; i++) {
			try {
				if (first[i] != second[i])
					equal = false;
			} catch (RuntimeException e) {
				return false;
			}
		}
		return equal;
	}

}
