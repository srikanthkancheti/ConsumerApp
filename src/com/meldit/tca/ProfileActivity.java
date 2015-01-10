package com.meldit.tca;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.meldit.tca.db.DbAdapter;

public class ProfileActivity extends Activity{
	

	DbAdapter mDbHelper = new DbAdapter(ProfileActivity.this);	
	TextView firstName_tv, lastName_tv, userName_tv, password_tv, email_tv, phone_tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		initializeUI();
		
		setData();
	}

	private void setData() {
		// TODO Auto-generated method stub
		
		
		Cursor profileCursor = mDbHelper.getProfileData("SELECT * FROM ProfileDetails");
		try{
			if(null != profileCursor && profileCursor.moveToFirst()){
				do{
					
					userName_tv.setText(profileCursor.getString(1));
					password_tv.setText(profileCursor.getString(2));
					email_tv.setText(profileCursor.getString(3));
					phone_tv.setText(profileCursor.getString(4));

				}while(profileCursor.moveToNext());
			}
		}catch(Exception ex){
		}finally{
			profileCursor.close();
		}
	
	}

	private void initializeUI() {
		// TODO Auto-generated method stub
		//firstName_tv = (TextView)findViewById(R.id.profile_firstname_tv);
		//lastName_tv = (TextView)findViewById(R.id.profile_lastname_tv);
		userName_tv = (TextView)findViewById(R.id.profile_username_tv);
		password_tv = (TextView)findViewById(R.id.profile_password_tv);
		email_tv = (TextView)findViewById(R.id.profile_email_tv);
		phone_tv = (TextView)findViewById(R.id.profile_phone_tv);
	}
	


}
