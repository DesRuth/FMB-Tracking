package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.pkvv.fmb_tracking.R;

public class PassangerMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapPassView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_map);
        mapPassView = findViewById(R.id.mapPassangerView);
        mapPassView.onCreate(savedInstanceState);

        mapPassView.getMapAsync(this);

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
