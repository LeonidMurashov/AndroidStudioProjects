package ru.leonid.json_request;

//https://tech.yandex.ru/translate/doc/dg/concepts/About-docpage/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
public class MainActivity extends AppCompatActivity {
    private String key = "trnsl.1.1.20170517T215755Z.a83945fa0cbc7b25.a3c095a3395c967a8250cef6843accfb6f063b4b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Translate("Здравствуйте, меня зовут Леонид!", "it");
    }

    public interface TaskService {
        @FormUrlEncoded
        @POST("/api/v1.5/tr.json/translate")
        void createTask(
                @Field("text") String text,
                @Field("lang") String lang,
                @Field("key") String key,
                Callback<Response> callback);
    }

    private void Translate(String text, String lang){
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://translate.yandex.net/") //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        TaskService api = adapter.create(TaskService.class);
        //Defining the method insertuser of our interface
        api.createTask(
                //Passing the values by getting it from editTexts
                text,
                lang,
                key,
                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {

                        try {
                            String translation = (new BufferedReader(new InputStreamReader(result.getBody().in()))).readLine();
                            int a = translation.indexOf('['),
                                b = translation.indexOf(']');
                            translation = translation.substring(a+2, b-1);
                            ((TextView)findViewById(R.id.Text)).setText(translation);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        ((TextView)findViewById(R.id.Text)).setText(error.toString());
                        Toast.makeText(MainActivity.this, error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );

    }
}