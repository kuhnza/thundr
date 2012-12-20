package com.threewks.thundr.action.method;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import jodd.util.ReflectUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.introspection.ParameterDescription;

public class MethodActionTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldFindClassAndMethod() {
		MethodAction methodAction = new MethodAction(FakeController.class, ReflectUtil.findMethod(FakeController.class, "methodOne"),
				Collections.<Annotation, ActionInterceptor<Annotation>> emptyMap());
		assertThat(methodAction.type().equals(FakeController.class), is(true));
		Method expectedMethod = ReflectUtil.findMethod(FakeController.class, "methodOne");
		assertThat(methodAction.method(), is(expectedMethod));
	}

	@Test
	public void shouldInvokeControllerMethod() throws Exception {
		MethodAction methodAction = new MethodAction(FakeController.class, ReflectUtil.findMethod(FakeController.class, "methodOne"),
				Collections.<Annotation, ActionInterceptor<Annotation>> emptyMap());

		FakeController controller = new FakeController();
		Object result = methodAction.invoke(controller, list("Arg 1"));

		assertThat(result, is((Object) "Result: Arg 1"));
		assertThat(controller.invocationCount, is(1));
	}

	/*
	 * @Test
	 * public void shouldThrowActionExceptionWhenInvokingControllerMethodFailsBecauseOfWrongArgumentNumber() throws Exception {
	 * thrown.expect(ActionException.class);
	 * thrown.expectMessage("Failed in class com.threewks.thundr.routes.FakeController.methodOne: wrong number of arguments");
	 * 
	 * MethodAction methodAction = new MethodAction(FakeController.class, ReflectUtil.findMethod(FakeController.class, "methodOne"), Collections.<Annotation, ActionInterceptor<Annotation>>
	 * emptyMap());
	 * 
	 * FakeController controller = new FakeController();
	 * methodAction.invoke(controller, list());
	 * }
	 */

	@Test
	public void shouldFindMethodParameters() {
		MethodAction methodAction = new MethodAction(FakeController.class, ReflectUtil.findMethod(FakeController.class, "methodOne"),
				Collections.<Annotation, ActionInterceptor<Annotation>> emptyMap());
		List<ParameterDescription> parameters = methodAction.parameters();
		assertThat(parameters.size(), is(1));
		assertThat(parameters.get(0).name(), is("argument1"));
		assertThat(String.class.equals(parameters.get(0).classType()), is(true));
	}
}
