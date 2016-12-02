package com.example.paper.paperinteractive;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Fragments.MeetingSelectChildFragment;
import com.example.paper.paperinteractive.Fragments.MeetingSelectLibraryFragment;
import com.example.paper.paperinteractive.Library.SerializableSparseArray;
import com.example.paper.paperinteractive.Objects.Child;
import com.example.paper.paperinteractive.Objects.CustomSpinnerAdapter;
import com.example.paper.paperinteractive.Objects.LibraryChild;

import java.util.ArrayList;
import java.util.List;

public class MeetingActivity extends AppCompatActivity
        implements MeetingSelectChildFragment.OnFragmentCheckedListener{

    Spinner spinner;
    CustomSpinnerAdapter adapter;
    Button btnTempAdd;

    RecyclerView recyclerView;
    DefaultItemAnimator animator;
    LinearLayoutManager linearLayoutManager;
    CustomRecyclerAdapter customRecyclerAdapter;
    List<LibraryChild> mDataset;

    public TextView childName;
    TextView childAge;
    TextView tempEmptyText;

    DBHandler dbHandler;

    Bundle checks = new Bundle();
    Integer groupId = null;
    Integer childId = null;

    Fragment fragment = new MeetingSelectLibraryFragment();
    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Skapa Möte");
        setSupportActionBar(toolbar);

        dbHandler = DBHandler.getInstance(this);

        spinner = (Spinner) findViewById(R.id.spinnerChild);
        btnTempAdd = (Button) findViewById(R.id.btnTempAdd);
        tempEmptyText = (TextView) findViewById(R.id.textEmptyList);
        childName = (TextView) findViewById(R.id.textChildName);
        childAge = (TextView) findViewById(R.id.textChildAge);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_group_children);
        animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        recyclerView.setItemAnimator(animator);
        linearLayoutManager = new LinearLayoutManager(this);
        mDataset = new ArrayList<>();
        customRecyclerAdapter = new CustomRecyclerAdapter();
        recyclerView.setAdapter(customRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        SpinnerMethod();
        UpdateRecyclerView();

        //TODO Fixa layouten då fragment glider in
        btnTempAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentManager.getBackStackEntryCount() == 0) {
                    fragment.setArguments(checks);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_bottom,
                            0, 0, R.animator.slide_out_bottom);
                    fragmentTransaction.addToBackStack("Fragment");
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    //Stänger fragment om ett finns öppet, annars stäng aktivitet
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() != 0){
            fragmentManager.popBackStack();
        } else
            super.onBackPressed();
    }

    // Spinner instantiering
    private void SpinnerMethod() {
        final Child[] children = dbHandler.getAllChildren()
                .toArray(new Child[dbHandler.getChildrenCount()]);

        adapter = new CustomSpinnerAdapter(this, R.layout.spinner_layout, children);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                childName.setText(((Child) adapterView.getItemAtPosition(position)).getName());
                childAge.setText(String.format("%1$s %2$s",
                        getString(R.string.age),
                        ((Child) adapterView.getItemAtPosition(position)).getAge()));

                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
                    fragmentManager.popBackStack();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void SaveChecks(int childId, int groupId, int libraryChildId){
        if (checks.getSerializable(childName.getText().toString()) != childName.getText().toString()) {
            // Outer
            // KEY: Child = Position in Spinner
            // VALUE: SparseArray<INTEGER, INTEGER>
            SerializableSparseArray<SparseIntArray> outer =
                    new SerializableSparseArray<SparseIntArray>();

            // Inner : (Initial capacity of how many children (performance))
            // KEY: Group id
            // VALUE: Child id
            SparseIntArray inner = new SparseIntArray(adapter.getCount());

            // Adding
            inner.append(groupId, libraryChildId);
            outer.append(childId, inner);
            checks.putSerializable(childName.getText().toString(), outer);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_confirm:
                //TODO Do something
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentCheckedListener(int groupId, int childId, boolean checked) {
        LibraryChild tempLibraryChild = dbHandler.getLibraryGroupChild(childId);
        Child tempChild = (Child) spinner.getSelectedItem();
        if (checked) {
            SaveChecks(tempChild.getId(), groupId, childId);
            dbHandler.addLccMeeting(tempChild, tempLibraryChild);
            mDataset.add(tempLibraryChild);
            customRecyclerAdapter.addItem();
            UpdateRecyclerView();
        } else {
            dbHandler.removeLccMeeting(tempChild.getId(), tempLibraryChild.getId());
            UpdateRecyclerView();
        }
    }

    private void UpdateRecyclerView() {
        if (mDataset.size() == 0){
            btnTempAdd.setVisibility(View.VISIBLE);
            tempEmptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            btnTempAdd.setVisibility(View.GONE);
            tempEmptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private class CustomRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);

            view.setPadding(10, 10, 10, 10);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mDataset.get(position).getName());
        }

        public void addItem(){
            customRecyclerAdapter.notifyItemInserted(mDataset.size());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}
