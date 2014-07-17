package de.hshannover.f4.trust.visitmeta.input.device;

import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;

/**
 * Interface for external control devices.
 * 
 * @author Bastian Hellmann
 *
 */
public interface Device {

	/**
	 * Method to initialize the {@link Device}, after the instance was
	 * constructed.
	 * 
	 * @return true, if initialization was successful
	 */
	public boolean init();

	/**
	 * Method to properly shutdown the {@link Device}.
	 * 
	 * @return true, if disconnection was successful
	 */
	public boolean shutdown();

	/**
	 * Start operations of the {@link Device}, i.e. start threads etc.
	 */
	public void start();

	/**
	 * Stop operations of the {@link Device}, i.e. stop threads etc.
	 */
	public void stop();

	/**
	 * Sets the {@link MotionControllerHandler} instance, used to bind the
	 * {@link Device} to the Graphical User Interface.
	 * 
	 * @param handler {@link MotionControllerHandler} instance
	 */
	public void setMotionControllerHandler(MotionControllerHandler handler);

	/**
	 * Returns the informational name of the {@link Device}.
	 * 
	 * @return name of the {@link Device}
	 */
	public String getName();
}
