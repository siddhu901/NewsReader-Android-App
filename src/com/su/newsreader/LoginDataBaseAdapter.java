

package com.su.newsreader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter 
{
		static final String DATABASE_NAME = "login.db";
		static final int DATABASE_VERSION = 1;
		public static final int NAME_COLUMN = 1;
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String DATABASE_CREATE = "create table "+"LOGIN"+
		                             "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";
		
		static final String FAV_DATABASE_CREATE="create table "+"FAVOURITES"+
                "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME text,IMAGE text,TITLE text,BODY text,SOURCE text); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  LoginDataBaseAdapter(Context _context) 
		{
			context = _context;
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public  LoginDataBaseAdapter open() throws SQLException 
		{
			db = dbHelper.getWritableDatabase();
			return this;
		}
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{
			return db;
		}

		public void insertEntry(String userName,String password)
		{
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put("USERNAME", userName);
			newValues.put("PASSWORD",password);
			
			// Insert the row into your table
			db.insert("LOGIN", null, newValues);
			///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
		}
		public void insertFavEntry(String userName,String imageurl,String title,String body,String source)
		{
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put("USERNAME", userName);
			newValues.put("IMAGE",imageurl);
			newValues.put("TITLE",title);
			newValues.put("BODY",body);
			newValues.put("SOURCE",source);
			
			// Insert the row into your table
				db.insert("FAVOURITES", null, newValues);
		}
		
		public int deleteEntry(String UserName)
		{
			//String id=String.valueOf(ID);
		    String where="USERNAME=?";
		    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
	       // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
	        return numberOFEntriesDeleted;
		}	
		public String getSinlgeEntry(String userName)
		{
			Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	        	return "NOT EXIST";
	        }
		    cursor.moveToFirst();
			String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
			cursor.close();
			return password;				
		}
		
		public List<String> getFavSinlgeEntry(String userName)
		{
			Cursor cursor=db.query("FAVOURITES", null, " USERNAME=?", new String[]{userName}, null, null, null);
			List<String> allentries=new ArrayList<String>();
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	        	String temp=new String();
	        	temp="NOT EXIST";
	        	allentries.add(temp);
	        	return allentries;
	        }
		    cursor.moveToFirst();
			
		    do
		    {
		    	String temp=new String();
		    	temp=cursor.getString(cursor.getColumnIndex("TITLE"));
		    	temp+="~";
		    	temp+=cursor.getString(cursor.getColumnIndex("BODY"));
		    	temp+="~";
		    	temp+=cursor.getString(cursor.getColumnIndex("IMAGE"));
		    	temp+="~";
		    	temp+=cursor.getString(cursor.getColumnIndex("SOURCE"));
			    allentries.add(temp);
		    }while (cursor.moveToNext());
			cursor.close();
			return allentries;			
		}
		public void  updateEntry(String userName,String password)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("USERNAME", userName);
			updatedValues.put("PASSWORD",password);
			
	        String where="USERNAME = ?";
		    db.update("LOGIN",updatedValues, where, new String[]{userName});			   
		}		
}

