package com.zoltu.MovieReleases.server.exceptions;

@SuppressWarnings("serial")
public class AlreadyExistsException extends Exception
{
	public AlreadyExistsException()
	{
		super();
	}
	
	public AlreadyExistsException(String pMessage)
	{
		super(pMessage);
	}
}
