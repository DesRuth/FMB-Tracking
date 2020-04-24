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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkvv.fmb_tracking.Fragments.DialogAddNotice;
import com.pkvv.fmb_tracking.Fragments.DialogAssignDriver;
import com.pkvv.fmb_tracking.Fragments.DialogCreateBus;
import com.pkvv.fmb_tracking.Fragments.DialogDeleteBus;
import com.pkvv.fmb_tracking.IAdminHomeActivity;
import com.pkvv.fmb_tracking.MainActivity;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Notice;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity implements DialogCreateBus.OnInputListner ,DialogAssignDriver.OnInputListner2, DialogDeleteBus.OnInputListner3, IAdminHomeActivity {
    private Button mOpenDialog,mopenDialog2,mOpenDialog3,mSendNoticeDialog;
    public  String busNo;
    public static final String TAG ="AdminHomeActivity";
    private View mParentLayout;


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
    public void createNewNotice(String title, String content) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newNoticeRef =db.collection("Notice").document();
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setNote_id(newNoticeRef.getId());

        newNoticeRef.set(notice).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                      Toast.makeText(getApplicationContext(),"Sucessfully Notice has been Sent",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Failed to send Notice",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mOpenDialog =findViewById(R.id.open_dialog);
        mopenDialog2=findViewById(R.id.open_dialog2);
        mOpenDialog3=findViewById(R.id.open_dialog3);
        mSendNoticeDialog =findViewById(R.id.AddANoticeDialog);
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
        mOpenDialog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:openong3 ");
                DialogDeleteBus dialogDeleteBus =new DialogDeleteBus();
                dialogDeleteBus.show(getSupportFragmentManager(),"DialogDeleteBus");
            }
        });

mSendNoticeDialog.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DialogAddNotice dialog = new DialogAddNotice();
        dialog.show(getSupportFragmentManager(), "DialogNewNotice");
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


    public void clear_Notice(View view) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("Notice").document().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
