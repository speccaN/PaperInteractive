package com.example.paper.paperinteractive.Fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Objects.LibraryGroup;
import com.example.paper.paperinteractive.R;

import java.util.List;

public class MeetingSelectLibraryFragment extends Fragment {

    List<LibraryGroup> mGroupSet;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    MyAdapter adapter;
    LayoutInflater layoutInflater;
    DBHandler dbHandler;
    Bundle checks;

    public MeetingSelectLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_select_library, container, false);

        checks = getArguments();
        dbHandler = DBHandler.getInstance(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder>{

        public MyAdapter() {
            mGroupSet = dbHandler.getAllLibraryGroupsList();
            layoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            final ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MeetingSelectChildFragment fragment = new MeetingSelectChildFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBundle("CHECKS", checks);
                    bundle.putInt("ID", dbHandler.getLibraryGroupFromId(vh.id).getId());
                    bundle.putString("NAME", dbHandler.getLibraryGroupFromId(vh.id).getGroupName());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_in_bottom,
                            0, 0, R.animator.slide_out_bottom);
                    fragmentTransaction.addToBackStack("Fragment_child");
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                }
            });

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.dbHandler = dbHandler;
            holder.id = mGroupSet.get(position).getId();
            holder.mTextView.setText(mGroupSet.get(position).getGroupName());
        }

        @Override
        public int getItemCount() {
            return mGroupSet.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        DBHandler dbHandler;
        public int id;
        public TextView mTextView;
        public ViewHolder(final View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}
