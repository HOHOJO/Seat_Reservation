package com.cookandroid.client;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {
    Bundle bundle;
    View view;
    String id,pw,result,name, stare, seat,result2;
    TextView ID, NAME, STATE, SEAT;
    String[] result3;
    AppCompatButton backRoom;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        bundle = getArguments();

        if(bundle != null) {
            id = bundle.getString("id","0");
            result = bundle.getString("result","0");
        }
        result2=result.substring(17);
        result3=result2.split("-");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backRoom = (AppCompatButton) view.findViewById(R.id.myback);
        backRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_myFragment_to_roomFragment,bundle);
            }
        });

        ID = (TextView) view.findViewById(R.id.ids);
        ID.setText("학번:   "+id);

        NAME = (TextView) view.findViewById(R.id.names);
        STATE = (TextView) view.findViewById(R.id.states);
        SEAT = (TextView) view.findViewById(R.id.seats);
        NAME.setText("이름:   "+result3[1]);
        STATE.setText("예약상태"+result3[3]);
        SEAT.setText("좌석"+result3[5]);
    }
}