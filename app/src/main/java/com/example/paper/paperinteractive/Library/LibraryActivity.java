package com.example.paper.paperinteractive.Library;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.paper.paperinteractive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LibraryActivity extends ExpandableListActivity implements ExpandableListAdapter {

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mAdapter;

    private List<HashMap<String, String>> groupData = new ArrayList<>(); // List of Group Maps
    private List<List<Map<String, String>>> listOfChildGroups = new ArrayList<>();

    List<HashMap<String, String>> childData;
    HashMap<String, String> groups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        groupData.add(new HashMap<String, String>() {{
            put("ROOT_NAME", "Group1");
        }});
        groupData.add(new HashMap<String, String>(){{
            put("ROOT_NAME", "Group2");
        }});
        groupData.add(new HashMap<String, String>(){{
            put("ROOT_NAME", "Group3");
        }});

        List<Map<String, String>> childGroupForFirstGroupRow = new ArrayList<Map<String, String>>(){{
            add(new HashMap<String, String>() {{
                put("CHILD_NAME", "child in group 1");
            }});
        }};
        listOfChildGroups.add(childGroupForFirstGroupRow);

        List<Map<String, String>> childGroupForSecondGroupRow = new ArrayList<Map<String, String>>(){{
            add(new HashMap<String, String>(){{
                put("CHILD_NAME", "child in group 2");
            }});
        }};
        listOfChildGroups.add(childGroupForSecondGroupRow);

        mExpandableListView = getExpandableListView();

        mAdapter = new MyAdapter(
                this,

                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"ROOT_NAME"},
                new int[] {android.R.id.text1},

                listOfChildGroups,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{"CHILD_NAME", "CHILD_NAME"},
                new int[]{android.R.id.text1, android.R.id.text2});

        mExpandableListView.setAdapter(mAdapter);


    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childData.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }

    public class MyAdapter extends SimpleExpandableListAdapter{



        public MyAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout,
                         String[] groupFrom, int[] groupTo,
                         List<? extends List<? extends Map<String, ?>>> childData,
                         int childLayout, String[] childFrom, int[] childTo) {

            super(context, groupData, groupLayout, groupFrom, groupTo,
                    childData, childLayout, childFrom, childTo);


        }
    }
}
