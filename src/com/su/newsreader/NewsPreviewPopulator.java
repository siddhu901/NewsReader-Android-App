

package com.su.newsreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsPreviewPopulator extends ArrayAdapter<String>{
	private final Context context;
	private final List<String> valuesText;
	private final List<String> valuesImages;
	private List<String> newsTypeS;
	public NewsPreviewPopulator(Context context, List<String> newsPreviewListText, List<String> newsPreviewListImages, List<String>newsType) {
		super(context, R.layout.newspreview, newsPreviewListText);
		this.context = context;
		this.valuesText = newsPreviewListText;
		this.valuesImages =  newsPreviewListImages;
		this.newsTypeS = newsType;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.newspreview, parent, false);
		String s = valuesText.get(position);
		TextView textView = (TextView) rowView.findViewById(R.id.newsPreview);
		textView.setText(s);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.newsImagePreview);
		if(valuesImages.get(position) == ""){
			imageView.setImageResource(R.drawable.ic_launcher);
		}else{
			imageView.setImageBitmap(getBitmapFromURL(valuesImages.get(position)));	
		}
		TextView src = (TextView)rowView.findViewById(R.id.newsPreviewsource);
		if(newsTypeS.get(position).equalsIgnoreCase("GNews")){
			src.setText(Html.fromHtml("&copy; Source: Guardian"));
		}else{
			src.setText(Html.fromHtml("&copy; Source: NY Times"));
		}
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
