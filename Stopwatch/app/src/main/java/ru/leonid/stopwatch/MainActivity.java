package ru.leonid.stopwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    MyTimerTask timerTask;
    Timer timer;
    long currTime;
    boolean stopTimer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button)findViewById(R.id.clearBtn);
        Button btn2 = (Button)findViewById(R.id.startBtn);
        Button btn3 = (Button)findViewById(R.id.pauseBtn);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        // Initialize timer
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.schedule(timerTask, 1, 10);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case(R.id.clearBtn):
                currTime = 0;
                stopTimer = true;
                RenderTime();
                break;
            case(R.id.startBtn):
                stopTimer = false;
                break;
            case(R.id.pauseBtn):
                stopTimer = true;
                break;
        }
    }

    private void RenderTime()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");
                String time = sdf.format(new Date(currTime));

                TextView view = (TextView)findViewById(R.id.timeLabel);
                view.setText(time);
            }
        });
    }

    class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            if(!stopTimer){
                currTime += 10;
                RenderTime();
            }
        }
    }
}
