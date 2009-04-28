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

package org.vpac.qc.model.query;

import java.util.ArrayList;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.vpac.qc.model.clients.GenericClient;

/**
 * This class is the default implementation of the abstract {@link Query}. A class extending {@link UserInput} is needed to 
 * be able to retrieve user input.
 * <p>
 * For more information read the documentation of {@link Query}.
 * 
 * @author Markus Binsteiner
 *
 */
public class UserInputQuery extends Query {

	private ArrayList<QueryArgument> defaults = null;

	private ArrayList<QueryArgument> preselection = null;

	private ArrayList<QueryArgument> userInput = null;

	private ArrayList<QueryArgument> inputDependant = null;

	private ArrayList<QueryArgument> userNeeded = null;

	private ArrayList<Object> userNeededValues = null;

	private UserInput ui = null;

	public UserInputQuery(String name, GenericClient client, String context) {

		super(name, client, context);

	}
	
	public void init() throws JDOMException, ArgumentsException{
		defaults = new ArrayList<QueryArgument>();
		preselection = new ArrayList<QueryArgument>();
		userInput = new ArrayList<QueryArgument>();
		inputDependant = new ArrayList<QueryArgument>();
		userNeeded = new ArrayList<QueryArgument>();
		userNeededValues = new ArrayList<Object>();
		super.init();
	}

	protected void setArguments(String[] argNames) throws JDOMException,
			ArgumentsException {

		this.arguments = new QueryArgument[argNames.length];

		for (int i = 0; i < arguments.length; i++) {

			this.arguments[i] = QueryArgument
					.argumentFactory(this, argNames[i]);
			String typeString = this.arguments[i].getType().getAttributeValue(
					"name");
			if (ArgumentType.DEFAULT.equals(typeString))
				defaults.add(this.arguments[i]);
			else if (ArgumentType.INPUT_DEPENDANT.equals(typeString)) {
				inputDependant.add(this.arguments[i]);
			} else if (ArgumentType.PRESELECTION.equals(typeString)) {
				preselection.add(this.arguments[i]);
				userNeeded.add(this.arguments[i]);
			} else if (ArgumentType.USERINPUT.equals(typeString)) {
				userInput.add(this.arguments[i]);
				userNeeded.add(this.arguments[i]);
			} else
				throw new JDOMException("Could not get type of argument: "
						+ this.arguments[i].name);

		}
	}

	public void fillPreselections() throws ArgumentsException {

		for (QueryArgument arg : preselection) {

			try {
				arg.retrieveValue();
			} catch (Throwable e) {
				arg.changeType(ArgumentType.USERINPUT);
			}
			if (arg.getValue() == null) {
				arg.changeType(ArgumentType.USERINPUT);
			}

		}

	}

	public void fillUserInputWithDefaults() throws ArgumentsException {

		for (QueryArgument arg : userInput) {

			try {
				arg.retrieveValue();
			} catch (Throwable e) {
				arg.setValue(new Object[] {});
			}
			if (arg.getValue() == null) {
				arg.setValue(new Object[] {});
			}

		}

	}

	public void fillDefaults() throws ArgumentsException {

		for (QueryArgument arg : defaults) {

			try {
				arg.retrieveValue();
			} catch (Throwable e) {
				myLogger.warn("Could not fill argument \"" + arg.getName()
						+ "\"with default value. Cause: " + e.getMessage());
				arg.changeType(ArgumentType.USERINPUT);
			}
			if (arg.getValue() == null) {
				arg.changeType(ArgumentType.USERINPUT);
			}

		}
	}

	// public Element getRootElement() {
	// return rootElement;
	// }

	// public static void main (String[] args){
	//		
	// try {
	//			
	// GenericClient client = new
	// VomrsClient({""},/home/markus/workspace/voc/queries.xml);
	//			
	// UserInputQuery userInputQuery = new UserInputQuery("testQuery", new
	// File("/home/markus/workspace/voc/queries.xml"),
	// new String[]{ "test21", "test10", "test1009", "test50", "representatives"
	// });
	// //new String[]{ "default1", "default2", "testprop", "testprop_list",
	// "testprop_compbined", "test21", "test10", "test1009", "test50" }, new
	// File("/home/markus/workspace/voc/queries.xml"));
	//			
	// userInputQuery.fillDefaults();
	// userInputQuery.fillPreselections();
	//			
	// for ( QueryArgument qarg : userInputQuery.getArguments() ){
	//				
	// System.out.println("Argument: "+qarg.getName());
	// try {
	// for ( Object obj : (Object[])qarg.getValue() ){
	// System.out.println("Object: "+obj+"\tClass: "+obj.getClass().toString());
	// }
	// } catch (ClassCastException e) {
	// System.out.println("Single object: "+qarg.getValue()+"\tClass:
	// "+qarg.getValue().getClass().toString());
	// }
	// System.out.println();
	//				
	// }
	//			
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//		
	// }

	/**
	 * This class returns all the QueryArguments that need user interaction
	 * (userinput and preselection). They are converted into
	 * UserInterfaceArguments. The main difference to QueryArguments is that the
	 * preselection is filtered. The userinterface gives back an array of
	 * Objects in the same order than the userNeeded Array in this
	 * (UserInputQuery) class.
	 * 
	 * @return an array of UserInterfaceArguments
	 */
	public ArrayList<UserInterfaceArgument> getUserNeededArguments() {

		ArrayList<UserInterfaceArgument> result = new ArrayList<UserInterfaceArgument>();
		for (QueryArgument arg : userNeeded) {

			boolean isPreselection = false;

			ArrayList<Object> presel_new = null;
			if (ArgumentType.PRESELECTION.equals(arg.getType()
					.getAttributeValue("name"))) {
				isPreselection = true;
			}

			if (arg.getValue() == null || arg.getValue().length == 0) {
				result.add(new UserInterfaceArgument(arg.getName(), arg
						.getPrettyName(), arg.getDescription(), null, false));

			} else {
				Object[] presel = null;
				try {
					presel = (Object[]) arg.getValue();
				} catch (ClassCastException e) {
					presel = new Object[] { arg.getValue() };
				}
				presel_new = new ArrayList<Object>();
				String filter = arg.getType().getChild("method")
						.getAttributeValue("filter");
				int start = 0;
				int increment = 1;
				if ("even".equals(filter)) {
					start = 0;
					increment = 2;
				} else if ("uneven".equals(filter)) {
					start = 1;
					increment = 2;
				}
				for (int i = start; i < presel.length; i = i + increment) {
					presel_new.add(presel[i]);
				}

				result.add(new UserInterfaceArgument(arg.getName(), arg
						.getPrettyName(), arg.getDescription(), presel_new,
						isPreselection));
			}
		}

		return result;
	}

	public void connect(UserInput ui) {
		this.ui = ui;
	}

	public void fillUserInput() throws ArgumentsException {

		Object[] values = ui.getUserInput();

		if (values.length != userNeeded.size()) {
			throw new ArgumentsException(
					"The lengths of QueryArgument array and values array differ.");
		}

		// fill input dependent values now, because the value field is not
		// overwritten yet
		fillInputDependent(values);
		// TODO check user input
		for (int i = 0; i < userNeeded.size(); i++) {
			userNeeded.get(i).setValue(new Object[] { values[i] });
		}

	}

	private void fillInputDependent(Object[] values) throws ArgumentsException {

		for (int i = 0; i < inputDependant.size(); i++) {

			Element method = inputDependant.get(i).getType().getChild("method");
			String otherArgument = method.getAttributeValue("object");
			int index = -1;
			QueryArgument other = null;
			// find the other argument
			for (index = 0; index < userNeeded.size(); index++) {
				if (otherArgument.equals(userNeeded.get(index).getName()))
					break;
			}
			other = arguments[index];

			String property = method.getAttributeValue("object_property");
			Object new_value = processInputDependentValue(other, values[index],
					property);
			if (new_value == null) {
				throw new ArgumentsException(
						"Could not get user dependant value for QueryArgument: "
								+ inputDependant.get(i).getName());
			}
			inputDependant.get(i).setValue(new Object[] { new_value });
		}

	}

	// this is all a bit dodgy.
	private Object processInputDependentValue(QueryArgument otherArg,
			Object other, String filter) {

		int relative = Integer.MIN_VALUE;
		try {
			relative = new Integer(filter);
		} catch (NumberFormatException e) {
			// TODO implement other filters here
			return null;
		}

		// find the position of the other result. we can assume that the other
		// arg has an object[] as value
		int index = -1;
		Object[] values = ((Object[]) (otherArg.getValue()));
		for (index = 0; index < values.length; index++) {
			if (other.equals(values[index])) {
				break;
			}
		}
		return values[index + relative];
	}

	private void setUserNeededValues(ArrayList<Object> userNeededValues) {
		this.userNeededValues = userNeededValues;
	}

	public void prepare() throws ArgumentsException {

		this.fillDefaults();
		this.fillPreselections();
		this.fillUserInputWithDefaults();

	}



}
