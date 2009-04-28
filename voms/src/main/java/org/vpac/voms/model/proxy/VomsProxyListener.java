package org.vpac.voms.model.proxy;

import java.util.EventListener;

public interface VomsProxyListener extends EventListener{

		public void vomsProxiesChanged(VomsProxyEvent e);

	}

