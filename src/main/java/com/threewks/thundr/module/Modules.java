package com.threewks.thundr.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

public class Modules {
	private static final int MaxModules = 200;
	private List<Module> modules = new LinkedList<Module>();
	private Map<Module, Boolean> loaded = new HashMap<Module, Boolean>();

	public Modules() {
	}

	List<Module> listModules() {
		return new ArrayList<Module>(modules);
	}

	public Modules addModule(Module module) {
		modules.add(module);
		Logger.debug("Added module %s", module);
		return this;
	}

	public void loadModules(UpdatableInjectionContext injectionContext) {
		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			if (!Boolean.TRUE.equals(loaded.get(module))) {
				module.getConfiguration().configure(injectionContext);
				loaded.put(module, true);
				Logger.info("Loaded module %s", module);
				if (modules.size() > MaxModules) {
					throw new ModuleLoadingException(module, "exceeded the maximum number of allowed modules (%d) - it is likely that you have a module loading loop", MaxModules);
				}
			}
		}
	}
}
