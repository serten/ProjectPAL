package com.example.googleMaps;

import java.io.IOException;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

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
	
	ArrayList<Polygon> polyShapeMap1 = new ArrayList<Polygon>();
	
	int counterPoly = 0;
	int counterCirc = 0;
	int markClick = 0;
	MenuItem itemSel;
	
	Marker markerWithInfo;
	Marker markerOnDrag;
	
	Activity activity;
	Bundle info;
	
	private LatLng circCenter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = this;

		Log.d("Http", "1");

		Intent intent = getIntent();
		userName = intent.getStringExtra(PalMenu.EXTRA_MESSAGE);
		userID = intent.getStringExtra(PalMenu.USER_ID);
				
		Log.d("Http", "1.5");

		Log.d("Http", "2");
		
		setContentView(R.layout.follow_me);
		
		MapFragment mpFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mpFrag.getMapAsync(this);
		
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		
		//eT2 = (EditText) findViewById(R.id.editText2);
		//eT3 = (EditText) findViewById(R.id.editText3);
		
		

		/*TabSpec spec2 = tabHost.newTabSpec("tab1");
		spec2.setContent(R.id.tab1);
		spec2.setIndicator("Edit the Zones", null);
		tabHost.addTab(spec2);
		*/
		mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
	}

	
	private void drawPolygon(){
		
		PolygonOptions options = null;
		if (!onDrag){
			options = new PolygonOptions()
			.fillColor(0x330000FF)
			.strokeWidth(3)
			.strokeColor(Color.BLUE);
			Log.d("error1polyType-size ",String.valueOf(polyType.size()));
			Log.d("error1counterPoly ",String.valueOf(counterPoly));
			polyShape.add(map2.addPolygon(options));
			Log.d("error1counterPoly ","ok1");
			polyType.add(poly_num);
			Log.d("error1counterPoly ","ok2");
			counterPoly ++;
			Log.d("error1","ok3");
		}else{
				//removeEverything();
				Log.d("errDrag","ok1");
				polyShape = new ArrayList<Polygon>();
				for (int j = 0; j < counterPoly; j++) {
					Log.d("errDrag","ok2");
					options = new PolygonOptions()
					.fillColor(0x330000FF)
					.strokeWidth(3)
					.strokeColor(Color.BLUE);
					Log.d("errDrag","ok3");
					Log.d("errDrag","ok5");
					polyShape.add(map2.addPolygon(options));
				}
		}
	}
	

	
	
	
	private void removeEverything(){
		
		Log.d("error0 ","rmveth");
		
		if (!holdChange){
			if (!onDrag){
				Log.d("error1 ","rmveth");
				Log.d("error1size ",String.valueOf(polyShape.size()));
				if (polyShape.size()!=0 && polyShape.get(0) != null){
					Log.d("error2 ","rmveth");
					
					Log.d("error3 ","rmveth");
					Log.d("error3CP ",String.valueOf(counterPoly));
					for (int i = counterPoly; i>0 ; i--) {
						Log.d("error3i ",String.valueOf(i));
						polyShape.get(i-1).remove();
					}
					Log.d("error4 ","rmveth");
					polyShape = new ArrayList<Polygon>();
					
				}
				Log.d("error5 ","rmveth");
				counterPoly = 0;
			}else{
				if (polyShape.size()!=0){
					Log.d("error2-onDrag","rmveth");

					for (int i = 0 ; i<counterPoly ; i++) {
						polyShape.get(i).remove();
						Log.d("error3-onDrag","rmveth");
						polyShape.set(i,null);
					}
					polyShape = new ArrayList<Polygon>();
					Log.d("error4-onDrag ","rmveth");
				}
				Log.d("error4-1-onDrag ","rmveth");
				Log.d("onDrag-end","rmveth");
			}
		}else{
			if (onDrag){
				if (polyShape.size()!=0){
					Log.d("error2-onDrag2","rmveth");

					for (int i = 0 ; i<counterPoly ; i++) {
						polyShape.get(i).remove();
						Log.d("error3-onDrag2","rmveth");
						Log.d("i = ",String.valueOf(i));
						polyShape.set(i,null);
					}
					polyShape = new ArrayList<Polygon>();
					Log.d("error4-onDrag ","rmveth");
				}
				Log.d("error4-1-onDrag ","rmveth");
				Log.d("onDrag-end","rmveth");
			}else{
				//do nothing
			}
		}
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
			
			
		}
		else
		{
						
			final CharSequence[] items = {
	                "Rajesh", "Mahesh", "Vijayakumar"
	        };

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Make your selection");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	            	
	            	Button followFriends = (Button) findViewById(R.id.btnFollowFriends);
	            	
	            	followingStarted=true;
	    			
	    			followFriends.setText("End Following");

	    			followFriends.getBackground().setColorFilter(new LightingColorFilter(0xff000000, 0xFFAA0000));
	    			
	    			TextView followedFriend = (TextView) findViewById(R.id.followedFriend);
	    			
	    			followedFriend.setText(items[item]);
	    			
	    			followedFriend.bringToFront();
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
		}
		else
		{
			pathStarted=true;
			startPath.setText("End Path");
			//startPath.setBackgroundColor(Color.RED);
			int color = Color.parseColor("#AE6118"); //The color u want           
			
			v.getBackground().setColorFilter(new LightingColorFilter(0xff000000, 0xFFAA0000));
			Toast.makeText(this, "PATH SAVING STARTED...", Toast.LENGTH_LONG).show();
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
        	gotoLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),10);
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
						+ userID; 
				final String Link = URL.replace(" ", "%20");
				Log.d("Link", Link);
				myTask = new LongLatInfo();
				if (!myTask.isCancelled())
					myTask.execute(Link);
        	}
			
        	//eT2.setText(String.format("%.6f",i));
        	//eT3.setText(String.format("%.6f",j));
        	
        	//map2.addMarker(new MarkerOptions().position(myPos).title("We Are Here! Suleyman-Keyvan-Ali"));
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
    
    private class polyShapeGet extends AsyncTask<String, Integer, ArrayList<PolygonOptions>>{ // X,Y,Z
    	
    	protected ArrayList<PolygonOptions> doInBackground(String... params) { // Z,X
    		
    		ArrayList<PolygonOptions> options = new ArrayList<PolygonOptions>();
    		try {
				StringBuilder url = new StringBuilder("http://cs-server.usc.edu:1111/"+userName+"-allMarkers.txt");
				HttpGet get = new HttpGet(url.toString());
				HttpClient client = new DefaultHttpClient();
				HttpResponse r = client.execute(get);
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				
				Log.d("Error", "test0");
				if (status == 200) {
					HttpEntity e = r.getEntity();
					Log.d("polyShapeGet-0", "hi");
					data = EntityUtils.toString(e);
					Log.d("polyShapeGet-0", "hi-1");
					explrObject = new JSONObject(data);
					Log.d("polyShapeGet-1", explrObject.toString());
					JSONObject polys = new JSONObject(
							explrObject.getString("polys"));
					counterPoly = Integer.valueOf(polys.getString("polyCounts"));
					
					
					Log.d("polyShapeGet-2", String.valueOf(counterPoly));
					//Log.d("error1polyType-size ",String.valueOf(polyType.size()));
					
					for (int i = 0; i < counterPoly; i++) {
						Log.d("polyShapeGet-2", "i="+String.valueOf(i)+" "+String.valueOf(counterPoly));
						options.add(new PolygonOptions()
						.fillColor(0x330000FF)
						.strokeWidth(3)
						.strokeColor(Color.BLUE));
						Log.d("polyShapeGet-3.1", String.valueOf(counterPoly));
						JSONObject pol = new JSONObject(
								polys.getString("poly"+i));
						Log.d("polyShapeGet-3.2", String.valueOf(counterPoly));
						polyType.add(Integer.valueOf(pol.getString("polyType")));
						JSONObject points = new JSONObject(
								pol.getString("points"));
						for (int j = 0; j < polyType.get(i); j++) {
							JSONObject pointIs = new JSONObject(
									points.getString("point"+j+"is"));
							Log.d("polyShapeGet-3.3", String.valueOf(counterPoly));
							options.get(i).add(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))));
						}
						
					}
					Log.d("error1counterPoly ",String.valueOf(counterPoly));
					
					Log.d("error1counterPoly ","ok1");
					
					Log.d("Error", "test3");
					return options;
					
				} else {
					Toast.makeText(FollowMe.this, "error",
							Toast.LENGTH_SHORT);
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1);
			}
			return null;
		}

		protected void onPostExecute(ArrayList<PolygonOptions> result) { // Z
			if (result!=null){
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
					polyShapeMap1.add(map.addPolygon(result.get(i)));
				}
			}
		}
		
		
		protected void onCancelled (ArrayList<PolygonOptions> result){
			super.onCancelled(result);
		}
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
	
	
	public void eraseMap1(){
		if (polyShapeMap1.size()!=0 && polyShapeMap1.get(0) != null){
			int sizeShapeOnMap1 = polyShapeMap1.size();
			for (int i = sizeShapeOnMap1; i>0 ; i--) {
				polyShapeMap1.get(i-1).remove();
			}
			polyShapeMap1 = new ArrayList<Polygon>();
		}
		
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
