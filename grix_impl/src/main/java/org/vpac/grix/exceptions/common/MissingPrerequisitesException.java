package org.vpac.grix.exceptions.common;

import java.io.File;
import java.util.ArrayList;

/**
 * This Exception class holds missing prerequisites in an ArrayList<File>.
 * 
 * @author Markus Binsteiner
 * 
 */
public class MissingPrerequisitesException extends Exception {

	private static final long serialVersionUID = 1L;
	private ArrayList<File> missingFiles = null;

	public MissingPrerequisitesException(ArrayList<File> missingFiles) {
		super();
		this.missingFiles = missingFiles;
	}

	public ArrayList<File> getMissingFiles() {
		return missingFiles;
	}

}
