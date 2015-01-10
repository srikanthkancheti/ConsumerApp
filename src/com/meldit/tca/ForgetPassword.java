package com.meldit.tca;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassword extends Activity {
	
	EditText username_edt,email_edt;
	Button submit;
	String username, email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpassword);
		
		username_edt = (EditText) findViewById(R.id.edit_forget_username);
		email_edt    = (EditText) findViewById(R.id.edit_forget_email);
		
		submit = (Button) findViewById(R.id.submit_forget);
		submit.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				username = username_edt.getText().toString();
				email = email_edt.getText().toString();
				if(username.length()>0 && email.length()>0){
					
					new ForgetPasswordService().execute();
				}	
			}
		});	
	}
	
	String result;
	HttpResponse response;
	private class ForgetPasswordService extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
		try {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Utilities.forgetpassword_url+username+"&emailID="+email);
	        
           
           response = httpClient.execute(httpGet);
           Log.i("response","================================"+response.getStatusLine());
           
	         

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
		protected void onPostExecute(Void re) {
			// TODO Auto-generated method stub
			String status = response.getStatusLine().toString();
	        if(status.contains("200")){
		    try {
				result = EntityUtils.toString(response.getEntity());
				Toast.makeText(getApplicationContext(), "Check your email for password !", Toast.LENGTH_SHORT).show();
				finish();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    Log.i("result","================================"+result);
	           }else{
	        	   Toast.makeText(ForgetPassword.this,"Your UserName OR Email-ID is wrong " +
	        	   		"please provide valid Information", Toast.LENGTH_SHORT).show(); 
	           }
			super.onPostExecute(re);
		}
		
	}

}
