package com.pkvv.fmb_tracking.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pkvv.fmb_tracking.R;

import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.models.Drivers;
import com.pkvv.fmb_tracking.models.User;

import java.util.ArrayList;
import java.util.List;

public class PassangerBusSelectActivity extends AppCompatActivity {



    private FirebaseFirestore mdb;
    public static final String TAG = "BusesList";
    EditText etxt;
    List<Buses> Listbuses=new ArrayList<>();
    int pos  ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_bus_select);
        mdb = FirebaseFirestore.getInstance();
        etxt = findViewById(R.id.etxtt);

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





        //spinner

    }

    public void getData(View view){

        for(int i=0 ; i<Listbuses.size();i++ ){
            if(i==Integer.parseInt(Listbuses.get(i).getBusNo())){
                pos = i;
                break;
            }
        }




        Intent intent =new Intent(PassangerBusSelectActivity.this,PassangerMapActivity.class);
        intent.putExtra("key_identify",Listbuses.get(pos).getUser_id());
        startActivity(intent);


    }


}
