package com.example.paper.paperinteractive.Library;

import android.app.ExpandableListActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Fragments.LibraryAddChildFragment;
import com.example.paper.paperinteractive.Fragments.LibraryAddGroupFragment;
import com.example.paper.paperinteractive.R;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

public class LibraryDatabaseActivity extends ExpandableListActivity implements
        LibraryAddGroupFragment.OnGroupAddedListener,
        LibraryAddChildFragment.OnChildAdded {

    DBHandler dbHandler;
    Cursor groupsCursor; // Cursor for list of groups (lists top nodes)
    Cursor childCursor;

    MyAdapter adapter;

    FloatingActionButton fab;
    private FABToolbarLayout layout;
    private View btnAdd;
    private View btnEdit;
    private View btnDelete;
    private View fab_toolbar;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_database_toolbar);

        dbHandler = new DBHandler(this);

        ExpandableListView expandableListView = getExpandableListView();

        layout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        fab_toolbar = findViewById(R.id.fabtoolbar_fab);
        btnAdd = findViewById(R.id.library_database_add);
        btnEdit = findViewById(R.id.library_database_edit);
        btnDelete = findViewById(R.id.library_database_delete);

        fab = (FloatingActionButton) findViewById(R.id.fabtoolbar_fab);

        groupsCursor = dbHandler.getAllGroups();

        groupsCursor.moveToFirst();
        adapter = new MyAdapter(this,
                groupsCursor,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"group_name"},
                new int[]{android.R.id.text1},
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"child_name", "_id"},
                new int[]{android.R.id.text1, android.R.id.text2});

        expandableListView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.show();
                isExpanded = true;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LibraryAddGroupFragment fragment = new LibraryAddGroupFragment();
                fragmentTransaction.add(R.id.fragment_group_container, fragment);
                fragmentTransaction.commit();
                fab.setVisibility(View.GONE);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar s = Snackbar.make(view,"Edit Pressed", Snackbar.LENGTH_SHORT);
                s.show();

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

    @Override
    public void onChildAdded() {
        adapter.notifyDataSetChanged(true);
    }

    @Override
    public void onGroupAdded() {
        adapter.changeCursor(dbHandler.getAllGroups());
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
            childCursor = dbHandler.getAllGroupChildren(Integer.parseInt(cursor.getString(0)));
            return childCursor;
        }
    }
}
