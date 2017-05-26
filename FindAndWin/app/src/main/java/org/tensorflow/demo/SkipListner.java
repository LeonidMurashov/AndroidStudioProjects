package org.tensorflow.demo;

import android.view.View;

/**
 * Created by Леонид on 21.05.2017.
 * My onClickListner implementation
 */

class SkipListner implements View.OnClickListener {
	private RecognitionScoreView recognitionScoreView;

	SkipListner(RecognitionScoreView recognitionScoreView) {
		this.recognitionScoreView = recognitionScoreView;
	}

	@Override
	public void onClick(View view) {
		recognitionScoreView.changeTask();
	}
}
