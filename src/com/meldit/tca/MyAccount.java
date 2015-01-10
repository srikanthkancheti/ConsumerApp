package com.meldit.tca;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

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
	
}
