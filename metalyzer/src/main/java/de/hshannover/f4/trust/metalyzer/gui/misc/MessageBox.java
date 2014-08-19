/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta metalyzer, version 0.0.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.metalyzer.gui.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */


public class MessageBox {
	/**
	 * Shows a {@link JOptionPane} error dialog
	 * @param title the title of the error dialog
	 * @param msg the message that should be displayed
	 * @return true if exit button is pressed, false otherwise
	 * @see JOptionPane#showOptionDialog(java.awt.Component, Object, String, int, int, javax.swing.Icon, Object[], Object)
	 * */
	public static boolean showErrorDialogRetryExit(String title, String msg) {
		return showErrorDialog(title, msg, new Object[]{"Retry", "Exit"});
	}
	
	/**
	 * Shows a {@link JOptionPane} error dialog
	 * @param title the title of the error dialog
	 * @param msg the message that should be displayed
	 * @return true if exit button is pressed, false otherwise
	 * @see JOptionPane#showOptionDialog(java.awt.Component, Object, String, int, int, javax.swing.Icon, Object[], Object)
	 * */
	public static void showErrorDialog(String title, String msg) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * Shows a {@link JOptionPane} error dialog
	 * @param title the title of the error dialog
	 * @param msg the message that should be displayed
	 * @param option offers the option to customize the dialog buttons
	 * @return true if exit button is pressed, false otherwise
	 * @see JOptionPane#showOptionDialog(java.awt.Component, Object, String, int, int, javax.swing.Icon, Object[], Object)
	 * */
	public static boolean showErrorDialog(String title, String msg, Object[] options) {
		boolean doExit = false;
	    int n = JOptionPane.showOptionDialog(null,
	            msg, title,
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null,
	            options, options[1]);
	    if(n == JOptionPane.OK_OPTION){ 
	    	doExit = false;
	    } else {
	    	doExit = true;
	    }
		return doExit;
	}
	
	/**
	 * Shows a {@link JOptionPane} error dialog with output textarea for exceptions
	 * @param title the title of the error dialog
	 * @param msg that describes the error
	 * @param exception the {@link Exception} as String that will be printed in the output textarea
	 * @see  JOptionPane#showMessageDialog(java.awt.Component, Object, String, int)
	 * */
	public static void showErrorOutputDialog(String title, String msg, String exception) {
		final JPanel errorPanel = new JPanel();
		errorPanel.setLayout(new BorderLayout());
		
		final JTextArea errorMessage = new JTextArea(4, 35);
		errorMessage.setText(msg);
		errorMessage.setOpaque(false);
		errorMessage.setEditable(false);
		errorPanel.add(errorMessage, BorderLayout.NORTH);
		
		final JTextPane errorOutput = new JTextPane();
		errorOutput.setContentType("text/html");
		errorOutput.setPreferredSize(new Dimension(200,150));
		errorOutput.setEditable(false);
		errorOutput.setText(exception);
		errorOutput.setCaretPosition(0);
		//errorOutput.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(errorOutput);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		errorPanel.add(scrollPane, BorderLayout.CENTER);
		
		JOptionPane.showMessageDialog(null, errorPanel, title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Shows a {@link JOptionPane} error dialog
	 * @param title the title of the error dialog
	 * @param msg the message that should be displayed
	 * @return true if exit button is pressed, false otherwise
	 * @see JOptionPane#showOptionDialog(java.awt.Component, Object, String, int, int, javax.swing.Icon, Object[], Object)
	 * */
	public static void showInfomationDialog(String title, String msg) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/**
	 * Shows a custom  {@link JOptionPane} dialog
	 * @param parent of this dialog
	 * @param component to add custom elements
	 * @param title the title of the error dialog
	 * @param messageType e.g JOptionPane.PLAIN_MESSAGE
	 * @param icon offers the option to show a custom icon
	 * @param options offers the option to customize the dialog buttons
	 * @return true if optionPressed button is pressed, false otherwise
	 * @see  JOptionPane#showOptionDialog(Component, Object, String, int, int, Icon, Object[], Object)
	 * */
	public static boolean showCustomDialog(Component parent, JComponent component, String title, int messageType, Icon icon, Object[] options) {
		boolean optionPressed = false;
	    int n = JOptionPane.showOptionDialog(parent,
	    		component, title,
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon,
	            options, options[0]);
	    if(n == JOptionPane.OK_OPTION){ 
	    	optionPressed = true;
	    } 
		return optionPressed;
	}
	
	/**
	 * Shows a custom  {@link JOptionPane} dialog
	 * @param parent of this dialog
	 * @param component to add custom elements
	 * @param title the title of the error dialog
	 * @param messageType e.g JOptionPane.PLAIN_MESSAGE
	 * @param icon offers the option to show a custom icon
	 * @see  JOptionPane#showOptionDialog(Component, Object, String, int, int, Icon, Object[], Object)
	 * */
	public static void showCustomDialog(Component parent, JComponent component, String title, int messageType, Icon icon) {
		JOptionPane.showMessageDialog(parent, component, title, messageType, icon);
	}
	
	
}
