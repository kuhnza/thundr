/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.view.jsp;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.el.BeanELResolver;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

import jodd.introspector.ClassDescriptor;

/**
 * A more useful kind of bean.
 * Getters look like this: public PropertyType property()
 * Setters look like this: public Type property(PropertyType property)
 * 
 * This is a copy of the {@link BeanELResolver} class with some slight modifications allowing us to use methods other than
 * the get/set variants. Unfortunately the {@link BeanELResolver} isn't extensible enough to modify just the methods it uses.
 */
public class AwesomeBeanElResolver extends BeanELResolver {

	static private class BPSoftReference extends SoftReference<BeanProperties> {
		final Class<?> key;

		BPSoftReference(Class<?> key, BeanProperties beanProperties, ReferenceQueue<BeanProperties> refQ) {
			super(beanProperties, refQ);
			this.key = key;
		}
	}

	static private class SoftConcurrentHashMap extends ConcurrentHashMap<Class<?>, BeanProperties> {
		private static final long serialVersionUID = 1806111011201009188L;
		private static final int CACHE_INIT_SIZE = 1024;
		private ConcurrentHashMap<Class<?>, BPSoftReference> map = new ConcurrentHashMap<Class<?>, BPSoftReference>(CACHE_INIT_SIZE);
		private ReferenceQueue<BeanProperties> refQ = new ReferenceQueue<BeanProperties>();

		// Remove map entries that have been placed on the queue by GC.
		private void cleanup() {
			BPSoftReference BPRef = null;
			while ((BPRef = (BPSoftReference) refQ.poll()) != null) {
				map.remove(BPRef.key);
			}
		}

		@Override
		public BeanProperties put(Class<?> key, BeanProperties value) {
			cleanup();
			BPSoftReference prev = map.put(key, new BPSoftReference(key, value, refQ));
			return prev == null ? null : prev.get();
		}

		@Override
		public BeanProperties putIfAbsent(Class<?> key, BeanProperties value) {
			cleanup();
			BPSoftReference prev = map.putIfAbsent(key, new BPSoftReference(key, value, refQ));
			return prev == null ? null : prev.get();
		}

		@Override
		public BeanProperties get(Object key) {
			cleanup();
			BPSoftReference BPRef = map.get(key);
			if (BPRef == null) {
				return null;
			}
			if (BPRef.get() == null) {
				// value has been garbage collected, remove entry in map
				map.remove(key);
				return null;
			}
			return BPRef.get();
		}
	}

	private boolean isReadOnly;

	private static final SoftConcurrentHashMap properties = new SoftConcurrentHashMap();

	/*
	 * Defines a property for a bean.
	 */
	protected final static class BeanProperty {

		private Method readMethod;
		private Method writeMethod;
		private PropertyDescriptor descriptor;

		public BeanProperty(Class<?> baseClass, PropertyDescriptor descriptor) {
			this.descriptor = descriptor;
			readMethod = getMethod(baseClass, descriptor.getReadMethod());
			writeMethod = getMethod(baseClass, descriptor.getWriteMethod());
		}

		@SuppressWarnings("rawtypes")
		public Class getPropertyType() {
			return descriptor.getPropertyType();
		}

		public boolean isReadOnly() {
			return getWriteMethod() == null;
		}

		public Method getReadMethod() {
			return readMethod;
		}

		public Method getWriteMethod() {
			return writeMethod;
		}
	}

	/*
	 * Defines the properties for a bean.
	 */
	protected final static class BeanProperties {

		private final Map<String, BeanProperty> propertyMap = new HashMap<String, BeanProperty>();

		public BeanProperties(Class<?> baseClass) {
			try {
				ClassDescriptor cd = new ClassDescriptor(baseClass, false);
				Method[] allMethods = cd.getAllMethods();
				for (Method method : allMethods) {
					if (method.getParameterTypes().length == 0 && !void.class.equals(method.getReturnType()) && !Void.class.equals(method.getReturnType())) {
						// anything that takes no variables looks like a readMethod!
						String propertyName = method.getName();
						Method readMethod = cd.getMethod(propertyName, new Class[0]);
						// if there's a read method - look for a write method
						Method writeMethod = cd.getMethod(propertyName, new Class[] { readMethod.getReturnType() });
						PropertyDescriptor pd = new PropertyDescriptor(propertyName, readMethod, writeMethod);
						propertyMap.put(pd.getName(), new BeanProperty(baseClass, pd));
					}
				}
			} catch (Exception ie) {
				throw new ELException(ie);
			}
		}

		public BeanProperty getBeanProperty(String property) {
			return propertyMap.get(property);
		}
	}

	/**
	 * Creates a new read/write <code>BeanELResolver</code>.
	 */
	public AwesomeBeanElResolver() {
		this.isReadOnly = false;
	}

	/**
	 * Creates a new <code>BeanELResolver</code> whose read-only status is
	 * determined by the given parameter.
	 * 
	 * @param isReadOnly
	 *            <code>true</code> if this resolver cannot modify
	 *            beans; <code>false</code> otherwise.
	 */
	public AwesomeBeanElResolver(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/**
	 * If the base object is not <code>null</code>, returns the most
	 * general acceptable type that can be set on this bean property.
	 * 
	 * <p>
	 * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object must be set to <code>true</code> by this resolver, before returning. If
	 * this property is not <code>true</code> after this method is called, the caller should ignore the return value.
	 * </p>
	 * 
	 * <p>
	 * The provided property will first be coerced to a <code>String</code>. If there is a <code>BeanInfoProperty</code> for this property and there were no errors retrieving it, the
	 * <code>propertyType</code> of the <code>propertyDescriptor</code> is returned. Otherwise, a <code>PropertyNotFoundException</code> is thrown.
	 * </p>
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean to analyze.
	 * @param property
	 *            The name of the property to analyze. Will be coerced to
	 *            a <code>String</code>.
	 * @return If the <code>propertyResolved</code> property of <code>ELContext</code> was set to <code>true</code>, then
	 *         the most general acceptable type; otherwise undefined.
	 * @throws NullPointerException
	 *             if context is <code>null</code>
	 * @throws PropertyNotFoundException
	 *             if <code>base</code> is not <code>null</code> and the specified property does not exist
	 *             or is not readable.
	 * @throws ELException
	 *             if an exception was thrown while performing
	 *             the property or variable resolution. The thrown exception
	 *             must be included as the cause property of this exception, if
	 *             available.
	 */
	public Class<?> getType(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null) {
			return null;
		}

		BeanProperty bp = getBeanProperty(context, base, property);
		context.setPropertyResolved(true);
		return bp.getPropertyType();
	}

	/**
	 * If the base object is not <code>null</code>, returns the current
	 * value of the given property on this bean.
	 * 
	 * <p>
	 * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object must be set to <code>true</code> by this resolver, before returning. If
	 * this property is not <code>true</code> after this method is called, the caller should ignore the return value.
	 * </p>
	 * 
	 * <p>
	 * The provided property name will first be coerced to a <code>String</code>. If the property is a readable property of the base object, as per the JavaBeans specification, then return the result
	 * of the getter call. If the getter throws an exception, it is propagated to the caller. If the property is not found or is not readable, a <code>PropertyNotFoundException</code> is thrown.
	 * </p>
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean on which to get the property.
	 * @param property
	 *            The name of the property to get. Will be coerced to
	 *            a <code>String</code>.
	 * @return If the <code>propertyResolved</code> property of <code>ELContext</code> was set to <code>true</code>, then
	 *         the value of the given property. Otherwise, undefined.
	 * @throws NullPointerException
	 *             if context is <code>null</code>.
	 * @throws PropertyNotFoundException
	 *             if <code>base</code> is not <code>null</code> and the specified property does not exist
	 *             or is not readable.
	 * @throws ELException
	 *             if an exception was thrown while performing
	 *             the property or variable resolution. The thrown exception
	 *             must be included as the cause property of this exception, if
	 *             available.
	 */
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}

		Object value = null;
		if (base != null && property != null) {
			BeanProperty bp = getBeanProperty(context, base, property);
			Method method = bp == null ? null : bp.getReadMethod();
			if (method != null) {
				try {
					value = method.invoke(base, new Object[0]);
					context.setPropertyResolved(true);
				} catch (ELException ex) {
					throw ex;
				} catch (InvocationTargetException ite) {
					throw new ELException(ite.getCause());
				} catch (Exception ex) {
					throw new ELException(ex);
				}
			}
		}
		return value;
	}

	/**
	 * If the base object is not <code>null</code>, attempts to set the
	 * value of the given property on this bean.
	 * 
	 * <p>
	 * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object must be set to <code>true</code> by this resolver, before returning. If
	 * this property is not <code>true</code> after this method is called, the caller can safely assume no value was set.
	 * </p>
	 * 
	 * <p>
	 * If this resolver was constructed in read-only mode, this method will always throw <code>PropertyNotWritableException</code>.
	 * </p>
	 * 
	 * <p>
	 * The provided property name will first be coerced to a <code>String</code>. If property is a writable property of <code>base</code> (as per the JavaBeans Specification), the setter method is
	 * called (passing <code>value</code>). If the property exists but does not have a setter, then a <code>PropertyNotFoundException</code> is thrown. If the property does not exist, a
	 * <code>PropertyNotFoundException</code> is thrown.
	 * </p>
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean on which to set the property.
	 * @param property
	 *            The name of the property to set. Will be coerced to
	 *            a <code>String</code>.
	 * @param val
	 *            The value to be associated with the specified key.
	 * @throws NullPointerException
	 *             if context is <code>null</code>.
	 * @throws PropertyNotFoundException
	 *             if <code>base</code> is not <code>null</code> and the specified property does not exist.
	 * @throws PropertyNotWritableException
	 *             if this resolver was constructed
	 *             in read-only mode, or if there is no setter for the property.
	 * @throws ELException
	 *             if an exception was thrown while performing
	 *             the property or variable resolution. The thrown exception
	 *             must be included as the cause property of this exception, if
	 *             available.
	 */
	public void setValue(ELContext context, Object base, Object property, Object val) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null) {
			return;
		}

		if (isReadOnly) {
			throw new PropertyNotWritableException(String.format("Cannot write to %s", base.getClass().getName()));
		}

		BeanProperty bp = getBeanProperty(context, base, property);
		Method method = bp.getWriteMethod();
		if (method == null) {
			throw new PropertyNotWritableException(String.format("Property '%s' not writable on %s", property.toString(), base.getClass().getName()));
		}

		try {
			method.invoke(base, new Object[] { val });
			context.setPropertyResolved(true);
		} catch (ELException ex) {
			throw ex;
		} catch (InvocationTargetException ite) {
			throw new ELException(ite.getCause());
		} catch (Exception ex) {
			if (null == val) {
				val = "null";
			}
			String message = String.format("Failed to write to '%s' on %s with value %s", property.toString(), base.getClass().getName(), val);
			throw new ELException(message, ex);
		}
	}

	/**
	 * If the base object is not <code>null</code>, invoke the method, with
	 * the given parameters on this bean. The return value from the method
	 * is returned.
	 * 
	 * <p>
	 * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object must be set to <code>true</code> by this resolver, before returning. If
	 * this property is not <code>true</code> after this method is called, the caller should ignore the return value.
	 * </p>
	 * 
	 * <p>
	 * The provided method object will first be coerced to a <code>String</code>. The methods in the bean is then examined and an attempt will be made to select one for invocation. If no suitable can
	 * be found, a <code>MethodNotFoundException</code> is thrown.
	 * 
	 * If the given paramTypes is not <code>null</code>, select the method with the given name and parameter types.
	 * 
	 * Else select the method with the given name that has the same number of parameters. If there are more than one such method, the method selection process is undefined.
	 * 
	 * Else select the method with the given name that takes a variable number of arguments.
	 * 
	 * Note the resolution for overloaded methods will likely be clarified in a future version of the spec.
	 * 
	 * The provide parameters are coerced to the correcponding parameter types of the method, and the method is then invoked.
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean on which to invoke the method
	 * @param method
	 *            The simple name of the method to invoke.
	 *            Will be coerced to a <code>String</code>. If method is
	 *            "&lt;init&gt;"or "&lt;clinit&gt;" a MethodNotFoundException is
	 *            thrown.
	 * @param paramTypes
	 *            An array of Class objects identifying the
	 *            method's formal parameter types, in declared order.
	 *            Use an empty array if the method has no parameters.
	 *            Can be <code>null</code>, in which case the method's formal
	 *            parameter types are assumed to be unknown.
	 * @param params
	 *            The parameters to pass to the method, or <code>null</code> if no parameters.
	 * @return The result of the method invocation (<code>null</code> if
	 *         the method has a <code>void</code> return type).
	 * @throws MethodNotFoundException
	 *             if no suitable method can be found.
	 * @throws ELException
	 *             if an exception was thrown while performing
	 *             (base, method) resolution. The thrown exception must be
	 *             included as the cause property of this exception, if
	 *             available. If the exception thrown is an <code>InvocationTargetException</code>, extract its <code>cause</code> and pass it to the <code>ELException</code> constructor.
	 * @since EL 2.2
	 */

	public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {

		if (base == null || method == null) {
			return null;
		}
		Method m = findMethod(base, method.toString(), paramTypes, params);
		Object ret = invokeMethod(m, base, params);
		context.setPropertyResolved(true);
		return ret;
	}

	/**
	 * If the base object is not <code>null</code>, returns whether a call
	 * to {@link #setValue} will always fail.
	 * 
	 * <p>
	 * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object must be set to <code>true</code> by this resolver, before returning. If
	 * this property is not <code>true</code> after this method is called, the caller can safely assume no value was set.
	 * </p>
	 * 
	 * <p>
	 * If this resolver was constructed in read-only mode, this method will always return <code>true</code>.
	 * </p>
	 * 
	 * <p>
	 * The provided property name will first be coerced to a <code>String</code>. If property is a writable property of <code>base</code>, <code>false</code> is returned. If the property is found but
	 * is not writable, <code>true</code> is returned. If the property is not found, a <code>PropertyNotFoundException</code> is thrown.
	 * </p>
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean to analyze.
	 * @param property
	 *            The name of the property to analyzed. Will be coerced to
	 *            a <code>String</code>.
	 * @return If the <code>propertyResolved</code> property of <code>ELContext</code> was set to <code>true</code>, then <code>true</code> if calling the <code>setValue</code> method
	 *         will always fail or <code>false</code> if it is possible that
	 *         such a call may succeed; otherwise undefined.
	 * @throws NullPointerException
	 *             if context is <code>null</code>
	 * @throws PropertyNotFoundException
	 *             if <code>base</code> is not <code>null</code> and the specified property does not exist.
	 * @throws ELException
	 *             if an exception was thrown while performing
	 *             the property or variable resolution. The thrown exception
	 *             must be included as the cause property of this exception, if
	 *             available.
	 */
	public boolean isReadOnly(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null) {
			return false;
		}

		context.setPropertyResolved(true);
		if (isReadOnly) {
			return true;
		}

		BeanProperty bp = getBeanProperty(context, base, property);
		return bp.isReadOnly();
	}

	/**
	 * If the base object is not <code>null</code>, returns an <code>Iterator</code> containing the set of JavaBeans properties
	 * available on the given object. Otherwise, returns <code>null</code>.
	 * 
	 * <p>
	 * The <code>Iterator</code> returned must contain zero or more instances of {@link java.beans.FeatureDescriptor}. Each info object contains information about a property in the bean, as obtained
	 * by calling the <code>BeanInfo.getPropertyDescriptors</code> method. The <code>FeatureDescriptor</code> is initialized using the same fields as are present in the <code>PropertyDescriptor</code>
	 * , with the additional required named attributes "<code>type</code>" and "<code>resolvableAtDesignTime</code>" set as follows:
	 * <dl>
	 * <li>{@link ELResolver#TYPE} - The runtime type of the property, from <code>PropertyDescriptor.getPropertyType()</code>.</li>
	 * <li>{@link ELResolver#RESOLVABLE_AT_DESIGN_TIME} - <code>true</code>.</li>
	 * </dl>
	 * </p>
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean to analyze.
	 * @return An <code>Iterator</code> containing zero or more <code>FeatureDescriptor</code> objects, each representing a property
	 *         on this bean, or <code>null</code> if the <code>base</code> object is <code>null</code>.
	 */
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		if (base == null) {
			return null;
		}

		BeanInfo info = null;
		try {
			info = Introspector.getBeanInfo(base.getClass());
		} catch (Exception ex) {
		}
		if (info == null) {
			return null;
		}
		ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>(info.getPropertyDescriptors().length);
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			pd.setValue("type", pd.getPropertyType());
			pd.setValue("resolvableAtDesignTime", Boolean.TRUE);
			list.add(pd);
		}
		return list.iterator();
	}

	/**
	 * If the base object is not <code>null</code>, returns the most
	 * general type that this resolver accepts for the <code>property</code> argument. Otherwise, returns <code>null</code>.
	 * 
	 * <p>
	 * Assuming the base is not <code>null</code>, this method will always return <code>Object.class</code>. This is because any object is accepted as a key and is coerced into a string.
	 * </p>
	 * 
	 * @param context
	 *            The context of this evaluation.
	 * @param base
	 *            The bean to analyze.
	 * @return <code>null</code> if base is <code>null</code>; otherwise <code>Object.class</code>.
	 */
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base == null) {
			return null;
		}

		return Object.class;
	}

	/*
	 * Get a public method form a public class or interface of a given method.
	 * Note that if a PropertyDescriptor is obtained for a non-public class that
	 * implements a public interface, the read/write methods will be for the
	 * class, and therefore inaccessible. To correct this, a version of the
	 * same method must be found in a superclass or interface.
	 */

	static private Method getMethod(Class<?> cl, Method method) {

		if (method == null) {
			return null;
		}

		if (Modifier.isPublic(cl.getModifiers())) {
			return method;
		}
		Class<?>[] interfaces = cl.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			Class<?> c = interfaces[i];
			Method m = null;
			try {
				m = c.getMethod(method.getName(), method.getParameterTypes());
				c = m.getDeclaringClass();
				if ((m = getMethod(c, m)) != null)
					return m;
			} catch (NoSuchMethodException ex) {
			}
		}
		Class<?> c = cl.getSuperclass();
		if (c != null) {
			Method m = null;
			try {
				m = c.getMethod(method.getName(), method.getParameterTypes());
				c = m.getDeclaringClass();
				if ((m = getMethod(c, m)) != null)
					return m;
			} catch (NoSuchMethodException ex) {
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private BeanProperty getBeanProperty(ELContext context, Object base, Object prop) {
		String property = prop.toString();
		Class baseClass = base.getClass();
		BeanProperties bps = properties.get(baseClass);
		if (bps == null) {
			bps = new BeanProperties(baseClass);
			properties.put(baseClass, bps);
		}
		BeanProperty bp = bps.getBeanProperty(property);
		return bp;
	}

	private Method findMethod(Object base, String method, Class<?>[] paramTypes, Object[] params) {
		Class<?> beanClass = base.getClass();
		if (paramTypes != null) {
			try {
				return beanClass.getMethod(method, paramTypes);
			} catch (java.lang.NoSuchMethodException ex) {
				throw new MethodNotFoundException(ex);
			}
		}

		int paramCount = (params == null) ? 0 : params.length;
		for (Method m : base.getClass().getMethods()) {
			if (m.getName().equals(method) && (m.isVarArgs() || m.getParameterTypes().length == paramCount)) {
				return m;
			}
		}
		throw new MethodNotFoundException("Method " + method + " not found");
	}

	static private ExpressionFactory expressionFactory;

	static private ExpressionFactory getExpressionFactory() {
		if (expressionFactory == null) {
			expressionFactory = ExpressionFactory.newInstance();
		}
		return expressionFactory;
	}

	@SuppressWarnings("rawtypes")
	private Object invokeMethod(Method m, Object base, Object[] params) {
		Class[] parameterTypes = m.getParameterTypes();
		Object[] parameters = null;
		if (parameterTypes.length > 0) {
			ExpressionFactory exprFactory = getExpressionFactory();
			if (m.isVarArgs()) {
				// TODO
			} else {
				parameters = new Object[parameterTypes.length];
				for (int i = 0; i < parameterTypes.length; i++) {
					parameters[i] = exprFactory.coerceToType(params[i], parameterTypes[i]);
				}
			}
		}
		try {
			return m.invoke(base, parameters);
		} catch (IllegalAccessException iae) {
			throw new ELException(iae);
		} catch (InvocationTargetException ite) {
			throw new ELException(ite.getCause());
		}
	}
}
