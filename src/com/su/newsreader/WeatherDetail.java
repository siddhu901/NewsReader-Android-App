package com.su.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import android.widget.EditText;

import android.widget.ImageView;

import android.widget.TextView;

public class WeatherDetail extends Activity  {
	
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	String latitude="";
    String longitude="";
    boolean isCurrent = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.weatherdetail);
	      
	      GPSTracker gpsTracker = new GPSTracker(this);

	        if (gpsTracker.canGetLocation())
	        {
	        	latitude = String.valueOf(gpsTracker.latitude);
	        	longitude = String.valueOf(gpsTracker.longitude);
	        	isCurrent = true;
	        	seeWeatherDetailsCurent(null);
	        }
	        else
	        {
	            gpsTracker.showSettingsAlert();
	        }
	}
	
	public void seeWeatherDetailsCurent(View v){
		isCurrent = true;
		seeWeatherDetails();
	}
	
	public void seeWeatherDetailsUserInput(View v){
		isCurrent = false;
		seeWeatherDetails();
	}
	
	public void seeWeatherDetails()
	{
		StrictMode.setThreadPolicy(policy); 
		EditText edittext = (EditText) findViewById(R.id.searchplace); 
		//Todays
		TextView weatherTodayTemp = (TextView) findViewById(R.id.weatherPlace);
		TextView weatherPlace = (TextView) findViewById(R.id.weatherTodayTemp);
		TextView weatherTodayDesc = (TextView) findViewById(R.id.weatherTodayDesc);
		TextView weatherTodayWind = (TextView) findViewById(R.id.weatherTodayWind);
		ImageView weatherTodayImage = (ImageView)findViewById(R.id.weatherTodayImage);
		
		//Next five days
		TextView weatherText1 = (TextView) findViewById(R.id.weatherText1);
		TextView weatherText2 = (TextView) findViewById(R.id.weatherText2);
		TextView weatherText3 = (TextView) findViewById(R.id.weatherText3);
		TextView weatherText4 = (TextView) findViewById(R.id.weatherText4);
		ImageView weatherImage1 = (ImageView)findViewById(R.id.weatherImage1);
		ImageView weatherImage2 = (ImageView)findViewById(R.id.weatherImage2);
		ImageView weatherImage3 = (ImageView)findViewById(R.id.weatherImage3);
		ImageView weatherImage4 = (ImageView)findViewById(R.id.weatherImage4);
		
		ArrayList<ArrayList<String>> allentries=new ArrayList<ArrayList<String>>();
	    ArrayList<String> entry=new ArrayList<String>();
		
		String location="";
		String observedtime="";
		String temp_c="";
		String temp_f="";
		String description="";
		String imgurl="";
		String date="";
		String dates[];
		String windmph="";
		String inputlocation="";
		if(isCurrent && latitude != null && latitude != ""){
			inputlocation = latitude + "," + longitude;
		}else{
			inputlocation = edittext.getText().toString(); 
		}
		
		String stringUrl = "http://api.worldweatheronline.com/free/v1/weather.ashx?q="+inputlocation+"&format=json&num_of_days=5&key=8k2tdtbxtxpfjmtw8suuycdt";
		JSONObject objBase = null;
		
		
		try{
				URL url = new URL(stringUrl);
				Scanner scan = new Scanner(url.openStream());
				String str = new String();
			    while (scan.hasNext())
			        str += scan.nextLine();
			    scan.close();
			    objBase = new JSONObject(str);
			    JSONObject obj = objBase.getJSONObject("data");
			    JSONArray locarr = obj.getJSONArray("request");
			    JSONArray curarr= obj.getJSONArray("current_condition");
			    
			    
			    //Storing weather description for current day
			    
			    location=locarr.getJSONObject(0).getString("query");
		    	//desc.add(location);
		    	temp_c=curarr.getJSONObject(0).getString("temp_C");
			    temp_f=curarr.getJSONObject(0).getString("temp_F");
			    observedtime=curarr.getJSONObject(0).getString("observation_time");
			    description=curarr.getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");//getString(0);
			    imgurl=curarr.getJSONObject(0).getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
			    windmph=curarr.getJSONObject(0).getString("windspeedMiles");
			    
			    entry.add(location);
			    entry.add(temp_c);
			    entry.add(temp_f);
			    entry.add(observedtime.substring(0,5));
			    entry.add(description);
			    entry.add(imgurl);
			    entry.add(windmph);
			    
			    allentries.add(entry);
			    
			    
			    weatherPlace.setText(location);
			    weatherTodayTemp.setText("Temperature: "+temp_c + " C");
			    weatherTodayDesc.setText(description);
			    weatherTodayWind.setText("Wind: "+windmph);
			    
			    weatherTodayImage.setImageBitmap(getBitmapFromURL(imgurl));
			  //To store weather descriptions for the next 4 days
			    JSONArray temparr=objBase.getJSONObject("data").getJSONArray("weather");//.getJSONObject("1");
				for(int i=0;i<4;i++)
				{
					//entry.clear();
					ArrayList<String> entry1=new ArrayList<String>();
					JSONObject obj1=temparr.getJSONObject(i+1);
				    date=obj1.getString("date");
				    temp_c=obj1.getString("tempMaxC");
					temp_f=obj1.getString("tempMinC");
				    description=obj1.getJSONArray("weatherDesc").getJSONObject(0).getString("value");
				    imgurl=obj1.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
				    windmph=obj1.getString("windspeedMiles");
				    
				    entry1.add(date.substring(5,date.length()));
				    entry1.add(temp_c);
				    entry1.add(temp_f);
				    entry1.add(imgurl);
				    entry1.add(description);
				    entry1.add(windmph);
				    
				    allentries.add(entry1);
				    
				}
				
				for(int i=0;i<4;i++)
			    {
			    	ArrayList<String> entry1=allentries.get(i+1);
			    	String imageUrl = entry1.get(3);
				    if(i == 0 ){
				    	weatherText1.setText(entry1.get(0) + "  " +entry1.get(2) + " C  " +entry1.get(4));
				    	weatherImage1.setImageBitmap(getBitmapFromURL(imageUrl));
				    }
				    if(i == 1 ){
				    	weatherText2.setText(entry1.get(0) + "  " +entry1.get(2) + " C  " +entry1.get(4));
				    	weatherImage2.setImageBitmap(getBitmapFromURL(imageUrl));
				    }
				    if(i == 2 ){
				    	weatherText3.setText(entry1.get(0) + "  " +entry1.get(2) + " C  " +entry1.get(4));
				    	weatherImage3.setImageBitmap(getBitmapFromURL(imageUrl));
				    }
				    if(i == 3 ){
				    	weatherText4.setText(entry1.get(0) + "   " +entry1.get(2) + " C  " +entry1.get(4));
				    	weatherImage4.setImageBitmap(getBitmapFromURL(imageUrl));
				    }
			    }
				
			}
			catch(Exception e){
			
				System.out.println("Exception is:"+e);
			
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
}


