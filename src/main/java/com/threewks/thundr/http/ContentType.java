package com.threewks.thundr.http;

/** 
 * Some common ContentType definitions for MIME types.
 * For a world of fun, check out http://www.iana.org/assignments/media-types/index.html
 */
public enum ContentType {
	TextPlain("text/plain"),
	TextHtml("text/html"),
	TextCss("text/css"),
	TextJavascript("text/javascript"),
	ApplicationFormUrlEncoded("application/x-www-form-urlencoded"),
	ApplicationOctetStream("application/octet-stream"),
	ApplicationJson("application/json"),
	ApplicationXml("application/xml"),
	ApplicationXmlDtd("application/xml-dtd"),
	ApplicationSoapXml("application/soap+xml");
	
	private String value;

	private ContentType(String encoding) {
		this.value = encoding;
	}
	
	public String value(){
		return value;
	}
	
	public ContentType from(String encoding){
		for(ContentType contentType : values()){
			if(contentType.value.equalsIgnoreCase(encoding)){
				return contentType;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
