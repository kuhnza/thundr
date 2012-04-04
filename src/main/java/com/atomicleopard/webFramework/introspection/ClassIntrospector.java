package com.atomicleopard.webFramework.introspection;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jodd.introspector.ClassDescriptor;
import scala.actors.threadpool.Arrays;

public class ClassIntrospector {

	@SuppressWarnings("unchecked")
	public <T> List<Constructor<T>> listConstructors(Class<T> type) {
		ClassDescriptor classDescriptor = new ClassDescriptor(type, true);
		List<Constructor<T>> ctors = Arrays.asList(classDescriptor.getAllCtors());
		Collections.sort(ctors, new Comparator<Constructor<T>>() {
			@Override
			public int compare(Constructor<T> o1, Constructor<T> o2) {
				Class<?>[] types1 = o1.getParameterTypes();
				Class<?>[] types2 = o2.getParameterTypes();
				int compare = new Integer(types1.length).compareTo(types2.length);
				if (compare == 0) {
					// to keep the outcome consistent, we want to deterministically sort
					for (int i = 0; compare == 0 && i < types1.length; i++) {
						compare = types1[i].getName().compareTo(types2[i].getName());
					}
				}
				return compare;
			}
		});
		return ctors;
	}

}
