package com.threewks.thundr.profiler;

import java.util.UUID;

public interface Profiler {
	public static final String CategoryAction = "Action";
	public static final String CategoryDatabase = "Database";
	public static final String CategoryDatabaseRead = "DatabaseRead";
	public static final String CategoryDatabaseWrite = "DatabaseWrite";
	public static final String CategoryHttp = "Http";
	public static final String CategoryHttpRequest = "HttpRequest";
	public static final String CategoryHttpResponse = "HttpResponse";
	public static final String CategoryService = "Service";
	public static final String CategoryServiceRequest = "ServiceRequest";
	public static final String CategoryServiceResponse = "ServiceResponse";
	public static final String CategoryBusinessLogic = "BusinessLogic";
	public static final String CategoryView = "View";

	public void beginProfileSession(String data);

	public void endProfileSession();

	public UUID start(String category, String data);

	public void end(UUID eventKey);

	public void end(UUID eventKey, ProfileEventStatus status);

	public ProfileSession getCurrent();

	public <T> T profile(String category, String data, Profilable<T> profilable);
}
