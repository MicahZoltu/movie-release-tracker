package com.zoltu.MovieReleases.server.exceptions;

public class ElementNotFound extends Exception
{
	private static final long serialVersionUID = -3337306165880287393L;	
	
	public ElementNotFound(String pElementName)
	{
		super("Unable to locate " + pElementName + " in the primary item.");
	}
}
