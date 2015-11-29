package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.internal.view.menu.MenuView.ItemView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class FollowMe extends FragmentActivity implements OnMapReadyCallback,ConnectionCallbacks, OnConnectionFailedListener,LocationListener, android.location.LocationListener {
	private final LatLng LOCATION_LA = new LatLng(34.022324, -118.282522);
	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
	String userName,userID;
	
	private GoogleMap map;
	private GoogleMap map2;
	private Boolean pathStarted=false;
	private Boolean followingStarted=false;

    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;

    protected Location mCurrentLocation;
    
    protected Boolean mRequestingLocationUpdates;

    protected String mLastUpdateTime;
    private LongLatInfo myTask;

    
    protected static final String TAG = "location-updates-sample";
    
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    private LocationManager locationManager;
	private int poly_num = 3;
	private int poly_tracker = 3;
	private boolean cirCon = false;
	private boolean circ_tracker = false;
	private boolean onDrag = false;
	private boolean infoShown = false;
	private boolean startView = true;
	private boolean holdChange = false;
	private boolean sendMode = false;
	private String locality = null;
	private int dragIndex = -1;
	private int dragRad = 10000;
	private int circRad = 10000;
	private boolean circleDrag = false;
	ArrayList<Polygon> polyShape = new ArrayList<Polygon>();
	
	ArrayList<Integer> polyType = new ArrayList<Integer>();
	
	
	int counterPoly = 0;
	int counterCirc = 0;
	int markClick = 0;
	MenuItem itemSel;
	
	Marker markerWithInfo;
	Marker markerOnDrag;
	
	Activity activity;
	Bundle info;
	
	private LatLng circCenter;
	
	CharSequence[] friendNames;
	CharSequence[] friendIds;
	String followedFriendId;
	String useOnlyRoads;
	LatLngBounds.Builder builder;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = this;

		Log.d("Http", "1");

		Intent intent = getIntent();
		userName = intent.getStringExtra(PalMenu.EXTRA_MESSAGE);
		userID = intent.getStringExtra(PalMenu.USER_ID);
		builder = new LatLngBounds.Builder();
		useOnlyRoads="2";
		
		Log.d("Http", "1.5");

		Log.d("Http", "2");
		
		setContentView(R.layout.follow_me);
    	Button startPath = (Button) findViewById(R.id.btnStartPath);
		startPath.getBackground().setColorFilter(new LightingColorFilter(0xffcccccc, 0xff000000));		
		Button followFriends = (Button) findViewById(R.id.btnFollowFriends);
		followFriends.getBackground().setColorFilter(new LightingColorFilter(0xffcccccc, 0xff000000));
		
		MapFragment mpFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mpFrag.getMapAsync(this);
		
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		
		mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        
        //get friends
        DownloadTask downloadTask = new DownloadTask();
        
        /** Starting the task created above */
        downloadTask.execute("http://54.187.253.246/selectuser/friendlist_postgre.php");

	}

	@SuppressWarnings("finally")
	private String downloadUrl(String strUrl) throws IOException{
	
	    String strFileContents=null;
	    BufferedInputStream in=null;
		
	    try{
	        URL url = new URL(strUrl);
	        /** Creating an http connection to communcate with url */
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	        
	        /** Connecting to url */
	        //urlConnection.connect();
	        urlConnection.setRequestMethod("POST");
	        
	        String urlParameters = "userID="+userID;
			
			// Send post request
	        urlConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	
	        
	        //urlConnection.connect();
	        in = new BufferedInputStream(urlConnection.getInputStream() );
	        byte[] contents = new byte[1024];
	
	        int bytesRead=0;
	         
	        while( (bytesRead = in.read(contents)) != -1){ 
	           strFileContents = new String(contents, 0, bytesRead);               
	        }
	        
			
	
	        /** Creating a bitmap from the stream returned from the url */
	        
	
	    }catch(Exception e){
	        Log.d("Exception while downloading url", e.toString());
	        
	    }finally{
	        in.close();
	        return strFileContents;
	       
	    }
	   
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String>{
	    String bitmap = null;
	    protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FollowMe.this, "Friends information is loading...", "Please wait until the retrieve is complete!", true, false);
        }
	    @Override
	    protected String doInBackground(String... url) {
	        try{
	            bitmap = downloadUrl(url[0]);
	        }catch(Exception e){
	            Log.d("Background Task",e.toString());
	        }
	        return bitmap;
	    }
	
	    @Override
	    protected void onPostExecute(String result) {
	        /** Getting a reference to ImageView to display the
	        * downloaded image
	        */
	    	
	    	if(result.contains("denied"))
	    	{
	    		String[] parts={"NO RESULT"};
				friendNames = null;		        
	    		//createList(parts);
	    		
	    	}
	    	else
	    	{
	    		
				int d=result.length();
	    		String n=result.substring(0, d-7);
	    		result=n;
	    		String[] parts = result.split(" ");
            	//Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            	ArrayList<String> names = new ArrayList<String>();
            	
            	for(int i = 0; i<parts.length/2;i++)
            	{
            		  names.add(parts[i]);  		
            	
            	}
            	String[] namesArray = new String[names.size()];
            	namesArray = names.toArray(namesArray);
            	friendNames=namesArray;
            	
            	ArrayList<String> ids = new ArrayList<String>();
            	for(int i = parts.length/2; i<parts.length;i++)
            	{
            		  ids.add(parts[i]);  		
            	
            	}
            	String[] idArray = new String[ids.size()];
            	idArray = ids.toArray(idArray);
            	friendIds=idArray;
            	//idsArray = new String[ids.size()];
            	//idsArray = ids.toArray(idsArray);            	
            	
            	
	    		//createList(namesArray);
	    	}
   	
	    	
	    	progressDialog.dismiss();
	    	/** Showing a message, on completion of download process */
	        //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	    }
	    
		protected void onCancelled (String result){
			super.onCancelled(result);
		}
	}
	
	private void removeEverything(){
		
		map.clear();
		polyType.clear();
		

	}
	

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }
    
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    
    
	
	
	public void onClick_FollowFriends(View v) {
		Button followFriends = (Button) findViewById(R.id.btnFollowFriends);
		if(followingStarted)
		{
			followingStarted=false;
			
			followFriends.setText("Follow Friends");
			
			followFriends.getBackground().setColorFilter(new LightingColorFilter(0xffcccccc, 0xff000000));
			
			TextView followedFriend = (TextView) findViewById(R.id.followedFriend);
			
			followedFriend.setText("");
			
			removeEverything();
			
			
		}
		else
		{
						
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Make your selection");
	        builder.setItems(friendNames, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	
	            	Button followFriends = (Button) findViewById(R.id.btnFollowFriends);
	            	
	            	followingStarted=true;
	    			
	    			followFriends.setText("End Following");

	    			followFriends.getBackground().setColorFilter(new LightingColorFilter(0xff000000, 0xFFAA0000));
	    			
	    			TextView followedFriend = (TextView) findViewById(R.id.followedFriend);
	    			
	    			followedFriend.setText(friendNames[item]+"  is followed" );
	    			
	    			followedFriend.bringToFront();
	    			followedFriendId=friendIds[item].toString();
	    			new polyShapeGet().execute();
	    			
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();
		}
		
	}
	public void onClick_StartPath(View v) {
		
		Button startPath = (Button) findViewById(R.id.btnStartPath);
		if(pathStarted)
		{
			pathStarted=false;			
			startPath.setText("Start Path");
			startPath.getBackground().setColorFilter(new LightingColorFilter(0xffcccccc, 0xff000000));
			Toast.makeText(this, "PATH SAVING ENDED...", Toast.LENGTH_LONG).show();
			TextView pathMethod = (TextView) findViewById(R.id.followedFriend);			
			pathMethod.setText("");
			removeEverything();
		}
		else
		{
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Make your selection");
		        String[] choices={"Use Roads","Free Roaming"};
		        builder.setItems(choices, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int item) {
		            	
		            	Button startPath = (Button) findViewById(R.id.btnStartPath);
		            	
		            	pathStarted=true;
		    			
		            	startPath.setText("End Path");

		            	startPath.getBackground().setColorFilter(new LightingColorFilter(0xff000000, 0xFFAA0000));
		            	
		            	TextView pathMethod = (TextView) findViewById(R.id.followedFriend);
		    			
		            	
		            	
		            	if(item==0)
		            	{
		            		useOnlyRoads="1";
		            		pathMethod.setText("Road-Path");			    			
			            	pathMethod.bringToFront();
		            	}
		            	else if(item==1)
		            	{
		            		useOnlyRoads="2";
		            		pathMethod.setText("Free Roam");			    			
			            	pathMethod.bringToFront();
		            	}
		    			
		            	//Toast.makeText(this, "PATH SAVING STARTED...", Toast.LENGTH_LONG).show();
		            	
		    			new polyShapeGet().execute();
		    			
		            }
		        });
		        AlertDialog alert = builder.create();
		        alert.show();
			
		}

		
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		map.addMarker(new MarkerOptions()
        .position(new LatLng(0, 0))
        .title("Marker"));
		
	}
	
	private void gotoLocation(double lat, double lng,
			float zoom) {
		//eT2.setText(String.format("%.6f",lat));
    	//eT3.setText(String.format("%.6f",lng));
		if (startView){
			LatLng ll = new LatLng(lat, lng);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
			map.animateCamera(update);
			map.moveCamera(update);
			startView = false;
		}
	}
	
		
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }
	
    

    
    
    /**
     * Update the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        if (mCurrentLocation != null) {
        	gotoLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),1);
        	LatLng myPos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        	double i = (double) mCurrentLocation.getLatitude();
        	double j = (double) mCurrentLocation.getLongitude();
        	if(pathStarted)
        	{
	        	String forToast = "You are at: " + i + " , " +j;
	        	Toast.makeText(this, forToast, Toast.LENGTH_LONG).show();
	        	String URL = "http://54.187.253.246/selectuser/savePath_postgre.php?lat="
						+ i
						+ "&long="
						+ j
						+ "&userID="
						+ userID
						+ "&useOnlyRoads="
						+ useOnlyRoads; 
				final String Link = URL.replace(" ", "%20");
				Log.d("Link", Link);
				myTask = new LongLatInfo();
				if (!myTask.isCancelled())
					myTask.execute(Link);
        	}
			
        	//eT2.setText(String.format("%.6f",i));
        	//eT3.setText(String.format("%.6f",j));
        	
        	//map2.addMarker(new MarkerOptions().position(myPos).title("We Are Here! Suleyman-Keyvan-Ali"));
        	
        	if(followingStarted||pathStarted)
        	{
        		polyType.clear();
        		new polyShapeGet().execute();
        	}
        	
        }
    }
    
    
    
    private class LongLatInfo extends AsyncTask<String, Integer, String>{ // X,Y,Z
    	protected String doInBackground(String... params) { // Z,X
    		
    		try {
    			if (!isCancelled()){
					Log.d("params[0]", params[0]);
					HttpClient client = new DefaultHttpClient();
					StringBuilder url = new StringBuilder(params[0]);
					if (!isCancelled()){
						HttpGet get = new HttpGet(url.toString());
						Log.d("1Error", "1");
						client.execute(get);
					}
    			}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Log.d("4Error", "4");
			return null;
		}

		protected void onPostExecute(String result) { // Z
		}
		
		
		protected void onCancelled (String result){
			super.onCancelled(result);
		}
    }
    
    private class polyShapeGet extends AsyncTask<String, Integer, String>{ // X,Y,Z
    	
    	protected String doInBackground(String... params) { // Z,X
    		
    		
    		try {
    			StringBuilder url=null;
    			if(pathStarted)
    			{
    				url = new StringBuilder("http://54.187.253.246/selectuser/get_path_postgre.php?userID="+userID);
    				HttpGet get = new HttpGet(url.toString());
    				HttpClient client = new DefaultHttpClient();
    				HttpResponse r = client.execute(get);
    				int status = r.getStatusLine().getStatusCode();
    				String data = null;
    				JSONObject explrObject = null;
    				data = null;
    				Log.d("Error", "test0");
    				if (status == 200) {
    					HttpEntity e = r.getEntity();
    					Log.d("polyShapeGet-0", "hi");
    					data = EntityUtils.toString(e);
    				}
    				return data;
    			}
    			else if(followingStarted)
    			{
    				url = new StringBuilder("http://54.187.253.246/selectuser/get_path_postgre.php?userID="+followedFriendId);    				
					HttpGet get = new HttpGet(url.toString());
					HttpClient client = new DefaultHttpClient();
					HttpResponse r = client.execute(get);
					int status = r.getStatusLine().getStatusCode();
					String data = null;
					JSONObject explrObject = null;
					data = null;
					Log.d("Error", "test0");
					if (status == 200) {
						HttpEntity e = r.getEntity();
						Log.d("polyShapeGet-0", "hi");
						data = EntityUtils.toString(e);
					}
					return data;
    			}
    			else
    			{
    				return null;
    			}
					

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String data) { // Z
			if (data != null)
			{
				ArrayList<PolylineOptions> options = new ArrayList<PolylineOptions>();
				Log.d("polyShapeGet-0", "hi-1");
				
				try{
					
					JSONObject explrObject = new JSONObject(data);
					Log.d("polyShapeGet-1", explrObject.toString());
					JSONObject polys = new JSONObject(
							explrObject.getString("polys"));
					counterPoly = Integer.valueOf(polys.getString("polyCounts"));
					
					
					Log.d("polyShapeGet-2", String.valueOf(counterPoly));
					//Log.d("error1polyType-size ",String.valueOf(polyType.size()));
					
					if(followingStarted||pathStarted)
					{
						for (int i = 0; i < counterPoly; i++) 
						{
							Log.d("polyShapeGet-2", "i="+String.valueOf(i)+" "+String.valueOf(counterPoly));
							if(pathStarted)
							{
							options.add(new PolylineOptions()
							.width(20)
							.color(Color.GREEN));
							}
							if(followingStarted)
							{
							options.add(new PolylineOptions()
							.width(20)
							.color(Color.BLUE));
							}
							Log.d("polyShapeGet-3.1", String.valueOf(counterPoly));
							JSONObject pol = new JSONObject(
									polys.getString("poly"+i));
							Log.d("polyShapeGet-3.2", String.valueOf(counterPoly));
							polyType.add(Integer.valueOf(pol.getString("polyType")));
							Log.d("polytype:", String.valueOf(polyType));
							JSONObject points = new JSONObject(
									pol.getString("points"));
							for (int j = 0; j < polyType.get(i); j++) {
								JSONObject pointIs = new JSONObject(
										points.getString("point"+j+"is"));
								//JSONObject point2Is = new JSONObject(
									//	points.getString("point"+(j+1)+"is"));
								Log.d("polyShapeGet-3.3", String.valueOf(counterPoly));
								/*Polyline line = map.addPolyline(new PolylineOptions()
							     .add(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))),new LatLng(Double.valueOf(point2Is.getString("lat")),Double.valueOf(pointIs.getString("long"))))
							     .width(5)
							     .color(Color.RED));*/
								
								options.get(i).add(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))));
								map.addPolyline(options.get(i));
								
								if((j+1 )== polyType.get(i)){							
									map.addMarker(new MarkerOptions()
				                     .position(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))))
				                     .snippet("Lat: "+Double.valueOf(pointIs.getString("lat"))+"\nLong:"+Double.valueOf(pointIs.getString("long")))
				                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.middlepointblue))
				                     .anchor(0.5f, 0.5f));
								}
								else
								{
									map.addMarker(new MarkerOptions()
				                     .position(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))))
				                     .snippet("Lat: "+Double.valueOf(pointIs.getString("lat"))+"\nLong:"+Double.valueOf(pointIs.getString("long")))
				                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.middlepoint))
				                     .anchor(0.5f, 0.5f));
									
								}
								
								 builder.include(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))));
							}
						}
						if(counterPoly>0)
						{
							if(mCurrentLocation!=null)
							{
								double i = (double) mCurrentLocation.getLatitude();
					        	double j = (double) mCurrentLocation.getLongitude();
					        	builder.include(new LatLng(i,j));
				        	}
							
							LatLngBounds bounds = builder.build();
					        int padding = 100; // offset from edges of the map
					                                            // in pixels
					        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
					                padding);
					        map.moveCamera(update);
						}
					}
				} 
				catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println(e1);
				}
			}
				
			}
		protected void onCancelled (String result){
			super.onCancelled(result);
		}
			
		
			/*if (result!=null){
				for (int i = 0; i < result.size(); i++) {

					for (int j = 0; j < result.get(i).getPoints().size(); j++) {
						Geocoder gc = new Geocoder(FollowMe.this);
						List<Address> list = null;
						try {
							list = gc.getFromLocation(result.get(i).getPoints().get(j).latitude, result.get(i).getPoints().get(j).longitude, 1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						Address add = list.get(0);
						locality = add.getLocality();
						
						//markers.get(i).add(map2.addMarker(options));
					}
					//polyShape.add(map2.addPolygon(result.get(i)));
					//polyShapeMap1.add(map.addPolyline(result.get(i)));
				}
			}*/
		}
		
		
		
    
    
 
    protected void stopLocationUpdates() {
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    
    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    
    
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }
    
    
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updateUI();
        }
        
        if (locationManager != null){
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        }
        	
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        
        
    }
    
 
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        
        super.onStop();
    }
    
    
    
    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

 
    
    
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
	
	private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
	    @Override
	    public void onMyLocationChange(Location location) {
	        
	        
	    }
	};


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		updateUI();
                
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
  


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.follow_me_menu, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		
		case R.id.settings:
			item.setChecked(true);
			break;
		case R.id.test:
			item.setChecked(true);
			break;
		case R.id.test1:
			item.setChecked(true);
			break;
		case R.id.test2:
			item.setChecked(true);
			break;
		case R.id.test3:
			item.setChecked(true);
			break;
		case R.id.test4:
			item.setChecked(true);
			break;
		
		default:
			break;	
		}
		return super.onOptionsItemSelected(item);
	}
    
    public void onBackPressed() {
    	if(myTask!=null)
			myTask.cancel(true);
		
    	Button startPath = (Button) findViewById(R.id.btnStartPath);
		startPath.getBackground().setColorFilter(new LightingColorFilter(0xffcccccc, 0xff000000));
		
		Button followFriends = (Button) findViewById(R.id.btnFollowFriends);
		followFriends.getBackground().setColorFilter(new LightingColorFilter(0xffcccccc, 0xff000000));
		
    	Intent intent = new Intent(FollowMe.this,PalMenu.class);
    	intent.putExtra(EXTRA_MESSAGE, userName);
    	intent.putExtra(USER_ID, userID);
		startActivity(intent);
		
		finish();
		//moveTaskToBack(true);
    }
	
    
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
    }
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}


	
}
