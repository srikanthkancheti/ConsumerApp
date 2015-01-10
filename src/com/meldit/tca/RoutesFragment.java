package com.meldit.tca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meldit.tca.map.RouteMapActivity;
import com.meldit.tca.parsers.HandleAllRoutesXML;
import com.meldit.tca.parsers.HandleAllRoutesXML.RoutesModel;

public class RoutesFragment extends Fragment {

	private View rootView;
	private String [] startPage = new String[2];
	private String [] endpage = new String[2];
	private String result;
	private HttpResponse response;
	private ListView routes_lv;
	private RoutesListAdapter routesAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.routes, container, false);
		
		startPage[0] = "1";
		endpage[0] = "10";
		
		InitializeUI();
		
		getAllRoutes();
		
		routesAdapter = new RoutesListAdapter(getActivity(), Utilities.routesAdapter);
		
		return rootView;
	}

	private void getAllRoutes() {
		// TODO Auto-generated method stub
		new getSelectedRoutes().execute(startPage[0], endpage[0]);
	}

	private void InitializeUI() {
		// TODO Auto-generated method stub
		routes_lv = (ListView) rootView.findViewById(R.id.routes_list);
//		route_num_tv = (TextView)rootView.findViewById(R.id.button_route_no);
//		source_tv = (TextView) rootView.findViewById(R.id.text_from);
//		destination_tv = (TextView) rootView.findViewById(R.id.text_to);
		
	}
	
	public class getSelectedRoutes extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(Utilities.getAllRoutes_url+params[0]+"&size="+params[1]);
			        
		           response = httpClient.execute(httpGet);
			           
			       result = EntityUtils.toString(response.getEntity());
			       Log.i("all routes result","================================"+result);
			         

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
			if(null!= result){
				if(result.contains("No Services")){
					Toast.makeText(getActivity(),"No Route Available", Toast.LENGTH_SHORT).show();
				}else{
					InputStream is = new ByteArrayInputStream(result.getBytes());
					//new WebServiceParsers(is);
					HandleAllRoutesXML parseRoutes = new HandleAllRoutesXML();
					Utilities.routesAdapter = parseRoutes.parseRoutesXML(is);
					routes_lv.setAdapter(routesAdapter);
		
					}
			  }else{
				  Toast.makeText(getActivity(),"No Services Available", Toast.LENGTH_SHORT).show();
			  }
			}

	}
	
	
	public class RoutesListAdapter extends BaseAdapter{
		
		private Context _context;

		public RoutesListAdapter(FragmentActivity activity,
				ArrayList<RoutesModel> routesAdapter) {
			// TODO Auto-generated constructor stub
			this._context = activity;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Utilities.routesAdapter.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Utilities.routesAdapter.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return Utilities.routesAdapter.indexOf(getItem(position));
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			
			LayoutInflater mInflater = (LayoutInflater) 
					this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        if (convertView == null) {
		            convertView = mInflater.inflate(R.layout.route_list, null);
		            holder = new ViewHolder();
		            holder.route_num_tv = (TextView) convertView.findViewById(R.id.button_route_no);
		            holder.source_tv = (TextView) convertView.findViewById(R.id.text_from);
		            holder.destination_tv = (TextView) convertView.findViewById(R.id.text_to);
		            holder.route_item_rl = (RelativeLayout) convertView.findViewById(R.id.routes_list_item_rl);
		            convertView.setTag(holder);
		        }
		        else {
		            holder = (ViewHolder) convertView.getTag();
		        }
		         
		         
		        holder.route_num_tv.setText(Utilities.routesAdapter.get(position).getRouteNo());
		        holder.source_tv.setText(Utilities.routesAdapter.get(position).getSourceName());
		        holder.destination_tv.setText(Utilities.routesAdapter.get(position).getDestinationName());
		        
		        holder.route_item_rl.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String routeNumber = Utilities.routesAdapter.get(position).getRouteNo();
						Intent mapIntent = new Intent(getActivity(), RouteMapActivity.class).putExtra("route_no", routeNumber);
						startActivity(mapIntent);
					}
				});
		        
			return convertView;
		}
		
		/*private view holder class*/
	    private class ViewHolder {
	        TextView route_num_tv, source_tv, destination_tv;
	        RelativeLayout route_item_rl;
	    }

	}
}

