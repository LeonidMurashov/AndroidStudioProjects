package org.tensorflow.demo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


public class Translator {

    public interface TaskService {
        @FormUrlEncoded
        @POST("/api/v1.5/tr.json/translate")
        void createTask(
                @Field("text") String text,
                @Field("lang") String lang,
                @Field("key") String key,
                Callback<Response> callback);
    }

    public static void Translate(String text, String lang, final SpeechModule speechModule, final boolean isOutQueue){
        String key = "trnsl.1.1.20170517T215755Z.a83945fa0cbc7b25.a3c095a3395c967a8250cef6843accfb6f063b4b";
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

                        Log.e("GOOD","ggrgrgrgrggr");
                        try {
                            String translation = (new BufferedReader(new InputStreamReader(result.getBody().in()))).readLine();
                            int a = translation.indexOf('['),
                                    b = translation.indexOf(']');
                            translation = translation.substring(a+2, b-1);
                            speechModule.TextToSpeechFunction(translation, isOutQueue);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("ERROR", error.toString());
                        //If any error occured displaying the error as toast
                    }
                }
        );

    }

    public static void Translate(String text, String lang, final TranslationRequestor requestor){
        String key = "trnsl.1.1.20170517T215755Z.a83945fa0cbc7b25.a3c095a3395c967a8250cef6843accfb6f063b4b";
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

                        Log.e("GOOD","ggrgrgrgrggr");
                        try {
                            String translation = (new BufferedReader(new InputStreamReader(result.getBody().in()))).readLine();
                            int a = translation.indexOf('['),
                                    b = translation.indexOf(']');
                            translation = translation.substring(a+2, b-1);
                            requestor.ReceiveTranslation(translation);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("ERROR", error.toString());
                        //If any error occured displaying the error as toast
                    }
                }
        );

    }
}