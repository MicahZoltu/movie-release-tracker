package com.zoltu.MovieReleases.server.exceptions;

@SuppressWarnings("serial")
public class NotLoggedInException extends Exception
{
	public NotLoggedInException()
	{
		super();
	}
	
	public NotLoggedInException(String pMessage)
	{
		super(pMessage);
	}
}
