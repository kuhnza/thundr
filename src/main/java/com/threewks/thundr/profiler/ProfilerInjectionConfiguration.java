package com.threewks.thundr.profiler;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ProfilerInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		injectionContext.inject(Profiler.class).as(new NoProfiler());
	}
}
