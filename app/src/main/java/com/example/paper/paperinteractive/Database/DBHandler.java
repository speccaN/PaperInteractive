package com.example.paper.paperinteractive.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paper.paperinteractive.Objects.Child;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "PaperInteractiveDB";

    //Children table name
    private static final String TABLE_CHILDREN = "children";

    //Children Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHILDREN_TABLE = "CREATE TABLE " + TABLE_CHILDREN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT" + ")";
        db.execSQL(CREATE_CHILDREN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if one exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        //Create tables again
        onCreate(db);
    }

    public void addChild(Child child){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, child.getName());
        values.put(KEY_AGE, child.getAge());

        //Insert Row
        db.insert(TABLE_CHILDREN, null, values);
        db.close(); // Close database connection
    }

    // Get specific child
    public Child getChild(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHILDREN, new String[] {KEY_ID, KEY_NAME, KEY_AGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Child child = new Child(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        cursor.close();

        // Return Child
        return child;
    }

    // Get all Children in Database
    public ArrayList<Child> getAllChildren() {
        ArrayList<Child> childList = new ArrayList<Child>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_CHILDREN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to the list
        if(cursor.moveToFirst()){
            do {
                Child child = new Child();
                child.setId(Integer.parseInt(cursor.getString(0)));
                child.setName(cursor.getString(1));
                child.setAge(cursor.getString(2));

                //Add to the Child list
                childList.add(child);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Return Child list
        return childList;
    }

    // Get Children count
    public int getChildrenCount(){
        String countQuery = "SELECT * FROM " + TABLE_CHILDREN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        //Return count
        return count;
    }

    // Update a Child
    public int updateChild(Child child){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, child.getName());
        values.put(KEY_AGE, child.getAge());

        //Update row
        return db.update(TABLE_CHILDREN, values, KEY_ID + " = ?",
                new String[]{String.valueOf(child.getId())});
    }

    //Delete Child
    public void deleteChild(Child child){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHILDREN, KEY_ID + " = ?",
                new String[]{String.valueOf(child.getId())});
        db.close();
    }

    private boolean checkDataBaseExist(Context context, String dbName){
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
