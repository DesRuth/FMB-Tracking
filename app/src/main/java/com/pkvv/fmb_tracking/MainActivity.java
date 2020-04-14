package com.pkvv.fmb_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pkvv.fmb_tracking.ui.AdminLoginActivity;
import com.pkvv.fmb_tracking.ui.DriverLoginActivity;
import com.pkvv.fmb_tracking.ui.PassangerLoginActivity;

public class MainActivity extends AppCompatActivity {
Button mDrivBtn,mPassBtn,mAdmbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrivBtn= findViewById(R.id.Drivbutton);
        mPassBtn= findViewById(R.id.Passbutton);
        mAdmbtn= findViewById(R.id.AdmButton);

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
