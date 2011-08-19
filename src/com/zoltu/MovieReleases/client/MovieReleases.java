package com.zoltu.MovieReleases.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.zoltu.MovieReleases.shared.RequestFactory;
import com.zoltu.gae_authentication.client.AuthenticationRequestTransport;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MovieReleases implements EntryPoint
{
	public static final EventBus sEventBus = new SimpleEventBus();
	public static final AuthenticationRequestTransport sRequestTransport = new AuthenticationRequestTransport(sEventBus);
	public static final Logger sLogger = Logger.getLogger("");
	public static RequestFactory sRequestFactory = GWT.create(RequestFactory.class);
	static
	{
		sRequestFactory.initialize(sEventBus, sRequestTransport);
	}
	
	private final UiMain mUiMain;
	
	public MovieReleases()
	{
		mUiMain = new UiMain();
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
		RootLayoutPanel.get().add(mUiMain);
	}
}
