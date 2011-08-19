package com.zoltu.MovieReleases.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.annotation.Unindexed;
import com.zoltu.MovieReleases.server.exceptions.AccessDeniedException;
import com.zoltu.MovieReleases.server.exceptions.ElementNotFound;
import com.zoltu.MovieReleases.server.exceptions.PrimaryItemNotFound;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy.OrderBy;

public class TrackedMovie
{
	/** mUserId::mImdbId **/
	@Id
	private String compositeKey;
	private String userId;
	private String imdbId;
	@Unindexed
	private Long version = 0L;
	private Date theaterRelease;
	private Date dvdRelease;
	@Unindexed
	private String title;
	
	@SuppressWarnings("unused")
	@PrePersist
	private void incrementVersion()
	{
		version++;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	@PostLoad
	private void validateId()
	{
		assert userId + "::" + imdbId == compositeKey : "Attempted to save or load an invalid entity from the datastore!";
	}
	
	private TrackedMovie()
	{}
	
	private TrackedMovie(String imdbId, InternetVideoArchive iva)
	{
		userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		this.imdbId = imdbId;
		compositeKey = createCompositeKey(userId, imdbId);
		theaterRelease = iva.getTheaterReleaseDate();
		dvdRelease = iva.getDvdReleaseDate();
		title = iva.getTitle();
	}
	
	public String getCompositeKey()
	{
		return compositeKey;
	}
	
	public String getImdbId()
	{
		return imdbId;
	}
	
	public String getUserId()
	{
		return userId;
	}
	
	public Date getTheaterRelease()
	{
		return theaterRelease;
	}
	
	public Date getDvdRelease()
	{
		return dvdRelease;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Save this entity to the datastore.
	 */
	public void save() throws AccessDeniedException
	{
		validateUser();
		ObjectifyService.begin().put(this);
	}
	
	/**
	 * Delete this TrackedMovie from the datastore.
	 */
	public void delete() throws AccessDeniedException
	{
		validateUser();
		Movie.removeUserFromMovie(imdbId);
		ObjectifyService.begin().delete(this);
	}
	
	/**
	 * Get movies the currently logged in user is tracking.
	 * 
	 * @param pOrderBy
	 *            The field you want the results to be sorted by.
	 **/
	public static List<TrackedMovie> getPersonalMovies(OrderBy orderBy)
	{
		final String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		final Objectify objectify = ObjectifyService.begin();
		final Query<TrackedMovie> query = objectify.query(TrackedMovie.class);
		query.filter("userId =", userId);
		switch (orderBy)
		{
			case DVD:
				query.order("dvdRelease");
				break;
			case THEATER:
				query.order("theaterRelease");
				break;
		}
		final Iterator<TrackedMovie> queryResults = query.iterator();
		final List<TrackedMovie> pesults = new ArrayList<TrackedMovie>();
		while (queryResults.hasNext())
		{
			pesults.add(queryResults.next());
		}
		return pesults;
	}
	
	/**
	 * Start tracking the given movie.
	 * 
	 * @param pImdbUrl
	 *            A valid IMDB link.
	 * @return The new TrackedMovie, an old tracked movie if it already existed or null if the URL was bad.
	 * @throws ElementNotFound 
	 * @throws ParseException 
	 * @throws PrimaryItemNotFound 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 **/
	public static TrackedMovie trackMovieByImdbId(String imdbId) throws ParserConfigurationException, SAXException, IOException, PrimaryItemNotFound, ParseException, ElementNotFound
	{
		final String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		final String compositeKey = createCompositeKey(userId, imdbId);
		final Objectify objectify = ObjectifyService.begin();
		
		// check to see if this movie is already listed
		TrackedMovie result = objectify.find(TrackedMovie.class, compositeKey);
		if (result != null) return result;
		
		// query the Internet Video Archive for movie info
		InternetVideoArchive iva = new InternetVideoArchive(imdbId);
		
		// create a new TrackedMovie
		result = new TrackedMovie(imdbId, iva);
		
		// populate a movie entity if one doesn't exist, or add ourselves to it if it does exist
		Movie.addUserToMovie(imdbId, iva);
		
		// save the new TrackedMovie to the datastore
		objectify.put(result);
		
		// return the TrackedMovie to the user
		return result;
	}
	
	/**
	 * Convenience function for tracking a movie by URL. Gets the ID and calls the ID version.
	 * 
	 * @param imdbUrl
	 *            Url to the IMDB page of the movie to track.
	 * @return The tracked movie (new or old if it existed).
	 * @throws ElementNotFound 
	 * @throws ParseException 
	 * @throws PrimaryItemNotFound 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static TrackedMovie trackMovieByImdbUrl(String imdbUrl) throws ParserConfigurationException, SAXException, IOException, PrimaryItemNotFound, ParseException, ElementNotFound
	{
		final String imdbId = getImdbIdFromUrl(imdbUrl);
		return trackMovieByImdbId(imdbId);
	}
	
	private static String createCompositeKey(String userId, String imdbId)
	{
		final String lCompositeKey = userId + "::" + imdbId;
		return lCompositeKey;
	}
	
	private void validateUser() throws AccessDeniedException
	{
		final String lUserId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		if (!lUserId.equals(userId)) throw new AccessDeniedException("You can only work with movies in your personal list.");
	}
	
	private static String getImdbIdFromUrl(String imdbLink) throws MalformedURLException
	{
		final URL imdbUrl = new URL(imdbLink);
		final String[] splitUrl = imdbUrl.getPath().split("/");
		if (!splitUrl[1].equals("title")) throw new MalformedURLException("Invalid URL: 'title' part not found in first section");
		String imdbId = splitUrl[2];
		if (!imdbId.startsWith("tt")) throw new MalformedURLException("Invalid URL: second section does not begin with 'tt'");
		if (!imdbId.substring(2, imdbId.length() - 1).matches("^[0-9]+$")) throw new MalformedURLException(
				"Invalid URL: second section does not contain only digits after the 'tt'");
		return imdbId;
	}
	
	/**
	 * Locator required by RequestFactory. IdType generic parameter must match the type of your @Id field.
	 */
	public static class Locator extends MyLocator.WithStringId<TrackedMovie>
	{
		@Override
		public String getId(TrackedMovie trackedMovie)
		{
			return trackedMovie.compositeKey;
		}
		
		@Override
		public Object getVersion(TrackedMovie trackedMove)
		{
			return trackedMove.version;
		}
	}
}
