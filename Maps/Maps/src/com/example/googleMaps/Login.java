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

public class Login extends Activity{

	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
	private String username = null;
	private String password = null;
	
	private EditText userET;
	private EditText passET;
	private String comeBack = null;
	public int doStart= 1; 
	public String USERPKID = null;
	@Override
	protected void onStop() {
	    super.onStart();  // Always call the superclass method first

	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        

        
        setContentView(R.layout.login);
        /*if (doStart == 2){
	        Bundle extras = null;
	        if (getIntent()!=null)
	        	if (getIntent().getExtras()!=null)
	        		extras = getIntent().getExtras();
	        TextView tv = (TextView) findViewById(R.id.iv_image);
	        *//** Getting a reference to the button
	        * in the layout activity_main.xml *//*
	        
	        if (extras != null) {
				comeBack = extras.getString("LogOut").toString();
				 
				tv.setText("Logged Out! See you soon PAL!");
				extras = null;
			}else {
				tv.setText("Login Status");
			}
        }else 
        	doStart = 2;
        */
        ImageView btnLogin = (ImageView) findViewById(R.id.login);
        ImageView btnRegister = (ImageView) findViewById(R.id.register);
        /** Defining a click event listener for the button */
        OnClickListener downloadListener = new OnClickListener() {
 
            @Override
            public void onClick(View v) {
            	switch(v.getId())
        		{
        			case R.id.login:
        			{
		                if(isNetworkAvailable()){
		                    /** Getting a reference to Edit text containing url */
		                    //EditText etUrl = (EditText) findViewById(R.id.et_url);
		                    userET = (EditText) findViewById(R.id.username);
		                    username = userET.getText().toString();
		                    passET = (EditText) findViewById(R.id.password);
		                    password = passET.getText().toString();
		                    /** Creating a new non-ui thread task */
		                    DownloadTask downloadTask = new DownloadTask();
		                   
		                    /** Starting the task created above */
		                    downloadTask.execute("http://54.187.253.246/selectuser/login_postgre.php");
		                }else{
		                    Toast.makeText(Login.this, "Network is not Available", Toast.LENGTH_SHORT).show();
		                }
        			}
        			
        		}
            }
        };
 
        /** Setting Click listener for the download button */
        btnLogin.setOnClickListener(downloadListener);
        
        OnClickListener registerListener = new OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
            	switch(v.getId())
        		{
	        		case R.id.register:
	    			{
	    				if(isNetworkAvailable()){
	    					Intent intent = new Intent(Login.this,RegisterPage.class);
	    			    	startActivity(intent);
		                }else{
		                    Toast.makeText(Login.this, "Network is not Available", Toast.LENGTH_SHORT).show();
		                }
	    				
	    				break;
	    			}
        		}
            }
        };
       btnRegister.setOnClickListener(registerListener);
       
    }
    
    
    @Override
	public boolean dispatchTouchEvent(MotionEvent event) {

	    View v = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (v instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	return ret;
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
            
            String urlParameters = "username="+username+"&password="+password;
    		
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
        	
        	TextView iView = (TextView) findViewById(R.id.iv_text);
            Log.d("7Error", "7");
            if (result.contains("denied")){
            	
                iView.setText(result);
                Toast.makeText(Login.this, "DENIED! TRY AGAIN.", Toast.LENGTH_SHORT).show();
            }
            else
            {
            	iView.setText("Success");
            	Toast.makeText(Login.this, "Welcome back! "+username, Toast.LENGTH_SHORT).show();
				Intent results = new Intent(Login.this, PalMenu.class);
				USERPKID=result.toString();
				int d=USERPKID.length();
	    		String n=USERPKID.substring(0, d-7);
	    		USERPKID=n;
				results.putExtra(USER_ID, USERPKID);
				results.putExtra(EXTRA_MESSAGE, username);
				startActivity(results);
				finish();
            }
            /** Showing a message, on completion of download process */
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
    

    
    protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    
    protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
    
    public void registerPage(View view)
    {
    	Intent intent = new Intent(Login.this,RegisterPage.class);
    	String message = "new intent from main activity";
    	intent.putExtra(EXTRA_MESSAGE, message);
    	
    	startActivity(intent);
    }  
}
