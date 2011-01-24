/* Copyright 2006 VPAC
 * 
 * This file is part of grix.
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

package org.vpac.common.view.swing.messagePanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.centerkey.utils.BareBonesBrowserLaunch;

/**
 * The easiest implementation of a MessagePanel. Uses a JEditorPane to display
 * plain HTML. Also has a hyperlink handler.
 * 
 * @author Markus Binsteiner
 * 
 */
public class SimpleMessagePanel extends MessagePanel implements
		HyperlinkListener {

	private static final long serialVersionUID = 1L;

	static final Logger myLogger = Logger.getLogger(SimpleMessagePanel.class
			.getName()); // @jve:decl-index=0:

	private JEditorPane jEditorPane = null;

	private JScrollPane jScrollPane = null;

	// String uri = null;

	/*
	 * (non-Javadoc) Not properly implemented. Don't use it!
	 * 
	 * @see
	 * org.vpac.grix.view.swing.common.MessagePanel#setDocument(org.w3c.dom.
	 * Document, java.lang.String)
	 */
	public void setDocument(Document doc, String uri) {
		// this.uri = uri;
		jEditorPane.setText(doc.getTextContent());
		jEditorPane.setCaretPosition(0);
	}

	// private void setDocument(){
	// if ( uri == null || "".equals(uri) || doc == null ) return;
	// try {
	//
	// jEditorPane.setDocument(doc);
	// myLogger.debug("Document set in simpleMessagePanel.");
	// } catch (Exception e) {
	// setDefaultErrorPage();
	// }
	// }

	protected void setDefaultErrorPage() {
		// TODO
		myLogger.debug("Something went wrong. Page not found.");
		revalidate();

	}

	public void setDocument(String message, boolean include_header_and_footer) {
		if (include_header_and_footer) {
			jEditorPane.setText(MessagePanel.getMessages().getString(
					"HTML.header")
					+ message
					+ MessagePanel.getMessages().getString("HTML.footer"));
		} else {
			jEditorPane.setText(message);
		}
		jEditorPane.setCaretPosition(0);

	}

	public void setDocument(String message) {
		setDocument(message, false);
		jEditorPane.setCaretPosition(0);
	}

	public void setDocument(File file) {
		try {
			jEditorPane.setPage(file.toURL());
		} catch (MalformedURLException e) {
			setDefaultErrorPage();
		} catch (IOException e) {
			setDefaultErrorPage();
		}
		jEditorPane.setCaretPosition(0);
	}

	public void setDocument(URL url) {

		try {
			jEditorPane.setPage(url);
		} catch (IOException e) {
			setDefaultErrorPage();
		}
		jEditorPane.setCaretPosition(0);
	}

	private String getHTMLbody(String in) {
		String tmp = in.toLowerCase();
		int i = tmp.indexOf("<body>") + 6;
		int j = tmp.indexOf("</body>");
		return in.substring(i, j);
	}

	/**
	 * This is the default constructor
	 */
	public SimpleMessagePanel(Color background) {
		super();
		this.setBackground(background);
		getJEditorPane().setContentType("text/html");
		initialize();
	}

	public SimpleMessagePanel(String message, Color background) {
		super();
		this.setBackground(background);
		initialize();
		setDocument(message);
	}

	public SimpleMessagePanel(URL url, Color background) {
		super();
		this.setBackground(background);
		initialize();
		setDocument(url);

	}

	public SimpleMessagePanel() {
		super();
		getJEditorPane().setContentType("text/html");
		initialize();
	}

	public SimpleMessagePanel(String message) {
		super();
		initialize();
		setDocument(message);
	}

	public SimpleMessagePanel(URL url) {
		super();
		initialize();
		setDocument(url);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.weighty = 1.0;
		gridBagConstraints3.weightx = 1.0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJScrollPane(), gridBagConstraints3);
	}

	public void setMargins(Insets insets) {
		jEditorPane.setMargin(insets);
	}

	/**
	 * This method initializes jEditorPane
	 * 
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
			jEditorPane.setContentType("text/html");
			jEditorPane.setEditorKit(new HTMLEditorKit());
			jEditorPane.setEditable(false);
			jEditorPane.setDisabledTextColor(Color.black);
			jEditorPane.addHyperlinkListener(this);
		}
		return jEditorPane;
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				myLogger.debug("Clicked url within SimpleMessagePanel.");
				BareBonesBrowserLaunch.openURL(event.getURL().toExternalForm());
			} catch (NullPointerException npe) {
				BareBonesBrowserLaunch.openURL(event.getDescription());
			}
		}
	}

	public void setBackground(Color background) {
		getJEditorPane().setBackground(background);
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJEditorPane());
		}
		return jScrollPane;
	}

}
