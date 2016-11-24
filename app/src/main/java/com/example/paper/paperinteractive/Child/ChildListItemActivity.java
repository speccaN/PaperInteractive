package com.example.paper.paperinteractive.Child;

import android.app.FragmentManager;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.paper.paperinteractive.Fragments.ChildListItemFragment;
import com.example.paper.paperinteractive.Objects.Child;
import com.example.paper.paperinteractive.R;

public class ChildListItemActivity extends AppCompatActivity
        implements ChildListItemFragment.OnFragmentInteractionListener{

    public Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list_item); // Innehåller Fragment Container

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        child = (Child) bundle.getSerializable("EXTRA_CHILD");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // TODO Skicka med child som extra till fragmentet (för att förhindra public variabel)
        ChildListItemFragment fragment = new ChildListItemFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


/*        TextView childName = (TextView) this.findViewById(R.id.textChildName);
        childName.setText(child.getName());*/


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
