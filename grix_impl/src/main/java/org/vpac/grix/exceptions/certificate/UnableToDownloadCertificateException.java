package org.vpac.grix.exceptions.certificate;

public class UnableToDownloadCertificateException extends Exception {

	private String plugin = null;
	private Exception exception = null;

	public UnableToDownloadCertificateException(String plugin, Exception e) {
		super(e);
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
