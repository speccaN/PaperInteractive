package com.example.paper.paperinteractive.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paper.paperinteractive.Child.ChildListItemActivity;
import com.example.paper.paperinteractive.R;

public class ChildListItemFragment extends Fragment {

    public ChildListItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChildListItemFragment.
     */
    public static ChildListItemFragment newInstance() {
        return new ChildListItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child_list_item, container, false);

        ChildListItemActivity parentActivity = (ChildListItemActivity) getActivity();

        TextView childName = (TextView) view.findViewById(R.id.textChildName);
        TextView childAge = (TextView) view.findViewById(R.id.textChildAge);
        childName.setText(parentActivity.child.getName());
        childAge.setText("Ålder: " + parentActivity.child.getAge());

        return view;
    }
}
