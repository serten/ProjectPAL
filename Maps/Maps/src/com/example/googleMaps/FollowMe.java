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
    EditText eT2;
	EditText eT3;
	private LocationManager locationManager;
	List<List<Marker>> markers = new ArrayList<List<Marker>>(); 
	ArrayList<Marker> marker = new ArrayList<Marker>();
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
	ArrayList<Circle> circShape = new ArrayList<Circle>();
	ArrayList<Integer> polyType = new ArrayList<Integer>();
	ArrayList<Integer> circType = new ArrayList<Integer>();
	ArrayList<Polygon> polyShapeMap1 = new ArrayList<Polygon>();
	ArrayList<Circle> circShapeMap1 = new ArrayList<Circle>();
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
		userName = intent.getStringExtra(Login.EXTRA_MESSAGE);
		userID = intent.getStringExtra(Login.USER_ID);
		
		Log.d("Http", "1.5");

		Log.d("Http", "2");
		
		setContentView(R.layout.follow_me);
		
		MapFragment mpFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mpFrag.getMapAsync(this);
		
		MapFragment mpFrag2 = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);
		map2  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2)).getMap();
		mpFrag2.getMapAsync(this);
		

		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map2.getUiSettings().setZoomControlsEnabled(true);
		eT2 = (EditText) findViewById(R.id.editText2);
		eT3 = (EditText) findViewById(R.id.editText3);
		
		final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("tab1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("The Zones", null);
		tabHost.addTab(spec1);

		TabSpec spec2 = tabHost.newTabSpec("tab2");
		spec2.setContent(R.id.tab2);
		spec2.setIndicator("Edit the Zones", null);
		tabHost.addTab(spec2);
		
		mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
	}

	
	protected void setMarker(double latitude, double longitude) {
		// TODO Auto-generated method stub
		Log.d("error1 ","set");
		Geocoder gc = new Geocoder(FollowMe.this);
		List<Address> list = null;
		try {
			list = gc.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Address add = list.get(0);
		locality = add.getLocality();
		
		if (counterPoly==0 && markers.size()== 0){
			markers = new ArrayList<List<Marker>>(); 
			//marker = new ArrayList<Marker>();
			polyShape = new ArrayList<Polygon>();
			polyType = new ArrayList<Integer>();
		}
		
		if (counterCirc==0 && marker.size()== 0){
			//markers = new ArrayList<List<Marker>>(); 
			marker = new ArrayList<Marker>();
			circShape = new ArrayList<Circle>();
			//polyType = new ArrayList<Integer>();
		}
		MarkerOptions options = new MarkerOptions()
		.title(locality)
		.anchor(0.5f, 0.5f)
		.position(new LatLng(latitude, longitude))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker)).draggable(true);
				//defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true);
		Log.d("error2 ","set");
		if (!cirCon){
			
				Log.d("error3 ","set");
				
				Log.d("error3Mark ",String.valueOf(counterPoly));
				
				if (markers.size()!=0 && counterPoly!=0 && markers.get(counterPoly-1).size() == poly_tracker){
					removeEverything();
				}
				
				Log.d("error4 ","set");
				if (marker.size()!=0 && counterCirc!=0 && marker.get(counterCirc-1)!= null){
					removeEverything();
				}
				poly_tracker = poly_num;
				
				Log.d("error5 ","set");
				markers.add(new ArrayList<Marker>());
				Log.d("error5.1 ","set");
				Log.d("counterPoly: ",String.valueOf(counterPoly));
				Log.d("markers.size(): ",String.valueOf(markers.size()));
				if (markers.get(counterPoly).size()==0){
					Log.d("error5.2 ","set");
					options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).anchor(0.5f, 1f);
					Log.d("error5.3 ","set");
					}
				markers.get(counterPoly).add(map2.addMarker(options));
				Log.d("error5.2 ","set");
				//Log.d("error5Mark ",String.valueOf(marker.get(0).size()));
				if (markers.size()!=0 && markers.get(counterPoly).size() == poly_num){
					Log.d("error5.3 ","set");
					ArrayList<Marker> markersComplete = new ArrayList<Marker>(); 
					
					options = new MarkerOptions()
					.title(locality)
					.anchor(0.5f, 0.5f)
					.position(markers.get(counterPoly).get(0).getPosition())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.but_close)).draggable(true);
					markers.get(counterPoly).get(0).remove();
					markers.get(counterPoly).set(0,map2.addMarker(options));
					//drawPolygon();
				}
				Log.d("error6 ","set");
			
			
		} else{
			
			circCenter = new LatLng(latitude, longitude);
			
			if (markers.size() != 0){
				removeEverything();
			}
			
			if (marker.size()!=0 && counterCirc!=0 && marker.get(counterCirc-1)!= null){
				removeEverything();
			}
			//circ_tracker = cirCon;
			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.but_close)).anchor(0.5f, 0.5f).title(locality);
			marker.add(map2.addMarker(options));
			//drawCircle(marker.get(counterCirc).getPosition());
		}
	}

	
	
	private void drawPolygon(){
		
		PolygonOptions options = null;
		if (!onDrag){
			options = new PolygonOptions()
			.fillColor(0x330000FF)
			.strokeWidth(3)
			.strokeColor(Color.BLUE);
			Log.d("error1polyType-size ",String.valueOf(polyType.size()));
			for (int i = 0; i < poly_num; i++) {
				options.add(markers.get(counterPoly).get(i).getPosition());
			}
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
					for (int i = 0; i < polyType.get(j); i++) {
						Log.d("errDrag","ok4");
						Log.d("j = ",String.valueOf(j));
						Log.d("i = ",String.valueOf(i));
						Log.d("markers.get(j).size(): ",String.valueOf(markers.get(j).size()));
						Log.d("markers.get(j).get(i): ",String.valueOf(markers.get(j).get(i)));
						options.add(markers.get(j).get(i).getPosition());
					}
					Log.d("errDrag","ok5");
					polyShape.add(map2.addPolygon(options));
				}
		}
	}
	

	private void drawCircle(LatLng ll) {
		// TODO Auto-generated method stub
		CircleOptions options = null;
		if (!onDrag){
			circType.add(circRad);
			options = new CircleOptions()
			.center(ll)
			.radius(circRad)
			.fillColor(0x66ff0000)
			.strokeColor(Color.CYAN)
			.strokeWidth(3);
			circShape.add(map2.addCircle(options));
			counterCirc = counterCirc +1;
		}else{
				circShape = new ArrayList<Circle>();
				for (int j = 0; j < counterCirc; j++) {
					options = new CircleOptions()
					.center(marker.get(j).getPosition())
					.radius(circRad)
					.fillColor(0x66ff0000)
					.strokeWidth(3)
					.strokeColor(Color.CYAN);
					circShape.add(map2.addCircle(options));
				}
		}	
		onDrag = false;
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
						for (Marker marker : markers.get(i-1)) {
							Log.d("error3 ","rmveth-1");
							marker.remove();
						}
						markers.get(i-1).clear();
						polyShape.get(i-1).remove();
					}
					Log.d("error4 ","rmveth");
					polyShape = new ArrayList<Polygon>();
					
				}
				Log.d("error5 ","rmveth");
				if (circShape.size()!=0 && circShape.get(0) != null) {
					Log.d("error6 ","rmveth");
					
					for (int i = counterCirc; i >0 ; i--) {
						marker.get(i-1).remove();
						circShape.get(i-1).remove();
					}
					//marker = new ArrayList<Marker>();
					circShape = new ArrayList<Circle>();
					Log.d("error7 ","rmveth");
				}
				counterCirc = 0;
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
				Log.d("error4-2-onDrag ",String.valueOf(circShape.size()));
				if (circShape.size()!=0) {
					Log.d("error5--onDrag","rmveth");
					
					for (int i = 0; i <counterCirc ; i++) {
						circShape.get(i).remove();
						circShape.set(i,null);
					}
					circShape = new ArrayList<Circle>();
					Log.d("error6-onDrag","rmveth");
				}
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
				Log.d("error4-2-onDrag ",String.valueOf(circShape.size()));
				if (circShape.size()!=0) {
					Log.d("error5--onDrag","rmveth");
					
					for (int i = 0; i <counterCirc ; i++) {
						circShape.get(i).remove();
						circShape.set(i,null);
					}
					circShape = new ArrayList<Circle>();
					Log.d("error6-onDrag","rmveth");
				}
				Log.d("onDrag-end","rmveth");
			}else{
				//do nothing
			}
		}
	}
	
	
	
	public void holdChange(View v) {
		Log.d("hold","is changing");
		Button but = (Button) findViewById(R.id.hold);
		if(but.getText() =="Hold is On"){
			Log.d("hold","is off");
			but.setText("Hold is Off");
			but.setBackgroundColor(Color.GRAY);
			holdChange = false;
		}else{
			Log.d("hold","is On");
			but.setText("Hold is On");
			but.setBackgroundColor(Color.GREEN);
				
			holdChange = true;
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
    
    
	
	
	public void onClick_Sat(View v) {
//		CameraUpdate update = CameraUpdateFactory.newLatLng(LOCATION_BURNABY);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_LA, 9);
		map.animateCamera(update);
	}
	public void onClick_Norm(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_LA, 9);
		map.animateCamera(update);
		
	}
	public void onClick_Ter(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_LA, 9);
		map.animateCamera(update);
		
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
		eT2.setText(String.format("%.6f",lat));
    	eT3.setText(String.format("%.6f",lng));
		if (startView){
			LatLng ll = new LatLng(lat, lng);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
			map2.animateCamera(update);
			map.moveCamera(update);
			startView = false;
		}
	}
	
	public void geoLocate(View v) throws IOException {
		hideSoftKeyboard(v);
		
		EditText et = (EditText) findViewById(R.id.editText1);
		String location = et.getText().toString();
		
		Geocoder gc = new Geocoder(this);
		List<Address> list = gc.getFromLocationName(location, 1);
		if (!list.isEmpty()){
			et.setText("");
			Toast.makeText(this, "Processing ...",Toast.LENGTH_SHORT).show();
			Address add = list.get(0);
			
			String locality = add.getLocality();
			double lat = add.getLatitude();
			double lng = add.getLongitude();
			startView = true;
			gotoLocation(lat,lng,8);
			
		}else{
			et.setText("");
			Toast.makeText(this, "Please Enter a Valid Location to Look Up For You!",Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	private void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
        	String forToast = "You are at: " + i + " , " +j;
        	//Toast.makeText(this, forToast, Toast.LENGTH_LONG).show();
        	String URL = "http://cs-server.usc.edu:1111/maps.php/?lat="
					+ i
					+ "&long="
					+ j
					+ "&user="
					+ userName; 
			final String Link = URL.replace(" ", "%20");
			Log.d("Link", Link);
			myTask = new LongLatInfo();
			if (!myTask.isCancelled())
				myTask.execute(Link);
			
        	eT2.setText(String.format("%.6f",i));
        	eT3.setText(String.format("%.6f",j));
        	
        	map2.addMarker(new MarkerOptions().position(myPos).title("We Are Here! Suleyman-Keyvan-Ali"));
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
    
   /* private class shapeSend extends AsyncTask<String, Integer, String>{ // X,Y,Z
    	protected String doInBackground(String... params) { // Z,X
    		 
    		try {
    			Log.d("shapeSend", "1");
    			if (!isCancelled()){
					Log.d("params[0]", params[0]);
					Log.d("shapeSend", "2");
					HttpClient client = new DefaultHttpClient();
					StringBuilder url = new StringBuilder(params[0]);
					if (!isCancelled()){
						HttpGet get = new HttpGet(url.toString());
						Log.d("shapeSend", "3");
						client.execute(get);
					}
    			}
    			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Log.d("shapeSend", "4");
			return null;
		}

		protected void onPostExecute(String result) { // Z
			
		}
		
		
		protected void onCancelled (String result){
			super.onCancelled(result);
		}
    }
    */

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
					markers.add(new ArrayList<Marker>());
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
						
						MarkerOptions options = new MarkerOptions()
						.title(locality)
						.anchor(0.5f, 0.5f)
						.position(new LatLng(result.get(i).getPoints().get(j).latitude, result.get(i).getPoints().get(j).longitude))
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker)).anchor(0.5f, 0.5f).title(locality).draggable(true);
						if (j==0){
							options.icon(BitmapDescriptorFactory.fromResource(R.drawable.but_close));
						}
						markers.get(i).add(map2.addMarker(options));
					}
					polyShape.add(map2.addPolygon(result.get(i)));
					polyShapeMap1.add(map.addPolygon(result.get(i)));
				}
			}
		}
		
		
		protected void onCancelled (ArrayList<PolygonOptions> result){
			super.onCancelled(result);
		}
    }
    
    
    private class circShapeGet extends AsyncTask<String, Integer, ArrayList<CircleOptions>>{ // X,Y,Z
    	
    	protected ArrayList<CircleOptions> doInBackground(String... params) { // Z,X
    		
    		ArrayList<CircleOptions> options = new ArrayList<CircleOptions>();
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
					Log.d("circShapeGet-0", "hi");
					data = EntityUtils.toString(e);
					Log.d("circShapeGet-0", "hi-1");
					explrObject = new JSONObject(data);
					Log.d("circShapeGet-1", explrObject.toString());
					JSONObject circles = new JSONObject(
							explrObject.getString("circles"));
					counterCirc = Integer.valueOf(circles.getString("circCounts"));
					
					Log.d("circShapeGet-2", String.valueOf(counterCirc));
					//Log.d("error1polyType-size ",String.valueOf(polyType.size()));
					
					
					//circShape.add(map2.addCircle(options));
					//counterCirc = counterCirc +1;
					
					
					for (int i = 0; i < counterCirc; i++) {
						
						Log.d("circShapeGet-2", "i="+String.valueOf(i)+" "+String.valueOf(counterCirc));
						Log.d("circShapeGet-3.1", String.valueOf(counterCirc));
						JSONObject circ = new JSONObject(
								circles.getString("circ"+i));
						
						Log.d("circShapeGet-3.2", String.valueOf(counterCirc));
						circType.add(Integer.valueOf(circ.getString("radius")));
						Log.d("circShapeGet-3.3", String.valueOf(counterCirc));
						//options.get(i).add(new LatLng(Double.valueOf(pointIs.getString("lat")),Double.valueOf(pointIs.getString("long"))));
						options.add(new CircleOptions()
						.center(new LatLng(Double.valueOf(circ.getString("centerLat")),Double.valueOf(circ.getString("centerLong"))))
						.radius(Integer.valueOf(circ.getString("radius")))
						.fillColor(0x66ff0000)
						.strokeColor(Color.CYAN)
						.strokeWidth(3));
						Log.d("centerLat-3.35",String.valueOf(Double.valueOf(circ.getString("centerLat"))));
						Log.d("centerLong-3.36",String.valueOf(Double.valueOf(circ.getString("centerLong"))));
						Log.d("radius-3.37",String.valueOf(Double.valueOf(circ.getString("radius"))));
						Log.d("circShapeGet-3.4", String.valueOf(counterCirc));
					}
					Log.d("error1counterPoly ",String.valueOf(counterCirc));
					Log.d("optins ", options.toString());
					Log.d("optins ", String.valueOf(options.size()));
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

		protected void onPostExecute(ArrayList<CircleOptions> result) { // Z
			//Log.d("Onpostexecute-1",circShape.toString());
			//Log.d("Onpostexecute-1",result.toString());
			if (result!=null){
				for (int i = 0; i < result.size(); i++) {
					
					Geocoder gc = new Geocoder(FollowMe.this);
					List<Address> list = null;
					try {
						list = gc.getFromLocation(result.get(i).getCenter().latitude, result.get(i).getCenter().longitude, 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					Address add = list.get(0);
					locality = add.getLocality();
					
					MarkerOptions options = new MarkerOptions()
					.title(locality)
					.anchor(0.5f, 0.5f)
					.position(new LatLng(result.get(i).getCenter().latitude, result.get(i).getCenter().longitude))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.but_close)).anchor(0.5f, 0.5f).title(locality).draggable(true);
					marker.add(map2.addMarker(options));
					
					Log.d("2-Onpostexecute-i=",String.valueOf(i));
					circShape.add(map2.addCircle(result.get(i)));
					circShapeMap1.add(map.addCircle(result.get(i)));
				}	
			}
			Log.d("Onpostexecute-3","End");
		}
		
		
		protected void onCancelled (ArrayList<CircleOptions> result){
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
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 0, this);
        }
        	
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        
        
    }
    
 
    
    public void onClickHandler(View v) {
		switch (v.getId()) {
		case R.id.myPic:
			Intent in = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/keyvan.noury?fref=ts")); 
			startActivity(in); 
			break;
		case R.id.myPic2:
			Intent in1 = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/profile.php?id=705320455"));
			startActivity(in1); 
			break;
		case R.id.myPic3:
			Intent in2 = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/ali.ghahramani")); 

			startActivity(in2);
			break;
		// put your onclick code here
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
		getMenuInflater().inflate(R.menu.follow_me_menu, menu);
		return true;
	}
	
	/*public void getEverything(){
		eraseMap1();
		holdChange = false;
		removeEverything();
		Button but = (Button) findViewById(R.id.hold);
		but.setText("Hold is On");
		but.setBackgroundColor(Color.GREEN);
		holdChange = true;
		
		markers = new ArrayList<List<Marker>>(); 
		polyShape = new ArrayList<Polygon>();
		polyType = new ArrayList<Integer>();
	 
		marker = new ArrayList<Marker>();
		circShape = new ArrayList<Circle>();
		new polyShapeGet().execute();
		new circShapeGet().execute();

	}*/
	
	
	public void eraseMap1(){
		if (polyShapeMap1.size()!=0 && polyShapeMap1.get(0) != null){
			int sizeShapeOnMap1 = polyShapeMap1.size();
			for (int i = sizeShapeOnMap1; i>0 ; i--) {
				polyShapeMap1.get(i-1).remove();
			}
			polyShapeMap1 = new ArrayList<Polygon>();
		}
		if (circShapeMap1.size()!=0 && circShapeMap1.get(0) != null) {
			int sizeCircShapeOnMap1 = circShapeMap1.size();
			for (int i = sizeCircShapeOnMap1; i >0 ; i--) {
				circShapeMap1.get(i-1).remove();
			}
			circShapeMap1 = new ArrayList<Circle>();
		}
	}
	
	/*public void saveShapes() {
		boolean startIs = true;
		if (counterPoly==0){
			String URLMarker = "http://cs-server.usc.edu:1111/allMarkers.php/?start=" + startIs + "&user="+userName
					+ "&counterPoly=0"
					+ "&Polytype=0"
					+ "&polyCounts=0";
			String LinkMarker = URLMarker.replace(" ", "%20");
			new shapeSend().execute(LinkMarker);
		}
		
		for (int i = 0; i < counterPoly; i++) {
			Log.d("send1", "Hello");
			
			String URLMarker = "http://cs-server.usc.edu:1111/allMarkers.php/?start=" + startIs + "&user="+userName
					+"&counterPoly="+ i
					+"&Polytype=" + polyType.get(i);
			
			if (startIs){
				URLMarker += "&polyCounts="+counterPoly;
				startIs = false;
			}
			for (int j = 0; j < polyType.get(i); j++) {
				URLMarker += "&point"+j+"lat="+markers.get(i).get(j).getPosition().latitude;
				URLMarker += "&point"+j+"long="+markers.get(i).get(j).getPosition().longitude;
			}
			
			Log.d("send2", "Hello");
		    String LinkMarker = URLMarker.replace(" ", "%20");
		    Log.d("send3", "Hello");
		    Log.d("URLMarker", URLMarker);
			
			new shapeSend().execute(LinkMarker);
			//new shapeSend().execute(LinkPolyShapes);
		}
		
		startIs = true;
		boolean endIs = false;
		
		
		if (counterCirc==0){
			endIs = true;
			String URLMarker = "http://cs-server.usc.edu:1111/allMarkers.php/?start=" + startIs + "&end=" + endIs + "&user="+userName
					+"&counterCirc=0&centerLat=0&centerLong=0&radius=0&circCounts=0";
			String LinkMarker = URLMarker.replace(" ", "%20");
			new shapeSend().execute(LinkMarker);
		}
		
		for (int i = 0; i < counterCirc; i++) {
			Log.d("send1", "Hello");
			
			if (i == counterCirc-1)
				endIs = true;
			
			String URLMarker = "http://cs-server.usc.edu:1111/allMarkers.php/?start="+startIs+"&end="+endIs+"&user="+userName
					+"&counterCirc="+ i;
			
			URLMarker += "&centerLat=" + marker.get(i).getPosition().latitude;
			URLMarker += "&centerLong=" + marker.get(i).getPosition().longitude + "&radius="+circType.get(i);
	
			if (startIs){
				URLMarker += "&circCounts="+counterCirc;
				startIs = false;
			}
		
			Log.d("send2", "Hello");
		    String LinkMarker = URLMarker.replace(" ", "%20");
			Log.d("Link", LinkMarker);
			
			new shapeSend().execute(LinkMarker);
		}
	}
	*/
	
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
    	Intent intent = new Intent(FollowMe.this,PalMenu.class);
    	intent.putExtra(EXTRA_MESSAGE, userName);
    	intent.putExtra(USER_ID, userID);
		startActivity(intent);
		myTask.cancel(true);
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
