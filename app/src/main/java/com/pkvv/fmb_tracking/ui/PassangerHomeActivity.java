package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import com.pkvv.fmb_tracking.Fragments.DialogPassangerBusSelect;
import com.pkvv.fmb_tracking.Fragments.FragmentTimeTableView;
import com.pkvv.fmb_tracking.MainActivity;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Notice;

import java.util.ArrayList;
import java.util.List;

import static com.pkvv.fmb_tracking.Constants.ERROR_DIALOG_REQUEST;
import static com.pkvv.fmb_tracking.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.pkvv.fmb_tracking.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class PassangerHomeActivity extends AppCompatActivity implements DialogPassangerBusSelect.OnInputListnerPass1 {
    public static final String TAG ="PassangerHomeActivity";

    FirebaseAuth fAuth;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;

    private FirebaseFirestore mdb;
    private Button mOpenDialogPass1;

    private RecyclerView recyclerView;

    private FirestoreRecyclerAdapter adapter;





    @Override
    public void sendInputPass1(String input) {
        Log.d(TAG, "sendInput: got the input"+input);
        Toast.makeText(this,input,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_home);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fAuth = FirebaseAuth.getInstance();
        mdb = FirebaseFirestore.getInstance();
        mOpenDialogPass1 = findViewById(R.id.open_dialog_pass1);
        recyclerView =findViewById(R.id.recyclerView);

        Query query = mdb.collection("Notice");
        FirestoreRecyclerOptions<Notice> options = new FirestoreRecyclerOptions.Builder<Notice>().setQuery(query,Notice.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Notice, NoticeViewHolder>(options) {
            @NonNull
            @Override
            public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row,parent,false);
                return new NoticeViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull NoticeViewHolder holder, int position, @NonNull Notice model) {
            holder.LTitle.setText(model.getTitle());
            holder.LContent.setText(model.getContent());
            holder.LDate.setText(model.getTimestamp().toString());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




        mOpenDialogPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:openong3 ");
                DialogPassangerBusSelect dialogPassangerBusSelect =new DialogPassangerBusSelect();
                dialogPassangerBusSelect.show(getSupportFragmentManager(),"DialogPassangerBusSelect");



            }
        });
    }

    //start
    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        //tag Log.d(TAG, "isServicesOK: checking google services version");
        //tag  Log.d(TAG, "isServicesOK: ");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PassangerHomeActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            //tag Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            //tagLog.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PassangerHomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //tag Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {


                } else {
                    getLocationPermission();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {

            } else {
                getLocationPermission();
            }

        }
    }

    //end

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ViewTimeTable(View view) {
        FragmentManager f2 = getSupportFragmentManager();
        f2.beginTransaction().replace(R.id.passangerTT,new FragmentTimeTableView()).addToBackStack(null).commit();
    }


    private class NoticeViewHolder extends RecyclerView.ViewHolder {
        private TextView LTitle;
        private TextView LContent;
        private TextView LDate;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            LTitle = itemView.findViewById(R.id.title_recy);
            LContent = itemView.findViewById(R.id.content_recy);
            LDate = itemView.findViewById(R.id.date_recy);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

