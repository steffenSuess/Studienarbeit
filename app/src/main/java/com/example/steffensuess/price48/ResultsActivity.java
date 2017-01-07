package com.example.steffensuess.price48;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ResultsActivity extends AppCompatActivity {

    String searchText;


    DatabaseHandler db;

    ListView listView;
    TextView productName;
    ImageView productImage;
    String key;
    String name;
    int refreshInterval;
    final static String noImageFoundURL = "http://www.jordans.com/~/media/jordans%20redesign/no-image-found.ashx?h=275&la=en&w=275&hash=F87BC23F17E37D57E2A0B1CC6E2E3EEE312AAD5B";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        listView = (ListView) findViewById(R.id.result_list);
        productName = (TextView) findViewById(R.id.product_name);
        productImage = (ImageView) findViewById(R.id.product_image);
        db = new DatabaseHandler(this);
        handleIntent(getIntent());

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


            searchText = query;
        }

        if(searchText != null) {
            if(searchTextIsEANNumber(searchText)){
                key = "gtin";
            }else {
                key = "keyword";
            }
            new ImageLoadTask(noImageFoundURL, productImage).execute();
            refreshInterval = 1000;
            new GetOffers().execute();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private boolean searchTextIsEANNumber(String searchText) {
        try{
            Long.parseLong(searchText);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private class GetOffers extends AsyncTask<Void, Void, Void> {

        List<Offer> offerList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultsActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
            offerList = new ArrayList<Offer>();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            JSONObject response = null;
            BulkRequest bulk = new BulkRequest();

            JSONObject bulkStatus = bulk.request(searchText,
                    "geizhals", "de", key);
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
                    Thread.sleep(refreshInterval);
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

            if (response != null) {
                try {

                    JSONObject products = response.getJSONArray("products").getJSONObject(0);

                    name = products.getString("name");
                    JSONArray offers = products.getJSONArray("offers");

                    // looping through All Offers
                    for (int i = 0; i < offers.length(); i++) {
                        JSONObject c = offers.getJSONObject(i);
                        String shop_name = c.getString("shop_name");

                        Offer offer = new Offer();

                        offer.setProductName(name);
                        offer.setShop_Name(shop_name);
                        offer.setPrice(c.getString("price"));
                        offer.setPrice_With_Shipping(c.getString("price_with_shipping"));
                        offer.setCurrency(c.getString("currency"));
                        offer.setUrl(c.getString("url"));
                        offer.setCost_For_Shipping(c.getString("shipping_costs"));
                        offer.setAvailability(c.getString("availability_code"));


                        // adding offer to offer list
                        offerList.add(offer);
                    }
                } catch (final JSONException e) {

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

            productName.setText(name);
            OfferAdapter adapter = new OfferAdapter(ResultsActivity.this, R.layout.list_item, offerList);
            listView.setAdapter(adapter);

            if(offerList.size()>0){
                Offer cheapestOffer = new Offer();
                cheapestOffer = offerList.get(0);
                SearchQuery searchQuery = new SearchQuery();
                searchQuery.setProductName(name);
                searchQuery.setImageURL(noImageFoundURL);
                searchQuery.setShopName(cheapestOffer.getShop_Name());
                searchQuery.setSearchText(searchText);
                searchQuery.setPrice(cheapestOffer.getPrice());
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                Date date = today.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                searchQuery.setDate(dateFormat.format(date));
                db.addQuery(searchQuery);
            }
            if(searchTextIsEANNumber(searchText))
                searchText = name;
            new GetProductImage().execute();
        }
    }

    private class GetProductImage extends AsyncTask<Void, Void, Void>{
        final String count = "10";
        final String mkt = "de-de";

        JSONObject imageJson;
        String imageURL;

        @Override
        protected Void doInBackground(Void... params) {
            //search = search.replace(" ", "%20");
            String accountKey = "9a2f826a855140eca2e6357bc0238b2f";
            byte[] accountKeyBytes = Base64.encode(accountKey.getBytes(), Base64.DEFAULT);
            String accountKeyEnc = new String(accountKeyBytes);
            try {
                URL url = new URL("https://api.cognitive.microsoft.com/bing/v5.0/images/search?q="+searchText+"&count="+count+"&offset=0&mkt="+mkt+"&safeSearch=Moderate&imageType=Transparent");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "9a2f826a855140eca2e6357bc0238b2f");


                int status = urlConnection.getResponseCode();



                switch (status) {

                    case 200:
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        imageJson = new JSONObject(sb.toString());
                }

                if(imageJson != null){
                    JSONObject value = imageJson.getJSONArray("value").getJSONObject(0);
                    imageURL = value.getString("thumbnailUrl");
                }



            } catch (MalformedURLException e) {
                System.out.println("MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                System.out.println("ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage() + e.getCause());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!imageURL.equals(null) && !imageURL.isEmpty() && !imageURL.equals("null")){
                new ImageLoadTask(imageURL, productImage).execute();
                SearchQuery searchQuery = new SearchQuery();
                searchQuery = db.getQuery(db.getQueriesCount());
                searchQuery.setImageURL(imageURL);
                db.updateQuery(searchQuery);
            }
        }
    }

}



