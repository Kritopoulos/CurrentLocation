package gr.uom.adroid.mylocation;

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

public class IPhandler extends AsyncTask<Void, Void, Void> {
    String ip;
    StringBuilder data = new StringBuilder();
    MainPageActivity setIP;


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://ip.jsontest.com/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            line = bufferedReader.readLine();
            while (line!=null){
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
            ip = jsonObject.get("ip").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        setIP = new MainPageActivity();
        setIP.setIP(ip);

    }
}
