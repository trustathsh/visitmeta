package de.hshannover.f4.trust.visitmeta.ifmap;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeUpdate;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

public class SubscriptionHelper {

	private static final Logger log = Logger.getLogger(SubscriptionHelper.class);

	private static final PropertiesReaderWriter config = Application.getIFMAPConfig();
	private static final int MAX_DEPTH = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_DEPTH));
	private static final int MAX_SIZE = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_SIZE));
	public static final String JSON_KEY_SUBSCRIBE_NAME = "subscribeName";
	public static final String JSON_KEY_IDENTIFIER = "identifier";
	public static final String JSON_KEY_IDENTIFIER_TYPE = "identifierType";
	public static final String JSON_KEY_MAX_DEPTH = "maxDepth";
	public static final String JSON_KEY_MAX_SIZE = "maxSize";
	public static final String JSON_KEY_LINKS_FILTER = "linksFilter";
	public static final String JSON_KEY_RESULT_FILTER = "resultFilter";
	public static final String JSON_KEY_TERMINAL_IDENTIFIER_TYPES = "terminalIdentifierTypes";
	public static final String JSON_KEY_STARTUPSUBSCRIBE = "startupSubscribe";

	public static SubscribeRequest buildRequest(JSONObject jObj) {
		String subscribeName = null;
		String identifierType = null;
		String identifier = null;
		String linksFilter = null;
		String resultFilter = null;
		String terminalIdentifierTypes = null;
		int maxDepth = MAX_DEPTH;
		int maxSize = MAX_SIZE;

		Iterator<String> i = jObj.keys();
		while (i.hasNext()) {
			String jKey = i.next();

			switch (jKey) {
			// TODO [MR] NEXT RELEASE use SubscribeKey's
			case JSON_KEY_SUBSCRIBE_NAME: subscribeName = jObj.optString(jKey); break;
			case JSON_KEY_IDENTIFIER_TYPE: identifierType = jObj.optString(jKey); break;
			case JSON_KEY_IDENTIFIER: identifier = jObj.optString(jKey); break;
			case JSON_KEY_MAX_DEPTH: maxDepth = jObj.optInt(jKey); break;
			case JSON_KEY_MAX_SIZE: maxSize = jObj.optInt(jKey); break;
			case JSON_KEY_LINKS_FILTER: linksFilter = jObj.optString(jKey); break;
			case JSON_KEY_RESULT_FILTER: resultFilter = jObj.optString(jKey); break;
			case JSON_KEY_TERMINAL_IDENTIFIER_TYPES: terminalIdentifierTypes = jObj.optString(jKey); break;
			case JSON_KEY_STARTUPSUBSCRIBE: break; // nothing to do
			default: log.warn("The key: \"" + jKey + "\" is not a valide JSON-Key for subscriptions."); break;
			}
		}
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeUpdate subscribe = Requests.createSubscribeUpdate();

		subscribe.setName(subscribeName);
		subscribe.setMaxDepth(maxDepth);
		subscribe.setMaxSize(maxSize);
		subscribe.setMatchLinksFilter(linksFilter);
		subscribe.setResultFilter(resultFilter);
		subscribe.setTerminalIdentifierTypes(terminalIdentifierTypes);
		subscribe.setStartIdentifier(createStartIdentifier(identifierType, identifier));

		request.addSubscribeElement(subscribe);

		return request;
	}

	private static Identifier createStartIdentifier(String sIdentifierType, String sIdentifier) {
		switch (sIdentifierType) {
		case "device":
			return Identifiers.createDev(sIdentifier);
		case "access-request":
			return Identifiers.createAr(sIdentifier);
		case "ip-address":
			String[] split = sIdentifier.split(",");
			switch (split[0]) {
			case "IPv4":
				return Identifiers.createIp4(split[1]);
			case "IPv6":
				return Identifiers.createIp6(split[1]);
			default:
				throw new RuntimeException("unknown IP address type '"+split[0]+"'");
			}
		case "mac-address":
			return Identifiers.createMac(sIdentifier);

			// TODO identity and extended identifiers

		default:
			throw new RuntimeException("unknown identifier type '"+sIdentifierType+"'");
		}
	}

}
