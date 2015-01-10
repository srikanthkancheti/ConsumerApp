package com.meldit.tca;

import com.meldit.tca.db.DbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTripActivity extends Activity{
	
	EditText edtSource, edtDestination, edtRouteNo, edtTimings, edtDate;
	Button btn_cancel, btn_editTrip;
	Long mRowId;
	private DbAdapter mDbHelper = new DbAdapter(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittrip);
		InitializeUI();
		Intent intent = getIntent();
		String source = intent.getStringExtra("edt_source");
		String destination = intent.getStringExtra("edt_destination");
		String routuno = intent.getStringExtra("edt_routuno");
		String timings = intent.getStringExtra("edt_timings");
		String date = intent.getStringExtra("edt_date");
		final String ID = intent.getStringExtra("id");
		
		edtSource.setText(source);
		edtDestination.setText(destination);
		edtRouteNo.setText(routuno);
		edtTimings.setText(timings);
		edtDate.setText(date);
		
		mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DbAdapter.KEY_ROWID)
                                    : null;
        }
		
		btn_editTrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String Source = edtSource.getText().toString();
				String Destination = edtDestination.getText().toString();
				String RouteNo = edtRouteNo.getText().toString();
				String Timings = edtTimings.getText().toString();
				String Date = edtDate.getText().toString();
				 if (mRowId != null){
					 
					 mDbHelper.updateTrip(mRowId, Source, Destination, RouteNo, Timings, Date);
					 Log.v(this.getClass().getName(), "check..Edit Trip Activity ID...:  "+mRowId); 
					 
					 finish();
				 }else{
					 Toast.makeText(getApplicationContext(), "Row Id is null.", Toast.LENGTH_LONG).show();
				 }
				 
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	private void InitializeUI() {
		// TODO Auto-generated method stub
		edtSource = (EditText) findViewById(R.id.edit_from);
		edtDestination = (EditText) findViewById(R.id.edit_to);
		edtRouteNo = (EditText) findViewById(R.id.edit_bus);
		edtTimings = (EditText) findViewById(R.id.editText1);
		edtDate = (EditText) findViewById(R.id.editText2);
		btn_cancel = (Button) findViewById(R.id.edt_btn_cancel);
		btn_editTrip = (Button) findViewById(R.id.edt_btn_edit);
	}

}
