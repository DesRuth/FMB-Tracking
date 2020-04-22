package com.pkvv.fmb_tracking.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class DialogAssignDriver extends DialogFragment {
    public static final String TAG ="AdminHomeActivity";
    FirebaseFirestore fStore;
    List<Drivers> listDriver =new ArrayList<>();



    public interface OnInputListner2{
        void sendInput2(String input);
    }
    public OnInputListner2 mOnInputListner2;
    private EditText mInput2a,mInput2b;
    private TextView mActionOk2,mActionCancel2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_assign_driver,container,false);
        mActionCancel2 = view.findViewById(R.id.action_cancel2);
        mActionOk2 = view.findViewById(R.id.action_ok2);
        mInput2a = view.findViewById(R.id.input2a);
        mInput2b = view.findViewById(R.id.input2b);



        fStore =FirebaseFirestore.getInstance();

        fStore.collection("Drivers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                listDriver.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Drivers drivers = snapshot.toObject(Drivers.class);
                    listDriver.add(drivers);
                }
            }
        });



        mActionCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing");
                getDialog().dismiss();
            }
        });



        mActionOk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input2");
                String input2a = mInput2a.getText().toString();
                String input2b = mInput2b.getText().toString();




                int posi = 0;

                for(int i=0;i<=listDriver.size();i++){
                    if(input2b.equals(listDriver.get(i).getUsername())){
                        Log.d(TAG, "onClick: got it"+input2b+listDriver.get(i).getUsername());
                        posi = i;
                        break;
                    }
                }
                        Buses buses =new Buses();
                buses.setBusNo(input2a);
                buses.setUser_id(listDriver.get(posi).getUser_id());
                fStore.collection("Buses").document(input2a)
                        .set(buses).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "driver successfully Assigned!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Something went wrong");
                    }
                });



                mOnInputListner2.sendInput2("Successfully driver is assigned");
                getDialog().dismiss();


            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputListner2 =(DialogAssignDriver.OnInputListner2)getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage() );
        }
    }
}
