package com.pkvv.fmb_tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pkvv.fmb_tracking.Fragments.DialogConfirmAdminKey;
import com.pkvv.fmb_tracking.Fragments.DialogConfirmDriverKey;
import com.pkvv.fmb_tracking.models.AdminKey;
import com.pkvv.fmb_tracking.models.DriverKey;
import com.pkvv.fmb_tracking.ui.AdminLoginActivity;
import com.pkvv.fmb_tracking.ui.DriverLoginActivity;
import com.pkvv.fmb_tracking.ui.PassangerLoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogConfirmAdminKey.OnInputListnerConfirmAdmin,DialogConfirmDriverKey.OnInputListnerConfirmDriver {
Button mDrivBtn,mPassBtn,mAdmbtn;
FirebaseFirestore fStore;
String AK,DK;
public static final String TAG ="Main";

    @Override
    public void sendInputConfirmAdmon(String input) {

        if (AK.equals(input)) {
            Intent intent3 = new Intent(MainActivity.this, AdminLoginActivity.class);
            Toast.makeText(this," ACCESS GRANTED",Toast.LENGTH_LONG).show();
            startActivity(intent3);
            finish();

        }
        else
        {
            Toast.makeText(this,"INCORRECT ADMIN KEY : ACCESS DENIED",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void sendInputConfirmDriver(String input) {
        if (DK.equals(input)) {
            Intent intent3 = new Intent(MainActivity.this, DriverLoginActivity.class);
            Toast.makeText(this," ACCESS GRANTED",Toast.LENGTH_LONG).show();
            startActivity(intent3);
            finish();

        }
        else
        {
            Toast.makeText(this,"INCORRECT DRIVER KEY : ACCESS DENIED",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrivBtn= findViewById(R.id.Drivbutton);
        mPassBtn= findViewById(R.id.Passbutton);
        mAdmbtn= findViewById(R.id.AdmButton);
        fStore=FirebaseFirestore.getInstance();

    fStore.collection("AdminKey").document("AdminKey").get().
addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if(task.isSuccessful()){
            AdminKey adminKey = task.getResult().toObject(AdminKey.class);
            AK = adminKey.getAdminKey();
            Log.d(TAG, "onComplete: AKey"+AK);
        }
    }
});
        fStore.collection("DriverKey").document("DriverKey").get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DriverKey driverKey = task.getResult().toObject(DriverKey.class);
                            DK = driverKey.getDriverKey();
                        }
                    }
                });

        mDrivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogConfirmDriverKey dialogConfirmDriverKey = new DialogConfirmDriverKey();
                dialogConfirmDriverKey.show(getSupportFragmentManager(),"DialogConfirmDriverKey");
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

                DialogConfirmAdminKey dialogConfirmAdminKey = new DialogConfirmAdminKey();
                dialogConfirmAdminKey.show(getSupportFragmentManager(),"DialogConfirmAdminKey");

            }

        });

    }



}
