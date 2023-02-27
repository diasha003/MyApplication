package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  {


    private ArrayList<Model>modelArrayList;
    private MyApi myApi;
    private  ListView lv;
    private String BaseURL = "https://jsonplaceholder.typicode.com/";

    //private Button fetchDataButton;
    private Button saveButton;
    private Button exitButton;
    private int numberObject;

    private  Custom custom;

    public ArrayList<Model> getModel(){
        return modelArrayList;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        modelArrayList = new ArrayList<Model>();

        exitButton = (Button) findViewById(R.id.exitButton);
        saveButton = (Button) findViewById(R.id.saveButton);



        Bundle arguments = getIntent().getExtras();
        if(arguments != null){

            if (arguments.get("number") != null) {
                numberObject = Integer.parseInt(arguments.getString("number"));
                displayRetrofitData(numberObject);
            } else if (arguments.get("id") != null){

            }

        }


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(modelArrayList);

                try(FileOutputStream fileOutputStream =
                            getApplicationContext().openFileOutput("data.txt", Context.MODE_PRIVATE)) {
                    fileOutputStream.write(jsonString.getBytes());
                    messageOutput("Data is saved to the file data.txt", "Successfully");

                } catch (Exception e) {
                    messageOutput(e.getMessage(), "Exception...");
                }

            }
        });


      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


              Intent i1 = new Intent(MainActivity.this, Details.class);
              i1.putParcelableArrayListExtra("KEY", getCustomObjectList());
              i1.putExtra("id", String.valueOf(i));
              startActivity(i1);

              //Toast.makeText (getApplicationContext(), TextToast, Toast.LENGTH_SHORT).show();
          }
      });

    }


    private ArrayList<Model> getCustomObjectList() {
        return modelArrayList;
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private void displayRetrofitData(int size) {

        Retrofit retrofit = new Retrofit.Builder() // запрос к серверу
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi = retrofit.create(MyApi.class);
        Call<ArrayList<Model>> arrayListCall = myApi.callModel(); //получение конкретного запроса
        arrayListCall.enqueue(new Callback<ArrayList<Model>>() { //асинхронный метод
            @Override
            public void onResponse(Call<ArrayList<Model>> call, Response<ArrayList<Model>> response) {

                modelArrayList = response.body(); //ответ в виде строки

                ArrayList<Model> newArray = new ArrayList<Model>(modelArrayList.subList(0, size));

                for(int i=0; i<newArray.size(); i++){
                    custom = new Custom(newArray, MainActivity.this, R.layout.singleview);
                    lv.setAdapter(custom);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Model>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failled to load data", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void messageOutput(String message, String title){

        AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
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

    private void updateObject (String id){
        System.out.println(modelArrayList.get(Integer.parseInt(id)));
    }

}