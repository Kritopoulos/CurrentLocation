package gr.uom.adroid.mylocation;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.util.ArrayList;

public class LocationsActivity extends AppCompatActivity implements Serializable {

    ListView dataList;
    String listID,Lat,Lng,Name;
    ArrayList <String> locationsArray;
    LocationsBD dbLocation;
    String access;
    String user_id;
    private FirebaseAuth anAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        Intent getInfo = getIntent();
        user_id = getInfo.getStringExtra("ID");

        Log.d("KAPPA", "InLocationsActivity "+user_id);
        Intent intent = getIntent();
        access = intent.getStringExtra("ACCESS");

        listViewer();

    }

    public void deleteDATA(View v){
        int check = dbLocation.deleteData(listID);
        if(check > 0 ){
            Toast.makeText(LocationsActivity.this,"Succesfully deleted",Toast.LENGTH_SHORT).show();
            listViewer();
        }
        else{
            Toast.makeText(LocationsActivity.this,"IDIOT IDIOT IDIOT",Toast.LENGTH_SHORT).show();
        }
    }

    public void openDATA(View v){
        Intent i = new Intent (Intent.ACTION_VIEW);
        String geo = "google.navigation:q="+Lng +"," +Lat;

        i.setData(Uri.parse(geo));
        startActivity(i);

    }

    public static void restartActivity(Activity act){
        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.finish();

    }

    public void listViewer(){
        locationsArray = new ArrayList<>();
        dbLocation = new LocationsBD(this);
        Cursor res = dbLocation.getAllData();
        if(res.getColumnCount() == 0 ){
            locationsArray.add("Something happend. Could not load files.");
        }
        else {
            while (res.moveToNext()) {
                locationsArray.add(res.getString(1) +" \nLAT: " + res.getString(2) +" LNG: " + res.getString(3) +" \n\n\n\n\n "+res.getString(0));
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.activity_datas,R.id.dataTXT,locationsArray);
        dataList = findViewById(R.id.dataLIST);
        dataList.setAdapter(adapter);
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = dataList.getItemAtPosition(position);
                String astring=(String)o;
                String listSTR[] = astring.split(" ");
                listID = listSTR[6];
                Name = listSTR[0];
                Lat = listSTR[2];
                Lng = listSTR[4];
                Log.d("KAPPA", "\nID: "+listID+"\n" +" Lat: "+Lat + " Lng: "+ Lng);

            }
        });

    }

    public void addToFavBTN(View v){
        if(access.equals("true")){

            final FirebaseDatabase locationsOnlineDB = FirebaseDatabase.getInstance();
            DatabaseReference aref = locationsOnlineDB.getReference("USERS").child(user_id);
            String post;

            post =Name + " \nLng: " +Lng + " ,Lat: " +Lat + " " +listID;
            Log.d("KAPPA", "addToFavBTN: "+post.toString());
            if( listID != null && Lat  != null && Lng != null){
                aref.child("LOCATIONS").child(listID).setValue(post).toString();
                Toast.makeText(LocationsActivity.this,"Succesfully added",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LocationsActivity.this,"Select a location",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(LocationsActivity.this,"You are not conected or the developer sucks.",Toast.LENGTH_SHORT).show();
        }
    }
}

