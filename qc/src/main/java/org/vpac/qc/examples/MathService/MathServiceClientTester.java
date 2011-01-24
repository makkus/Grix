/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
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

package org.vpac.qc.examples.MathService;

import java.io.File;

import org.vpac.qc.model.clients.GenericClient;
import org.vpac.qc.model.clients.MathServiceClient;
import org.vpac.qc.model.query.UserInputQuery;
import org.vpac.qc.view.cli.Voc;

public class MathServiceClientTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		GenericClient client = new MathServiceClient(
				new File(
						"/home/markus/workspace/qc/src/org/vpac/qc/examples/MathService/queries.xml"));

		UserInputQuery currentQuery = client.createUserInputQuery("substract");

		currentQuery.init();

		Voc voc = new Voc();
		voc.connect(currentQuery);

		currentQuery.fillUserInput();

		currentQuery.submit();

		Object[] result = currentQuery.getResult();
		for (Object part : result) {
			System.out.println("Result: " + (String) part);
		}

	}

}
