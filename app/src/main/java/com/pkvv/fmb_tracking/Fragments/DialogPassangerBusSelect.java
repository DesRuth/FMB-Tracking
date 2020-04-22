package com.pkvv.fmb_tracking.Fragments;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pkvv.fmb_tracking.R;
import com.pkvv.fmb_tracking.models.Buses;
import com.pkvv.fmb_tracking.ui.PassangerMapActivity;

import java.util.ArrayList;
import java.util.List;

public class DialogPassangerBusSelect extends DialogFragment {
    public static final String TAG ="PassangerHomeActivity";
    FirebaseFirestore fStore;
    public interface OnInputListnerPass1{
        void sendInputPass1(String input);
    }
    public  OnInputListnerPass1 cmOnInputListnerPass1;
    private EditText mInputPass1;
    private TextView mActionOkPass1,mActionCancelPass1;
    List<Buses> Listbuses=new ArrayList<>();
    int pos = 0;
    int flag = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_passanger_bus_select,container,false);
        mActionCancelPass1 = view.findViewById(R.id.action_cancelPass1);
        mActionOkPass1 = view.findViewById(R.id.action_okPass1);
        mInputPass1 = view.findViewById(R.id.inputPass1);
        fStore =FirebaseFirestore.getInstance();

        fStore.collection("Buses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Listbuses.clear();


                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Buses buses = snapshot.toObject(Buses.class);
                    Listbuses.add(buses);


                }
            }
        });
        mActionCancelPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing");
                getDialog().dismiss();
            }
        });

        mActionOkPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input");
                String inputPass1 = mInputPass1.getText().toString();

                try {
                    for (int j = 0; j <= Listbuses.size(); j++) {
                        if (inputPass1.equals(Listbuses.get(j).getBusNo())) {
                            pos = j;
                            flag = 0;
                            break;
                        }
                        Log.d(TAG, "getData: loope" + j);
                    }
                }
                catch (IndexOutOfBoundsException e){
                    cmOnInputListnerPass1.sendInputPass1("Enter A valid Bus Number");
                }
                if(flag == 0) {
                    Intent intent = new Intent(getContext(), PassangerMapActivity.class);
                    intent.putExtra("key_identify", Listbuses.get(pos).getUser_id());
                    startActivity(intent);
                    getDialog().dismiss();
                }


            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            cmOnInputListnerPass1 =(OnInputListnerPass1) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException:"+e.getMessage() );
        }
    }
}
