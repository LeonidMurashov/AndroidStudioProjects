package ru.pavlenty.activityfilter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityDate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_date);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            String date = sdf.format(new Date(System.currentTimeMillis()));

            TextView tvDate = (TextView) findViewById(R.id.tvDate);
            tvDate.setText(date);
    }
}

