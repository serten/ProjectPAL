package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
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
	String userName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Intent intent = getIntent();
		userName = intent.getStringExtra(Login.EXTRA_MESSAGE);
        
		setContentView(R.layout.pal_menu);
        Button btnAlertZone = (Button) findViewById(R.id.alertZone);
        Button aboutDev = (Button) findViewById(R.id.aboutDev);
        
        //frindlist button
        Button listOfFriends = (Button) findViewById(R.id.listOfFriends);
        
        TextView tVW = (TextView) findViewById(R.id.tVW);
        tVW.setTextColor(Color.RED);
        tVW.setText("Welcome "+userName);
        /** Defining a click event listener for the button */
        btnAlertZone.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, AlertZone.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
    				startActivity(results);
    				onPause();
                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        btnAlertZone.setOnClickListener(new OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, AlertZone.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
    				startActivity(results);
    				onPause();
                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        aboutDev.setOnClickListener(new OnClickListener() {
       	 
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, AboutDev.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
    				startActivity(results);
    				onPause();
                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // goto friendlist page
        listOfFriends.setOnClickListener(new OnClickListener() {
          	 
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(PalMenu.this, ListOfFriends.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
    				startActivity(results);
    				onPause();
                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
 
        /** Setting Click listener for the download button */
        //btnAlertZone.setOnClickListener(actionToDo);
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
 

    
	@Override
    protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pal_actionbar_menu, menu);
		return true;
	}
	
    
    public void onBackPressed() {
        onPause();
        moveTaskToBack(true);
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	
    @Override
    protected void onStop() {
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
    }
    
 
}
