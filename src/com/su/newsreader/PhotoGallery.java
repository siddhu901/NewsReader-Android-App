

package com.su.newsreader;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoGallery extends Activity {
	
	List<String> pics = new ArrayList<String>();
	List<String> newsSource = new ArrayList<String>();
	List<String> description = new ArrayList<String>();
	
    ImageView imageView;
    TextView imgText;
    TextView sourceText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if(preferences.getBoolean("Guardian", true)){
			processGuardianApi();	
		}
		if(preferences.getBoolean("NY Times", true)){
			processNYTimesApi();
		}
        
        Gallery ga = (Gallery)findViewById(R.id.Gallery01);
        imgText = (TextView)findViewById(R.id.imageDescriptionGallery);
        sourceText = (TextView)findViewById(R.id.gallerySource);
        ga.setAdapter(new ImageAdapter(this));
        if(newsSource.size()>0){
	        imageView = (ImageView)findViewById(R.id.ImageView01);
	        imgText.setText(description.get(0));
			if(newsSource.get(0) == "NYNews"){
				sourceText.setText(Html.fromHtml("&copy; Source: New York Times"));
			}else if(newsSource.get(0) == "GNews"){
				sourceText.setText(Html.fromHtml("&copy; Source: Guardian"));
			} 
			imageView.setImageBitmap(getBitmapFromURL(pics.get(0)));
        }
        ga.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				imgText.setText(description.get(arg2));
				if(newsSource.get(arg2) == "NYNews"){
					sourceText.setText(Html.fromHtml("&copy; Source: New York Times"));
				}else if(newsSource.get(arg2) == "GNews"){
					sourceText.setText(Html.fromHtml("&copy; Source: Guardian"));
				} 
				imageView.setImageBitmap(getBitmapFromURL(pics.get(arg2)));
			}
        });
    }
	
	private void processNYTimesApi() {
		String stringUrl = "http://api.nytimes.com/svc/search/v1/article?format=json&query=politics&fields=small_image_url%2Ctitle%2Cabstract%2Curl&api-key=becca077f79afbd51b3e52b7f9bdc88e:13:69325874";
		JSONObject obj = null;
		try {
			URL url = new URL(stringUrl);
			Scanner scan = new Scanner(url.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();
		    scan.close();
			obj = new JSONObject(str);
			JSONArray arr = obj.getJSONArray("results");
			for (int i = 0; i < arr.length(); i++)
			{
				
			    String text = arr.getJSONObject(i).getString("title");
			    String image = "";
			    if(arr.getJSONObject(i).has("small_image_url")){
			    	image = arr.getJSONObject(i).getString("small_image_url");
			    }
			    try{
			    	newsSource.add("NYNews");
			    	description.add(text);
				    pics.add(image);
				}
			    catch(Exception e){
			    	continue;
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void processGuardianApi() {
		String stringUrl = "http://content.guardianapis.com/search?q=politics&show-fields=thumbnail&api-key=ggnt8jcucfkcqmqz3c6f5u5v";
		JSONObject objBase = null;
		try{
			URL url = new URL(stringUrl);
			Scanner scan = new Scanner(url.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();
		    scan.close();
		    objBase = new JSONObject(str);
		    JSONObject obj = objBase.getJSONObject("response");
		    JSONArray arr = obj.getJSONArray("results");
			for (int i = 0; i < arr.length(); i++)
			{
				String text = "";
				String image = "";
			    try{
			    	text = arr.getJSONObject(i).getString("webTitle");
				    image = "";
			    	image = arr.getJSONObject(i).getJSONObject("fields").getString("thumbnail");
			    	newsSource.add("GNews");
			    	description.add(text);
				    pics.add(image);
			    }
			    catch(Exception e){
			    	continue;
			    }
			}
		}catch(Exception e){
			Log.e("error", e.getMessage());
		}

	}

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
	
	public class ImageAdapter extends BaseAdapter {

    	private Context ctx;
    	int imageBackground;
    	
    	public ImageAdapter(Context c) {
			ctx = c;
			TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
			imageBackground = ta.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
			ta.recycle();
		}

		@Override
    	public int getCount() {
    		
    		return pics.size();
    	}

    	@Override
    	public Object getItem(int arg0) {
    		
    		return arg0;
    	}

    	@Override
    	public long getItemId(int arg0) {
    		
    		return arg0;
    	}

    	@Override
    	public View getView(int arg0, View arg1, ViewGroup arg2) {
    		ImageView iv = new ImageView(ctx);
    		iv.setImageBitmap(getBitmapFromURL(pics.get(arg0)));
    		iv.setScaleType(ImageView.ScaleType.FIT_XY);
    		iv.setLayoutParams(new Gallery.LayoutParams(150,120));
    		iv.setBackgroundResource(imageBackground);
    		return iv;
    	}

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
			Toast.makeText(PhotoGallery.this, "Congrats: Logged out Successfull", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(PhotoGallery.this, "Hey!! first login", Toast.LENGTH_LONG).show();
			
		}
	}


}
