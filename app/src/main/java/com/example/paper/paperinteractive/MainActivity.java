package com.example.paper.paperinteractive;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Objects.Child;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public DBHandler db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);

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
    }
}
