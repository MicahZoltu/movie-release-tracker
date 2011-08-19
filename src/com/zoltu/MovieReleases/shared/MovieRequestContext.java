package com.zoltu.MovieReleases.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.zoltu.MovieReleases.server.Movie;

@Service(Movie.class)
public interface MovieRequestContext extends RequestContext
{
	/**
	 * Get a list of the top 20 most popular movies.
	 * 
	 * @return A list of the top 20 most popular movies.
	 */
	public Request<List<MovieProxy>> getPopularMovies();
	
	/**
	 * Get details about a particular movie.
	 */
	public Request<MovieProxy> getMovie(String imdbId);
}
