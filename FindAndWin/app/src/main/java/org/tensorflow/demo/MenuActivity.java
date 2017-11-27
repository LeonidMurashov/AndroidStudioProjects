package org.tensorflow.demo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		Button game1Btn = (Button) findViewById(R.id.game1Btn);
		Button game2Btn = (Button) findViewById(R.id.game2Btn);

		game1Btn.setOnClickListener(this);
		game2Btn.setOnClickListener(this);

	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.game1Btn:
				new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent mainIntent = new Intent(MenuActivity.this,
								CameraActivity.class);
						mainIntent.putExtra("mode", 0);
						startActivity(mainIntent);
						overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
					}
				}, 1);
				break;
			case R.id.game2Btn:
				new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent mainIntent = new Intent(MenuActivity.this,
								CameraActivity.class);


						mainIntent.putExtra("mode", 1);
						startActivity(mainIntent);
						overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
					}
				}, 1);
				break;
			case R.id.settBtn:
				/*new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent mainIntent = new Intent(MenuActivity.this,
								SettingsActivity.class);
						startActivity(mainIntent);
						overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
					}
				}, 1);*/
				break;
		}
	}
}
