package com.meldit.tca;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meldit.tca.db.DbAdapter;

public class AddTripActivity extends Activity{
	
	EditText edt_from, edt_to, edt_routeNo, edt_timings, edt_date, edt_qty;
	Button btn_cancle, btn_addTrip;
	private DbAdapter mDbHelper = new DbAdapter(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtrip);
		InitializeUI();
		
		btn_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddTripActivity.this.finish();
			}
		});
		
		btn_addTrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String addTripSource = edt_from.getText().toString();
				String addTripDestination = edt_to.getText().toString();
				String addTripRouteNo = edt_routeNo.getText().toString();
				String addTripTimings = edt_timings.getText().toString();
				String addTripDate = edt_date.getText().toString();
				
				if(addTripSource.length()>0 && addTripDestination.length()>0){
					 
					if(addTripRouteNo.length() >0 && addTripTimings.length()>0){
						
						if(addTripDate.length()>0){
							
							mDbHelper.storeTrips("INSERT INTO TripPlannerTable (source, destination, route_no, timinngs, date, fare) values(" +
									"'"+addTripSource+"', '"+addTripDestination+"','"+addTripRouteNo+"', '"+addTripTimings+"', '"+addTripDate+"', '"+30.00+"')");
							
							Toast.makeText(getApplicationContext(), "Trip saved.", Toast.LENGTH_LONG).show();
							
							finish();
						}else{
							Toast.makeText(getApplicationContext(), "Please enter date!", Toast.LENGTH_LONG).show();
						}
						
					}else{
						Toast.makeText(getApplicationContext(), "Please enter route number and timings!", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please enter From and To values!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void InitializeUI() {
		// TODO Auto-generated method stub
		edt_from = (EditText) findViewById(R.id.add_edit_from);
		edt_to = (EditText) findViewById(R.id.add_edit_to);
		edt_routeNo = (EditText) findViewById(R.id.add_edit_routeNo);
		edt_timings = (EditText) findViewById(R.id.add_edit_timings);
		edt_date = (EditText) findViewById(R.id.add_edit_date);
		edt_qty = (EditText) findViewById(R.id.add_edit_qty);
		btn_cancle = (Button) findViewById(R.id.add_btn_cancel);
		btn_addTrip = (Button) findViewById(R.id.add_btn_add);
	}

	
}
