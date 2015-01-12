package com.meldit.tca.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.meldit.tca.Utilities;

public class HandleAvaliableServicesXML {
	
	XmlPullParserFactory xmlFactoryObject;
	public volatile boolean parsingComplete = true;
	private ServiceModel service;
	private String text, arrival_time, departure_time;

	
	public HandleAvaliableServicesXML() {
		// TODO Auto-generated constructor stub
		Utilities.servicesList = new ArrayList<ServiceModel>();
	}

	public List<ServiceModel> getEmployees() {
        return Utilities.servicesList;
    }

	public ArrayList<ServiceModel> parseXML(InputStream is) {
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
                if (tagname.equalsIgnoreCase("service")) {
                    // create a new instance of employee
                	service = new ServiceModel();
                }
                break;

            case XmlPullParser.TEXT:
                text = myparser.getText();
                break;

            case XmlPullParser.END_TAG:
                if (tagname.equalsIgnoreCase("service")) {
                    // add employee object to list
                	Utilities.servicesList.add(service);
                } else if (tagname.equalsIgnoreCase("serviceName")) {
                	service.setServiceName(text);
                } else if (tagname.equalsIgnoreCase("serviceNO")) {
                	service.setService_id(text);
                } else if (tagname.equalsIgnoreCase("routeNo")) {
                	service.setRoute_no(text);
                } else if (tagname.equalsIgnoreCase("arrivalTime")) {
                	arrival_time = text;
                } else if (tagname.equalsIgnoreCase("departureTime")) {
                	departure_time = text;
                	
                	String timings = arrival_time +" - "+ departure_time;
                	service.setTiminngs(timings);
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

    return Utilities.servicesList;
}
	

	
	public class ServiceModel {
		
		String service_name, source,destination,service_id,route_no,timinngs;
		
		public String getServiceName(){
			return service_name;
		}
		
		public void setServiceName(String serviceName){
			this.service_name = serviceName;
		}
		public String getService_id() {
			return service_id;
		}

		public void setService_id(String service_id) {
			this.service_id = service_id;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}

		public String getRoute_no() {
			return route_no;
		}

		public void setRoute_no(String route_no) {
			this.route_no = route_no;
		}

		public String getTiminngs() {
			return timinngs;
		}

		public void setTiminngs(String timinngs) {
			this.timinngs = timinngs;
		}

	}

}
