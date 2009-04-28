package org.vpac.common.model.gridproxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.globus.gsi.GlobusCredential;
import org.vpac.common.exceptions.MissingPrerequisitesException;

public class PlainProxy extends GridProxy {

	public PlainProxy(File proxyFile, GlobusCredential cred) throws IOException {
		super(proxyFile, cred);
	}
	
	@Override
	protected void checkPrerequisites() throws MissingPrerequisitesException {
		return;
	}

	@Override
	protected GlobusCredential createProxy(char[] passphrase,
			long lifetime_in_ms) throws Exception {
		// TODO Auto-generated method stub
		return this.globusCredential;
	}

	@Override
	protected ArrayList<String> proxyInfo() {
		//TODO ?
		return new ArrayList<String>();
	}

}
