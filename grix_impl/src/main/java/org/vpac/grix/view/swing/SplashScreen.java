package org.vpac.grix.view.swing;

import javax.swing.ImageIcon;
import javax.swing.JWindow;
import java.awt.BorderLayout;
import javax.swing.JLabel;

public class SplashScreen extends JWindow {
	
	public SplashScreen() {
		
		ImageIcon ii = new ImageIcon(SplashScreen.class.getResource("/grix-splash.png"));
		setSize(ii.getIconWidth(), ii.getIconHeight());
		setLocationRelativeTo(null); 
		JLabel label = new JLabel(ii);
		getContentPane().add(label, BorderLayout.CENTER);
	}

	
	public static void main (String[] args) {
		
		SplashScreen splash = new SplashScreen();
		splash.setVisible(true);
		
	}
}
