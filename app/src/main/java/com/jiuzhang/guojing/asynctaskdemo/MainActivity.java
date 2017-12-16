package com.jiuzhang.guojing.asynctaskdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.image);

        Button loadTextButton = (Button) findViewById(R.id.load_text_button);
        loadTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskCompat.executeParallel(new LoadTextTask("http://www.jiuzhang.com/api/course/?format=json"));
            }
        });

        Button loadImageButton = (Button) findViewById(R.id.load_image_button);
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskCompat.executeParallel(new LoadImageTask("http://www.jiuzhang.com/media/avatars/guojing2.png"));
            }
        });

        client = new OkHttpClient();
    }

    private class LoadTextTask extends AsyncTask<Void, Void, String> {

        String url;

        LoadTextTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(s);
        }

    }

    private class LoadImageTask extends AsyncTask<Void, Void, byte[]> {

        String imageUrl;

        LoadImageTask(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override//NON-UI
        protected byte[] doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override//UI
        protected void onPostExecute(byte[] bytes) {
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }

}
