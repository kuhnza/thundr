package com.threewks.thundr.action.method.bind.http;

import static com.atomicleopard.expressive.Expressive.list;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;

public class HttpBinderTest {
    private HttpBinder binder;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Map<String, String> pathVariables;
    private List<ParameterDescription> parameterDescriptions;
    
    @Before
    public void before() {
        binder = new HttpBinder(new PathVariableBinder());
        
        parameterDescriptions = new ArrayList<ParameterDescription>();
        pathVariables = new HashMap<String, String>();
        
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        
        when(request.getParameterMap()).thenReturn(Collections.<String, String[]> emptyMap());
    }
    
    @Test
    public void shouldBindNullContentType() {
        assertThat(binder.canBind(null), is(true));
    }
    
    @Test
    public void shouldBindValidContentTypes() {
        List<ContentType> validContentTypes = list(ContentType.ApplicationFormUrlEncoded, ContentType.TextHtml, ContentType.TextPlain);
        for (ContentType contentType : ContentType.values()) {
            assertThat(binder.canBind(contentType.value()), is(validContentTypes.contains(contentType)));
        }
    }
    
    @Test
    public void shouldBindToHttpServletRequestParam() {
        parameterDescriptions.add(new ParameterDescription("request", HttpServletRequest.class));
        
        List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);
        assertThat(boundVariables, Matchers.<Object>hasItem(request));
        assertThat(boundVariables.size(), is(1));
    }
    
    @Test
    public void shouldBindToHttpServletResponseParam() {
        parameterDescriptions.add(new ParameterDescription("response", HttpServletResponse.class));
        
        List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);
        assertThat(boundVariables, Matchers.<Object>hasItem(response));
        assertThat(boundVariables.size(), is(1));
    }
    
    @Test
    public void shouldBindToHttpSessionParam() {
        parameterDescriptions.add(new ParameterDescription("session", HttpSession.class));
        when(request.getSession()).thenReturn(session);
        
        List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);
        assertThat(boundVariables, Matchers.<Object>hasItem(session));
        assertThat(boundVariables.size(), is(1));
    }
    
    @Test
    public void shouldBindToRequestDomainParam() {
        parameterDescriptions.add(new ParameterDescription("requestDomain", String.class));
        String serverName = "www.example.com";
        when(request.getServerName()).thenReturn(serverName);
        
        List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);
        assertThat(boundVariables, Matchers.<Object>hasItem(serverName));
        assertThat(boundVariables.size(), is(1));
    }
    
    @Test
    public void shouldBindMultipleParams() {
        parameterDescriptions.add(new ParameterDescription("request", HttpServletRequest.class));
        parameterDescriptions.add(new ParameterDescription("response", HttpServletResponse.class));
        parameterDescriptions.add(new ParameterDescription("session", HttpSession.class));
        when(request.getSession()).thenReturn(session);
        
        List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);
        assertThat(boundVariables, Matchers.<Object>hasItem(request));
        assertThat(boundVariables, Matchers.<Object>hasItem(response));
        assertThat(boundVariables, Matchers.<Object>hasItem(session));
        assertThat(boundVariables.size(), is(3));
    }
    
    @Test
    public void shouldDelegateToPathVariableBinder() {
        PathVariableBinder pathVariableBinder = mock(PathVariableBinder.class);
        List<Object> boundVariables = emptyList();
        when(pathVariableBinder.bindAll(parameterDescriptions, request, response, pathVariables)).thenReturn(boundVariables);
        
        binder = new HttpBinder(pathVariableBinder);
        binder.bindAll(parameterDescriptions, request, response, pathVariables);
        verify(pathVariableBinder).bindAll(parameterDescriptions, request, response, pathVariables);
    }
    
}
