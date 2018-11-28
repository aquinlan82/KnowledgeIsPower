package com.example.aquin.knowledgeispower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class PetScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_screen);

        final ProgressBar foodBar = (ProgressBar) findViewById(R.id.foodProgress);
        final ProgressBar waterBar = (ProgressBar) findViewById(R.id.waterProgress);
        final ProgressBar exerciseBar = (ProgressBar) findViewById(R.id.exerciseProgress);

        final ImageView doggo = (ImageView) findViewById(R.id.goodDog);

    }
}
