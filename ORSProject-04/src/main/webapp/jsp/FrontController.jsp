package in.co.sunrays.proj4.controller;

import in.co.sunrays.proj4.controller.ORSView;
import in.co.sunrays.proj4.util.ServletUtility;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Main Controller performs session checking and logging operations before
 * calling any application controller. It prevents any user to access
 * application without login.
 * 
 * 
 * @author Proxy
 * @version 1.0
 * Copyright (c) Proxy
 */
@WebFilter(urlPatterns = { "/doc/*", "/ctl/*" })
public class FrontController implements Filter {

	public void init(FilterConfig conf) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		HttpSession session = request.getSession();

		String uri = request.getRequestURI();
		request.setAttribute("uri", uri);

		if (session.getAttribute("user") == null) {
			request.setAttribute("error", "Your session has been expired. Please Login again!");
			ServletUtility.forward(ORSView.LOGIN_VIEW, request, response);
			return;
		} else {
			chain.doFilter(req, resp);
		}
	}

	public void destroy() {
	}
}