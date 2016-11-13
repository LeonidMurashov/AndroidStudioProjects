package com.example.testapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    int score = 0;
    float bx = 100, by = 0, t = 0;
    int speed = 1, record;
    Button button;
    MyTimerTask timerTask;
    Timer timer;
    boolean clockwise = true;
    String FILENAME = "ScoreFile";
    FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Random random = new Random();

        // Getting records from file
        record = ReadRecord();
        RewriteRecord();

        // Initialize timer
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.schedule(timerTask, 1, 10);

        // Initialize buttons and listeners
        button = (Button)findViewById(R.id.button3);
        Button hardBtn = (Button)findViewById(R.id.harderBtn);
        Button easBtn = (Button)findViewById(R.id.easBtn);
        OnClickListener onClickListener = new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                score += Math.pow(10, speed - 1);
                TextView tw = (TextView) findViewById(R.id.scoreView);
                tw.setText("Очки: " + String.valueOf(score));
                clockwise = random.nextBoolean();

                // Checking for new record
                if (score > record)
                {
                    record = score;
                    WriteRecord(record);
                    RewriteRecord();
                }

                // Adding random sizing of btn
                if(score > 200)
                {
                    button.setHeight(random.nextInt(200) + 50);
                    button.setWidth(random.nextInt(200) + 50);
                }
            }
        };
        button.setOnClickListener(onClickListener);

        OnClickListener onClickListener1 = new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(speed != 3)
                {
                    speed++;
                    RewriteHardness();
                }
            }
        };
        hardBtn.setOnClickListener(onClickListener1);

        OnClickListener onClickListener2 = new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(speed != 1)
                {
                    speed--;
                    RewriteHardness();
                }
            }
        };
        easBtn.setOnClickListener(onClickListener2);

        button.setHeight(130);
        RewriteHardness();
    }

    private void RewriteRecord()
    {
        TextView rw = (TextView) findViewById(R.id.recordView);
        rw.setText("Рекорд: " + String.valueOf(record));
    }

    private void RewriteHardness()
    {
        TextView sw = (TextView) findViewById(R.id.hardnessView);
        if(speed == 1)
        {
            sw.setText("Сложность: просто\nМножитель: X1");
        }
        else if(speed == 2)
        {
            sw.setText("Сложность: средне\nМножитель: X10");
        }
        else
        {
            sw.setText("Сложность: сложно\nМножитель: X100");
        }

    }

    class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    int w = 280;
                    t += ((clockwise) ? 1 : -1) * (speed * 2 - 1);
                    if (t > 360)
                        t = 0;
                    bx = (float) Math.sin(t / 57.3) * w;
                    by = (float) Math.cos(t / 57.3) * w;

                    button.setX(bx + 500);
                    button.setY(by + 280);
                }
            });
        }
    }

    private int ReadRecord()
    {
        try {
            InputStream inputStream = openFileInput(FILENAME);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);

                // Read the first line
                int _record;
                try {
                    _record = Integer.parseInt(reader.readLine());
                }
                catch (Throwable th){
                    return 0;
                }
                inputStream.close();
                return _record;
            }
        }
        catch (Throwable t) {
        }
        return 0;
    }

    private void WriteRecord(int _record)
    {
        try {
            OutputStream outputStream = openFileOutput(FILENAME, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(String.valueOf(_record));
            osw.close();
        } catch (Throwable t) {
        }
    }
}
