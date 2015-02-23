
package com.su.newsreader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.UiLifecycleHelper;
import com.su.newsreader.SignUPActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class FavNewsDetails  extends Activity{

	
	LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.favsdetails);
	      favDetailsNews();	
	}
	
	//Display the news article's full content
	public void favDetailsNews()
	{
		TextView header = (TextView) findViewById(R.id.favNewsDetailHeader);
	  	ImageView newsImage = (ImageView) findViewById(R.id.favNewsDetailImage);
	  	TextView body = (TextView) findViewById(R.id.favNewsDetailBody);
		Bundle extras = getIntent().getExtras();
		String value="";
		if (extras != null) 
		{
		    value = extras.getString("news");
		}
		
		String[] news=value.split("~");
		
		header.setText(news[0]);
		newsImage.setImageBitmap(getBitmapFromURL((news[2])));
		body.setText(Html.fromHtml(news[1]));
		TextView tv = (TextView)findViewById(R.id.favDetailsSource);
		if(news[3].equalsIgnoreCase("Guardian")){
			tv.setText(Html.fromHtml("&copy; Source: Guardian"));	
		}else{
			tv.setText(Html.fromHtml("&copy; Source: NY Times"));
		}
		
		
	}
	
	//convert String to Bitmap
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		  switch (item.getItemId()) {
		    case R.id.weather:
		    	Intent i = new Intent(this, WeatherDetail.class);
		        startActivity(i);
		        return true;
		    case R.id.favourites:
		    	Intent ifav = new Intent(this,Favourites.class);
		        startActivity(ifav);
		        return true;
		    case R.id.logout:
		    	logOut();
		    	return true;
		    case R.id.search:
		    	Intent iSearch = new Intent(this, Search.class);
		        startActivity(iSearch);
		        return true;
		    case R.id.photogallery:
		    	Intent igallery = new Intent(this, PhotoGallery.class);
		        startActivity(igallery);
		        return true;
		    default:
		    	return super.onOptionsItemSelected(item);
		  }
	}
	private void logOut()
	{
		if(MainActivity.isLogedin==true)
		{
			MainActivity.isLogedin=false;
			MainActivity.username="";
			Toast.makeText(FavNewsDetails.this, "Congrats: Logged out Successfull", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(FavNewsDetails.this, "Hey!! first login", Toast.LENGTH_LONG).show();
			
		}
	}



}