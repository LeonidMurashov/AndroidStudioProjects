package org.tensorflow.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static org.tensorflow.demo.R.styleable.Toolbar;

/**
 * Test your speech
 */

public class SpeechActivity extends Activity implements TranslationRequestor{
	private SpeechRecognizer speechRecognizer;
	private String position, lang, positionTranslated;
	private Locale locale;
	private TextView textView1, textView2;
	private Map<Integer, String> translations;
	String results = "", label1 = "";
	boolean speaked = false, congrats = false, taskSaid = false, congratsSaid = false;

	SpeechModule speechModule;


	private static class Codes {
		static int label01 = 0, label02 = 1, position = 2, congrats = 3, resHelper = 4;
	}

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
		textView1 = (TextView)findViewById(R.id.speechLabel1);
		textView2 = (TextView)findViewById(R.id.speechLabel2);
		translations = new TreeMap<>();
		//textView1.setText(Html.fromHtml("Now you have to say " + "<b>" + position + "</b>" + ". Press button to start."));
		Translator.Translate("Now let's say: ", lang, Codes.label01,this);
		Translator.Translate(". Press button to start.", lang, Codes.label02,this);
		Translator.Translate(position, lang, Codes.position,this);

		findViewById(R.id.speakLay).setOnClickListener(new View.OnClickListener() {
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


		speechModule = new SpeechModule(this, locale);
		//speechModule.textToSpeech.setLanguage(locale);
		findViewById(R.id.listenLay).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				speechRecognizer.stopListening();
				speechModule.TextToSpeechFunction(positionTranslated, true);

			}
		});


		/*Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
	}
	public final static String THIEF = "ru.alexanderklimov.sherlock.THIEF";
	public void pushSpeechResult(ArrayList data) {
		results = "";
		for(int i = 0; i < data.size(); i++){
			results += "\t" + (i+1) + ") " + data.get(i).toString() + "\n";
		}

		Translator.Translate("That was like:\n", lang, Codes.resHelper,this);


		if(data.get(0).toString().toLowerCase().replace('-', ' ').replace("\'", "").equals(positionTranslated.toLowerCase().replace('-', ' ').replace("\'", ""))) {
			Translator.Translate("\n\n\nThat was Perfect! You are great!", lang, Codes.congrats, this);
			congrats = true;

			Intent answerIntent = new Intent();
			answerIntent.putExtra(THIEF, true);
			setResult(RESULT_OK, answerIntent);
		}
		speaked = true;
	}

	public void pushErrorCode(int code)
	{

	}

	@Override
	public void ReceiveTranslation(String translation, int code) {
		translation = translation.replace("\\n", "\n");
		translation = translation.replace("\\t", "\t");
		//translations.add(new Translation(translation, code));
		translations.put(code, translation);

		ArrayList<Integer> necessary1 = new ArrayList<Integer>();
		necessary1.add(Codes.position);
		necessary1.add(Codes.label01);
		necessary1.add(Codes.label02);
		// Check for necessary1
		boolean check = true;
		for(int c : necessary1)	{
			check &= translations.containsKey(c);
		}
		if(check) {
			textView1.setText(translations.get(Codes.label01) + translations.get(Codes.position) + translations.get(Codes.label02));
			positionTranslated = translations.get(Codes.position);
			translations.clear();
		}

		ArrayList<Integer> necessary2 = new ArrayList<Integer>();
		necessary2.add(Codes.resHelper);
		if(congrats)
			necessary2.add(Codes.congrats);

		if(!congratsSaid && translations.containsKey(Codes.congrats)) {
			congratsSaid = true;
			speechModule.TextToSpeechFunction(translations.get(Codes.congrats), false);
		}
		// Check for necessary2
		check = true;
		for(int c : necessary2)	{
			check &= translations.containsKey(c);
		}
		if(check){
			textView2.setText( translations.get(Codes.resHelper)+
					results +
					((congrats) ? translations.get(Codes.congrats) : "")
			);
			translations.clear();
		}

		if(!taskSaid)
		{
			taskSaid = true;
			Translator.Translate("Now let's say: " + position, lang, speechModule, true);
		}
	}
}
