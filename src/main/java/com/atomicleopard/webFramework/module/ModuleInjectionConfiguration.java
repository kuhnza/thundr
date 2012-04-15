package com.atomicleopard.webFramework.module;

import java.util.Map;

import com.atomicleopard.webFramework.configuration.PropertiesLoader;
import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;

public class ModuleInjectionConfiguration implements InjectionConfiguration {
	private PropertiesLoader propertiesLoader = new PropertiesLoader();
	private String filename;

	public ModuleInjectionConfiguration() {
		this("modules.properties");
	}

	public ModuleInjectionConfiguration(String filename) {
		this.filename = filename;
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Modules modules = injectionContext.get(Modules.class);
		loadModules(modules, injectionContext, filename);
	}

	protected void loadModules(Modules modules, UpdatableInjectionContext injectionContext, String filename) {
		Logger.info("Loading modules from %s", filename);
		Map<String, String> properties = propertiesLoader.load(filename);
		if (properties.isEmpty()) {
			throw new ModuleLoadingException("", "you must have an entry for your application configuration in %s", filename);
		}
		for (String key : properties.keySet()) {
			Module module = Module.createModule(key);
			modules.addModule(module);
		}
	}
}
