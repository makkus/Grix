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

package org.vpac.qc.view.swing;

import org.vpac.qc.model.query.Query;
import org.vpac.qc.model.query.UserInputQuery;

/**
 * To be able to hold a {@link OneQueryPanel} a JPanel has to implement this
 * interface.
 * 
 * @author Markus Binsteiner
 * 
 */
public interface OneQueryPanelParent {

	/**
	 * Implement what should happen if the user clicks the Cancel button.
	 */
	public void cancel();

	/**
	 * This one is executed by the {@link OneQueryPanel} to indicate that the
	 * {@link Query} was submitted.
	 * 
	 * @param success
	 *            whether the query submission was a success (true) or not
	 *            (false)
	 */
	public void submitted(boolean success);

	/**
	 * Since the {@link OneQueryPanel} does not hold a {@link Query} itself,
	 * this method connects to the one the parent is holding/has got a reference
	 * to.
	 * 
	 * @return the {@link Query} the user wants to submit
	 */
	public UserInputQuery getQuery();

	/**
	 * Asks the user for confirmation whether he really wants to submit the
	 * {@link Query}.
	 * <p>
	 * This one does not have to be implemented. A return true is enough.
	 * 
	 * @return whether the user confirms the query submission (true) or not
	 *         (false).
	 */
	public boolean tellUser();

}
