package com.zoltu.MovieReleases.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.annotation.Unindexed;

public class Movie
{
	@Id
	private String imdbId;
	@Unindexed
	private String title;
	@Unindexed
	private String description;
	@Unindexed
	private String imageLink;
	private Date theaterRelease;
	private Date dvdRelease;
	private Long userCount = 0L;
	@Unindexed
	private Set<String> userIds = new HashSet<String>();
	@Unindexed
	private Long version = 0L;
	
	@SuppressWarnings("unused")
	@PrePersist
	private void incrementVersion()
	{
		version++;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	private void setUserCount()
	{
		userCount = new Long(userIds.size());
	}
	
	public String getImdbId()
	{
		return imdbId;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getImageLink()
	{
		return imageLink;
	}
	
	public Date getTheaterRelease()
	{
		return theaterRelease;
	}
	
	public Date getDvdRelease()
	{
		return dvdRelease;
	}
	
	public Long getUserCount()
	{
		return userCount;
	}
	
	private Movie()
	{}
	
	private Movie(String imdbId, InternetVideoArchive info)
	{
		this.imdbId = imdbId;
		update(info);
	}
	
	private void update(InternetVideoArchive info)
	{
		// TODO: get the movie poster from http://www.imdbapi.com/ instead of the one supplied by IVA
		this.title = info.getTitle();
		this.description = info.getDescription();
		this.imageLink = info.getImageLink();
		this.theaterRelease = info.getTheaterReleaseDate();
		this.dvdRelease = info.getDvdReleaseDate();
	}
	
	/**
	 * Get a list of the top 20 most popular movies.
	 * 
	 * @return A list of the top 20 most popular movies.
	 */
	public static List<Movie> getPopularMovies()
	{
		final Objectify objectify = ObjectifyService.begin();
		final Query<Movie> query = objectify.query(Movie.class);
		query.order("-userCount");
		query.order("theaterRelease");
		query.limit(20);
		final Iterator<Movie> queryResults = query.iterator();
		final List<Movie> pesults = new ArrayList<Movie>();
		while (queryResults.hasNext())
		{
			pesults.add(queryResults.next());
		}
		return pesults;
	}
	
	/**
	 * Get details about a particular movie.
	 */
	public static Movie getMovie(String imdbId)
	{
		return ObjectifyService.begin().find(Movie.class, imdbId);
	}
	
	/**
	 * Add the currently logged in user to the user list of a DetailsView.
	 * 
	 * @param pImdbId
	 *            The IMDB ID of the movie to add the user to.
	 */
	protected static void addUserToMovie(String imdbId, InternetVideoArchive info)
	{
		// get & update or create the movie
		Objectify objectify = ObjectifyService.begin();
		Movie movie = objectify.find(Movie.class, imdbId);
		if (movie == null) movie = new Movie(imdbId, info);
		else movie.update(info);
		
		// add the user to the movie and save to the datastore
		movie.userIds.add(UserServiceFactory.getUserService().getCurrentUser().getUserId());
		objectify.put(movie);
	}
	
	/**
	 * Remove the currently logged in user from a DetailsView in the datastore.
	 * 
	 * @param pImdbId
	 *            IMDB ID of the movie to remove the user from.
	 */
	protected static void removeUserFromMovie(String imdbId)
	{
		// get the DetailsView entity for this IMDB ID from the data store
		Objectify lObjectify = ObjectifyService.begin();
		Movie lMovie = lObjectify.find(Movie.class, imdbId);
		
		// if the movie doesn't exist then we are done (nothing to remove the user from)
		if (lMovie == null) return;
		
		// remove the user from the user list
		lMovie.userIds.remove(UserServiceFactory.getUserService().getCurrentUser().getUserId());
		
		// if the user list is now empty, delete the movie from the datastore
		if (lMovie.userIds.size() == 0) lObjectify.delete(Movie.class, imdbId);
		// if the user list is not empty then write the changes to the datastore
		else lObjectify.put(lMovie);
	}
	
	/**
	 * Locator required by RequestFactory. IdType generic parameter must match the type of your @Id field.
	 */
	public static class Locator extends MyLocator.WithStringId<Movie>
	{
		@Override
		public String getId(Movie movie)
		{
			return movie.imdbId;
		}
		
		@Override
		public Object getVersion(Movie movie)
		{
			return movie.version;
		}
	}
}
