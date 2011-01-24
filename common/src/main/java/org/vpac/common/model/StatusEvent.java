package org.vpac.common.model;

import java.util.EventObject;

/**
 * A status event is an event that can happen to the internals of an object.
 * 
 * @author Markus Binsteiner
 * 
 */
public class StatusEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private int status = -1;
	private String[] message = null;

	public StatusEvent(Object source, int status) {
		super(source);
		this.status = status;
	}

	public StatusEvent(Object source, int status, String[] message) {
		super(source);
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String[] getMessage() {
		return message;
	}

}
