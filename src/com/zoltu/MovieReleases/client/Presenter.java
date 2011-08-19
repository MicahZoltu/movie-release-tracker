package com.zoltu.MovieReleases.client;

import java.util.List;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.zoltu.MovieReleases.client.widgets.add_imdb_link.AddImdbPresenter;
import com.zoltu.MovieReleases.client.widgets.movie_details.DetailsPresenter;
import com.zoltu.MovieReleases.client.widgets.movie_details.DetailsView;
import com.zoltu.MovieReleases.client.widgets.personal_list.PersonalListPresenter;
import com.zoltu.MovieReleases.client.widgets.personal_list.PersonalListView;
import com.zoltu.MovieReleases.client.widgets.popular_list.PopularListPresenter;
import com.zoltu.MovieReleases.client.widgets.popular_list.PopularListView;
import com.zoltu.MovieReleases.shared.MovieProxy;
import com.zoltu.MovieReleases.shared.MovieRequestContext;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy.OrderBy;
import com.zoltu.MovieReleases.shared.TrackedMovieRequestContext;

public class Presenter
		implements
		PersonalListPresenter,
		PopularListPresenter,
		DetailsPresenter,
		AddImdbPresenter
{
	private final PersonalListView personalList;
	private final PopularListView popularList;
	private final PersonalReceiver personalReceiver = new PersonalReceiver();
	private final PopularReceiver popularReceiver = new PopularReceiver();
	private final PersonalSelectionChangeEventHandler personalChangeHandler = new PersonalSelectionChangeEventHandler();
	private final PopularSelectionChangeEventHandler popularChangeHandler = new PopularSelectionChangeEventHandler();
	private final DetailsView details;
	private OrderBy orderBy = OrderBy.DVD;
	
	public Presenter(final PersonalListView personalList, final PopularListView popularList, final DetailsView details)
	{
		this.personalList = personalList;
		this.popularList = popularList;
		this.details = details;
		
		personalList.setSelectionChangeHandler(personalChangeHandler);
		popularList.setSelectionChangeHandler(popularChangeHandler);
		
		// get an initial list to populate the personal view with
		final TrackedMovieRequestContext personalRequest = MovieReleases.sRequestFactory.getTrackedMovieRequestContext();
		personalRequest.getPersonalMovies(orderBy).fire(personalReceiver);
		
		// TODO: only update when they switch tabs
		// get an initial list to populate the popular view with
		final MovieRequestContext popularRequest = MovieReleases.sRequestFactory.getMovieRequestContext();
		popularRequest.getPopularMovies().fire(popularReceiver);
	}
	
	@Override
	public void addImdbLink(final String imdbLink)
	{
		final TrackedMovieRequestContext lRequest = MovieReleases.sRequestFactory.getTrackedMovieRequestContext();
		lRequest.trackMovieByImdbUrl(imdbLink).fire(new Receiver<TrackedMovieProxy>()
		{
			@Override
			public void onSuccess(TrackedMovieProxy response)
			{
				// TODO: add the new movie to the list client side and re-sort
				MovieReleases.sRequestFactory.getTrackedMovieRequestContext().getPersonalMovies(orderBy).fire(personalReceiver);
				MovieReleases.sRequestFactory.getMovieRequestContext().getPopularMovies().fire(popularReceiver);
			}
		});
	}
	
	@Override
	public void deleteMovieRequest(TrackedMovieProxy moviePresenter)
	{
		final TrackedMovieRequestContext lRequest = MovieReleases.sRequestFactory.getTrackedMovieRequestContext();
		lRequest.delete().using(moviePresenter).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void empty)
			{
				MovieReleases.sRequestFactory.getTrackedMovieRequestContext().getPersonalMovies(orderBy).fire(personalReceiver);
				MovieReleases.sRequestFactory.getMovieRequestContext().getPopularMovies().fire(popularReceiver);
			}
		});
	}
	
	@Override
	public void sortDvdRequest()
	{
		orderBy = OrderBy.DVD;
		final TrackedMovieRequestContext lRequest = MovieReleases.sRequestFactory.getTrackedMovieRequestContext();
		lRequest.getPersonalMovies(orderBy).fire(personalReceiver);
	}
	
	@Override
	public void sortTheaterRequest()
	{
		orderBy = OrderBy.THEATER;
		final TrackedMovieRequestContext lRequest = MovieReleases.sRequestFactory.getTrackedMovieRequestContext();
		lRequest.getPersonalMovies(orderBy).fire(personalReceiver);
	}
	
	@Override
	public void addMovieRequest(MovieProxy movie)
	{
		final TrackedMovieRequestContext lRequest = MovieReleases.sRequestFactory.getTrackedMovieRequestContext();
		lRequest.trackMovieByImdbId(movie.getImdbId()).fire(new Receiver<TrackedMovieProxy>()
		{
			@Override
			public void onSuccess(TrackedMovieProxy trackedMovie)
			{
				MovieReleases.sRequestFactory.getTrackedMovieRequestContext().getPersonalMovies(orderBy).fire(personalReceiver);
			}
		});
	}
	
	private class PersonalReceiver extends Receiver<List<TrackedMovieProxy>>
	{
		@Override
		public void onSuccess(List<TrackedMovieProxy> response)
		{
			personalList.setCellList(response);
		}
	}
	
	private class PopularReceiver extends Receiver<List<MovieProxy>>
	{
		@Override
		public void onSuccess(List<MovieProxy> response)
		{
			popularList.setCellList(response);
		}
	}
	
	private class PersonalSelectionChangeEventHandler implements SelectionChangeEvent.Handler
	{
		@Override
		public void onSelectionChange(SelectionChangeEvent event)
		{
			// fetch movie details about the current selection and tell the details view
			final MovieRequestContext lRequest = MovieReleases.sRequestFactory.getMovieRequestContext();
			lRequest.getMovie(personalList.getSelection().getImdbId()).fire(new Receiver<MovieProxy>()
			{
				@Override
				public void onSuccess(MovieProxy response)
				{
					if (response == null)
					{
						details.clear();
						return;
					}
					details.setMovie(
							response.getTitle(),
							response.getDescription(),
							response.getImageLink(),
							response.getTheaterRelease(),
							response.getDvdRelease(),
							response.getUserCount());
				}
			});
		}
	}
	
	private class PopularSelectionChangeEventHandler implements SelectionChangeEvent.Handler
	{
		@Override
		public void onSelectionChange(SelectionChangeEvent event)
		{
			// fetch movie details about the current selection and tell the details view
			final MovieRequestContext lRequest = MovieReleases.sRequestFactory.getMovieRequestContext();
			lRequest.getMovie(popularList.getSelection().getImdbId()).fire(new Receiver<MovieProxy>()
			{
				@Override
				public void onSuccess(MovieProxy response)
				{
					if (response == null)
					{
						details.clear();
						return;
					}
					details.setMovie(
							response.getTitle(),
							response.getDescription(),
							response.getImageLink(),
							response.getTheaterRelease(),
							response.getDvdRelease(),
							response.getUserCount());
				}
			});
		}
	}
}
