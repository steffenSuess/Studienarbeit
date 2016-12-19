package com.example.steffensuess.price48;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.steffensuess.price48.R.id.price;

public class MainActivity extends AppCompatActivity {

    TextView barcodeResult;
    String ean;

    String TAG = MainActivity.class.getSimpleName();
    ListView listView;

    ArrayList<HashMap<String, String>> offerList;

    //List<Offer> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeResult = (TextView)findViewById(R.id.barcode_result);

        offerList =  new ArrayList<HashMap<String, String>>();
        listView = (ListView)findViewById(R.id.list);

        //new GetContacts().execute();
    }

    public void scanBarcode(View view){
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    public void listItemClick(View view){

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeResult.setText("Barcode value: " + barcode.displayValue);
                    ean = barcode.displayValue;
                    new GetContacts().execute();
                }else{
                    barcodeResult.setText("No barcode found!");
                    ean = "";
                }
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

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

            Log.e(TAG, "Response from url: " + response);
            if (response != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node

                    JSONObject test = response.getJSONArray("products").getJSONObject(0);
                    JSONArray contacts = test.getJSONArray("offers");

                    // looping through All Offers
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
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
                        HashMap<String, String> offer = new HashMap<>();
//                        Offer offer = new Offer();
//
//                        // adding each child node to HashMap key => value
//                        offer.shop_Name = shop_name;
//                        offer.price = price;
//                        offer.price_With_Shipping = price_with_shipping;
//                        offer.currency = currency;
//                        offer.url = offerURL;

                        offer.put("shop_name",shop_name);
                        offer.put("price", price);
                        offer.put("price_with_shipping", price_with_shipping);
                        offer.put("shipping_costs", shipping_costs);
                        offer.put("currency", currency);
                        offer.put("url", offerURL);

                        // adding contact to offer list
                        offerList.add(offer);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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
                Log.e(TAG, "Couldn't get json from server.");
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
            super.onPostExecute(result);
            //ListAdapter adapter = new ArrayAdapter<Offer>(MainActivity.this, R.layout.list_item, offerList);

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, offerList,
                    R.layout.list_item, new String[]{ "shop_name","price_with_shipping"},
                    new int[]{R.id.shop_name, price});
            listView.setAdapter(adapter);
        }
    }

    }
