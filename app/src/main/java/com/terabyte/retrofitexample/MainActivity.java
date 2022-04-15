package com.terabyte.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView textImageDescription;
    ImageView imageOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textImageDescription = findViewById(R.id.textImageDescription);
        imageOfDay = findViewById(R.id.imagePictureOfDay);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.NASA_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceService spaceService = retrofit.create(SpaceService.class);

        Call<SpaceResponse> call = spaceService.getSpaceInfo("DEMO_KEY");

        call.enqueue(new SpaceCallback());

    }
    //it's like a listener in Android
    class SpaceCallback implements Callback<SpaceResponse> {
        @Override
        public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
            if(response.isSuccessful()) {
                String s = response.body().explanation;
                textImageDescription.setText(s);
                // TODO: 15.04.2022 create a request to translate a text in microsoft
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Const.TRANSLATE_API)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TranslateService translateService = retrofit.create(TranslateService.class);
                TranslateBody translateBody = new TranslateBody();
                translateBody.Text = s;
                Call<TranslateResponse[]> callTranslate = translateService.getTranslate("ru-RU", new TranslateBody[] {translateBody});
                //command execute works in main thread. You can create your new thread if you want to use execute
                //command enqueue works in own another thread. So we can work in our main thread later
                callTranslate.enqueue(new TranslateCallback());















                if(response.body().media_type.equals("image")) {
                    Picasso.get()
                            .load(response.body().url)
                            .placeholder(R.drawable.default_image_of_day)
                            .into(imageOfDay);
                }
            }
            else {

            }
        }

        @Override
        public void onFailure(Call<SpaceResponse> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Failure to recieve a response from a server", Toast.LENGTH_LONG).show();
        }
    }

    class TranslateCallback implements Callback<TranslateResponse[]> {

        @Override
        public void onResponse(Call<TranslateResponse[]> call, Response<TranslateResponse[]> response) {
            if(response.isSuccessful()) {
                String s = response.body()[0].translations[0].text;
                textImageDescription.setText(s);
            }
        }

        @Override
        public void onFailure(Call<TranslateResponse[]> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Failed to translate text with microsoft azure translator api", Toast.LENGTH_LONG).show();
        }
    }
}