package com.zoltu.MovieReleases.server;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zoltu.MovieReleases.server.exceptions.ElementNotFound;
import com.zoltu.MovieReleases.server.exceptions.PrimaryItemNotFound;

public class InternetVideoArchive
{
	private static final String sBaseUrl =
			"http://api.internetvideoarchive.com/Video/PinPoint.aspx?DeveloperId=2298dd0f-3b66-42eb-a334-08269256a5bf&IdType=12&includeallassets=true&SearchTerm=";
	
	private final Document document;
	private final Element primaryItem;
	private final Date theaterReleaseDate;
	private final Date dvdReleaseDate;
	private final String title;
	private final String description;
	private final String imageLink;
	
	/**
	 * Blocking request for an IVA PinPoint.
	 * 
	 * @param pImdbId
	 * @throws LotsOfStuff
	 */
	public InternetVideoArchive(String pImdbId)
			throws ParserConfigurationException,
			SAXException,
			IOException,
			PrimaryItemNotFound,
			ParseException,
			ElementNotFound
	{
		document = fetchDocument(pImdbId);
		primaryItem = findPrimaryItem();
		theaterReleaseDate = findTheaterReleaseDate();
		dvdReleaseDate = findDvdReleaseDate();
		title = findTitle();
		description = findDescription();
		imageLink = findImage();
	}
	
	public Date getTheaterReleaseDate()
	{
		return theaterReleaseDate;
	}
	
	public Date getDvdReleaseDate()
	{
		return dvdReleaseDate;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getImageLink()
	{
		return imageLink;
	}
	
	private Document fetchDocument(String imdbId) throws ParserConfigurationException, SAXException, IOException
	{
		final URL url = new URL(sBaseUrl + imdbId);
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setCoalescing(true);
		final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(url.openStream());
	}
	
	private Element findPrimaryItem() throws PrimaryItemNotFound
	{
		final NodeList parentIdElementList = document.getElementsByTagName("ParentPublishedID");
		Integer numberOfChildren = parentIdElementList.getLength();
		if (numberOfChildren == 0) throw new PrimaryItemNotFound();
		for (Integer i = 0; i < numberOfChildren; ++i)
		{
			if (parentIdElementList.item(i).getTextContent().isEmpty())
			{
				return (Element) parentIdElementList.item(i).getParentNode();
			}
		}
		
		throw new PrimaryItemNotFound();
	}
	
	private String findElement(String elementName) throws ElementNotFound
	{
		final NodeList nodeList = primaryItem.getElementsByTagName(elementName);
		if (nodeList.getLength() == 0) throw new ElementNotFound(elementName);
		return nodeList.item(0).getTextContent();
	}
	
	private String findTitle() throws ElementNotFound
	{
		return findElement("Title");
	}
	
	private String findDescription() throws ElementNotFound
	{
		return findElement("Description");
	}
	
	private String findImage() throws ElementNotFound
	{
		try
		{
			return findElement("Image");
		}
		catch (ElementNotFound e)
		{
			return "";
		}
	}
	
	private Date findTheaterReleaseDate() throws ParseException, ElementNotFound
	{
		return new SimpleDateFormat("M/d/y").parse(findElement("TheatricalReleaseDate"));
	}
	
	private Date findDvdReleaseDate() throws ParseException, ElementNotFound
	{
		final Date dvdRelease = new SimpleDateFormat("M/d/y").parse(findElement("HomeVideoReleaseDate"));
		if (dvdRelease.getTime() == -2208988800000L) return deriveDvdFromTheaterRelease(theaterReleaseDate);
		else return dvdRelease;
	}
	
	private static Date deriveDvdFromTheaterRelease(Date theaterDate)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(theaterDate);
		calendar.add(Calendar.MONTH, 4);
		return calendar.getTime();
	}
}
