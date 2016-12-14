package ru.pavlenty.activityfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTime = (Button) findViewById(R.id.btnTime);
        Button btnDate = (Button) findViewById(R.id.btnDate);
        Button btnExit = (Button) findViewById(R.id.btnExit);

        btnTime.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.btnTime:
                intent = new Intent("TiliTiliTralivali");
                startActivity(intent);
                break;
            case R.id.btnDate:
                intent = new Intent("ru.pavlenty.activityfilter.intent.action.showdate");
                startActivity(intent);
                break;
            case R.id.btnExit:
               // System.exit(0);
                //android.os.Process.killProcess(android.os.Process.myPid());
                //finish();
               // android.os.Process.killProcess(android.os.Process.myPid());
                //super.onDestroy();

                System.exit(0);


        }


    }

}
