package com.example.ross.test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtJson;
    RecyclerViewAdapter adapter;

    List<JsonClass> yourList;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //txtJson = findViewById(R.id.tvJson);

        new JsonTask().execute("http://jsonplaceholder.typicode.com/albums/1/photos");


        //set up recycler view
        recyclerView = findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, yourList);
        //adapter.setOnClickListener((View.OnClickListener) this);
        recyclerView.setAdapter(adapter);


    }

    private class JsonTask extends AsyncTask<String, String, String>{

        protected void onPreExecute(){
            super.onPreExecute();

        }

        protected String doInBackground(String... params){

            HttpURLConnection connection = null;
            BufferedReader reader = null;


            URL url = null;
            InputStream in;

            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) !=null){
                    buffer.append(line+"\n");
                    //Log.d("Response: ", "> "+line); // response
                }
                return buffer.toString();


            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e) {
                e.printStackTrace();

            }finally {
                if(connection !=null){
                    connection.disconnect();
                }

                try{
                    if (reader !=null) {
                        reader.close();

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            //txtJson.setText(result);

            Type listType = new TypeToken<List<JsonClass>>() {}.getType();

            List<JsonClass> yourList = new Gson().fromJson(result,listType);


            adapter = new RecyclerViewAdapter(MainActivity.this, yourList);

            recyclerView.setAdapter(adapter);



        }


    }

}
