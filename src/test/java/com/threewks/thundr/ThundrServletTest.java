package com.threewks.thundr;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.threewks.thundr.injection.DefaultInjectionConfiguration;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.InjectionContextImpl;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.profiler.NoProfiler;
import com.threewks.thundr.profiler.Profiler;
import com.threewks.thundr.route.RouteType;
import com.threewks.thundr.route.Routes;
import com.threewks.thundr.test.TestSupport;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.test.mock.servlet.MockServletConfig;
import com.threewks.thundr.test.mock.servlet.MockServletContext;
import com.threewks.thundr.view.ViewResolver;
import com.threewks.thundr.view.ViewResolverRegistry;

public class ThundrServletTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private ThundrServlet servlet = new ThundrServlet();
	private UpdatableInjectionContext injectionContext;
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private Routes routes = mock(Routes.class);
	private ViewResolverRegistry viewResolverRegistry;

	@Before
	public void before() {
		injectionContext = new InjectionContextImpl();
		setInjectionContextIntoServlet(injectionContext);

		when(routes.invoke(anyString(), Mockito.any(RouteType.class), Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn("View Name");
		injectionContext.inject(routes).as(Routes.class);
		injectionContext.inject(new NoProfiler()).as(Profiler.class);

		viewResolverRegistry = new ViewResolverRegistry();
		injectionContext.inject(viewResolverRegistry).as(ViewResolverRegistry.class);
	}

	@SuppressWarnings("serial")
	@Test
	public void shouldInitializeInjectionContextOnServletInit() throws ServletException {
		ServletContext servletContext = new MockServletContext();
		ServletConfig config = new MockServletConfig(servletContext);
		final InjectionConfiguration injectionConfiguration = mock(InjectionConfiguration.class);
		ThundrServlet servlet = new ThundrServlet() {
			protected InjectionConfiguration getInjectionConfigInstance(ServletContext servletContext) {
				return injectionConfiguration;
			};
		};
		servlet.init(config);
		UpdatableInjectionContext injectionContext = getInjectionContextFromServlet(servlet);
		assertThat(injectionContext, is(notNullValue()));
		assertThat(injectionContext.get(ServletContext.class), is(servletContext));

		verify(injectionConfiguration).configure(injectionContext);
	}

	@SuppressWarnings("serial")
	@Test
	public void shouldWrapExceptionInServletException() throws ServletException {
		thrown.expect(ServletException.class);

		ServletContext servletContext = new MockServletContext();
		ServletConfig config = new MockServletConfig(servletContext);
		ThundrServlet servlet = new ThundrServlet() {
			protected InjectionConfiguration getInjectionConfigInstance(ServletContext servletContext) {
				throw new RuntimeException("Expected");
			};
		};
		servlet.init(config);
	}

	@Test
	public void shouldUseApplicationDefaultConfiguration() {
		ServletContext servletContext = new MockServletContext();
		assertThat(servlet.getInjectionConfigInstance(servletContext), is(DefaultInjectionConfiguration.class));
	}

	@Test
	public void shouldApplyGetRouteWhenDoGet() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		servlet = spy(servlet);
		servlet.doGet(req, resp);
		verify(servlet).applyRoute(RouteType.GET, req, resp);
	}

	@Test
	public void shouldApplyPostRouteWhenDoPost() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		servlet = spy(servlet);
		servlet.doPost(req, resp);
		verify(servlet).applyRoute(RouteType.POST, req, resp);
	}

	@Test
	public void shouldApplyPutRouteWhenDoPostWithPutMethodParameter() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.parameter("_method", "pUT");
		servlet = spy(servlet);
		servlet.doPost(req, resp);
		verify(servlet).applyRoute(RouteType.PUT, req, resp);
	}

	@Test
	public void shouldApplyDeleteRouteWhenDoPostWithDeleteMethodParameter() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.parameter("_method", "dEleTE");
		servlet = spy(servlet);
		servlet.doPost(req, resp);
		verify(servlet).applyRoute(RouteType.DELETE, req, resp);
	}

	@Test
	public void shouldApplyPutRouteWhenDoPut() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		servlet = spy(servlet);
		servlet.doPut(req, resp);
		verify(servlet).applyRoute(RouteType.PUT, req, resp);
	}

	@Test
	public void shouldApplyDeleteRouteWhenDoDelete() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		servlet = spy(servlet);
		servlet.doDelete(req, resp);
		verify(servlet).applyRoute(RouteType.DELETE, req, resp);
	}

	@Test
	public void shouldFindViewForResolvedRoute() throws ServletException, IOException {
		viewResolverRegistry.addResolver(String.class, new ViewResolver<String>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, String viewResult) {
				resp.setStatus(123);
			}
		});

		servlet.applyRoute(RouteType.GET, new MockHttpServletRequest("/get/"), resp);
		assertThat(resp.status(), is(123));
	}

	@Test
	public void shouldNotResolveViewWhenNullViewResultReturned() throws ServletException, IOException {
		when(routes.invoke(anyString(), Mockito.any(RouteType.class), Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn(null);

		viewResolverRegistry.addResolver(Object.class, new ViewResolver<Object>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, Object viewResult) {
				resp.setStatus(123);
			}
		});

		servlet.applyRoute(RouteType.GET, new MockHttpServletRequest("/get/"), resp);
		assertThat(resp.status(), is(-1));
	}

	@Test
	public void shouldCatchExceptionsFromViewResolversAndResolveExceptionWithExceptionView() throws ServletException, IOException {
		when(routes.invoke(anyString(), Mockito.any(RouteType.class), Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn("View Name");

		viewResolverRegistry.addResolver(String.class, new ViewResolver<String>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, String viewResult) {
				throw new RuntimeException("Intentional Exception");
			}
		});
		viewResolverRegistry.addResolver(Exception.class, new ViewResolver<Exception>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, Exception viewResult) {
				resp.setStatus(5678);
			}
		});

		servlet.applyRoute(RouteType.GET, new MockHttpServletRequest("/get/"), resp);
		assertThat(resp.status(), is(5678));
	}

	@Test
	public void shouldCatchExceptionsFromViewResolversButDoNothingWhenResponseAlreadyCommitted() throws ServletException, IOException {
		when(routes.invoke(anyString(), Mockito.any(RouteType.class), Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenThrow(
				new RuntimeException("Expected exception"));

		viewResolverRegistry.addResolver(Exception.class, new ViewResolver<Exception>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, Exception viewResult) {
				resp.setStatus(5678);
			}
		});

		resp.sendError(1234);
		servlet.applyRoute(RouteType.GET, new MockHttpServletRequest("/get/"), resp);
		assertThat(resp.status(), is(1234));
	}

	private void setInjectionContextIntoServlet(UpdatableInjectionContext injectionContext) {
		TestSupport.setField(servlet, "injectionContext", injectionContext);
	}

	private UpdatableInjectionContext getInjectionContextFromServlet(ThundrServlet servlet) {
		return TestSupport.getField(servlet, "injectionContext");
	}
}
