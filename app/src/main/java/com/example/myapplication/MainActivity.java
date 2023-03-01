package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  {
    private ArrayList<Model> modelArrayList;
    private MyApi myApi;
    private  ListView lv;
    private String BaseURL = "https://jsonplaceholder.typicode.com/";

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
        System.out.println("On create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        modelArrayList = new ArrayList<Model>();

        exitButton = (Button) findViewById(R.id.exitButton);
        saveButton = (Button) findViewById(R.id.saveButton);



        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Model model = result.getData().getParcelableExtra("model");
                    modelArrayList.replaceAll(m -> m.getId().equals(model.getId()) ? model : m);
                    custom.notifyDataSetChanged();
                }
        );

        Bundle arguments = getIntent().getExtras();
        if(arguments != null){

            if (arguments.get("number") != null) {
                numberObject = Integer.parseInt(arguments.getString("number"));
                displayRetrofitData(numberObject);
            }

        }

        exitButton.setOnClickListener(view -> finishAffinity());

        saveButton.setOnClickListener(view -> {
            Gson gson = new Gson();
            String jsonString = gson.toJson(modelArrayList);

            try(FileOutputStream fileOutputStream =
                        getApplicationContext().openFileOutput("data.txt", Context.MODE_PRIVATE)) {
                fileOutputStream.write(jsonString.getBytes());
                messageOutput("Data is saved to the file data.txt", "Successfully");

            } catch (Exception e) {
                messageOutput(e.getMessage(), "Exception...");
            }

        });

      lv.setOnItemClickListener((adapterView, view, i, l) -> {
          Intent i1 = new Intent(MainActivity.this, Details.class);
          i1.putExtra("model", (Parcelable)modelArrayList.get(i));
          activityResultLauncher.launch(i1); //
      });

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

                modelArrayList.clear();
                modelArrayList.addAll(response.body().subList(0, size)); //ответ в виде строки
                custom = new Custom(modelArrayList, MainActivity.this, R.layout.singleview);
                lv.setAdapter(custom);
            }

            @Override
            public void onFailure(Call<ArrayList<Model>> call, Throwable t) {
                messageOutput("Failled to load data", "Exception...");
                //Toast.makeText(MainActivity.this, "Failled to load data", Toast.LENGTH_SHORT).show();
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


}