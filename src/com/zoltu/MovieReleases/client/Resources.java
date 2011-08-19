package com.zoltu.MovieReleases.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle
{
	public static final Resources sSingleton = GWT.create(Resources.class);
	
	@Source("resources/bundled.css")
	BundledCssResource bundledCss();
	
	public interface BundledCssResource extends CssResource
	{
		
	}
	
	@Source("resources/delete.png")
	ImageResource delete();

	@Source("resources/add.png")
	ImageResource add();
}
