package com.example.paper.paperinteractive.Library;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.paper.paperinteractive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LibraryActivity extends ExpandableListActivity {

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mAdapter;

    private List<HashMap<String, String>> groupData;
    //private List<HashMap<String, String>> childData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        HashMap<String, String> groups = new HashMap<String, String>();
        groupData = new ArrayList<>();
        groupData.add(groups);


        groups.put("G1", "Group1");
        groups.put("G2", "Group2");
        groups.put("G3", "Group3");

        List<List<HashMap<String, String>>> children = new ArrayList<>();
        List<HashMap<String, String>> childData = new ArrayList<>();

        children.add(groupData);
        HashMap<String, String> childrenMap = new HashMap<>();
        childrenMap.put("C1", "Child1");
        childrenMap.put("C2", "Child2");

        childData.add(childrenMap);



       /* List<String> group1 = new ArrayList<>();
        List<String> group2 = new ArrayList<>();
        List<String> group3 = new ArrayList<>();

        List<String> child1 = new ArrayList<>();
        child1.add("C1");
        child1.add("C2");

        List<String> child2 = new ArrayList<>();
        child2.add("C1");
        child2.add("C2");

        List<String> child3 = new ArrayList<>();
        child3.add("C1");
        child3.add("C2");

        groupData = new HashMap<>();
        groupData.put("Group1", group1);
        groupData.put("Group2", group2);
        groupData.put("Group3", group3);

        childData = new HashMap<>();
        childData.put("Child1", child1);
        childData.put("Child2", child2);
        childData.put("Child3", child3);*/

        mExpandableListView = getExpandableListView();

        String[] gKeyArray = groups.keySet().toArray(new String[groups.size()]);
        String[] cKeyArray = childrenMap.keySet().toArray(new String[children.size()]);


        mAdapter = new MyAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1,
                gKeyArray,
                new int[] {android.R.id.text1, android.R.id.text2}, children,
                android.R.layout.simple_expandable_list_item_2, cKeyArray,
                new int[]{android.R.id.text1, android.R.id.text2});

        mExpandableListView.setAdapter(mAdapter);


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
