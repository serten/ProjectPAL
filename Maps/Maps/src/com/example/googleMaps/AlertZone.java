//works the best and correct with the notifications-9:10am with myhandler fixed 
package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.menu;
import android.R.string;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.internal.view.menu.MenuView.ItemView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
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
import com.google.android.gms.plus.model.people.Person.Name;

public class AlertZone extends FragmentActivity implements OnMapReadyCallback,ConnectionCallbacks, OnConnectionFailedListener,LocationListener, android.location.LocationListener {
	private final LatLng LOCATION_LA = new LatLng(34.022324, -118.282522);
	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	final static String GROUP_KEY_EMAILS = "group_key_emails";
	public final static String USER_ID = "com.example.USERID";
	public final static String ZOOM_ZONE = "com.example.ZOOMZONE";
	//public final static int tInterval = 7000;
	String userName,userID,zoomZone;
	
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
	ArrayList<Marker> friendMarkers = new ArrayList<Marker>();
	private int poly_num = 3;
	private int poly_tracker = 3;
	private boolean cirCon = false;
	private boolean circ_tracker = false;
	private boolean onDrag = false;
	private boolean infoShown = false;
	private boolean startView = true;
	private boolean holdChange = false;
	private boolean sendMode = false;
	private boolean checkLinkOk = false;
	private boolean justStartedActivityForNotifications = true;
	private String locality = null;
	private int dragIndex = -1;
	private int dragRad = 10000;
	private int counterWait;
	private int circRad = 10000;
	private double myCurrentLat = 0;
	private double myCurrentLong = 0;
	private boolean circleDrag = false;
	private UiLifecycleHelper uiHelper;
	
	ArrayList<Polygon> polyShape = new ArrayList<Polygon>();
	ArrayList<String> polyNames = new ArrayList<String>();
	ArrayList<String> polyNamesTrash = new ArrayList<String>();
	ArrayList<String> friendsNameInsideZonesList = new ArrayList<String>();
	ArrayList<String> friendsNameInsideZonesListCompare = new ArrayList<String>();
	ArrayList<LatLng> friendsInsideZonesLocation = new ArrayList<LatLng>();
	ArrayList<String> friendsInsideZonesUpdateTime = new ArrayList<String>();
	ArrayList<String> zonesNamesHaveFriendsInside = new ArrayList<String>();
	ArrayList<String> zonesNamesHaveFriendsInsideCompare = new ArrayList<String>();
	ArrayList<String> zonesSelectedToShowFriendsInside = new ArrayList<String>();
	ArrayList<Circle> circShape = new ArrayList<Circle>();
	ArrayList<Integer> polyType = new ArrayList<Integer>();
	ArrayList<Integer> circType = new ArrayList<Integer>();
	ArrayList<Polygon> polyShapeMap1 = new ArrayList<Polygon>();
	ArrayList<Circle> circShapeMap1 = new ArrayList<Circle>();
	int counterPoly = 0;
	int counterCirc = 0;
	int markClick = 0;
	int polyBeingShown = -1;
	int viewBeingShown = 0;
	int friendBeingShown = -1;
	MenuItem itemSel;

	
	Marker markerWithInfo;
	Marker markerOnDrag;
	EditText name;
	String polyName;
	String oneItem;
	String URL;
	String propertyAddress;
	String picture;
	String description;
	String addLink;
	String postId;
	String randomMove = "b";
	
	Activity activity;
	Bundle info;
	//private LatLng circCenter;
    
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mListTitles;
    private NotificationManager mnm;
	
    private String[] actionsForActioBar = new String[] {
            "Hold is On",
            "Hols is Off",
        };
    
    Bundle notificationBundle = null;
    int numMessages = 0;
    Handler myHandlerForZones = new Handler();
     
    private ArrayList<String> nameOfAllFriends = new ArrayList<String>();
    Marker oneFriendMarker;
    ArrayList<Marker> kNNFriendsMarkers = new ArrayList<Marker>();
    ArrayList<Marker> distFriendsMarkers = new ArrayList<Marker>();
    
    Circle myBlinkingCirc;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = this;

		
		
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		
		Log.d("Http", "1");

		Intent intent = getIntent();
		setIntent(intent);
		userName = intent.getStringExtra(PalMenu.EXTRA_MESSAGE);
		userID = intent.getStringExtra(PalMenu.USER_ID);
		
		if (notificationBundle != null) {
            String notificationData = notificationBundle.getString(ZOOM_ZONE);
            if(!notificationData.equals("com.example.ZOOMZONE")) {
            	userID = notificationBundle.getString(USER_ID);
    			userName = notificationBundle.getString(EXTRA_MESSAGE);
            	//Toast.makeText(this, notificationData,Toast.LENGTH_SHORT).show();
            	LatLngBounds.Builder builder = new LatLngBounds.Builder();
		        for (Marker m : markers.get(Integer.valueOf(notificationData))) {
		            builder.include(m.getPosition());
		        }
		        LatLngBounds bounds = builder.build();
		        int padding = 100; // offset from edges of the map
		                                            // in pixels
		        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
		                padding);
		        Toast.makeText(getBaseContext(), notificationData,Toast.LENGTH_SHORT).show();
		        map.animateCamera(update);
				map2.animateCamera(update);
            	Log.d("My toast", "should show");
            	notificationBundle = null;
            	if(mnm!=null)
            		mnm.cancelAll();
            	//mnm = null;
            }
        	Log.d("Bundle", "not empty");
        }else
        	Log.d("Bundle", "is empty!");
		
		/*Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String notificationData = bundle.getString(ZOOM_ZONE);
            //if (notificationData == null) {
            	Toast.makeText(this, "Hello from the other side",Toast.LENGTH_SHORT).show();
            	Log.d("My toast", "should show");
            //}

        }*/

		PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.googleMaps",PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(),0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
		    }
	        } catch (NameNotFoundException e1) {
	            Log.e("name not found", e1.toString());
	        } catch (NoSuchAlgorithmException e) {
	            Log.e("no such an algorithm", e.toString());
	        } catch (Exception e) {
	            Log.e("exception", e.toString());
        }
		
		
		Log.d("Http", "1.5");

		Log.d("Http", "2");
		
		setContentView(R.layout.alert_zone);
		
		/*mDropDownTitles = getResources().getStringArray(R.array.myDrop_Down_List);
		mDropDownList = (ListView) findViewById(R.id.drop_down);
		mDropDownList.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, mDropDownTitles));
		//ArrayAdapter<String> adapterForDropDown = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actionsForActioBar);
		mDropDownList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int itemPosition,
					long arg3) {
				// TODO Auto-generated method stub
				switch (itemPosition){
            	case 0:
            		if (!holdChange)
            			holdChange();
            		break;
            	case 1:
            		if (holdChange)
            			holdChange();
            		break;
        		default:
        			break;
            	}
				
			}
		});
		*/
		
		final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec2 = tabHost.newTabSpec("tab2");
		spec2.setContent(R.id.tab2);
		spec2.setIndicator("Edit the Zones", null);
		tabHost.addTab(spec2);
		
		TabSpec spec1 = tabHost.newTabSpec("tab1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("The Zones", null);
		tabHost.addTab(spec1);
		
		
		//tabHost.setCurrentTab(1);
		
		Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {			
			public void run() {
		     // Actions to do after 0.6 seconds
				tabHost.setCurrentTab(1);
				Handler handler2 = new Handler();
				handler2.postDelayed(new Runnable() {
					public void run() {
					    // Actions to do after 1.2 seconds
						tabHost.setCurrentTab(1);
				}}, 1400);
		    }}, 900);
		ArrayAdapter<String> adapterForDropDown = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actionsForActioBar);
		
        /** Enabling dropdown list navigation for the action bar */
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
 
        /** Defining Navigation listener */
        @SuppressWarnings("deprecation")
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
 
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            	switch (itemPosition){
            	case 0:
            		if (!holdChange)
            			holdChange();
            		break;
            	case 1:
            		if (holdChange)
            			holdChange();
            		break;
        		default:
        			break;
            	}
                //Toast.makeText(getBaseContext(), "You selected : " + actionsForActioBar[itemPosition]  , Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        /** Setting dropdown items and item navigation listener for the actionbar*/
        getActionBar().setListNavigationCallbacks(adapterForDropDown, navigationListener);
        
		mListTitles = getResources().getStringArray(R.array.myMaps_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Log.d("onCreate","-1.5");
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        Log.d("onCreate","-1.6");
        // set up the drawer's list view with items and click listener
        
		
		MapFragment mpFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mpFrag.getMapAsync(this);
		
		MapFragment mpFrag2 = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);
		map2  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2)).getMap();
		mpFrag2.getMapAsync(this);
		getEverything();
		//get the friends list for future use 
		new nameOfAllFriendsAsync().execute();
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.alert_zone_drawer_list_item, mListTitles));
        Log.d("onCreate","-1.7");
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        Log.d("onCreate","-2");        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        Log.d("onCreate","-3");
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                Log.d("onCreate","-4");
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                Log.d("onCreate","-5");
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            Log.d("onCreate","-6");
            selectItem(0);
        }
		
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map2.getUiSettings().setZoomControlsEnabled(true);
		
		
		if (map2 != null) {
			map2.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
				
				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}
				
				@Override
				public View getInfoContents(Marker marker) {
					if (markerWithInfo==null)
						markerWithInfo = marker;
					View v = getLayoutInflater().inflate(R.layout.info_window, null);
					
					TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
					TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
					TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
					TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);
					
					LatLng ll = markerWithInfo.getPosition();
					Geocoder gc = new Geocoder(AlertZone.this);
					List<Address> list = null;
					
					
					try {
						list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					Address add = list.get(0);
					locality = add.getLocality();
					
					tvLocality.setText(locality);
					tvLat.setText("Latitude: " + String.format("%.6f", ll.latitude));
					tvLng.setText("Longitude: " + String.format("%.6f", ll.longitude));
					//tvLocality.setText(markerWithInfo.getSnippet());
					

				
					return v;
					}
					
			});

	}

		map2.setOnMarkerClickListener(new OnMarkerClickListener() {

			
			@Override
			public boolean onMarkerClick(Marker markerSel) {
				
				// TODO Auto-generated method stub
				Log.d("error1 ","markListenr");
				if (markerWithInfo!=null){
					boolean infoShownTemp = infoShown;
					if (infoShown && markerWithInfo.equals(markerSel)){
						markerSel.hideInfoWindow();
						infoShown = false;
					}
					if (!infoShownTemp || !markerWithInfo.equals(markerSel)){
						markerWithInfo = markerSel;
						markerSel.showInfoWindow();
						infoShown = true;
					}
				}else{
					markerWithInfo = markerSel;
					markerSel.showInfoWindow();
					infoShown = true;
				}
				
				for (int i = 0; i<counterPoly;i++){
					Log.d("error2 ","markListenr");
					if (markers.get(i).size()!=0 && markerSel.equals(markers.get(i).get(0))){
						polyBeingShown = -1;
						Log.d("mklisten-1 ","markListenr");
						markerSel.hideInfoWindow();
						List<List<Marker>> markersTemp = new ArrayList<List<Marker>>(); 
						ArrayList<Polygon> polyShapeTemp = new ArrayList<Polygon>();
						ArrayList<String> polyNamesTemp = new ArrayList<String>();
						//ArrayList<Marker> markerTemp = new ArrayList<Marker>();
						//ArrayList<Circle> circShapeTemp = new ArrayList<Circle>();
						ArrayList<Integer> polyTypeTemp = new ArrayList<Integer>();
						
						Log.d("mklisten-2 ","markListenr");
						for (int k=0; k<counterPoly; k++){
							Log.d("mklisten-3 ","markListenr");
							Log.d("counterPoly= ",String.valueOf(counterPoly));
							if (k!=i){
								Log.d("k= ",String.valueOf(k));
								markersTemp.add(markers.get(k));
								polyShapeTemp.add(polyShape.get(k));
								Log.d("selector ","1");
								polyNamesTemp.add(polyNames.get(k));
								Log.d("selector ","1");
								polyTypeTemp.add(polyType.get(k));
							}
						}
						
						Log.d("error3 ","markListenr");
						for (Marker mark : markers.get(i)) {
							Log.d("error4 ","markListenr");
							mark.remove();
						}
						Log.d("error5 ","markListenr");
						Log.d("i ",String.valueOf(i));
						Log.d("shape.size(): ",String.valueOf(polyShape.size()));
						polyShape.get(i).remove();
						
						Log.d("error5.1 ","markListenr");
						for (int j = i;j<counterPoly-1;j++) {
							Log.d("error6 ","markListenr");
							markers.set(j, markers.get(j+1));
							Log.d("error7 ","markListenr");
							polyShape.set(j,polyShape.get(j+1));
							polyNames.set(j,polyNames.get(j+1));
							Log.d("error8 ","markListenr");
							polyType.set(j,polyType.get(j+1));
						}
						
						Log.d("error10","markListenr");
						polyShape.set(counterPoly-1, null);						
						polyNames.remove(counterPoly-1);
						//polyNames.set(counterPoly-1, null);
						Log.d("error11","markListenr");
						polyType.set(counterPoly-1, null);
						
						
						Log.d("error9","markListenr");
						//markers.get(counterPoly-1).clear();
						markers = markersTemp;
						polyShape = polyShapeTemp;
						polyNames = polyNamesTemp;
						polyType = polyTypeTemp;
						counterPoly--;
						Log.d("counterPoly: ",String.valueOf(counterPoly));
						Log.d("markers.size(): ",String.valueOf(markers.size()));
						Log.d("polyShape.size(): ",String.valueOf(polyShape.size()));
						Log.d("error ","counterPoly==0");
						if (counterPoly ==0){
							markers = new ArrayList<List<Marker>>();
							polyShape = new ArrayList<Polygon>();
							polyNames = new ArrayList<String>();
							polyType = new ArrayList<Integer>();
						}
							
						Log.d("counterPoly: ",String.valueOf(counterPoly));
						Log.d("markers.size(): ",String.valueOf(markers.size()));
						Log.d("polyShape.size(): ",String.valueOf(polyShape.size()));
						Log.d("errorend ","markListenr");
						break;
					}
				}
				for (int i = 0; i<counterCirc;i++){
					Log.d("error2 ","markListenr");
					if (marker.size()!=0 && markerSel.equals(marker.get(i))){
						infoShown = false;
						markerWithInfo.hideInfoWindow();
						ArrayList<Marker> markerTemp = new ArrayList<Marker>();
						ArrayList<Circle> circShapeTemp = new ArrayList<Circle>();
						ArrayList<Integer> circTypeTemp = new ArrayList<Integer>();
						//ArrayList<Marker> markerTemp = new ArrayList<Marker>();
						//ArrayList<Circle> circShapeTemp = new ArrayList<Circle>();
						for (int k=0; k<counterCirc; k++){
							if (k!=i){
								markerTemp.add(marker.get(k));
								circShapeTemp.add(circShape.get(k));
								circTypeTemp.add(circType.get(k));
							}
						}
						
						Log.d("error3 ","markListenr");
						marker.get(i).remove();
						
						Log.d("error5 ","markListenr");
						Log.d("i ",String.valueOf(i));
						Log.d("circShape.size(): ",String.valueOf(circShape.size()));
						circShape.get(i).remove();
						Log.d("error5.1 ","markListenr");
						for (int j = i; j<counterCirc-1; j++) {
							Log.d("error6 ","markListenr");
							marker.set(j, marker.get(j+1));
							Log.d("error7 ","markListenr");
							circShape.set(j,circShape.get(j+1));
							circType.set(j,circType.get(j+1));
						}
						
						Log.d("error10","markListenr");
						circShape.set(counterCirc-1, null);
						Log.d("error9","markListenr");
						marker = markerTemp;
						circShape = circShapeTemp;
						circType = circTypeTemp;
						counterCirc--;
						Log.d("counterCirc: ",String.valueOf(counterCirc));
						Log.d("marker.size(): ",String.valueOf(marker.size()));
						Log.d("circShape.size(): ",String.valueOf(circShape.size()));
						Log.d("error ","counterPoly==0");
						if (counterCirc ==0){
							marker = new ArrayList<Marker>();
							circShape = new ArrayList<Circle>();
							circTypeTemp =  new ArrayList<Integer>();
							//polyType = new ArrayList<Integer>();
						}
							
						Log.d("counterCirc: ",String.valueOf(counterCirc));
						Log.d("marker.size(): ",String.valueOf(marker.size()));
						Log.d("circShape.size(): ",String.valueOf(circShape.size()));
						Log.d("errorend ","markListenr");
						break;
					}
				}
				
				/*AlertDialog.Builder builder = new AlertDialog.Builder(null);
				builder.setTitle("What do you want to do?")
						.setCancelable(false)
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								})

						.setPositiveButton("Delete the polygon or circle",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if (marker.equals(markers.get(0))){
											marker.remove();
										}
									}
								});
				AlertDialog alert = builder.create();
				alert.show();*/
				return true;
			}
			
		});
		//addListener(new markers.get(i).onClickHandler({
		  //  infowindow.open(map2, marker);
		  // }));


		map2.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng ll) {
				if (!infoShown){
					Geocoder gc = new Geocoder(AlertZone.this);
					List<Address> list = null;
	
					try {
						list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
	
					Address add = list.get(0);
					locality = add.getLocality();
					AlertZone.this.setMarker(ll.latitude, ll.longitude);
				}else{
					markerWithInfo.hideInfoWindow();
					infoShown = false;
				}
					
			}

		});
		
		map2.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker markerDragged) {
				// TODO Auto-generated method stub
				onDrag = true;
				circleDrag = false;
				for (int i = 0 ; i<counterPoly;i++){
					for (int j = 0 ; j < polyType.get(i) ;j++){
						if (markers.get(i).get(j).equals(markerDragged)){
							markerOnDrag = markerDragged;
							dragIndex = i;
							break;
						}
					}
				}
				
				if (dragIndex == -1){
					for (int i = 0 ; i<counterCirc;i++){
						if (marker.get(i).equals(markerDragged)){
							markerOnDrag = markerDragged;
							dragIndex = i;
							dragRad = circType.get(i);
							circleDrag = true;
							break;
						}
					}
				}
			}
			
			@Override
			public void onMarkerDragEnd(Marker marker) {
				// TODO Auto-generated method stub

				if (infoShown && markerWithInfo.equals(marker)){
					marker.hideInfoWindow();
					marker.showInfoWindow();
				}
				dragIndex = -1;
				onDrag = false;				
			}
			
			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
				
				if (circleDrag == false){
					PolygonOptions options = null;
					polyShape.get(dragIndex).remove();
					options = new PolygonOptions()
					.fillColor(0x330000FF)
					.strokeWidth(3)
					.strokeColor(Color.BLUE);
					Log.d("errDrag","ok3");
					for (int i = 0; i < polyType.get(dragIndex); i++) {
						options.add(markers.get(dragIndex).get(i).getPosition());
					}
					Log.d("errDrag","ok5");
					polyShape.set(dragIndex,map2.addPolygon(options));
				}else{
					CircleOptions options = null;
					circShape.get(dragIndex).remove();
					options = new CircleOptions()
					.center(marker.get(dragIndex).getPosition())
					.radius(dragRad)
					.fillColor(0x66ff0000)
					.strokeWidth(3)
					.strokeColor(Color.CYAN);
					circShape.set(dragIndex,map2.addCircle(options));

				}
				
				onDrag = true;
				
			}
		});
		
		//map.addMarker(new MarkerOptions().position(LOCATION_LA).title("Find me here! \n Suleyman-Keyvan-Ali").draggable(false));
		eT2 = (EditText) findViewById(R.id.editText2);
		eT3 = (EditText) findViewById(R.id.editText3);
		
		
		
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
		Geocoder gc = new Geocoder(AlertZone.this);
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
			polyNames = new ArrayList<String>();
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
					drawPolygon();
					polyNames();
					//Log.d("3-polyName is:",polyName);
				}
				Log.d("error6 ","set");
			
			
		} else{
			if (markers.size() != 0){
				removeEverything();
			}
			
			if (marker.size()!=0 && counterCirc!=0 && marker.get(counterCirc-1)!= null){
				removeEverything();
			}
			//previously active: options.icon(BitmapDescriptorFactory.fromResource(R.drawable.but_close)).anchor(0.5f, 0.5f).title(locality);
			//previously active: marker.add(map2.addMarker(options));
			//previously active: drawCircle(marker.get(counterCirc).getPosition());
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
	
/*	private void polyNames(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Name The Polygon")
				.setCancelable(false)
				.setNegativeButton("Cancel.",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								//dialog.cancel();
								Toast.makeText(AlertZone.this,
										"Can't be Cancelled! Name the polygon.",
										Toast.LENGTH_SHORT).show();
							}
						})

				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								Log.d("dia-1", "Hello");
								saveShapes();
								Log.d("dia-2", "Hello");
								Toast.makeText(AlertZone.this,
										"Named The Polygon",
										Toast.LENGTH_SHORT).show();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}*/
	
	//public Dialog onCreateDialog(Bundle savedInstanceState) {
	private void polyNames(){
		Log.d("polyNames: ", "1");
	    Log.d("polyNames: ", "2");
	    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	    alertDialog.setTitle("Name The Polygon");
	    alertDialog.setMessage("Name");
	    //LayoutInflater inflater = this.getLayoutInflater();
	    final EditText input = new EditText(this);
	    input.requestFocus();
	    /*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		*/
		//InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		
		input.requestFocus();
		input.setHint("poly name");
	    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	        LinearLayout.LayoutParams.MATCH_PARENT,
	        LinearLayout.LayoutParams.MATCH_PARENT);
	    input.setLayoutParams(lp);
	    Log.d("polyNames: ", "3");
	    input.setInputType(InputType.TYPE_CLASS_TEXT);
	    alertDialog.setView(input);
	    Log.d("polyNames: ", "4");
	    alertDialog.setIcon(R.drawable.pol);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    //builder.setView(inflater.inflate(R.layout.dialog_polyNames_alert_zone, null))
	    // Add action buttons
	    
	    Log.d("polyNames: ", "5");
	    alertDialog
	    .setCancelable(false)
	           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   	Log.d("inside alert:", "1");
	            	   	Log.d("inside alert:", "2");
						//name = (EditText) findViewById(R.id.pName);
						polyName = input.getText().toString();
						//saveShapes();
						//Log.d("dia-3",polyName);
						realpolyNames();
						/*
						Toast.makeText(AlertZone.this,
								"Named The Polygon: "+polyName,
								Toast.LENGTH_SHORT).show();*/
	               }
	           });
	    Log.d("polyNames: ", "6");
	    //Log.d("polyName:",polyName);
	    AlertDialog alert = alertDialog.create();
		alert.show();
		//Log.d("1-polyName is:",polyName);
		
		
		Log.d("polyNames Size:",String.valueOf(polyNames.size()));
		//Log.d("polyName is:",polyName);
		/*Toast.makeText(AlertZone.this,
				"hello:"+ polyNames.get(0),
				Toast.LENGTH_SHORT).show();*/
	    
	}
	
	private void realpolyNames() {
		Log.d("2-polyName is:",polyName);
		//polyNamesTrash.add(polyName);
		Log.d("3-polyName is:",polyName);

		boolean nameExist = false;
		if (polyName.trim().length()==0){
			nameExist = true;
			polyNames();
		}
		
		if (polyNames.size()!= counterPoly){
			for (int i = 0; i < polyNames.size(); i++) {
				Log.d("7-polyName is:",polyName);
				if (polyNames.get(i).equals(polyName)){
					nameExist = true;
					polyNames();
				}
			}
			if (nameExist == false)
				polyNames.add(polyName);
		}
		/*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		imm.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
		hideSoftKeyboard(getWindow().getDecorView().getRootView());*/
		
	}
	
	
	
	
	

	private void drawCircle(final LatLng ll,int time, final int colorType) {
		// TODO Auto-generated method stub
		if (colorType==0){  //previously if(!onDrag){
			//circType.add(circRad);
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {			
				public void run() {
			     // Actions to do after 0.5 seconds
					if (myBlinkingCirc!=null){
						myBlinkingCirc.remove();
					}
					CircleOptions options = null;
					options = new CircleOptions()
					.center(ll)
					.radius(circRad)
					.fillColor(0x66ff0000)
					.strokeColor(Color.CYAN)
					.strokeWidth(2);
					myBlinkingCirc = map.addCircle(options);
			    }}, time+500);
			//circShape.add(map2.addCircle(options));
			//counterCirc ++;
		} else {  //previously if(!onDrag){
			//circType.add(circRad);
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {			
				public void run() {
			     // Actions to do after 0.5 seconds
					if (myBlinkingCirc!=null){
						myBlinkingCirc.remove();
					}
					/*CircleOptions options = null;
					options = new CircleOptions()
					.center(ll)
					.radius(circRad)
					.fillColor(0xd2b48c00)
					.strokeColor(Color.GRAY)
					.strokeWidth(3);
					myBlinkingCirc = map.addCircle(options);*/
			    }}, time+500);
			//circShape.add(map2.addCircle(options));
			//counterCirc ++;
		}/*previously active: else{
				circShape = new ArrayList<Circle>();
				for (int j = 0; j < counterCirc; j++) {
					CircleOptions options = null;
					options = new CircleOptions()
					.center(marker.get(j).getPosition())
					.radius(circRad)
					.fillColor(0x66ff0000)
					.strokeWidth(3)
					.strokeColor(Color.CYAN);
					circShape.add(map2.addCircle(options));
				}
		}*/	
		if (time<3000 && !myTask.isCancelled() && colorType == 0)
			drawCircle(ll,time+500,1);
		else if (time<3000 && !myTask.isCancelled() && colorType == 1){
			drawCircle(ll,time+500,0);
		}
		//onDrag = false;
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
					polyNames = new ArrayList<String>();
				}
				Log.d("error5 ","rmveth");
				if (circShape.size()!=0 && circShape.get(0) != null) {
					Log.d("error6 ","rmveth");
					
					for (int i = counterCirc; i >0 ; i--) {
						marker.get(i-1).remove();
						circShape.get(i-1).remove();
					}
					marker.clear();
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
					polyNames = new ArrayList<String>();
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
					polyNames = new ArrayList<String>();
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
	
	
	
	public void holdChange() {
		Log.d("hold","is changing");
		//Button but = (Button) findViewById(R.id.hold);
		if(holdChange){
			Log.d("hold","is off");
			//but.setText("Hold is Off");
			//but.setBackgroundColor(Color.GRAY);
			holdChange = false;
			removeEverything();
			counterPoly = 0;
			counterCirc = 0;
			polyType = new ArrayList<Integer>();
			circType = new ArrayList<Integer>();
		}else{
			Log.d("hold","is On");
			//but.setText("Hold is On");
			//but.setBackgroundColor(Color.GREEN);
			counterPoly = 0;
			//counterCirc = 0;
			polyType = new ArrayList<Integer>();
			//circType = new ArrayList<Integer>();
			Log.d("polyShape.size(): ",String.valueOf(polyShape.size()));
			if (polyShape.size()!=0 && !cirCon){
				Log.d("some-er","why?");
				polyType.add(poly_num);
				counterPoly ++;
			}
			if (polyShape.size()!=0 && cirCon){
				polyType.add(markers.get(0).size());
				counterPoly ++;
			}
				
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
    
    
	
	
	public void onClick_Sat() {
//		CameraUpdate update = CameraUpdateFactory.newLatLng(LOCATION_BURNABY);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map2.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	}
	public void onClick_Norm() {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map2.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}
	public void onClick_Ter() {
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		map2.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		/*This is not necessary it just adds a marker to the center of map (close to africa)
		 * map.addMarker(new MarkerOptions()
        .position(new LatLng(0, 0))
        .title("Marker"));*/
		
	}
	
	private void gotoLocation(double lat, double lng,
			float zoom) {
		eT2.setText("Lat: " + String.format("%.6f",lat));
    	eT3.setText("Long: " + String.format("%.6f",lng));
		if (startView){
			LatLng ll = new LatLng(lat, lng);
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
			map2.animateCamera(update);
			map.moveCamera(update);
			startView = false;
		}
	}
	
/*	public void geoLocate(View v) throws IOException {
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
	}*/
	
	
	
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
        	String URL = "http://54.187.253.246/selectuser/updateCurrentCoordinateGet_postgre.php/?lat="
					+ i
					+ "&long="
					+ j
					+ "&userID="
					+ userID; 
        	myCurrentLat = i;
        	myCurrentLong = j;
			final String Link = URL.replace(" ", "%20");
			Log.d("Link", Link);
			myTask = new LongLatInfo();
			if (!myTask.isCancelled())
				myTask.execute(Link);

        	eT2.setText("Lat: " + String.format("%.6f",i));
        	eT3.setText("Long: " + String.format("%.6f",j));
        	
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
    
    private class findDistFriendsAsync extends AsyncTask<String, Integer, ArrayList<ArrayList<String>>>{// X,Y,Z
    	
		@Override
		protected ArrayList<ArrayList<String>> doInBackground(String... arg0) { // Z,X
			// TODO Auto-generated method stub
			
			ArrayList<ArrayList<String>> distFriends = new ArrayList<ArrayList<String>>();
			ArrayList<String> oneRow;
    		try {
    			//distance can not directly be resolved to integer because it includes "m" or "Km" characters we need to get rid of that part.
    			int d=arg0[0].length();
    			String n=null;
    			if (arg0[0].contains("Km"))
    			{
    				n=arg0[0].substring(0, d-2);
    				n+="000";
    			}
    			else if (arg0[0].contains("m"))
    			{
    				n=arg0[0].substring(0, d-1);
    			}
    			arg0[0]=n;
    			
    			String strURL="http://54.187.253.246/selectuser/distance_alertZone.php?userID="+userID+"&distance="+arg0[0];
    			Log.d("Link-f",strURL);
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());	
				HttpClient client = new DefaultHttpClient();				
				/*HttpPost post = new HttpPost(url.toString());				
				HttpParams p=new BasicHttpParams();
				p.setParameter("userID", userID);
				post.setParams(p);*/
				
				HttpResponse r = client.execute(get);
				
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				Log.d("Error", "test0");

				
				
				if (status == 200) {
					HttpEntity e = r.getEntity();				
					data = EntityUtils.toString(e);
					
					if(data.contains("An error occurred"))
					{
						oneRow = new ArrayList<String>();
						oneRow.add(arg0[0]);
						distFriends.add(oneRow);					
						return distFriends;
					}
					else
					{
						explrObject = new JSONObject(data);
	
						int numberOfFriendsReturned = Integer.valueOf(explrObject.getString("numberOfFriendsReturned"));
						
						for (int i = 1; i <= numberOfFriendsReturned; i++) {
							oneRow = new ArrayList<String>();
							oneRow.add(explrObject.getString("name"+String.valueOf(i)));
							oneRow.add(explrObject.getString("lat"+String.valueOf(i)));
							oneRow.add(explrObject.getString("long"+String.valueOf(i)));
							oneRow.add(explrObject.getString("friendUpdateTime"+String.valueOf(i)));
							
							distFriends.add(oneRow);
						}
						
					}
					
					
				} else {
					Toast.makeText(AlertZone.this, "Error Occured",
							Toast.LENGTH_SHORT).show();
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
    		oneRow = new ArrayList<String>();
			oneRow.add(arg0[0]);
			distFriends.add(oneRow);					
			return distFriends;
		}
		
		
		protected void onPostExecute(ArrayList<ArrayList<String>> result) { // Z
			if(result.size()!=1)
			{
				if (Double.valueOf(result.get(result.size()-1).get(0))<4000)
					Toast.makeText(AlertZone.this,
							"There are \""+String.valueOf(result.size()-1)+"\" friends available within "+result.get(result.size()-1).get(0)+"m distance.",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(AlertZone.this,
							"There are \""+String.valueOf(result.size()-1)+"\" friends available within "+String.valueOf(Integer.valueOf(result.get(result.size()-1).get(0))/1000)+"Km distance.",
							Toast.LENGTH_SHORT).show();
				for (int i = 0; i < result.size()-1; i++) {
					
					double lat = Double.valueOf(result.get(i).get(1));
					double lng = Double.valueOf(result.get(i).get(2));
					Log.d("Error-ins-f", "3");					
					String lastTime = result.get(i).get(3);
					Log.d("Error-ins-f", "4");
					LatLng ll = new LatLng(lat, lng);
					String nameOfDistFriend = result.get(i).get(0);
					
					MarkerOptions options = new MarkerOptions()
					.title(nameOfDistFriend)
					.anchor(0.5f, 0.5f)
					.snippet(lastTime)
					.position(ll)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.friendthumb)).anchor(0.5f, 0.5f).draggable(false);
				
					distFriendsMarkers.add(map.addMarker(options));
				}
			}
			else
			{
				Toast.makeText(AlertZone.this,
						"Ooops, There is no friends available within the distance.",Toast.LENGTH_SHORT).show();			
			}
			
			drawCircle(new LatLng(myCurrentLat, myCurrentLong),0,0);//arguments: drawCircle(center,,time,color)
			
			double[] realDistance = {10,50,100,250,500,1000,2500,5000,10000,50000,100000,1000000,3000000,7000000};
	        int[] zoomLevel = {21,19,18,16,15,14,13,12,11,9,8,4,2,1};
	        for (int i = 0; i < realDistance.length; i++) {
	        	if (Double.valueOf(result.get(result.size()-1).get(0))==realDistance[i]){
	        		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(myCurrentLat, myCurrentLong),
	    	                zoomLevel[i]);
	    	        map.animateCamera(update);
	    			map2.animateCamera(update);
	        	}
			}
	        
	        
		}

		protected void onCancelled (ArrayList<ArrayList<String>> result){
			super.onCancelled(result);
		}
			
    	
    }
    
    
    private class nameOfAllFriendsAsync extends AsyncTask<String, Integer, Integer>{ // X,Y,Z
    	
    	protected Integer doInBackground(String... params) { // Z,X
    		
    		try {
    			String strURL="http://54.187.253.246/selectuser/friendsList_alertZone.php?userID="+userID;
    			
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());	
				HttpClient client = new DefaultHttpClient();				
				/*HttpPost post = new HttpPost(url.toString());				
				HttpParams p=new BasicHttpParams();
				p.setParameter("userID", userID);
				post.setParams(p);*/
				
				HttpResponse r = client.execute(get);
				
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				Log.d("Error", "test0");
				if (status == 200) {
					Log.d("Error-ins", "test0");
					HttpEntity e = r.getEntity();
					data = EntityUtils.toString(e);
					explrObject = new JSONObject(data);
					Log.d("Error-ins", "test1");
					int numberOfFriends = Integer.valueOf(explrObject.getString("numberOfFriends"));
					
					Log.d("Error is-1", explrObject.getString("numberOfFriends"));
					
					for (int i = 1; i <= numberOfFriends; i++) {
						nameOfAllFriends.add(explrObject.getString("row"+i));
					}
					
					return numberOfFriends;
					
				} else {
					Toast.makeText(AlertZone.this, "Error Occured",
							Toast.LENGTH_SHORT).show();
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
			return 0;
		}

		protected void onPostExecute(Integer result) { // Z
			
		}

		protected void onCancelled (Integer result){
			super.onCancelled(result);
		}
    }
    
    
    
    private class findOneFriendAsync extends AsyncTask<String, Integer, String[]>{// X,Y,Z

		@Override
		protected String[] doInBackground(String... arg0) { // Z,X
			// TODO Auto-generated method stub

    		try {
    			String strURL="http://54.187.253.246/selectuser/friendsList_alertZone.php?userID="+userID+"&friendName="+arg0[0];
    			Log.d("Link-f",strURL);
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());	
				HttpClient client = new DefaultHttpClient();				
				/*HttpPost post = new HttpPost(url.toString());				
				HttpParams p=new BasicHttpParams();
				p.setParameter("userID", userID);
				post.setParams(p);*/
				
				HttpResponse r = client.execute(get);
				
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				Log.d("Error", "test0");
				if (status == 200) {
					Log.d("Error-ins-f", "1");
					HttpEntity e = r.getEntity();
					Log.d("Error-ins-f-2", e.toString());					
					data = EntityUtils.toString(e);
					Log.d("Error-ins-f-3", data.toString());
					explrObject = new JSONObject(data);
					
					
					String[] putTo = {explrObject.getString("lat"), explrObject.getString("long"), explrObject.getString("friendUpdateTime"), arg0[0]};
					
					return putTo;
					
				} else {
					return null;
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
		
		
		protected void onPostExecute(String[] result) { // Z

			if (result!=null){
				Log.d("Error-ins-f", "2");					
				double lat = Double.valueOf(result[0]);
				double lng = Double.valueOf(result[1]);
				Log.d("Error-ins-f", "3");					
				String lastTime = result[2];
				Log.d("Error-ins-f", "4");
				LatLng ll = new LatLng(lat, lng);
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				builder.include(ll);
	            builder.include(new LatLng(myCurrentLat, myCurrentLong));
	            
				LatLngBounds bounds = builder.build();
		        int padding = 25; // offset from edges of the map
		                                            // in pixels
		        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
		                padding);
				map2.animateCamera(update);
				map.animateCamera(update);
				Geocoder gc = new Geocoder(AlertZone.this);
				List<Address> list = null;
				try {
					list = gc.getFromLocation(lat, lng, 1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				Address add = list.get(0);
				locality = add.getLocality();
				Log.d("locality-friend:", locality);					
				MarkerOptions options = new MarkerOptions()
				.title(locality)
				.anchor(0.5f, 0.5f)
				.snippet(lastTime)
				.position(ll)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.friendthumb)).anchor(0.5f, 0.5f).draggable(false);
				
				Toast.makeText(AlertZone.this, "Last Update:\n  "+ lastTime,
						Toast.LENGTH_LONG).show();
				oneFriendMarker = map.addMarker(options);
				
				blinkLight(0,options);			

			}else 
				Toast.makeText(AlertZone.this, "Sorry, couldn't locate your friend!",
						Toast.LENGTH_SHORT).show();
		}

		protected void onCancelled (String[] result){
			super.onCancelled(result);
		}
			
    	
    }
    
    
    
    
    public void blinkLight(int t, final MarkerOptions op){
    	Handler handle = new Handler();
    	handle.postDelayed(new Runnable() {			
			public void run() {
				oneFriendMarker.remove();
				op.icon(BitmapDescriptorFactory.fromResource(R.drawable.friendthumb));
				oneFriendMarker = map.addMarker(op);
		    }}, t);
    	if (t<4200)
    		blinkDark(t+300, op);
    	else
    		handle.postDelayed(new Runnable() {			
    			public void run() {
    				oneFriendMarker.remove();
    		    }}, t+300);
			
    }
    
    public void blinkDark(int t, final MarkerOptions op){
    	Handler handle = new Handler();
		handle.postDelayed(new Runnable() {			
			public void run() {
				oneFriendMarker.remove();
				op.icon(BitmapDescriptorFactory.fromResource(R.drawable.friendthumbdark));
				oneFriendMarker = map.addMarker(op);
		    }}, t);
    	if (t<4200)
    		blinkLight(t+300, op);
    	else
    		handle.postDelayed(new Runnable() {			
    			public void run() {
    				oneFriendMarker.remove();
    		    }}, t+300);

    }
    
    
    private class findKNNFriendsAsync extends AsyncTask<String, Integer, ArrayList<ArrayList<String>>>{// X,Y,Z

		@Override
		protected ArrayList<ArrayList<String>> doInBackground(String... arg0) { // Z,X
			// TODO Auto-generated method stub.

    		try {
    			String strURL="http://54.187.253.246/selectuser/kNN_alertZone.php?userID="+userID+"&kFriends="+arg0[0];
    			Log.d("Link-f",strURL);
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());	
				HttpClient client = new DefaultHttpClient();				
				/*HttpPost post = new HttpPost(url.toString());				
				HttpParams p=new BasicHttpParams();
				p.setParameter("userID", userID);
				post.setParams(p);*/
				
				HttpResponse r = client.execute(get);
				ArrayList<ArrayList<String>> knnFriends = new ArrayList<ArrayList<String>>();
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				Log.d("Error", "test0");

				if (status == 200) {
					HttpEntity e = r.getEntity();				
					data = EntityUtils.toString(e);
					if(data.contains("An error occurred"))
					{
						return null;
					}
					else
					{
						explrObject = new JSONObject(data);
	
						int numberOfFriendsReturned = Integer.valueOf(explrObject.getString("numberOfFriendsReturned"));
						ArrayList<String> oneRow;
						for (int i = 1; i <= numberOfFriendsReturned; i++) {
							oneRow = new ArrayList<String>();
							oneRow.add(explrObject.getString("name"+String.valueOf(i)));
							oneRow.add(explrObject.getString("lat"+String.valueOf(i)));
							oneRow.add(explrObject.getString("long"+String.valueOf(i)));
							oneRow.add(explrObject.getString("friendUpdateTime"+String.valueOf(i)));
							
							knnFriends.add(oneRow);
						}
						oneRow = new ArrayList<String>();
						oneRow.add(arg0[0]);
						knnFriends.add(oneRow);					
						return knnFriends;
					}
					
				} else {
					Toast.makeText(AlertZone.this, "Error Occured",
							Toast.LENGTH_SHORT).show();
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
		
		
		protected void onPostExecute(ArrayList<ArrayList<String>> result) { // Z
			
			if(result!=null)
			{
				if (Integer.valueOf(result.get(result.size()-1).get(0))>result.size())
					Toast.makeText(AlertZone.this,
							"Ooops, there are only \""+String.valueOf(result.size()-1)+"\" friends available.",
							Toast.LENGTH_SHORT).show();
				for (int i = 0; i < result.size()-1; i++) {
					
					double lat = Double.valueOf(result.get(i).get(1));
					double lng = Double.valueOf(result.get(i).get(2));
					Log.d("Error-ins-f", "3");					
					String lastTime = result.get(i).get(3);
					Log.d("Error-ins-f", "4");
					LatLng ll = new LatLng(lat, lng);
					String nameOfKFriend = result.get(i).get(0);
					
					MarkerOptions options = new MarkerOptions()
					.title(nameOfKFriend+" is "+String.valueOf(i))
					.anchor(0.5f, 0.5f)
					.snippet(lastTime)
					.position(ll)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.friendthumb)).anchor(0.5f, 0.5f).draggable(false);
				
					kNNFriendsMarkers.add(map.addMarker(options));
				}
				
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (Marker m : kNNFriendsMarkers) {
		            builder.include(m.getPosition());
		        }
				builder.include(new LatLng(myCurrentLat, myCurrentLong));
				LatLngBounds bounds = builder.build();
		        int padding = 25; // offset from edges of the map
		                                            // in pixels
		        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
		                padding);
		        map.moveCamera(update);
				map2.moveCamera(update);
	    
			}
			else
			{
				Toast.makeText(AlertZone.this,
						"Ooops, There is no friend available.",Toast.LENGTH_SHORT).show();			
			}
		}

		protected void onCancelled (ArrayList<ArrayList<String>> result){
			super.onCancelled(result);
		}
			
    	
    }
    
    
    private class shapeSend extends AsyncTask<String, Integer, String>{ // X,Y,Z
    	
    	/*protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AlertZone.this, "Sending The Zones ...", "Please wait until the submit is complete!", true, false);
        }
    	*/
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
			//progressDialog.dismiss();
			Log.d("shapeSendSync","0");
			eraseMap1();
			for (int i = 0; i < polyShape.size(); i++) {
				Log.d("shapeSendSync","1");
				
				PolygonOptions options = null;
				options = new PolygonOptions()
				.fillColor(0x330000FF)
				.strokeWidth(3)
				.strokeColor(Color.BLUE);
				//Log.d("error1polyType-size ",String.valueOf(polyType.size()));
				//options.add(polyShape.get(i).getPoints().get(j));
				for (int j = 0; j < polyType.get(i); j++) {
					options.add(polyShape.get(i).getPoints().get(j));
				}
				Log.d("shapeSendSync","1.5");
				polyShapeMap1.add(map.addPolygon(options));
			
				Log.d("shapeSendSync","2");
			}
			Log.d("shapeSendSync","3");
		}
		
		
		protected void onCancelled (String result){
			super.onCancelled(result);
		}
    }
    
    /*private class checkLinkExistence extends AsyncTask<String, Integer, Integer>{ // X,Y,Z
    	
    	protected Integer doInBackground(String... params) { // Z,X
		    URL u = null;
			try {
				u = new URL ("http://cs-server.usc.edu:1111/"+userName+"-allMarkers.txt");
			} catch (MalformedURLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			HttpURLConnection huc = null;
			try {
				huc = ( HttpURLConnection )  u.openConnection ();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} 
			try {
				huc.setRequestMethod ("GET");
			} catch (ProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}  //OR  huc.setRequestMethod ("HEAD"); 
			try {
				huc.connect () ;
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} 
			try {
				int code = huc.getResponseCode() ;
				return code;
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			return null;
    	}

		protected void onPostExecute(Integer result) { // Z

			if (result==200)
				checkLinkOk = true;
			Log.d("checkLinkOk=",String.valueOf(checkLinkOk));
			Log.d("resultOnPost=",String.valueOf(result));
		}
		
		
		protected void onCancelled (Integer result){
			super.onCancelled(result);
		}
    }*/
	

    private class polyShapeGet extends AsyncTask<String, Integer, ArrayList<PolygonOptions>>{ // X,Y,Z
    	


        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AlertZone.this, "Loading Poly and Circle Zones...", "Please wait until the retrieve is complete!", true, false);
        }
    	
    	protected ArrayList<PolygonOptions> doInBackground(String... params) { // Z,X
    		
    		ArrayList<PolygonOptions> options = new ArrayList<PolygonOptions>();
    		try {
    			String strURL="http://54.187.253.246/selectuser/get_polygons_postgre.php?userID="+userID;
    			
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());	
				HttpClient client = new DefaultHttpClient();				
				/*HttpPost post = new HttpPost(url.toString());				
				HttpParams p=new BasicHttpParams();
				p.setParameter("userID", userID);
				post.setParams(p);*/
				
				HttpResponse r = client.execute(get);
				
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
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
						polyNames.add(pol.getString("polyName"));
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
					Toast.makeText(AlertZone.this, "Error Occured",
							Toast.LENGTH_SHORT).show();
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
						Geocoder gc = new Geocoder(AlertZone.this);
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
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker)).anchor(0.5f, 0.5f).draggable(true);
						if (j==0){
							options.icon(BitmapDescriptorFactory.fromResource(R.drawable.but_close));
						}
						markers.get(i).add(map2.addMarker(options));
					}
					polyShape.add(map2.addPolygon(result.get(i)));
					polyShapeMap1.add(map.addPolygon(result.get(i)));
				}
				if (0<result.size()){
					Handler handler = new Handler();
			        
					handler.postDelayed(new Runnable() {			
						public void run() {
					     // Actions to do after 0.5 seconds
							LatLngBounds.Builder builder = new LatLngBounds.Builder();
							boolean includesPoint = false;
							for (int j = 0; j < markers.size(); j++) {
								for (Marker m : markers.get(j)) {
						            builder.include(m.getPosition());
						            includesPoint = true;
						        }
							}
							if (includesPoint){
								LatLngBounds bounds = builder.build();
						        int padding = 10; // offset from edges of the map
						                                            // in pixels
						        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
						                padding);
						        map.moveCamera(update);
								map2.moveCamera(update);
							}
					    }}, 800);
				}
				
			}
			
			progressDialog.dismiss();
		}
		
		
		protected void onCancelled (ArrayList<PolygonOptions> result){
			super.onCancelled(result);
		}
    }
    
    private class findFriendsInsideZone extends AsyncTask<String, Integer, Integer>{ // X,Y,Z
    	
    	protected Integer doInBackground(String... params) { // Z,X
    		
    		try {
    			String strURL="http://54.187.253.246/selectuser/alertZoneFriends.php?userID="+userID;
    			
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());	
				HttpClient client = new DefaultHttpClient();				
				/*HttpPost post = new HttpPost(url.toString());				
				HttpParams p=new BasicHttpParams();
				p.setParameter("userID", userID);
				post.setParams(p);*/
				
				HttpResponse r = client.execute(get);
				
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				friendsNameInsideZonesListCompare = friendsNameInsideZonesList;
				zonesNamesHaveFriendsInsideCompare = zonesNamesHaveFriendsInside;
				Log.d("Error", "test0");
				if (status == 200) {
					HttpEntity e = r.getEntity();
					data = EntityUtils.toString(e);
					explrObject = new JSONObject(data);
					
					int peopleInsideZoneNumber = Integer.valueOf(explrObject.getString("peopleInsideZoneNumber"));
					int numberOfFriends = Integer.valueOf(explrObject.getString("numberOfFriends"));
					if (justStartedActivityForNotifications){
						//I am including the notification for the person himself too. So I add 1 to number of friends
						//mnm = new ArrayList<NotificationManager>(numberOfFriends+1); 
						justStartedActivityForNotifications = false;
					}
					friendsNameInsideZonesList = new ArrayList<String>();
					friendsInsideZonesLocation = new ArrayList<LatLng>();
					friendsInsideZonesUpdateTime = new ArrayList<String>();
					zonesNamesHaveFriendsInside = new ArrayList<String>();
					for (int i = 1; i <= peopleInsideZoneNumber; i++) {
						JSONObject row = new JSONObject(explrObject.getString("row"+i));
						friendsNameInsideZonesList.add(row.getString("name"));
						LatLng loc = new LatLng(Double.valueOf(row.getString("lat")), Double.valueOf(row.getString("long")));
						friendsInsideZonesLocation.add(loc);
						friendsInsideZonesUpdateTime.add(row.getString("friendUpdateTime"));
						zonesNamesHaveFriendsInside.add(row.getString("zoneName"));
					}
					return peopleInsideZoneNumber;
					
				} else {
					Toast.makeText(AlertZone.this, "Error Occured",
							Toast.LENGTH_SHORT).show();
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
			return 0;
		}

		protected void onPostExecute(Integer result) { // Z
			
			for (int i = friendMarkers.size()-1; i>-1 ; i--) {
				friendMarkers.get(i).remove();
			}
			friendMarkers.clear();
		
			for (int i = 0; i < result; i++) {
				Geocoder gc = new Geocoder(AlertZone.this);
				List<Address> list = null;
				try {
					list = gc.getFromLocation(friendsInsideZonesLocation.get(i).latitude, friendsInsideZonesLocation.get(i).longitude, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				Address add = list.get(0);
				locality = add.getLocality();
				if (zonesSelectedToShowFriendsInside.contains(zonesNamesHaveFriendsInside.get(i))){
					if (!friendsNameInsideZonesList.get(i).equals(userName)){
						MarkerOptions options = new MarkerOptions()
						.title(friendsNameInsideZonesList.get(i))
						.snippet("Last Time Seen Here: "+friendsInsideZonesUpdateTime.get(i))
						.anchor(0.5f, 0.5f)
						.position(friendsInsideZonesLocation.get(i))
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.frface)).anchor(0.5f, 0.5f).draggable(false);
						//options.icon(BitmapDescriptorFactory.fromResource(R.drawable.sameown));
						friendMarkers.add(map.addMarker(options));
					}
					
				}
				//friendMarkers.add(map2.addMarker(options));
			}
			
			
			//in here I calculate the recent changes of friendlist, and put in recentChangesOfList
			ArrayList<String> recentChangesOfList = new ArrayList<String>();
			for (int i = 0; i < friendsNameInsideZonesList.size(); i++) {
				if (!friendsNameInsideZonesListCompare.contains(friendsNameInsideZonesList.get(i)))
					recentChangesOfList.add(friendsNameInsideZonesList.get(i));
			}
			Log.d("recentChangesOfList:", String.valueOf(recentChangesOfList.size()));
			
			//in here I calculate the changes of previous friendlistCompare, and put in recentChangesOfListCompare
			ArrayList<String> recentChangesOfListCompare = new ArrayList<String>();
			for (int i = 0; i < friendsNameInsideZonesListCompare.size(); i++) {
				if (!friendsNameInsideZonesList.contains(friendsNameInsideZonesListCompare.get(i)))
					recentChangesOfListCompare.add(friendsNameInsideZonesListCompare.get(i));
			}
			Log.d("recentChangesOfListCompare:", String.valueOf(recentChangesOfListCompare.size()));
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
	        boolean atLeastOnePoint = false;
			
			if (recentChangesOfList.size()!=0 || recentChangesOfListCompare.size()!=0 || mnm==null){
				Log.d("friendsNameInsideZonesList:", String.valueOf(friendsNameInsideZonesList.size()));
				Log.d("notif-1", "1");
				Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				/*if (mnm!=null){
					mnm.cancelAll();
					mnm = null;
				}*/

				for (int i = 0; i < recentChangesOfList.size(); i++) {
					Log.d("notif-1", "2");
					if (zonesSelectedToShowFriendsInside.contains(zonesNamesHaveFriendsInside.get(friendsNameInsideZonesList.indexOf(recentChangesOfList.get(i))))){
						Log.d("notif-1", "3");
						for (int k = 0; k < polyNames.size(); k++) {
							for (int j = 0; j < zonesNamesHaveFriendsInside.size(); j++) {
								if (polyNames.get(k).equals(zonesNamesHaveFriendsInside.get(friendsNameInsideZonesList.indexOf(recentChangesOfList.get(i)))))
									for (Marker m : markers.get(k)) {
							            builder.include(m.getPosition());
							            atLeastOnePoint = true;
							        }
							}
						}
						for (int j = 0; j < friendsNameInsideZonesList.size(); j++) {
							Log.d("notif-1", "4");
							if (friendsNameInsideZonesList.get(j).equals(recentChangesOfList.get(i))){
								
								Log.d("notif-1", "5");
								NotificationCompat.Builder mBuilder =
		    			        new NotificationCompat.Builder(activity)
		    			        .setSmallIcon(R.drawable.pol)
		    			        .setContentTitle("Friend has changed his Zone")
		    			        .setContentText(friendsNameInsideZonesList.get(j) + " has moved into zone \"" + zonesNamesHaveFriendsInside.get(j) + "\"")
		    			        .setGroup(GROUP_KEY_EMAILS)
		    			        .setNumber(++numMessages)
		    			        .setSound(soundUri);
								Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
							    r.play();
							    
							    mBuilder.setPriority(Notification.PRIORITY_HIGH);

				    			// Creates an explicit intent for an Activity in your app
				    			Intent resultIntent = new Intent();
				    			notificationBundle = new Bundle();
				    			for (int k = 0; k < polyNames.size(); k++) {
									if (polyNames.get(k).equals(zonesNamesHaveFriendsInside.get(j)))
										notificationBundle.putString(ZOOM_ZONE,String.valueOf(k));
								}
				    			notificationBundle.putString(USER_ID, userID);
				    			notificationBundle.putString(EXTRA_MESSAGE, userName);
				    			
				    			resultIntent.putExtras(notificationBundle);
				                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				    			//resultIntent.putExtra(ZOOM_ZONE, "ali");
				    			// The stack builder object will contain an artificial back stack for the
				    			// started Activity.
				    			// This ensures that navigating backward from the Activity leads out of
				    			// your application to the Home screen.
				    			TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
				    			// Adds the back stack for the Intent (but not the Intent itself)
				    			stackBuilder.addParentStack(AlertZone.class);
				    			// Adds the Intent that starts the Activity to the top of the stack
				    			stackBuilder.addNextIntent(resultIntent);
				    			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
				    			mBuilder.setContentIntent(resultPendingIntent);
				    			mnm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				    			// mId allows you to update the notification later on.
				    			mnm.notify(new Random().nextInt(10000), mBuilder.build());
				    			j = friendsNameInsideZonesList.size();
							}
						}
					}
				}
				
				Log.d("friendsNameInsideZonesListCompare:", String.valueOf(friendsNameInsideZonesListCompare.size()));
				Log.d("notif-2", "1");
				for (int i = 0; i < recentChangesOfListCompare.size(); i++) {
					Log.d("notif-2", "2");
					if (zonesSelectedToShowFriendsInside.contains(zonesNamesHaveFriendsInsideCompare.get(friendsNameInsideZonesListCompare.indexOf(recentChangesOfListCompare.get(i))))){
						Log.d("notif-2", "3");
						for (int k = 0; k < polyNames.size(); k++) {
							for (int j = 0; j < zonesNamesHaveFriendsInsideCompare.size(); j++) {
								if (polyNames.get(k).equals(zonesNamesHaveFriendsInsideCompare.get(friendsNameInsideZonesListCompare.indexOf(recentChangesOfListCompare.get(i)))))
									for (Marker m : markers.get(k)) {
							            builder.include(m.getPosition());
							            atLeastOnePoint = true;
							        }
							}
						}
						for (int j = 0; j < friendsNameInsideZonesListCompare.size(); j++) {
							Log.d("notif-2", "4");
							if (friendsNameInsideZonesListCompare.get(j).equals(recentChangesOfListCompare.get(i))){								
								Log.d("notif-2", "5");
								NotificationCompat.Builder mBuilder =
		    			        new NotificationCompat.Builder(activity)
		    			        .setSmallIcon(R.drawable.pol)
		    			        .setContentTitle("Friend has changed his Zone")
		    			        .setContentText(friendsNameInsideZonesListCompare.get(j) + " has moved out of zone \"" + zonesNamesHaveFriendsInsideCompare.get(j) + "\"")
		    			        .setNumber(++numMessages)
								.setSound(soundUri);
								Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
							    r.play();
				    			// Creates an explicit intent for an Activity in your app
							    

							    mBuilder.setPriority(Notification.PRIORITY_HIGH);

				    			Intent resultIntent = new Intent();
				    			notificationBundle = new Bundle();
				    			for (int k = 0; k < polyNames.size(); k++) {
									if (polyNames.get(k).equals(zonesNamesHaveFriendsInsideCompare.get(j)))
										notificationBundle.putString(ZOOM_ZONE,String.valueOf(k));
								}
				    			notificationBundle.putString(USER_ID, userID);
				    			notificationBundle.putString(EXTRA_MESSAGE, userName);
				    			
				    			resultIntent.putExtras(notificationBundle);
				                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				    			//resultIntent.putExtra(ZOOM_ZONE, "ali");
				    			// The stack builder object will contain an artificial back stack for the
				    			// started Activity.
				    			// This ensures that navigating backward from the Activity leads out of
				    			// your application to the Home screen.
				    			TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
				    			// Adds the back stack for the Intent (but not the Intent itself)
				    			stackBuilder.addParentStack(AlertZone.class);
				    			// Adds the Intent that starts the Activity to the top of the stack
				    			stackBuilder.addNextIntent(resultIntent);
				    			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
				    			mBuilder.setContentIntent(resultPendingIntent);
				    			mnm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				    			// mId allows you to update the notification later on.
				    			mnm.notify(new Random().nextInt(10000), mBuilder.build());
				    			j = friendsNameInsideZonesListCompare.size();
							}
						}
					}
				}
			}
			if (atLeastOnePoint){
		        LatLngBounds bounds = builder.build();
		        int padding = 25; // offset from edges of the map
		                                            // in pixels
		        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
		                padding);
		        map.animateCamera(update);
				map2.animateCamera(update);
			}
			
		}
		
		
		protected void onCancelled (Integer result){
			super.onCancelled(result);
		}
    }
    
    
    private class circShapeGet extends AsyncTask<String, Integer, ArrayList<CircleOptions>>{ // X,Y,Z
    	
    	
    	protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AlertZone.this, "Loading Poly and Circle Zones ...", "Please wait until the retrieve is complete!", true, false);
        }
    	
    	protected ArrayList<CircleOptions> doInBackground(String... params) { // Z,X
    		
    		ArrayList<CircleOptions> options = new ArrayList<CircleOptions>();
    		try {
    			String strURL="http://54.187.253.246/selectuser/get_polygons_postgre.php?userID="+userID;    			
				StringBuilder url = new StringBuilder(strURL);
				HttpGet get = new HttpGet(url.toString());				
				HttpClient client = new DefaultHttpClient();				
				HttpResponse r = client.execute(get);
				int status = r.getStatusLine().getStatusCode();
				String data = null;
				JSONObject explrObject = null;
				
				///////////////////////////////////////////////////////////
				
				/////////////////////////////////////////////////////////
				
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
					Toast.makeText(AlertZone.this, "error",
							Toast.LENGTH_SHORT).show();
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
					
					Geocoder gc = new Geocoder(AlertZone.this);
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
			mListTitles[0] = "# Total Zones: "+String.valueOf(counterPoly+counterCirc);
			progressDialog.dismiss();
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
        Log.d("notification", "is clicked");
        //Toast.makeText(getBaseContext(), "sal",Toast.LENGTH_SHORT).show();
        //Bundle bundle = getIntent().getExtras();
        if (notificationBundle != null) {
            String notificationData = notificationBundle.getString(ZOOM_ZONE);
            if(!notificationData.equals("com.example.ZOOMZONE")) {
            	userID = notificationBundle.getString(USER_ID);
    			userName = notificationBundle.getString(EXTRA_MESSAGE);
            	//Toast.makeText(this, notificationData,Toast.LENGTH_SHORT).show();
            	LatLngBounds.Builder builder = new LatLngBounds.Builder();
            	boolean includesPoint = false;
		        for (Marker m : markers.get(Integer.valueOf(notificationData))) {
		            builder.include(m.getPosition());
	            	includesPoint = true;
		        }
		        if (includesPoint){
			        LatLngBounds bounds = builder.build();
			        int padding = 100; // offset from edges of the map
			                                            // in pixels
			        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
			                padding);
			        Toast.makeText(AlertZone.this, notificationData,Toast.LENGTH_SHORT).show();
			        map.animateCamera(update);
					map2.animateCamera(update);
		        }
            	Log.d("My toast", "should show");
            	notificationBundle = null;
            	if(mnm!=null)
            		mnm.cancelAll();
            	//mnm = null;
            }
        	Log.d("Bundle", "not empty");
        }else
        	Log.d("Bundle", "is empty!");
        
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        uiHelper.onResume();
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
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800, 0, this);
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
        myHandlerForZones.removeCallbacksAndMessages(null);
        if(mnm!=null)
    		mnm.cancelAll();
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
        uiHelper.onPause();
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
		getMenuInflater().inflate(R.menu.alert_zone_3_dot_menu, menu);
		getMenuInflater().inflate(R.menu.alert_zone_search_item_actionbar, menu);
		getMenuInflater().inflate(R.menu.alert_zone_drop_down_actionbar, menu);
		
		MenuItem searchItem = menu.findItem(R.id.search_icon);
		//searchItem.collapseActionView();
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    searchView.setIconified(true); //to be opened without textform	    
	    //searchView.setQuery("", true);
	    searchView.setQueryHint("Search Your Location");
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				invalidateOptionsMenu();
				hideSoftKeyboard(getWindow().getDecorView().getRootView());
				Geocoder gc = new Geocoder(getBaseContext());
				List<Address> list = null;
				try {
					list = gc.getFromLocationName(query, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!list.isEmpty()){
					Toast.makeText(getBaseContext(), "Processing ...",Toast.LENGTH_SHORT).show();
					Address add = list.get(0);
					
					String locality = add.getLocality();
					double lat = add.getLatitude();
					double lng = add.getLongitude();
					startView = true;
					gotoLocation(lat,lng,8);
				}else{
					//et.setText("");
					Toast.makeText(getBaseContext(), "Please Enter a Valid Location to Look Up For You!",Toast.LENGTH_SHORT).show();
				}
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	    
		//menuItem.
	    // When using the support library, the setOnActionExpandListener() method is
	    // static and accepts the MenuItem object as an argument
	    MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
	        @Override
	        public boolean onMenuItemActionCollapse(MenuItem item) {
	            // Do something when collapsed
	        	SearchView m = (SearchView) MenuItemCompat.getActionView(item);
	        	m.setQuery("", false);
	        	m.clearFocus();
	        	m.setIconified(true);
	            item.collapseActionView();//to be opened without textform
	            return true;  // Return true to collapse action view
	        }

	        @Override
	        public boolean onMenuItemActionExpand(MenuItem item) {
	            // Do something when expanded
	        	SearchView m = (SearchView) MenuItemCompat.getActionView(item);
	        	m.onActionViewCollapsed();
	        	m.setIconified(false);  //to be opened with textform
	            return true;  // Return true to expand action view
	        }
	    });

		return true;
	}

	
	public void getEverything(){

		/*int linkStatus = 0;
		if (!checkLinkOk){
			try {
				linkStatus = new checkLinkExistence().execute().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		eraseMap1();
		holdChange = false;
		removeEverything();
		/*Button but = (Button) findViewById(R.id.hold);
		but.setText("Hold is On");
		but.setBackgroundColor(Color.GREEN);*/
		holdChange = true;
		
		markers = new ArrayList<List<Marker>>(); 
		polyShape = new ArrayList<Polygon>();
		polyType = new ArrayList<Integer>();
	 
		marker = new ArrayList<Marker>();
		circShape = new ArrayList<Circle>();
		
		//if (linkStatus==200 || checkLinkOk){ //404 is for not OK
				new polyShapeGet().execute();
				new circShapeGet().execute();
		//}else{
			//	Toast.makeText(AlertZone.this," Your Alert Zone Preference \r\n is Not Available! Set it Up!",Toast.LENGTH_LONG).show();
		//}
	}
	
	
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
	
	public void saveShapes() {
		boolean startIs = true;
		//first deleting all polygons from db
		String URLMarker = "http://54.187.253.246/selectuser/delete_all_polygons_postgre.php/?userID="+userID;
		String LinkMarker = URLMarker.replace(" ", "%20");
		new shapeSend().execute(LinkMarker);
		
		
		
		if (counterPoly==0){
			URLMarker = "http://54.187.253.246/selectuser/save_polygons_postgre.php/?start=" + startIs + "&user="+userName
					+ "&counterPoly=0"
					+ "&Polytype=0"
					+ "&polyCounts=0"
					+"&zoneName=0";
			LinkMarker = URLMarker.replace(" ", "%20");
			new shapeSend().execute(LinkMarker);
		}
		
		for (int i = 0; i < counterPoly; i++) {
			Log.d("send1", "Hello");
			
			URLMarker = "http://54.187.253.246/selectuser/save_polygons_postgre.php/?start=" + startIs + "&userID="+userID
					+"&counterPoly="+ i
					+"&Polytype=" + polyType.get(i)
					+"&zoneName=" + polyNames.get(i);
			
			if (startIs){
				URLMarker += "&polyCounts="+counterPoly;
				startIs = false;
			}
			for (int j = 0; j < polyType.get(i); j++) {
				URLMarker += "&point"+j+"lat="+markers.get(i).get(j).getPosition().latitude;
				URLMarker += "&point"+j+"long="+markers.get(i).get(j).getPosition().longitude;
			}
			
			Log.d("send2", "Hello");
		    LinkMarker = URLMarker.replace(" ", "%20");
		    Log.d("send3", "Hello");
		    Log.d("URLMarker", URLMarker);
			
			new shapeSend().execute(LinkMarker);
			//new shapeSend().execute(LinkPolyShapes);
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//MenuItem map3 = (MenuItem) findViewById(R.id.map3);
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		
		switch (item.getItemId()) {
		case R.id.send:
			Log.d("send0", "Hello");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Save to Server")
					.setCancelable(false)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									Toast.makeText(AlertZone.this,
											"Save Cancelled",
											Toast.LENGTH_SHORT).show();
								}
							})

					.setPositiveButton("Yes! Save them!",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Log.d("dia-1", "Hello");
									saveShapes();
									Log.d("dia-2", "Hello");
									Toast.makeText(AlertZone.this,
											"Zones are being saved ...",
											Toast.LENGTH_SHORT).show();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			
			break;
		case R.id.gets:
			getEverything();
			
			break;
		case R.id.map3:
			poly_num = 3;
			item.setChecked(true);
			cirCon = false;
			break;
		case R.id.map4:
			poly_num = 4;
			//map3.setChecked(false);
			item.setChecked(true);
			cirCon = false;
			break;
		case R.id.map5:
			poly_num = 5;
			item.setChecked(true);
			cirCon = false;
			break;
		case R.id.map6:
			poly_num = 6;
			item.setChecked(true);
			cirCon = false;
			break;
/*		case R.id.circle:
			item.setChecked(true);
			cirCon = true;
			break;
		case R.id.r5000:
			item.setChecked(true);
			circRad = 5000;
			cirCon = true;
			break;
		case R.id.r10000:
			item.setChecked(true);
			circRad = 10000;
			cirCon = true;
			break;
		case R.id.r20000:
			item.setChecked(true);
			circRad = 20000;
			cirCon = true;
			break;
		case R.id.r40000:
			item.setChecked(true);
			circRad = 40000;
			cirCon = true;
			break;*/
		
		default:
			break;
		
		
		}
		return super.onOptionsItemSelected(item);
	}
    
    public void onBackPressed() {
    	if(myTask!=null)
			myTask.cancel(true);
    	Intent intent = new Intent(AlertZone.this,PalMenu.class);
    	intent.putExtra(EXTRA_MESSAGE, userName);
    	intent.putExtra(USER_ID, userID);
		startActivity(intent);
		myHandlerForZones.removeCallbacksAndMessages(null);
		if(mnm!=null)
    		mnm.cancelAll();
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
        uiHelper.onDestroy();
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }



    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	
    	for (int i = kNNFriendsMarkers.size()-1; i >-1; i--) {
    		kNNFriendsMarkers.get(i).remove();
		}
    	kNNFriendsMarkers = new ArrayList<Marker>();
    	for (int i = distFriendsMarkers.size()-1; i >-1; i--) {
    		distFriendsMarkers.get(i).remove();
		}
    	distFriendsMarkers = new ArrayList<Marker>();
    	if (myBlinkingCirc!=null)
    		myBlinkingCirc.remove();
    	

    	File imageFile = null;
    	switch (position){
	    	case 0:
	    		//Toast.makeText(this, "You have "+String.valueOf(counterPoly)+" defined zones.",Toast.LENGTH_SHORT).show();
	    		break;
    		/*case 1:
    			Toast.makeText(this, "KS will complete it later. Not Working Now!",Toast.LENGTH_SHORT).show();
				break;*/
    		case 5:
    			final String[] knnNumberFriend = {"1","2","3","4","5","10","15","20","40","50","100"};
				
				/*
				if (friendBeingShown!=-1 && friendBeingShown<findAFriend.length){
					findAFriend[friendBeingShown] = ("\u2713"+findAFriend[polyBeingShown]);
    			}
				
				for (int i = 0; i < findAFriend.length; i++) {
					if (i!=polyBeingShown){
						findAFriend[i] = "   "+findAFriend[i];
					}
				}*/
				
				AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
    	        builder5.setTitle("Make your selection of K for NNs:");
    	        builder5.setItems(knnNumberFriend, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int kOfNN) {
    	            	//friendBeingShown = friend;
    	            	/*Toast.makeText(AlertZone.this,
								"The "+knnNumberFriend[kOfNN]+ " NNs are being shown...",
								Toast.LENGTH_SHORT).show();*/
    	            	new findKNNFriendsAsync().execute(knnNumberFriend[kOfNN]);
    	            }
    	        }).setCancelable(false)
	           .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   	Log.d("case 11", "1");
						Log.d("case 11", "2");
						
						/*Log.d("case 11",polyName);

						Toast.makeText(AlertZone.this,
								"Named The Polygon: "+polyName,
								Toast.LENGTH_SHORT).show();*/
	               }
	           });
    	        
    	        Log.d("alert-p:","3");
    	        AlertDialog alert5 = builder5.create();
    	        alert5.show();
				
				break;
			case 4:

				String[] findAFriend = new String[nameOfAllFriends.size()];
				for (int i = 0; i < nameOfAllFriends.size(); i++) {
					findAFriend[i] = nameOfAllFriends.get(i);
				}
				/*
				if (friendBeingShown!=-1 && friendBeingShown<findAFriend.length){
					findAFriend[friendBeingShown] = ("\u2713"+findAFriend[polyBeingShown]);
    			}
				
				for (int i = 0; i < findAFriend.length; i++) {
					if (i!=polyBeingShown){
						findAFriend[i] = "   "+findAFriend[i];
					}
				}*/
				
				AlertDialog.Builder builder0 = new AlertDialog.Builder(this);
    	        builder0.setTitle("Select Your Friend:");
    	        builder0.setItems(findAFriend, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int friend) {
    	            	//friendBeingShown = friend;
	            	   	Log.d("case 6-name", nameOfAllFriends.get(friend));
    	            	new findOneFriendAsync().execute(nameOfAllFriends.get(friend));	    					

    	            }
    	        }).setCancelable(false)
	           .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   	Log.d("case 11", "1");
						Log.d("case 11", "2");
						
						/*Log.d("case 11",polyName);

						Toast.makeText(AlertZone.this,
								"Named The Polygon: "+polyName,
								Toast.LENGTH_SHORT).show();*/
	               }
	           });
    	        
    	        Log.d("alert-p:","3");
    	        AlertDialog alert0 = builder0.create();
    	        alert0.show();
				//Toast.makeText(this, "KS will complete it later. Not Working Now!",Toast.LENGTH_SHORT).show();   
				break;
			case 1:
				
				String[] views = {"Normal View","Satellite View","Terrain View"};
				
				views[viewBeingShown] = ("\u2713"+views[viewBeingShown]);
    			
				for (int i = 0; i < 3; i++) {
					if (i!=viewBeingShown){
						views[i] = "   "+views[i];
					}
				}
				
				AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
    	        builder1.setTitle("Desired View:");
    	        builder1.setItems(views, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int view) {
    	            	viewBeingShown = view;
	    				if (view == 0)
	    					onClick_Norm();
	    				else if (view == 1)
	    					onClick_Sat();
	    				else 
	    					onClick_Ter();
    	            }
    	        }).setCancelable(false)
	           .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   	Log.d("case 11", "1");
						Log.d("case 11", "2");
						
						/*Log.d("case 11",polyName);

						Toast.makeText(AlertZone.this,
								"Named The Polygon: "+polyName,
								Toast.LENGTH_SHORT).show();*/
	               }
	           });
    	        
    	        Log.d("alert-p:","3");
    	        AlertDialog alert1 = builder1.create();
    	        alert1.show();
				break;
    		case 2:
    			if (counterPoly>0)
    				zoneTour(0);
    			else
    				Toast.makeText(AlertZone.this,
    						"Currently No Zone Has Been Established By the User!",
    						Toast.LENGTH_SHORT).show();
				
    			break;
    		case 3:
    			Log.d("case 11", "-1");
    			
    			String[] items = new String[counterPoly];
    			
    			for (int i = 0; i < counterPoly; i++) {
    				Log.d("case 11", "0");
    				items[i] = polyNames.get(i);
				}	
    			if (polyBeingShown!=-1 && polyBeingShown<counterPoly){
    				items[polyBeingShown] = ("\u2713"+items[polyBeingShown]);
    			}
    			
				for (int i = 0; i < counterPoly; i++) {
					if (i!=polyBeingShown){
						items[i] = "   "+items[i];
					}
				}
    				
    			
    			//polyNames.remove(counterPoly);
    			Log.d("counterPoly:",String.valueOf(counterPoly));
				
    	        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
    	        builder2.setTitle("Visit a Zone:");
    	        builder2.setItems(items, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int item) {
    	            	
    	            	polyBeingShown = item;
    	            	
	    				LatLngBounds.Builder builder = new LatLngBounds.Builder();
	    		        for (Marker m : markers.get(item)) {
	    		            builder.include(m.getPosition());
	    		        }
	    		        LatLngBounds bounds = builder.build();
	    		        int padding = 100; // offset from edges of the map
	    		                                            // in pixels
	    		        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
	    		                padding);
	    		        Toast.makeText(AlertZone.this, "Fetching the zone \"" + polyNames.get(item) +"\"", Toast.LENGTH_SHORT).show();
	    		        map.animateCamera(update);
	    				map2.animateCamera(update);
    	            }
    	        }).setCancelable(false)
	           .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   	Log.d("case 11", "1");
						Log.d("case 11", "2");
						
						/*Log.d("case 11",polyName);

						Toast.makeText(AlertZone.this,
								"Named The Polygon: "+polyName,
								Toast.LENGTH_SHORT).show();*/
	               }
	           });;
    	        
    	        Log.d("alert-p:","3");
    	        AlertDialog alert2 = builder2.create();
    	        alert2.show();
    	        Log.d("alert-p:","4");
    	        break;
    		
    		case 6:
    			
    			Log.d("checkFriends", "Yes-1");
    			for (int i = friendMarkers.size()-1; i>-1 ; i--) {
    				friendMarkers.get(i).remove();
    			}
    			friendMarkers.clear();
    			friendsNameInsideZonesList = new ArrayList<String>();
    			friendsNameInsideZonesListCompare = new ArrayList<String>();
    			friendsInsideZonesLocation = new ArrayList<LatLng>();
    			friendsInsideZonesUpdateTime = new ArrayList<String>();
    			zonesNamesHaveFriendsInside = new ArrayList<String>();
    			zonesNamesHaveFriendsInsideCompare = new ArrayList<String>();
    			zonesSelectedToShowFriendsInside = new ArrayList<String>();
    			justStartedActivityForNotifications = true;
    			myHandlerForZones.removeCallbacksAndMessages(null);
    			if (mnm!=null){
    				mnm.cancelAll();
    			}
    			
    			final String[] multiItems = new String[counterPoly];
    			//final CharSequence myList[] = { "Tea", "Coffee", "Milk" };
    			
    			final ArrayList<Integer> selList= new ArrayList<Integer>();
    			for (int i = 0; i < counterPoly; i++) {
    				Log.d("case 11", "0");
    				multiItems[i] = polyNames.get(i);
				}	
				
				AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
    	        builder3.setTitle("Select Zones To Locate Friends Inside:");
    	        builder3.setMultiChoiceItems(multiItems, null,
    	        	    new DialogInterface.OnMultiChoiceClickListener() {
    	        	    public void onClick(DialogInterface dialog, int item, boolean isChecked) {
    	        	    	Log.d("Item:",String.valueOf(item));
    	        	        if(isChecked){ // If user select a item then add it in selected items
    	        	        	Log.d("multiSel:","1");
    	        	             selList.add(item);
    	        	             Log.d("multiSel:","2");
    	        	         }else
    	        	        	 for (int i = selList.size()-1; i >-1; i--) {
    	        	        		 if (selList.get(i)==item){
    	        	        			 selList.remove(i);
    	        	        			 break;
    	        	        		 }
    	        	        	 }	
    	        	    }
    	       }).setCancelable(true)
    	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialog, int which) {
				    // TODO Auto-generated method stub
				    /*Toast.makeText(getApplicationContext(),
				      "You Have Cancelled the Dialog box", Toast.LENGTH_LONG)     
				      .show();*/
			   }})
	           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	   for (int i = 0; i < selList.size(); i++) {
	            		   	oneItem = multiItems[selList.get(i)];
	            		   	if (i==selList.size()-1)
	            		   		zoneSelection(true);
	            		   	else
	            		   		zoneSelection(false);
	            		   //Log.d("zoneNamesselected",zonesSelectedToShowFriendsInside.get(0));
	            	   }
	               }
	           });
    	        
		        AlertDialog alert3 = builder3.create();
		        alert3.show();
		        //Log.d("zoneNamesselected-1",zonesSelectedToShowFriendsInside.get(0));
		        Log.d("alert-p:","3");
				
				
				Log.d("checkFriends", "Yes-2");
				break;
    		
    		case 7:
    			circRad = 0;
    			final String[] distance = {"10m","50m","100m","250m","500m","1000m","2500m","5000m","10Km","50Km","100Km","1000Km","3000Km","15000Km"};
    			final int[] realDistance = {10,50,100,250,500,1000,2500,5000,10000,50000,100000,1000000,3000000,7000000};
				/*
				if (friendBeingShown!=-1 && friendBeingShown<findAFriend.length){
					findAFriend[friendBeingShown] = ("\u2713"+findAFriend[polyBeingShown]);
    			}
				
				for (int i = 0; i < findAFriend.length; i++) {
					if (i!=polyBeingShown){
						findAFriend[i] = "   "+findAFriend[i];
					}
				}*/
				
				AlertDialog.Builder builder7 = new AlertDialog.Builder(this);
    	        builder7.setTitle("Select the Distance:");
    	        builder7.setItems(distance, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int distSel) {
    	            	//friendBeingShown = friend;
    	            	/*Toast.makeText(AlertZone.this,
								"Friends in the distance "+distance[distSel]+ " are being searched...",
								Toast.LENGTH_SHORT).show();*/
    	            	circRad = realDistance[distSel];
    	            	if (circRad==7000000)
    	            		circRad =6000000;
    	            	new findDistFriendsAsync().execute(distance[distSel]);
    	            }
    	        }).setCancelable(false)
	           .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   	Log.d("case 11", "1");
						Log.d("case 11", "2");
						
						/*Log.d("case 11",polyName);

						Toast.makeText(AlertZone.this,
								"Named The Polygon: "+polyName,
								Toast.LENGTH_SHORT).show();*/
	               }
	           });
    	        
    	        Log.d("alert-p:","3");
    	        AlertDialog alert7 = builder7.create();
    	        alert7.show();
				
				break;
    		case 8:
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle("Post to Facebook:")
    					.setCancelable(false)
    					.setNegativeButton("Cancel",
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,
    										int id) {
    									dialog.cancel();
    									Toast.makeText(AlertZone.this,
    											"Post Cancelled",
    											Toast.LENGTH_SHORT).show();
    								}
    							})

    					.setPositiveButton("Post Your Location",
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,
    										int id) {
    									FacebookDialog.ShareDialogBuilder shareDialogBuilder = new FacebookDialog.ShareDialogBuilder(
    											activity);
    									shareDialogBuilder.setPicture("http://54.187.253.246/selectuser/fbSharePic.jpg");
    									// shareDialogBuilder.set
    									shareDialogBuilder.setName("Find Me!");// info.getString("Address"));
    									shareDialogBuilder
    											.setDescription("I am at " + locality + " right now!");
    									shareDialogBuilder
    											.setCaption("By \""+userName+ "\", user of PalHunter App!");
    									shareDialogBuilder.setLink("https://www.google.com/maps/place/@"+myCurrentLat+","+myCurrentLong+",16.75z/data=!4m2!3m1!1s0x0:0x0");

    									FacebookDialog shareDialog = shareDialogBuilder
    											.build();

    									uiHelper.trackPendingDialogCall(shareDialog
    											.present());

    								}
    							});
    			AlertDialog alert = builder.create();
    			alert.show();
    			break;
			
    		default:
				break;
    	}
        mDrawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    
    private void zoneSelection(boolean b) {
    	zonesSelectedToShowFriendsInside.add(oneItem);
    	Log.d("zoneNamesselected-1",zonesSelectedToShowFriendsInside.get(0));
    	myHandlerForZones.removeCallbacksAndMessages(null);
    	if (mnm != null)	
			mnm.cancelAll();
		numMessages = 0;
    	if (b){
    		zoneSelectionAsync(0);
    	}
    }
    
    private void zoneSelectionAsync(final int time) {
    	
		//zonesSelectedToShowFriendsInside = new ArrayList<String>();
    	//Log.d("zoneNamesselected-1",zonesSelectedToShowFriendsInside.get(0));
    	myHandlerForZones.postDelayed(new Runnable() {			
			public void run() {
		     // Actions to do after 0.5 seconds
				if (!myTask.isCancelled()){
					new findFriendsInsideZone().execute();
				}
				if (time<1200 && !myTask.isCancelled())
		    		zoneSelectionAsync(time+1);
		    }}, 1000*(time));
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {

					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
						Toast.makeText(activity.getApplicationContext(),
								"Cancelled", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {

					}

				});
	}
    
    
    public void zoneTour(final int counterWait) {
    	Handler handler = new Handler();
		handler.postDelayed(new Runnable() {			
			public void run() {
		     // Actions to do after 0.5 seconds
		
			    Log.d("side-pol", String.valueOf(counterWait));
		    	Log.d("inside-pol", String.valueOf(counterWait));
		    	
		    	LatLngBounds.Builder builder = new LatLngBounds.Builder();
		        for (Marker m : markers.get(counterWait)) {
		            builder.include(m.getPosition());
		        }
		        
				/*for (int j = 0; j < polyType.get(counterWait); j++) {
					//builder.include(polyShape.get(counterWait).getPoints());
					for (LatLng m : polyShapeMap1.get(counterWait).getPoints()) {
			            builder.include(m);
			        }
				}*/
			
		        LatLngBounds bounds = builder.build();
		        int padding = 100; // offset from edges of the map
		                                            // in pixels
		        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,
		                padding);
		        map.animateCamera(update);
				map2.animateCamera(update);
				Toast.makeText(AlertZone.this,
						"Zone name: "+polyNames.get(counterWait),
						Toast.LENGTH_SHORT).show();
		    
		    }}, 2000*(counterWait+1));
		if (counterWait < counterPoly-1) 
			zoneTour(counterWait+1);
    }

    @Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
}
