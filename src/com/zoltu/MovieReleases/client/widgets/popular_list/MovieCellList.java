package com.zoltu.MovieReleases.client.widgets.popular_list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.zoltu.MovieReleases.shared.MovieProxy;

public class MovieCellList extends CellList<MovieProxy>
{
	private final SingleSelectionModel<MovieProxy> selectionModel = new SingleSelectionModel<MovieProxy>(MovieProxy.keyProvider);
	private final MovieCell cell;
	
	public MovieCellList()
	{
		super(new MovieCell(), MovieProxy.keyProvider);
		cell = (MovieCell) getCell();
		
		setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		setSelectionModel(selectionModel);
	}
	
	public void setPresenter(PopularListPresenter presenter)
	{
		cell.setPresenter(presenter);
	}
	
	public void setSelectionChangeHandler(SelectionChangeEvent.Handler eventHandler)
	{
		selectionModel.addSelectionChangeHandler(eventHandler);
	}
	
	public MovieProxy getSelected()
	{
		return selectionModel.getSelectedObject();
	}
}
