import java.util.List;

import org.ietf.jgss.GSSCredential;

import au.edu.archer.desktopshibboleth.idp.IDP;
import au.org.mams.slcs.client.SLCSClient;


public class SlcsTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		SLCSClient client = new SLCSClient();
		List<IDP> idps = client.getAvailableIDPs();
		GSSCredential cred = client.slcsLogin(idps.get(2), "staff", "testing");
		
		System.out.println("Lifetime: "+cred.getRemainingLifetime());
		
	}

}
