package com.zoltu.MovieReleases.shared;

public interface RequestFactory extends com.google.web.bindery.requestfactory.shared.RequestFactory
{
	public MovieRequestContext getMovieRequestContext();
	public TrackedMovieRequestContext getTrackedMovieRequestContext();
}
