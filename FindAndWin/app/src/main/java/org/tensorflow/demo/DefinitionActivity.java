package org.tensorflow.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DefinitionActivity extends Activity implements TranslationRequestor{

	String position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_definition);

		position = getIntent().getStringExtra("position");
		String lang = getIntent().getStringExtra("lang");

		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.definitions)));
		String raw = null;
		try {
			raw = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String result = "";
			JSONObject dataJsonObj = new JSONObject(raw);
			JSONObject words = dataJsonObj.getJSONObject(position);
			JSONArray wordsList = words.names();
			for(int i = 0; i < words.length(); i++)
			{
				JSONArray defs = words.getJSONArray(wordsList.get(i).toString());
				result += wordsList.get(i).toString() + '\n';
				for(int j = 0; j < defs.length(); j++)
				{
					result += Html.fromHtml("<p>" + (j+1) + ") " + defs.getString(j) + "</p>");
				}
				result += '\n';
			}
			if(result.equals(""))
				result = "Sorry, no definition for this words.\n";
			Translator.Translate(result, lang, 0, this);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void ReceiveTranslation(String translation, int code) {
		TextView defView = (TextView)findViewById(R.id.defView);
		translation = translation.replace("\\n", "\n");
		defView.setText(translation + "Original english: " + position);
	}
}
