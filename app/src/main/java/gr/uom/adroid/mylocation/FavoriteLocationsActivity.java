package gr.uom.adroid.mylocation;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteLocationsActivity extends AppCompatActivity {


    EditText txt;
    Button addBTD,deleteBTN;
    ArrayList<String> favlocationsArray;
    ListView favDataList;
    String user_id;
    FirebaseDatabase locationsOnlineDB;
    String favListId,aName,aLat,aLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_locations);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("ID");

        locationsOnlineDB = FirebaseDatabase.getInstance();
        final DatabaseReference aref = locationsOnlineDB.getReference("USERS").child(user_id).child("LOCATIONS");
        deleteBTN = findViewById(R.id.deleteBTN);
        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aref.child(favListId).removeValue();
            }
        });

        aref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favlocationsArray = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String loadedData = ds.getValue().toString();
                    favlocationsArray.add(loadedData);
                    favListViewer();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void favListViewer(){
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.activity_datas,R.id.dataTXT,favlocationsArray);
        favDataList = findViewById(R.id.favDataList);
        favDataList.setAdapter(adapter);
        favDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = favDataList.getItemAtPosition(position);
                String astring=(String)o;
                String listSTR[] = astring.split(" ");
                favListId = listSTR[5];
                aName = listSTR[0];
                aLat = listSTR[4];
                aLng = listSTR[2];
                Log.d("KAPPA", "\nID: "+favListId+"\n"+aName +"Lat: "+aLat + "\nLng: "+ aLng);

            }
        });
    }

    public void openBTN(View v){
        Intent i = new Intent (Intent.ACTION_VIEW);
        String geo = "google.navigation:q= " +aLng +"," +aLat;

        i.setData(Uri.parse(geo));
        startActivity(i);
    }


}



