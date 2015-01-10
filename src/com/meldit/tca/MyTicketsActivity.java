package com.meldit.tca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;

public class MyTicketsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tickets);
		
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

			 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						MyTicketsActivity.this.finish();
					}
				};
		        AlertDialog.Builder bldr = new Builder(MyTicketsActivity.this);
		        bldr.setMessage("Are you sure you want to exit?");
		        bldr.setPositiveButton(android.R.string.yes, listener);
		        bldr.setNegativeButton(android.R.string.no, null);
		        bldr.show();
		
		    }
}
