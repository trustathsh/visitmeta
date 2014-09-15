package de.hshannover.f4.trust.visitmeta.util;

/**
 * A class containing constants and methods to handle Operations System specific
 * tasks.
 * 
 * @author Bastian Hellmann
 * 
 */
public class OperatingSystemUtils {

	// AIX (Advanced Interactive eXecutive.)
	// Digital Unix
	// FreeBSD
	// HP (Hewlett Packard) UX
	// Irix
	// Linux
	// Mac OS (Operating System)
	// Mac OS X
	// MPE/iX
	// Netware 4.11
	// OS/2
	// Solaris
	// Windows 2000
	// Windows 7
	// Windows 8
	// Windows 95
	// Windows 98
	// Windows NT
	// Windows Vista
	// Windows XP
	public static final String OS_NAME_MAC_OSX = "Mac OS X";
	public static final String OS_NAME_LINUX = "Linux";
	public static final String OS_NAME_WINDOWS = "Windows";

	public static final String OS_ARCH_X64 = "x86_64";
	public static final String OS_ARCH_X86 = "x86";

	/**
	 * Returns the folder name for a given OS architecture, to be used for
	 * finding native-libraries.
	 * 
	 * @param osArch
	 *            OS architecture string (e.g. by System.getProperty)
	 * @return substring that represents the folder name for the given OS
	 *         architecture
	 *         <ul>
	 *         <li><i>x64</i> for 64bit operating systems
	 *         <li><i>x86</i> for 32bit operating systems
	 *         </ul>
	 */
	public static String getSystemArchitectureFolder(String osArch) {
		if (osArch.equals(OS_ARCH_X86)) {
			return "x86";
		} else if (osArch.equals(OS_ARCH_X64)) {
			return "x64";
		} else {
			throw new IllegalArgumentException(
					"Unsupported operating system architecture: " + osArch);
		}
	}

	/**
	 * Returns the folder name for a given OS name, to be used for finding
	 * native-libraries.
	 * 
	 * @param osName
	 *            OS name string (e.g. by System.getProperty)
	 * @return substring that represents the folder name for the given OS name
	 *         <ul>
	 *         <li><i>windows</i> for all Windows-based operating systems
	 *         <li><i>linux</i> for all Linux-based operating systems
	 *         <li><i>osx</i> for all Mac OSX-based operating systems
	 *         </ul>
	 */
	public static String getOperatingSystemNameFolder(String osName) {
		if (osName.contains(OS_NAME_MAC_OSX)) {
			return "osx";
		} else if (osName.contains(OS_NAME_LINUX)) {
			return "linux";
		} else if (osName.contains(OS_NAME_WINDOWS)) {
			return "windows";
		} else {
			throw new IllegalArgumentException("Unsupported operating system: "
					+ osName);
		}
	}

}
