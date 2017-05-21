package ru.leonid.buttontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.button)).setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Azerbaijani","Albanian","Amharic","English","Arab","Armenian","Afrikaans","Basque","Bashkir","Belorussian","Bengal","Burmese","Bulgarian","Bosnian","Welsh","Hungarian","Vietnamese","Haitian (Creole)","Galician","Dutch","Mining","Greek","Georgian","Gujarati","Danish","Hebrew","Yiddish","Indonesian","Irish","Italian","Icelandic","Spanish","Kazakh","Kannada","Catalan","Kyrgyz","Chinese","Korean","Spit","Khmer","Laotian","Latin","Latvian","Lithuanian","Luxembourgish","Malagasy","Malay","Malayalam","Maltese","Macedonian","Maori","Marathi","Mari","Mongolian","German","Nepali","Norwegian","Punjabi","Papiamento","Persian","Polish","Portuguese","Romanian","Russian","Sebuanian","Serbian","Sinhalese","Slovak","Slovenian","Swahili","Sudanese","Tajik","Thai","Tagalog","Tamil","Tatar","Telugu","Turkish","Udmurt","Uzbek","Ukrainian","Urdu","Finnish","French","Hindi","Croatian","Czech","Swedish","Scottish","Estonian","Esperanto","Javanese","Japanese",});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        ((TextView)findViewById(R.id.textView)).setText("You has pushed button!");
    }
}
