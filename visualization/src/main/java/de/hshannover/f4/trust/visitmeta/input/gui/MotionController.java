package de.hshannover.f4.trust.visitmeta.input.gui;

/**
 * Interface that defines methods to transfer incoming input information to
 * the rendering components (controls virtual cameras etc.).
 * 
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 */
public interface MotionController {

	/**
	 * Tell the rendering component to hide its content.
	 */
	public void makeInvisible();

	/**
	 * Tell the rendering component to repaint its content.
	 */
	public void repaint();

	/**
	 * Move the virtual camera of the rendering component (in 2D coordinates).
	 * 
	 * @param x units in x direction to move the camery by
	 * @param y units in y direction to move the camery by
	 */
	public void moveCamera(double x, double y);

	/**
	 * Move the virtual camera of the rendering component (in 3D coordinates).
	 * 
	 * @param x units in x direction to move the camery by
	 * @param y units in y direction to move the camery by
	 * @param z units in z direction to move the camery by
	 */
	public void moveCamera(double x, double y, double z);

	/**
	 * Directly set the camera to a position (in 2D coordinates).
	 * 
	 * @param x the new x position for the camera
	 * @param y the new y position for the camera
	 */
	public void setCameraPosition(double x, double y);

	/**
	 * Directly set the camera to a position (in 3D coordinates).
	 * 
	 * @param x the new x position for the camera
	 * @param y the new y position for the camera
	 * @param z the new z position for the camera
	 */
	public void setCameraPositon(double x, double y, double z);

	/**
	 * Directly set the camera rotation (in 3D coordinates).
	 * 
	 * @param x the degree of rotation in x axis
	 * @param y the degree of rotation in y axis
	 * @param z the degree of rotation in z axis
	 */
	public void setCameraRotation(double x, double y, double z);

	/**
	 * Set the position of the virtual mouse cursor (in 2D coordinates).
	 * 
	 * @param x the new x position of the mouse cursor
	 * @param y the new y position of the mouse cursor
	 * @param z the new z position of the mouse cursor
	 */
	public void setCursorPosition(double x, double y, double z);

	/**
	 * Zooms the virtual camera.
	 * 
	 * @param z zoom factor; values between -1.0 and 1.0 (both included) are
	 * allowed;
	 * <ul>
	 * <li> -1.0 means zooming out with maximum speed
	 * <li> 0.0 means no zooming at all (should be handled by calling class directly)
	 * <li> 1.0 means zooming in with maximum speed
	 * </ul>
	 */
	public void zoom(double z);

}
