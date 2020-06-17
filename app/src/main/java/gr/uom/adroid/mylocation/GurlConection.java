package gr.uom.adroid.mylocation;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GurlConection extends AsyncTask<Void, Void, Void> {
    String ip;
    StringBuilder data = new StringBuilder();
    MainPageActivity setIP;
    String lat,lng;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyAszYzHyFkeGp-d-5CY0cI3LO1rXApSxew");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            line = bufferedReader.readLine();
            while (line!=null){
                data.append(line);
                line = bufferedReader.readLine();
            }
            Log.d("KAPPA", "doInBackground: " + data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject((data).toString());
            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lat").toString();
            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lng").toString();
            Log.d("KAPPA", "doInBackground: "+lat.toString());
            Log.d("KAPPA", "lng "+lng.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("KAPPA", "fail");
            Log.d("KAPPA", "fail");

        }
        return null;
    }
    public String getIP(){
        return ip;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}

