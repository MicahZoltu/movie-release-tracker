package com.zoltu.MovieReleases.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.zoltu.MovieReleases.server.TrackedMovie;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy.OrderBy;

@Service(TrackedMovie.class)
public interface TrackedMovieRequestContext extends RequestContext
{
	/**
	 * Save this entity to the datastore.
	 */
	// public InstanceRequest<TrackedMovieProxy, Void> fSave();
	
	/**
	 * Delete this TrackedMovie from the datastore.
	 */
	// public InstanceRequest<TrackedMovieProxy, Void> fDelete();
	/**
	 * Get movies the currently logged in user is tracking.
	 * 
	 * @param pOrderBy
	 *            The field you want the results to be sorted by.
	 **/
	public Request<List<TrackedMovieProxy>> getPersonalMovies(OrderBy orderBy);
	
	/**
	 * Start tracking the given movie.
	 * 
	 * @param pImdbId
	 *            A valid IMDB ID.
	 * @return The new TrackedMovie, an old tracked movie if it already existed or null if the URL was bad.
	 **/
	public Request<TrackedMovieProxy> trackMovieByImdbId(String imdbId);
	
	/**
	 * Start tracking the given movie.
	 * 
	 * @param imdbUrl
	 *            A valid IMDB URL
	 * @return The new TrackedMovie, an old tracked movie if it already existed or an exception if the URL was bad.
	 */
	public Request<TrackedMovieProxy> trackMovieByImdbUrl(String imdbUrl);
	
	/**
	 * Delete a movie from the personal list.
	 */
	public InstanceRequest<TrackedMovieProxy, Void> delete();
}
