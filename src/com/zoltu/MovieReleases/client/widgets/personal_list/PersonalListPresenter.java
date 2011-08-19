package com.zoltu.MovieReleases.client.widgets.personal_list;

import com.zoltu.MovieReleases.shared.TrackedMovieProxy;

public interface PersonalListPresenter
{
	public void deleteMovieRequest(TrackedMovieProxy moviePresenter);
	public void sortDvdRequest();
	public void sortTheaterRequest();
}
