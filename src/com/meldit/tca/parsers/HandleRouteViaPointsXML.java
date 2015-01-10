package com.meldit.tca.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.gms.maps.model.LatLng;

public class HandleRouteViaPointsXML {
	
		private RouteViaPointsModel routeVia;
		private XmlPullParserFactory xmlFactoryObject;
		public volatile boolean parsingComplete = true;
		public ArrayList<RouteViaPointsModel> routeViaPoints;
		private String text;
		private LatLng viaLatitudeLongitude;
		
		public HandleRouteViaPointsXML() {
			// TODO Auto-generated constructor stub
			routeViaPoints = new ArrayList<RouteViaPointsModel>();
		}

		public List<RouteViaPointsModel> getRouteViaPoints() {
	        return routeViaPoints;
	    }


	public ArrayList<RouteViaPointsModel> parseRouteViaXML(InputStream is) {
		// TODO Auto-generated method stub
		try{
			
		xmlFactoryObject = XmlPullParserFactory.newInstance();
        XmlPullParser myparser = xmlFactoryObject.newPullParser();

        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        myparser.setInput(is, null);
        
        int eventType = myparser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = myparser.getName();
            switch (eventType) {
            case XmlPullParser.START_TAG:
                if (tagname.equalsIgnoreCase("ViaNames")) {
                    // create a new instance of employee
                	routeVia = new RouteViaPointsModel();
                }
                break;

            case XmlPullParser.TEXT:
                text = myparser.getText();
                break;

            case XmlPullParser.END_TAG:
                if (tagname.equalsIgnoreCase("ViaNames")) {
                    // add employee object to list
                	routeViaPoints.add(routeVia);
                } else if (tagname.equalsIgnoreCase("vName")) {
                	routeVia.setViaName(text);
                } else if (tagname.equalsIgnoreCase("vNameId")) {
                	routeVia.setViaNameId(text);
                } else if (tagname.equalsIgnoreCase("vLatLong")) {
                	String latlng = text;
                	StringTokenizer tokensCurrent = new StringTokenizer(latlng, "  ");
                	double latitude = Double.parseDouble(tokensCurrent.nextToken());
                	double longitude = Double.parseDouble(tokensCurrent.nextToken());
                	viaLatitudeLongitude = new LatLng(latitude, longitude);
                	routeVia.setViaLatLng(viaLatitudeLongitude);
                } 

                break;

            default:
                break;
            }
            eventType = myparser.next();
        }

    } catch (XmlPullParserException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
		return routeViaPoints;
	}

	
	public class RouteViaPointsModel {

		String via_name, via_id;
		LatLng via_latlng;
		
		public String getViaName(){
			return via_name;
		}
		
		public void setViaName(String vName){
			this.via_name = vName;
		}
		
		public String getViaNameId(){
			return via_id;
		}
		
		public void setViaNameId(String vNameId){
			this.via_id = vNameId;
		}
		
		public LatLng getViaLatLng(){
			return via_latlng;
		}
		
		public void setViaLatLng(LatLng viaLatLng){
			this.via_latlng = viaLatLng;
		}
	}


}
