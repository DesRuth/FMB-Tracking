package com.pkvv.fmb_tracking.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.models.Drivers;

import java.util.ArrayList;
import java.util.List;

public class PassangerBusSelectActivity extends AppCompatActivity {
    private List<Buses> Listbuses=new ArrayList<>();
    private FirebaseFirestore mdb;
    public static final String TAG = "BusesList";
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_bus_select);
        mdb = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.spinner);


       //spinner

        mdb.collection("Buses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Listbuses.clear();


                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Buses buses = snapshot.toObject(Buses.class);
                    Listbuses.add(buses);
                }
            }
        });
        ArrayAdapter<Buses> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,Listbuses);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinner

    }

    public void getData(View view){

    Buses buses =(Buses)spinner.getSelectedItem();
        Intent intent =new Intent(PassangerBusSelectActivity.this,PassangerMapActivity.class);
        intent.putExtra("key_identify",buses);
        startActivity(intent);

    }
}
