package ru.leonid.json_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		try {
			ParseJSON();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void ParseJSON() throws JSONException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.defs)));
		String raw = reader.readLine();
		reader.close();

		TextView label = (TextView)findViewById(R.id.label);
		JSONObject dataJsonObj = new JSONObject(raw);
		JSONObject words = dataJsonObj.getJSONObject("brain coral");
		JSONArray defs = words.getJSONArray(words.names().get(1).toString());
		label.setText(defs.getString(0));
	}

}
