package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Implementation of {@link MetadataInformationStrategy} that creates
 * textual information for {@link Metadata} containing just the typename.
 * 
 * Example:<br>
 * request-for-investigation
 * 
 * @author Bastian Hellmann
 *
 */
public class MetadataInformationSingleLine implements MetadataInformationStrategy {

	@Override
	public String getText(Metadata metadata) {
		return metadata.getTypeName();
	}

}
