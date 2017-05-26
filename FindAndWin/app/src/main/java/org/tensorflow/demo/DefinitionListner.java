package org.tensorflow.demo;

import android.view.View;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Леонид on 21.05.2017.
 * My onClickListner implementation
 */

class DefinitionListner implements View.OnClickListener {
	private RecognitionScoreView recognitionScoreView;

	DefinitionListner(RecognitionScoreView recognitionScoreView) {
		this.recognitionScoreView = recognitionScoreView;
	}

	@Override
	public void onClick(View view) {
		try {
			recognitionScoreView.showDefinition();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
