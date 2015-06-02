package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Holder class that contains a prefix map for each Connection
 * 
 * @author oelsner
 *
 */
public class NamespaceHolder {
	private static Map<String, HashMap<String, String>> maps = new HashMap<String, HashMap<String, String>>();

	/**
	 * 
	 * @param connection
	 * @param prefix
	 * @param value
	 */
	public static void addPrefix(String connection, String prefix, String value) {
		if(!maps.containsKey(connection)) {
			maps.put(connection, new HashMap<String,String>());
		}
		if (maps.get(connection).containsKey(prefix)) {
			if (!maps.get(connection).get(prefix).equals(value)) {
				// todo warning same prefix with different value
			}
		} else {
			maps.get(connection).put(prefix, value);
		}
	}

	/**
	 * 
	 * @param connection
	 * @return Prefix map of the given Connection name
	 */
	public static Map<String, String> getPrefixMap(String connection) {
		if(!maps.containsKey("standard")) {
			HashMap<String, String> standard = new HashMap<String, String>();
			standard.put("meta", "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2");
			maps.put("standard", standard);
		}
		if(maps.containsKey(connection)) {
			return maps.get(connection);
		}
		return maps.get("standard");
	}
}
