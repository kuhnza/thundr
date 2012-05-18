package com.threewks.thundr.view.jsp;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

public class RegisterElResolverServletContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			// horrible hack to prevent an NPE further down.
			Class.forName("org.apache.jasper.compiler.JspRuntimeContext");
		} catch (ClassNotFoundException e) {
		}
		
		ServletContext context = sce.getServletContext();
		JspApplicationContext jspContext = JspFactory.getDefaultFactory().getJspApplicationContext(context);
		jspContext.addELResolver(new AwesomeBeanElResolver());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Noop
	}
}
