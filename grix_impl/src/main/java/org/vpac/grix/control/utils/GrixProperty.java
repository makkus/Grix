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

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Helper class to read the file "config.properties" which is located at the
 * root of the package
 * 
 * @author Markus Binsteiner
 * 
 */
public class GrixProperty {

	// try to provide some defaults in case properties file could not be loaded
	// don't know, who reasonable that really is
	private static Hashtable<String, String> defaults;
	static {
		defaults = new Hashtable<String, String>();
		defaults.put("help.email.address", "markus@vpac.org");

		defaults.put("signature.algorithm", "MD5WithRSA");
		defaults.put("default.keysize", "1024");
		defaults.put("GLOBUS_DIR", ".globus");
		defaults.put("PRIVATE_KEY_FILENAME", "userkey.pem");
		defaults.put("CERTIFICATE_FILENAME", "usercert.pem");
		defaults.put("CERTIFICATION_REQUEST_FILENAME", "usercert_request.pem");
		defaults.put("backup.file.extension", ".bak");
	}

	static final Logger myLogger = Logger.getLogger(GrixProperty.class
			.getName());

	static final String PROPERTY_FILE = "config.properties";

	private static boolean initialized = false;

	static Properties res_properties = new Properties();

	/**
	 * Consturctor to load at application start. Loads all the properties in a
	 * static GrixProperty.properties object.
	 * 
	 * @throws IOException
	 */
	public static void initProperties() {

		myLogger.debug("Loading properties...");

		try {
			ClassLoader cl = GrixProperty.class.getClassLoader();
			InputStream is = cl.getResourceAsStream(PROPERTY_FILE);
			res_properties.load(is);
			is.close();
			GrixProperty.initialized = true;
		} catch (IOException ioe) {
			myLogger.error("Properties file could not be loaded. Please check your installation or contact the person who packaged this application ("
					+ GrixProperty.defaults.get("help.email.address") + ").");
		}
	}

	/**
	 * Returns the string property specified by a key.
	 * 
	 * @param key
	 *            - The key to the value you want to know.
	 * @return Returns the property associated with the key
	 */
	public static String getString(String key) {

		// failsave, always try to initialize the GrixProperty when your
		// application starts!
		if (!GrixProperty.initialized)
			GrixProperty.initProperties();

		String result = res_properties.getProperty(key);
		if (result == null) {
			myLogger.error("Could not find property: " + key
					+ " in properties file. Trying default value.");
			result = GrixProperty.defaults.get(key);
			if (result == null) {
				myLogger.error("could not find property: "
						+ key
						+ " in defaults. Check your installation or contact the person who packaged this application ("
						+ GrixProperty.defaults.get("help.email.address")
						+ ").");
			}
		}
		return result;

	}

	/**
	 * 
	 * @param key
	 *            - The key to the value you want to know.
	 * @return Returns the integer property associated with the key. If the
	 *         property is not available Integer.MIN_VALUE is returned.
	 */
	public static int getInt(String key) {

		String string_result = getString(key);
		int result = Integer.MIN_VALUE;
		try {
			result = Integer.parseInt(string_result);
		} catch (NumberFormatException nfe) {
			myLogger.error("Could not parse number specified by key: "
					+ key
					+ ". Check your installation or contact the person who packaged this application ("
					+ GrixProperty.defaults.get("help.email.address") + ").");
		}
		return result;
	}

}
