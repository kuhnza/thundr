package com.atomicleopard.webFramework.module.test;

import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;

public class TestInjectionConfiguration implements InjectionConfiguration {
	public boolean loaded = false;

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		loaded = true;
	}
}
