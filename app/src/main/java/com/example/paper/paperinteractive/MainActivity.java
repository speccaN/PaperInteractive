package com.example.paper.paperinteractive;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.paper.paperinteractive.Library.LibraryDatabaseActivity;
import com.example.paper.paperinteractive.Objects.Child;
import com.example.paper.paperinteractive.Objects.LibraryChild;
import com.example.paper.paperinteractive.Objects.LibraryGroup;

import java.util.List;

//TODO Skapa en LoadingScreen
public class MainActivity extends AppCompatActivity {

    public DBHandler db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLibrary = (Button) findViewById(R.id.btnLibrary);
        Button btnMeeting = (Button) findViewById(R.id.btnMeeting);
        Button btn3 = (Button) findViewById(R.id.btnNewChild);
        Button btnPdf = (Button) findViewById(R.id.btnViewPdf);
        final Button btnChildList = (Button) findViewById(R.id.btnChildList);

        TextView header = (TextView) findViewById(R.id.textHeader);

        header.setText(R.string.welcome);
        header.setGravity(Gravity.CENTER);

        new AddTestItems().execute();

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

        btnMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
                startActivity(intent);
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
                startActivity(intent);
            }
        });
    }

    private class AddTestItems extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            db = DBHandler.getInstance(getApplicationContext());

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
                db.addLibraryGroup(new LibraryGroup("Överkropp"));
                db.addLibraryGroup(new LibraryGroup("Underkropp"));
                db.addLibraryGroup(new LibraryGroup("Ben"));

                db.addLibraryGroupChild(new LibraryChild("Överkropp", "Övning1"));
                db.addLibraryGroupChild(new LibraryChild("Ben", "Övning3"));
            }

            //Read all children
            Log.d("Reading: ", "Reading all children..");
            List<Child> children = db.getAllChildren();

            for(Child child : children){
                String log = "Id: " + child.getId() + " ,Name: " + child.getName() + " ,Age: " + child.getAge();
                // write children to log
                Log.d("Child: : ", log);
            }
            return null;
        }
    }
}
