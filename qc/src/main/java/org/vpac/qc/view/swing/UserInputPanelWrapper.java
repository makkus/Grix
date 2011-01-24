/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
 * qc is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.

 * qc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with qc; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.vpac.qc.view.swing;

import org.vpac.qc.model.query.UserInput;
import org.vpac.qc.model.query.UserInputQuery;

/**
 * This class extends the {@link UserInput} abstract class to be able to connect
 * to a {@link UserInputQuery}.
 * <p>
 * Since {@link UserInputPanel} already extends JPanel, this is just a wrapper
 * to be able to extend the {@link UserInput} abstract class.
 * 
 * @author Markus Binsteiner
 * 
 */
public class UserInputPanelWrapper extends UserInput {

	private UserInputPanel panel = null;

	private boolean clipboardSupport = false;

	public UserInputPanelWrapper(boolean clipboardSupport) {
		this.clipboardSupport = clipboardSupport;
	}

	protected void init() {
		panel = new UserInputPanel(this.arguments, clipboardSupport);
	}

	protected Object[] getValues() {
		return panel.getValues();
	}

	public UserInputPanel getPanel() {
		return panel;
	}

	protected void setPanel(UserInputPanel panel) {
		this.panel = panel;
	}

}
