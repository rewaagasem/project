package com.rewaagasem.university_project_android.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jasmineborno129.university_project_android.model.Food;

import java.util.ArrayList;

public class FoodDBHelper extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME = "mohanad_computing";
    //database version
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "tbl_food";

    public FoodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        //creating table
        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, Name Text,Category Text,Image BLOB,Calory Text,UID String,key_name Text)";
        db.execSQL(query);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add the new note
    public void addRecord(String name, String category , byte [] image, String calory,String uid,String key_name) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("category", category);
        values.put("image", image);
        values.put("calory", calory);
        values.put("uid", uid);
        values.put("key_name", key_name);

        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    //get the all notes
    public ArrayList<Food> getRecords() {
        ArrayList<Food> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Food record = new Food();
                record.setId(cursor.getString(0));
                record.setName(cursor.getString(1));
                record.setCategory(cursor.getString(2));
                record.setCalory(cursor.getString(4));
                record.setUid(cursor.getString(5));
//                record.setImage(cursor.getBlob(3));
                record.setKey_name(cursor.getString(6));
                arrayList.add(record);
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


    public void updateFoodInformation(String ID,String name,String cate , String calory,byte [] image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("name", name);
        values.put("category", cate);
        values.put("calory", calory);
        values.put("image", image);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

}