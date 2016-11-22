package com.example.paper.paperinteractive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Objects.Child;

import java.util.ArrayList;
import java.util.List;

public class ChildListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);

        db = new DBHandler(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.child_recycler_view);

        ArrayList<Child> myDataset = new ArrayList<Child>();

        myDataset = db.getAllChildren();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Child> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder{
            // each data item is just a string in this case
            public TextView mChildName;
            public TextView mChildAge;
            public ViewHolder(View v){
                super(v);
                mChildName = (TextView) v.findViewById(R.id.textName);
                mChildAge = (TextView) v.findViewById(R.id.textAge);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Clicked: " + getPosition(),
                                Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("EXTRA_CHILD", mDataset.get(getAdapterPosition()));

                        Intent intent = new Intent(getApplicationContext(),
                                ChildListItemActivity.class);
                        intent.setClass(getApplicationContext(), ChildListItemActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<Child> myDataset){
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            v.findViewById(R.id.textName);
            v.setPadding(10, 10, 10, 10);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mChildName.setText(mDataset.get(position).getName());
            holder.mChildAge.setText("Ålder: " + mDataset.get(position).getAge());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount(){
            return mDataset.size();
        }
    }
}
