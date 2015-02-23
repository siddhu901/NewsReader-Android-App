
package com.su.newsreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Search extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.search);
		 EditText text = (EditText) findViewById(R.id.searchText);
		 text.setText("");
	}
	
	public void searchClick(View v){
		EditText text = (EditText) findViewById(R.id.searchText);
		MainActivity.SearchText = text.getText().toString(); 
		Intent intent = new Intent(this, NewsPreview.class);
		this.startActivity(intent);
	}
}
