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
}