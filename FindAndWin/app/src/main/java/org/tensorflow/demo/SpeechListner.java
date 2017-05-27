package org.tensorflow.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * This one listens for speech
 */

public class SpeechListner implements RecognitionListener{
	private static final String TAG = "MyStt3Activity";
	SpeechActivity speechActivity;

	public SpeechListner(SpeechActivity speechActivity) {
		this.speechActivity = speechActivity;
	}

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
	}
	public void onResults(Bundle results)
	{
		String str = new String();
		Log.d(TAG, "onResults " + results);
		ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		/*for (int i = 0; i < data.size(); i++)
		{
			Log.d(TAG, "result " + data.get(i));
			str += data.get(i);
		}*/

		speechActivity.pushSpeechResult(data);
		//Intent intent = new Intent("ShowDefinition");
		//intent.putExtra("position", classes[showClass]);
		//intent.putExtra("lang", target);
		//context.startActivity(intent);

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

