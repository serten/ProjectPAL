package com.example.googleMaps;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;




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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPage extends Activity {
	String regusername=null;
	String regpassword=null;
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
	    String message = intent.getStringExtra(Login.EXTRA_MESSAGE);

	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(message);

	    // Set the text view as the activity layout
	    setContentView(R.layout.activity_register_page);
	    Button regbutton = (Button) findViewById(R.id.regbutton);
	    
        /** Setting Click listener for the download button */
        regbutton.setOnClickListener( new OnClickListener() {
	    	 
            @Override
            public void onClick(View v) {
            	
            	
                if(regisNetworkAvailable()){
                    /** Getting a reference to Edit text containing url */
                    EditText etUrl = (EditText) findViewById(R.id.reg_url);
                    EditText e=(EditText) findViewById(R.id.regusername);
                    regusername=e.getText().toString();
                    e=(EditText) findViewById(R.id.regpassword);
                    regpassword=e.getText().toString();
                    /** Creating a new non-ui thread task */
                    RegTask downloadTask = new RegTask();
                   
                    /** Starting the task created above */
                    downloadTask.execute(etUrl.getText().toString());

                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
			}
        });
        
	}


	private boolean regisNetworkAvailable(){
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
	private String regUrl(String strUrl) throws IOException{

        String strFileContents=null;
        BufferedInputStream in=null;
    	
        try{
            URL url = new URL(strUrl);
            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            
            /** Connecting to url */
            //urlConnection.connect();
            urlConnection.setRequestMethod("POST");
            
            String urlParameters = "username="+regusername+"&password="+regpassword;
    		
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
	
	   private class RegTask extends AsyncTask<String, Integer, String>{
	        String bitmap = null;
	        @Override
	        protected String doInBackground(String... url) {
	            try{
	                bitmap = regUrl(url[0]);
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
	 
	            /** Showing a message, on completion of download process */
	        	if (result.contains("Denied"))
	        	{
	        		Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
	        		EditText e=(EditText) findViewById(R.id.regusername);
                    e.setText("");
                    e=(EditText) findViewById(R.id.regpassword);
                    e.setText("");
	        	}
	        	else
	        	{
	        		Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
		            closethis();
	        	}
	            
	        }
	    }
	   
	   public void closethis()
	   {		   
		   finish();
		   startActivity(new Intent (RegisterPage.this, Login.class));
	   }
}

