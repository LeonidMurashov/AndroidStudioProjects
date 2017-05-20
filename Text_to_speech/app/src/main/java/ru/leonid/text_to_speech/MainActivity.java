package ru.leonid.text_to_speech;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    TextToSpeech textToSpeech;
    Button button;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(MainActivity.this, MainActivity.this);

        button = (Button) findViewById(R.id.button);

        editText = (EditText) findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TextToSpeechFunction() ;
            }

        });
    }

    public void TextToSpeechFunction()
    {

        String textholder = editText.getText().toString();

        textToSpeech.speak(textholder, TextToSpeech.QUEUE_FLUSH, null);

        Toast.makeText(MainActivity.this , textholder, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {

        textToSpeech.shutdown();

        super.onDestroy();
    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {

        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {

            //textToSpeech.setLanguage(Locale.UK);

            button.setEnabled(true);

            TextToSpeechFunction();
        }

    }
}