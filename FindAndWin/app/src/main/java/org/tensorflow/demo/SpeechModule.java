package org.tensorflow.demo;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.io.Serializable;
import java.util.Locale;

public class SpeechModule implements TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech;
    Locale locale = Locale.ENGLISH;

    public SpeechModule(Context context) {
        textToSpeech = new TextToSpeech(context, this);
        //
    }
    public SpeechModule(Context context, Locale locale) {
        textToSpeech = new TextToSpeech(context, this);
        this.locale = locale;
        //
    }
    public void TextToSpeechFunction(String textholder, boolean isOutQueue)
    {
        if(isOutQueue)
            textToSpeech.stop();
        textToSpeech.speak(textholder, (isOutQueue) ? TextToSpeech.QUEUE_FLUSH : TextToSpeech.QUEUE_ADD, null);

    }

    public void onDestroy() {
        textToSpeech.shutdown();
    }

    @Override
    public void onInit(int i) {
        textToSpeech.setLanguage(locale);
    }
}