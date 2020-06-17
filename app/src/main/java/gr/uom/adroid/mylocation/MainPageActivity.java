package gr.uom.adroid.mylocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private static TextView cordsTXT, iptxt;
    EditText nameTXT;
    IPhandler ip;
    URLconection urlGetCords;
    private static String ipconection, incomeLNG, incomeLAT;
    String user_id;
    LocationsBD dbLocation;
    String acceess = "false";
    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        cordsTXT = findViewById(R.id.cordsTXT);
        nameTXT = findViewById(R.id.nameTXT);
        iptxt = findViewById(R.id.ipTXT);

        Intent intent = getIntent();
        acceess = intent.getStringExtra("ACCESS");
        user_id = intent.getStringExtra("ID");

        Log.d("KAPPA", "favoriteLocations on create: " + acceess);
        dbLocation = new LocationsBD(this);
        ip = new IPhandler();
        ip.execute();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setCords(String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

    }

    public void myLocationsBTN(View v) {
        Log.d("KAPPA", "BUTTON PRESSED");

        Intent intent = new Intent(MainPageActivity.this, LocationsActivity.class);
        intent.putExtra("ACCESS", acceess);
        intent.putExtra("ID", user_id);

        Log.d("KAPPA", "favBTNid " + user_id);
        intent.putExtra("ID", user_id);
        startActivity(intent);
    }

    public void favoriteLocationsBTN(View v) {

        if (acceess.equals("true")) {
            Intent LocationsIntent = new Intent(MainPageActivity.this, FavoriteLocationsActivity.class);
            LocationsIntent.putExtra("ID", user_id);
            startActivity(LocationsIntent);
        } else {
            Toast.makeText(MainPageActivity.this, "YOU NEED TO CONECT WITH EMAIL", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocationBTN(View v) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }

    public void SaveLocationBTN(View v){
        //NEED TO GIVE A NAME TO LOCATION.

        String incomeName = nameTXT.getText().toString();
        boolean checkForGabsInName = false;
        int spaceCount = 0;
        for (char c : incomeName.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        if(spaceCount == 0){
            checkForGabsInName =true;
        }
        if(checkForGabsInName) {
            if(incomeLAT == null || incomeLNG == null){
                Toast.makeText(MainPageActivity.this,"Plaese press the button to get cords",Toast.LENGTH_SHORT).show();
            }
            else {
                if(incomeName.equals(null)|| incomeName.equals("")){
                    Toast.makeText(MainPageActivity.this, "Give a name", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean isInserted = dbLocation.insertData(incomeName, incomeLAT.toString(), incomeLNG.toString());

                    if (isInserted == true) {
                        Toast.makeText(MainPageActivity.this, "Succesfully saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainPageActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else{
            Toast.makeText(MainPageActivity.this,"Please do not put spaces",Toast.LENGTH_SHORT).show();
        }
    }

    public void setIP(String astring){
        ipconection = astring;
        iptxt.setText("IP: " + astring);
    }

    public  void setCords(String aLat,String aLng){
        incomeLAT = aLat;
        incomeLNG = aLng;
        cordsTXT.setText("Latitude: " +aLat+"\nLongitude: "+aLng);
    }

}

