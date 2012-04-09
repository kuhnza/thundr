package scalate;

import org.fusesource.scalate.TemplateEngine;

import com.atomicleopard.webFramework.logger.Logger;
import com.atomicleopard.webFramework.view.scalate.TemplateViewResolver;

/**
 * This is a magic class invoked by by the scalate TemplateEngine on startup.
 * It is significant in that when using precompiled templates, the configuration in here
 * must be applied <strong>at compile time</strong> as well as runtime.
 */
public class Boot {
	private TemplateEngine engine;

	public Boot(TemplateEngine engine) {
		this.engine = engine;
	}

	public void run() {
		Logger.info("Bootstrapping template engine");
		TemplateViewResolver.applyTemplateEngineConfiguration(engine);
	}
}
