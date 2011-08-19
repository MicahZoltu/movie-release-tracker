package com.zoltu.MovieReleases.client.widgets.movie_details;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DetailsViewImpl extends Composite implements DetailsView
{
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiTemplate("DetailsView.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, DetailsViewImpl>
	{}
	
	@UiField
	Label title;
	@UiField
	InlineHTML description;
	@UiField
	Label trackingCount;
	
	@SuppressWarnings("unused")
	private DetailsPresenter presenter;
	
	public DetailsViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(DetailsPresenter presenter)
	{
		this.presenter = presenter;
	}
	
	@Override
	public void setMovie(String title, String description, String imageLink, Date theater, Date dvd, Long trackers)
	{
		this.title.setText(title);
		
		SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
		safeHtmlBuilder.append(MyTemplates.singleton.generateHtml(description, imageLink));
		this.description.setHTML(safeHtmlBuilder.toSafeHtml());
		
		this.trackingCount.setText("Popularity: " + trackers.toString());
	}

	@Override
	public void clear()
	{
		this.title.setText("");
		this.description.setText("");
		this.trackingCount.setText("");
	}
	
	public interface MyTemplates extends SafeHtmlTemplates
	{
		public static final MyTemplates singleton = GWT.create(MyTemplates.class);
		
		@Template("<div style='padding:1em; text-align:justify;'><img src='{1}' style='float:left; padding-right:1em'/>{0}</div>")
		SafeHtml generateHtml(String description, String imageLink);
	}
}
