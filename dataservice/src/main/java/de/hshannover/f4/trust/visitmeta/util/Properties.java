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

	public Object getValue(String propertyKey) {
		return mApplicationConfigs.get(propertyKey);
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
	}

	@Override
	public String toString(){
		return mApplicationConfigs.toString();
	}
}
