package com.meldit.tca.map;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.meldit.tca.MyAccount;
import com.meldit.tca.R;
import com.meldit.tca.Utilities;
import com.meldit.tca.parsers.HandleRouteViaPointsXML;
import com.meldit.tca.parsers.HandleRouteViaPointsXML.RouteViaPointsModel;

public class RouteMapActivity extends FragmentActivity implements LocationListener{
	
	private GoogleMap mGoogleMap;
	private ArrayList<LatLng> mMarkerPoints;
	private String addressText = " ", routeNo, result;
	double mLatitude=0;
    double mLongitude=0;
    private Geocoder geocoder;
    private List<Address> addresses;
    private LatLng Source, Destination;
    private ActionBar actionBar;
    private ListView route_via_lv;
    private HttpResponse response;
    private RoutesViaPointsAdapter routeViaPointsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_route_map);
		route_via_lv = (ListView) findViewById(R.id.route_via_listView);
		actionBar = getActionBar();
		
		Intent intent = getIntent();
		routeNo = intent.getStringExtra("route_no");
		routeViaPointsAdapter = new RoutesViaPointsAdapter(RouteMapActivity.this, Utilities.routeViaPoints);
		getListViewData();
		
		//actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#110000ff")));
		//actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);	
		
		Source = new LatLng(17.36167230, 78.47457469);
		Destination = new LatLng(17.43992950, 78.49827410); 
		// Initializing 
		mMarkerPoints = new ArrayList<LatLng>();
		
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.route_map);
		
		// Getting Map for the SupportMapFragment
		mGoogleMap = fm.getMap();	
		
		//?android:attr/actionBarSize parameters for padding(left, top, right, bottom)
        int actionBarSize = obtainActionBarHeight();
        mGoogleMap.setPadding(0, actionBarSize, 0, 0);
        
        mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#110000ff")));
			}
		});
        
        //mGoogleMap.setOnM
		
		
		if(mGoogleMap!=null){
		
			// Enable MyLocation Button in the Map
			mGoogleMap.setMyLocationEnabled(true);	
			
			// Getting LocationManager object from System Service LOCATION_SERVICE
	        LocationManager locationManager = (LocationManager) MyAccount.getCustomAppContext().getSystemService(Context.LOCATION_SERVICE);
	
	        // Creating a criteria object to retrieve provider
	        Criteria criteria = new Criteria();
	
	        // Getting the name of the best provider
	        String provider = locationManager.getBestProvider(criteria, true);
	        	        
            locationManager.requestLocationUpdates(provider, 20000, 0, this);			
         // define point to center on	        
	        CameraUpdate panToOrigin = CameraUpdateFactory.newLatLng(Source);
	        mGoogleMap.moveCamera(panToOrigin);
	     // set zoom level with animation
	        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
					
    		mMarkerPoints.add(Source);
    		mMarkerPoints.add(Destination);
    		
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
					geocoder = new Geocoder(this, Locale.getDefault());
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
	
	private void getListViewData() {
		// TODO Auto-generated method stub
		new getSelectedRouteViaPoints().execute(routeNo);
	}
	
	public class getSelectedRouteViaPoints extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(Utilities.getRouteViaPoints_url+params[0]);
			        
		           response = httpClient.execute(httpGet);
			           
			       result = EntityUtils.toString(response.getEntity());
			       Log.i("all routen via points result","================================"+result);
			         

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
					Toast.makeText(getApplicationContext(),"No Route via points Available", Toast.LENGTH_SHORT).show();
				}else{
					InputStream is = new ByteArrayInputStream(result.getBytes());
					HandleRouteViaPointsXML parseRouteVia = new HandleRouteViaPointsXML();
					Utilities.routeViaPoints = parseRouteVia.parseRouteViaXML(is);
					route_via_lv.setAdapter(routeViaPointsAdapter);
		
					}
			  }else{
				  Toast.makeText(getApplicationContext(),"No via point Available", Toast.LENGTH_SHORT).show();
			  }
			}


	}


	private int obtainActionBarHeight() {
		// TODO Auto-generated method stub
		int[] textSizeAttr = new int[] { android.R.attr.actionBarSize };
	    TypedValue typedValue = new TypedValue(); 
	    TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
	    int textSize = a.getDimensionPixelSize(0, -1);
	    a.recycle();
		return textSize;
	}

	private String getDirectionsUrl(LatLng origin,LatLng dest){
					
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
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
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

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
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
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
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
			MarkerOptions markerOptions = new MarkerOptions();
			
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

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	public class RoutesViaPointsAdapter extends BaseAdapter{
		
		private Context _context;

		public RoutesViaPointsAdapter(RouteMapActivity routeMapActivity,
				ArrayList<RouteViaPointsModel> routeViaPoints) {
			// TODO Auto-generated constructor stub
			this._context = routeMapActivity;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Utilities.routeViaPoints.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Utilities.routeViaPoints.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return Utilities.routeViaPoints.indexOf(getItem(position));
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			
			LayoutInflater mInflater = (LayoutInflater) 
					this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        if (convertView == null) {
		            convertView = mInflater.inflate(R.layout.route_via_points_item, null);
		            holder = new ViewHolder();
		            holder.route_name = (TextView) convertView.findViewById(R.id.route_via_name_textView);
		            holder.route_id = (TextView) convertView.findViewById(R.id.route_via_id_textView);
		            convertView.setTag(holder);
		        }
		        else {
		            holder = (ViewHolder) convertView.getTag();
		        }
		         
		        holder.route_name.setText(Utilities.routeViaPoints.get(position).getViaName());
		        holder.route_id.setText(Utilities.routeViaPoints.get(position).getViaNameId());
		        
			return convertView;
		}
		
		/*private view holder class*/
	    private class ViewHolder {
	        TextView route_name, route_id;
	    }

	}

}
