package de.hshannover.f4.trust.visitmeta.yaml;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface YamlPersist {

	@Retention(RetentionPolicy.RUNTIME)
	public @interface REQUIRED {

	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface OPTIONAL {

		/**
		 * OPTIONAL required the default field name.
		 * The default field is the default value from the field when nothing is set.
		 * 
		 * @return
		 */
		String value();
	}
}
