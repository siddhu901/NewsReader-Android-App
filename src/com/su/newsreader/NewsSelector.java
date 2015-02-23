

package com.su.newsreader;


import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsSelector extends ArrayAdapter<String>{
	private final Context context;
	private final String[] values;
	private final List<String> valuesSelected;
	public NewsSelector(Context context, String[] values, List<String> valuesSelected) {
		super(context, R.layout.activity_main, values);
		this.context = context;
		this.values = values;
		this.valuesSelected = valuesSelected;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_main, parent, false);
		Button btn = (Button) rowView.findViewById(R.id.button1);
		btn.setVisibility(View.GONE);
		
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		ImageView imageTick = (ImageView) rowView.findViewById(R.id.tick);
		
		textView.setText(values[position]);
		String s = values[position];
		if (s.equals("Guardian")) {
			imageView.setImageResource(R.drawable.guardian);
		} else if (s.equals("NY Times")) {
			imageView.setImageResource(R.drawable.nytimes);
		} else if (s.equals("Washington Post")) {
			imageView.setImageResource(R.drawable.washington);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		if(!valuesSelected.contains(s)){
			imageTick.setImageResource(R.drawable.untick);
		}
		if(position == values.length-1){
			textView.setVisibility(View.GONE);
			imageView.setImageResource(R.drawable.untick);
			imageTick.setImageResource(R.drawable.untick);
			btn.setVisibility(View.VISIBLE);	
			btn.setText("Show News");
			
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity.SearchText = "";
					Intent intent = new Intent(getContext(), NewsPreview.class);
					context.startActivity(intent);
				}
			});
			return rowView;
		}
		return rowView;
	}

}
