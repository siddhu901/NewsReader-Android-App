

package com.su.newsreader;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FavouriteSelector extends ArrayAdapter<String>{
	private final Context context;
	private final List<String> valuesSelected;
	public FavouriteSelector(Context context,List<String> valuesSelected) 
	{
		super(context, R.layout.favs_main,valuesSelected);
		this.context = context;
		this.valuesSelected = valuesSelected;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.favs_main, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.favheader);
		TextView textView1 = (TextView) rowView.findViewById(R.id.favsource);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.favimage);
		
		String faventry=valuesSelected.get(position);
		String[] values=faventry.split("~");
		
		textView.setText(values[0]);
		imageView.setImageBitmap(getBitmapFromURL((values[2])));
		textView1.setText(values[3]);
		
		return rowView;
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
