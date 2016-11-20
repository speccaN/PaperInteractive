package com.example.paper.paperinteractive;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler db = new DBHandler(this);

        // Test items
        if(db.getChildrenCount() == 0) {
            Log.d("Insert: ", "Inserting ..");
            db.addChild(new Child("Göte Börjesson", 5));
            db.addChild(new Child("Anders Andersson", 4));
        }

        //Read all children
        Log.d("Reading: ", "Reading all children..");
        List<Child> children = db.getAllChildren();

        for(Child child : children){
            String log = "Id: " + child.getId() + " ,Name: " + child.getName() + " ,Age: " + child.getAge();
            // write children to log
            Log.d("Child: : ", log);
        }

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
                if(findViewById(R.id.fragment_container) != null) {

                    if(savedInstanceState != null){
                        return;
                    }

                    CreateChildFragment childFragment = new CreateChildFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.createChildFragment, childFragment);
                    ft.addToBackStack("childFragment");
                    ft.commit();
                }
            }
        });
    }
}
