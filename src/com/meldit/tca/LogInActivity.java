package com.meldit.tca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.meldit.tca.db.DbAdapter;

public class LogInActivity extends Activity implements OnClickListener{
	
	

	DbAdapter mDbHelper = new DbAdapter(LogInActivity.this);		
	
	EditText editText1,editText2;
	Button login,password,account;
	Switch mySwitch;
	String result;
	StringEntity entity;
	HttpResponse response;
	
	String uname,pword, userName, Password, email, phoneNumber;
	   public static final String MyPREFERENCES = "MyPrefs" ;
	   public static final String name = "nameKey"; 
	   public static final String pass = "passwordKey"; 
	   SharedPreferences sharedpreferences;

    @SuppressLint("NewApi") 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
         
    	sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    	if (sharedpreferences.contains(name) && sharedpreferences.contains(pass)){
        	
        	Intent i = new Intent(this,MyAccount.class);
    		startActivity(i);
       }
    	
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        
        login     = (Button) findViewById(R.id.button1);
        login.setOnClickListener(this);
        password  = (Button) findViewById(R.id.button_password);
        password.setOnClickListener(this);
        account   = (Button) findViewById(R.id.button3);
        account.setOnClickListener(this);
        
        mySwitch 	  = (Switch) findViewById(R.id.toggleButton1);      
        
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1:
			uname = editText1.getText().toString();
			pword = editText2.getText().toString();
			uname = uname.trim();
			pword = pword.trim();
			if(mySwitch.isChecked() && uname.length()>0 && pword.length() >0){
					  
			new LoginService().execute();
			}else if(uname.length()>0 && pword.length() >0){
				new LoginService().execute();
			}
			else
				Toast.makeText(LogInActivity.this,"user name and password both are empty", Toast.LENGTH_SHORT).show();			
		 break;
		case R.id.button_password:
			Intent intent_forgetpassword = new Intent(LogInActivity.this,ForgetPassword.class);
			startActivity(intent_forgetpassword);
			 break;
		case R.id.button3:
			Intent intent = new Intent(LogInActivity.this,RegistrationActivity.class);
			startActivity(intent);
			 break;
		}
	}
	
	private class  LoginService extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
			try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Utilities.consumer_login_url);
		        StringBuilder sb = new StringBuilder();
		       		        
		        sb.append("<MD_CONSUMER_DETAILS>");
		        
		        sb.append("<ticsUserName>");
		        sb.append(uname);
		        sb.append("</ticsUserName>");
		        sb.append("<ticsPassword>");
		        sb.append(pword);
		        sb.append("</ticsPassword>");
		        
		        sb.append("</MD_CONSUMER_DETAILS>");
	            
		        entity = new StringEntity(sb.toString());
		        httpPost.setEntity(entity);  
		        httpPost.setHeader("Accept", "application/xml");
		        httpPost.setHeader("Content-Type", "application/xml");
		      
	           response = httpClient.execute(httpPost);
	           Log.i("response","================================"+response.getStatusLine());
	           String status = response.getStatusLine().toString();
	           if(status.contains("200")){
		       result = EntityUtils.toString(response.getEntity());
		       Log.i("result","================================"+result);
	           }else{
	        	   result = "Unauthorized User";
	           }
		         

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
			//Toast.makeText(LogInActivity.this,"result : "+result, Toast.LENGTH_SHORT).show();
			if(null!= result){
				if(result.contains("Unauthorized User")){
					Toast.makeText(LogInActivity.this,"Unauthorized User", Toast.LENGTH_SHORT).show();
				}else{
				InputStream is = new ByteArrayInputStream(result.getBytes());
				String [] credentials= new String [5];
				credentials = new WebServiceParsers().parseResult(is);
				if(credentials[0].equalsIgnoreCase(uname) && credentials[1].equalsIgnoreCase(pword)){
					userName = credentials[0].toString();
					Password = credentials[1].toString();					
					email = credentials[2].toString();
					phoneNumber = credentials[3].toString();
					mDbHelper.storeProfileDetails("INSERT INTO ProfileDetails (User_Name, Password, Email_Id, Phone_Number) values('"+userName+"', '"+password+"','"+email+"', '"+phoneNumber+"')"); //WHERE '"+userName+"' NOT IN '"+profileNames+"')");
					  Editor editor = sharedpreferences.edit();
				      editor.putString(name, userName);
				      editor.putString(pass, Password);
				      editor.commit();
					Toast.makeText(LogInActivity.this,"Log in Succesfully", Toast.LENGTH_SHORT).show();
					// new Activity
					Intent intent_account = new Intent(LogInActivity.this,MyAccount.class);
					startActivity(intent_account);
					editText1.setText("");
					editText2.setText("");
					
				}else if(credentials[0].equalsIgnoreCase(uname) || credentials[1].equalsIgnoreCase(pword)){
					Toast.makeText(LogInActivity.this,"wrong username or password", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(LogInActivity.this,"username and possword are wrong", Toast.LENGTH_SHORT).show();
				}
				}
			  }else{
				  Toast.makeText(LogInActivity.this,"No Services Available", Toast.LENGTH_SHORT).show();
			  }
		}
		
	}
	
	
	
	OnCheckedChangeListener listener_switch = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			
		}
	};
    
}
