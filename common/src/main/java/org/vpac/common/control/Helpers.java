/* Copyright 2006 VPAC
 * 
 * This file is part of common.
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

package org.vpac.common.control;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Helpers {

	static final Logger myLogger = Logger.getLogger(Helpers.class.getName());

	public static String getClipboard() {
		// get the system clipboard
		Clipboard systemClipboard = Toolkit.getDefaultToolkit()
				.getSystemClipboard();
		// get the contents on the clipboard in a
		// transferable object
		Transferable clipboardContents = systemClipboard.getContents(null);
		// check if clipboard is empty
		if (clipboardContents == null) {
			return ("Clipboard is empty!!!");
		} else
			try {
				// see if DataFlavor of
				// DataFlavor.stringFlavor is supported
				if (clipboardContents
						.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					// return text content
					String returnText = (String) clipboardContents
							.getTransferData(DataFlavor.stringFlavor);
					return returnText;
				}
			} catch (UnsupportedFlavorException ufe) {
				// ufe.printStackTrace();
				myLogger.error(ufe);

			} catch (IOException ioe) {
				myLogger.error(ioe);
				// ioe.printStackTrace();
			}
		return null;
	}

	public static void setClipboard(String text) {
		Clipboard systemClipboard = Toolkit.getDefaultToolkit()
				.getSystemClipboard();
		StringSelection fieldContent = new StringSelection(text);
		systemClipboard.setContents(fieldContent, fieldContent);
	}

}
