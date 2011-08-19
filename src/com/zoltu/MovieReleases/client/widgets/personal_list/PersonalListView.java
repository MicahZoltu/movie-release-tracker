package com.zoltu.MovieReleases.client.widgets.personal_list;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy;

public interface PersonalListView extends IsWidget
{
	public void setPresenter(PersonalListPresenter presenter);
	public void setSelectionChangeHandler(SelectionChangeEvent.Handler eventHandler);
	public void setCellList(List<TrackedMovieProxy> moviePresenters);
	public TrackedMovieProxy getSelection();
}
