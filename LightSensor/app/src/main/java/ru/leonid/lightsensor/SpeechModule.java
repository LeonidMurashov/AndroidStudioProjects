package ru.leonid.lightsensor;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeechModule implements TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech;

    public SpeechModule(Context context) {
        textToSpeech = new TextToSpeech(context, this);
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
    public void onInit(int Text2SpeechCurrentStatus) {
        if (Text2SpeechCurrentStatus == TextToSpeech.SUCCESS) {
            //textToSpeech.setLanguage(Locale.ENGLISH);
        }
    }
}