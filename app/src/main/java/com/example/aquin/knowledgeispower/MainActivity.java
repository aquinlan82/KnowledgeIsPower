package com.example.aquin.knowledgeispower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button petButton;
    Button readButton;
    TextView streakView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petButton = (Button) findViewById(R.id.petButton);
        readButton = (Button) findViewById(R.id.readButton);
        streakView = (TextView) findViewById(R.id.streak);

        updateStreak();
        readStreak();
        updateDateVisited();

        petButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PetScreen.class));
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReadScreen.class));
            }
        });
    }

    private void updateStreak() {
        String lastVisit = readDateVisited();
        String fileContents=" ";
        FileOutputStream outputStream;
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date lastVisitAsDate = format.parse(lastVisit);
            Date todayAsDate = Calendar.getInstance().getTime();

            long difference = Math.abs(todayAsDate.getTime() - lastVisitAsDate.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            if (differenceDates == 0) {
                return;
            }
            if (differenceDates == (long) 1) {
                updateReadProgress();
                fileContents = (Integer.parseInt(readStreak()) + 1) + "";
            }
            if (differenceDates > 1) {
                updateReadProgress();
                fileContents = 0+"";
            }

        } catch (Exception e) {

        }
        try {
            outputStream = openFileOutput("streak", Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String readStreak() {
        try {
            FileInputStream in = openFileInput("streak");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            streakView.setText(streakView.getText() + sb.toString() + " days in a row!");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void updateDateVisited() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String today = df.format(c);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput("lastVisited", Context.MODE_PRIVATE);
            outputStream.write(today.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private String readDateVisited() {
        Log.d("MAIN", "readDateCall");
        try {
            FileInputStream in = openFileInput("lastVisited");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            if (sb.toString().equals("")){
                updateDateVisited();
                readDateVisited();
            }
            return sb.toString();
        } catch (Exception e) {
            updateDateVisited();
            e.printStackTrace();
        }
        return "";
    }
}

