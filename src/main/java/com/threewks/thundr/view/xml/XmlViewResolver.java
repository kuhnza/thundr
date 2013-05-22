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
package com.threewks.thundr.view.xml;

import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;
import jodd.util.MimeTypes;
import jodd.util.StringPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.io.StringWriter;

public class XmlViewResolver implements ViewResolver<XmlView> {

	@Override
	@SuppressWarnings("unchecked")
	public void resolve(HttpServletRequest req, HttpServletResponse resp, XmlView viewResult) {
		Object output = viewResult.getOutput();

		// If this is an exception, wrap it in an exception for display purposes
		if (output instanceof Throwable) {
			output = new XmlExceptionWrapper((Throwable) output);
		}

		try {
			StringWriter sw = new StringWriter();

			Class outputType = output.getClass();
			JAXBContext context = JAXBContext.newInstance(output.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			if (!outputType.isAnnotationPresent(XmlRootElement.class)) {
				// Where root element is missing, substitute with class name
				JAXBElement element = new JAXBElement(new QName(null, outputType.getName()), outputType, output);
				marshaller.marshal(element, sw);
			} else {
				marshaller.marshal(output, sw);
			}

			String xml = sw.toString();
			resp.setContentType(MimeTypes.MIME_APPLICATION_XML);
			resp.setCharacterEncoding(StringPool.UTF_8);
			resp.setContentLength(xml.getBytes().length);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(xml);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to generate XML output for object '%s': %s", output.toString(), e.getMessage());
		}
	}
}
