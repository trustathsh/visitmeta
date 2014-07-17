package de.hshannover.f4.trust.visitmeta.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * Utility class for Reflection operations.
 *
 * @author Bastian Hellmann
 *
 */
public class ReflectionUtils {

	private static Logger logger = Logger.getLogger(ReflectionUtils.class);

	/**
	 * Checks a list of given class names if they implement a given interface
	 * and loads it via reflection.
	 * 
	 * @param classLoader a given {@link ClassLoader} instance (configured with specific URLs for JARs etc.)
	 * @param classNames a {@link List} of class names
	 * @param t a {@link Class} that specifies a interface class
	 * @return a fresh instance of the class type given as parameter; <b<null</b> if no class was found
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadDevice(ClassLoader classLoader, List<String> classNames, Class<T> t) {
		for (String className : classNames) {
			try {
				Class<?> classToLoad = Class.forName(className, true, classLoader);
				Class<?>[] interfaces = classToLoad.getInterfaces();

				boolean implementsInterface = false;
				for (Class<?> i : interfaces) {
					if (i.getCanonicalName().equals(t.getCanonicalName())) {
						implementsInterface = true;
					}
				}

				if (implementsInterface) {
					logger.trace(className + " implements " + t.getCanonicalName());
					return (T) classToLoad.newInstance();
				} else {
					logger.trace(className + " does not implement " + t.getCanonicalName());
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				logger.warn("could not load " + className + ": " + e);
			}
		}

		return null;
	}

	/**
	 * Creates a {@link List} of all classes within a given JAR file.
	 * 
	 * From: http://stackoverflow.com/questions/15720822/how-to-get-names-of-classes-inside-a-jar-file
	 * 
	 * @param jarFile the JAR file to search for Classes
	 * @return a {@link List} with all found classes
	 * @throws IOException
	 */
	public static List<String> getClassNames(URL jarFile) throws IOException {
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile.getPath()));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
			if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
				// This ZipEntry represents a class. Now, what class does it represent?
				StringBuilder className = new StringBuilder();
				for (String part : entry.getName().split("/")) {
					if(className.length() != 0) {
						className.append(".");
					}
					className.append(part);
					if (part.endsWith(".class")) {
						className.setLength(className.length() - ".class".length());
					}
				}
				classNames.add(className.toString());
			}
		}

		zip.close();

		return classNames;
	}

}
