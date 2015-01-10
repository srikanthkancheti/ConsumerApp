package com.meldit.tca;

import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.database.Cursor;
import android.net.Credentials;
import android.util.Log;



public class WebServiceParsers {
	
	
	SetterAndGetters obj_settersAndGetters;
	
	public WebServiceParsers(InputStream input) {
		// TODO Auto-generated constructor stub
		parsing(input);
	}
	public WebServiceParsers() {
		// TODO Auto-generated constructor stub
	}
	Document doc;
	NodeList nodes, serviceList;
	Element element, stopsElement, serviceIDElement;
	//Vector<SetterAndGetterMethods> stopInfovector;

	public void parsing(InputStream input) {

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			doc = builder.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			nodes = doc.getElementsByTagName("MD_Service");
			if (Utilities.flag)Log.i("list", "===============================" + nodes.getLength());

			if (nodes.getLength() > 0) {
				firstTagParsing();
				// infomercialParsing();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void firstTagParsing() {
		// TODO Auto-generated method stub
		Utilities.TripsVector = new Vector<SetterAndGetters>();
		element = (Element) nodes.item(0);
		serviceList = element.getElementsByTagName("service");
		Log.i("stopsList","========================" + serviceList.getLength());
		if(serviceList.getLength() >0){
		for(int i =0;i<serviceList.getLength();i++){
	    obj_settersAndGetters = new SetterAndGetters();
		NodeList serviceName = element.getElementsByTagName("serviceName");
		Element serviceNameElement = (Element) serviceName.item(i);
		if (Utilities.flag)
			Log.i("serviceName", "==============================="
					+ serviceNameElement.getTextContent());
		

		NodeList serviceID = element.getElementsByTagName("serviceID");
		serviceIDElement = (Element) serviceID.item(i);
		if (Utilities.flag)
			Log.i("serviceID", "==============================="
					+ serviceIDElement.getTextContent());
		obj_settersAndGetters.setService_id(serviceIDElement.getTextContent());

		NodeList routeNo = element.getElementsByTagName("routeNo");
		Element routeNoElement = (Element) routeNo.item(i);
		if (Utilities.flag)
			Log.i("routeNo", "==============================="
					+ routeNoElement.getTextContent());
		obj_settersAndGetters.setRoute_no(routeNoElement.getTextContent());
		NodeList departureTime = element.getElementsByTagName("departureTime");
		Element departureTimeElement = (Element) departureTime.item(i);
		if (Utilities.flag)
			Log.i("departureTime", "==============================="
					+ departureTimeElement.getTextContent());
		NodeList arrivalTime = element.getElementsByTagName("arrivalTime");
		Element arrivalTimeElement = (Element) arrivalTime.item(i);
		if (Utilities.flag)
			Log.i("arrivalTime", "==============================="
					+ arrivalTimeElement.getTextContent());
		obj_settersAndGetters.setTiminngs(arrivalTimeElement.getTextContent()+"-"+departureTimeElement.getTextContent());
		Utilities.TripsVector.add(obj_settersAndGetters);
		}
		}else{
			Log.i("parsing ", "No Services Available ****************** ");
		}
	}
	
	public String[] parseResult(InputStream input){

		String [] credentials = new String [5];
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			doc = builder.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			
			nodes = doc.getElementsByTagName("MD_CONSUMER_DETAILS");
			if (Utilities.flag)Log.i("list", "===============================" + nodes.getLength());

			if (nodes.getLength() > 0) {
				element = (Element) nodes.item(0);
				NodeList userName = element.getElementsByTagName("ticsUserName");
				Element userNameElement = (Element) userName.item(0);
				if (Utilities.flag)
				Log.i("userName", "==============================="+ userNameElement.getTextContent());
				credentials[0]= userNameElement.getTextContent();
				
				NodeList password = element.getElementsByTagName("ticsPassword");
				Element passwordElement = (Element) password.item(0);
				if (Utilities.flag)
				Log.i("password", "==============================="	+ passwordElement.getTextContent());
				credentials[1]= passwordElement.getTextContent();								
				
				NodeList emailid = element.getElementsByTagName("email");
				Element emailIdElement = (Element) emailid.item(0);
				if(Utilities.flag)
					Log.i("Emailid", "============================"+emailIdElement.getTextContent());
				credentials[2] = emailIdElement.getTextContent();
				
				NodeList phonenumber = element.getElementsByTagName("phone");
				Element phoneNumberElement = (Element) phonenumber.item(0);
				if(Utilities.flag)
					Log.i("PhoneNumber", "==========================="+phoneNumberElement.getTextContent());
				credentials[3] = phoneNumberElement.getTextContent();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return credentials;
	}

}
