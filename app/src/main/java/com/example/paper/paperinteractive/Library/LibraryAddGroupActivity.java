package com.example.paper.paperinteractive.Library;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

//TODO Lägga till dialog att bekräfta när man går tillbaka (cancel operation)

public class LibraryAddGroupActivity extends AppCompatActivity implements
        LibraryAddChildFragment.OnChildAdded {

    private ProgressDialog progressBar;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private DefaultItemAnimator animator;
    private List<LibraryChild> mDataset;
    private RecyclerView.LayoutManager layoutManager;

    public EditText groupTitle;
    private TextView emptyText;

    public LibraryGroup tempGroup;
    List<LibraryChild> list = new ArrayList<>();

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_add_group);

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Lägger till Grupp");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setCancelable(false);

        emptyText = (TextView) findViewById(R.id.text_empty_recycler);

        tempGroup = new LibraryGroup();

        Button addGroupButton = (Button) findViewById(R.id.btnAddGroup);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_group_children);
        animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        recyclerView.setItemAnimator(animator);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        groupTitle = (EditText) findViewById(R.id.textGroupName);
        groupTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                tempGroup.setName(groupTitle.getText().toString());
            }
        });

        intent = getIntent();
        if (intent != null)
            groupTitle.setText(intent.getStringExtra("GROUP"));

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        UpdateRecyclerView();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment addChildFragment = new LibraryAddChildFragment();
        fragmentTransaction.add(R.id.child_fragment_container, addChildFragment);
        fragmentTransaction.commit();

        if (intent != null){
            GetListOperation task = new GetListOperation(list);
            task.execute(intent.getStringExtra("GROUP"));
        }

        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groupTitle.getText().toString().equals("")) {
                    progressBar.setMax(100);
                    progressBar.setProgress(0);
                    progressBar.show();
                    new LongOperation().execute();
                }
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

    private class LongOperation extends AsyncTask<Void, Integer, Void>{

        int counter = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            DBHandler db = DBHandler.getInstance(getApplicationContext());
            db.addLibraryGroup(tempGroup);
            for (LibraryChild child : tempGroup.getList()) {
                db.addLibraryGroupChild(child);
                counter++;
                publishProgress((counter*100)/tempGroup.getList().size());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
            progressBar.dismiss();
        }
    }

    private class GetListOperation extends AsyncTask<String, Integer, Void>{

        List<LibraryChild> mList;

        public GetListOperation(List<LibraryChild> list) {
            mList = list;
        }

        @Override
        protected Void doInBackground(String... strings) {
            list = new ArrayList<>();
            DBHandler db = DBHandler.getInstance(getApplicationContext());
            list = db.getLibraryGroupChildren(intent.getIntExtra("ID", 0));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDataset = list;
            adapter.notifyDataSetChanged();
            UpdateRecyclerView();
        }
    }

    // Event listener från LibraryAddChildFragment
    @Override
    public void onChildAdded() {
        mDataset = tempGroup.getList();
        adapter.addItem();
        if (recyclerView.getVisibility() == View.GONE)
            UpdateRecyclerView();
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder>{

        public MyAdapter(List<LibraryChild> myDataset) {
            mDataset = myDataset;
        }
        public MyAdapter(){mDataset = new ArrayList<>();}

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view
            View v = getLayoutInflater()
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset.get(position).getName());
        }

        public void addItem() {
            adapter.notifyItemInserted(mDataset.size());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v;
        }
    }
}
