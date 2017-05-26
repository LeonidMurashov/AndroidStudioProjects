/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.demo.Classifier.Recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class RecognitionScoreView extends View implements TranslationRequestor{
  private static final float TEXT_SIZE_DIP = 24;
  private List<Recognition> results;
  private final float textSizePx;
  private final Paint fgPaint;
  private final Paint bgPaint;
  private Context context;
  private SpeechModule speechModule;
  private String[] classes;
  Map<String, Integer> class2ID;
  private static final int nClasses = 1001;
  private int showClass;
  private String translation;
  private String title_phrase;
  Random random;
  private boolean isListnerSet = false;
  private String target = "en";
  private int lastPred = 0;
  private long lastTime = 0;
  private int repeated = 0;

  public RecognitionScoreView (final Context context, final AttributeSet set) throws IOException {
    super(context, set);

    this.context = context;
    speechModule = new SpeechModule(context);
    class2ID = new TreeMap<String, Integer>();

    classes = new String[nClasses];
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.imagenet_comp_graph_label_strings)));
      for (int i = 0; i < nClasses; i++) {
        classes[i] = reader.readLine();
        class2ID.put(classes[i], i);
      }
    }
    catch (Exception e) {
      Log.e("EXCEPTION", e.toString());
    }

    random = new Random();
    showClass = random.nextInt(nClasses);
    translation = "";

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    fgPaint = new Paint();
    fgPaint.setTextSize(textSizePx);

    bgPaint = new Paint();
    bgPaint.setColor(0xcc4285f4);
    //((Button)(findViewById(R.id.nextBtn))).setOnClickListener((RecognitionScoreView) findViewById(R.id.results));
  }

 // public void setSpeechModule(SpeechModule speechModule)
 // {
 // //  this.speechModule = speechModule;
  //}

  public void setResults(final List<Recognition> results) {
    this.results = results;
    postInvalidate();
  }

  public void setTranslation(String translation)
  {
    this.translation = translation;
  }

  @Override
  public void onDraw(final Canvas canvas) {

    final int x = 10;
    int y = (int) (fgPaint.getTextSize() * 1.5f);
    canvas.drawPaint(bgPaint);

    if(translation.length() > 27) {
      canvas.drawText(translation.substring(0, 27), x, y, fgPaint);
      y += fgPaint.getTextSize() * 1.5f;
      canvas.drawText(translation.substring(27, translation.length()), x, y, fgPaint);
      y += fgPaint.getTextSize() * 1.5f;
    }
    else
    {
      canvas.drawText(translation, x, y, fgPaint);
      y += fgPaint.getTextSize() * 1.5f;
    }

    boolean isIn = false;
    if (results != null)
      for(Recognition r : results)
        if(r.getTitle().equalsIgnoreCase(classes[showClass])) {
          isIn = true;
          break;
        }

    if(isIn || showClass == -1) {
      if(showClass != -1) {
        Translator.Translate(classes[showClass] + ". Perfect work!", target, speechModule, false);
      }
      showClass = random.nextInt(nClasses);
      Log.e("Okay, now show me " + classes[showClass],"--------");
      Translator.Translate("Okay, now show me: " + classes[showClass], target, speechModule, false);
      Translator.Translate("Show me: " + classes[showClass], target, this);
    }
    else {

      if (results != null && !results.isEmpty()) {
        String str = results.get(0).getTitle();
        if(!classes[lastPred].equals(str)) {
          Translator.Translate(str, target, speechModule, false);
          lastTime = System.currentTimeMillis();
          repeated = 0;
        }
        else if (System.currentTimeMillis() - lastTime > 1500 && repeated < 4) {
          String beginPhrase = "";
          int rand = Math.abs(random.nextInt())%3;
          switch (rand){
            case 0: beginPhrase = "This is exactly "; break;
            case 1: beginPhrase = "This is "; break;
            case 2: beginPhrase = "I see "; break;
          }
          Translator.Translate(beginPhrase + str, target, speechModule, false);
          lastTime = System.currentTimeMillis();

          repeated++;
        }

        lastPred = class2ID.get(str);

        for (final Recognition recog : results) {
          canvas.drawText(recog.getTitle() + ": " + recog.getConfidence(), x, y, fgPaint);
          y += fgPaint.getTextSize() * 1.5f;
        }
        //canvas.drawText(recog.getTitle() + ": " + recog.getConfidence(), x, y, fgPaint);
      }
    }
  }

  public void changeTask() {
    showClass = random.nextInt(nClasses);
    Translator.Translate("Okay, now show me " + classes[showClass], target, speechModule, true);
    Translator.Translate("Show me " + classes[showClass], target, this);

  }

  public void setLanguage(String itemAtPosition) {
    //speechModule.TextToSpeechFunction(itemAtPosition, true);
    //String langs[] = {"Azerbaijani","Albanian","Amharic","English","Arab","Armenian","Afrikaans","Basque","Bashkir","Belorussian","Bengal","Burmese","Bulgarian","Bosnian","Welsh","Hungarian","Vietnamese","Haitian (Creole)","Galician","Dutch","Mining","Greek","Georgian","Gujarati","Danish","Hebrew","Yiddish","Indonesian","Irish","Italian","Icelandic","Spanish","Kazakh","Kannada","Catalan","Kyrgyz","Chinese","Korean","Spit","Khmer","Laotian","Latin","Latvian","Lithuanian","Luxembourgish","Malagasy","Malay","Malayalam","Maltese","Macedonian","Maori","Marathi","Mari","Mongolian","German","Nepali","Norwegian","Punjabi","Papiamento","Persian","Polish","Portuguese","Romanian","Russian","Sebuanian","Serbian","Sinhalese","Slovak","Slovenian","Swahili","Sudanese","Tajik","Thai","Tagalog","Tamil","Tatar","Telugu","Turkish","Udmurt","Uzbek","Ukrainian","Urdu","Finnish","French","Hindi","Croatian","Czech","Swedish","Scottish","Estonian","Esperanto","Javanese","Japanese",};
    //String codes[] = {"az","sq","am","en","ar","hy","af","eu","ba","be","bn","my","bg","bs","cy","hu","vi","ht","gl","nl","mrj","el","ka","gu","da","he","yi","id","ga","it","is","es","kk","kn","ca","ky","zh","ko","xh","km","lo","la","lv","lt","lb","mg","ms","ml","mt","mk","mi","mr","mhr","mn","de","ne","no","pa","pap","fa","pl","pt","ro","ru","ceb","sr","si","sk","sl","sw","su","tg","th","tl","ta","tt","te","tr","udm","uz","uk","ur","fi","fr","hi","hr","cs","sv","gd","et","eo","jv","ja",};
    //Locale locales[] = {Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ITALIAN,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.CHINESE,Locale.KOREAN,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.GERMANY,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.getDefault(),Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.TAIWAN,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.getDefault(),Locale.getDefault(),Locale.ENGLISH,Locale.ENGLISH,Locale.FRENCH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.ENGLISH,Locale.JAPANESE,};

    //String langs[] = {"Belorussian","Chinese","English","French","German","Italian","Japanese","Korean","Russian","Thai","Ukrainian",};
   // String codes[] = {"be","zh","en","fr","de","it","ja","ko","ru","th","uk",};
   // Locale locales[] = {Locale.getDefault(),Locale.CHINESE,Locale.ENGLISH,Locale.FRENCH,Locale.GERMAN,Locale.ITALIAN,Locale.JAPANESE,Locale.KOREAN,Locale.getDefault(),Locale.TAIWAN,Locale.getDefault(),};

    String langs[] = {"Belorussian","English","French","German","Italian","Russian","Ukrainian",};
    String codes[] = {"be","en","fr","de","it","ru","uk",};
    Locale locales[] = {Locale.getDefault(),Locale.ENGLISH,Locale.FRENCH,Locale.GERMAN,Locale.ITALIAN,Locale.getDefault(),Locale.getDefault(),};


    assert(langs.length == codes.length && langs.length == locales.length);

    for(int i = 0; i < langs.length; i++)
      if (langs[i].equals(itemAtPosition)) {
        target = codes[i];
        speechModule.textToSpeech.setLanguage(locales[i]);
        break;
      }

    Log.e("Okay, now show me " + classes[showClass],"--------");
    Translator.Translate("Okay, now show me: " + classes[showClass], target, speechModule, false);
    Translator.Translate("Show me: " + classes[showClass], target, this);
  }

  public void showDefinition() throws IOException, JSONException {

    //TextView view = (TextView)findViewById(R.id.editText2);
    //view.setVisibility(VISIBLE);

    Intent intent = new Intent("ShowDefinition");
    intent.putExtra("position", classes[showClass]);
    intent.putExtra("lang", target);
    context.startActivity(intent);


    //intent.putExtra(EXTRA_MESSAGE, message);

    /*BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.definitions)));
    String raw = reader.readLine();
    reader.close();

    JSONObject dataJsonObj = new JSONObject(raw);
    JSONObject words = dataJsonObj.getJSONObject("brain coral");
    JSONArray defs = words.getJSONArray(words.names().get(1).toString());
    label.setText(defs.getString(0));*/
  }

  @Override
  public void ReceiveTranslation(String translation) {
    this.translation = translation;
  }
}
