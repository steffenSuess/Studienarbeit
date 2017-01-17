package com.example.steffensuess.price48.HttpRequestHandling;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by steffensuess on 19.12.16.
 */

public class BulkRequest {
    private String token = "BRNRIIEICXPVACWUNHLLLNBANKQXHVSPGGCGFSSMXQBQXJYINFMOLSPSYPBKDIOV";
    private static Integer refreshInterval = 30000;


    public JSONObject request(String values, String source, String country,
                              String key) {
        String st = "token=%s&source=%s&country=%s&key=%s&values=%s";
        String query = String.format(st, token, source, country, key, values);

        String response = postRequest("/jobs", query);

        System.out.println(response);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getStatus(String jobId) {
        String response = getRequest("/jobs/" + jobId, "token=" + token);

        System.out.println(response);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getResults(String jobId, String format) {
        String response = getRequest("/products/bulk/" + jobId + '.' + format,
                "token=" + token);

        System.out.println(response);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getRequest(String path, String query) {
        String response = "";

        try {
            URI uri = new URI("https", "api.priceapi.com", path, query, null);
            URL url = uri.toURL();
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response += inputLine;
            }
            br.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String postRequest(String path, String query) {
        String response = "";

        try {
            URI uri = new URI("https", "api.priceapi.com", path, null);
            URL url = uri.toURL();
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    conn.getOutputStream());
            writer.write(query);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response += inputLine;
            }
            reader.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }
}
