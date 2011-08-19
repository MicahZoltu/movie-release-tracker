package com.zoltu.MovieReleases.client.widgets.popular_list;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
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
import com.zoltu.MovieReleases.shared.MovieProxy;

public class MovieCell extends AbstractCell<MovieProxy>
{
	private PopularListPresenter presenter;
	
	public MovieCell()
	{
		super("click");
	}
	
	public void setPresenter(PopularListPresenter presenter)
	{
		this.presenter = presenter;
	}
	
	@Override
	public void onBrowserEvent(Context context, Element parent, MovieProxy movie, NativeEvent event, ValueUpdater<MovieProxy> valueUpdater)
	{
		super.onBrowserEvent(context, parent, movie, event, valueUpdater);
		
		// if this isn't a click event return
		if (!event.getType().equals("click")) return;
		// if the click wasn't on an element return
		if (!Element.is(event.getEventTarget())) return;
		// if the click wasn't on the delete_button return
		final Element lTargetElement = Element.as(event.getEventTarget());
		if (!lTargetElement.getId().equals("add_button")) return;
		
		// send delete button click event to the presenter
		if (presenter != null) presenter.addMovieRequest(movie);
	}
	
	@Override
	public void render(Cell.Context context, MovieProxy movie, SafeHtmlBuilder safeHtmlBuilder)
	{
		// Value can be null, so do a null check..
		if (movie == null) return;
		
		final String dvdRelease = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(movie.getDvdRelease());
		final String theaterRelease = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM).format(movie.getTheaterRelease());
		final ImagePrototypeElement imageElement = AbstractImagePrototype.create(Resources.sSingleton.add()).createElement();
		imageElement.setId("add_button");
		final SafeHtml imageSafeHtml = new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(imageElement.getString());
		safeHtmlBuilder.append(MyTemplates.sSingleton.generateCellHtml(movie.getTitle(), movie.getUserCount().toString(), imageSafeHtml, theaterRelease, dvdRelease));
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
				"		<td rowspan='2' width='48' height='48' style='font-size:x-large; text-align:center'>" +
				"			{1}" +
				"		</td>" +
				"		<td rowspan='2' width='48' height='48'>" +
				"			{2}" +
				"		</td>" +
				"	</tr>" +
				"	<tr>" +
				"		<td style='text-align:left; float:left'>" +
				"			<b>Theater:</b> {3}" +
				"		</td>" +
				"		<td style='text-align:right; float:right'>" +
				"			<b>DVD:</b> {4}" +
				"		</td>" +
				"	</tr>" +
				"</table>")
		SafeHtml generateCellHtml(String title, String popularity, SafeHtml addButtonUrl, String theater, String dvd);
	}
}
