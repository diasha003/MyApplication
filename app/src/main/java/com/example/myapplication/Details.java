package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Details extends AppCompatActivity {

    private Button backButton;
    private Button updateButton;

    private EditText idEdit;
    private EditText titleEdit;
    private EditText bodyEdit;

    private int index;
    private  Model model;
    private   ArrayList<Model> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        backButton = (Button) findViewById(R.id.Back);
        updateButton = (Button) findViewById(R.id.Update);

        idEdit = (EditText) findViewById(R.id.idEdit);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        bodyEdit = (EditText) findViewById(R.id.bodyEdit);



        itemList = getIntent().getParcelableArrayListExtra("KEY");
        Bundle arguments = getIntent().getExtras();
        if(arguments != null) {
            index = Integer.parseInt(arguments.getString("id"));
            model = itemList.get(index);
            idEdit.setText(model.getId());
            titleEdit.setText(model.getTitle());
            bodyEdit.setText(model.getBody());
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idUpdate = String.valueOf(idEdit.getText());
                String titleUpdate = String.valueOf(titleEdit.getText());
                String bodyUpdate = String.valueOf(bodyEdit.getText());


                System.out.println(index);
                model.setTitle(titleUpdate);
                model.setBody(bodyUpdate);


                Intent i2 = new Intent(Details.this, MainActivity.class);
                i2.putParcelableArrayListExtra("KEY", getCustomObjectList());
                i2.putExtra("id", String.valueOf(idUpdate));
                startActivity(i2);


            }
        });
    }

    private ArrayList<Model> getCustomObjectList() {
        return itemList;
    }
}
