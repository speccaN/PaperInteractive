package com.example.paper.paperinteractive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CreateChildFragment extends Fragment {

    private String textToDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_create_child, container, false);

        textToDisplay = new String(new char[450]).replace("\0", "The quick brown fox jumps over the lazy dog.");
        View view = inflater.inflate(R.layout.fragment_create_child, container, false);
        TextView textView = (TextView)view.findViewById(R.id.textView1);
        textView.setText(textToDisplay);

        return view;
    }

}
