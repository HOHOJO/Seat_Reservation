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
import android.widget.Button;


public class one_laptopFragment extends Fragment {

    AppCompatButton backKey;
    Button Btn1,Btn2,Btn3,Btn4,Btn5,Btn6,Btn7,Btn8;
    String id,pw;
    Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_one_laptop, container, false);

        bundle = getArguments();

        if(bundle != null) {
            id = bundle.getString("id", "0");
            pw = bundle.getString("pw", "0");
            Log.d("qq", "전달2: "+id+pw);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backKey = (AppCompatButton) view.findViewById(R.id.BackRoom);
        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_roomFragment, bundle);
            }
        });

        Btn1 = (Button) view.findViewById(R.id.btn1);
        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "1");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn2 = (Button) view.findViewById(R.id.btn2);
        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "2");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn3 = (Button) view.findViewById(R.id.btn3);
        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "3");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn4 = (Button) view.findViewById(R.id.btn4);
        Btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "4");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn5 = (Button) view.findViewById(R.id.btn5);
        Btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "5");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn6 = (Button) view.findViewById(R.id.btn6);
        Btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "6");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn7 = (Button) view.findViewById(R.id.btn7);
        Btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "7");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });

        Btn8 = (Button) view.findViewById(R.id.btn8);
        Btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("seat", "8");
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_one_laptopFragment_to_reFragment, bundle);
            }
        });
    }
}