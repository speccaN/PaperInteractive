package com.example.paper.paperinteractive.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Interfaces.OnChecked;
import com.example.paper.paperinteractive.Library.SerializableSparseArray;
import com.example.paper.paperinteractive.MeetingActivity;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentCheckedListener} interface
 * to handle interaction events.
 */
public class MeetingSelectChildFragment extends Fragment implements OnChecked{

    Bundle bundle;
    LayoutInflater layoutInflater;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter adapter;
    List<LibraryChild> mChildSet;
    TextView groupName;

    private OnFragmentCheckedListener mListener;

    public MeetingSelectChildFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_select_child, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.child_recycler_view);
        bundle = getArguments();
        layoutManager = new LinearLayoutManager(getContext());
        mChildSet = new ArrayList<>();
        adapter = new MyAdapter(bundle.getInt("ID"));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnCheckedListener(this);

        groupName = (TextView) view.findViewById(R.id.textGroupName);
        groupName.setText(bundle.getString("NAME"));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCheckedListener) {
            mListener = (OnFragmentCheckedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCheckedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedListener(int groupId, int childId, boolean checked) {
        mListener.onFragmentCheckedListener(groupId, childId, checked);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentCheckedListener {
        void onFragmentCheckedListener(int groupId, int childId, boolean checked);
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder>{

        DBHandler dbHandler = DBHandler.getInstance(getContext());
        Bundle checks;

        int groupId;

        OnChecked myInterface;

        public MyAdapter(int groupId) {

            this.groupId = groupId;
            if (bundle.getBundle("CHECKS") != null){
                checks = bundle.getBundle("CHECKS");
                SerializableSparseArray array =
                        (SerializableSparseArray) checks
                                .getSerializable(((MeetingActivity)getActivity())
                                        .childName.getText().toString());
            }

            Cursor cursor = dbHandler.getAllLibraryGroupChildren(groupId);
            if (cursor.moveToFirst()){
                do {
                    LibraryChild libraryChild = new LibraryChild();
                    libraryChild.setId(Integer.parseInt(cursor.getString(0)));
                    libraryChild.setName(cursor.getString(1));
                    mChildSet.add(libraryChild);
                } while (cursor.moveToNext());
            }
            layoutInflater = LayoutInflater.from(getContext());
        }

        public void setOnCheckedListener(OnChecked myInterface){
            this.myInterface = myInterface;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.meeting_select_list_item, parent, false);
            final ViewHolder vh = new ViewHolder(view);

            vh.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    myInterface.onCheckedListener(groupId, vh.id, vh.mCheckBox.isChecked());
                }
            });

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mChildSet.get(position).getName());
            holder.id = mChildSet.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return mChildSet.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public int id;
        public CheckBox mCheckBox;
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            mTextView = (TextView) itemView.findViewById(R.id.textGroupChildName);
        }
    }
}