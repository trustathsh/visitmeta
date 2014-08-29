package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import de.hshannover.f4.trust.visitmeta.util.NullCheck;
import de.hshannover.f4.trust.visitmeta.util.yaml.PropertiesReader;
import de.hshannover.f4.trust.visitmeta.util.yaml.PropertiesWriter;
import de.hshannover.f4.trust.visitmeta.util.yaml.PropertyException;

public class Properties {

	private PropertiesWriter mWriter;

	private PropertiesReader mReader;

	private String mFileName;

	private Map<String, Object> mApplicationConfigs;

	private Properties(Properties context) {
		mWriter = context.mWriter;
		mReader = context.mReader;
		mFileName = context.mFileName;
		mApplicationConfigs = context.mApplicationConfigs;
	}

	/**
	 * Create a ApplicationProperties for load and save Application-YAML files.
	 *
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public Properties(String fileName) throws FileNotFoundException{
		NullCheck.check(fileName, "fileName is null");
		mFileName = fileName;
		mWriter = new PropertiesWriter(mFileName);
		mReader = new PropertiesReader(mFileName);
		load();
	}

	/**
	 * Load the YAML-files new.
	 *
	 * @throws FileNotFoundException
	 */
	public void load() throws FileNotFoundException{
		mApplicationConfigs = mReader.load();
	}

	/**
	 * Save all properties in a YAML-files and override them.
	 *
	 * @throws FileNotFoundException
	 */
	public void save() throws FileNotFoundException {
		mWriter.persist(mApplicationConfigs);
	}

	/**
	 * Get a new Properties from the property key. Only property keys accepted but not a path.
	 * 
	 * @param propertyKey
	 * @throws PropertyException
	 */
	@SuppressWarnings("unchecked")
	public Properties get(String propertyKey) throws PropertyException {
		NullCheck.check(propertyKey, "propertyKey is null");
		Properties context = new Properties(this);
		Object o = mApplicationConfigs.get(propertyKey);
		if(o == null){
			throw new PropertyException(propertyKey + " is not a property key!");
		}
		if(o instanceof Map){
			context.mApplicationConfigs = (Map<String, Object>) o;
		}else{
			throw new PropertyException(propertyKey + " is not part of a property path!");
		}
		return context;
	}

	/**
	 * Get the value from the property path.
	 * 
	 * @param propertyPath foo.bar.property
	 * @return Object
	 * @throws PropertyException
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(String propertyPath) throws PropertyException {
		// check propertyPath
		NullCheck.check(propertyPath, "propertyPath is null");

		// split propertyPath
		String[] propertyKeyArray = propertyPath.split("\\.");

		// iterate the root Map for every key
		Map<String, Object> applicationConfigs = mApplicationConfigs;
		for(int i=0; i<propertyKeyArray.length; i++){
			Object tmp = applicationConfigs.get(propertyKeyArray[i]);
			if(tmp == null){
				if(propertyKeyArray.length > 1){
					throw new PropertyException("property path[" + propertyPath +"] have not a property key["+ propertyKeyArray[i] + "] !");
				} else {
					throw new PropertyException(propertyKeyArray[i] + "] is not a property key!");
				}
			}
			if(tmp instanceof Map){
				applicationConfigs = (Map<String, Object>) applicationConfigs.get(propertyKeyArray[i]);
			}else {
				return tmp;
			}
		}
		return applicationConfigs;
	}

	/**
	 * Get the String-value from the property path. Throw a PropertyException when the value is not a String.
	 * 
	 * @param propertyPath foo.bar.property
	 * @return String
	 * @throws PropertyException
	 */
	public String getString(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		if(o instanceof String){
			return (String) o;
		}
		throw new PropertyException("The value from property path[" + propertyPath + "] is not a String!");
	}

	/**
	 * Get the int-value from the property path. Throw a PropertyException when the value is not a int.
	 * 
	 * @param propertyPath foo.bar.property
	 * @return String
	 * @throws PropertyException
	 */
	public int getInt(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		if(o instanceof Integer){
			return (int) o;
		}
		throw new PropertyException("The value from property path[" + propertyPath + "] is not a int!");
	}

	/**
	 * Get the double-value from the property path. Throw a PropertyException when the value is not a double.
	 * 
	 * @param propertyPath foo.bar.property
	 * @return String
	 * @throws PropertyException
	 */
	public double getDouble(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		if(o instanceof Double){
			return (double) o;
		}
		throw new PropertyException("The value from property path[" + propertyPath + "] is not a double!");
	}

	/**
	 * Get the boolean-value from the property path. Throw a PropertyException when the value is not a boolean.
	 * 
	 * @param propertyPath foo.bar.property
	 * @return String
	 * @throws PropertyException
	 */
	public boolean getBoolean(String propertyPath) throws PropertyException {
		Object o =  getValue(propertyPath);
		if(o instanceof Boolean){
			return (boolean) o;
		}
		throw new PropertyException("The value from property path[" + propertyPath + "] is not a boolean!");
	}

	/**
	 * Set and save the value under the key.
	 * 
	 * @param propertyPath foo.bar.property
	 * @param propertyValue Only String|int|double|boolean
	 * @throws PropertyException
	 */
	public void set(String propertyPath, Object propertyValue) throws PropertyException {
		// check propertyPath
		NullCheck.check(propertyPath, "propertyPath is null");

		// check propertyValue
		if(!(propertyValue instanceof String) && !(propertyValue instanceof Integer)
				&& !(propertyValue instanceof Double) && !(propertyValue instanceof Boolean)){
			throw new PropertyException("Only String|int|double|boolean can be set!");
		}

		// split propertyPath
		String[] propertyKeyArray = propertyPath.split("\\.");

		// build new Map's and add to root map
		if(propertyKeyArray.length > 1){
			Map<String, Object> tmpValueMap = new HashMap<String, Object>();
			tmpValueMap.put(propertyKeyArray[propertyKeyArray.length-1], propertyValue);

			for(int i=propertyKeyArray.length-2; i >= 1; i--){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put(propertyKeyArray[i], tmpValueMap);
				tmpValueMap = tmp;
			}

			mApplicationConfigs.put(propertyKeyArray[0], tmpValueMap);
		}else{
			mApplicationConfigs.put(propertyPath, propertyValue);
		}

		// save all
		try {
			save();
		} catch (FileNotFoundException e) {
			throw new PropertyException("File " + mFileName + " not found!");
		}
	}


	@Override
	public String toString(){
		return mApplicationConfigs.toString();
	}
}
