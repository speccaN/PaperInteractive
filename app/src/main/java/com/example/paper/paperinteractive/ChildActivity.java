package com.example.paper.paperinteractive;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Objects.Child;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ChildActivity extends AppCompatActivity {

    Spinner spinner;
    DBHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        db = new DBHandler(this);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        final EditText child_name = (EditText) findViewById(R.id.child_name);
        spinner = (Spinner) findViewById(R.id.spinner);


        SpinnerMethod();

        // TODO Förhindra att lägga till tomt barn
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Child child = new Child(child_name.getText().toString(), spinner.getSelectedItem().toString());
                db.addChild(child);
                Log.i("Child: ", child.getName() + " Created");
                finish();

            }
        });



    }

    private void SpinnerMethod() {



        List<Integer> spinnerArray = new ArrayList<>();

        for (int i = 0; i < 15; i++){
            spinnerArray.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(adapter);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }
    }
}
