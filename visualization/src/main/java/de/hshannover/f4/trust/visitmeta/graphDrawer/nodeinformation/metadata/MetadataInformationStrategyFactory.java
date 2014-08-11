package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata;

/**
 * Factory class for {@link MetadataInformationStrategy}.
 * 
 * @author Bastian Hellmann
 *
 */
public class MetadataInformationStrategyFactory {

	/**
	 * Returns a {@link MetadataInformationStrategy} instance
	 * based on the given {@link MetadataInformationStrategyType}.
	 * 
	 * @param type {@link MetadataInformationStrategyType}
	 * @return {@link MetadataInformationStrategy} instance for the given {@link MetadataInformationStrategyType}
	 */
	public static MetadataInformationStrategy create(
			MetadataInformationStrategyType type) {

		switch (type) {
		case SINGLE_LINE:
			return new MetadataInformationSingleLine();
		case WITH_TIMESTAMP:
			return new MetadataInformationWithTimestamp();
		default:
			throw new IllegalArgumentException("No strategy for given type '" + type + "'");
		}
	}

}
