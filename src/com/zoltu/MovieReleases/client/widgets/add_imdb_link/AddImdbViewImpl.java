package com.zoltu.MovieReleases.client.widgets.add_imdb_link;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddImdbViewImpl extends Composite implements AddImdbView
{
	private static ImdbViewUiBinder sUiBinder = GWT.create(ImdbViewUiBinder.class);
	
	@UiTemplate("AddImdbView.ui.xml")
	interface ImdbViewUiBinder extends UiBinder<Widget, AddImdbViewImpl>
	{}
	
	@UiField
	TextBox textBox;
	
	private AddImdbPresenter presenter;
	
	public AddImdbViewImpl()
	{
		initWidget(sUiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(AddImdbPresenter presenter)
	{
		this.presenter = presenter;
	}
	
	@UiHandler("button")
	void onButtonClicked(ClickEvent event)
	{
		// tell the presenter that the user wants to add an IMDB link
		if (presenter != null) presenter.addImdbLink(textBox.getText());
		
		// clear the textbox so it's ready for the next link
		textBox.setText("");
	}
}
