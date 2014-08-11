package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

/**
 * Factory class for {@link IdentifierInformationStrategy}.
 * 
 * @author Bastian Hellmann
 *
 */
public class IdentifierInformationStrategyFactory {

	/**
	 * Returns a {@link IdentiferInformationStrategy} instance
	 * based on the given {@link IdentifierInformationStrategyType}.
	 * 
	 * @param type {@link IdentifierInformationStrategyType}
	 * @return {@link IdentifierInformationStrategy} instance for the given {@link IdentifierInformationStrategyType}
	 */
	public static IdentifierInformationStrategy create(
			IdentifierInformationStrategyType type) {

		switch (type) {
		case SINGLE_LINE:
			return new IdentifierInformationSingleLine();
		case MULTI_LINE:
			return new IdentifierInformationMultiLine();
		default:
			throw new IllegalArgumentException("No strategy for given type '" + type + "'");
		}
	}

}
