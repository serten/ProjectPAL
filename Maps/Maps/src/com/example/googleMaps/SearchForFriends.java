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

public class SearchForFriends extends ListActivity {
	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
	String userName,userID;
	EditText searchFriendInput;
	String inputString;
	//ArrayAdapter ListAdapter;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	}
	

	
	public void onBackPressed() {
		Intent intent = new Intent(SearchForFriends.this, ListOfFriends.class);
		intent.putExtra(EXTRA_MESSAGE, userName);
		intent.putExtra(USER_ID, userID);
		startActivity(intent);
		finish();
		//moveTaskToBack(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userName = intent.getStringExtra(ListOfFriends.EXTRA_MESSAGE);
        userID = intent.getStringExtra(ListOfFriends.USER_ID);	  
	    // Set the text view as the activity layout
	    setContentView(R.layout.search_for_friends);
	    searchFriendInput = (EditText) findViewById(R.id.searchFriendInput);
	    
	    Button searchFromDB = (Button) findViewById(R.id.searchFromDB);
	    searchFromDB.setOnClickListener(new OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	 	
	                    inputString= searchFriendInput.getText().toString();
	                    //Toast.makeText(getBaseContext(), inputString, Toast.LENGTH_SHORT).show();
	                    /** Creating a new non-ui thread task */
	                    NetworkTask networkTask = new NetworkTask();
	                   
	                    /** Starting the task created above */
	                    String url="http://54.187.253.246/selectuser/searchFriends_postgre.php";
	                    networkTask.execute(url);
                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
	            
	            String urlParameters = "inputString="+inputString;
	    		
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
	            	
	            	String[] parts={"NO RESULT"};
	            	createList(parts);
	            }
	            else
	            {
	            	String[] parts = result.split(" ");
	            	//Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
		    		createList(parts);
	            }
	            /** Showing a message, on completion of download process */
	            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	        }
	    } 
	 
	 public void createList(String[] parts)
		{
			
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, parts);
		    setListAdapter(adapter);
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
}
