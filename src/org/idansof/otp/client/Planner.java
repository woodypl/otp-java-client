/*
 * Copyright 2010, 2011 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.idansof.otp.client;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpProtocolParams;
import org.idansof.otp.client.Leg.Mode;
import org.idansof.otp.client.WalkStep.AbsoluteDirection;
import org.idansof.otp.client.WalkStep.RelativeDirection;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class Planner {
	
	private static final String ZULU_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	
	private Locale locale = Locale.getDefault();
	private String host;
	private String uri = "opentripplanner-api-webapp/ws/plan";
	
	DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
	DateFormat zuluDateTimeFormat = new SimpleDateFormat(ZULU_DATETIME_FORMAT);
	
	public Planner(String host) {
		super();
		this.host = host;
	}
	
	public Planner(String host,String uri) {
		this(host);
		this.uri = uri;
	}
	
	public Planner(String host,String uri,Locale locale) {
		this(host, uri);
		this.locale = locale;
	}

	
	public PlanResult generatePlan(PlanRequest planRequest) throws IOException, XmlPullParserException, ParseException
	{
		
		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");
		try
		{
			
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,locale);
			DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT,locale);
			
			
			Uri.Builder builder = Uri.parse("http://"+host).buildUpon();

			builder.appendEncodedPath(uri);
			builder.appendQueryParameter("fromPlace",planRequest.getFrom().getCoordinateString());		
			builder.appendQueryParameter("toPlace",planRequest.getTo().getCoordinateString());		
			builder.appendQueryParameter("date",dateFormat.format(planRequest.getDate()));		
			builder.appendQueryParameter("time",timeFormat.format(planRequest.getDate()));		
			
			HttpProtocolParams.setContentCharset(androidHttpClient.getParams(), "utf-8");
			String uri = builder.build().toString();
			Log.i(Planner.class.toString(),"Fetching plan from "+uri);
			HttpUriRequest httpUriRequest = new HttpGet(	uri);
			httpUriRequest.setHeader("Accept", "text/xml");
			HttpResponse httpResponse = androidHttpClient.execute(httpUriRequest);
			
			InputStream contentStream = httpResponse.getEntity().getContent();
			Log.i(Planner.class.toString(),"Parsing content , size :"+httpResponse.getEntity().getContentLength());			
			return parseXMLResponse(contentStream);		
		}
		finally
		{
			androidHttpClient.close();		
		}
	}
	
	private PlanResult parseXMLResponse(InputStream contentStream) throws IOException, XmlPullParserException, ParseException
	{
		TripPlan tripPlan = new TripPlan();
		XmlPullParser pullParser = new KXmlParser();
		pullParser.setInput(contentStream, "UTF-8");
		int eventType = pullParser.getEventType();				
		// Skip to the first tag
		while (eventType != XmlPullParser.END_DOCUMENT && (eventType != XmlPullParser.START_TAG || !pullParser.getName().equals("plan"))){
			eventType  = pullParser.next();
		}
		if(eventType != XmlPullParser.END_DOCUMENT)
		{
			eventType = pullParser.nextTag(); // <date>
			String d = pullParser.nextText();
			try {
				tripPlan.setDate(dateTimeFormat.parse(d));
			} catch (ParseException e) {
				tripPlan.setDate(zuluDateTimeFormat.parse(d));
			}
	
			eventType = pullParser.nextTag(); // <from>
			tripPlan.setFrom(parseLocation(pullParser));
			eventType = pullParser.nextTag(); // <to>
			tripPlan.setTo(parseLocation(pullParser));
			eventType = pullParser.nextTag(); // <itineraries>
			parseItineraries(pullParser,tripPlan.getItineraries());
		}
		return new PlanResult(tripPlan);
	}


	private void parseItineraries(XmlPullParser pullParser,
			List<Itinerary> itineraries) throws XmlPullParserException, IOException, ParseException {
		
	    while(pullParser.nextTag()==XmlPullParser.START_TAG) //<itinerary>
	    {
	    	Itinerary itinerary = new Itinerary();
	    	while(pullParser.nextTag()==XmlPullParser.START_TAG)	    	
	    	{
		    	if (pullParser.getName().equals("walkDistance"))
		    	{
		    		itinerary.setWalkDistance(Double.parseDouble(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("duration"))
		    	{
		    		itinerary.setDuration(Long.parseLong(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("startTime"))
		    	{
		    		String d = pullParser.nextText();
					try {
						itinerary.setStartTime(dateTimeFormat.parse(d));
					} catch (ParseException e) {
						itinerary.setStartTime(zuluDateTimeFormat.parse(d));
					}
		    	}
		    	else if (pullParser.getName().equals("endTime"))
		    	{
		    		String d = pullParser.nextText();
		    		try {
						itinerary.setEndTime(dateTimeFormat.parse(d));
					} catch (ParseException e) {
						itinerary.setEndTime(zuluDateTimeFormat.parse(d));
					}
		    	}
		    	else if (pullParser.getName().equals("walkTime"))
		    	{
		    		itinerary.setWalkTime(Long.parseLong(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("transitTime"))
		    	{
		    		itinerary.setTransitTime(Long.parseLong(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("waitingTime"))
		    	{
		    		itinerary.setWaitingTime(Long.parseLong(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("transfers"))
		    	{
		    		itinerary.setTransfers(Integer.parseInt(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("legs"))
		    	{
		    		parseLegs(pullParser,itinerary.getLegs());
		    	}
		    	else
		    	{
		    		parseUnsupportedElement(pullParser);
		    	}
	    	}
	    	
	    	itineraries.add(itinerary);
	    	
	    }
		
		
		
	}


	private void parseLegs(XmlPullParser pullParser, List<Leg> legs)  throws XmlPullParserException, IOException, ParseException {
	    while(pullParser.nextTag()==XmlPullParser.START_TAG) //<leg>
	    {
	    	Leg leg = new Leg();
	    	leg.setMode(Mode.valueOf(pullParser.getAttributeValue(null, "mode")));
	    	leg.setRoute(pullParser.getAttributeValue(null, "route"));
	    	
	    	while(pullParser.nextTag()==XmlPullParser.START_TAG) 
	    	{	    	
		    	if (pullParser.getName().equals("duration"))
		    	{
		    		leg.setDuration(Long.parseLong(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("distance"))
		    	{
		    		leg.setDistance(Double.parseDouble(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("startTime"))
		    	{
		    		String d = pullParser.nextText();
		    		try {
						leg.setStartTime(dateTimeFormat.parse(d));
					} catch (ParseException e) {
						leg.setStartTime(zuluDateTimeFormat.parse(d));
					}
		    	}
		    	else if (pullParser.getName().equals("endTime"))
		    	{
		    		String d = pullParser.nextText();
		    		try {
						leg.setEndTime(dateTimeFormat.parse(d));
					} catch (ParseException e) {
						leg.setEndTime(zuluDateTimeFormat.parse(d));
					}
		    	}
		    	else if (pullParser.getName().equals("from"))
		    	{
		    		leg.setFrom(parseLocation(pullParser));
		    	}
		    	else if (pullParser.getName().equals("to"))
		    	{
		    		leg.setTo(parseLocation(pullParser));
		    	}
		    	else if (pullParser.getName().equals("legGeometry"))
		    	{
		    		parseGeometry(pullParser,leg.getGeometry());
		    	}
		    	else if (pullParser.getName().equals("steps"))
		    	{
		    		parseWalkSteps(pullParser,leg.getWalkSteps());
		    	}
		    	else
		    	{
		    		parseUnsupportedElement(pullParser);
		    	}
	    	}
	    	
	    	legs.add(leg);
	    }
		
		
		
		
	}
	
	/**
	 * Parse elements we do not recognize. Just walk the subtree until we reach the END_TAG which started it - useful for forward compatability
	 * 
	 * Assumes the pull parser currently points to the START_TAG of the element
	 * 
	 * When this method returns the pull parser will be positioned at the END_TAG of the element
	 * 
	 * @param pullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void parseUnsupportedElement(XmlPullParser pullParser) throws XmlPullParserException, IOException
	{
		int open_tags = 1;
		do
		{
			int event = pullParser.next();
			if (event == XmlPullParser.START_TAG)
				++open_tags;
			if (event == XmlPullParser.END_TAG)
				--open_tags;			
		} while(open_tags>0);
	}

	private void parseGeometry(XmlPullParser pullParser,
			List<Location> locations) throws XmlPullParserException, IOException 
	{
		    while(pullParser.nextTag()==XmlPullParser.START_TAG) 
		    {
		    	if (pullParser.getName().equals("points"))
		    	{
		    		String points = pullParser.nextText();
		    		// Adapted from http://jeffreysambells.com/posts/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java/
		    		int index = 0, len = points.length();
		    	    int lat = 0, lng = 0;

		    	    while (index < len) {
		    	        int b, shift = 0, result = 0;
		    	        do {
		    	            b = points.charAt(index++) - 63;
		    	            result |= (b & 0x1f) << shift;
		    	            shift += 5;
		    	        } while (b >= 0x20);
		    	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		    	        lat += dlat;

		    	        shift = 0;
		    	        result = 0;
		    	        do {
		    	            b = points.charAt(index++) - 63;
		    	            result |= (b & 0x1f) << shift;
		    	            shift += 5;
		    	        } while (b >= 0x20);
		    	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		    	        lng += dlng;

		    	        Location p = new  Location(null,((double) lat / 1E5),((double) lng / 1E5));
		    	        locations.add(p);
		    	    }		    		
		    		
		    	}
		    	else
		    	{
		    		pullParser.nextText();	    		
		    	}
	    }
		
	}

	
	
	private void parseWalkSteps(XmlPullParser pullParser,
			List<WalkStep> walkSteps) throws XmlPullParserException, IOException {
		
	    while(pullParser.nextTag()==XmlPullParser.START_TAG) //<walkSteps>
	    {
	    	WalkStep walkStep = new WalkStep();
			String address = "";
			double longitude = 0,latitude = 0;
	    	
	    	while(pullParser.nextTag()==XmlPullParser.START_TAG) {
	    		
		    	if (pullParser.getName().equals("distance"))
		    	{
		    		walkStep.setDistance(Double.parseDouble(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("streetName"))
		    	{
		    		address = pullParser.nextText();
		    	}
		    	else if (pullParser.getName().equals("lat"))
		    	{
		    		latitude = Double.parseDouble(pullParser.nextText());
		    	}
		    	else if (pullParser.getName().equals("lon"))
		    	{
		    		longitude = Double.parseDouble(pullParser.nextText());
		    	}
		    	else if (pullParser.getName().equals("relativeDirection"))
		    	{
		    		walkStep.setRelativeDirection(RelativeDirection.valueOf(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("absoluteDirection"))
		    	{
		    		walkStep.setAbsoluteDirection(AbsoluteDirection.valueOf(pullParser.nextText()));
		    	}
		    	else
		    	{
		    		parseUnsupportedElement(pullParser);
		    	}
	    	}
	    	
	    	walkStep.setLocation(new Location(address, latitude, longitude));
	    	walkSteps.add(walkStep);
	    }
		
	}


	private Location parseLocation(XmlPullParser pullParser) throws NumberFormatException, XmlPullParserException, IOException {
		String address = "";
		double longitude = 0,latitude = 0;
	    while(pullParser.nextTag()==XmlPullParser.START_TAG) 
	    {
	    	if (pullParser.getName().equals("address") || pullParser.getName().equals("name"))
	    	{
	    		address = pullParser.nextText();
	    	}
	    	else if (pullParser.getName().equals("lat"))
	    	{
	    		latitude = Double.parseDouble(pullParser.nextText());
	    	}
	    	else if (pullParser.getName().equals("lon"))
	    	{
	    		longitude = Double.parseDouble(pullParser.nextText());
	    	}
	    	else
	    	{
	    		parseUnsupportedElement(pullParser);
	    	}
	    	

	    	
	    }
	    return new Location(address, latitude, longitude);
		
	}
	

}
