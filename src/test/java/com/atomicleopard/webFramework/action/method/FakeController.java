package com.atomicleopard.webFramework.action.method;

public class FakeController {
	public int invocationCount = 0;

	public String methodOne(String argument1) {
		invocationCount++;
		return "Result: " + argument1;
	}
}
