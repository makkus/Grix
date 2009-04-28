package org.vpac.grix.view.swing.proxy;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.globus.gsi.GlobusCredential;
import org.vpac.common.model.gridproxy.LocalProxy;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ProxyInfoPanel extends JPanel {

	private JTextField strengthTextField;
	private JTextField certNumTextField;
	private JTextField identityTextField;
	private JTextField issuerTextField;
	private JLabel strengthLabel;
	private JLabel certificateNumberLabel;
	private JLabel identityLabel;
	private JLabel issuerLabel;
	private JTextField subjectTextField;
	private JLabel subjectLabel;
	private JTextField remainingTextField;
	private JLabel timeRemainingLabel;
	private GlobusCredential cred = null;
	
	
	private Action updateTimeAction = null;  
	private Timer updatePanel = null;
	
	/**
	 * Create the panel
	 */
	public ProxyInfoPanel(GlobusCredential cred) {
		super();

		this.cred = cred;
		initialize();
		fillProxyInformation();
	}

	public ProxyInfoPanel() {
		super();
		initialize();
	}

	public void setCredential(GlobusCredential cred) {
		this.cred = cred;
		fillProxyInformation();
	}

	private void initialize() {
		
		updateTime();
		
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec("74dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				new ColumnSpec("default:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				new RowSpec("top:12dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC}));
		//
		add(getTimeRemainingLabel(), new CellConstraints(2, 12));
		add(getRemainingTextField(), new CellConstraints(4, 12));
		add(getSubjectLabel(), new CellConstraints(2, 2));
		add(getSubjectTextField(), new CellConstraints(4, 2,
				CellConstraints.FILL, CellConstraints.DEFAULT));
		add(getIssuerLabel(), new CellConstraints(2, 4));
		add(getIdentityLabel(), new CellConstraints(2, 6));
		add(getCertificateNumberLabel(), new CellConstraints(2, 8));
		add(getStrengthLabel(), new CellConstraints(2, 10));
		add(getIssuerTextField(), new CellConstraints(4, 4));
		add(getIdentityTextField(), new CellConstraints(4, 6));
		add(getCertNumTextField(), new CellConstraints(4, 8));
		add(getStrengthTextField(), new CellConstraints(4, 10));
	}
	
	private void updateTime() {
		
		updateTimeAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				try {
					String timeLeft = LocalProxy.getDefaultProxy().getFormatedTimeWithoutSeconds();
					
					if ( timeLeft == null || "".equals(timeLeft) ) {
						updatePanel.stop();
					}
					getRemainingTextField().setText(timeLeft);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		updatePanel = new Timer(1000, updateTimeAction);
		
	}

	private void fillProxyInformation() {

		if (cred == null) {
			updatePanel.stop();
			getSubjectTextField().setHorizontalAlignment(JTextField.CENTER);
			getSubjectTextField().setText("n/a");
			getIssuerTextField().setHorizontalAlignment(JTextField.CENTER);
			getIssuerTextField().setText("n/a");
			getIdentityTextField().setHorizontalAlignment(JTextField.CENTER);
			getIdentityTextField().setText("n/a");
			getCertNumTextField().setHorizontalAlignment(JTextField.CENTER);
			getCertNumTextField().setText("n/a");
			getStrengthTextField().setHorizontalAlignment(JTextField.CENTER);
			getStrengthTextField().setText("n/a");
			getRemainingTextField().setText("n/a");
			
		} else {
			updatePanel.start();
			String subject = cred.getSubject();
			String issuer = cred.getIssuer();
			String identity = cred.getIdentity();
			int certNum = cred.getCertNum();
			int strength = cred.getStrength();
			long remaining = cred.getTimeLeft();

			getSubjectTextField().setHorizontalAlignment(JTextField.LEADING);
			getSubjectTextField().setText(subject);
			getIssuerTextField().setHorizontalAlignment(JTextField.LEADING);
			getIssuerTextField().setText(issuer);
			getIdentityTextField().setHorizontalAlignment(JTextField.LEADING);
			getIdentityTextField().setText(identity);
			getCertNumTextField().setHorizontalAlignment(JTextField.LEADING);
			getCertNumTextField().setText(new Integer(certNum).toString());
			getStrengthTextField().setHorizontalAlignment(JTextField.LEADING);
			getStrengthTextField().setText(
					new Integer(strength).toString() + " bits");
//			getRemainingTextField().setText(new Long(remaining).toString());
		}
	}

	/**
	 * @return
	 */
	/**
	 * @return
	 */
	protected JLabel getTimeRemainingLabel() {
		if (timeRemainingLabel == null) {
			timeRemainingLabel = new JLabel();
			timeRemainingLabel.setText("Time remaining:");
		}
		return timeRemainingLabel;
	}

	/**
	 * @return
	 */
	protected JTextField getRemainingTextField() {
		if (remainingTextField == null) {
			remainingTextField = new JTextField();
			remainingTextField.setHorizontalAlignment(SwingConstants.CENTER);
			remainingTextField.setEditable(false);
		}
		return remainingTextField;
	}

	/**
	 * @return
	 */
	protected JLabel getSubjectLabel() {
		if (subjectLabel == null) {
			subjectLabel = new JLabel();
			subjectLabel.setText("Subject:");
		}
		return subjectLabel;
	}

	/**
	 * @return
	 */
	protected JTextField getSubjectTextField() {
		if (subjectTextField == null) {
			subjectTextField = new JTextField();
			subjectTextField.setEditable(false);
		}
		return subjectTextField;
	}

	/**
	 * @return
	 */
	protected JLabel getIssuerLabel() {
		if (issuerLabel == null) {
			issuerLabel = new JLabel();
			issuerLabel.setText("Issuer:");
		}
		return issuerLabel;
	}

	/**
	 * @return
	 */
	protected JLabel getIdentityLabel() {
		if (identityLabel == null) {
			identityLabel = new JLabel();
			identityLabel.setText("Identity:");
		}
		return identityLabel;
	}

	/**
	 * @return
	 */
	protected JLabel getCertificateNumberLabel() {
		if (certificateNumberLabel == null) {
			certificateNumberLabel = new JLabel();
			certificateNumberLabel.setText("Certificate number:");
		}
		return certificateNumberLabel;
	}

	/**
	 * @return
	 */
	protected JLabel getStrengthLabel() {
		if (strengthLabel == null) {
			strengthLabel = new JLabel();
			strengthLabel.setText("Strength:");
		}
		return strengthLabel;
	}

	/**
	 * @return
	 */
	protected JTextField getIssuerTextField() {
		if (issuerTextField == null) {
			issuerTextField = new JTextField();
			issuerTextField.setEditable(false);
		}
		return issuerTextField;
	}

	/**
	 * @return
	 */
	protected JTextField getIdentityTextField() {
		if (identityTextField == null) {
			identityTextField = new JTextField();
			identityTextField.setEditable(false);
		}
		return identityTextField;
	}

	/**
	 * @return
	 */
	protected JTextField getCertNumTextField() {
		if (certNumTextField == null) {
			certNumTextField = new JTextField();
			certNumTextField.setHorizontalAlignment(SwingConstants.CENTER);
			certNumTextField.setEditable(false);
		}
		return certNumTextField;
	}

	/**
	 * @return
	 */
	protected JTextField getStrengthTextField() {
		if (strengthTextField == null) {
			strengthTextField = new JTextField();
			strengthTextField.setHorizontalAlignment(SwingConstants.CENTER);
			strengthTextField.setEditable(false);
		}
		return strengthTextField;
	}

}
