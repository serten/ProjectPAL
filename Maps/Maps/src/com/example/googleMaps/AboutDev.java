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

public class AboutDev extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.MESSAGE";
	public final static String USER_ID = "com.example.USERID";
	String userName,userID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userName = intent.getStringExtra(Login.EXTRA_MESSAGE);
		userID = intent.getStringExtra(Login.USER_ID);
		setContentView(R.layout.about_dev);
		
	}

	public void onBackPressed() {
		Intent intent = new Intent(AboutDev.this, PalMenu.class);
		intent.putExtra(EXTRA_MESSAGE, userName);
		intent.putExtra(USER_ID, userID);
		startActivity(intent);
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

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void onClickHandler(View v) {
		switch (v.getId()) {
		case R.id.myPic:
			Intent in = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/keyvan.noury?fref=ts")); // homesetails
			startActivity(in); // Opening the addresse's website

			break;
		case R.id.myPic2:
			Intent in1 = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://www.linkedin.com/in/suleyman-erten-70525492")); // homesetails
			// is
			// result[22]
			startActivity(in1); // Opening the addresse's website

			break;
		case R.id.myPic3:
			Intent in2 = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/ali.ghahramani")); // homesetails
			// is
			// result[22]
			startActivity(in2); // Opening the addresse's website

			break;
		// put your onclick code here
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
