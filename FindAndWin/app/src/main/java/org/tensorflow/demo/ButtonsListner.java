package org.tensorflow.demo;

import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;

/**
 * Universal click listner
 */

public class ButtonsListner implements View.OnClickListener{
	private RecognitionScoreView recognitionScoreView;
	public ButtonsListner(RecognitionScoreView recognitionScoreView) {
		this.recognitionScoreView = recognitionScoreView;
	}

	@Override
	public void onClick(View view) {
		switch (((Button)view).getId()){
			case R.id.defBtn:
				try {
					recognitionScoreView.showDefinition();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.skipBtn:
				recognitionScoreView.changeTask();
				break;
			case R.id.talkBtn:
				recognitionScoreView.requestSpeech();
				break;
		}
	}
}
