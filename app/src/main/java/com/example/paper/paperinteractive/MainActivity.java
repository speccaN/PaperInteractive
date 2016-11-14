package com.example.paper.paperinteractive;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.btnLibrary);
        Button btn2 = (Button) findViewById(R.id.btnMeeting);
        Button btn3 = (Button) findViewById(R.id.btnNewChild);

        final FragmentManager fm = getSupportFragmentManager();

        TextView header = (TextView) findViewById(R.id.textHeader);

        header.setText("Välkommen!");
        header.setGravity(Gravity.CENTER);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MeetingActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fm.getBackStackEntryCount() == 0) {
                    CreateChildFragment createChildFragment = new CreateChildFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.fragment_container, createChildFragment);
                    ft.addToBackStack("createChildFragment");
                    ft.commit();
                }
            }
        });
    }
}
