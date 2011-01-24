/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
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

package org.vpac.qc.model.clients;

public class ClientNotInitializedException extends Exception {

	/**
	 * Thrown if a client can't be initialized for whatever reason.
	 */
	private static final long serialVersionUID = 1L;
	private Exception exception = null;
	private String message = null;

	public ClientNotInitializedException(String message) {
		super(message);
		this.message = message;
	}

	public ClientNotInitializedException(Exception e) {
		super(e.getMessage());
		this.message = e.getMessage();
		this.exception = e;
	}

	public ClientNotInitializedException(String message, Exception e) {
		super(message);
		this.exception = e;
		this.message = message;
	}

	public Exception getException() {
		return exception;
	}

}
