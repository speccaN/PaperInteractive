package com.example.paper.paperinteractive.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paper.paperinteractive.Objects.Child;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.Objects.LibraryGroup;

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

    //Library Groups table name
    private static final String TABLE_LIBRARY_GROUPS = "groups";

    //Library Groups Children table name
    private static final String TABLE_LIBRARY_CHILDREN = "library_children";

    //Children Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    //Library Groups Columns names
    private static final String KEY_GROUP_ID = "_id";
    private static final String KEY_GROUP_NAME = "group_name";

    //Library Library Groups Children names
    private static final String KEY_LIBRARY_CHILDREN_ID = "_id";
    private static final String KEY_LIBRARY_CHILD_NAME = "child_name";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        String CREATE_CHILDREN_TABLE = "CREATE TABLE " + TABLE_CHILDREN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT" + ")";

        String CREATE_LIBRARY_GROUPS_TABLE = "CREATE TABLE " + TABLE_LIBRARY_GROUPS + "("
                + KEY_GROUP_ID + " INTEGER PRIMARY KEY," + KEY_GROUP_NAME + " TEXT" + ")";

        String CREATE_LIBRARY_GROUPS_CHILD_TABLE = "CREATE TABLE " + TABLE_LIBRARY_CHILDREN + "("
                + KEY_LIBRARY_CHILDREN_ID + " INTEGER PRIMARY KEY," + KEY_LIBRARY_CHILD_NAME + " TEXT," +
                "library_group_id INTEGER NOT NULL," +
                "FOREIGN KEY (library_group_id) REFERENCES " + TABLE_LIBRARY_GROUPS +
                "(" + KEY_GROUP_ID + "))";
        db.execSQL(CREATE_LIBRARY_GROUPS_TABLE);
        db.execSQL(CREATE_CHILDREN_TABLE);
        db.execSQL(CREATE_LIBRARY_GROUPS_CHILD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if one exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY_GROUPS);
        //Create tables again
        onCreate(db);
    }

    public void addChild(Child child) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, child.getName());
        values.put(KEY_AGE, child.getAge());

        //Insert Row
        db.insert(TABLE_CHILDREN, null, values);
        db.close(); // Close database connection
    }

    // Get specific child
    public Child getChild(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHILDREN, new String[]{KEY_ID, KEY_NAME, KEY_AGE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
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
        if (cursor.moveToFirst()) {
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
        db.close();

        // Return Child list
        return childList;
    }

    // Get Children count
    public int getChildrenCount() {
        String countQuery = "SELECT * FROM " + TABLE_CHILDREN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        //Return count
        return count;
    }

    // Update a Child
    public int updateChild(Child child) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, child.getName());
        values.put(KEY_AGE, child.getAge());

        //Update row
        return db.update(TABLE_CHILDREN, values, KEY_ID + " = ?",
                new String[]{String.valueOf(child.getId())});
    }

    //Delete Child
    public void deleteChild(Child child) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHILDREN, KEY_ID + " = ?",
                new String[]{String.valueOf(child.getId())});
        db.close();
    }

    private boolean checkDataBaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public int getLibraryGroupCount() {
        String countQuery = "SELECT * FROM " + TABLE_LIBRARY_GROUPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        //Return count
        return count;
    }

    public void addGroup(LibraryGroup group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, group.getGroupName());

        //Insert Row
        db.insert(TABLE_LIBRARY_GROUPS, null, values);
        db.close(); // Close database connection
    }

    public Cursor getAllGroups() {
        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_LIBRARY_GROUPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Return Group Cursor
        return cursor;
    }

    public Cursor fetchGroup() {
        String query = "SELECT * FROM " + TABLE_LIBRARY_GROUPS;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public Cursor fetchChildren(String libraryChild) {
        String query = "SELECT * FROM " + TABLE_LIBRARY_CHILDREN + " WHERE " + KEY_GROUP_ID + " = " +
                "'" + libraryChild + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addGroupChild(LibraryChild groupChild) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LIBRARY_CHILD_NAME, groupChild.getName());
        values.put("library_group_id", groupChild.getGroupId());

        //Insert Row
        db.insert(TABLE_LIBRARY_CHILDREN, null, values);
        db.close(); // Close database connection
    }

    public Cursor getAllGroupChildren(int id){
        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_LIBRARY_CHILDREN +
                " WHERE library_group_id = " + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Return Group Cursor
        return cursor;
    }

    public LibraryGroup getLibraryGroup(String group) {
        SQLiteDatabase db = this.getReadableDatabase();
        LibraryGroup lGroup = new LibraryGroup();
        String query = "SELECT * FROM " + TABLE_LIBRARY_GROUPS + " WHERE " + KEY_GROUP_NAME + " = " +
                "'" + group + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                lGroup.setId(Integer.parseInt(cursor.getString(0)));
                lGroup.setName(cursor.getString(1));

            } while (cursor.moveToNext());
        }
        return lGroup;
    }

    public int getLibraryGroupId(String groupName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_GROUP_ID + " FROM " + TABLE_LIBRARY_GROUPS + " WHERE "
                + KEY_GROUP_NAME + " = " +
                "'" + groupName + "'";
        Cursor c = db.rawQuery(query, null);
        if (c != null)
            c.moveToFirst();
        int i = Integer.parseInt(c.getString(0));
        c.close();

        return i;
    }

    public Cursor getMatchingGroupAndChild(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT group_name, _id, library_child_id, child_name FROM " +
                TABLE_LIBRARY_CHILDREN + " INNER JOIN " + TABLE_LIBRARY_GROUPS +
                " ON library_children.library_group_id = groups._id";

        Cursor c = db.rawQuery(query, null);

        return c;
    }
}
