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



public class ListOfFriends extends ListActivity{
	
	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
	String userName,userID;
	String[] items;
	ArrayAdapter ListAdapter;
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    Intent results = new Intent(ListOfFriends.this, PalMenu.class);
    	results.putExtra(EXTRA_MESSAGE, userName);
    	results.putExtra(USER_ID, userID);
		startActivity(results);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userName = intent.getStringExtra(Login.EXTRA_MESSAGE);
        userID = intent.getStringExtra(Login.USER_ID);
	    

	  
	    // Set the text view as the activity layout
	    setContentView(R.layout.list_of_friends);
	   
	    
	    
	    DownloadTask downloadTask = new DownloadTask();
        
        /** Starting the task created above */
        downloadTask.execute("http://54.187.253.246/selectuser/friendlist_postgre.php");
       

	}	
	
	public void createList(String[] parts)
	{
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, parts);
	    setListAdapter(adapter);
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
	    		
	    		
	    	}
	    	else
	    	{
	    		String[] parts = result.split(" ");
	    		createList(parts);
	    	}
	    	
	    	
	    	
	    	/** Showing a message, on completion of download process */
	        //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	    }
	}
}
