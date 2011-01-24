package org.vpac.common.model.gridproxy;

import java.util.EventObject;

/**
 * This one is thrown when the status of a proxy changes. E.g. from
 * NOT_INITIALIZED to INITIALIZED
 * 
 * @author Markus Binsteiner
 * 
 */
public class GridProxyEvent extends EventObject {

	private int status;

	public GridProxyEvent(Object source, int status) {
		super(source);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
