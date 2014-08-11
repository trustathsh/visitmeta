package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * Abstract superclass for classes that create the text for {@link Identifier}
 * elements within the graphical representation.
 * 
 * @author Bastian Hellmann
 *
 */
public abstract class IdentifierInformationStrategy {

	/**
	 * Creates the text written into a {@link Identifier}-node,
	 * based on the type and the information of the identifier itself.
	 * 
	 * Uses abstract methods for each {@link Identifier}-type, e.g. device or
	 * mac-address.
	 *
	 * @param identifier the {@link Identifier} for creating the text
	 * @return a {@link String} containing the specific information for the given {@link Identifier}
	 */
	public String getText(Identifier identifier) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);

		switch (wrapper.getTypeName()) {
		case IdentifierHelper.ACCESS_REQUEST_EL_NAME:
			return createTextForAccessRequest(wrapper);
		case IdentifierHelper.DEVICE_EL_NAME:
			return createTextForDevice(wrapper);
		case IdentifierHelper.IDENTITY_EL_NAME:
			return createTextForIdentity(wrapper);
		case IdentifierHelper.MAC_ADDRESS_EL_NAME:
			return createTextForMacAddress(wrapper);
		case IdentifierHelper.IP_ADDRESS_EL_NAME:
			return createTextForIPAddress(wrapper);
		default:
			throw new IllegalArgumentException("Unsupported identifier type: " + wrapper.getTypeName());
		}
	}

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>access-request</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>access-request</i> {@link Identifier}
	 */
	protected abstract String createTextForAccessRequest(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>ip-address</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>ip-address</i> {@link Identifier}
	 */
	protected abstract String createTextForIPAddress(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>mac-address</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>mac-address</i> {@link Identifier}
	 */
	protected abstract String createTextForMacAddress(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>identity</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>identity</i> {@link Identifier}
	 */
	protected abstract String createTextForIdentity(IdentifierWrapper identifier);

	/**
	 * Returns a {@link String} representation of a IF-MAP <i>device</i> {@link Identifier}.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>device</i> {@link Identifier}
	 */
	protected abstract String createTextForDevice(IdentifierWrapper identifier);
}
