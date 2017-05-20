package org.tensorflow.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;
import static android.R.attr.text;

public class SpeechModule implements TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech;

    public SpeechModule(Context context) {
        textToSpeech = new TextToSpeech(context, this);
        //textToSpeech.setLanguage(Locale.UK);
    }

    public void TextToSpeechFunction(String textholder)
    {
        textToSpeech.speak(textholder, TextToSpeech.QUEUE_ADD, null);
    }

    public void onDestroy() {
        textToSpeech.shutdown();
    }

    @Override
    public void onInit(int Text2SpeechCurrentStatus) {
        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {
            //textToSpeech.setLanguage(Locale.UK);
        }
    }
}