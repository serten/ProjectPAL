package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
	String inputString;
	String deleteFriendID=null;
	String idsArray[];
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    /*Intent results = new Intent(ListOfFriends.this, PalMenu.class);
    	results.putExtra(EXTRA_MESSAGE, userName);
    	results.putExtra(USER_ID, userID);
		startActivity(results);*/

	}
	
	@Override
    protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userName = intent.getStringExtra(Login.EXTRA_MESSAGE);
        userID = intent.getStringExtra(Login.USER_ID);	  
	    // Set the text view as the activity layout
	    setContentView(R.layout.list_of_friends);
	   
	    
	    Button searchNewFriends = (Button) findViewById(R.id.serachForFriends);
	    
	    searchNewFriends.setOnClickListener(new OnClickListener() {
         	 
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                	Intent results = new Intent(ListOfFriends.this, SearchForFriends.class);
                	results.putExtra(EXTRA_MESSAGE, userName);
                	results.putExtra(USER_ID, userID);
    				startActivity(results);
    				//onPause();
    				finish();
                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
	    
	    
	    DownloadTask downloadTask = new DownloadTask();
        
        /** Starting the task created above */
        downloadTask.execute("http://54.187.253.246/selectuser/friendlist_postgre.php");
       

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
	public void createList(String[] parts)
	{
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, parts);
	    setListAdapter(adapter);
	}
	
	public void onBackPressed() {
		Intent intent = new Intent(ListOfFriends.this, PalMenu.class);
		intent.putExtra(EXTRA_MESSAGE, userName);
		intent.putExtra(USER_ID, userID);
		startActivity(intent);
		finish();
		//moveTaskToBack(true);
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
            progressDialog = ProgressDialog.show(ListOfFriends.this, "List of your friends is loading...", "Please wait until the retrieve is complete!", true, false);
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
            	createList(parts);
	    		
	    	}
	    	else
	    	{
	    		String[] parts = result.split(" ");
            	//Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            	ArrayList<String> names = new ArrayList<String>();
            	
            	for(int i = 0; i<parts.length/2;i++)
            	{
            		  names.add(parts[i]);  		
            	
            	}
            	String[] namesArray = new String[names.size()];
            	namesArray = names.toArray(namesArray);
            	
            	ArrayList<String> ids = new ArrayList<String>();
            	for(int i = parts.length/2; i<parts.length;i++)
            	{
            		  ids.add(parts[i]);  		
            	
            	}
            	idsArray = new String[ids.size()];
            	idsArray = ids.toArray(idsArray);            	
            	
            	
	    		createList(namesArray);
	    	}
   	
	    	
	    	progressDialog.dismiss();
	    	/** Showing a message, on completion of download process */
	        //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	    }
	}

	 @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get selected items
		deleteFriendID=""+idsArray[position];
		//String selectedValue = (String) getListAdapter().getItem(position);
		//Toast.makeText(this, deleteFriendID, Toast.LENGTH_SHORT).show();
		DeleteFriendTask  deleteFriendTask = new  DeleteFriendTask();
         
        /** Starting the task created above */
        String url="http://54.187.253.246/selectuser/deleteFriend_postgre.php";
        deleteFriendTask.execute(url);

	}	
	
	
	/** friendlist DELETING section **/
	@SuppressWarnings("finally")
	private String DeleteFriend(String strUrl) throws IOException{

	        String strFileContents=null;
	        BufferedInputStream in=null;
	    	
	        try{
	            URL url = new URL(strUrl);
	            /** Creating an http connection to communcate with url */
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	            
	            /** Connecting to url */
	            //urlConnection.connect();
	            urlConnection.setRequestMethod("POST");
	            
	            String urlParameters ="userID="+userID+"&inputString="+deleteFriendID;
	            					
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

	 private class DeleteFriendTask extends AsyncTask<String, Integer, String>{
	        String bitmap = null;
	        @Override
	        protected String doInBackground(String... url) {
	            try{
	            	
	                bitmap = DeleteFriend(url[0]);
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
	        	if (result.contains("Record deleted successfully"))
	            {	            	
	            		Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	            		Intent intent = new Intent(ListOfFriends.this, ListOfFriends.class);
	            		intent.putExtra(EXTRA_MESSAGE, userName);
	            		intent.putExtra(USER_ID, userID);
	            		startActivity(intent);
	            		finish();
	            }
	        	else
	        	{
	            	Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	        	}	
	            /** Showing a message, on completion of download process */
	            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	        }
	    }
}
