package org.tensorflow.demo;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Леонид on 21.05.2017.
 * My OnItemClickListener implementation
 */

public class ItemSelectedListner implements AdapterView.OnItemSelectedListener {
	RecognitionScoreView recognitionScoreView;

	public ItemSelectedListner(RecognitionScoreView recognitionScoreView) {
		this.recognitionScoreView = recognitionScoreView;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

		recognitionScoreView.setLanguage((String)adapterView.getItemAtPosition(i));
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}
}
