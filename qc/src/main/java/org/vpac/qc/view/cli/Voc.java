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

package org.vpac.qc.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.vpac.common.view.cli.Menu;
import org.vpac.qc.model.query.QueryArgument;
import org.vpac.qc.model.query.UserInput;
import org.vpac.qc.model.query.UserInputQuery;

/**
 * This class is a very simple implementation of the {@link UserInput} abstract class. It asks the user on 
 * the commandline about {@link QueryArgument}s that are needed to submit a {@link UserInputQuery}.
 * 
 * @author Markus Binsteiner
 *
 */
public class Voc extends UserInput {
	
	protected void init(){
		
	}
	
	public Object[] getValues() {
		
		for ( int i=0; i<arguments.size(); i++ ) {
			
			if ( arguments.get(i).getPreselection() == null ){
				
				// userinput
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Please provide a value for "+arguments.get(i).getPrettyName()+":");
				while (true) {
					try {
						String c = br.readLine();
						values[i] = c;
						break;
					} catch (IOException e) {
						System.err
								.println("Can't read your input. Please try again.");
					}
				}				
			} else  {
				
				// preselection
				
				int choice = Integer.MIN_VALUE;
				// TODO check for non-arrays
				String[] menuItems = arguments.get(i).getPreselectionAsString();

				choice = Menu.menu(menuItems);
				
				if ( choice == -1 ) return null;

				values[i] = arguments.get(i).getPreselection().get(choice);
			} 
		}
				
		
		return values;
	}
	

//	public Object[] getUserInput(QueryArgument[] arguments) {
//		
//		Object[] values = new Object[arguments.length];
//		
//		for ( int i=0; i<arguments.length; i++ ) {
//			
//			if ( ArgumentType.USERINPUT.equals(arguments[i].getType().getAttributeValue("name")) ){
//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				System.out.println("Please provide a value for "+arguments[i].getPrettyName()+":");
//				while (true) {
//					try {
//						String c = br.readLine();
//						values[i] = c;
//						break;
//					} catch (IOException e) {
//						System.err
//								.println("Can't read your input. Please try again.");
//					}
//				}				
//			} else if ( ArgumentType.PRESELECTION.equals(arguments[i].getType().getAttributeValue("name")) ) {
//				int choice = Integer.MIN_VALUE;
//				// TODO check for non-arrays
//				String[] menuItems = new String[((Object[])arguments[i].getValue()).length];
//				for ( int j=0; j<menuItems.length; j++){
//					menuItems[j] = ((Object[])arguments[i].getValue())[j].toString();
//				}
//
//				choice = Menu.menu(menuItems);
//				
//				if ( choice == -1 ) return null;
//
//				values[i] = ((Object[])arguments[i].getValue())[choice];
//			} else if ( ArgumentType.DEFAULT.equals(arguments[i].getType().getAttributeValue("name")) ){
//				// just copy the default value
//				try {
//					values[i] = ((Object[])arguments[i].getValue());
//				} catch (ClassCastException e) {
//					values[i] = ((Object)arguments[i].getValue());
//				}
//			} else {
//				// userinput
//				values[i] = null;
//			}
//		}
//		
//		
//		return values;
//	}

	
	
}
