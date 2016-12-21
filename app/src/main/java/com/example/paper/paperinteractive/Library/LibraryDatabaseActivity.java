package com.example.paper.paperinteractive.Library;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.R;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

public class LibraryDatabaseActivity extends ExpandableListActivity{

    @Override
    protected void onResume() {
        super.onResume();
        groupsCursor = dbHandler.getAllLibraryGroups();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (groupsCursor != null) {
            if (!groupsCursor.isClosed()) {
                groupsCursor.close();
            }
        }

        if (childCursor != null) {
            if (!childCursor.isClosed()) {
                childCursor.close();
            }
        }
    }

    private DBHandler dbHandler;
    private Cursor groupsCursor; // Cursor for list of groups (lists top nodes)
    private Cursor childCursor; // Cursor for list of children (child nodes)

    private MyAdapter adapter;

    //FloatingActionButton fab;
    private FABToolbarLayout layout;
    private View btnAdd;
    private View btnEdit;
    private View btnDelete;
    private View fab_toolbar;
    private boolean isExpanded = false;

    private String groupName;
    private int groupID = 0;
    private int requestCode = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.changeCursor(dbHandler.getAllLibraryGroups());
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        TextView textView = (TextView) v.findViewById(android.R.id.text1);
        Intent intent = new Intent(this, ExpandableListChild.class);
        intent.putExtra("ID", id);
        intent.putExtra("NAME", textView.getText().toString());
        startActivity(intent);
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_database_toolbar);

        dbHandler = DBHandler.getInstance(this);

        final ExpandableListView expandableListView = getExpandableListView();

        layout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        fab_toolbar = findViewById(R.id.fabtoolbar_fab);
        btnAdd = findViewById(R.id.library_database_add);
        btnEdit = findViewById(R.id.library_database_edit);
        btnDelete = findViewById(R.id.library_database_delete);

        groupsCursor = dbHandler.getAllLibraryGroups();

        adapter = new MyAdapter(this,
                groupsCursor,
                R.layout.groupview,
                new String[]{"group_name"},
                new int[]{R.id.textView3},
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"child_name", "_id"},
                new int[]{android.R.id.text1, android.R.id.text2});

        expandableListView.setAdapter(adapter);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ExpandableListView.getPackedPositionType(l) ==
                        ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    Snackbar s = Snackbar.make(view, "Long press on ID: " + i,
                            Snackbar.LENGTH_SHORT);
                    s.show();
                    expandableListView.setItemChecked(i, true);
                    Cursor e = (Cursor) expandableListView.getItemAtPosition(i);
                    groupName = e.getString(1);
                    groupID = Integer.parseInt(e.getString(0));
/*                    Cursor cursor = adapter.getGroup(i);
                    cursor.moveToFirst();
                    groupName = cursor.getString(1);
                    groupID = Integer.parseInt(cursor.getString(0));*/
                    return true;
                }
                return true;
            }
        });

        fab_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.show();
                isExpanded = true;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LibraryDatabaseActivity.this,
                        LibraryAddGroupActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar s = Snackbar.make(view,"Edit Pressed", Snackbar.LENGTH_SHORT);
                s.show();
                Intent intent = new Intent(LibraryDatabaseActivity.this,
                        LibraryAddGroupActivity.class);
                intent.putExtra("GROUP", groupName);
                intent.putExtra("ID", groupID);
                startActivityForResult(intent, requestCode);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar s = Snackbar.make(view,"Delete Pressed", Snackbar.LENGTH_SHORT);
                s.show();
            }
        });

        // Minimera toolbar när man scrollar och visar FAB igen
        expandableListView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                layout.hide();
                isExpanded = false;
            }
        });



    }

    @Override
    public void onBackPressed() {
        if (isExpanded) {
            layout.hide();
            isExpanded = !isExpanded;
        }
        else
            super.onBackPressed();
    }

    public class MyAdapter extends SimpleCursorTreeAdapter{
        public MyAdapter(Context context, Cursor cursor, int groupLayout, String[] groupFrom,
                         int[] groupTo,
                         int childLayout, String[] childFrom, int[] childTo) {

            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childFrom, childTo);
        }

        @Override
        protected Cursor getChildrenCursor(Cursor cursor) {
            childCursor = dbHandler.getAllLibraryGroupChildren(Integer.parseInt(cursor.getString(0)));
            return childCursor;
        }
    }
}
