package de.hshannover.f4.trust.visitmeta.input.gui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import de.hshannover.f4.trust.visitmeta.input.device.Device;

/**
 * Component that handles and encapsulates the connection between input devices
 * and the GUI.
 * GUI components only report to this class,
 * this class creates and manages {@link MotionController} instances for that
 * specific GUI component and
 * motion control devices can retrieve the current active
 * {@link MotionController} instance from this class.
 * 
 * @author Bastian Hellmann
 *
 */
public class MotionControllerHandler {

	private MotionController mCurrentMotionController = null;
	private Map<ConnectionTab, MotionController> mMotionControllerMap;

	public MotionControllerHandler() {
		mMotionControllerMap = new ConcurrentHashMap<>();
	}

	/**
	 * Method for GUI components to report the current {@link ConnectionTab}
	 * instance that shall be target for incoming motion information by
	 * {@link Device}s.
	 * 
	 * @param tab the {@link ConnectionTab} instance, reported by the GUI
	 */
	public synchronized void setCurrentConnectionTab(ConnectionTab tab) {
		MotionController motionController = null;

		if (mMotionControllerMap.containsKey(tab)) {
			motionController = mMotionControllerMap.get(tab);
		} else {
			motionController = MotionControllerFactory.create(tab);
			mMotionControllerMap.put(tab, motionController);
		}

		mCurrentMotionController = motionController;
	}

	/**
	 * Returns the current active {@link MotionController} instance, so that
	 * arbitrary input devices can send their motion information to this
	 * controller.
	 * 
	 * @return current active {@link MotionController} instance;
	 * returns <code>null</code> if there is currently not active
	 * {@link MotionController}
	 */
	public synchronized MotionController getCurrentMotionController() {
		return mCurrentMotionController;
	}

	/**
	 * Removes the given {@link ConnectionTab} from the internal list, if
	 * existing.
	 * 
	 * @param tab {@link ConnectionTab}
	 */
	public synchronized void removeConnectionTab(ConnectionTab tab) {
		MotionController controller = mMotionControllerMap.get(tab);
		if (controller != null) {
			mMotionControllerMap.remove(tab);

			if (mCurrentMotionController == controller) {
				mCurrentMotionController = null;
			}
		}
	}

}
