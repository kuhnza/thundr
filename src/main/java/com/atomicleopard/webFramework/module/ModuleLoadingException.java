package com.atomicleopard.webFramework.module;

import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.webFramework.exception.BaseException;

public class ModuleLoadingException extends BaseException {

	private static final long serialVersionUID = -2383234783452550560L;

	public ModuleLoadingException(Module module, String format, Object... formatArgs) {
		super("Failed to load module '%s' - " + format, Expressive.<Object> list(module).addItems(formatArgs).toArray());
	}

	public ModuleLoadingException(String modulePackage, String format, Object... formatArgs) {
		super("Failed to load module '%s' - " + format, Expressive.<Object> list(modulePackage).addItems(formatArgs).toArray());
	}

	public ModuleLoadingException(Throwable cause, String modulePackage, String format, Object... formatArgs) {
		super(cause, "Failed to load module '%s' - " + format, Expressive.<Object> list(modulePackage).addItems(formatArgs).toArray());
	}
}
