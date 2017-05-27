package ru.leonid.speechrecognizer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;



public class MainActivity extends Activity implements OnClickListener
{
	private static final int con = 1;
	private TextView mText;
	private SpeechRecognizer sr;
	private static final String TAG = "MyStt3Activity";
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button speakButton = (Button) findViewById(R.id.btn_speak);
		mText = (TextView) findViewById(R.id.textView1);
		speakButton.setOnClickListener(this);
		sr = SpeechRecognizer.createSpeechRecognizer(this);
		sr.setRecognitionListener(new listener());


		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.RECORD_AUDIO)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.RECORD_AUDIO)) {

				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {

				// No explanation needed, we can request the permission.

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.RECORD_AUDIO}, con
						);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}

		//int permissionCheck = ContextCompat.checkSelfPermission(this,
		//		Manifest.permission.RECORD_AUDIO);
		//mText.setText(permissionCheck);

	}

	class listener implements RecognitionListener
	{
		public void onReadyForSpeech(Bundle params)
		{
			Log.d(TAG, "onReadyForSpeech");
		}
		public void onBeginningOfSpeech()
		{
			Log.d(TAG, "onBeginningOfSpeech");
		}
		public void onRmsChanged(float rmsdB)
		{
			Log.d(TAG, "onRmsChanged");
		}
		public void onBufferReceived(byte[] buffer)
		{
			Log.d(TAG, "onBufferReceived");
		}
		public void onEndOfSpeech()
		{
			Log.d(TAG, "onEndofSpeech");
		}
		public void onError(int error)
		{

			Log.d(TAG,  "error " +  error);
			mText.setText("error " + error);
		}
		public void onResults(Bundle results)
		{
			String str = new String();
			Log.d(TAG, "onResults " + results);
			ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			float[] score =results.getFloatArray(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
			for (int i = 0; i < data.size(); i++)
			{
				Log.d(TAG, "result " + data.get(i));
				str += data.get(i);
			}

			float a = -2;
			if(score !=null)
				a = score[0];
			mText.setText(data.get(0).toString() + " " + a);//"results: "+String.valueOf(data.size()));
		}
		public void onPartialResults(Bundle partialResults)
		{
			Log.d(TAG, "onPartialResults");
		}
		public void onEvent(int eventType, Bundle params)
		{
			Log.d(TAG, "onEvent " + eventType);
		}
	}
	public void onClick(View v) {
		if (v.getId() == R.id.btn_speak)
		{
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"ru.leonid.speechrecognizer");

			intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toLanguageTag());
			sr.startListening(intent);
			Log.i("111111","11111111");
		}
	}

	//void print()
}