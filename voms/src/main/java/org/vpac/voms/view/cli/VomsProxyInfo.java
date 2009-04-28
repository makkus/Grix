/* Copyright 2006 VPAC
 * 
 * This file is part of voms.
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

package org.vpac.voms.view.cli;

import java.io.File;

import org.vpac.common.model.gridproxy.LocalProxy;
import org.vpac.voms.model.proxy.NoVomsProxyException;
import org.vpac.voms.model.proxy.VomsProxy;

/**
 * Quick hack.
 * 
 * @author Markus Binsteiner
 *
 */
public class VomsProxyInfo {

	public static void main(String[] args) {
		
		File proxyFile = LocalProxy.getProxyFile();
		VomsProxy proxy = null;

		try {
			proxy = new VomsProxy(proxyFile);
		} catch (NoVomsProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for ( String info : proxy.info() ){
			System.out.println(info);
		}
		
	}

}
