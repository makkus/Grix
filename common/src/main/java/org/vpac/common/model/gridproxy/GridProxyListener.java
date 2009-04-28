package org.vpac.common.model.gridproxy;

import java.util.EventListener;

/**
 * A GridProxyListener reacts to changes of the status of a GridProxy.
 * 
 * @author Markus Binsteiner
 *
 */
public interface GridProxyListener extends EventListener{

		public void gridProxyStatusChanged(GridProxyEvent e);

	}

