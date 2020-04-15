package com.pkvv.fmb_tracking.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.pkvv.fmb_tracking.R;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

    }

    public void addnewBus(View view) {
        Intent intent =new Intent(AdminHomeActivity.this,AdminNewBus.class);
        startActivity(intent);
        finish();
    }
}
