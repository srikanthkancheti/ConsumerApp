package com.meldit.tca;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MyAccount extends FragmentActivity implements ActionBar.TabListener {

	private static Context context;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "My Ride", "Routes", "Trips Planner" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.pager);
		context = getApplicationContext();
		
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	

		
		// here you add an array of your icon   
		 final int[] ICONS = new int[] {
		                    R.drawable.menu_pin_36x36,
		                    R.drawable.menu_bus_36x36,
		                    R.drawable.menu_calendar_36x36,
		                    R.drawable.ic_launcher,
		            };
		// add this following code to solve your problem
		    // here NewsFeedActivity.this.getResources() is used to get the drawable folder resource
		    // instead of NewsFeedActivity you have to use your activity name here  
		    for (int i=0; i < tabs.length; i++)
		            {
		            actionBar.addTab(actionBar.newTab().setText(tabs[i])
		                                     .setIcon(MyAccount.this.getResources().getDrawable(ICONS[i]))
		                                     .setTabListener(this));
		            }//endfor

//		// Adding Tabs
//		for (String tab_name : tabs) {
//			actionBar.addTab(actionBar.newTab().setText(tab_name)
//					.setTabListener(this));
//		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	
	public static Context getCustomAppContext() {
		// TODO Auto-generated method stub
		return context;
	}
	

			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.main, menu);
			   
			   // setMenuBackground();
			    return true;
			}
			
			
			
			 @Override
			  public boolean onOptionsItemSelected(MenuItem item) {
				 Log.i("onOptions","========================item"+item.getItemId());
				 
			    switch (item.getItemId()) {
			    case R.id.item1:
			     Intent myProfileIntent = new Intent(MyAccount.this, ProfileActivity.class);
			     startActivity(myProfileIntent);
			      break;
			    case R.id.item2:
			    	Toast.makeText(getApplicationContext(),"Favorites List",Toast.LENGTH_SHORT).show();
			    	//tabHost.setCurrentTab(2);
				      break;
			    case R.id.item3:
				    // tabHost.setCurrentTab(3);
				      break;
			    case R.id.item4:
				     Intent settingsIntent = new Intent(MyAccount.this, SettingsActivity.class);
				     startActivity(settingsIntent);
				      break;
			    case R.id.item5:
			    	SharedPreferences sharedpreferences = getSharedPreferences
				      (LogInActivity.MyPREFERENCES, Context.MODE_PRIVATE);
				      Editor editor = sharedpreferences.edit();
				      editor.clear();
				      editor.commit();
				      moveTaskToBack(true); 
				      MyAccount.this.finish();
				      System.exit(0);
				      break;
			    default:
			    	 
			      break;
			    }
			    return true;
			  }
			
			 
			 protected void setMenuBackground(){                     
			        // Log.d(TAG, "Enterting setMenuBackGround");  
			        getLayoutInflater().setFactory( new Factory() {  
			            public View onCreateView(String name, Context context, AttributeSet attrs) {
			                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
			                    try { // Ask our inflater to create the view  
			                        LayoutInflater f = getLayoutInflater();  
			                        final View view = f.createView( name, null, attrs );  
			                        /* The background gets refreshed each time a new item is added the options menu.  
			                        * So each time Android applies the default background we need to set our own  
			                        * background. This is done using a thread giving the background change as runnable 
			                        * object */
			                        new Handler().post( new Runnable() {  
			                            public void run () {  
			                                // sets the background color   
			                                view.setBackgroundResource(Color.parseColor("#FFA500"));
			                                // sets the text color              
			                               // ((TextView) view).setTextColor(Color.BLACK);
			                                // sets the text size              
			                               // ((TextView) view).setTextSize(18);
			                }
			                        } );  
			                    return view;
			                }
			            catch ( InflateException e ) {}
			            catch ( ClassNotFoundException e ) {}  
			        } 
			        return null;
			    }}); 
			 }
			 
			 
	
}
