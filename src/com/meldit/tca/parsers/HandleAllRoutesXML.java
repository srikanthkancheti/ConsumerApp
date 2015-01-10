package com.meldit.tca.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class HandleAllRoutesXML {
	
	private XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	public ArrayList<RoutesModel> routes;
	private RoutesModel route;
	private String text;
	
	public HandleAllRoutesXML() {
		// TODO Auto-generated constructor stub
		routes = new ArrayList<RoutesModel>();
	}

	public List<RoutesModel> getRoutes() {
        return routes;
    }
	
	public ArrayList<RoutesModel> parseRoutesXML(InputStream is) {
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
                if (tagname.equalsIgnoreCase("Route")) {
                    // create a new instance of employee
                	route = new RoutesModel();
                }
                break;

            case XmlPullParser.TEXT:
                text = myparser.getText();
                break;

            case XmlPullParser.END_TAG:
                if (tagname.equalsIgnoreCase("Route")) {
                    // add employee object to list
                	routes.add(route);
                } else if (tagname.equalsIgnoreCase("routeNo")) {
                	route.setRouteNo(text);
                } else if (tagname.equalsIgnoreCase("sourceName")) {
                	route.setSourceName(text);
                } else if (tagname.equalsIgnoreCase("destinationName")) {
                	route.setDestinationName(text);
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

    return routes;
}
	
	public class RoutesModel {
		
		String route_no, source_name, destinatio_name;
		
		public String getRouteNo(){
			return route_no;
		}
		
		public void setRouteNo(String routeNo){
			this.route_no = routeNo;
		}
		
		public String getSourceName(){
			return source_name;
		}
		
		public void setSourceName(String sourceName){
			this.source_name = sourceName;
		}
		
		public String getDestinationName(){
			return destinatio_name;
		}
		
		public void setDestinationName(String destinationName){
			this.destinatio_name = destinationName;
		}

	}

}
