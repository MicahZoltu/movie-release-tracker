package com.zoltu.MovieReleases.client.widgets.movie_details;

import java.util.Date;

import com.google.gwt.user.client.ui.IsWidget;

public interface DetailsView extends IsWidget
{
	/**
	 * Set the movie to be displayed by this view.
	 * 
	 * @param title
	 *            Title of the movie.
	 * @param description
	 *            Description of the movie.
	 * @param theater
	 *            Theater release date of the movie.
	 * @param dvd
	 *            DVD release date of the movie.
	 * @param trackers
	 *            Number of users tracking the movie.
	 */
	public void clear();
	public void setMovie(String title, String description, String imageLink, Date theater, Date dvd, Long trackers);
	public void setPresenter(DetailsPresenter presenter);
}
