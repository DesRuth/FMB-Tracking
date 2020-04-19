package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.pkvv.fmb_tracking.models.DriverCurrentLocation;
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
    String st;

    //extra
    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int LOCATION_UPDATE_INTERVAL = 4000;
    private GeoPoint geoloc;
    //extra





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


        st =(String) getIntent().getStringExtra("key_identify");
      //  getDriverLoc(st);
       // getUserLoc();

        //extra
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocationUpdates();
        //extra



       Log.d(TAG, "onComplete: driver check"+mDriverPosition.getGeo_point());



    }

    private void startUserLocationsRunnable(){
        Log.d(TAG, "startUserLocationsRunnable: starting runnable for retrieving updated locations.");
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: user location updates"+geoloc);
                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
                retrieveUserLocations();
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates(){
        mHandler.removeCallbacks(mRunnable);
    }

    private void retrieveUserLocations(){
        Log.d(TAG, "retrieveUserLocations: retrieving location of all users in the chatroom.");

        try {


            LatLng updatedLatLng = new LatLng(
                    geoloc.getLatitude(),
                    geoloc.getLongitude()
            );

            mClusterMarker.get(0).setPosition(updatedLatLng);
            mClusterManagerRenderer.setUpdateMarker(mClusterMarker.get(0));



        } catch (NullPointerException e) {
            Log.e(TAG, "retrieveUserLocations: NullPointerException: " + e.getMessage());
        }

        try{


                DocumentReference userLocationRef = FirebaseFirestore.getInstance()
                        .collection(getString(R.string.collection_driver_current_location))
                        .document(st);

                userLocationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            final DriverCurrentLocation updatedDriverLocation = task.getResult().toObject(DriverCurrentLocation.class);

                                try {


                                        LatLng updatedLatLng = new LatLng(
                                                updatedDriverLocation.getGeo_point().getLatitude(),
                                                updatedDriverLocation.getGeo_point().getLongitude()
                                        );

                                    mClusterMarker.get(1).setPosition(updatedLatLng);
                                        mClusterManagerRenderer.setUpdateMarker(mClusterMarker.get(1));

                                } catch (NullPointerException e) {
                                    Log.e(TAG, "retrieveUserLocations: NullPointerException: " + e.getMessage());
                                }

                        }
                    }
                });

        }catch (IllegalStateException e){
            Log.e(TAG, "retrieveUserLocations: Fragment was destroyed during Firestore query. Ending query." + e.getMessage() );
        }

    }



    public void getUserLocationUpdates(){


        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        Log.d(TAG, "onLocationResult: got location result.");

                        Location location = locationResult.getLastLocation();

                        if (location != null) {

                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            DriverCurrentLocation driverCurrentLocation = new DriverCurrentLocation(FirebaseAuth.getInstance().getUid(), geoPoint, null);
                                        geoloc=geoPoint;
                        }
                    }
                },
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
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


    @Override
    protected void onResume() {
        super.onResume();
        mapPassView.onResume();
        startUserLocationsRunnable();
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
        stopLocationUpdates();
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
