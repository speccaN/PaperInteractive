package com.example.paper.paperinteractive;

import android.app.FragmentManager;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.paper.paperinteractive.Fragments.ChildListItemFragment;
import com.example.paper.paperinteractive.Objects.Child;

public class ChildListItemActivity extends AppCompatActivity
        implements ChildListItemFragment.OnFragmentInteractionListener{

    public Child e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list_item);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        e = (Child) bundle.getSerializable("EXTRA_CHILD");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChildListItemFragment fragment = new ChildListItemFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


/*        TextView childName = (TextView) this.findViewById(R.id.textChildName);
        childName.setText(e.getName());*/


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
