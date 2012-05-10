package com.threewks.thundr.module.test;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class TestInjectionConfiguration implements InjectionConfiguration {
	public boolean loaded = false;

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		loaded = true;
	}
}
