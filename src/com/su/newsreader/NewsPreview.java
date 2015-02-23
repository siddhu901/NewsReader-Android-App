

package com.su.newsreader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class NewsPreview extends ListActivity{
	List<String> listOfNews = new ArrayList<String>();
	List<String> listOfImages = new ArrayList<String>();
	List<String> listOfIds = new ArrayList<String>();
	List<String> newsType = new ArrayList<String>();
	List<String> urlToShare = new ArrayList<String>();
	//List<String> newsDetailNYTimes = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		 //setContentView(R.layout.newspreview);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		if(preferences.getBoolean("Guardian", true)){
			populateGuardianNews();	
		}
		if(preferences.getBoolean("NY Times", true)){
			if(MainActivity.SearchText == ""){
				populateNYTimesNews();	
			}else{
				populateNYTimesSearchNews();	
			}
		}
		
		
		
		ListAdapter ad = new NewsPreviewPopulator(this, listOfNews, listOfImages, newsType);
		setListAdapter(ad);
	}
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		MainActivity.newsId = newsType.get(position)+ "|" + listOfIds.get(position);
		MainActivity.link = urlToShare.get(position);
		Intent i = new Intent(this, NewsDetails.class);
        startActivity(i);
	}
	

	private void populateGuardianNews() {
		String stringUrl = "";
		if(MainActivity.SearchText == ""){
			stringUrl = "http://content.guardianapis.com/search?section=world&show-fields=thumbnail&show-redistributable-only=body&api-key=ggnt8jcucfkcqmqz3c6f5u5v";
		}else{
			stringUrl = "http://content.guardianapis.com/search?q="+MainActivity.SearchText+"&show-fields=thumbnail&show-redistributable-only=body&api-key=ggnt8jcucfkcqmqz3c6f5u5v";
		}
		
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
				
				String id = "";
				String body = "";
				String image = "";
			    try{
			    	id = arr.getJSONObject(i).getString("id");
					body = arr.getJSONObject(i).getString("webTitle");
				    image = "";
			    	image = arr.getJSONObject(i).getJSONObject("fields").getString("thumbnail");
			    	urlToShare.add(arr.getJSONObject(i).getString("webUrl"));
			    	newsType.add("GNews");
			    	listOfIds.add(id);
				    listOfNews.add(body);
				    listOfImages.add(image);
			    }
			    catch(Exception e){
			    	continue;
			    }
			}
		}catch(Exception e){
			Log.e("error", e.getMessage());
		}
	}

	private void populateNYTimesNews() {
		String stringUrl = "http://api.nytimes.com/svc/mostpopular/v2/mostemailed/world/1.json?api-key=dc5db892d8a3463a6d6e05e41be9133a:13:69325874";
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
				
			    String body = arr.getJSONObject(i).getString("title");
			    String image = "";
			    String largeImage = "";
			    try{
				    if(arr.getJSONObject(i).getJSONArray("media").length() > 0){
				    	JSONArray ja = arr.getJSONObject(i).getJSONArray("media").getJSONObject(0).getJSONArray("media-metadata");	
				    	image = ja.getJSONObject(0).getString("url");
				    	if(ja.length() == 3){
				    		largeImage = ja.getJSONObject(2).getString("url");
				    	}else if(ja.length() == 2){
				    		largeImage = ja.getJSONObject(1).getString("url");
				    	}else{
				    		largeImage = ja.getJSONObject(0).getString("url");
				    	}
				    }
				    urlToShare.add(arr.getJSONObject(i).getString("url"));
				    newsType.add("NYNews");
				    listOfIds.add(body + "|" + largeImage + "|" +arr.getJSONObject(i).getString("abstract"));
				    listOfNews.add(body);
				    listOfImages.add(image);
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
	 
	private void populateNYTimesSearchNews() {
		String stringUrl = "http://api.nytimes.com/svc/search/v1/article?format=json&query="+MainActivity.SearchText+"&fields=small_image_url%2Ctitle%2Cabstract%2Curl&api-key=becca077f79afbd51b3e52b7f9bdc88e:13:69325874";
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
				
			    String body = arr.getJSONObject(i).getString("title");
			    String image = "";
			    String largeImage = "";
			    if(arr.getJSONObject(i).has("small_image_url")){
			    	image = arr.getJSONObject(i).getString("small_image_url");
			    	largeImage = arr.getJSONObject(i).getString("small_image_url");
			    }
			    try{
				    urlToShare.add(arr.getJSONObject(i).getString("url"));
				    newsType.add("NYNews");
				    listOfIds.add(body + "|" + largeImage + "|" +arr.getJSONObject(i).getString("abstract"));
				    listOfNews.add(body);
				    listOfImages.add(image);
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
	
	//@Override
	/*public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//getMenuInflater().inflate(R.menu.main, menu);
		//if(MainActivity.isLogedin==true)
			getMenuInflater().inflate(R.menu.main, menu);
		//else
			//getMenuInflater().inflate(R.menu.logoutmain, menu);
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
			Toast.makeText(NewsPreview.this, "Congrats: Logged out Successfull", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(NewsPreview.this, "Hey!! first login", Toast.LENGTH_LONG).show();
			
		}
	}


}
