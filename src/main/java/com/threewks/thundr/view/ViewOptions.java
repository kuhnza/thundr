package com.threewks.thundr.view;

import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.HttpSupport;
import jodd.util.StringPool;

import javax.servlet.http.HttpServletResponse;

public class ViewOptions {

	public static ViewOptions Default = new ViewOptions();

	private String characterEncoding = StringPool.UTF_8;
	private String contentType = ContentType.TextHtml.value();
	private int status = HttpSupport.Status.OK;

	private ViewOptions() {}

	public ViewOptions withCharacterEncoding(String characterEncoding) {
		ViewOptions copy = copy() ;
		copy.characterEncoding = characterEncoding;
		return copy;
	}

	public ViewOptions withContentType(String contentType) {
		ViewOptions copy = copy() ;
		copy.contentType = contentType;
		return copy;
	}

	public ViewOptions withStatus(int status) {
		ViewOptions copy = copy() ;
		copy.status = status;
		return copy;
	}

	public void apply(HttpServletResponse resp) {
		resp.setCharacterEncoding(this.characterEncoding);
		resp.setContentType(this.contentType);
		resp.setStatus(this.status);
	}

	ViewOptions copy() {
		ViewOptions copy = new ViewOptions();
		copy.characterEncoding = this.characterEncoding;
		copy.contentType = this.contentType;
		copy.status = this.status;
		return copy;
	}
}
