

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
import com.facebook.widget.FacebookDialog;
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



public class NewsDetails  extends Activity{
	SocialAuthAdapter adapter;
	private UiLifecycleHelper uiHelper;
	Button fbShareButton;
LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.newsdetail);
	      setNewsDetails();
	      
// create a instance of SQLite Database which stores login info
		     loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		     loginDataBaseAdapter=loginDataBaseAdapter.open();
	      Button share = (Button) findViewById(R.id.share);
	      share.setText("Share");
	      share.setTextColor(Color.WHITE);
	      adapter = new SocialAuthAdapter(new ResponseListener());
	      adapter.addProvider(Provider.EMAIL, R.drawable.tick);
	      adapter.addProvider(Provider.LINKEDIN, R.drawable.tick);
	      adapter.enable(share);
	      fbShareButton = (Button) findViewById(R.id.shareByFB);
	      fbShareButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				      String urlToShare = MainActivity.link;
				      Intent intent = new Intent(Intent.ACTION_SEND);
				      intent.setType("text/plain");
				      intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
				      boolean facebookAppFound = false;
				      List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
				      for (ResolveInfo info : matches) {
				          if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
				              intent.setPackage(info.activityInfo.packageName);
				              facebookAppFound = true;
				              break;
				          }
				      }
				      if (!facebookAppFound) {
				          String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
				          intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
				      }
				      startActivity(intent);
				}});
	      
	}
	public void addToFavourites(View v)
	{
		if(MainActivity.isLogedin==false)
		{
			Intent loginintent=new Intent(getApplicationContext(),HomeActivity.class);
			startActivity(loginintent);
		}
		else
		{
			Toast.makeText(NewsDetails.this, "Added to favourites!!", Toast.LENGTH_LONG).show();
			loginDataBaseAdapter.insertFavEntry(MainActivity.username, MainActivity.favimageurl, MainActivity.favtitle, MainActivity.favbody,MainActivity.favsource);
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
			Toast.makeText(NewsDetails.this, "Congrats: Logged out Successfull", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(NewsDetails.this, "Hey!! first login", Toast.LENGTH_LONG).show();
			
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    //uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    //uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    //uiHelper.onDestroy();
	}
	
	private void setNewsDetails() {
		String id = MainActivity.newsId;
		
		String newsSource = "";
		String newsId = "";
		try{
			for(int i = 0; i < id.split("\\|").length; i++){
				newsSource = id.split("\\|")[0];
				if(i == 0 ){
					continue;
				}
				newsId += id.split("\\|")[i] + "|";
			}
		}catch(Exception e){
			Log.e("error", e.getMessage());
		}
		if(newsSource.contentEquals("GNews")){
			populateGNewsDetails(newsId);
		}else if(newsSource.contains("NYNews")){
			populateNYNewsDetails(newsId);
		}
	}

	private void populateGNewsDetails(String newsId) {
		newsId = newsId.substring(0, newsId.lastIndexOf('|'));
		String stringUrl = "http://content.guardianapis.com/search?show-fields=all&ids="+newsId+"&api-key=ggnt8jcucfkcqmqz3c6f5u5v";
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
				String title = "";
				String body = "";
				String image = "";
				TextView header = (TextView) findViewById(R.id.newsDetailHeader);
				TextView bod = (TextView) findViewById(R.id.newsDetailBody);
				ImageView newsImage = (ImageView) findViewById(R.id.newsDetailImage);
				try{
			    	title = arr.getJSONObject(i).getString("webTitle");
				    body = arr.getJSONObject(i).getJSONObject("fields").getString("body");
				    if(body.contains("<!-- Redistribution rights for this field are unavailable -->")){
				    	header.setText(title);
				    	bod.setText(body);
				    	break;
				    }
				    //image = body.substring(body.indexOf('<'), body.indexOf('>')+1);
				    image = arr.getJSONObject(i).getJSONObject("fields").getString("thumbnail");
				    String subString = body.substring(body.indexOf('<'), body.indexOf('>')+1);
				    body = body.replace(subString, "");
				    MainActivity.favimageurl=image;
				    MainActivity.favtitle=title;
				    MainActivity.favbody=body;
				    MainActivity.favsource="Guardian";
			    }
			    catch(Exception e){
			    	continue;
			    }
				newsImage.setImageBitmap(getBitmapFromURL(image));
				header.setText(title);
				bod.setText(Html.fromHtml(body));
				TextView tv = (TextView)findViewById(R.id.newsDetailsSource);
				tv.setText(Html.fromHtml("&copy; Source: Guardian"));
				break;//Taking first value only
			}
		}catch(Exception e){
			Log.e("error", e.getMessage());
		}
	}
	
	private void populateNYNewsDetails(String newsId) {
		TextView header = (TextView) findViewById(R.id.newsDetailHeader);
		TextView bod = (TextView) findViewById(R.id.newsDetailBody);
		ImageView newsImage = (ImageView) findViewById(R.id.newsDetailImage);
		newsImage.setImageBitmap(getBitmapFromURL(newsId.split("\\|")[1]));
		header.setText(newsId.split("\\|")[0]);
		bod.setText(newsId.split("\\|")[2]);
 MainActivity.favimageurl=newsId.split("\\|")[1];
		 MainActivity.favtitle=newsId.split("\\|")[0];
		 MainActivity.favbody=newsId.split("\\|")[2];
		 MainActivity.favsource="NY Times";
		 TextView tv = (TextView)findViewById(R.id.newsDetailsSource);
			tv.setText(Html.fromHtml("&copy; Source: NY Times"));
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
	
	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			Log.d("Share-Menu", "Authentication Successful");
			final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("Share-Bar", "Provider Name = " + providerName);
			if (providerName.equalsIgnoreCase("share_mail")) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
						"mvurimi@syr.edu", null));
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "News from News Reader Application");
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
						"image5964402.png");
				Uri uri = Uri.fromFile(file);
				emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(emailIntent, "Test"));
				return;
			}else{
				adapter.updateStatus(MainActivity.link, new MessageListener(), false);	
			}
			
		}
		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			Log.d("Share-Menu", error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("Share-Menu", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Menu", "Dialog Closed by pressing Back Key");

		}
	}
	
	
		private final class MessageListener implements SocialAuthListener<Integer> {
			@Override
			public void onExecute(String provider, Integer t) {
				Integer status = t;
				if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
					Log.d("debug", "message posted");
				else
					Log.d("debug", "message not posted");
			}

			@Override
			public void onError(SocialAuthError e) {
				Log.d("error", e.getMessage());
			}
		}


		

}
