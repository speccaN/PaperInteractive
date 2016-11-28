package com.example.paper.paperinteractive.Library;

import android.app.Fragment;
import android.content.Intent;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Fragments.LibraryAddChildFragment;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.Objects.LibraryGroup;
import com.example.paper.paperinteractive.R;

import java.util.ArrayList;
import java.util.List;

public class LibraryAddGroupActivity extends AppCompatActivity implements
        LibraryAddChildFragment.OnChildAdded {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<LibraryChild> mDataset;
    RecyclerView.LayoutManager layoutManager;

    public EditText groupTitle;
    TextView emptyText;

    public LibraryGroup tempGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_add_group);

        emptyText = (TextView) findViewById(R.id.text_empty_recycler);

        tempGroup = new LibraryGroup();

        Button addGroupButton = (Button) findViewById(R.id.btnAddGroup);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_group_children);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        groupTitle = (EditText) findViewById(R.id.textGroupName);
        groupTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                tempGroup.setName(groupTitle.getText().toString());
            }
        });

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        UpdateRecyclerView();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment addChildFragment = new LibraryAddChildFragment();
        fragmentTransaction.add(R.id.child_fragment_container, addChildFragment);
        fragmentTransaction.commit();

        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHandler db = new DBHandler(getApplicationContext());
                db.addGroup(tempGroup);
                for(LibraryChild child : tempGroup.getList()){
                    db.addGroupChild(child);
                }
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void UpdateRecyclerView() {
        if(adapter.getItemCount() == 0){
            recyclerView.setVisibility(View.GONE);
            emptyText.setText("Listan är tom! Lägg till övningar");
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }

    // Event listener från LibraryAddChildFragment
    @Override
    public void onChildAdded() {
        mDataset = tempGroup.getList();
        adapter.notifyDataSetChanged();
        if (recyclerView.getVisibility() == View.GONE)
            UpdateRecyclerView();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public ViewHolder(TextView v) {
                super(v);
                mTextView = v;
            }
        }


        public MyAdapter(List<LibraryChild> myDataset) {
            mDataset = myDataset;
        }
        public MyAdapter(){mDataset = new ArrayList<>();}



        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            ViewHolder vh = new ViewHolder((TextView) v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
