package com.atomicleopard.webFramework.routes;

public class FakeController {
	public int invocationCount = 0;

	public String methodOne(String argument1) {
		invocationCount++;
		return "Result: " + argument1;
	}
}
