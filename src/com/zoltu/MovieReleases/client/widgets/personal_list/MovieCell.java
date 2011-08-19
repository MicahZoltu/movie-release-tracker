package com.zoltu.MovieReleases.client.widgets.personal_list;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AbstractImagePrototype.ImagePrototypeElement;
import com.zoltu.MovieReleases.client.Resources;
import com.zoltu.MovieReleases.shared.TrackedMovieProxy;

public class MovieCell extends AbstractCell<TrackedMovieProxy>
{
	private PersonalListPresenter presenter;
	
	public MovieCell()
	{
		super("click");
	}
	
	public void setPresenter(PersonalListPresenter presenter)
	{
		this.presenter = presenter;
	}
	
	@Override
	public void onBrowserEvent(Context context, Element parent, TrackedMovieProxy moviePresenter, NativeEvent event, ValueUpdater<TrackedMovieProxy> valueUpdater)
	{
		super.onBrowserEvent(context, parent, moviePresenter, event, valueUpdater);
		
		// if this isn't a click event return
		if (!event.getType().equals("click")) return;
		// if the click wasn't on an element return
		if (!Element.is(event.getEventTarget())) return;
		// if the click wasn't on the delete_button return
		final Element lTargetElement = Element.as(event.getEventTarget());
		if (!lTargetElement.getId().equals("delete_button")) return;
		
		// send delete button click event to the presenter
		if (presenter != null) presenter.deleteMovieRequest(moviePresenter);
	}
	
	@Override
	public void render(Context context, TrackedMovieProxy moviePresenter, SafeHtmlBuilder safeHtmlBuilder)
	{
		// Value can be null, so do a null check..
		if (moviePresenter == null) return;
		
		final String lDvdRelease = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(moviePresenter.getDvdRelease());
		final String lTheaterRelease = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(moviePresenter.getTheaterRelease());
		final ImagePrototypeElement lImageElement = AbstractImagePrototype.create(Resources.sSingleton.delete()).createElement();
		lImageElement.setId("delete_button");
		final SafeHtml lImageSafeHtml = new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(lImageElement.getString());
		safeHtmlBuilder.append(MyTemplates.sSingleton.fGenerateCellHtml(moviePresenter.getTitle(), lImageSafeHtml, lTheaterRelease, lDvdRelease));
	}
	
	public interface MyTemplates extends SafeHtmlTemplates
	{
		public static final MyTemplates sSingleton = GWT.create(MyTemplates.class);
		
		@Template(
				"<table width='100%'>" +
				"	<tr>" +
				"		<td colspan='2'>" +
				"			<b><i>{0}</i></b>" +
				"		</td>" +
				"		<td rowspan='2' width='48' height='48'>" +
				"			{1}" +
				"		</td>" +
				"	</tr>" +
				"	<tr>" +
				"		<td style='text-align:left; float:left'>" +
				"			<b>Theater:</b> {2}" +
				"		</td>" +
				"		<td style='text-align:right; float:right'>" +
				"			<b>DVD:</b> {3}" +
				"		</td>" +
				"	</tr>" +
				"</table>")
		SafeHtml fGenerateCellHtml(String pTitle, SafeHtml pDeleteButtonLink, String pTheaterReleaseDate, String pDvdReleaseDate);
	}
}
