package com.example.paper.paperinteractive.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.paper.paperinteractive.Library.LibraryAddGroupActivity;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChildAdded} interface
 * to handle interaction events.
 * Use the {@link LibraryAddChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryAddChildFragment extends Fragment {

    private OnChildAdded mListener;

    public LibraryAddChildFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LibraryAddChildFragment.
     */
    public static LibraryAddChildFragment newInstance() {
        return new LibraryAddChildFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_add_child, container, false);

        final EditText childText = (EditText) view.findViewById(R.id.textAddChild);
        Button btnAddChild = (Button) view.findViewById(R.id.btnAddChild);

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((LibraryAddGroupActivity) getActivity()).groupTitle.getText().toString().equals("")) {
                    String grpName = ((LibraryAddGroupActivity) getActivity())
                            .groupTitle.getText().toString();

                    LibraryChild lbChild = new LibraryChild(grpName,
                            childText.getText().toString());
                    ((LibraryAddGroupActivity) getActivity())
                            .tempGroup.getList().add(lbChild);
                    mListener.onChildAdded();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChildAdded) {
            mListener = (OnChildAdded) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChildAddedListener");
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
    public interface OnChildAdded {
        void onChildAdded();
    }
}
