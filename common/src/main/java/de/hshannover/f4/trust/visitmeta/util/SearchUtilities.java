package de.hshannover.f4.trust.visitmeta.util;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class SearchUtilities {

	public static boolean containsSearchTerm(Identifier identifier,
			String searchTerm) {
		if (!searchTerm.equals("")) {
			return identifier.getRawData().contains(searchTerm);
		} else {
			return false;
		}
	}

	public static boolean containsSearchTerm(Metadata metadata,
			String searchTerm) {
		if (!searchTerm.equals("")) {
			return metadata.getRawData().contains(searchTerm);
		} else {
			return false;
		}
	}

}
