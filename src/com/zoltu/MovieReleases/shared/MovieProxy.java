package com.zoltu.MovieReleases.shared;

import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.zoltu.MovieReleases.server.Movie;

@ProxyFor(value = Movie.class, locator = Movie.Locator.class)
public interface MovieProxy extends EntityProxy
{
	public static final class KeyProvider implements ProvidesKey<MovieProxy>
	{
		public Object getKey(MovieProxy movie)
		{
			return movie == null ? null : movie.getImdbId();
		}
	}
	public static final ProvidesKey<MovieProxy> keyProvider = new KeyProvider();
	
	public String getImdbId();
	public String getTitle();
	public String getDescription();
	public String getImageLink();
	public Date getTheaterRelease();
	public Date getDvdRelease();
	public Long getUserCount();
}
