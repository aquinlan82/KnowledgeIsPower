package com.example.aquin.knowledgeispower;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class PetScreen extends AppCompatActivity {
    ProgressBar foodBar;
    ProgressBar waterBar;
    ProgressBar exerciseBar;
    Button readButton;
    ImageView doggo;

    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_screen);

        requestQueue = Volley.newRequestQueue(this);

        foodBar = (ProgressBar) findViewById(R.id.foodProgress);
        waterBar = (ProgressBar) findViewById(R.id.waterProgress);
        exerciseBar = (ProgressBar) findViewById(R.id.exerciseProgress);
        readButton = (Button) findViewById(R.id.readButton);
        doggo = (ImageView) findViewById(R.id.goodDog);

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PetScreen.this, ReadScreen.class));
            }
        });
        if(readProgress()) {
            foodBar.setProgress(10);
            waterBar.setProgress(10);
            exerciseBar.setProgress(10);
        } else {
            Random rand = new Random();
            waterBar.setProgress(rand.nextInt(5));
            foodBar.setProgress(rand.nextInt(5));
            exerciseBar.setProgress(rand.nextInt(5));
        }
        startAPICall();
    }

    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://dog.ceo/api/breeds/image/random",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                String picture = (String) response.get("message");
                                loadImage(picture);
                            } catch (Exception e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                        }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
        }
    }

    public void loadImage(String url) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        doggo.setImageBitmap(response);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(imageRequest);
    }

    private boolean readProgress() {
        try {
            FileInputStream in = openFileInput("readProgress");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            if (sb.toString().equals("t")) {
                return true;
            }
        } catch (Exception e) {
            updateReadProgress();
            e.printStackTrace();
            readProgress();
        }
        return false;
    }
    private void updateReadProgress() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("readProgress", Context.MODE_PRIVATE);
            outputStream.write("f".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
