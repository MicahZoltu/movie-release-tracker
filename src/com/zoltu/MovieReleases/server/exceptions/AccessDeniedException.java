package com.zoltu.MovieReleases.server.exceptions;

@SuppressWarnings("serial")
public class AccessDeniedException extends Exception
{
	public AccessDeniedException()
	{
		super();
	}
	
	public AccessDeniedException(String pMessage)
	{
		super(pMessage);
	}
}
