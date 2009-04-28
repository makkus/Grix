package org.vpac.grix.control.certificate;

public interface CreateRequestInterface {

	/**
	 * Updates the status panel (if any). This function can be empty.
	 * @param string
	 */
	void setStatus(String string);

	/**
	 * Shows a message to the user.
	 * 
	 * @param string the message
	 */
	void message(String string);

	String getC();

	String getEmail();

	String getO();

	String getOU();

	String getCN();
	
	String getFirstName();
	
	String getLastName();

	char[] getPassphrase();

	char[] getPassphrase2();

	String getPhone();

	void clearPassphrases();

	/**
	 * Locks the User Interface when the creation of the request starts.
	 */
	void lockInput();
	
	/**
	 * Unlocks the User Interface when the creation of the request is finished.
	 */
	void unlockInput();

}
