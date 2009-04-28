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

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A MessagePanel helps displaying formatted text (like HTML, XHTML) on a JPanel. It provides several ways to set the document.
 * I invented this abstract class because at first I used flyingsaucer to display help text within Grix. Turned out
 * that was way to slow and a normal JEditorPane is much better for that. Hence SimpleMessagePanel from now on.
 * 
 * @author Markus Binsteiner
 *
 */
public abstract class MessagePanel extends JPanel{
	
	static final Logger myLogger = Logger
	.getLogger(MessagePanel.class.getName());	
	
	private static ResourceBundle messages = ResourceBundle.getBundle(
			"SwingViewMessagesBundle", java.util.Locale.getDefault()); 	

	private static DocumentBuilder builder = null;  //  @jve:decl-index=0:

	
	
	abstract public void setDocument(Document doc, String url);
	abstract public void setDocument(String message);
	//abstract public void appendText(String message);
	abstract public void setDocument(String message, boolean include_header_and_footer);
	abstract public void setDocument(File file);
	abstract public void setMargins(Insets insets);
	abstract protected void setDefaultErrorPage();

	public static DocumentBuilder getDocumentBuilder() {
		return builder;
	}
	
	public static Document getXHTMLDocument(String name) throws SAXException, IOException{
		if ( builder == null ) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				myLogger.debug("Could not init DocumentBuilder. Exiting...");
				System.exit(1);
			}			
		}
		String uri = MessagePanel.class.getResource("/org/vpac/grix/xhtml/"+name+".xhtml").getFile();
		Document doc = null;
		doc = builder.parse( new File(uri) );
		return doc;
	}
	
	public static URL getHTMLDocument(String name) throws SAXException, IOException{

		return MessagePanel.class.getResource("/org/vpac/grix/html/"+name+".html");

	}
	
	public static String getHTML(String name) throws IOException {
	
		InputStream in = MessagePanel.class.getResourceAsStream("/org/vpac/grix/html/"+name+".html");
		if ( in == null ) {
			throw new IOException("Cound not find resource: org/vpac/grix/html/"+name+".html");
		}
		BufferedReader d = new BufferedReader(new InputStreamReader(in));

		StringBuffer sb = new StringBuffer();
		try{
		String line = null;
		while((line=d.readLine()) != null){
			sb.append(line+"\n");
		}
		}catch(Exception ex){
			ex.getMessage();
		}finally{
		try{
			in.close();
		}catch(Exception ex){}
		}
		return sb.toString();

		
	}
	
	public static InputStream getHTMLDocumentAsStream(String name) {
	
		return MessagePanel.class.getResourceAsStream(name);
	
	}
	
	 public static String getDocumentXHTMLParent(){
		 return MessagePanel.class.getResource("/org/vpac/grix/xhtml/").toString();
	 }

	public static String getDocumentHTMLParent() {
		return MessagePanel.class.getResource("/org/vpac/grix/html/").toString();
	}	
	
	public static ResourceBundle getMessages() {
		return messages;
	}

}
