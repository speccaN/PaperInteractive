package com.example.paper.paperinteractive.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paper.paperinteractive.Objects.Child;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.Objects.LibraryGroup;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler sInstance;

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "PaperInteractiveDB";

    //TABLES
    private static final String TABLE_CHILDREN = "Children"; //Children table name
    private static final String TABLE_LIBRARY_GROUPS = "Library_Groups"; //Library Groups table name
    private static final String TABLE_LIBRARY_CHILDREN = "Library_Children"; //Library Groups Children table name
    private static final String TABLE_MEETINGS = "Meetings"; // Meeting Table Name
    private static final String TABLE_ASSIGNED_EXERCISES = "Assigned_Exercises";

    //Children Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    //Library Groups Columns names
    private static final String KEY_GROUP_ID = "_id";
    private static final String KEY_GROUP_NAME = "group_name";

    //Library Library Groups Children names
    private static final String KEY_LIBRARY_CHILD_ID = "_id";
    private static final String KEY_LIBRARY_CHILD_NAME = "child_name";
    private static final String KEY_LIBRARY_CHILD_GROUP_NAME = "group_name";

    // Meetings Table Column names
    private static final String KEY_MEETINGS_MEETING_ID = "_id";
    private static final String KEY_MEETINGS_ID = "child_id";
    private static final String KEY_MEETINGS_CHILD_NAME = "child_name";
    private static final String KEY_MEETINGS_DATE = "date";

    // Assigned Exercises table column names
    private static final String KEY_AE_CHILD_ID = "child_id";
    private static final String KEY_AE_CHILD_NAME = "child_name";
    private static final String KEY_AE_EXERCISE_ID = "exercise_id";
    private static final String KEY_AE_EXERCISE_GROUP = "exercise_group";
    private static final String KEY_AE_EXERCISE_NAME = "exercise_name";

    public static synchronized DBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if(sInstance == null) {
            sInstance = new DBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        String CREATE_CHILDREN_TABLE = "CREATE TABLE " + TABLE_CHILDREN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT)";

        String CREATE_LIBRARY_GROUPS_TABLE = "CREATE TABLE " + TABLE_LIBRARY_GROUPS + "("
                + KEY_GROUP_ID + " INTEGER PRIMARY KEY," + KEY_GROUP_NAME + " TEXT" + ")";

        String CREATE_LIBRARY_GROUPS_CHILD_TABLE = "CREATE TABLE " + TABLE_LIBRARY_CHILDREN + "("
                + KEY_LIBRARY_CHILD_ID + " INTEGER PRIMARY KEY," + KEY_LIBRARY_CHILD_NAME + " TEXT," +
                "group_id INTEGER NOT NULL," + KEY_LIBRARY_CHILD_GROUP_NAME + " TEXT," +
                "FOREIGN KEY (group_id) REFERENCES " + TABLE_LIBRARY_GROUPS +
                "(" + KEY_GROUP_ID + "))";

        String CREATE_MEETINGS_TABLE = "CREATE TABLE " + TABLE_MEETINGS + "("
                + KEY_MEETINGS_MEETING_ID + " INTEGER PRIMARY KEY,"
                + KEY_MEETINGS_ID + " INTEGER," + KEY_MEETINGS_CHILD_NAME + " TEXT,"
                + KEY_MEETINGS_DATE + " TIMESTAMP DEFAULT CURRENT_DATE,"
                + "FOREIGN KEY (" + KEY_MEETINGS_ID + ") REFERENCES " + TABLE_CHILDREN + "(" + KEY_ID + "))";

        String CREATE_AE_TABLE = "CREATE TABLE " + TABLE_ASSIGNED_EXERCISES + "("
                + KEY_AE_CHILD_ID + " INTEGER,"
                + KEY_AE_CHILD_NAME + " TEXT,"
                + KEY_AE_EXERCISE_ID + " INTEGER,"
                + KEY_AE_EXERCISE_GROUP + " TEXT,"
                + KEY_AE_EXERCISE_NAME + " TEXT,"
                + "FOREIGN KEY (" + KEY_AE_CHILD_ID + ") " +
                "REFERENCES " + TABLE_CHILDREN + "(" + KEY_ID + "), "
                + "FOREIGN KEY (" + KEY_AE_EXERCISE_ID + ") " +
                "REFERENCES " + TABLE_LIBRARY_CHILDREN + "(" + KEY_LIBRARY_CHILD_ID + "))";

        db.execSQL(CREATE_LIBRARY_GROUPS_TABLE);
        db.execSQL(CREATE_CHILDREN_TABLE);
        db.execSQL(CREATE_LIBRARY_GROUPS_CHILD_TABLE);
        db.execSQL(CREATE_MEETINGS_TABLE);
        db.execSQL(CREATE_AE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if one exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY_CHILDREN);
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
        ArrayList<Child> childList = new ArrayList<>();

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

    public int getLibraryGroupCount() {
        String countQuery = "SELECT * FROM " + TABLE_LIBRARY_GROUPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        //Return count
        return count;
    }

    public void addLibraryGroup(LibraryGroup group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, group.getGroupName());

        //Insert Row
        db.insert(TABLE_LIBRARY_GROUPS, null, values);
        db.close(); // Close database connection
    }

    public Cursor getAllLibraryGroups() {
        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_LIBRARY_GROUPS;
        SQLiteDatabase db = this.getWritableDatabase();

        // Return Group Cursor
        return db.rawQuery(selectQuery, null);
    }

    public List<LibraryGroup> getAllLibraryGroupsList(){
        List<LibraryGroup> list = new ArrayList<>();
        Cursor cursor = getAllLibraryGroups();
        if (cursor.moveToFirst()) {
            do {
                LibraryGroup group = new LibraryGroup();
                group.setId(Integer.parseInt(cursor.getString(0)));
                group.setName(cursor.getString(1));
                list.add(group);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public Cursor getLibraryGroupChildren(String groupId) {
        String query = "SELECT * FROM " + TABLE_LIBRARY_CHILDREN + " WHERE " + KEY_GROUP_ID + " = " +
                "'" + groupId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addLibraryGroupChild(LibraryChild groupChild) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LIBRARY_CHILD_NAME, groupChild.getName());
        values.put("group_id", getLibraryGroupFromId(groupChild.getGroupName()));
        values.put("group_name", groupChild.getGroupName());

        //Insert Row
        db.insert(TABLE_LIBRARY_CHILDREN, null, values);
        db.close(); // Close database connection
    }

    public LibraryChild getLibraryGroupChild(int childId){
        String query = "SELECT * FROM " + TABLE_LIBRARY_CHILDREN + " WHERE " + KEY_LIBRARY_CHILD_ID
                + " = " + childId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        LibraryChild child = new LibraryChild();
        LibraryGroup group;
        if (cursor.moveToFirst()){
            do {
                child.setId(cursor.getInt(0));
                child.setName(cursor.getString(1));
                child.setGroup(group = getLibraryGroupFromId(cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return child;
    }

    public Cursor getAllLibraryGroupChildren(int id){
        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_LIBRARY_CHILDREN +
                " WHERE group_id = " + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();

        // Return Group Cursor
        return db.rawQuery(selectQuery, null);
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
        cursor.close();
        return lGroup;
    }

    public LibraryGroup getLibraryGroupFromId(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        LibraryGroup lGroup = new LibraryGroup();
        String query = "SELECT * FROM " + TABLE_LIBRARY_GROUPS + " WHERE " + KEY_GROUP_ID + " = " +
                "'" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                lGroup.setId(Integer.parseInt(cursor.getString(0)));
                lGroup.setName(cursor.getString(1));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return lGroup;
    }

    public int getLibraryGroupFromId(String groupName){
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

        return db.rawQuery(query, null);
    }

    public void addMeeting(Child child, LibraryChild[] exercises){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MEETINGS_ID, child.getId());
        values.put(KEY_MEETINGS_CHILD_NAME, child.getName());
        //Insert Row
        db.insert(TABLE_MEETINGS, null, values);

        for (int i = 0; i < exercises.length; i++){
            ContentValues aeValues = new ContentValues();
            aeValues.put(KEY_AE_CHILD_ID, child.getId());
            aeValues.put(KEY_AE_CHILD_NAME, child.getName());
            aeValues.put(KEY_AE_EXERCISE_ID, exercises[i].getId());
            aeValues.put(KEY_AE_EXERCISE_GROUP, exercises[i].getGroupName());
            aeValues.put(KEY_AE_EXERCISE_NAME, exercises[i].getName());
            db.insert(TABLE_ASSIGNED_EXERCISES, null, aeValues);
        }

        db.close(); // Close database connection
    }

    public void removeLccMeeting(int childId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = KEY_MEETINGS_ID + " = ?";
        String[] args = new String[]{String.valueOf(childId)};
        db.delete(TABLE_MEETINGS, where, args);
    }
}
