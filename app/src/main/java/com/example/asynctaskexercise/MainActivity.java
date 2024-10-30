package com.example.asynctaskexercise;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView titleTextView, categoryTextView, errorTextView;
    private ImageView imageView;
    private Button displayDataButton;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.titleTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        imageView = findViewById(R.id.imageView);
        displayDataButton = findViewById(R.id.displayData);
        errorTextView = findViewById(R.id.errorTextView);

        displayDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jalankan AsyncTask untuk mengambil data dan menampilkan gambar
                new FetchDataTask().execute();
            }
        });
    }

    // AsyncTask untuk mengambil data latar belakang
    private class FetchDataTask extends AsyncTask<Void, Void, DataResult> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Tampilkan ProgressDialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false); // Tidak bisa dibatalkan
            progressDialog.show();

            titleTextView.setText("Loading...");
            categoryTextView.setText("");
            imageView.setImageResource(0); // Kosongkan ImageView
        }

        @Override
        protected DataResult doInBackground(Void... voids) {
            // Simulasi pengambilan data dari server
            String title = "Beautiful Landscape";
            String category = "Nature";
            String imageUrl = "https://images.squarespace-cdn.com/content/v1/61c4da8eb1b30a201b9669f2/e2e0e62f-0064-4f86-b9d8-5a55cb7110ca/Korembi-January-2024.jpg"; // URL gambar contoh

            Bitmap imageBitmap = null;
            try {
                // Mengambil gambar dari URL
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                imageBitmap = BitmapFactory.decodeStream(input);

                Thread.sleep(2000);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return new DataResult(title, category, imageBitmap);
        }

        @Override
        protected void onPostExecute(DataResult result) {
            super.onPostExecute(result);

            // Tutup ProgressDialog
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // Tampilkan hasil ke UI
            titleTextView.setText("Title: " + result.title);
            categoryTextView.setText("Category: " + result.category);
            if (result.imageBitmap != null) {
                imageView.setImageBitmap(result.imageBitmap);
            } else {
                imageView.setVisibility(View.GONE); // Sembunyikan ImageView
                errorTextView.setVisibility(View.VISIBLE); // Tampilkan pesan error
            }
        }
    }

    // Kelas untuk menampung data hasil AsyncTask
    private static class DataResult {
        String title;
        String category;
        Bitmap imageBitmap;

        public DataResult(String title, String category, Bitmap imageBitmap) {
            this.title = title;
            this.category = category;
            this.imageBitmap = imageBitmap;
        }
    }
}
