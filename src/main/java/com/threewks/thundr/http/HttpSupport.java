/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.http;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpSupport {
	public static class Header {
		public static final String ContentLength = "Content-Length";
		public static final String ContentType = "Content-Type";
		public static final String ContentDisposition = "Content-Disposition";
		public static final String ContentEncoding = "Content-Encoding";
		public static final String LastModified = "Last-Modified";
		public static final String Expires = "Expires";
		public static final String CacheControl = "Cache-Control";
		public static final String SetCookie = "Set-Cookie";
		public static final String SetCookie2 = "Set-Cookie2";
		public static final String Pragma = "Pragma";
		public static final String Authorization = "Authorization";
		public static final String UserAgent = "User-Agent";
		public static final String Origin = "Origin";
		public static final String Accept = "Accept";
		public static final String AcceptCharset = "Accept-Charset";
		public static final String AcceptEncoding = "Accept-Encoding";
	}

	public static class Authorizations {
		public static final String Basic = "Basic";
	}

	public static String convertToValidUrl(String url) {
		try {
			return new URI(url).toASCIIString();
		} catch (Exception e) {
			return url;
		}
	}

	public static class Status {
		public static final int Continue = 100;
		public static final int SwitchingProtocols = 101;
		public static final int Processing = 102;
		public static final int OK = 200;
		public static final int Created = 201;
		public static final int Accepted = 202;
		public static final int NonAuthoritativeInformation = 203;
		public static final int NoContent = 204;
		public static final int ResetContent = 205;
		public static final int PartialContent = 206;
		public static final int MultiStatus = 207;
		public static final int AlreadyReported = 208;
		public static final int IMUsed = 226;
		public static final int AuthenticationSuccessful = 230;
		public static final int MultipleChoices = 300;
		public static final int MovedPermanently = 301;
		public static final int Found = 302;
		public static final int SeeOther = 303;
		public static final int NotModified = 304;
		public static final int UseProxy = 305;
		public static final int SwitchProxy = 306;
		public static final int TemporaryRedirect = 307;
		public static final int PermanentRedirect = 308;
		public static final int BadRequest = 400;
		public static final int Unauthorized = 401;
		public static final int PaymentRequired = 402;
		public static final int Forbidden = 403;
		public static final int NotFound = 404;
		public static final int MethodNotAllowed = 405;
		public static final int NotAcceptable = 406;
		public static final int ProxyAuthenticationRequired = 407;
		public static final int RequestTimeout = 408;
		public static final int Conflict = 409;
		public static final int Gone = 410;
		public static final int LengthRequired = 411;
		public static final int PreconditionFailed = 412;
		public static final int RequestEntityTooLarge = 413;
		public static final int RequestURITooLong = 414;
		public static final int UnsupportedMediaType = 415;
		public static final int RequestedRangeNotSatisfiable = 416;
		public static final int ExpectationFailed = 417;
		public static final int ImATeapot = 418;
		public static final int EnhanceYourCalm = 420;
		public static final int UnprocessableEntity = 422;
		public static final int Locked = 423;
		public static final int FailedDependency = 424;
		public static final int MethodFailure = 424;
		public static final int UnorderedCollection = 425;
		public static final int UpgradeRequired = 426;
		public static final int PreconditionRequired = 428;
		public static final int TooManyRequests = 429;
		public static final int RequestHeaderFieldsTooLarge = 431;
		public static final int NoResponse = 444;
		public static final int RetryWith = 449;
		public static final int BlockedbyWindowsParentalControls = 450;
		public static final int UnavailableForLegalReasons = 451;
		public static final int Redirect = 451;
		public static final int RequestHeaderTooLarge = 494;
		public static final int CertError = 495;
		public static final int NoCert = 496;
		public static final int HTTPtoHTTPS = 497;
		public static final int ClientClosedRequest = 499;
		public static final int InternalServerError = 500;
		public static final int NotImplemented = 501;
		public static final int BadGateway = 502;
		public static final int ServiceUnavailable = 503;
		public static final int GatewayTimeout = 504;
		public static final int HTTPVersionNotSupported = 505;
		public static final int VariantAlsoNegotiates = 506;
		public static final int InsufficientStorage = 507;
		public static final int LoopDetected = 508;
		public static final int BandwidthLimitExceeded = 509;
		public static final int NotExtended = 510;
		public static final int NetworkAuthenticationRequired = 511;
		public static final int Networkreadtimeouterror = 598;
		public static final int Networkconnecttimeouterror = 599;

		public static String getText(int status) {
			return Status.statusCodesAndReasons.get(status);
		}

		private static final Map<Integer, String> statusCodesAndReasons = new LinkedHashMap<Integer, String>() {{
				put(100, "Continue");
				put(101, "Switching Protocols");
				put(102, "Processing");
				put(200, "OK");
				put(201, "Created");
				put(202, "Accepted");
				put(203, "Non-Authoritative Information");
				put(204, "No Content");
				put(205, "Reset Content");
				put(206, "Partial Content");
				put(207, "Multi-Status");
				put(208, "Already Reported");
				put(226, "IM Used");
				put(230, "Authentication Successful");
				put(300, "Multiple Choices");
				put(301, "Moved Permanently");
				put(302, "Found");
				put(303, "See Other");
				put(304, "Not Modified");
				put(305, "Use Proxy");
				put(306, "Switch Proxy");
				put(307, "Temporary Redirect");
				put(308, "Permanent Redirect");
				put(400, "Bad Request");
				put(401, "Unauthorized");
				put(402, "Payment Required");
				put(403, "Forbidden");
				put(404, "Not Found");
				put(405, "Method Not Allowed");
				put(406, "Not Acceptable");
				put(407, "Proxy Authentication Required");
				put(408, "Request Timeout");
				put(409, "Conflict");
				put(410, "Gone");
				put(411, "Length Required");
				put(412, "Precondition Failed");
				put(413, "Request Entity Too Large");
				put(414, "Request-URI Too Long");
				put(415, "Unsupported Media Type");
				put(416, "Requested Range Not Satisfiable");
				put(417, "Expectation Failed");
				put(418, "I'm a teapot");
				put(420, "Enhance Your Calm");
				put(422, "Unprocessable Entity");
				put(423, "Locked");
				put(424, "Failed Dependency");
				put(424, "Method Failure");
				put(425, "Unordered Collection");
				put(426, "Upgrade Required");
				put(428, "Precondition Required");
				put(429, "Too Many Requests");
				put(431, "Request Header Fields Too Large");
				put(444, "No Response");
				put(449, "Retry With");
				put(450, "Blocked by Windows Parental Controls");
				put(451, "Unavailable For Legal Reasons");
				put(451, "Redirect");
				put(494, "Request Header Too Large");
				put(495, "Cert Error");
				put(496, "No Cert");
				put(497, "HTTP to HTTPS");
				put(499, "Client Closed Request");
				put(500, "Internal Server Error");
				put(501, "Not Implemented");
				put(502, "Bad Gateway");
				put(503, "Service Unavailable");
				put(504, "Gateway Timeout");
				put(505, "HTTP Version Not Supported");
				put(506, "Variant Also Negotiates");
				put(507, "Insufficient Storage");
				put(508, "Loop Detected");
				put(509, "Bandwidth Limit Exceeded");
				put(510, "Not Extended");
				put(511, "Network Authentication Required");
				put(598, "Network read timeout error");
				put(599, "Network connect timeout error");
			}
			private static final long serialVersionUID = 1134413179033027409L;
		};
	}
}
