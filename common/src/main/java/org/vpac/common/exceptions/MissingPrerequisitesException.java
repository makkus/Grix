package org.vpac.common.exceptions;

import java.io.File;
import java.util.ArrayList;


/**
 * This Exception class holds missing prerequisites in an ArrayList<File>. It is thrown when 
 * the certificate and/or the private key are not found.
 * 
 * @author Markus Binsteiner
 *
 */
public class MissingPrerequisitesException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<File> missingFiles = null;
	
	public MissingPrerequisitesException(ArrayList<File> missingFiles){
		super();
		this.missingFiles = missingFiles;
	}

	public ArrayList<File> getMissingFiles() {
		return missingFiles;
	}

}
