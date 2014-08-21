package de.hshannover.f4.trust.visitmeta.util;

import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.util.yaml.PropertiesReader;
import de.hshannover.f4.trust.visitmeta.util.yaml.PropertiesWriter;

public class Properties {

	private static final Logger log = Logger.getLogger(Properties.class);

	private PropertiesWriter mWriter;

	private PropertiesReader mReader;

	private String mFileName;

	private Map<String, Object> mApplicationConfigs;

	private Properties(Properties context) throws FileNotFoundException{
		mWriter = context.mWriter;
		mReader = context.mReader;
		mFileName = context.mFileName;
		mApplicationConfigs = context.mApplicationConfigs;
	}

	/**
	 * Create a ApplicationProperties for load and save Application-YML files.
	 *
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public Properties(String fileName) throws FileNotFoundException{
		log.trace("new Properties()...");
		mFileName = fileName;
		mWriter = new PropertiesWriter(mFileName);
		mReader = new PropertiesReader(mFileName);
		load();
	}

	public void load() throws FileNotFoundException{
		mApplicationConfigs = mReader.load();
	}

	public void save() throws FileNotFoundException {
		mWriter.persist(mApplicationConfigs);
	}

	@SuppressWarnings("unchecked")
	public Properties get(String propertyKey) throws FileNotFoundException{
		Properties context = new Properties(this);
		context.mApplicationConfigs = (Map<String, Object>) mApplicationConfigs.get(propertyKey);
		return context;
	}

	@SuppressWarnings("unchecked")
	public Object getValue(String propertyPath) {
		String[] propertyKeyArray = propertyPath.split("\\.");
		Map<String, Object> applicationConfigs = mApplicationConfigs;
		for(int i=0; i<propertyKeyArray.length; i++){
			Object tmp = applicationConfigs.get(propertyKeyArray[i]);
			if(tmp instanceof Map){
				applicationConfigs = (Map<String, Object>) applicationConfigs.get(propertyKeyArray[i]);
			}else if(tmp instanceof String || tmp instanceof Integer || tmp instanceof Double || tmp instanceof Boolean){
				return tmp;
			}
		}
		return null;
	}

	public String getString(String propertyKey) {
		return (String) getValue(propertyKey);
	}

	public int getInt(String propertyKey) {
		return (int) getValue(propertyKey);
	}

	public double getDouble(String propertyKey) {
		return (double) getValue(propertyKey);
	}

	public boolean getBoolean(String propertyKey) {
		return (boolean) getValue(propertyKey);
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
