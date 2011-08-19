package com.zoltu.MovieReleases.server.exceptions;

public class PrimaryItemNotFound extends Exception
{
	private static final long serialVersionUID = 9066621990018400217L;

	public PrimaryItemNotFound()
	{
		super("Unable to find the primary item (item with empty ParentPublishedID).");
	}
}
