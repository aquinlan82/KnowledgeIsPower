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

public class ReadScreen extends AppCompatActivity {
    long startTime = 0;
    WebView page;
    TextView timer;
    Button doneButton;
    boolean changeStatus = false;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timer.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);

            if(minutes >= 1) {
                doneButton.setEnabled(true);
                if (changeStatus == false) {
                    updateRead();
                    changeStatus = true;
                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

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

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadScreen.this,PetScreen.class));
            }
        });

        //set timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void updateRead() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("readProgress", Context.MODE_PRIVATE);
            outputStream.write("t".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
