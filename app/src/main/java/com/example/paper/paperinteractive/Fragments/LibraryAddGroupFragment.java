package com.example.paper.paperinteractive.Fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Library.LibraryDatabaseActivity;
import com.example.paper.paperinteractive.Objects.LibraryGroup;
import com.example.paper.paperinteractive.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGroupAddedListener} interface
 * to handle interaction events.
 * Use the {@link LibraryAddGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryAddGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btnAddGroup;
    Button btnAddChild;
    EditText groupName;
    EditText childName;
    View childContainer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnGroupAddedListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_add_group, container, false);

        groupName = (EditText) view.findViewById(R.id.textGroupName);


        btnAddGroup = (Button) view.findViewById(R.id.btnAddGroup);
        btnAddChild = (Button) view.findViewById(R.id.btnAddChild);
        btnAddChild.setVisibility(View.GONE);

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHandler db = new DBHandler(getContext());
                LibraryGroup group = new LibraryGroup(groupName.getText().toString());
                db.addGroup(group);
                mListener.onGroupAdded();
                btnAddGroup.setEnabled(false);
                btnAddChild.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager)
                            getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                groupName.setEnabled(false);
            }
        });

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new LibraryAddChildFragment();
                Bundle bundle = new Bundle();
                bundle.putString("GROUP_NAME", groupName.getText().toString());
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.fragment_child_container, fragment);
                fragmentTransaction.commit();

                btnAddChild.setEnabled(false);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onGroupAdded() {
        if (mListener != null) {
            mListener.onGroupAdded();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGroupAddedListener) {
            mListener = (OnGroupAddedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGroupAddedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnGroupAddedListener {
        // TODO: Update argument type and name
        void onGroupAdded();
    }
}
