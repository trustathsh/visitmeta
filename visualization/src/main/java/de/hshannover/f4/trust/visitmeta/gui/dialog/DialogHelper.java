package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

import de.hshannover.f4.trust.visitmeta.util.StringHelper;

public class DialogHelper {

	public static void showErrorDialog(Component parent, String message, String title) {
		JOptionPane.showMessageDialog(null, StringHelper.breakLongString(message, 80), title, JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorDialog(String message, String title) {
		showErrorDialog(null, message, title);
	}

	public static void showWarningDialog(Component parent, String message, String title) {
		JOptionPane.showMessageDialog(parent, StringHelper.breakLongString(message, 80), title, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showWarningDialog(String message, String title) {
		showWarningDialog(null, message, title);
	}
}
