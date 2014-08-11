package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Interface for classes that create the text for {@link Metadata} elements
 * within the graphical representation.
 * 
 * @author Bastian Hellmann
 *
 */
public interface MetadataInformationStrategy {

	/**
	 * Create the text regarding the given {@link Metadata} instance.
	 * 
	 * @param metadata {@link Metadata} instance
	 * @return text for the given {@link Metadata} instance
	 */
	public String getText(Metadata metadata);

}
