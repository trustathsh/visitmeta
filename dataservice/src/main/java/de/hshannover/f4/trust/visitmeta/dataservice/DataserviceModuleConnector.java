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
 * This file is part of visitmeta-dataservice, version 0.4.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.dataservice;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.DataserviceModule;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.util.FileUtils;
import de.hshannover.f4.trust.visitmeta.util.OperatingSystemUtils;
import de.hshannover.f4.trust.visitmeta.util.ReflectionUtils;

/**
 * Class that loads and initializes {@link DataserviceModule}.
 * {@link DataserviceModule}s will be searched in a subfolder named
 * <i>dataservice-modules</i>.
 * 
 * @author Bastian Hellmann
 * 
 */
public class DataserviceModuleConnector {

	private static Logger logger = Logger
			.getLogger(DataserviceModuleConnector.class);

	private static final String DATASERVICE_MODULES_FOLDER = "dataservice-modules";
	private static final String LIB_FOLDER = "lib";
	private static final String NATIVE_LIBS_FOLDER = "native-libs";

	/**
	 * Initializes external input {@link DataserviceModule}s. Tries to load them
	 * from subfolders of <i>dataservice-modules</i> inside the projects root
	 * directory.
	 * 
	 * Inside each subdirectory,
	 * <ul>
	 * <li>native libraries are searched in <i>native-libs</i>, with
	 * subdirectories for the operating system
	 * <ul>
	 * <li>windows
	 * <li>osx
	 * <li>linux
	 * </ul>
	 * and inside them for the systems architecture
	 * <ul>
	 * <li>x64
	 * <li>x86
	 * </ul>
	 * <li>external dependancies in <i>lib</i>
	 * </ul>
	 * For example, a {@link DataserviceModule} called <i>module-1</i> with
	 * native libraries for Linux 64bit would have the path
	 * <i>dataservice-modules/module-1/native-libs/linux/x64/</i>
	 * 
	 * @param manager
	 *            a {@link ConnectionManager} instance to be used by all
	 *            {@link DataserviceModule}s
	 * @return a {@link List} of {@link DataserviceModule} that were loaded,
	 *         initialized and started
	 */
	public static List<DataserviceModule> initializeModules(
			ConnectionManager manager) {
		List<File> moduleDirectories = FileUtils
				.listSubDirectories(DATASERVICE_MODULES_FOLDER);

		List<DataserviceModule> modules = new ArrayList<>();
		List<DataserviceModule> startedModules = new ArrayList<>();

		if (moduleDirectories.size() > 0) {
			logger.info("Dataservice module directories found: "
					+ moduleDirectories);

			String osName = System.getProperty("os.name");
			String osArch = System.getProperty("os.arch");
			String osVersion = System.getProperty("os.version");

			String osNameFolder = OperatingSystemUtils
					.getOperatingSystemNameFolder(osName);
			String osArchFolder = OperatingSystemUtils
					.getSystemArchitectureFolder(osArch);

			logger.info("Operating system information: " + osName + " v"
					+ osVersion + " (" + osArch + ")");

			for (File subDirectory : moduleDirectories) {
				DataserviceModule module = null;
				List<URL> jarFilesForDataserviceModule = new ArrayList<>();
				List<URL> jarFilesForClassloader = new ArrayList<>();
				List<URL> subdirectoryJarFiles = new ArrayList<>();

				String nativeLibsPath = NATIVE_LIBS_FOLDER + File.separator
						+ osNameFolder + File.separator + osArchFolder;
				File nativeLibsFolder = FileUtils.findDirectory(subDirectory,
						nativeLibsPath);
				if (nativeLibsFolder != null) {
					FileUtils.appendToLibraryPath(nativeLibsFolder
							.getAbsolutePath());
				} else {
					logger.warn("Did not found native-library folder for dataservice module '"
							+ subDirectory + "'");
				}

				subdirectoryJarFiles = FileUtils.listJarFiles(subDirectory);
				jarFilesForDataserviceModule.addAll(subdirectoryJarFiles);
				jarFilesForClassloader.addAll(subdirectoryJarFiles);

				File libFolder = FileUtils.findDirectory(subDirectory,
						LIB_FOLDER);
				if (libFolder != null) {
					jarFilesForClassloader.addAll(FileUtils
							.listJarFiles(libFolder));
				} else {
					logger.warn("Did not found dependency-library folder for dataservice module '"
							+ subDirectory + "'");
				}

				if (jarFilesForDataserviceModule.size() > 0) {
					ClassLoader loader = URLClassLoader
							.newInstance(jarFilesForClassloader
									.toArray(new URL[] {}));

					for (URL jarFile : jarFilesForDataserviceModule) {
						module = loadModuleFromJarFile(loader, jarFile);

						if (module != null) {
							modules.add(module);
							logger.debug("Dataservice module '"
									+ module.getName()
									+ "' was loaded from jar-file '" + jarFile
									+ "'");
						} else {
							logger.warn("Could not instantiate dataservice module '"
									+ subDirectory
									+ "' from jar-file '"
									+ jarFile + "'");
						}
					}
				} else {
					logger.warn("Did not found any jar-files for dataservice module '"
							+ subDirectory + "'");
				}
			}

			if (modules.size() > 0) {
				startedModules = initAndStartModules(modules, manager);
			} else {
				logger.warn("Did not found any dataservice modules inside '"
						+ DATASERVICE_MODULES_FOLDER);
			}
		}

		return startedModules;
	}

	/**
	 * Tries to initialize and start the already loaded
	 * {@link DataserviceModule}s, by calling their corresponding interface
	 * methods.
	 * 
	 * @param modules
	 *            a {@link List} of {@link DataserviceModule} that were loaded
	 *            and instantiated via Java Reflection
	 * @param manager
	 *            a {@link ConnectionManager} instance to be used by all
	 *            {@link DataserviceModule}s
	 * @return a {@link List} of {@link DataserviceModule} that were
	 *         successfully initialized and started
	 */
	private static List<DataserviceModule> initAndStartModules(
			List<DataserviceModule> modules, ConnectionManager manager) {
		boolean initializationResult = false;

		int i = 1;
		int num = modules.size();

		List<DataserviceModule> startedModules = new ArrayList<>();
		for (DataserviceModule module : modules) {
			if (module != null) {
				module.setConnectionManager(manager);
				initializationResult = module.init();
				if (initializationResult) {
					module.start();
					startedModules.add(module);
				} else {
					logger.error("Could not initialize dataservice module ("
							+ i + "/" + num + ") '" + module.getName() + "'");
				}
			}

			i++;
		}

		return startedModules;
	}

	/**
	 * Try to load a {@link DataserviceModule} from a Jar-File with a given
	 * {@link ClassLoader} and returns a fresh instance of that class. If
	 * loading fails, <code>null</code> is returned.
	 * 
	 * @param classLoader
	 *            a {@link ClassLoader} that contains all needed native
	 *            libraries and Java dependencies for loading a
	 *            {@link DataserviceModule} from the JAR file
	 * @param jarFile
	 *            JAR file to load the {@link DataserviceModule} from
	 * @return a instance of {@link DataserviceModule}, or null if loading fails
	 */
	private static DataserviceModule loadModuleFromJarFile(
			ClassLoader classLoader, URL jarFile) {
		try {
			List<String> classNames = ReflectionUtils.getClassNames(jarFile);
			return ReflectionUtils.loadClass(classLoader, classNames,
					DataserviceModule.class);
		} catch (IOException | SecurityException | IllegalArgumentException e) {
			logger.warn("Could not load dataservice module from " + jarFile
					+ ": " + e);
		}

		return null;
	}
}
