package com.zoltu.MovieReleases.client.widgets.personal_list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy;

public class MovieCellList extends CellList<TrackedMovieProxy>
{
	private final SingleSelectionModel<TrackedMovieProxy> selectionModel = new SingleSelectionModel<TrackedMovieProxy>(TrackedMovieProxy.keyProvider);
	private final MovieCell cell;
	
	public MovieCellList()
	{
		super(new MovieCell(), TrackedMovieProxy.keyProvider);
		cell = (MovieCell) getCell();
		
		setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		setSelectionModel(selectionModel);
	}
	
	public void setPresenter(PersonalListPresenter presenter)
	{
		cell.setPresenter(presenter);
	}
	
	public void setSelectionChangeHandler(SelectionChangeEvent.Handler eventHandler)
	{
		selectionModel.addSelectionChangeHandler(eventHandler);
	}
	
	public TrackedMovieProxy getSelected()
	{
		return selectionModel.getSelectedObject();
	}
}
