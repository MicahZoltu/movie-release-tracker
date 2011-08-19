package com.zoltu.MovieReleases.client.widgets.popular_list;

import java.util.List;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.zoltu.MovieReleases.shared.MovieProxy;

public interface PopularListView
{
	public void setPresenter(PopularListPresenter presenter);
	public void setSelectionChangeHandler(SelectionChangeEvent.Handler eventHandler);
	public void setCellList(List<MovieProxy> moviePresenters);
	public MovieProxy getSelection();
}
