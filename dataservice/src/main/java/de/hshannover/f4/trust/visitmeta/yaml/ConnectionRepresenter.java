package de.hshannover.f4.trust.visitmeta.yaml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.yaml.YamlPersist.OPTIONAL;
import de.hshannover.f4.trust.visitmeta.yaml.YamlPersist.REQUIRED;

public class ConnectionRepresenter extends Representer {

	private static final Logger log = Logger.getLogger(ConnectionRepresenter.class);

	public static boolean mMinOutput = true;

	public ConnectionRepresenter() {
		this.representers.put(Connection.class, new RepresentConnection());
	}

	/**
	 * When the key start with 'm' and a upper case is follow then remove 'm' and start with lower case.
	 * 
	 * @param key
	 * @return
	 */
	private String transformKey(String key){
		if(key.charAt(0) == 'm' && Character.isUpperCase(key.charAt(1))){
			char lowerFirstChar = Character.toLowerCase(key.charAt(1));
			StringBuilder sb = new StringBuilder();
			sb.append(lowerFirstChar);
			sb.append(key.substring(2));
			return sb.toString();
		}
		return key;
	}

	private boolean same(Object one, Object other){
		if (one != null){
			return one.equals(other);
		}else if (other != null) {
			return other.equals(one);
		} else {
			return true;
		}
	}

	public boolean isMinoutput() {
		return mMinOutput;
	}

	public void setMinoutput(boolean maxOutput) {
		mMinOutput = maxOutput;
	}

	private class RepresentConnection implements Represent {

		/**
		 * Return a node with a key value map for all fields with the annotation REQUIRED or OPTIONAL
		 * in Connection.class. Optional field only add to the map, when the value is different to the
		 * default value or maxOutput is true. The default value field name for the optional field must be set.
		 */
		@Override
		public Node representData(Object data) {
			Map<String, Object> dataMap = new HashMap<String, Object>();

			if(data instanceof Connection){
				Connection connection = (Connection) data;

				for (Field field: Connection.class.getDeclaredFields()){
					// find all REQUIRED and OPTIONAL annotations fields
					for(int i=0; i<field.getAnnotations().length; i++){
						try{

							if(field.getAnnotations()[i] instanceof REQUIRED){
								field.setAccessible(true);
								Object fieldValue = field.get(connection);
								dataMap.put(transformKey(field.getName()), fieldValue);

							}else if(field.getAnnotations()[i] instanceof OPTIONAL){
								field.setAccessible(true);
								OPTIONAL optionalAnnotation = (OPTIONAL) field.getAnnotations()[i];
								Object fieldValue = field.get(connection);
								if(!same(fieldValue, Connection.class.getField(optionalAnnotation.value()).get(connection)) || !mMinOutput){
									dataMap.put(transformKey(field.getName()), fieldValue);
								}

							}

						} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
							log.error("Error while get the optional or required fields from the Connection object.", e);
						}
					}
				}
			}

			Node nodeMap = representMapping(new Tag(ConnectionPersister.CONNECTION_TAG), dataMap, false);
			return nodeMap;
		}
	}

}
