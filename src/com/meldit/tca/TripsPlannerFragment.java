package com.meldit.tca;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meldit.tca.db.DbAdapter;


public class TripsPlannerFragment extends Fragment {		

	ImageView image_addtrip, image_removeTrip;
	static PopupWindow popupWindow, pw;
	ListView myListView;
	ArrayList<TripsPlannerModel> tripsList = null;
	TripsPlannerAdapter _tripsAdapter = null;
	RelativeLayout trips_lv_item;
	TextView Source_tv, Destination_tv, Route_no, Timings_tv, Date_tv, Fare_tv;
	String edtSource, edtDestination, edtRouteNo, edtTimings, edtDate, edtFare;
	Long Trip_ID;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.trip_planner, container, false);
		
		tripsList = new ArrayList<TripsPlannerFragment.TripsPlannerModel>();
		
		myListView =(ListView) rootView.findViewById(R.id.list_tripsplanner);
		image_addtrip = (ImageView) rootView.findViewById(R.id.image_addtrip);
		image_removeTrip = (ImageView) rootView.findViewById(R.id.image_remove_plan);
		trips_lv_item = (RelativeLayout) rootView.findViewById(R.id.tripsPlanner_list_item);
		Source_tv = (TextView) rootView.findViewById(R.id.text_plan_from);
		Destination_tv = (TextView) rootView.findViewById(R.id.text_plan_destination);
		Route_no = (TextView)rootView.findViewById(R.id.text_routeno_plan);
		Timings_tv = (TextView) rootView.findViewById(R.id.text_plan_timings);
		Date_tv = (TextView) rootView.findViewById(R.id.textView_plan_date);
		Fare_tv = (TextView) rootView.findViewById(R.id.textView_plan_fare);
		
		// DB open
		if (Utilities.dbAdapter == null)
			Utilities.dbAdapter = new DbAdapter(getActivity());
		if (null != Utilities.dbAdapter) {
			Utilities.dbAdapter.open();	
		}
		getResultsFromDB();
		
		image_addtrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addtripIntent = new Intent(getActivity(), AddTripActivity.class);
				startActivity(addtripIntent);
			}
		});
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(),"Your Selected Item position", Toast.LENGTH_LONG).show();
			}
		});
		
		return rootView;
		
	}
	
	@Override
	public void onResume() {
		getResultsFromDB();
        super.onResume();   
       
	}
	

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

	private void getResultsFromDB() {
		tripsList.clear();
		Cursor cursor = Utilities.dbAdapter.fetchAllTrips("SELECT * FROM TripPlannerTable");
		
		try{
			
	    	if (null != cursor && cursor.moveToFirst()) {
			do {
				TripsPlannerModel _plannerModel = new TripsPlannerModel();					
					
					_plannerModel.setSource(cursor.getString(1));
					_plannerModel.setDestination(cursor.getString(2));
					_plannerModel.setRoute_no(cursor.getString(3));
					_plannerModel.setTiminngs(cursor.getString(4));
					_plannerModel.setDate(cursor.getString(5));
					_plannerModel.set_id(cursor.getLong(0));
					tripsList.add(_plannerModel);										
					_plannerModel = null;
				}while(cursor.moveToNext());
			
		  }
	  }catch(Exception ex){
		
   	  }finally{
   		  cursor.close();
   	  }
		
		_tripsAdapter = new TripsPlannerAdapter();
		
		myListView.setAdapter(_tripsAdapter);	
		
		
	}

	// Constant for identifying the dialog
	private static final int DIALOG_ALERT = 10;
	
	
	public class  TripsPlannerAdapter extends BaseAdapter {
		
	LayoutInflater mInflater;
	
	public  TripsPlannerAdapter() {
		// TODO Auto-generated constructor stub
		
		this.mInflater = LayoutInflater.from(getActivity());
		}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tripsList == null ? 0 : tripsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tripsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = convertView == null ? new ViewHolder() : (ViewHolder) convertView.getTag();
		if(convertView ==null){
			
			convertView=mInflater.inflate(R.layout.trips_planner_list,null);
			holder.text_source = (TextView) convertView.findViewById(R.id.text_plan_from);
			holder.text_destination = (TextView) convertView.findViewById(R.id.text_plan_destination);
			holder.text_routeno = (TextView) convertView.findViewById(R.id.text_routeno_plan);
			holder.text_timings = (TextView) convertView.findViewById(R.id.text_plan_timings);
			holder.image_removetrip = (ImageView) convertView.findViewById(R.id.image_remove_plan);
			holder.text_date = (TextView) convertView.findViewById(R.id.textView_plan_date);
            
			convertView.setTag(holder);
		}
		
		holder.text_source.setText(tripsList.get(position).getSource());
		holder.text_destination.setText(tripsList.get(position).getDestination());
		holder.text_routeno.setText(tripsList.get(position).getRoute_no());
		holder.text_timings.setText(tripsList.get(position).getTiminngs());
		holder.text_date.setText(tripsList.get(position).getDate());
		_tripsAdapter.notifyDataSetChanged();
		
		holder.image_removetrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new Builder(getActivity());
				
				builder.setTitle("delete");
				builder.setMessage("Are you sure you want to delete?");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub					
						String item = tripsList.get(position).getRoute_no();																							
						Utilities.dbAdapter.deleteSelectedtrip(item);	
						myListView.setAdapter(_tripsAdapter);
						tripsList.remove(position);
						notifyDataSetChanged();
						Toast.makeText(getActivity(), "Trip deleted", Toast.LENGTH_LONG).show();
					}
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}
				});
				builder.show();
			}
		});
		return convertView;
	}
	
	 class ViewHolder {				 
		    TextView text_source;
		    TextView text_destination;
		    TextView text_routeno;
		    TextView text_timings, text_date;
		    ImageView image_edittrip, image_removetrip;
		    TextView text_dispatch;
  	 }
	 
	
}
	 
	MyAccount myAccount = new MyAccount();
	OnClickListener listener_edittrip = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getActivity().showDialog(DIALOG_ALERT);
		}
	};
	
	
	public class TripsPlannerModel {

		String source,destination,route_no,timinngs,date,fare ;
		public long  _id;
		
		public long get_id() {
			return _id;
		}

		public void set_id(long _id) {
			this._id = _id;
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

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getFare() {
			return fare;
		}

		public void setFare(String fare) {
			this.fare = fare;
		}
	}
}
