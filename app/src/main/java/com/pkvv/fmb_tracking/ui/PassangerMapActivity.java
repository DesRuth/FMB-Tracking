package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.CluserMarker;
import com.pkvv.fmb_tracking.models.DriverCurrentLocation;
import com.pkvv.fmb_tracking.models.PolylineData;
import com.pkvv.fmb_tracking.util.MyClusterManagerRenderer;

import java.util.ArrayList;
import java.util.List;

public class PassangerMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    MapView mapPassView;
    public static final String TAG = "Busesshow";
   private  GeoApiContext mGeoApiContext = null;
   private FirebaseFirestore mdb;
   private FirebaseAuth fAuth;


    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private int i=0;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<CluserMarker> mClusterMarker = new ArrayList<>();
    String st;
    private TextView TexDistance,TexDutarion;

    //extra
    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private Handler mHandler2 = new Handler();
    private Handler mHandler3 = new Handler();
    private static final int LOCATION_UPDATE_INTERVAL = 4000;
    private GeoPoint geoloc;
    private Runnable r3;
    private Runnable r;
    private DriverCurrentLocation mdcl;

    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();

    //extra

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_map);

        mdb = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        mapPassView = findViewById(R.id.mapPassangerView);
        TexDistance =findViewById(R.id.Tdistance);
        TexDutarion=findViewById(R.id.Tduration);
        initGoogleMap(savedInstanceState);
        st =(String) getIntent().getStringExtra("key_identify");
      //  getDriverLoc(st);
       // getUserLoc();

        //extra
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocationUpdates();


        //extra
    }
    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);
                if(mPolyLinesData.size()>0){
                    for(PolylineData polylineData:mPolyLinesData){
                        polylineData.getPolyline().remove();
                    }
                    mPolyLinesData.clear();
                    mPolyLinesData = new ArrayList<>();

                }

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.green1));
                    polyline.setClickable(true);
                    mPolyLinesData.add(new PolylineData(polyline,route.legs[0]));
                    zoomRoute(polyline.getPoints());




                }
            }
        });
    }

    private void calculateDirections(DriverCurrentLocation updatedLoc){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                geoloc.getLatitude(),
                geoloc.getLongitude()
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        updatedLoc.getGeo_point().getLatitude(),
                        updatedLoc.getGeo_point().getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                TexDistance.setText(result.routes[0].legs[0].distance.toString());
                TexDutarion.setText(result.routes[0].legs[0].duration.toString());
                addPolylinesToMap(result);

            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
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
                                mdcl = updatedDriverLocation;

                                try {


                                        LatLng updatedLatLng = new LatLng(
                                                updatedDriverLocation.getGeo_point().getLatitude(),
                                                updatedDriverLocation.getGeo_point().getLongitude()
                                        );

                                    mClusterMarker.get(1).setPosition(updatedLatLng);
                                        mClusterManagerRenderer.setUpdateMarker(mClusterMarker.get(1));
                                    calculateDirections(updatedDriverLocation);

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

        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDNFpAIhSeH62DaDBVemYXjffCRdyC5Rug")
                    .build();

        }


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
                        new LatLng(geoloc.getLatitude(), geoloc.getLongitude()),
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
            snippet = "find route to this bus";
            int avatar = R.drawable.bus_logo; // set the default avatar

            CluserMarker newClusterMarker = new CluserMarker(
                    new LatLng(15.8955074, 74.568201),
                    "bus",
                    snippet,
                    avatar

            );
            mClusterManager.addItem(newClusterMarker);
            mClusterMarker.add(newClusterMarker);

        }catch (NullPointerException e){
            Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
        }


    }


    public void settingMarkerwithDelay(){
        mHandler3 = new Handler();
        r3 = new Runnable()
        {
            public void run()
            {
                addMapMarkers();
            }
        };
        mHandler3.postDelayed(r3, 2000);
            }




    //camera view
    private void setCameraView(){

        mHandler2 = new Handler();
        r = new Runnable()
        {
            public void run()
            {
                double bottomBoundary = geoloc.getLatitude() -.001;
                double leftBoundary = geoloc.getLongitude() - .001;
                double topBoundary = geoloc.getLatitude() + .001;
                double rightBoundary = geoloc.getLongitude() + .001;

                mMapBoundary = new LatLngBounds(
                        new LatLng(bottomBoundary, leftBoundary),
                        new LatLng(topBoundary, rightBoundary)
                );

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary,0));

            }
        };
        mHandler2.postDelayed(r, 2000);



    }
    private void stopCameraView(){
        mHandler2.removeCallbacks(r);
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
        mGoogleMap.setOnPolylineClickListener(this);

        settingMarkerwithDelay();

    }

    @Override
    protected void onPause() {
        mapPassView.onPause();
        super.onPause();
        stopLocationUpdates();
        stopCameraView();
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




    @Override
    public void onPolylineClick(Polyline polyline) {

        for(PolylineData polylineData: mPolyLinesData){
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.blue1));
                polylineData.getPolyline().setZIndex(1);
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.darkGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }
}
