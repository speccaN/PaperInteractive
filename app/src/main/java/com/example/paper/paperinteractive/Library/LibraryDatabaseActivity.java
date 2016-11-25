package com.example.paper.paperinteractive.Library;

import android.app.ExpandableListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LibraryDatabaseActivity extends ExpandableListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = getClass().getSimpleName();

    DBHandler dbHandler;
    //Cursor groupsCursor; // Cursor for list of groups (lists top nodes)
    //int groupIdColumnIndex;

    MyAdapter adapter;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_database);

        dbHandler = new DBHandler(this);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(android.R.id.list);

        groupsCursor = dbHandler.getAllGroups();

        String[] test = new String[3];
        int count = 0;
        if(groupsCursor.moveToFirst()){
            do {
                test[count] = groupsCursor.getString(0);
                count++;
            } while (groupsCursor.moveToNext());
        }

        adapter = new MyAdapter(this,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"group_name"},
                new int[]{android.R.id.text1},
                android.R.layout.simple_expandable_list_item_2,
                new String[]{"child_name"},
                new int[]{android.R.id.text1, android.R.id.text2});

        expandableListView.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class MyAdapter extends SimpleCursorTreeAdapter{
        private final String LOG_TAG = getClass().getSimpleName();
        private LibraryDatabaseActivity mActivity;
        protected final HashMap<Integer, Integer> mGroupMap;


        public MyAdapter(Context context, int groupLayout, String[] groupFrom,
                         int[] groupTo,
                         int childLayout, String[] childFrom, int[] childTo) {

            super(context, null, groupLayout, groupFrom, groupTo,
                    childLayout, childFrom, childTo);

            mActivity = (LibraryDatabaseActivity) context;
            mGroupMap = new HashMap<>();
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            int groupPos = groupCursor.getPosition();
            int groupId = groupCursor.getInt(groupCursor.getColumnIndex("group_id"));

            mGroupMap.put(groupId, groupPos);
            Loader<Cursor> loader = mActivity.getLoaderManager().getLoader(groupId);
            if (loader != null && !loader.isReset()){
                mActivity.getLoaderManager().restartLoader(groupId, null, mActivity);
            } else{
                mActivity.getLoaderManager().initLoader(groupId, null, mActivity);
            }

            return null;
        }

        public HashMap<Integer, Integer> getGroupMap(){
            return mGroupMap;
        }
    }
}
