package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlPersist.OPTIONAL;

public class ConnectionConstructor extends Constructor {

	private static final Logger log = Logger.getLogger(ConnectionConstructor.class);

	private boolean mConnectionAsMap = false;

	public ConnectionConstructor() {
		this.yamlConstructors.put(new Tag(ConnectionPersister.CONNECTION_TAG), new ConstructConnection());
	}

	/**
	 * When the key not start with 'm' or a upper case not follow then put 'm' to begin at the key and transform
	 * the next case to upper case.
	 * 
	 * @param key
	 * @return
	 */
	private String transformKey(String key){
		if(key.charAt(0) != 'm' || !Character.isUpperCase(key.charAt(1))){
			char upperSecondChar = Character.toUpperCase(key.charAt(0));
			StringBuilder sb = new StringBuilder("m");
			sb.append(upperSecondChar);
			sb.append(key.substring(1));
			return sb.toString();
		}
		return key;
	}

	private Map<String, Object> getKeyTransformedMapping(Node node) {
		Map<Object, Object> mapping = super.constructMapping((MappingNode) node);
		Map<String, Object> keyTransformMap = new HashMap<String, Object>();

		for(Entry<Object, Object> e: mapping.entrySet()){
			String tmpKey = transformKey((String) e.getKey());
			Object tmpValue = e.getValue();
			keyTransformMap.put(tmpKey, tmpValue);
		}
		return keyTransformMap;
	}

	public boolean isConnectionAsMap() {
		return mConnectionAsMap;
	}

	public void setConnectionAsMap(boolean b) {
		mConnectionAsMap = b;
	}

	private class ConstructConnection extends AbstractConstruct {

		/**
		 * Build a Connection from the node with REQUIRED and OPTIONAL fields.
		 */
		@Override
		public Object construct(Node node) {
			Map<String, Object> connectionDataMap = getKeyTransformedMapping(node);

			if(!mConnectionAsMap){
				return constructConnection(connectionDataMap);
			}else{
				return constructMap(connectionDataMap);
			}
		}

		private Connection constructConnection(Map<String, Object> connectionDataMap){
			String connectionName = (String) connectionDataMap.get("mConnectionName");	// TODO not good, is the field name at the connection class
			String url = (String) connectionDataMap.get("mUrl");						// TODO not good, is the field name at the connection class
			String userName = (String) connectionDataMap.get("mUserName");				// TODO not good, is the field name at the connection class
			String userPass = (String) connectionDataMap.get("mUserPass");				// TODO not good, is the field name at the connection class

			Connection c = null;
			try {
				c = new Connection(connectionName, url, userName, userPass);
			} catch (ConnectionException e) {
				log.error("Error while create a new Connection.", e);
			}

			for (Field field: Connection.class.getDeclaredFields()){
				try {

					if(field.isAnnotationPresent(OPTIONAL.class)){
						field.setAccessible(true);
						String fieldName = field.getName();
						if (connectionDataMap.containsKey(fieldName)){
							field.set(c, connectionDataMap.get(fieldName));
						}
					}

				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("Error while set the optional fields in the Connection object.", e);
				}
			}
			return c;
		}

		private Map<String, String> constructMap(Map<String, Object> connectionDataMap){
			Map<String, String> newMap = new HashMap<String, String>();
			for(Entry<String, Object> e: connectionDataMap.entrySet()){
				newMap.put(e.getKey(), e.getValue().toString());
			}
			return newMap;
		}
	}
}
