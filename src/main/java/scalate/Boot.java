package scalate;

import java.util.logging.Logger;

import org.fusesource.scalate.TemplateEngine;

import com.atomicleopard.webFramework.WebFrameworkServlet;

/**
 * This is a magic class invoked by by the scalate TemplateEngine on startup.
 * It is significant in that when using precompiled templates, the configuration in here
 * must be applied <strong>at compile time</strong> as well as runtime.
 */
public class Boot {
	public static final Logger logger = Logger.getLogger(Boot.class.getName());
	private TemplateEngine engine;

	public Boot(TemplateEngine engine) {
		this.engine = engine;
	}

	public void run() {
		logger.info("Bootstrapping template engine");
		WebFrameworkServlet.applyTemplateEngineConfiguration(engine);
	}
}
