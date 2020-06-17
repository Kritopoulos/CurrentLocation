package gr.uom.adroid.mylocation;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLconection extends AsyncTask<Void,Void,Void> {

    StringBuilder data = new StringBuilder();
    String lat = "";
    String lng = "";
    String aUrl="";
    String incomeIP;



    public URLconection(String astring){
        aUrl="http://api.ipstack.com/" +astring +"?access_key=3a80660b0222fd03253f97a1ecfa0f97";
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL(aUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            line = bufferedReader.readLine();
            while (line !=null){
                data.append(line);
                line = bufferedReader.readLine();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data.toString());
            lat = jsonObject.get("latitude").toString();
            lng = jsonObject.get("longitude").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainPageActivity mainPageActivity = new MainPageActivity();
        mainPageActivity.setCords(lat,lng);

    }

}
