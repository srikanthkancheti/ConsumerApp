package com.meldit.tca;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity implements OnClickListener {
	
	EditText editText1,editText2,editText3,editText4,editText5,editText6 ;
	Button submit;
	String result;
	StringEntity entity;
	HttpResponse response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.registration);
		 
		setTitle("Registration");
		editText1 = (EditText) findViewById(R.id.editText_reg1);
		editText2 = (EditText) findViewById(R.id.editText_reg2);
		editText3 = (EditText) findViewById(R.id.editText_reg3);
		editText4 = (EditText) findViewById(R.id.editText_reg4);
		editText5 = (EditText) findViewById(R.id.editText_reg5);
		editText6 = (EditText) findViewById(R.id.editText_reg6);
		
		submit 	  = (Button) findViewById(R.id.button_submit);
		submit.setOnClickListener(this);
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		/*Intent intent = new Intent(RegistrationActivity.this,MyAccount.class);
		startActivity(intent);*/
		Utilities.first_name = editText1.getText().toString();
		Utilities.last_name  = editText2.getText().toString();
		Utilities.uname 	 = editText3.getText().toString();
		Utilities.pword 	 = editText4.getText().toString();
		Utilities.email 	 = editText5.getText().toString();
		Utilities.contact 	 = editText6.getText().toString();
		
		new PostDataTask().execute();
		
		/*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Name",Utilities.uname);
		editor.putString("Password", Utilities.pword);
		editor.putString("email", Utilities.email);
		editor.commit();*/
	
		//finish();
	}

	private class  PostDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
			try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Utilities.registration_url);
		        StringBuilder sb = new StringBuilder();
		       		        
		        sb.append("<MD_CONSUMER_DETAILS>");
		        sb.append("<firstname>");
		        sb.append(Utilities.first_name);
		        sb.append("</firstname>");
		        sb.append("<ticsUserName>");
		        sb.append(Utilities.uname);
		        sb.append("</ticsUserName>");
		        sb.append("<ticsPassword>");
		        sb.append(Utilities.pword);
		        sb.append("</ticsPassword>");
		        sb.append("<lastname>");
		        sb.append(Utilities.last_name); 
		        sb.append("</lastname>");
		        sb.append("<email>");
		        sb.append(Utilities.email);
		        sb.append("</email>");
		        sb.append("<type>");
		        sb.append("Consumer");
		        sb.append("</type>");
		        sb.append("<status>");
		        sb.append("N");
		        sb.append("</status>");
		        sb.append("<subtype>");
		        sb.append("</subtype>");
		        sb.append("<phone>");
		        sb.append(Utilities.contact);
		        sb.append("</phone>");
		        sb.append("<indexId>");
		        sb.append("</indexId>");
		        sb.append("<company>");
		        sb.append("zzz");
		        sb.append("</company>");
		        
		        sb.append("</MD_CONSUMER_DETAILS>");
	            
		        entity = new StringEntity(sb.toString());
		        Log.i("entity","================================"+sb.toString());
		        httpPost.setEntity(entity);  
		        httpPost.setHeader("Accept", "application/xml");
		        httpPost.setHeader("Content-Type", "application/xml");
		      
	           Log.i("httpPost","================================"+httpPost.toString());
	           response = httpClient.execute(httpPost);
		           
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
			editText1.setText("");
			editText2.setText("");
			editText3.setText("");
			editText4.setText("");
			editText5.setText("");
			finish();
			Toast.makeText(RegistrationActivity.this,"result : "+result, Toast.LENGTH_LONG).show();
		}
		
	}
	
}
