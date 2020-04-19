package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.model.DirectionsResult;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.models.CluserMarker;
import com.pkvv.fmb_tracking.models.DriverLocation;
import com.pkvv.fmb_tracking.models.User;
import com.pkvv.fmb_tracking.models.UserLocation;
import com.pkvv.fmb_tracking.util.MyClusterManagerRenderer;

import java.util.ArrayList;

public class PassangerMapActivity extends AppCompatActivity implements OnMapReadyCallback  {

    MapView mapPassView;
    public static final String TAG = "Busesshow";
   private  GeoApiContext mGeoApiContext = null;
   private FirebaseFirestore mdb;
   private FirebaseAuth fAuth;
    private DriverLocation mDriverPosition;
    private UserLocation mUserPosition;
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private int i=0;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<CluserMarker> mClusterMarker = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_map);

        mdb = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();




        mapPassView = findViewById(R.id.mapPassangerView);
        initGoogleMap(savedInstanceState);


        mUserPosition = new UserLocation();
        mDriverPosition = new DriverLocation();


        String st =(String) getIntent().getStringExtra("key_identify");
        getDriverLoc(st);
        getUserLoc();




       Log.d(TAG, "onComplete: driver check"+mDriverPosition.getGeo_point());



    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapPassView.onCreate(mapViewBundle);
        mapPassView.getMapAsync(this);
    }


    //marker
    private void addMapMarkers(){

        if(mGoogleMap != null){

            if(mClusterManager == null){
                mClusterManager = new ClusterManager<CluserMarker>(getApplicationContext(), mGoogleMap);
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        this,
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }
            forUSerLocationMarker();
            forDriverLocationMarker();

            mClusterManager.cluster();

            setCameraView();
        }
    }

    public void forUSerLocationMarker(){


            Log.d(TAG, "addMapMarkers: location: " + "{ latitude=15.8555074, longitude=74.5128201 }");
            try{
                String snippet = "";
                    snippet = "This is you";
                int avatar = R.drawable.userlogo; // set the default avatar

                CluserMarker newClusterMarker = new CluserMarker(
                        new LatLng(15.8555074, 74.5128201),
                        "You",
                        snippet,
                        avatar

                );
                mClusterManager.addItem(newClusterMarker);
                mClusterMarker.add(newClusterMarker);

            }catch (NullPointerException e){
                Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
            }


    }

    public void forDriverLocationMarker(){


        Log.d(TAG, "addMapMarkers: location: " + "{ latitude=15.8755074, longitude=74.5428201 }");
        try{
            String snippet = "";
            snippet = "Bus";
            int avatar = R.drawable.bus_logo; // set the default avatar

            CluserMarker newClusterMarker = new CluserMarker(
                    new LatLng(15.8755074, 74.548201),
                    "Driver",
                    snippet,
                    avatar

            );
            mClusterManager.addItem(newClusterMarker);
            mClusterMarker.add(newClusterMarker);

        }catch (NullPointerException e){
            Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
        }


    }


    //camera view
    private void setCameraView(){
        double bottomBoundary = 15.8555074 - .1;
        double leftBoundary = 74.5128201 - .1;
        double topBoundary = 15.8555074 + .1;
        double rightBoundary = 74.5128201 + .1;

        mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary,0));
    }



    //geting driver location
    public  void getDriverLoc(String st){

        DocumentReference locationRef = mdb.collection(getString(R.string.collection_driver_location))
                .document(st);
        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().toObject(DriverLocation.class)!=null){
                        mDriverPosition.setDrivers(task.getResult().toObject(DriverLocation.class).getDrivers());
                        mDriverPosition.setGeo_point(task.getResult().toObject(DriverLocation.class).getGeo_point());
                        mDriverPosition.setTimestamp(task.getResult().toObject(DriverLocation.class).getTimestamp());


                    }
                }
            }
        });

    }

   public void  getUserLoc(){


        DocumentReference locationRef = mdb.collection(getString(R.string.collection_user_location))
                .document(FirebaseAuth.getInstance().getUid());

        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().toObject(UserLocation.class)!=null){
                        mUserPosition.setUser(task.getResult().toObject(UserLocation.class).getUser());
                        mUserPosition.setGeo_point(task.getResult().toObject(DriverLocation.class).getGeo_point());
                        mUserPosition.setTimestamp(task.getResult().toObject(DriverLocation.class).getTimestamp());

                    }
                }
                Log.d(TAG, "onComplete: user inside uio"+mUserPosition.getGeo_point());

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

        //map.setMyLocationEnabled(true);
        mGoogleMap=map;

        addMapMarkers();




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
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapPassView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapPassView.onLowMemory();
    }
}
