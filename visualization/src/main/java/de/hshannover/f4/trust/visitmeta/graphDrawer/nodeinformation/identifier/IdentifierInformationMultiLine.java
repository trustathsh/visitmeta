package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * A class that implements {@link IdentifierInformationStrategy} and returns
 * a multiline textual representation of an {@link Identifier}, according
 * to its type.
 * 
 * Example:<br>
 * identity<br>
 * [ name=John Doe ]<br>
 * [ type=username ]<br>
 * 
 * @author Bastian Hellmann
 *
 */
public class IdentifierInformationMultiLine extends IdentifierInformationStrategy {

	@Override
	public String createTextForAccessRequest(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ name=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.ACCESS_REQUEST_ATTR_NAME, "name"));	// name
		sb.append(" ]");
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForIPAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ value=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IP_ADDRESS_ATTR_VALUE, "value"));	// value
		sb.append(" ]");
		sb.append("\n");
		sb.append("[ type=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IP_ADDRESS_ATTR_TYPE, "type"));	// type
		sb.append(" ]");
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForMacAddress(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ value=");
		sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.MAC_ADDRESS_ATTR_VALUE, "value"));	// value
		sb.append(" ]");
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForIdentity(IdentifierWrapper wrapper) {
		String type = wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_TYPE, "type");

		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());

		if (type.equals("other")) {
			sb.append("\n");
			String otherTypeDefinition = wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_NAME, "name");	// name
			sb.append("[ name=");
			sb.append(otherTypeDefinition.substring(otherTypeDefinition.indexOf(";") + 1, otherTypeDefinition.indexOf(" ")));
			sb.append(" ]");
			sb.append("\n");

			sb.append("[ type=");
			sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_TYPE, "type"));	// type
			sb.append(" ]");
			sb.append("\n");

			sb.append("[ other-type-definition=");
			sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_OTHER_TYPE_DEF, "other-type-definition"));	// other-type-definition
			sb.append(" ]");
		} else {
			sb.append("\n");
			sb.append("[ name=");
			sb.append(wrapper.getValueForXpathExpressionOrElse("@" + IdentifierHelper.IDENTITY_ATTR_NAME, "name"));	// name
			sb.append(" ]");
			sb.append("\n");
			sb.append("[ type=");
			sb.append(type);	// type
			sb.append(" ]");
		}
		sb.append(getAdministrativeDomain(wrapper));
		return sb.toString();
	}

	@Override
	public String createTextForDevice(IdentifierWrapper wrapper) {
		StringBuilder sb = new StringBuilder();
		sb.append(wrapper.getTypeName());
		sb.append("\n");
		sb.append("[ name=");
		sb.append(wrapper.getValueForXpathExpressionOrElse(IdentifierHelper.DEVICE_NAME_EL_NAME, "name"));	// name
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * Returns a {@link String} representation of the <i>administrative domain</i>
	 * attribute, if it exists.
	 * 
	 * @param identifier {@link IdentifierWrapper} instance (encapsulating a {@link Identifier})
	 * @return {@link String} representing the <i>administrative domain</i> attribute
	 */
	private String getAdministrativeDomain(
			IdentifierWrapper identifier) {
		String administrativeDomain = identifier.getValueForXpathExpression("@" + IdentifierHelper.IDENTIFIER_ATTR_ADMIN_DOMAIN);	// administrative-domain

		StringBuilder sb = new StringBuilder();
		if (administrativeDomain != null && !administrativeDomain.isEmpty()) {
			sb.append("\n");
			sb.append("[ administrative-domain=");
			sb.append(administrativeDomain);
			sb.append(" ]");
		}
		return sb.toString();
	}
}
