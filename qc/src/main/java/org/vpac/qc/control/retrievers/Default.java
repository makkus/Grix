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

package org.vpac.qc.control.retrievers;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.vpac.qc.model.query.QueryArgument;

/**
 * This class is able to create objects that have constructors with either no
 * argument or arguments that have a constructor that takes one String, e.g.
 * new Integer(String) or new String(string)...
 * 
 * <p>The xml code for this plugin looks like:</p>
 * <pre>{@code
 * <type name="default">
 *        <method name="Default">
 *               <method_arg object="java.lang.String">full</method_arg>
 *        </method>
 * </type>}
 * </pre>
 * 
 * @author Markus Binsteiner
 *
 */
public class Default extends QcRetriever {
	
	static final Logger myLogger = Logger.getLogger(Default.class
			.getName());	
	
	List properties = null;
	
	public Default(QueryArgument arg){
		super(arg);
		
		properties = method_element.getChildren("method_arg");

	}
	
	public Object[] retrieveValue() throws Exception{
		
		if ( properties.size() == 0 ) return null;
		else if ( properties.size() == 1 ) {
			String object = ((Element)properties.get(0)).getAttributeValue("object");
			String value = ((Element)properties.get(0)).getValue();
			Constructor constructor = Class.forName(object).getConstructor(new Class[]{String.class});
			return new Object[]{constructor.newInstance(new Object[]{value})};
		} else {
			//return array
			Object[] result = new Object[properties.size()];
			for ( int i=0; i<properties.size(); i++) {
				String object = ((Element)properties.get(i)).getAttributeValue("object");
				String value = ((Element)properties.get(i)).getValue();
				Constructor constructor = Class.forName(object).getConstructor(new Class[]{String.class});
				result[i] = constructor.newInstance(new Object[]{value});
			}
			return result;
		}
		
	}
}
