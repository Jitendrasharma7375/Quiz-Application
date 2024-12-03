package com.smartdocs.question_service.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseSizeFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse) response);

		try {
			chain.doFilter(request, responseWrapper);
			byte[] responseData = responseWrapper.getResponseData();
			response.getOutputStream().write(responseData);
		} finally {
			response.getOutputStream().flush();
		}
	}
}