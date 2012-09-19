package com.threewks.thundr.injection;

public class InstanceAlreadyExistsException extends InjectionException {
	private static final long serialVersionUID = 7013626764201680479L;

	public InstanceAlreadyExistsException(String format, Object[] formatArgs) {
		super(format, formatArgs);
	}
}
