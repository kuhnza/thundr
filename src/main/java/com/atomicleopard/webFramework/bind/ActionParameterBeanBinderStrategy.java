package com.atomicleopard.webFramework.bind;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jodd.bean.BeanTool;
import jodd.bean.BeanUtil;
import scala.actors.threadpool.Arrays;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class ActionParameterBeanBinderStrategy implements ActionParameterBinderStrategy {

	private Map<String, String[]> requestParameters;

	public ActionParameterBeanBinderStrategy(Map<String, String[]> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public void bind(ActionParameter actionParameter, Map<String, Object> bindings) {
		String parameterName = actionParameter.name;
		for (String requestParameter : BeanTool.resolveProperties(requestParameters, false)) {
			if (applies(requestParameter, parameterName)) {
				String[] value = requestParameters.get(requestParameter);
				if (actionParameter.isA(Array.class)) {
					BeanUtil.setPropertyForcedSilent(bindings, requestParameter, value);
				} else if (actionParameter.isA(List.class)) {
					BeanUtil.setPropertyForcedSilent(bindings, requestParameter, new ArrayList(Arrays.asList(value)));
				} else if (actionParameter.isA(Set.class)) {
					BeanUtil.setPropertyForcedSilent(bindings, requestParameter, new HashSet(Arrays.asList(value)));
				} else if (actionParameter.isA(Collection.class)) {
					BeanUtil.setPropertyForcedSilent(bindings, requestParameter, new ArrayList(Arrays.asList(value)));
				} else {
					BeanUtil.setPropertyForcedSilent(bindings, requestParameter, value[0]);
				}
			}
		}
	}

	private boolean applies(String requestParam, String parameterName) {
		return requestParam.equals(parameterName) || requestParam.startsWith(parameterName + ".");
	}
}
