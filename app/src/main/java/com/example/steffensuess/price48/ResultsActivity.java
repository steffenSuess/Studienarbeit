package com.example.steffensuess.price48;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ListView listView = (ListView) findViewById(R.id.result_list);
        TextView productName = (TextView) findViewById(R.id.product_name);
        ImageView productImage = (ImageView) findViewById(R.id.product_image);
        Intent intent = getIntent();
        new ImageLoadTask(intent.getStringExtra("productImage"), productImage).execute();

        productName.setText(intent.getStringExtra("productName"));
        ArrayList<Offer> offerList = new ArrayList<Offer>();
        offerList = (ArrayList<Offer>) intent.getSerializableExtra("offerList");
        OfferAdapter adapter = new OfferAdapter(ResultsActivity.this, R.layout.list_item, offerList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Offer selectedOffer = (Offer) parent.getAdapter().getItem(position);
                Uri uri = Uri.parse(selectedOffer.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                System.out.println("Bitmap returned");
                return myBitmap;
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }

}



