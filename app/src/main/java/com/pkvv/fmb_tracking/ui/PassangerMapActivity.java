package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.GeoApiContext;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.models.DriverLocation;

public class PassangerMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapPassView;
    public static final String TAG = "Busesshow";
   private  GeoApiContext mGeoApiContext = null;
   private FirebaseFirestore mdb;
   private FirebaseAuth fAuth;
    private DriverLocation mDriverPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_map);

        mdb = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();




        mapPassView = findViewById(R.id.mapPassangerView);
        mapPassView.onCreate(savedInstanceState);

        mapPassView.getMapAsync(this);



    }


    //geting driver location
    public  void getDriverLoc(String st){
        mDriverPosition = new DriverLocation();
        DocumentReference locationRef = mdb.collection(getString(R.string.collection_driver_location))
                .document("Sp7Szy1ivfYvIwsGM7RHVGd8UUy2");
        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().toObject(DriverLocation.class)!=null){
                        mDriverPosition.setDrivers(task.getResult().toObject(DriverLocation.class).getDrivers());
                        mDriverPosition.setGeo_point(task.getResult().toObject(DriverLocation.class).getGeo_point());
                        mDriverPosition.setTimestamp(task.getResult().toObject(DriverLocation.class).getTimestamp());
                        Toast.makeText(PassangerMapActivity.this,"Geo point"+mDriverPosition.getGeo_point(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapPassView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapPassView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapPassView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        String st =(String) getIntent().getStringExtra("key_identify");
        getDriverLoc(st);

    }

    @Override
    protected void onPause() {
        mapPassView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapPassView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapPassView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapPassView.onLowMemory();
    }
}
