package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PalMenu extends Activity{

	
	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
    protected final static String LOCATION_KEY = "location-key";
    private NetworkTask networkTask;
	
	String userName,userID;

	
	//save position to DB//
	LocationManager locationManager; 
	LocationListener locationListener; 
	protected Location mCurrentLocation;
	String LAT,LONG;
	  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Intent intent = getIntent();
		userName = intent.getStringExtra(Login.EXTRA_MESSAGE);
        userID = intent.getStringExtra(Login.USER_ID);

		setContentView(R.layout.pal_menu);
        Button btnAlertZone = (Button) findViewById(R.id.alertZone);
        Button aboutDev = (Button) findViewById(R.id.aboutDev);
        
        //frindlist button
        Button listOfFriends = (Button) findViewById(R.id.listOfFriends);
        Button followMe = (Button) findViewById(R.id.followMe);
        
        TextView tVW = (TextView) findViewById(R.id.tVW);
        tVW.setTextColor(Color.RED);
        tVW.setText(userName);

        
        /** Defining a click event listener for the button */
        btnAlertZone.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
            	Button btnAlertZone = (Button) findViewById(R.id.alertZone);
            	btnAlertZone.setBackgroundColor(0xff888888);
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, AlertZone.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
                	results.putExtra(USER_ID, userID);
    				startActivity(results);
    				
                }else{
                    Toast.makeText(PalMenu.this, "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
        aboutDev.setOnClickListener(new OnClickListener() {
       	 
            @Override
            public void onClick(View v) {
            	 Button aboutDev = (Button) findViewById(R.id.aboutDev);
            	 aboutDev.setBackgroundColor(0xff888888);
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, AboutDev.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
                	results.putExtra(USER_ID, userID);
    				startActivity(results);
    				
                }else{
                    Toast.makeText(PalMenu.this, "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // goto friendlist page
        listOfFriends.setOnClickListener(new OnClickListener() {
          	 
            @Override
            public void onClick(View v) {
            	  Button listOfFriends = (Button) findViewById(R.id.listOfFriends);
            	  listOfFriends.setBackgroundColor(0xff888888);
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, ListOfFriends.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
                	results.putExtra(USER_ID, userID);

    				startActivity(results);
    				
                }else{
                    Toast.makeText(PalMenu.this, "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
        followMe.setOnClickListener(new OnClickListener() {
         	 
            @Override
            public void onClick(View v) {
            	Button followMe = (Button) findViewById(R.id.followMe);
            	followMe.setBackgroundColor(0xff888888);
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, FollowMe.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
                	results.putExtra(USER_ID, userID);
    				startActivity(results);
    				
                }else{
                    Toast.makeText(PalMenu.this, "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        //location update service start
        
        /** Setting Click listener for the download button */
        //btnAlertZone.setOnClickListener(actionToDo);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
      	    public void onLocationChanged(Location loc) {
      	      // Called when a new location is found by the network location provider.
      	    	LAT=""+loc.getLatitude();
      	    	LONG=""+loc.getLongitude();
      	    	networkTask = new NetworkTask();
                
                /** Starting the task created above */
                String url="http://54.187.253.246/selectuser/updateCurrentCoordinate_postgre.php";
                networkTask.execute(url);
      	      //makeUseOfNewLocation(location);
      	    }
      	    
      		public void onStatusChanged(String provider, int status, Bundle extras) {}

      	    public void onProviderEnabled(String provider) {}

      	    public void onProviderDisabled(String provider) {}
      	  };      
      	  
      	  startLocationSaving();
    }
   
    public void startLocationSaving()
    {
    	//Toast.makeText(getBaseContext(), "LOCATION SERVICE STARTED", Toast.LENGTH_SHORT).show();
		PalMenu.this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 0, PalMenu.this.locationListener);    	
    }
    
    public void endLocationSaving()
    {
    	 //Toast.makeText(getBaseContext(), "LOCATION SERVICE ENDED", Toast.LENGTH_SHORT).show();
		 PalMenu.this.locationManager.removeUpdates(PalMenu.this.locationListener);    	
    }

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.logOut:
				Intent intent = new Intent(PalMenu.this,Login.class);
				String message = "Come Back My PAL!";
				intent.putExtra("LogOut", message);
				startActivity(intent);
				finish();
			break;
		}
		return super.onOptionsItemSelected(item);
    }
    
    
 
    private boolean isNetworkAvailable(){
        boolean available = false;
        /** Getting the system's connectivity service */
        
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
 
        /** Getting active network interface  to get the network's status */
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
 
        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;
 
        /** Returning the status of the network */
        return available;
    }
 
    @SuppressWarnings("finally")
	private String networkConnect(String strUrl) throws IOException{

	        String strFileContents=null;
	        BufferedInputStream in=null;
	    	
	        try{
	            URL url = new URL(strUrl);
	            /** Creating an http connection to communcate with url */
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	            
	            /** Connecting to url */
	            //urlConnection.connect();
	            urlConnection.setRequestMethod("POST");
	            
	            String urlParameters = "userID="+userID+"&&lat="+LAT+"&&long="+LONG;
	    		
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

	 private class NetworkTask extends AsyncTask<String, Integer, String>{
	        String bitmap = null;
	        @Override
	        protected String doInBackground(String... url) {
	            try{
	            	
	                bitmap = networkConnect(url[0]);
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

	            
	            Log.d("7Error", "7");
	            if (result.contains("denied")){	            	
	            	Toast.makeText(PalMenu.this, "CURRENT POSITION CAN NOT UPDATE!", Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
	            	Toast.makeText(PalMenu.this, "CURRENT POSITION UPDATED", Toast.LENGTH_SHORT).show();
	            }
	            /** Showing a message, on completion of download process */
	            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	        }
	    } 

    
	@Override
    protected void onPause() {
		// TODO Auto-generated method stub
		endLocationSaving();
		if(networkTask!=null)
        	networkTask.cancel(true);
		super.onPause();
		
		
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pal_actionbar_menu, menu);
		return true;
	}
	
    
    public void onBackPressed() {
    	endLocationSaving();
		if(networkTask!=null)
        	networkTask.cancel(true);
        moveTaskToBack(true);

    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	
    @Override
    protected void onStop() {
    	endLocationSaving();
		if(networkTask!=null)
        	networkTask.cancel(true);
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
    @Override
    public void onResume() {
        super.onResume();
        startLocationSaving();
    }
    
    

    
 
}
