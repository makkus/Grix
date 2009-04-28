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

import java.util.ArrayList;

import org.vpac.common.view.swing.ElementsPanel;
import org.vpac.qc.model.query.Query;
import org.vpac.qc.model.query.UserInterfaceArgument;

/**
 * A UserInputPanel holds all necessary {@link OneUserInputPanel}s for a {@link Query}
 * 
 * @author Markus Binsteiner
 *
 */
public class UserInputPanel extends ElementsPanel {

	private static final long serialVersionUID = 1L;

	ArrayList<UserInterfaceArgument> arguments = null;
	Object[] values = null;
	
	/**
	 * This is the default constructor
	 */
	public UserInputPanel(ArrayList<UserInterfaceArgument> arguments, boolean clipboardSupport) {
		super();
		this.arguments = arguments;
		values = new Object[arguments.size()];
		initialize();
		for ( UserInterfaceArgument arg : arguments ){
			if ( clipboardSupport ) {
				this.addOneElementPanel(new ClipboardOneUserInputPanel(this, arg));
			} else {
				this.addOneElementPanel(new OneUserInputPanel(this, arg));
			}
		}
	}
		
	public Object[] getValues(){
		Object[] result = new Object[arguments.size()];
		for ( int i=0; i<result.length; i++ ) {
			result[i] = ((OneUserInputPanelInterface)this.getPanels().get(i)).getUserInput();
		}
		return result;
	}

}
