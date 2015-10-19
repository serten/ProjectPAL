package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SearchForFriends extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
	String userName,userID;
	//ArrayAdapter ListAdapter;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    Intent results = new Intent(SearchForFriends.this, ListOfFriends.class);
    	results.putExtra(EXTRA_MESSAGE, userName);
    	results.putExtra(USER_ID, userID);
		startActivity(results);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userName = intent.getStringExtra(ListOfFriends.EXTRA_MESSAGE);
        userID = intent.getStringExtra(ListOfFriends.USER_ID);	  
	    // Set the text view as the activity layout
	    setContentView(R.layout.search_for_friends);	 
       

	}	

}
