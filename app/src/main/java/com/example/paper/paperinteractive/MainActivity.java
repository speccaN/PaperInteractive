package com.example.paper.paperinteractive;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.paper.paperinteractive.Child.ChildActivity;
import com.example.paper.paperinteractive.Child.ChildListActivity;
import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Library.LibraryActivity;
import com.example.paper.paperinteractive.Library.LibraryDatabaseActivity;
import com.example.paper.paperinteractive.Objects.Child;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.Objects.LibraryGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    public DBHandler db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DBHandler.getInstance(this);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // Test items
        if(db.getChildrenCount() == 0) {
            Log.d("Insert: ", "Inserting ..");
            db.addChild(new Child("Göte Börjesson", "5"));
            db.addChild(new Child("Anders Andersson", "4"));
            db.addChild(new Child("Test Test1", String.valueOf((int)Math.ceil(Math.random() * 7))));
            db.addChild(new Child("Test Test2", String.valueOf((int)Math.ceil(Math.random() * 7))));
            db.addChild(new Child("Test Test3", String.valueOf((int)Math.ceil(Math.random() * 7))));
            db.addChild(new Child("Test Test4", String.valueOf((int)Math.ceil(Math.random() * 7))));
            db.addChild(new Child("Test Test5", String.valueOf((int)Math.ceil(Math.random() * 7))));
        }

        if (db.getLibraryGroupCount() == 0){
            Log.d("Insert: ", "Inserting Group ..");
            db.addGroup(new LibraryGroup("Överkropp"));
            db.addGroup(new LibraryGroup("Underkropp"));
            db.addGroup(new LibraryGroup("Ben"));

            db.addGroupChild(new LibraryChild("Överkropp", "Övning1"));
            db.addGroupChild(new LibraryChild("Ben", "Övning3"));
        }

        //Read all children
        Log.d("Reading: ", "Reading all children..");
        List<Child> children = db.getAllChildren();

        for(Child child : children){
            String log = "Id: " + child.getId() + " ,Name: " + child.getName() + " ,Age: " + child.getAge();
            // write children to log
            Log.d("Child: : ", log);
        }

        Button btnLibrary = (Button) findViewById(R.id.btnLibrary);
        Button btn2 = (Button) findViewById(R.id.btnMeeting);
        Button btn3 = (Button) findViewById(R.id.btnNewChild);
        final Button btnChildList = (Button) findViewById(R.id.btnChildList);

        TextView header = (TextView) findViewById(R.id.textHeader);

        header.setText(R.string.welcome);
        header.setGravity(Gravity.CENTER);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
                intent.setClass(getApplicationContext(), ChildActivity.class);
                startActivity(intent);
            }
        });

        btnChildList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChildListActivity.class);
                intent.setClass(getApplicationContext(), ChildListActivity.class);
                startActivity(intent);
            }
        });

        btnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LibraryDatabaseActivity.class);
                intent.setClass(getApplicationContext(), LibraryDatabaseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }
}
