package de.hshannover.f4.trust.visitmeta.input.gui;

import de.hshannover.f4.trust.visitmeta.graphDrawer.Piccolo2DPanel;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;

/**
 * Factory to create {@link MotionController} suitable for given
 * {@link ConnectionTab} and the underlying GUI implementation.
 * 
 * @author Bastian Hellmann
 *
 */
public class MotionControllerFactory {

	/**
	 * Creates a new {@link MotionController} instance for the given
	 * {@link ConnectionTab} instance.
	 * Throws an {@link IllegalArgumentException} if no {@link MotionController}
	 * implementation is available.
	 * 
	 * @param tab {@link ConnectionTab}
	 * @return {@link MotionController} instance
	 */
	public static MotionController create(ConnectionTab tab) {
		if (tab.getGraphPanel() instanceof Piccolo2DPanel) {
			return new MotionControllerPiccolo2D(tab);
		} else {
			throw new IllegalArgumentException("No GUI available for given panel instance type");
		}
	}

}
