package com.zoltu.MovieReleases.client.widgets.personal_list;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy;

public class PersonalListViewImpl extends Composite implements PersonalListView
{
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiTemplate("PersonalListView.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, PersonalListViewImpl>
	{}
	
	private MovieCellList movieCellList = new MovieCellList();
	@UiField(provided = true)
	CellList<TrackedMovieProxy> cellList = movieCellList;
	
	private PersonalListPresenter presenter;
	
	public PersonalListViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(PersonalListPresenter presenter)
	{
		this.presenter = presenter;
		movieCellList.setPresenter(presenter);
	}
	
	@Override
	public void setSelectionChangeHandler(SelectionChangeEvent.Handler eventHandler)
	{
		movieCellList.setSelectionChangeHandler(eventHandler);
	}
	
	@Override
	public void setCellList(List<TrackedMovieProxy> moviePresenters)
	{
		movieCellList.setRowData(moviePresenters);
	}
	
	@UiHandler("sortDvd")
	void onSortDvd(ClickEvent event)
	{
		if (presenter != null) presenter.sortDvdRequest();
	}
	
	@UiHandler("sortTheater")
	void onSortTheater(ClickEvent event)
	{
		if (presenter != null) presenter.sortTheaterRequest();
	}

	@Override
	public TrackedMovieProxy getSelection()
	{
		return movieCellList.getSelected();
	}
}
