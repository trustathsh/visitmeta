package de.hshannover.f4.trust.visitmeta.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Utility class for {@link File} operations.
 * 
 * @author Bastian Hellmann
 *
 */
public class FileUtils {

	private static Logger logger = Logger.getLogger(FileUtils.class);

	/**
	 * Lists all direct subdirectories for a given directory.
	 * 
	 * @param startDirectoryPath the directory, for which subdirectories will be searched
	 * @return a {@link List} with all subdirectories for the given directory
	 */
	public static List<File> listSubDirectories(String startDirectoryPath) {
		List<File> subDirectories = new ArrayList<>();

		File startDirectory = new File(startDirectoryPath);
		if (startDirectory.isDirectory()) {
			File[] files = startDirectory.listFiles();

			for (File file : files) {
				if (file.isDirectory()) {
					subDirectories.add(file);
				}
			}
		} else {
			logger.warn("'" + startDirectoryPath + "' is not a directory.");
		}

		return subDirectories;
	}

	/**
	 * Searches for a folder within a given directory.
	 * 
	 * @param startDirectory the directory, in which the subdirectory will be searched
	 * @return the subfolder, if found; elsewise <b>null</b> is returned
	 */
	public static File findDirectory(File startDirectory, String folderToSearchFor) {
		File searchForFolder = new File(startDirectory.getAbsolutePath() + File.separator + folderToSearchFor);
		if (searchForFolder.exists()) {
			logger.debug("Found '" + folderToSearchFor + "' subfolder at: " + searchForFolder.getAbsolutePath());
			return searchForFolder;
		} else {
			logger.warn("Did not found '" + folderToSearchFor + "' subfolder at: " + searchForFolder.getAbsolutePath());
			return null;
		}
	}

	/**
	 * Returns a list of {@link URL} instances, containing all jar URL from
	 * the procedure directory.
	 * 
	 * @param directory the directory, in which JAR file swill be searched
	 * @return a {@link List} of {@link URL} for every JAR file in the given directory
	 */
	public static List<URL> listJarFiles(File directory) {
		List<URL> jarFiles = new ArrayList<URL>();

		String[] files = directory.list();

		for (String f : files) {
			if (f.endsWith(".jar")) {
				try {
					URL u = new File(directory + File.separator + f).toURI().toURL();
					jarFiles.add(u);
					logger.debug("Found jar: " + u);

				} catch (MalformedURLException e) {
					logger.warn("Could not load " + f + ": " + e.getMessage());
				}
			}
		}

		return jarFiles;
	}

	/**
	 * Partly from: http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
	 * 
	 * Appends the given directory to the java.library.path and FORCES the JVM
	 * to reload all native library folder.
	 * 
	 * @param directoryPath
	 */
	public static void appendToLibraryPath(String directoryPath) {
		String originalJavaLibraryPath = System.getProperty("java.library.path");
		String newJavaLibraryPath = originalJavaLibraryPath + File.pathSeparator + directoryPath;

		System.setProperty("java.library.path", newJavaLibraryPath);
		logger.debug("Setting 'java.library.path' to '" + newJavaLibraryPath + "'");

		try {
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.warn("Could not append '" + directoryPath + "' to java.library.path: " + e);
		}
	}
}
