package com.meldit.tca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.meldit.tca.db.DbAdapter;
import com.meldit.tca.parsers.HandleAllRoutesXML.RoutesModel;
import com.meldit.tca.parsers.HandleAvaliableServicesXML.ServiceModel;
import com.meldit.tca.parsers.HandleRouteViaPointsXML.RouteViaPointsModel;

public class Utilities {

	public static boolean signin;
	public static boolean exit;
	public static String first_name;
	public static String last_name;
	public static String uname;
	public static String pword;
	public static String email;
	public static String contact;
	public static boolean flag = true;
	public static Vector <SetterAndGetters>TripsVector ;
	public static ArrayList<ServiceModel> servicesList;
	public static ArrayList<RoutesModel> routesList;
	public static ArrayList<RouteViaPointsModel> routeViaPoints;
	public static HashMap<String, List<String>> listDataChild;
	public static DbAdapter dbAdapter;	
	
	
	public static String availble_trips_url =
			//"http://192.168.1.200:8080/TICS/services/servicemanagement/getpossibleServices?sourcename=";
			//"http://dev.meldit.com:8080/TICS/services/servicemanagement/getpossibleServices?sourcename=";
			"http://dev.meldit.com:8080/TICS/services/servicemanagement/getservicedetails?userid=63&connectionid=1432463&datastoreName=APSRTC&flagStopsOnly=false&svcFlagViaNames=false&flagConnections=false&flagConDetails=false&rutFlagViaNames=false&flagMatchExact=false&sourceName=";
	public static String forgetpassword_url = 
			//"http://192.168.1.200:8080/TICS/services/consumermanagement/consumerForgotPassword?userName=";
			"http://dev.meldit.com:8080/TICS/services/consumermanagement/consumerForgotPassword?userName=";
	public static String consumer_login_url = 
			//"http://192.168.1.200:8080/TICS/services/consumermanagement/consumerLogin";
			"http://dev.meldit.com:8080/TICS/services/consumermanagement/consumerLogin";
	public static String registration_url = 
			//"http://192.168.1.200:8080/TICS/services/consumermanagement/consumerCreate";
			"http://dev.meldit.com:8080/TICS/services/consumermanagement/consumerCreate";
	public static String getAllRoutes_url =
			"http://dev.meldit.com:8080/TICS/services/routemanagement/getall?userid=63&connectionid=1432463&datastoreName=APSRTC&flagViaNames=false&PNO=";
	public static String getRouteViaPoints_url =
			"http://dev.meldit.com:8080/TICS/services/routemanagement/getDetailsByRouteNo?userid=63&connectionid=1432463&datastoreName=APSRTC&flagViaNames=true&routeNo=";
	
}
