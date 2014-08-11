package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * A class that implements {@link IdentifierInformationStrategy} and returns
 * a singleline textual representation of an {@link Identifier}, according
 * to its type.
 * 
 * Examples:
 * <ul>
 * <li> access-request: ar1
 * <li> device: switch
 * <li> ip-address: 127.0.0.1 (IPv4)
 * <li> mac-address: aa:bb:cc:dd:ee:ff
 * <li> identity: John Doe (username)
 * <li> extended-identifier: service
 * </ul>
 * 
 * @author Bastian Hellmann
 *
 */
public class IdentifierInformationSingleLine extends IdentifierInformationStrategy {

	@Override
	public String createTextForAccessRequest(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.ACCESS_REQUEST_ATTR_NAME, "name"));	// name
		return sb.toString();
	}

	@Override
	public String createTextForIPAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IP_ADDRESS_ATTR_VALUE, "value"));	// value
		sb.append(" (");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IP_ADDRESS_ATTR_TYPE, "type"));	// type
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String createTextForMacAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.MAC_ADDRESS_ATTR_VALUE, "value"));	// value
		return sb.toString();
	}

	@Override
	public String createTextForIdentity(IdentifierWrapper wrapper) {
		String type = wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_TYPE, "type");

		StringBuilder sb = new StringBuilder();
		if (type.equals("other")) {
			sb.append("extended-identifier: ");
			String otherTypeDefinition = wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_NAME, "name");	// name
			sb.append(otherTypeDefinition.substring(otherTypeDefinition.indexOf(";") + 1, otherTypeDefinition.indexOf(" ")));
		} else {
			sb.append(wrapper.getTypeName());
			sb.append(": ");
			sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_NAME, "name"));	// name
			sb.append(" (");
			sb.append(type);	// type
			sb.append(")");
		}
		return sb.toString();
	}

	@Override
	public String createTextForDevice(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append(": ");
		sb.append(wrapper.getValueForXpathExpressionOrElse(IdentifierHelper.DEVICE_NAME_EL_NAME, "name"));	// name
		return sb.toString();
	}
}