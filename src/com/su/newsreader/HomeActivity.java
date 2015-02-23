

package com.su.newsreader;

import com.su.newsreader.LoginDataBaseAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity 
{
	Button btnSignIn,btnSignUp;
	LoginDataBaseAdapter loginDataBaseAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.main);
	     
	     // create a instance of SQLite Database which stores login info
	     loginDataBaseAdapter=new LoginDataBaseAdapter(this);
	     loginDataBaseAdapter=loginDataBaseAdapter.open();
	     
	  // create a instance of SQLite Database which stores favourites

	     // Get The Refference Of Buttons
	     btnSignIn=(Button)findViewById(R.id.buttonSignIN);
	     btnSignUp=(Button)findViewById(R.id.buttonSignUP);
			
	    // Set OnClick Listener on SignUp button 
	    btnSignUp.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			/// Create Intent for SignUpActivity  and Start The Activity
			Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
			startActivity(intentSignUP);
			}
		});
	}
	
	
	// Methods to handleClick Event of Sign In Button
	public void signIn(View V)
	   {
			final Dialog dialog = new Dialog(HomeActivity.this);
			dialog.setContentView(R.layout.login);
		    dialog.setTitle("Login");
	
		    // get the References of views
		    final  EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
		    final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);
		    
			Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);
				
			// Set On ClickListener
			btnSignIn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// get The User name and Password
					String userName=editTextUserName.getText().toString();
					String password=editTextPassword.getText().toString();
					
					// fetch the Password form database for respective user name
					String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
					
					// check if the Stored password matches with  Password entered by user
					if(password.equals(storedPassword))
					{
						MainActivity.isLogedin=true;
						MainActivity.username=userName;
						Toast.makeText(HomeActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
						dialog.dismiss();
						Bundle extras = getIntent().getExtras();
						String value="";
						if (extras != null) 
						{
						    value = extras.getString("isfavbutton");
						}
						if(!value.contentEquals("yes"))
						{
							loginDataBaseAdapter.insertFavEntry(userName, MainActivity.favimageurl, MainActivity.favtitle, MainActivity.favbody,MainActivity.favsource);
						}
						
		                 Intent intent=new Intent();  
		                 intent.putExtra("Username",userName);  
		                      
		                 setResult(9,intent);  
		                 finish();
					}
					else
					{
						Toast.makeText(HomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
						finish();
					}
				}
			});
			
			dialog.show();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		loginDataBaseAdapter.close();
	}
}
