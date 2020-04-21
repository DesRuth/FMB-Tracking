package com.pkvv.fmb_tracking.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.ui.AdminHomeActivity;

public class DialogCreateBus extends DialogFragment {
    public static final String TAG ="AdminHomeActivity";
    FirebaseFirestore fStore;

    public interface OnInputListner{
        void sendInput(String input);
    }
    public OnInputListner mOnInputListner;
    private EditText mInput;
    private TextView mActionOk,mActionCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_create_bus,container,false);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mActionOk = view.findViewById(R.id.action_ok);
        mInput = view.findViewById(R.id.input);
        fStore =FirebaseFirestore.getInstance();
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing");
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input");
                String input = mInput.getText().toString();
                    mOnInputListner.sendInput("Successfully added a bus");

                Buses buses = new Buses();
                buses.setBusNo(input);
                buses.setUser_id(null);
                    fStore.collection("Buses").document(input)
                            .set(buses).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
                    getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
mOnInputListner =(OnInputListner)getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage() );
        }
    }
}
