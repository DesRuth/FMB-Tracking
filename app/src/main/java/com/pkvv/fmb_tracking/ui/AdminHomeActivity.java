package com.pkvv.fmb_tracking.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pkvv.fmb_tracking.Fragments.DialogAssignDriver;
import com.pkvv.fmb_tracking.Fragments.DialogCreateBus;
import com.pkvv.fmb_tracking.Fragments.DialogDeleteBus;
import com.pkvv.fmb_tracking.MainActivity;
import com.pkvv.fmb_tracking.R;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity implements DialogCreateBus.OnInputListner ,DialogAssignDriver.OnInputListner2, DialogDeleteBus.OnInputListner3 {
    private Button mOpenDialog,mopenDialog2,getmOpenDialog3;
    public  String busNo;
    public static final String TAG ="AdminHomeActivity";


    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: got the input"+input);
        Toast.makeText(this,input,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void sendInput2(String input) {
        Log.d(TAG, "sendInput: got the input2"+input);
        Toast.makeText(this,input,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void sendInput3(String input) {
        Log.d(TAG, "sendInput: got the input3"+input);
        Toast.makeText(this,input,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mOpenDialog =findViewById(R.id.open_dialog);
        mopenDialog2=findViewById(R.id.open_dialog2);
        getmOpenDialog3=findViewById(R.id.open_dialog3);
        mOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:openong ");
                DialogCreateBus dialogCreateBus = new DialogCreateBus();
                dialogCreateBus.show(getSupportFragmentManager(),"  DialogCreateBus");
                
            }
        });

        mopenDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:openong2 ");
                DialogAssignDriver dialogAssignDriver =new DialogAssignDriver();
                dialogAssignDriver.show(getSupportFragmentManager(),"DialogAssignDriver");
            }
        });
        getmOpenDialog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:openong3 ");
                DialogDeleteBus dialogDeleteBus =new DialogDeleteBus();
                dialogDeleteBus.show(getSupportFragmentManager(),"DialogDeleteBus");
            }
        });


    }


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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
