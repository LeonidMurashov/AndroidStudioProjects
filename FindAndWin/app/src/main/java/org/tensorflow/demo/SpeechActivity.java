package org.tensorflow.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Test your speech
 */

public class SpeechActivity extends Activity implements TranslationRequestor{
	private SpeechRecognizer speechRecognizer;
	private String position, lang, positionTranslated;
	private Locale locale;
	private TextView textView1, textView2;
	private String translation;
	private int requesting = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_speech);
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
		speechRecognizer.setRecognitionListener(new SpeechListner(this));

		Bundle extras = getIntent().getExtras();
		position = (String)extras.get("position");
		lang = (String)extras.get("lang");
		locale = (Locale)extras.get("langLoc");
		textView2 = (TextView)findViewById(R.id.speechLabel2);
		textView1 = (TextView)findViewById(R.id.speechLabel1);
		//textView1.setText(Html.fromHtml("Now you have to say " + "<b>" + position + "</b>" + ". Press button to start."));
		Translator.Translate("Now you have to say: " + position + ". Press button to start.", lang, this);
		Translator.Translate(position, lang, this);
		requesting = 1;

		findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"org.tensorflow.demo");

				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.toLanguageTag());
				speechRecognizer.startListening(intent);
			}
		});
	}

	public void pushSpeechResult(ArrayList data) {
		String res = "";
		for(int i = 0; i < data.size(); i++){
			res += "\t" + (i+1) + ") " + data.get(i).toString() + "\n";
		}
		res = "That was like:\n" + res;
		if(data.get(0).toString().toLowerCase().equals(position.toLowerCase()))
			res += "\n\n\nThat was Perfect! You are great!";

		Translator.Translate(res, lang, this);
		requesting = 2;
	}

	public void pushErrorCode(int code)
	{

	}


	@Override
	public void ReceiveTranslation(String translation) {
		if(requesting == 1) {
			if(translation.length() > 30)
				textView1.setText(translation);
			else
				positionTranslated = translation;
		}
		if(requesting == 2) {
			translation = translation.replace("\\n", "\n");
			translation = translation.replace("\\t", "\t");
			textView2.setText(translation);
		}
	}
}
