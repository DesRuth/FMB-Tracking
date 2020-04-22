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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkvv.fmb_tracking.R;

public class DialogDeleteBus extends DialogFragment {
    public static final String TAG ="AdminHomeActivity";
    FirebaseFirestore fStore;
    public interface OnInputListner3{
        void sendInput3(String input);
    }
    public OnInputListner3 mOnInputListner3;
    private EditText mInput3;
    private TextView mActionOk3,mActionCancel3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_delete_bus,container,false);
        mActionCancel3 = view.findViewById(R.id.action_cancel3);
        mActionOk3 = view.findViewById(R.id.action_ok3);
        mInput3 = view.findViewById(R.id.input3);
        fStore =FirebaseFirestore.getInstance();

        mActionCancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing");
                getDialog().dismiss();
            }
        });

        mActionOk3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input");
                String input3 = mInput3.getText().toString();
                fStore.collection("Buses").document(input3).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mOnInputListner3.sendInput3("Selected bus is successfully deleted");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mOnInputListner3.sendInput3("Failed to delete the bus");
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputListner3 =(OnInputListner3) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage() );
        }
    }
}
