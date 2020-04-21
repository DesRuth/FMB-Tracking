package com.pkvv.fmb_tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkvv.fmb_tracking.ui.AdminLoginActivity;
import com.pkvv.fmb_tracking.ui.DriverLoginActivity;
import com.pkvv.fmb_tracking.ui.PassangerLoginActivity;

public class MainActivity extends AppCompatActivity {
Button mDrivBtn,mPassBtn,mAdmbtn;
FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrivBtn= findViewById(R.id.Drivbutton);
        mPassBtn= findViewById(R.id.Passbutton);
        mAdmbtn= findViewById(R.id.AdmButton);
        fStore=FirebaseFirestore.getInstance();


        mDrivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

        mPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 =new Intent(MainActivity.this, PassangerLoginActivity.class);
                startActivity(intent2);

                finish();
            }
        });

        mAdmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 =new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent3);
                finish();
            }
        });
    }

}
