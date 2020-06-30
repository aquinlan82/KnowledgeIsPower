package com.example.aquin.knowledgeispower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;

/**
 * Screen to read a random wikipedia article
 */
public class ReadScreen extends AppCompatActivity {
    long startTime = 0;  //time read screen opened
    WebView page;    //wikipedia article
    TextView timer;     //displays how much time has passed
    Button doneButton;    //goes to dog screen
    boolean changeStatus = false;    //checks if streak goal met

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        //Checks if time goal is met and updates timer
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timer.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);

            if(seconds > 30 || minutes >= 1) {
                doneButton.setEnabled(true);
                doneButton.setVisibility(View.VISIBLE);
                if (changeStatus == false) {
                    updateRead();
                    changeStatus = true ;
                }
            }
        }
    };

    //Pauses timer if leaving app
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    //Loads gui
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_screen);

        page = (WebView) findViewById(R.id.webView);
        timer = (TextView) findViewById(R.id.timer);
        doneButton = (Button) findViewById(R.id.doneButton);

        page.getSettings().setJavaScriptEnabled(true);
        page.setWebViewClient(new WebViewClient());
        page.loadUrl("http://en.wikipedia.org/wiki/Special:Random");

        //Goes to Pet screen
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadScreen.this,PetScreen.class));
            }
        });

        //Set timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    /*
        Sets 'readProgress' file to true when goal met
     */
    private void updateRead() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("readProgress", Context.MODE_PRIVATE);
            outputStream.write("t".getBytes());
            outputStream.close();
        } catch (Exception e) {}

    }
}
