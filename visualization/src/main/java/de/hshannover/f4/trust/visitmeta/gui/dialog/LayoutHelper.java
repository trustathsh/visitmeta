package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LayoutHelper {

	// Auxiliary objects so that all distances are harmonious.
	public static final Insets mNullInsets = new Insets(0, 0, 0, 0); // no free space

	public static final Insets mLblInsets = new Insets(2, 5, 2, 2); // label distances

	public static final Insets mXinsets = new Insets(0, 10, 0, 10); // Input components Clearances

	/**
	 * Hilfsroutine beim Hinzufuegen einer Komponente zu einem Container im
	 * GridBagLayout. Die Parameter sind Constraints beim Hinzufuegen.
	 * 
	 * @param x x-Position
	 * @param y y-Position
	 * @param width Breite in Zellen
	 * @param height Hoehe in Zellen
	 * @param weightx Gewicht
	 * @param weighty Gewicht
	 * @param cont Container
	 * @param comp Hinzuzufuegende Komponente
	 * @param insets Abstaende rund um die Komponente
	 */
	public static void addComponent(int x, int y, int width, int height, double weightx, double weighty,
			Container cont, Component comp, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		cont.add(comp, gbc);
	}
}
