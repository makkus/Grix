package org.vpac.grix.exceptions.certificate;

public class UnableToUploadCertificationRequestException extends Exception {

	private String plugin = null;
	private Exception exception = null;

	public UnableToUploadCertificationRequestException(String plugin,
			Exception e) {
		super();
		this.plugin = plugin;
		this.exception = e;
	}

	public Exception getException() {
		return exception;
	}

	public String getPlugin() {
		return plugin;
	}
}
