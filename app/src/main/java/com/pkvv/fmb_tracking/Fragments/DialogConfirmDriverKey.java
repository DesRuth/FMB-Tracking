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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pkvv.fmb_tracking.R;

public class DialogConfirmDriverKey extends DialogFragment {
    FirebaseFirestore fStore;
    String DK;

    public interface OnInputListnerConfirmDriver{
        void sendInputConfirmDriver(String input);
    }
    public OnInputListnerConfirmDriver mOnInputListnerConfirmDriver;
    private EditText mInputCD;
    private TextView mActionOkCD,mActionCancelCD;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.driver_key_confirm_dialog,container,false);
        mActionCancelCD = view.findViewById(R.id.action_cancelCD);
        mActionOkCD = view.findViewById(R.id.action_okCD);
        mInputCD = view.findViewById(R.id.inputCD);
        fStore =FirebaseFirestore.getInstance();

        mActionCancelCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });
        mActionOkCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mInputCD.getText().toString();

            mOnInputListnerConfirmDriver.sendInputConfirmDriver(input);
            getDialog().dismiss();
            }

        });
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputListnerConfirmDriver =(OnInputListnerConfirmDriver) getActivity();
        }catch (ClassCastException e){

        }
    }
}
