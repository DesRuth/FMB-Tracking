package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.pkvv.fmb_tracking.R;

public class DisplayMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapDrivView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);

        mapDrivView = findViewById(R.id.mapDriverView);
        mapDrivView.onCreate(savedInstanceState);

        mapDrivView.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapDrivView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapDrivView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapDrivView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {

    }

    @Override
    protected void onPause() {
        mapDrivView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapDrivView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapDrivView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapDrivView.onLowMemory();
    }
}
