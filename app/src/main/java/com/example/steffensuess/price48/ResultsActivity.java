package com.example.steffensuess.price48;

import android.app.SearchManager;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    String ean;

    ListView listView;
    TextView productName;
    ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        handleIntent(getIntent());
        listView = (ListView) findViewById(R.id.result_list);
        productName = (TextView) findViewById(R.id.product_name);
        productImage = (ImageView) findViewById(R.id.product_image);
        //Intent intent = getIntent();

//        ArrayList<Offer> offerList = new ArrayList<Offer>();
//        offerList = (ArrayList<Offer>) intent.getSerializableExtra("offerList");
//        OfferAdapter adapter = new OfferAdapter(ResultsActivity.this, R.layout.list_item, offerList);
//        listView.setAdapter(adapter);

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);


            ean = query;
        }

        if(ean != null)
            new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        List<Offer> offerList;
        String imageURL;
        String name;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultsActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
            offerList = new ArrayList<Offer>();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();



            JSONObject response = null;
            BulkRequest bulk = new BulkRequest();

            JSONObject bulkStatus = bulk.request(ean,
                    "google-shopping", "de", "gtin");
            String jobId = "";
            try {
                jobId = (String) bulkStatus.get("job_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
                //return;
            }

            Boolean done = false;
            while (!done) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bulkStatus = bulk.getStatus(jobId);

                Boolean isComplete = false;
                try {
                    String status = (String) bulkStatus.get("status");
                    isComplete = status.equals("finished");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isComplete) {
                    response = bulk.getResults(jobId, "json");
                    done = true;
                }
            }
            System.out.println(response.toString());


            // Making a request to url and getting response
            //String url = "http://api.androidhive.info/contacts/";
            //String jsonStr = sh.makeServiceCall(url, "GET");

//            Log.e(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node

                    JSONObject products = response.getJSONArray("products").getJSONObject(0);
                    imageURL = products.getString("image_url");
                    name = products.getString("name");
                    JSONArray offers = products.getJSONArray("offers");

                    // looping through All Offers
                    for (int i = 0; i < offers.length(); i++) {
                        JSONObject c = offers.getJSONObject(i);
                        String shop_name = c.getString("shop_name");
                        String price = c.getString("price");
                        String price_with_shipping = c.getString("price_with_shipping");
                        String shipping_costs = c.getString("shipping_costs");
                        String currency = c.getString("currency");
                        String offerURL = c.getString("url");

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        //HashMap<String, String> offer = new HashMap<>();
                        Offer offer = new Offer();
//
//                        // adding each child node to HashMap key => value
                        offer.setProductName(name);
                        offer.setProductImage(imageURL);
                        offer.shop_Name = shop_name;
                        offer.price = price;
                        offer.price_With_Shipping = price_with_shipping;
                        offer.currency = currency;
                        offer.url = offerURL;

//                        offer.put("shop_name",shop_name);
//                        offer.put("price", price);
//                        offer.put("price_with_shipping", price_with_shipping);
//                        offer.put("shipping_costs", shipping_costs);
//                        offer.put("currency", currency);
//                        offer.put("url", offerURL);

                        // adding contact to offer list
                        offerList.add(offer);
                    }
                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
//                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            OfferAdapter adapter = new OfferAdapter(MainActivity.this, R.layout.list_item, offerList);
//
////            ListAdapter adapter = new SimpleAdapter(MainActivity.this, offerList,
////                    R.layout.list_item, new String[]{ "shop_name","price_with_shipping"},
////                    new int[]{R.id.shop_name, price});
//            //listView.setAdapter(adapter);
//            Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
//            intent.putExtra("offerList", (Serializable) offerList);
//            intent.putExtra("productName", offerList.get(0).getProductName());
//            intent.putExtra("productImage", offerList.get(0).getProductImage());
////            ListView test = (ListView) findViewById(R.id.result_list);
////            test.setAdapter(adapter);
//            startActivity(intent);
            new ImageLoadTask(imageURL, productImage).execute();

            productName.setText(name);
            OfferAdapter adapter = new OfferAdapter(ResultsActivity.this, R.layout.list_item, offerList);
            listView.setAdapter(adapter);
        }
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



