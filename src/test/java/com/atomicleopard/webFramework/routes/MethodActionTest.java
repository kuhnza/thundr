package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import jodd.util.ReflectUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class MethodActionTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldFindClassAndMethod() {
		MethodAction methodAction = new MethodAction("com.atomicleopard.webFramework.routes.FakeController.methodOne");
		assertThat(methodAction.type().equals(FakeController.class), is(true));
		Method expectedMethod = ReflectUtil.findMethod(FakeController.class, "methodOne");
		assertThat(methodAction.method(), is(expectedMethod));
	}

	@Test
	public void shouldThrowActionExceptionWhenMethodCannotBeFound() {
		thrown.expect(ActionException.class);
		thrown.expectMessage("Method thereIsNoMethodCalledThis does not exist");

		new MethodAction("com.atomicleopard.webFramework.routes.FakeController.thereIsNoMethodCalledThis");
	}

	@Test
	public void shouldThrowActionExceptionWhenClassCannotBeFound() {
		thrown.expect(ActionException.class);
		thrown.expectMessage("Controller com.atomicleopard.webFramework.routes.SomeOtherFakeController could not be loaded");

		new MethodAction("com.atomicleopard.webFramework.routes.SomeOtherFakeController.methodOne");
	}

	@Test
	public void shouldInvokeControllerMethod() {
		MethodAction methodAction = new MethodAction("com.atomicleopard.webFramework.routes.FakeController.methodOne");

		FakeController controller = new FakeController();
		Object result = methodAction.invoke(controller, list("Arg 1"));

		assertThat(result, is((Object) "Result: Arg 1"));
		assertThat(controller.invocationCount, is(1));
	}

	@Test
	public void shouldThrowActionExceptionWhenInvokingControllerMethodFailsBecauseOfWrongArgumentNumber() {
		thrown.expect(ActionException.class);
		thrown.expectMessage("Failed to invoke controller method class com.atomicleopard.webFramework.routes.FakeController.methodOne: wrong number of arguments");

		MethodAction methodAction = new MethodAction("com.atomicleopard.webFramework.routes.FakeController.methodOne");

		FakeController controller = new FakeController();
		methodAction.invoke(controller, list());
	}

	@Test
	public void shouldFindMethodParameters() {
		MethodAction methodAction = new MethodAction("com.atomicleopard.webFramework.routes.FakeController.methodOne");
		List<ParameterDescription> parameters = methodAction.parameters();
		assertThat(parameters.size(), is(1));
		assertThat(parameters.get(0).name(), is("argument1"));
		assertThat(String.class.equals(parameters.get(0).type()), is(true));
	}
}
