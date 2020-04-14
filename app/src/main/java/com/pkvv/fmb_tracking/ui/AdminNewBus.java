package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.models.DriverLocation;
import com.pkvv.fmb_tracking.models.Drivers;

public class AdminNewBus extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore mdb;
    Buses mbuses;
    EditText mBusnum,mDUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_bus);
        mBusnum = findViewById(R.id.busNo);
        mDUID = findViewById(R.id.dUID);

    }

    public void BusCreate(View view) {
        final String mBusNum = mBusnum.getText().toString().trim();
        final String mDuid = mDUID.getText().toString().trim();
        if(mbuses==null){
            mbuses = new Buses();
            DocumentReference busesRef = mdb.collection(getString(R.string.collection_drivers))
                    .document(mDuid);
            busesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Drivers drivers =task.getResult().toObject(Drivers.class);
                        mbuses.setBusNo(mBusNum);

                    }
                }
            });
            if(mbuses!=null){
                DocumentReference locationRef =mdb.collection(getString(R.string.collection_buses))
                        .document(mDuid);
                locationRef.set(mbuses).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });
            }
        }

    }
}
