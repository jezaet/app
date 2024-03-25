package com.example.greenrover.networkTasks;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLsoa extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate;

    public GetLsoa(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(String... strings) {
        String postcode = strings[0];
        String Surl = "https://api.postcodes.io/postcodes/" + postcode;

        try {
            URL url = new URL(Surl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String json = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(json);
            String lsoa = jsonObject.getJSONObject("result").getString("lsoa");

            return lsoa;
        } catch (IOException | JSONException e) {
            e.printStackTrace();

        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }


    public interface AsyncResponse {
        void processFinish(String result);
    }
}

