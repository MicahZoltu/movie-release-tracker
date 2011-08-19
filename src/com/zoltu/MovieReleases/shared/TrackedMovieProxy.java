package com.zoltu.MovieReleases.shared;

import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.zoltu.MovieReleases.server.TrackedMovie;

@ProxyFor(value = TrackedMovie.class, locator = TrackedMovie.Locator.class)
public interface TrackedMovieProxy extends EntityProxy
{	
	public static final class KeyProvider implements ProvidesKey<TrackedMovieProxy>
	{
		public Object getKey(TrackedMovieProxy trackedMovie)
		{
			return trackedMovie == null ? null : trackedMovie.getCompositeKey();
		}
	}
	public static final ProvidesKey<TrackedMovieProxy> keyProvider = new KeyProvider();
	
	public String getCompositeKey();
	public String getImdbId();
	public String getUserId();
	public Date getTheaterRelease();
	public Date getDvdRelease();
	public String getTitle();
	
	public enum OrderBy
	{
		DVD, THEATER
	}
}
