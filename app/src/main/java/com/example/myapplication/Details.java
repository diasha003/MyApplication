package com.example.myapplication;

import android.app.Activity;
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
    private Model model;
    private ArrayList<Model> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        backButton = (Button) findViewById(R.id.Back);
        updateButton = (Button) findViewById(R.id.Update);

        idEdit = (EditText) findViewById(R.id.idEdit);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        bodyEdit = (EditText) findViewById(R.id.bodyEdit);

        model = getIntent().getParcelableExtra("model");
        idEdit.setText(model.getId());
        titleEdit.setText(model.getTitle());
        bodyEdit.setText(model.getBody());

        backButton.setOnClickListener(view -> finish());

        updateButton.setOnClickListener(view -> {
            String titleUpdate = String.valueOf(titleEdit.getText());
            String bodyUpdate = String.valueOf(bodyEdit.getText());

            model.setTitle(titleUpdate);
            model.setBody(bodyUpdate);

            Intent i2 = new Intent(Details.this, MainActivity.class);
            i2.putExtra("model", model);
            setResult(Activity.RESULT_OK, i2);
            finish();
        });
    }
}
