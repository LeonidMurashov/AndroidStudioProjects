package ru.leonid.lightsensor;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	SpeechModule speech;
	TextView textLIGHT_reading;
	TextView textLIGHT_available;
	int light = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		speech = new SpeechModule(this);

		textLIGHT_reading = (TextView)findViewById(R.id.textLIGHT_reading);
		textLIGHT_available = (TextView)findViewById(R.id.textLIGHT_available);


		SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

		Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		if(LightSensor != null){
			textLIGHT_available.setText("Sensor.TYPE_LIGHT Available");
			mySensorManager.registerListener(
					LightSensorListener,
					LightSensor,
					SensorManager.SENSOR_DELAY_NORMAL);

		}else{
			textLIGHT_available.setText("Sensor.TYPE_LIGHT NOT Available");
		}
	}

	private final SensorEventListener LightSensorListener
			= new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_LIGHT){
				textLIGHT_reading.setText("LIGHT: " + event.values[0]);

				//RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
				//int color = Math.min((int)event.values[0]*255/700, 255);
				//layout.setBackgroundColor(Color.rgb(color,color,color));

				if (event.values[0] < 8 && light != 0) {
					speech.TextToSpeechFunction("Очень темно!", true);
					light = 0;
				}
				if (event.values[0] > 8 && event.values[0] < 30 && light != 1) {
					speech.TextToSpeechFunction("Темно!", true);
					light = 1;
				}
				if (event.values[0] > 30 && event.values[0] < 700 && light != 2) {
					speech.TextToSpeechFunction("Светло!", true);
					light = 2;
				}
				if (event.values[0] > 700 && event.values[0] < 10000 && light != 3) {
					speech.TextToSpeechFunction("Очень светло!", true);
					light = 3;
				}
				if (event.values[0] > 10000 && light != 4) {
					speech.TextToSpeechFunction("Я ослеплена!", true);
					light = 4;
				}
			}
		}
	};
}
