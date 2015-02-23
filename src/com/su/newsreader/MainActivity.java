

package com.su.newsreader;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.preference.PreferenceManager;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity  {
	static final String[] papers = new String[] { "Guardian", "NY Times", " " };
	private List<String> papersSelected = new ArrayList<String>();
	static String newsId = "";
	public static String link = "www.syr.edu";
	public static String SearchText = "";
	public static String favimageurl="";
	public static String favtitle="";
	public static String favbody="";
	public static String favsource="";
	public static boolean isLogedin=false;
	public static String username="";
	public static boolean isFavButtonClicked=false;
	private SharedPreferences preferences;
	private Editor editor;
	
    ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		editor = preferences.edit();
		for(String s: papers){
			if(preferences.getBoolean(s, true)){
				papersSelected.add(s);	
			}
		}
		ListAdapter ad = new NewsSelector(this, papers, papersSelected);
		setListAdapter(ad);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String selectedValue = (String) getListAdapter().getItem(position);
		if(papersSelected.contains(selectedValue)){
			papersSelected.remove(selectedValue);
			editor.putBoolean(selectedValue, false);
      	  	editor.commit();
		}else{
			papersSelected.add(selectedValue);
			editor.putBoolean(selectedValue, true);
      	  	editor.commit();
		}
		setListAdapter(new NewsSelector(this, papers, papersSelected));
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
			Toast.makeText(MainActivity.this, "Congrats: Logged out Successfull", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(MainActivity.this, "Hey!! first login", Toast.LENGTH_LONG).show();
			
		}
	}
}
