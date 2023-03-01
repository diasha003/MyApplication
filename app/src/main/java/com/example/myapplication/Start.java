package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {

    private Button fetchData;

    private Button exit;
    private EditText editText;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        fetchData = (Button) findViewById(R.id.fetchButton);
        exit = (Button) findViewById(R.id.Exit);
        editText = (EditText) findViewById(R.id.sizeInformation);



        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    String numberSize = String.valueOf(editText.getText());

                    Exception NumberFormatException = null;
                    if (Integer.parseInt(numberSize) > 100 || Integer.parseInt(numberSize)<0) throw NumberFormatException;


                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("number", numberSize);
                    startActivity(i);

                } catch (Exception e){
                    messageOutput(e.getMessage(), "Exception...");
                }


            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });



    }

    private void messageOutput(String message, String title){

        AlertDialog.Builder alertbox = new AlertDialog.Builder(Start.this);
        alertbox.setTitle(title);
        alertbox.setCancelable(true);

        alertbox.setMessage(message);

        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (title != "Successfully"){
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        AlertDialog alertbox2 = alertbox.create();
        alertbox2.show();

    }


}