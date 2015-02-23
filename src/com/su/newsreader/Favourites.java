

package com.su.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.ListActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class Favourites extends ListActivity {

	private SQLiteDatabase db;
	LoginDataBaseAdapter loginDataBaseAdapter;
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	List<String> faventries=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter.open();
	   
		showFavourites();
	
	}

		public void showFavourites()
		{
			MainActivity.isFavButtonClicked=true;
			//Navigate to login and signup page if not logged in
			if(MainActivity.isLogedin==false)
			{
				Intent loginintent=new Intent(this,HomeActivity.class);
				loginintent.putExtra("isfavbutton","yes");
				startActivityForResult(loginintent,0);
			}
			
			else
			{
				StrictMode.setThreadPolicy(policy); 
				TextView title = (TextView) findViewById(R.id.favnewsDetailHeader);
				TextView body = (TextView) findViewById(R.id.favnewsDetailBody);
				ImageView image = (ImageView)findViewById(R.id.favnewsDetailImage); 
			    faventries=loginDataBaseAdapter.getFavSinlgeEntry(MainActivity.username);
			    ListAdapter ad = new FavouriteSelector(this,faventries);
				setListAdapter(ad);
			}
			 
		}
		
		//retrieve the username from the HomeActvity and display favourites 
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			if(resultCode==9)
			{
				StrictMode.setThreadPolicy(policy); 
				TextView title = (TextView) findViewById(R.id.favnewsDetailHeader);
				TextView body = (TextView) findViewById(R.id.favnewsDetailBody);
				ImageView image = (ImageView)findViewById(R.id.favnewsDetailImage);
				String username=data.getStringExtra("Username");  
				List<String> faventries=null;
			    faventries=loginDataBaseAdapter.getFavSinlgeEntry(username);
			    if( faventries.size()==1)
			    {
			    	if(faventries.get(0)=="NOT EXIST")
			    	{
			    		Toast.makeText(Favourites.this, "No favourites!!", Toast.LENGTH_LONG).show();
			    		finish();
			    	}
			    }
			    ListAdapter ad = new FavouriteSelector(this,faventries);
				setListAdapter(ad);
			}
			
			else
				finish();
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
		
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			String selectedValue = (String) getListAdapter().getItem(position);
			Intent i = new Intent(this,FavNewsDetails.class);
			i.putExtra("news",selectedValue);
			startActivity(i);
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
			Toast.makeText(Favourites.this, "Congrats: Logged out Successfull", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(Favourites.this, "Hey!! first login", Toast.LENGTH_LONG).show();
			
		}
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_favourites,
					container, false);
			return rootView;
		}
	}

}
