package com.zoltu.gae_authentication.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Filters allow us to catch page requests before they are processed and either continue on with the page or send a
 * different response. In this case, we check to see if they are logged in and if not we send them to the login page
 * instead of the requested page. If they are logged in then we let them through to the desired page without touching
 * the request.
 */
public class AuthenticationFilter implements Filter
{
	@Override
	public void destroy()
	{}
	
	@Override
	public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pFilterChain) throws IOException, ServletException
	{
		UserService lUserService = UserServiceFactory.getUserService();
		HttpServletRequest lRequest = (HttpServletRequest) pRequest;
		HttpServletResponse lResponse = (HttpServletResponse) pResponse;
		
		// if the user _isn't_ logged in: redirect them to the login page
		if (!lUserService.isUserLoggedIn())
		{
			String lRedirectUrl = lUserService.createLoginURL(lRequest.getHeader("pageurl"));
			lResponse.setHeader("login", lRedirectUrl);
			lResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		// if the user _is_ logged in: forward them on to their original location (or other filters along the way)
		pFilterChain.doFilter(lRequest, lResponse);
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{}
}
