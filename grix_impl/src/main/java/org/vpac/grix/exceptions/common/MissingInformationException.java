package org.vpac.grix.exceptions.common;

import java.util.ArrayList;

public class MissingInformationException extends Exception {

	private ArrayList<String> missingInformation = null;

	public MissingInformationException(String message,
			ArrayList<String> missingInformation) {
		super(message);
		this.missingInformation = missingInformation;
	}

	public MissingInformationException(ArrayList<String> missingInformation) {
		super();
		this.missingInformation = missingInformation;
	}

	public ArrayList<String> getMissingInformation() {
		return missingInformation;
	}

}
