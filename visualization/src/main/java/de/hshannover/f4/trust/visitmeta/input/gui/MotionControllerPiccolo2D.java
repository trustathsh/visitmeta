package de.hshannover.f4.trust.visitmeta.input.gui;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;

/**
 * An implementation of the {@link MotionController} interface for
 * Piccolo2D.
 * Translates incoming motion input to operations on the {@link PCamera} of
 * Piccolo2D.
 * 
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 *
 */
public class MotionControllerPiccolo2D implements MotionController {

	private PCamera vCamera;
	private double zfactor;
	private double xyfactor;
	private MotionInformationPane motionInformationPane;

	private static final Logger logger = Logger.getLogger(MotionControllerPiccolo2D.class);

	public MotionControllerPiccolo2D(ConnectionTab tab) {
		this.motionInformationPane = tab.getMotionInformationPane();

		this.vCamera = ((PCanvas) tab.getGraphPanel().getPanel()).getCamera();
		this.zfactor = Double.valueOf(PropertiesManager.getProperty("input",
				"guicontroller.zfactor", ""));
		this.xyfactor = Double.valueOf(PropertiesManager.getProperty("input",
				"guicontroller.xyfactor", ""));
	}

	@Override
	public void moveCamera(double x, double y) {
		vCamera.translateView(x * xyfactor, y * xyfactor);
		logger.trace("moveCamera -> x: " + x + ", y: ");
	}

	@Override
	public void zoom(double z) {
		vCamera.scaleViewAboutPoint(1 - z * zfactor, vCamera.getViewBounds()
				.getCenterX(), vCamera.getViewBounds().getCenterY());
		logger.trace("zoom -> z: " + z);
	}

	@Override
	public void moveCamera(double x, double y, double z) {
		moveCamera(x, y);
		zoom(z);
		logger.trace("moveCamera -> x: " + x + ", y: " + ", z: " + z);
	}

	@Override
	public void makeInvisible() {
		motionInformationPane.hideMotionInformationPanel();
	}

	@Override
	public void repaint() {
		motionInformationPane.repaint();
	}

	@Override
	public void setCameraPosition(double x, double y) {
		// not implemented
	}

	@Override
	public void setCameraPositon(double x, double y, double z) {
		// not implemented
	}

	@Override
	public void setCameraRotation(double x, double y, double z) {
		// not implemented
	}

	@Override
	public void setCursorPosition(double x, double y, double z) {
		motionInformationPane.setCursorPosition(x, y, z);
		logger.trace("setCursorPosition -> x: " + x + ", y: " + ", z: " + z);
	}
}