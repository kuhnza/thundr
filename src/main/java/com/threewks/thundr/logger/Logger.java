package com.threewks.thundr.logger;

import java.util.logging.Level;

public class Logger {
	public static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Web Framework");

	public static final void debug(String format, Object... args) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(String.format(format, args));
		}
	}

	public static final void info(String format, Object... args) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info(String.format(format, args));
		}
	}

	public static final void warn(String format, Object... args) {
		if (logger.isLoggable(Level.WARNING)) {
			logger.warning(String.format(format, args));
		}
	}

	public static final void error(String format, Object... args) {
		if (logger.isLoggable(Level.SEVERE)) {
			logger.severe(String.format(format, args));
		}
	}
}
