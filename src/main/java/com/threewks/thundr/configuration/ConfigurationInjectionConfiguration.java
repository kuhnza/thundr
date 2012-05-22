package com.threewks.thundr.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.appengine.api.utils.SystemProperty;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

public class ConfigurationInjectionConfiguration implements InjectionConfiguration {

	private static final String NAMESPACE_SEPARATOR = "%";

    private static final String DEV_APPLICATION_ID = "dev";
	
    private PropertiesLoader propertiesLoader = new PropertiesLoader();
	private String filename;

	public ConfigurationInjectionConfiguration() {
		this("application.properties");
	}

	public ConfigurationInjectionConfiguration(String filename) {
		this.filename = filename;
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		
	    Logger.info("Loading application properties from %s", filename);
		Map<String, String> properties = loadProperties();
		
		String applicationId = getApplicationId();
		Logger.info("Running as %s applicationId", applicationId);
		injectPropertiesBasedOnApplicationId(injectionContext, properties, applicationId);
		
		Logger.debug("Loaded application properties");
	}

	private Map<String, String> loadProperties() {
	    return propertiesLoader.load(filename);
	}
	
	/**
	 * Get the application id of the running application. When running in Development mode
	 * (ie: in a local SDK environment) this simply returns the value "dev". When running
	 * in Production mode, this returns the value of the application id.
	 *   
	 * @return the application id.
	 */
	protected String getApplicationId() {
	    String environment = SystemProperty.environment.get();
	    if (SystemProperty.Environment.Value.Development.value().equals(environment)) {
	        return DEV_APPLICATION_ID;
	    } else {
	        return SystemProperty.applicationId.get();
	    }
	}

	private void injectPropertiesBasedOnApplicationId(UpdatableInjectionContext injectionContext, Map<String, String> properties, String applicationId) {
	    
	    // sort the keys to ensure environment specific values override the base values 
	    List<String> keys = new ArrayList<String>(properties.keySet());
	    Collections.sort(keys);
	    
	    for (String key : keys) {
	        String value = properties.get(key);
	        String baseKey = filter(key, applicationId);
	        if (baseKey != null) {
	            injectionContext.inject(String.class).named(baseKey).as(value);
	        }
	    }
	}
	
	/**
     * Filter a property key given a key and the application id.
     * 
     * Property keys can be namespaced with the application id to make the property
     * specific to that environment. For example the property 'myProperty' can be 
     * namespaced like this:
     * 
     * myProperty%dev
     * myProperty%my-application-id
     * 
     * If the key is not namespaced then the key is returned as is.
     * 
     * If the key is namespaced, and it is namespaced with the given application id
     * then the base key is returned (ie: myProperty). If the namespace does not
     * match the given application id then null is returned. 
     *
     * @param key the property key.
     * @param applicationId the application id.
     * @return the base key name if the key is namespaced with applciation id, null otherwise. 
     */
    protected String filter(String key, String applicationId) {
        
        if (key != null && key.contains(NAMESPACE_SEPARATOR)) {
            String filtered = key.replaceFirst(Pattern.quote(NAMESPACE_SEPARATOR + applicationId), "");
            return filtered.contains(NAMESPACE_SEPARATOR) ? null : filtered;
        }
        
        // key is not namespaced, return as is
        return key;
    }
}
