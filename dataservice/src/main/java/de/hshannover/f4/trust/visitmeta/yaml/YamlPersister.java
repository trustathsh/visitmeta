package de.hshannover.f4.trust.visitmeta.yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class YamlPersister {

	private static final Charset UTF8 = Charset.forName("UTF-8");


	public static void persist(String fileName, Object obj, boolean append, Representer representer, DumperOptions options) throws FileNotFoundException {
		OutputStream outFile = new FileOutputStream(fileName, append);
		Writer outWriter = new OutputStreamWriter(outFile, UTF8);

		Yaml yaml = getYaml(null, representer, options);

		yaml.dump(obj, outWriter);
	}

	public static void persist(String fileName, Object obj, boolean append) throws FileNotFoundException {
		persist(fileName, obj, append, null, null);
	}

	public static void persist(String fileName, Object obj, boolean append, Representer representer) throws FileNotFoundException {
		persist(fileName, obj, append, representer, null);
	}

	public static void persist(String fileName, Object obj, boolean append, DumperOptions options) throws FileNotFoundException {
		persist(fileName, obj, append, null, options);
	}

	public static Object load(String fileName, BaseConstructor constructor) throws FileNotFoundException{
		InputStream  input = new FileInputStream(fileName);

		Yaml yaml = getYaml(constructor, null, null);
		Object data = yaml.load(input);

		return data;
	}

	@SuppressWarnings("unchecked")
	public static <T> T loadAs(String fileName, Class<T> clazz, BaseConstructor constructor) throws FileNotFoundException{
		InputStream  input = new FileInputStream(fileName);

		Yaml yaml = getYaml(constructor, null, null);
		Object data = yaml.loadAs(input, clazz);

		return (T) data;
	}

	public static Iterable<Object> loadAll(String fileName, BaseConstructor constructor) throws FileNotFoundException{
		InputStream  input = new FileInputStream(fileName);

		Yaml yaml = getYaml(constructor, null, null);
		Iterable<Object> data = yaml.loadAll(input);

		return data;
	}

	public static Iterable<Object> loadAll(String fileName) throws FileNotFoundException{
		return loadAll(fileName, null);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadMap(String fileName, BaseConstructor constructor) throws FileNotFoundException {
		return loadAs(fileName, HashMap.class, constructor);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadMap(String fileName) throws FileNotFoundException {
		return loadAs(fileName, HashMap.class, null);
	}

	@SuppressWarnings("unchecked")
	public static Set<Object> loadSet(String fileName, BaseConstructor constructor) throws FileNotFoundException {
		return loadAs(fileName, HashSet.class, constructor);
	}

	@SuppressWarnings("unchecked")
	public static Set<Object> loadSet(String fileName) throws FileNotFoundException {
		return loadAs(fileName, HashSet.class, null);
	}

	private static Yaml getYaml(BaseConstructor constructor, Representer representer, DumperOptions options){
		BaseConstructor yamlConstructor;
		Representer yamlRepresenter;
		DumperOptions yamlOptions;

		if(constructor == null){
			yamlConstructor = new Constructor();
		}else{
			yamlConstructor = constructor;
		}

		if(representer == null){
			yamlRepresenter = new Representer();
		}else{
			yamlRepresenter = representer;
		}

		if(options == null){
			yamlOptions = new DumperOptions();
		}else{
			yamlOptions = options;
		}
		return new Yaml(yamlConstructor, yamlRepresenter, yamlOptions);
	}

}
