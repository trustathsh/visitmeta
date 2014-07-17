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
 * This file is part of visitmeta visualization, version 0.0.6,
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
package de.hshannover.f4.trust.visitmeta.input;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.input.device.Device;
import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;
import de.hshannover.f4.trust.visitmeta.util.FileUtils;
import de.hshannover.f4.trust.visitmeta.util.OperatingSystemConstants;
import de.hshannover.f4.trust.visitmeta.util.ReflectionUtils;

/**
 * Class that loads and initializes external input devices.
 * Devices where searched in a subfolder named <i>devices</i>.
 * 
 * @author Bastian Hellmann
 *
 */
public class DeviceToGuiConnector {

	private static Logger logger = Logger.getLogger(DeviceToGuiConnector.class);

	private static final String DEVICE_FOLDER = "devices";
	private static final String LIB_FOLDER = "lib";
	private static final String NATIVE_LIBS_FOLDER = "native-libs";

	/**
	 * Initializes external input devices.
	 * Tries to load them from subfolders of <i>devices</i> inside the projects
	 * root directory.
	 * 
	 * Inside each subdirectory,
	 * <ul>
	 * <li> native libraries are searched in <i>native-libs</i>,
	 * with subdirectories for the operating system
	 * <ul>
	 * <li> windows
	 * <li> osx
	 * <li> linux
	 * </ul>
	 * and inside them for the systems architecture
	 * <ul>
	 * <li> x64
	 * <li> x86
	 * </ul>
	 * <li> external dependancies in <i>lib</i>
	 * </ul>
	 * For example, a device called <i>device-1</i> with native libraries for
	 * Linux 64bit would have the path
	 * <i>devices/device-1/native-libs/linux/x64/</i>
	 * 
	 * @param motionControllerHandler a {@link MotionControllerHandler} instance to be used by all {@link Device}s
	 * @return a {@link List} of {@link Device} that were loaded, initialized and started
	 */
	public static List<Device> initializeDevices(MotionControllerHandler motionControllerHandler) {
		List<File> deviceDirectories = FileUtils.listSubDirectories(DEVICE_FOLDER);

		List<Device> devices = new ArrayList<>();
		List<Device> startedDevices = new ArrayList<>();

		if (deviceDirectories.size() > 0) {
			logger.info("Device directories found: " + deviceDirectories);

			String osName = System.getProperty("os.name");
			String osArch = System.getProperty("os.arch");
			String osVersion = System.getProperty("os.version");

			String osNameFolder = getOperatingSystemNameFolder(osName);
			String osArchFolder = getSystemArchitectureFolder(osArch);

			logger.info("Operating system information: " + osName + " v" + osVersion + " (" + osArch + ")");

			for (File subDirectory : deviceDirectories) {
				Device device = null;
				List<URL> jarFilesForDevice = new ArrayList<>();
				List<URL> jarFilesForClassloader = new ArrayList<>();
				List<URL> subdirectoryJarFiles = new ArrayList<>();

				File nativeLibsFolder= FileUtils.findDirectory(subDirectory, NATIVE_LIBS_FOLDER + File.separator + osNameFolder + File.separator + osArchFolder);
				//				String platformSpecificPath = subDirectory.getAbsolutePath() + File.separator + NATIVE_LIBS_FOLDER + File.separator + osNameFolder + File.separator + osArchFolder;
				//				FileUtils.appendToLibraryPath(platformSpecificPath);
				if (nativeLibsFolder != null) {
					FileUtils.appendToLibraryPath(nativeLibsFolder.getAbsolutePath());

					subdirectoryJarFiles = FileUtils.listJarFiles(subDirectory);
					jarFilesForDevice.addAll(subdirectoryJarFiles);
					jarFilesForClassloader.addAll(subdirectoryJarFiles);

					File libFolder = FileUtils.findDirectory(subDirectory, LIB_FOLDER);
					if (libFolder != null) {
						jarFilesForClassloader.addAll(FileUtils.listJarFiles(libFolder));
					} else {
						logger.warn("Did not found dependency-library folder for device '" + subDirectory + "'");
					}

					if (jarFilesForDevice.size() > 0) {
						ClassLoader loader = URLClassLoader.newInstance(jarFilesForClassloader.toArray(new URL[] {}));

						for (URL jarFile : jarFilesForDevice) {
							device = loadDeviceFromJarFile(loader, jarFile);

							if (device != null) {
								devices.add(device);
								logger.debug("Device '" + device.getName() + "' was loaded.");
							} else {
								logger.warn("Could not instantiate device '" + subDirectory + "' from jar-file");
							}
						}
					} else {
						logger.warn("Did not found any jar-files for device '" + subDirectory + "'");
					}
				} else {
					logger.warn("Did not found native-library folder for device '" + subDirectory + "'");
				}
			}

			if (devices.size() > 0) {
				startedDevices = initAndStartDevices(devices, motionControllerHandler);
			} else {
				logger.warn("Did not found any device inside '" + DEVICE_FOLDER);
			}
		}

		return startedDevices;
	}

	/**
	 * Tries to initialize and start the already loaded {@link Device}s, by calling
	 * their corresponding interface methods.
	 * 
	 * @param devices a {@link List} of {@link Device} that were loaded and instantiated via Java Reflection
	 * @param motionControllerHandler a {@link MotionControllerHandler} instance to be used by all {@link Device}s
	 * @return a {@link List} of {@link Device} that were successfully
	 * initialized and started
	 */
	private static List<Device> initAndStartDevices(List<Device> devices, MotionControllerHandler motionControllerHandler) {
		boolean initializationResult = false;

		List<Device> startedDevices = new ArrayList<>();
		for (Device device : devices) {
			device.setMotionControllerHandler(motionControllerHandler);
			if (device != null) {
				initializationResult = device.init();
				if (initializationResult) {
					device.start();
					startedDevices.add(device);
				} else {
					logger.error("Could not initialize device '" + device.getName() + "'");
				}
			}
		}

		return startedDevices;
	}

	/**
	 * Returns the folder name for a given OS architecture, to be used for
	 * finding native-libraries.
	 * 
	 * @param osArch OS architecture string (e.g. by System.getProperty)
	 * @return substring that represents the folder name for the given OS architecture
	 * <ul>
	 * <li> <i>x64</i> for 64bit operating systems
	 * <li> <i>x86</i> for 32bit operating systems
	 * </ul>
	 */
	private static String getSystemArchitectureFolder(String osArch) {
		if (osArch.equals(OperatingSystemConstants.OS_ARCH_X86)) {
			return "x86";
		} else if (osArch.equals(OperatingSystemConstants.OS_ARCH_X64)) {
			return "x64";
		} else {
			throw new IllegalArgumentException("Unsupported operating system architecture: " + osArch);
		}
	}

	/**
	 * Returns the folder name for a given OS name, to be used for
	 * finding native-libraries.
	 * 
	 * @param osName OS name string (e.g. by System.getProperty)
	 * @return substring that represents the folder name for the given OS name
	 * <ul>
	 * <li> <i>windows</i> for all Windows-based operating systems
	 * <li> <i>linux</i> for all Linux-based operating systems
	 * <li> <i>osx</i> for all Mac OSX-based operating systems
	 * </ul>
	 */
	private static String getOperatingSystemNameFolder(String osName) {
		if (osName.contains(OperatingSystemConstants.OS_NAME_MAC_OSX)) {
			return "osx";
		} else if (osName.contains(OperatingSystemConstants.OS_NAME_LINUX)) {
			return "linux";
		} else if (osName.contains(OperatingSystemConstants.OS_NAME_WINDOWS)) {
			return "windows";
		} else {
			throw new IllegalArgumentException("Unsupported operation system: " + osName);
		}
	}

	/**
	 * Try to load a {@link Device} from a Jar-File with a given
	 * {@link ClassLoader} and returns a fresh instance
	 * of that class. If loading fails, <code>null</code> is returned.
	 * 
	 * @param classLoader a {@link ClassLoader} that contains all needed native
	 * libraries and Java dependencies for loading a {@link Device} from the
	 * JAR file
	 * @param jarFile JAR file to load the {@link Device} from
	 * @return a instance of {@link Device}, or null if loading fails
	 */
	private static Device loadDeviceFromJarFile(ClassLoader classLoader, URL jarFile) {
		try {
			List<String> classNames = ReflectionUtils.getClassNames(jarFile);
			return ReflectionUtils.loadDevice(classLoader, classNames, Device.class);
		} catch (IOException | SecurityException | IllegalArgumentException e) {
			logger.warn("Could not load device from " + jarFile + ": " + e);
		}

		return null;
	}
}
