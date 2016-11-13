package ru.leonid.accelerometertest2;

/*
 *
 * IBMEyes.java
 * sample code for IBM Developerworks Article
 * Author: W. Frank Ableson
 * fableson@msiservices.com
 *
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.hardware.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MainActivity extends Activity implements SensorListener {

    final String tag = "IBMEyes";
    SensorManager sm = null;

    TextView xViewA = null;
    TextView yViewA = null;
    TextView zViewA = null;
    TextView xViewO = null;
    TextView yViewO = null;
    TextView zViewO = null;
    TextView sum = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.activity_main);
        xViewA = (TextView) findViewById(R.id.axisX);
        yViewA = (TextView) findViewById(R.id.axisY);
        zViewA = (TextView) findViewById(R.id.axisZ);
        xViewO = (TextView) findViewById(R.id.xboxo);
        yViewO = (TextView) findViewById(R.id.yboxo);
        zViewO = (TextView) findViewById(R.id.zboxo);
        sum = (TextView) findViewById(R.id.sum);

    }



    public void onSensorChanged(int sensor, float[] values) {
        synchronized (this) {
            Log.d(tag, "onSensorChanged: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
            if (sensor == SensorManager.SENSOR_ORIENTATION) {
                xViewO.setText("Orientation X: " + values[0]);
                yViewO.setText("Orientation Y: " + values[1]);
                zViewO.setText("Orientation Z: " + values[2]);
            }
            if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
                xViewA.setText("Accel X: " + values[0]);
                yViewA.setText("Accel Y: " + values[1]);
                zViewA.setText("Accel Z: " + values[2]);
                sum.setText("Sum: " + sqrt(pow(values[0], 2) + pow(values[1], 2) + pow(values[2], 2)));
            }
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
        Log.d(tag,"onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);

    }


    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this,
                SensorManager.SENSOR_ORIENTATION |
                        SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sm.unregisterListener(this);
        super.onStop();
    }


}