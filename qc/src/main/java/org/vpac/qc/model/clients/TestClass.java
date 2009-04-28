/* Copyright 2006 VPAC
 * 
 * This file is part of qc.
 * qc is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.

 * qc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with qc; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.vpac.qc.model.clients;

/**
 * A test class to demostrate the {@link org.vpac.qc.control.retrievers.CallClass} retrievers.
 * 
 * @author Markus Binsteiner
 *
 */
public class TestClass {
	
	private int result = 0;
	
	public static int mirror(Integer arg){
		return arg;
	}
	
	public int addToConstructor(Integer intOne){
		return result+intOne;
	}
	
	public TestClass(Integer intOne, Integer intTwo){
		result = intOne+intTwo;
	}
	
	public TestClass(Integer intOne){
		result = intOne;
	}
	
	public TestClass(){
		result = 999;
	}

	public int getResult() {
		return result;
	}

}
