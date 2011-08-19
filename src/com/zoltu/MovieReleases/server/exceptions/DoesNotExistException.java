package com.zoltu.MovieReleases.server.exceptions;

@SuppressWarnings("serial")
public class DoesNotExistException extends Exception
{
	public DoesNotExistException()
	{
		super();
	}
	
	public DoesNotExistException(String pMessage)
	{
		super(pMessage);
	}
}
