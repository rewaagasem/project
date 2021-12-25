package com.rewaagasem.university_project_android.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jasmineborno129.university_project_android.model.User;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME = "mohanad_computing";
    //database version
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "tbl_users";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        //creating table
        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, Username TEXT, Password TEXT, Email Text,Gender Text,Weight Text,Length Text,DOB Text)";
        db.execSQL(query);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add the new note
    public void addUser(String username, String password , String email) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);

        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    //get the all notes
    public ArrayList<User> getUsers() {
        ArrayList<User> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(cursor.getString(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setGender(cursor.getString(4));
                user.setWeight(cursor.getString(5));
                user.setLength(cursor.getString(6));
                user.setDob(cursor.getString(7));
                arrayList.add(user);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TABLE_NAME, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateUser(String ID,String username, String email, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    public void updateUserInformation(String ID,String gender,String weight , String length,String dob) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("gender", gender);
        values.put("Weight", weight);
        values.put("length", length);
        values.put("dob", dob);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }


}