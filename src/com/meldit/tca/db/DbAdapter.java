package com.meldit.tca.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter
{
	private DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	private Context m_Context;
	
	private static final String DATABASE_NAME = "tcatripplanner.db";
	private static final int DATABASE_VERSION = 2;
	
		public static final String KEY_ROWID = "_id";
		public static final String KEY_INFMID = "infm_id";
		//TRIP PLANNER COLOUMS	
		public static final String KEY_SOURCE = "source";
		public static final String KEY_DESTINATION = "destination";
		public static final String KEY_ROUTENO = "route_no";
		public static final String KEY_TIMINGS = "timinngs";
		public static final String KEY_DATE = "date";
		public static final String KEY_FARE = "fare";
		public static final String KEY_QUANTITY = "quantity";
			
		private static final String DATABASE_TABLE_TRIPPLANNER = "TripPlannerTable";
	
//		private static final String DATABASE_TRIPPLANNER_CREATE = "create table if not exists TripPlannerTable (_id integer primary key autoincrement,"
//				+ "source text not null,destination text not null,route_no text not null," +
//				  "timinngs text not null,date text not null,fare text not null);";
//		
//		

	public static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			 //db.execSQL(DATABASE_TRIPPLANNER_CREATE);
			 
			db.execSQL("create table if not exists TripPlannerTable (_id integer primary key autoincrement,"
					+ "source text not null,destination text not null,route_no text not null," +
					  "timinngs text not null,date text not null,fare text);"); 
			//db.execSQL(DATABASE_TRIPPLANNER_CREATE);
			db.execSQL(" CREATE TABLE ProfileDetails (_Id  integer primary key autoincrement, User_Name varchar(50), Password varchar(50), Email_Id varchar(50), Phone_Number varchar(15));");
			 Log.v("DataBaseHelper Class", "Table Created");
			 
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			// Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS TripPlannerTable");
	        db.execSQL("DROP TABLE IF EXISTS ProfileDetails");
	 
	        // Create tables again
	        onCreate(db);
		}
		
	}
	
	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(m_Context);
		mDb = mDbHelper.getWritableDatabase();
		mDb = mDbHelper.getReadableDatabase();
		return this;
	}
	public DbAdapter(Context ctx) {
		// TODO Auto-generated constructor stub
		this.m_Context = ctx;
	}
	
	
	
	public long insert_trips(String source,String destination,String route_no,String timinngs,String date,String fare)
	{
		 Long return_value = null;
	  	 ContentValues initialValues = new ContentValues();
		 initialValues.put(KEY_SOURCE, source);
	     initialValues.put(KEY_DESTINATION, destination);
	     initialValues.put(KEY_ROUTENO, route_no);
	     initialValues.put(KEY_TIMINGS, timinngs);
	     initialValues.put(KEY_DATE, date);
	     initialValues.put(KEY_FARE, fare);
	     
      
		/*if (db.update(plist_name, args_new, KEY_NAME + "=" + "\""
				+ sname + "\"", null) > 0) {
			return_value = 999L;
		} else {
			myDebug.i(" ","reminder values ate inserted................... ...........");
			*/
			return_value = mDb.insert(DATABASE_TABLE_TRIPPLANNER, null, initialValues);
		//}
		return return_value;
      //  mDb.insert(plist_name, null, initialValues);
		
	}
	
	
//	public Cursor fetchAllTrips() {
//		Log.i("fetch","playlist data  ******************success*******");
//		return mDb.query(DATABASE_TABLE_TRIPPLANNER, 
//				new String[] { KEY_ROWID,KEY_SOURCE,KEY_DESTINATION,KEY_ROUTENO,KEY_TIMINGS,KEY_DATE,KEY_FARE}, null, null, null, null, "_id DESC");
//	}
	public void storeProfileDetails(String string) {
		// TODO Auto-generated method stub
		try{
			open();
			mDb.execSQL(string);
		}catch(Exception e){
			System.out.println("Error:::"+e.getMessage());
		}finally{
			//mDb.close();
		}
	}
	
	
	public Cursor getProfileData(String string) {
		// TODO Auto-generated method stub
		Cursor profileCur=null;
    	try{
    		open();
    		profileCur=mDb.rawQuery(string, null);
    		
    	}catch(Exception e){
    		profileCur=null;
    		System.out.println("Error::::"+e.getMessage());
    	}finally{
    		
    	}
    	return profileCur;
	}
	public Cursor fetchAllTrips(String string) {
		// TODO Auto-generated method stub
		Cursor tripsCur=null;
    	try{
    		open();
    		tripsCur=mDb.rawQuery(string, null);
    		
    	}catch(Exception e){
    		tripsCur=null;
    		System.out.println("Error::::"+e.getMessage());
    	}finally{
    		
    	}
    	return tripsCur;
	}
	public Cursor fetchProfile(String string) {
		// TODO Auto-generated method stub
		Cursor profiCur=null;
    	try{
    		open();
    		profiCur=mDb.rawQuery(string, null);
    		
    	}catch(Exception e){
    		profiCur=null;
    		System.out.println("Error::::"+e.getMessage());
    	}finally{
    		
    	}
    	return profiCur;
	}
	public void storeTrips(String string) {
		// TODO Auto-generated method stub
		try{
			open();
			mDb.execSQL(string);
			
		}catch(Exception e){
			System.out.println("Error:::"+e.getMessage());
		}finally{
			//mDb.close();
		}
	}
	/**
	 * 
	 * @param string
	 */
	public void deleteSelectedtrip(String dataStr) {
		// TODO Auto-generated method stub
		mDb.execSQL("DELETE from TripPlannerTable where route_no IN ("+dataStr+")");
	}
	public void updateTrip(Long mRowId, String source, String destination,
			String routeNo, String timings, String date) {
		// TODO Auto-generated method stub
		ContentValues args = new ContentValues();
        args.put(KEY_SOURCE, source);
        args.put(KEY_DESTINATION, destination);
        args.put(KEY_ROUTENO, routeNo);
        args.put(KEY_TIMINGS, timings);
        args.put(KEY_DATE, date);
        
        mDb.update(DATABASE_TABLE_TRIPPLANNER, args, KEY_ROWID + "=" + mRowId, null);
       
	}
	
	
		

}
