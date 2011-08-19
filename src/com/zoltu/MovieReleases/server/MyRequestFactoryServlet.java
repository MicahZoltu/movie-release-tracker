package com.zoltu.MovieReleases.server;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.googlecode.objectify.ObjectifyService;

public class MyRequestFactoryServlet extends RequestFactoryServlet
{
	private static final long serialVersionUID = 3977004996453070175L;
	
	/**
	 * This is the one reliable entry point into the server code so do global setup here such as registering Objectify
	 * classes and getting the logged in user's name.
	 */
	static
	{
		ObjectifyService.register(TrackedMovie.class);
		ObjectifyService.register(Movie.class);
	}
	
	public MyRequestFactoryServlet()
	{
		super(new MyExceptionHandler());
	}
	
	private static class MyExceptionHandler implements ExceptionHandler
	{
		@Override
		public ServerFailure createServerFailure(Throwable pThrowable)
		{
			return new ServerFailure(pThrowable.getMessage(), pThrowable.getClass().getName(), getStackTrace(pThrowable), true);
		}
		
		private String getStackTrace(Throwable pThrowable)
		{
			final StringWriter lStringWriter = new StringWriter();
			final PrintWriter lPrintWriter = new PrintWriter(lStringWriter);
			pThrowable.printStackTrace(lPrintWriter);
			return lStringWriter.toString();
		}
	}
}
