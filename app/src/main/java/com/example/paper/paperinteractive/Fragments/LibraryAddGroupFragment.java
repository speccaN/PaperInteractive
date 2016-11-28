package com.example.paper.paperinteractive.Fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.paper.paperinteractive.Library.LibraryAddGroupActivity;
import com.example.paper.paperinteractive.R;

public class LibraryAddGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btnAddGroup;
    EditText groupName;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryAddGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryAddGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryAddGroupFragment newInstance(String param1, String param2) {
        LibraryAddGroupFragment fragment = new LibraryAddGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_add_group, container, false);

        groupName = (EditText) view.findViewById(R.id.textGroupName);

        btnAddGroup = (Button) view.findViewById(R.id.btnAddGroup);

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Intent intent = new Intent(getContext(), LibraryAddGroupActivity.class);
                intent.putExtra("GROUP_NAME", groupName.getText().toString());

                if(intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intent);
            }
        });

        return view;
    }
}
