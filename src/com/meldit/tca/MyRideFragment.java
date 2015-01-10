package com.meldit.tca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.meldit.tca.db.DbAdapter;
import com.meldit.tca.map.DirectionsJSONParser;
import com.meldit.tca.parsers.HandleAvaliableServicesXML;
import com.meldit.tca.parsers.HandleAvaliableServicesXML.ServiceModel;


//public class MyRideActivity extends FragmentActivity implements LocationListener {
public class MyRideFragment extends Fragment implements LocationListener {
	String currentDateTime;

	ImageView logo,user_icon;
	boolean flag;
	MyExpandableListAdapter expListAdapter;
	ListView mListView;
	ExpandableListView expListView;
	private ActionBar actionBar;
	
	//private GoogleMap mMap;
	CameraPosition CameraPosition,current_camera_position;
	LinearLayout ll_listview,ll_from_ride,ll_to_ride ;
	Button findmyride;
	FrameLayout frame;
	public ArrayAdapter<String> adapter,adapter2;
	AutoCompleteTextView autoText1,autoText2;
	Vector <Double> coordinates = new Vector<Double>();
	RelativeLayout popup,share,favorite,save,close, rl_findmyride;
	PopupWindow popupWindow;  
	String route_no,timinngs,date,fare, value = null;
	String [] source,destination;
	int count = 0, auto_seperate, popup_position;
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		 if (rootView != null) {
		        ViewGroup parent = (ViewGroup) rootView.getParent();
		        if (parent != null)
		            parent.removeView(rootView);
		    }
		    try {
		    	rootView = inflater.inflate(R.layout.myride, container, false);
		    } catch (InflateException e) {
		        /* map is already there, just return view as it is */
		    }
		    
		    
	   findmyride = (Button) rootView.findViewById(R.id.button_findmyride);
	   findmyride.setOnClickListener(listener_findmyride);
	   frame = (FrameLayout) rootView.findViewById(R.id.map_frame_layout);
	   frame.setVisibility(View.INVISIBLE);
	   ll_from_ride = (LinearLayout) rootView.findViewById(R.id.ll_from_ride);
	   ll_to_ride = (LinearLayout) rootView.findViewById(R.id.ll_to_ride);
	   rl_findmyride = (RelativeLayout) rootView.findViewById(R.id.rl_findMyRide);
	   
	   adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item);
	   adapter2 = new ArrayAdapter<String>(getActivity(),R.layout.list_item);
	   adapter.setNotifyOnChange(true);
	   autoText1 = (AutoCompleteTextView) rootView.findViewById(R.id.autoComplete1);
	   autoText1.setOnItemClickListener(listener_autocomplete);
	   autoText1.setAdapter(adapter);
	   
	   autoText2 = (AutoCompleteTextView) rootView.findViewById(R.id.autoComplete2);
	   autoText2.setOnItemClickListener(listener_autocomplete2);
	   autoText2.setAdapter(adapter2);
	   
	    ll_listview = (LinearLayout) rootView.findViewById(R.id.ll_listview);
	    expListView = (ExpandableListView) rootView.findViewById(R.id.exp_list);
		expListAdapter = new MyExpandableListAdapter(getActivity(), Utilities.servicesAdapter, Utilities.listDataChild);
        
		ll_listview.setVisibility(View.INVISIBLE);
		actionBar = getActivity().getActionBar();
		
		hideKeypad();
		
		/**
		 * here the following code hides the from, to and find my ride button views
		 * and displays the map view with from and to locations and expands the listview childs  
		 */
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				rl_findmyride.setVisibility(View.INVISIBLE);
						
//				//Display Map 
				frame.setVisibility(View.VISIBLE);				
				//hidesoftKeyBoard();
				displayMap();
				
				return true;
			}

//			private void hidesoftKeyBoard() {
//				// TODO Auto-generated method stub
//			
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(findmyride.getWindowToken(), 0);				
//			}
		});
		
		frame.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#110000ff")));
				actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#220000ff")));
				return true;
			}
		});
		
		/**
		 * This code will execute while user collapse the exrtended listview 
		 * here user collapses the child list the map view is gone and from, to anf find ride details are come to foreground.
		 */
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				rl_findmyride.setVisibility(View.VISIBLE);
				frame.setVisibility(View.INVISIBLE);
				
			}
		});
		
		autoText1.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count%3 == 1) {
				auto_seperate =1;
				availableLocations(count,auto_seperate);
				}
			
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
			// TODO Auto-generated method stub
			
			}
			
			public void afterTextChanged(Editable s) {
			
			}
			});
		
			autoText2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int count) {
				// TODO Auto-generated method stub
				if (count%3 == 1) {
				auto_seperate =2;
				availableLocations(count,auto_seperate);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			return rootView;
	
	}


	private void availableLocations(int count,int i) {
		// TODO Auto-generated method stub
		if(i==1){
			if(null != adapter)adapter.clear();
			//now pass the argument in the textview to the task
			Log.i("GetPlaces", "=============================char "+autoText1.getText().toString());
			GetPlaces task = new GetPlaces();
			value = autoText1.getText().toString();
			task.execute();
		}else{
			if(null != adapter2)adapter2.clear();
			//now pass the argument in the textview to the task
			Log.i("GetPlaces", "=============================char "+autoText2.getText().toString());
			GetPlaces task = new GetPlaces();
			value = autoText2.getText().toString();
			task.execute();
		
		}
	}
	
	ArrayList<String> predictionsArr ; 
	String latlong;
	double lat, lng;
	
	class GetPlaces extends AsyncTask<Void, Void,Void>
	{
	
	@Override
	 // three dots is java for an array of strings
	protected Void doInBackground(Void... arg0)
	{
	Log.i("doInBackground", "=============================args.toString() "+value.toString());
	try
	{
		predictionsArr = new ArrayList<String>(); 
	            URL googlePlaces = new URL(
	                    "http://maps.googleapis.com/maps/api/geocode/json?address="+ 
	            		URLEncoder.encode(value.toString(), "UTF-8") +
	            		"&sensor=true");
	            URLConnection tc = googlePlaces.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    tc.getInputStream()));
	
	            String line;
	            StringBuffer sb = new StringBuffer();
	            //take Google's legible JSON and turn it into one big string.
	            while ((line = in.readLine()) != null) {
	            sb.append(line);
	            }
	                            //turn that string into a JSON object
	            JSONObject predictions = new JSONObject(sb.toString());	
	                           //now get the JSON array that's inside that object            
	            JSONArray ja = new JSONArray(predictions.getString("results"));
	
	                for (int i = 0; i < ja.length(); i++) {
	                    JSONObject jo = (JSONObject) ja.get(i);
	                                    //add each entry to our array
	                    predictionsArr.add(jo.getString("formatted_address"));
	                    JSONObject geoJson=jo.getJSONObject("geometry");
	                    JSONObject locJson=geoJson.getJSONObject("location");
	                   lat =  Double.parseDouble(locJson.getString("lat"));
	                   lng = Double.parseDouble(locJson.getString("lng"));
	                   Log.i("doInBackground", "==========lat "+lat+"=============lng "+lng);
	                      
	                }
	} catch (IOException e)
	{
	
	Log.i("YourApp", "GetPlaces : doInBackground", e);
	
	} catch (JSONException e)
	{
	
	Log.i("YourApp", "GetPlaces : doInBackground", e);
	
	}
	return null;
	
	//return predictionsArr;
	
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
	Log.i("YourApp", "onPostExecute : " + predictionsArr.size());
	//update the adapter
	//attach the adapter to textview
	if(predictionsArr.size()>0){
	if(auto_seperate ==1){
		Log.i("YourApp", "onPostExecute : =========================adapter 1");
		adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item);
		adapter.setNotifyOnChange(true);
		autoText1.setAdapter(adapter);
		Source = new LatLng(lat, lng);
		Log.i("YourApp", "onPostExecute : =========================Source LatLng:"+Source.toString());
	}
	if(auto_seperate == 2){
		Log.i("YourApp", "onPostExecute : =========================adapter 2");
		adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_item);
		adapter2.setNotifyOnChange(true);
		autoText2.setAdapter(adapter2);	
		 Destination = new LatLng(lat, lng);
		 Log.i("YourApp", "onPostExecute : =========================Destination LatLng:"+Destination.toString());
	}
	for (String string : predictionsArr)
	{
	if(auto_seperate ==1){
	Log.i("YourApp", "onPostExecute : result = " + string);
	adapter.add(string);
	adapter.notifyDataSetChanged();
	}
	if (auto_seperate == 2) {
		Log.i("YourApp", "onPostExecute : result = " + string);
		adapter2.add(string);
		adapter2.notifyDataSetChanged();
	}
	
	}
  }else{
	 // Toast.makeText(getApplicationContext(), "NO Results", Toast.LENGTH_SHORT).show();
  }
 }
}
	
	OnItemClickListener listener_autocomplete = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View parent1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			value = (String) parent.getItemAtPosition(position);
			new getPlacesTask().execute(value);
		}
	};
	
	OnItemClickListener listener_autocomplete2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View parent1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			value = (String) parent.getItemAtPosition(position);
			new getPlacesTask().execute(value);
			
		}
		
	};
	
	OnClickListener listener_findmyride = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			count = 1;
			//hidesoftKeyBoard();
			if(autoText1.getText().toString().length()> 0 && autoText2.getText().toString().length() > 0){
			callWebService();
			
			hideKeypad();
			} else
				Toast.makeText(getActivity(),"Don't Leave any of above fields empty", Toast.LENGTH_SHORT).show();
			
		}

		private void callWebService() {
			// TODO Auto-generated method stub
			source = autoText1.getText().toString().split(",");
			destination = autoText2.getText().toString().split(",");
			new AvailableServices().execute(source[0],destination[0]);
			
		}
		String result;
		HttpResponse response;
		class  AvailableServices extends AsyncTask<String, Void, Void>{

			@Override
			protected Void doInBackground(String... params) {
					// TODO Auto-generated method stub
				try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(Utilities.availble_trips_url+params[0]+"&destinationName="+params[1]);
			        
		           response = httpClient.execute(httpGet);
			           
			       result = EntityUtils.toString(response.getEntity());
			       Log.i("result","================================"+result);
			         

				} catch (ClientProtocolException cpe) {
						System.out.println("First Exception becaz of HttpResponese :" + cpe);
						cpe.printStackTrace();
					} catch (IOException ioe) {
						System.out.println("Second Exception becaz of HttpResponse :" + ioe);
						ioe.printStackTrace();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void info_result) {
				// TODO Auto-generated method stub
				super.onPostExecute(info_result);
				if(null!= result){
					if(result.contains("No Services")){
						Toast.makeText(getActivity(),"No Services Available", Toast.LENGTH_SHORT).show();
					}else{
						InputStream is = new ByteArrayInputStream(result.getBytes());
						//new WebServiceParsers(is);
						HandleAvaliableServicesXML parseService = new HandleAvaliableServicesXML();
						Utilities.servicesAdapter = parseService.parseXML(is);
						
						findmyride.setVisibility(View.VISIBLE);
						ll_from_ride.setVisibility(View.VISIBLE);
						ll_to_ride.setVisibility(View.VISIBLE);
						ll_listview.setVisibility(View.VISIBLE);
						//myListView.setAdapter(myAdapter);
						// setting list adapter
				        expListView.setAdapter(expListAdapter);
				        //mapVisibility();
						//myListView.setOnItemClickListener(listener_list);
					}
			  }else{
				  Toast.makeText(getActivity(),"No Services Available", Toast.LENGTH_SHORT).show();
			  }
			}
						
		}
		

	};
	
	
	
	class getPlacesTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			Log.i("doInBackground", "============================value="+params[0]);
			try
			{
				predictionsArr = new ArrayList<String>(); 
			            URL googlePlaces = new URL(
			                    "http://maps.googleapis.com/maps/api/geocode/json?address="+ 
			            		URLEncoder.encode(params[0], "UTF-8") +
			            		"&sensor=true");
			            URLConnection tc = googlePlaces.openConnection();
			            BufferedReader in = new BufferedReader(new InputStreamReader(
			                    tc.getInputStream()));
			
			            String line;
			            StringBuffer sb = new StringBuffer();
			            //take Google's legible JSON and turn it into one big string.
			            while ((line = in.readLine()) != null) {
			            sb.append(line);
			            }
			                            //turn that string into a JSON object
			            JSONObject predictions = new JSONObject(sb.toString());	
			                           //now get the JSON array that's inside that object            
			            JSONArray ja = new JSONArray(predictions.getString("results"));
			
			              //  for (int i = 0; i < ja.length(); i++) {
			                    JSONObject jo = (JSONObject) ja.get(0);
			                     //add each entry to our array
			                    predictionsArr.add(jo.getString("formatted_address"));
			                    JSONObject geoJson=jo.getJSONObject("geometry");
			                    JSONObject locJson=geoJson.getJSONObject("location");
			                    if(null!= locJson && locJson.length() != 0){
			                    coordinates.add(Double.parseDouble(locJson.getString("lat")));
			                    coordinates.add(Double.parseDouble(locJson.getString("lng")));
			                    Log.i("getPlacesTask", "::::::::: lat : "+locJson.getString("lat"));
			                    Log.i("getPlacesTask", "::::::::: lng : "+locJson.getString("lng"));
			                    }else{
			                    	Log.i("getPlacesTask", "::::::::: doInBackground location object is null");
			                    }
			                //}
			} catch (IOException e)
			{
			
			Log.i("YourApp", "GetPlaces : doInBackground", e);
			
			} catch (JSONException e)
			{
			
			Log.i("YourApp", "GetPlaces : doInBackground", e);
			
			}
			return null;
			
			//return predictionsArr;
			
			}
		
	}
	
		GoogleMap mGoogleMap;
		ArrayList<LatLng> mMarkerPoints;
		double mLatitude=0;
	    double mLongitude=0;
	    Geocoder geocoder;
	    List<Address> addresses;
	    LatLng Source, Destination, narketpalli, suryapet, kodad, nandigama;
	    String addressText = " ";
	    Context mContext;
		private void displayMap() {
			// TODO Auto-generated method stub

			// Getting Google Play availability status
	        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

	        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
	            dialog.show();

	        }else { // Google Play Services are available
			
				// Initializing 
				mMarkerPoints = new ArrayList<LatLng>();
				
				// Getting reference to SupportMapFragment of the activity_main
				SupportMapFragment fm = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.map_fragment);
				
				
				
				// Getting Map for the SupportMapFragment
				mGoogleMap = fm.getMap();
				
				// Enable MyLocation Button in the Map
				mGoogleMap.setMyLocationEnabled(true);
				
				// Getting LocationManager object from System Service LOCATION_SERVICE
		        LocationManager locationManager = (LocationManager) MyAccount.getCustomAppContext().getSystemService(Context.LOCATION_SERVICE);
		
		        // Creating a criteria object to retrieve provider
		        Criteria criteria = new Criteria();
		
		        // Getting the name of the best provider
		        String provider = locationManager.getBestProvider(criteria, true);
		        	        
	            locationManager.requestLocationUpdates(provider, 20000, 0, this);	
	            
	            mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
					
					@Override
					public void onMapClick(LatLng arg0) {
						// TODO Auto-generated method stub
						//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#11000000")));
						//actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#11000000")));
						//actionBar.hide();
					}

					
				});
	           
//	            narketpalli = new LatLng(17.2027217, 79.1971742);
//	            suryapet = new LatLng(17.141447, 79.623585);
//	            kodad = new LatLng(17.0006699, 79.9606304);
//	            nandigama = new LatLng(16.7725089, 80.285935);
	            
	         // define point to center on	        
		        CameraUpdate panToOrigin = CameraUpdateFactory.newLatLng(Source);
		        mGoogleMap.moveCamera(panToOrigin);
		     // set zoom level with animation
		        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
						
	    		mMarkerPoints.add(Source);
	    		mMarkerPoints.add(Destination);
//	    		mMarkerPoints.add(narketpalli);
//	    		mMarkerPoints.add(suryapet);
//	    		mMarkerPoints.add(kodad);
//	    		mMarkerPoints.add(nandigama);
	    		
	        	
	        	// Creating MarkerOptions
	    		MarkerOptions options = new MarkerOptions();
	    		
	    		// Setting the position of the marker
	    		options.position(Source);
	    		options.position(Destination);
	   
	    		// Add new marker to the Google Map Android API V2
	    		mGoogleMap.addMarker(new MarkerOptions().position(Source).title(Source.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_red)));
				mGoogleMap.addMarker(new MarkerOptions().position(Destination).title(Destination.toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_red)));		
				if(mMarkerPoints.size() >= 3){
					
					options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					
					for(int i=2;i<mMarkerPoints.size();i++){
						
						LatLng points  = (LatLng) mMarkerPoints.get(i);
						geocoder = new Geocoder(getActivity(), Locale.getDefault());
						try {
							addresses = geocoder.getFromLocation(points.latitude, points.longitude, 1);
							
							if (addresses!=null&&addresses.size() > 0) {
				                Address address = addresses.get(0);
				                addressText = String.format(
				                    "%s, %s, %s",
				                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
				                    address.getLocality(),
				                    address.getCountryName());
				                Log.v(getClass().getName(), "address :::::::: "+addressText);
				                
				            }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
						
						options = new MarkerOptions();
				        options.position(points);
				        options.title(addressText);

				        mGoogleMap.addMarker(options).showInfoWindow();
					}
				}else{
					
				}
				// Checks, whether start and end locations are captured
						if(mMarkerPoints.size() > 0){	
							
							LatLng origin = mMarkerPoints.get(0);
							LatLng dest = mMarkerPoints.get(1);
							
							Log.i("Source location::::::::::", "::::::::: latlng : "+origin.toString());
							Log.i("Destination location::::::::::", "::::::::: latlng : "+dest.toString());
							
							// Getting URL to the Google Directions API
							String url = getDirectionsUrl(origin, dest);				
							
							DownloadTask downloadTask = new DownloadTask();
							
							// Start downloading json data from Google Directions API
							downloadTask.execute(url);
						}					
		
	        }		
	     }
		


		protected void hideKeypad() {
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager) MyAccount.getCustomAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(findmyride.getWindowToken(), 0);		
		}


		private String getDirectionsUrl(LatLng origin, LatLng dest) {
			// TODO Auto-generated method stub
			// Origin of route
			String str_origin = "origin="+origin.latitude+","+origin.longitude;
			
			// Destination of route
			String str_dest = "destination="+dest.latitude+","+dest.longitude;			
						
			// Sensor enabled
			String sensor = "sensor=false";			
						
			// Building the parameters to the web service
			String parameters = str_origin+"&"+str_dest+"&"+sensor;
						
			// Output format
			String output = "json";
			
			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;		
			
			return url;
		}
		
		
	    public void onLocationChanged(Location location) {
			// Draw the marker, if destination location is not set
			if(mMarkerPoints.size() < 2){
				
				mLatitude = location.getLatitude();
		        mLongitude = location.getLongitude();
		        LatLng point = new LatLng(mLatitude, mLongitude);
		
		        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
		        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));        
	        
	  //      	drawMarker(point);			
	        } 
	        
		}

	    
		ArrayList<LatLng> list = new ArrayList<LatLng>();

		
		
		public void onBackPressed() {
			// TODO Auto-generated method stub

				 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							getActivity().finish();
						}
					};
			        AlertDialog.Builder bldr = new Builder(getActivity());
			        bldr.setMessage("Are you sure you want to exit?");
			        bldr.setPositiveButton(android.R.string.yes, listener);
			        bldr.setNegativeButton(android.R.string.no, null);
			        bldr.show();
			
			    }

		
	/**
	 * 
	 * @author Srikanth
	 * This adapter is used to bind the data for trip details listview 
	 * and assigned to expandable list view to display the list group header and child list values
	 *
	 */
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private Context _context;
		
//		public MyExpandableListAdapter(Context context, List<ServiceModel> services, HashMap<String,List<String>> listDataChild) {
//			// TODO Auto-generated constructor stub
//			//this._context = context;
//		}
//
//		public MyExpandableListAdapter(FragmentActivity activity,
//				ArrayAdapter<ServiceModel> servicesAdapter,
//				HashMap<String, List<String>> listDataChild) {
//			// TODO Auto-generated constructor stub
//			this._context = activity;
//		}

		public MyExpandableListAdapter(FragmentActivity activity,
				List<ServiceModel> services,
				HashMap<String, List<String>> listDataChild) {
			// TODO Auto-generated constructor stub
			this._context = activity;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			 
	       // final String childText = (String) getChild(groupPosition, childPosition);
	 
	        if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) this._context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.routeinfo_view, null);
	        }
	 
	        TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.info_source);
	 
	       // txtListChild.setText(SanJose,CA);
	        return convertView;
	    }

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return Utilities.servicesAdapter.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return Utilities.servicesAdapter.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
		            LayoutInflater infalInflater = (LayoutInflater) this._context
		                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            convertView = infalInflater.inflate(R.layout.rides_list, null);
		           
		        }
		 
		        TextView headerRouteNo = (TextView) convertView.findViewById(R.id.route_no);
		        TextView headerTimings = (TextView) convertView.findViewById(R.id.text_timings);
		        //lblListHeader.setTypeface(null, Typeface.BOLD);
		          headerRouteNo.setText(Utilities.servicesAdapter.get(groupPosition).getRoute_no());
		          headerTimings.setText(Utilities.servicesAdapter.get(groupPosition).getTiminngs());
		          
		        ImageView plus = (ImageView) convertView.findViewById(R.id.image_popup);
		        plus.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// initialize a pop up window type
						
						Calendar c = Calendar.getInstance();
					    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					    currentDateTime = sdf.format(c.getTime());
					    
				        popupWindow = new PopupWindow(getActivity());
				        LayoutInflater popupInflater = (LayoutInflater) getActivity()
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				        View layout = popupInflater.inflate(R.layout.popupwindow,(ViewGroup) rootView.findViewById(R.id.relative_popup));
						
				        // some other visual settings
				       // popupWindow = new PopupWindow(layout,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
				        popupWindow.setFocusable(false);
				        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
				        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
				        
				        
				        // set the list view as pop up window content
				        popupWindow.setContentView(layout);
				        
				        share    = (RelativeLayout) layout.findViewById(R.id.relative_share);
						favorite = (RelativeLayout) layout.findViewById(R.id.relative_save);
						//save     = (RelativeLayout) layout.findViewById(R.id.relative_save);
						close    = (RelativeLayout) layout.findViewById(R.id.relative_close);						
						
						favorite.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								int i = popup_position; 
								 if (Utilities.dbAdapter == null)
										Utilities.dbAdapter = new DbAdapter(getActivity());
									if (null != Utilities.dbAdapter) {
										Utilities.dbAdapter.open();
										
									}
									
								Utilities.dbAdapter.storeTrips("INSERT INTO TripPlannerTable (source, destination, route_no, timinngs, date, fare) values('"+source[0]+"', '"+destination[0]+"','"+Utilities.servicesAdapter.get(groupPosition).getService_id()+"', '"+Utilities.servicesAdapter.get(groupPosition).getTiminngs()+"', '"+currentDateTime+"', '"+13.00+"')");
								popupWindow.dismiss();				
							}
							
						});
						
						close.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								popupWindow.dismiss();
							}
						});
						
						popup_position = groupPosition;
						popupWindow.showAsDropDown(v, -5, 0);
					}
				});
		        return convertView;
		   }
		
		public boolean onGroupClick (ExpandableListView parent, View v, int groupPosition, long id){
			Toast.makeText(getActivity(), "Group clicked", Toast.LENGTH_SHORT).show();
			Log.i("Group clicked","================= GroupClicked =======");
			return true;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	ExpandableListView.OnGroupClickListener onGroupClick = new OnGroupClickListener() {
		
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			// TODO Auto-generated method stub
			Log.i("onGroupClick","================= onGroupClick ");
			Toast.makeText(getActivity(), "Group clicked", Toast.LENGTH_SHORT).show();
			
			return false;
		}
	};
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	public class DownloadTask extends AsyncTask<String, Void, String>{
		
		// Downloading data in non-ui thread

		

		@Override
		protected String doInBackground(String... url) {
		
			// For storing data from web service
						String data = "";
								
						try{
							// Fetching the data from web service
							data = downloadUrl(url[0]);
						}catch(Exception e){
							Log.d("Background Task",e.toString());
						}
						return data;		
		}
		
		private String downloadUrl(String strUrl) throws IOException{
			// TODO Auto-generated method stub
			String data = "";
	        InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	                URL url = new URL(strUrl);

	                // Creating an http connection to communicate with url 
	                urlConnection = (HttpURLConnection) url.openConnection();

	                // Connecting to url 
	                urlConnection.connect();

	                // Reading data from url 
	                iStream = urlConnection.getInputStream();

	                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

	                StringBuffer sb  = new StringBuffer();

	                String line = "";
	                while( ( line = br.readLine())  != null){
	                        sb.append(line);
	                }
	                
	                data = sb.toString();

	                br.close();

	        }catch(Exception e){
	                Log.d("Exception while downloading url", e.toString());
	        }finally{
	                iStream.close();
	                urlConnection.disconnect();
	        }
	        return data;
		}

				// Executes in UI thread, after the execution of
				// doInBackground()
				@Override
				protected void onPostExecute(String result) {			
					super.onPostExecute(result);			
					
					ParserTask parserTask = new ParserTask();
					
					// Invokes the thread for parsing the JSON data
					parserTask.execute(result);
				}
				
				
				public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{				
					
					// Parsing the data in non-ui thread  

					@Override
					protected List<List<HashMap<String, String>>> doInBackground(
							String... jsonData) {
						// TODO Auto-generated method stub
						JSONObject jObject;	
						List<List<HashMap<String, String>>> routes = null;			           
				        
				        try{
				        	jObject = new JSONObject(jsonData[0]);
				        	DirectionsJSONParser parser = new DirectionsJSONParser();
				        	
				        	// Starts parsing data
				        	routes = parser.parse(jObject);    
				        }catch(Exception e){
				        	e.printStackTrace();
				        }
				        return routes;
					}
					
					// Executes in UI thread, after the parsing process
							@Override
							protected void onPostExecute(List<List<HashMap<String, String>>> result) {
								ArrayList<LatLng> points = null;
								PolylineOptions lineOptions = null;
								
								// Traversing through all the routes
								for(int i=0;i<result.size();i++){
									points = new ArrayList<LatLng>();
									lineOptions = new PolylineOptions();
									
									// Fetching i-th route
									List<HashMap<String, String>> path = result.get(i);
									
									// Fetching all the points in i-th route
									for(int j=0;j<path.size();j++){
										HashMap<String,String> point = path.get(j);					
										
										double lat = Double.parseDouble(point.get("lat"));
										double lng = Double.parseDouble(point.get("lng"));
										LatLng position = new LatLng(lat, lng);	
										
										points.add(position);						
									}
									
									// Adding all the points in the route to LineOptions
									lineOptions.addAll(points);
									lineOptions.width(2);
									lineOptions.color(Color.RED);	
									
								}
								
								// Drawing polyline in the Google Map for the i-th route
								mGoogleMap.addPolyline(lineOptions);							
							}			
					    }   

	}

	
}
