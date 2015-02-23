

package com.su.newsreader;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUPActivity extends Activity
{
	EditText editTextUserName,editTextPassword,editTextConfirmPassword;
	Button btnCreateAccount;
	 SQLiteDatabase db;
	LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		// get Instance  of Database Adapter
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		
		// Get Refferences of Views
		editTextUserName=(EditText)findViewById(R.id.editTextUserName);
		editTextPassword=(EditText)findViewById(R.id.editTextPassword);
		editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
		
		btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String userName=editTextUserName.getText().toString();
			String password=editTextPassword.getText().toString();
			String confirmPassword=editTextConfirmPassword.getText().toString();
			db=loginDataBaseAdapter.getDatabaseInstance();
			String qu="select * from LOGIN where USERNAME="+userName;
			//Cursor c=db.rawQuery(qu,null);
			Cursor c=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
			 int count = c.getCount();
			 
			 //check whether username already is used
			 if(count>0)
			 {
				 Toast.makeText(getApplicationContext(), "Username not available!! Try other name", Toast.LENGTH_LONG).show();
			 }

			// check if any of the fields are vaccant
			 else if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
			{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
			}
			// check if both password matches
			 else if(!password.equals(confirmPassword))
			{
				Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
				return;
			}
			
			
			else
			{
			    // Save the Data in Database
			    loginDataBaseAdapter.insertEntry(userName, password);
			    
			    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
			}
			//setContentView(R.layout.main);
			finish();
		}
	});
}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		loginDataBaseAdapter.close();
	}
}
