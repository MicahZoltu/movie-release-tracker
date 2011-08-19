package com.zoltu.MovieReleases.client.widgets.popular_list;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.zoltu.MovieReleases.shared.MovieProxy;

public class PopularListViewImpl extends Composite implements PopularListView
{
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiTemplate("PopularListView.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, PopularListViewImpl>
	{}
	
	private MovieCellList movieCellList = new MovieCellList();
	@UiField(provided = true)
	CellList<MovieProxy> cellList = movieCellList;
	
	public PopularListViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(PopularListPresenter presenter)
	{
		movieCellList.setPresenter(presenter);
	}
	
	@Override
	public void setSelectionChangeHandler(SelectionChangeEvent.Handler eventHandler)
	{
		movieCellList.setSelectionChangeHandler(eventHandler);
	}
	
	@Override
	public void setCellList(List<MovieProxy> moviePresenters)
	{
		movieCellList.setRowData(moviePresenters);
	}
	
	@Override
	public MovieProxy getSelection()
	{
		return movieCellList.getSelected();
	}
}
