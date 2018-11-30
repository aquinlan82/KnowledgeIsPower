package com.example.aquin.knowledgeispower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class PetScreen extends AppCompatActivity {
    ProgressBar foodBar;
    ProgressBar waterBar;
    ProgressBar exerciseBar;
    Button readButton;
    ImageView doggo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_screen);

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
            //set bars based on time of day
        }
        //web api to set image

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
