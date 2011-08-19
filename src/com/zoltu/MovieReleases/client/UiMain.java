package com.zoltu.MovieReleases.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.zoltu.MovieReleases.client.widgets.add_imdb_link.AddImdbView;
import com.zoltu.MovieReleases.client.widgets.personal_list.PersonalListView;
import com.zoltu.MovieReleases.client.widgets.popular_list.PopularListView;
import com.zoltu.MovieReleases.client.widgets.movie_details.DetailsView;

public class UiMain extends Composite
{
	interface UiMainUiBinder extends UiBinder<Widget, UiMain>
	{}
	
	private static UiMainUiBinder uiBinder = GWT.create(UiMainUiBinder.class);
	
	@UiField
	PersonalListView personalListView;
	@UiField
	PopularListView popularListView;
	@UiField
	AddImdbView addImdbView;
	@UiField
	DetailsView detailsView;
	
	public UiMain()
	{		
		// initialize the UI
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	protected void onLoad()
	{
		super.onLoad();
		
		// TODO: setup tracking of tab switching and do appropriate refreshes then
		
		Presenter presenter = new Presenter(personalListView, popularListView, detailsView);
		
		personalListView.setPresenter(presenter);
		popularListView.setPresenter(presenter);
		addImdbView.setPresenter(presenter);
		detailsView.setPresenter(presenter);
	}
}
