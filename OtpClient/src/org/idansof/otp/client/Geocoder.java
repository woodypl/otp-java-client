package org.idansof.otp.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpProtocolParams;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.net.Uri;
import android.net.http.AndroidHttpClient;

public class Geocoder {
	
	private String host;
	private static final String uri = "opentripplanner-geocoder/geocode";
	
	
	
	public Geocoder(String host) {
		super();
		this.host = host;
	}

	public GeocoderResult geodecode(String address) throws IOException, XmlPullParserException
	{
		Uri.Builder builder =  new Uri.Builder();
		builder.scheme("http");
		builder.authority(host);
		builder.appendEncodedPath(uri);
		builder.appendQueryParameter("address",address);
		
		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");
		HttpProtocolParams.setContentCharset(androidHttpClient.getParams(), "utf-8");
		HttpUriRequest httpUriRequest = new HttpGet(	builder.build().toString());
		HttpResponse httpResponse = androidHttpClient.execute(httpUriRequest);
		
		InputStream contentStream = httpResponse.getEntity().getContent();
		return parseXMLResponse(contentStream);
	}
	
	private GeocoderResult parseXMLResponse(InputStream contentStream) throws IOException, XmlPullParserException
	{
		XmlPullParser pullParser = new KXmlParser();
		pullParser.setInput(contentStream, "UTF-8");
		int eventType = pullParser.getEventType();				
		// Skip to the first tag
		while (eventType != XmlPullParser.START_TAG){
			eventType  = pullParser.next();
		}
		
		List<Location> locations = new ArrayList<Location>();
		
		// Now at <geocoderResults> 
		eventType = pullParser.nextTag(); // <count>
		// skip count...
		do {
			eventType  = pullParser.next();
		} while (eventType != XmlPullParser.START_TAG);
		
	    while(pullParser.nextTag()==XmlPullParser.START_TAG) // <result>
	    {
	    	
	    	pullParser.nextTag(); // <description>
	    	String description = pullParser.nextText();
	    	pullParser.nextTag(); // <lat>
	    	double latitude = Double.parseDouble(pullParser.nextText());
	    	pullParser.nextTag(); // <lon>
	    	double longitude = Double.parseDouble(pullParser.nextText());
	    	
	    	locations.add(new Location(description, latitude, longitude));
	    	
	    	pullParser.nextTag();
	    }
	    return new GeocoderResult(locations);
		
		
	}
	
	
	

}
