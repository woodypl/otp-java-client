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
	
	
	private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	private Locale locale;
	
	private String host;
	private static final String uri = "opentripplanner-api-webapp/ws/plan";
	
	public Planner(String host) {
		this(host,Locale.getDefault());
	}
	
	public Planner(String host,Locale locale) {
		super();
		this.host = host;
		this.locale = locale;
	}

	
	public PlanResult generatePlan(PlanRequest planRequest) throws IOException, XmlPullParserException, ParseException
	{
		
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,locale);
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT,locale);
		
		
		Uri.Builder builder =  new Uri.Builder();
		builder.scheme("http");
		builder.authority(host);
		builder.appendEncodedPath(uri);
		builder.appendQueryParameter("fromPlace",planRequest.getFrom().getCoordinateString());		
		builder.appendQueryParameter("toPlace",planRequest.getTo().getCoordinateString());		
		builder.appendQueryParameter("date",dateFormat.format(planRequest.getDate()));		
		builder.appendQueryParameter("time",timeFormat.format(planRequest.getDate()));		
		
		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");
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
	
	private PlanResult parseXMLResponse(InputStream contentStream) throws IOException, XmlPullParserException, ParseException
	{
		TripPlan tripPlan = new TripPlan();
		DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
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
			tripPlan.setDate(dateTimeFormat.parse(pullParser.nextText()));
	
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
		
		DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
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
		    		itinerary.setStartTime(dateTimeFormat.parse(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("endTime"))
		    	{
		    		itinerary.setEndTime(dateTimeFormat.parse(pullParser.nextText()));
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
		    		pullParser.nextText();
		    	}
	    	}
	    	
	    	itineraries.add(itinerary);
	    	
	    }
		
		
		
	}


	private void parseLegs(XmlPullParser pullParser, List<Leg> legs)  throws XmlPullParserException, IOException, ParseException {
		DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
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
		    		leg.setStartTime(dateTimeFormat.parse(pullParser.nextText()));
		    	}
		    	else if (pullParser.getName().equals("endTime"))
		    	{
		    		leg.setEndTime(dateTimeFormat.parse(pullParser.nextText()));
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
		    		String tagName = pullParser.getName();
		    		// Jump the end of this section
		    		do
		    		{
		    			pullParser.next();
		    		} while(pullParser.getEventType()!=XmlPullParser.END_TAG || !pullParser.getName().equals(tagName));
		    	}
	    	}
	    	
	    	legs.add(leg);
	    }
		
		
		
		
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
		    		pullParser.nextText();
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
	    	else if (pullParser.getName().equals("stopId"))
	    	{
	    		// Skip this subtree
	    		do
	    		{
	    			pullParser.next();
	    			
	    		} while(pullParser.getEventType()!=XmlPullParser.END_TAG || !pullParser.getName().equals( "stopId"));
	    	}
	    	else
	    	{
	    		pullParser.nextText();	    		
	    	}
	    	

	    	
	    }
	    return new Location(address, latitude, longitude);
		
	}
	

}
