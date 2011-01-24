package org.vpac.common.view.swing.messagePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class PanelTester extends JDialog {

	private static final long serialVersionUID = 1L;

	static ResourceBundle messages = ResourceBundle.getBundle(
			"SwingViewMessagesBundle", java.util.Locale.getDefault()); // @jve:decl-index=0:

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	// private XHTMLMessagePanel messagePanel = null;

	private SimpleMessagePanel messagePanel = null;

	// private CertificateInfoPanel certificateInfoPanel = null;
	//
	// private CertificateStatusPanel certificateStatusPanel = null;

	private InfoPagePanel certificateRequestedPanel = null;

	private String uri = null;

	/**
	 * @param owner
	 */
	public PanelTester(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			// jPanel.add(getCertificateRequestedPanel(), BorderLayout.CENTER);
			// jPanel.add(getCertificateStatusPanel(), BorderLayout.CENTER);
			// getCertificateStatusPanel().statusChanged(new
			// CertificateEvent(this,CertificatePanel.READY_ON_CA_SERVER));
			jPanel.add((JPanel) getMessagePanel(), BorderLayout.CENTER);
			getMessagePanel().setDocument("test");
			// jPanel.add(getCertificateInfoPanel(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	//
	// private CertificateStatusPanel getCertificateStatusPanel(){
	// if ( certificateStatusPanel == null ){
	// certificateStatusPanel = new CertificateStatusPanel();
	// }
	// return certificateStatusPanel;
	// }

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private MessagePanel getMessagePanel() {
		if (messagePanel == null) {
			messagePanel = new SimpleMessagePanel();
			// uri =
			// this.getClass().getClassLoader().getResource("about.xhtml").getFile();

			// DocumentBuilderFactory factory =
			// DocumentBuilderFactory.newInstance();
			// DocumentBuilder builder = factory.newDocumentBuilder();
			// Document document = builder.parse( new File(uri) );
			// URL url = this.getClass().getClassLoader().getResource(".");
			// messagePanel.setDocument(document, url.toString());
			// messagePanel.setDocument("<html><h1>Hallo</h1><p>asdfasd asdf sfsd fasdfa sdfasd f sadfs dfsd fsdaf sadf sdfa sdf sdf asdf asdf asdf asdf asdf asdf asdf asdf asdf</p></html>");

		}
		return messagePanel;
	}

	private InfoPagePanel getCertificateRequestedPanel() {
		if (certificateRequestedPanel == null) {
			certificateRequestedPanel = new InfoPagePanel("vomrs_register",
					Color.white);

		}
		return certificateRequestedPanel;
	}

	// private CertificateInfoPanel getCertificateInfoPanel() {
	// if (certificateInfoPanel == null) {
	// try {
	// certificateInfoPanel = new CertificateInfoPanel(new Certificate(new
	// File("/home/markus/.globus/usercert.pem")));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (GeneralSecurityException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return certificateInfoPanel;
	// }

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {

		PanelTester md = new PanelTester(null);
		md.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
