package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * A class that implements {@link MetadataInformationStrategy} and returns
 * the typename of {@link Metadata} and the publish timestamp in a second line.
 * 
 * Eaxmple:<br>
 * request-for-investigation<br>
 * 1400001
 * 
 * @author Bastian Hellmann
 *
 */
public class MetadataInformationWithTimestamp implements MetadataInformationStrategy {

	@Override
	public String getText(Metadata metadata) {
		StringBuilder sb = new StringBuilder();
		sb.append(metadata.getTypeName());
		sb.append("\n");
		sb.append(metadata.getPublishTimestamp());
		return sb.toString();
	}

}
