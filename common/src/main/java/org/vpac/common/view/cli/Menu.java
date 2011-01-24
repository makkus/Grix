package org.vpac.common.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
	/**
	 * Generates a menu with all the elements of the provided String array.
	 * 
	 * @param module
	 *            An array with all the options of the menu
	 * @return the index of the choosen menu item or -1 for exit
	 */
	public static int menu(String[] module) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please choose the module you want to use:");
		System.out.println("-----------------------------------------\n");
		for (int i = 0; i < module.length; i++)
			System.out.println((i + 1) + ". " + module[i]);

		System.out.println("\n0. Exit");

		while (true) {
			try {
				int result = Integer.parseInt(br.readLine());
				if (result < 0 || result > module.length)
					System.out.println("Please provide a number between 0 and "
							+ module.length + ".");
				else
					return result - 1;
			} catch (NumberFormatException e) {
				System.out
						.println("Can't parse your input. Please provide a number.");
			} catch (IOException e) {
				System.err.println("Can't read input. Please try again.");
			}
		}
	}
}
