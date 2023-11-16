package com.cookandroid.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReFragment extends Fragment {
    public static Context DayContext;
    char result2;
    AppCompatButton back;
    String id,pw,seat,Time,re;
    Bundle bundle;
    TextView number, s_num;
    ArrayAdapter<CharSequence> adapter =null;
    Spinner spinner =null;
    RadioButton rdoCal, rdoTime;
    DatePicker dPicker;
    TimePicker tPicker;
    TextView tvYear, tvMonth, tvDay, tvHour, tvMinute;
    Button btnDayOk, btnLast;
    View view;
    private static String IP_ADDRESS = "172.20.10.8:8080";
    private static String TAG = "예약";

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_re, container, false);
        bundle = getArguments();
        if(bundle != null) {
            id = bundle.getString("id", "0");
            pw = bundle.getString("pw", "0");
            seat = bundle.getString("seat", "0");
            Log.d("qq", "최종전달: "+id+pw);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.time, android.R.layout.simple_spinner_dropdown_item);
        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        rdoCal = (RadioButton) view.findViewById(R.id.rdoCal);
        rdoTime = (RadioButton) view.findViewById(R.id.rdoTime);
        // FrameLayout의 2개 위젯
        dPicker = (DatePicker) view.findViewById(R.id.datePicker1);
        tPicker = (TimePicker) view.findViewById(R.id.timePicker1);
        // 텍스트뷰 중에서 연,월,일,시,분 숫자
        tvYear = (TextView) view.findViewById(R.id.tvYear);
        tvMonth = (TextView) view.findViewById(R.id.tvMonth);
        tvDay = (TextView) view.findViewById(R.id.tvDay);
        tvHour = (TextView) view.findViewById(R.id.tvHour);
        tvMinute = (TextView) view.findViewById(R.id.tvMinute);

        spinner.setVisibility(View.INVISIBLE);
        tPicker.setVisibility(View.INVISIBLE);
        dPicker.setVisibility(View.INVISIBLE);

        rdoCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tPicker.setVisibility(View.INVISIBLE);
                dPicker.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.INVISIBLE);
            }
        });

        rdoTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                tPicker.setVisibility(View.VISIBLE);
                dPicker.setVisibility(View.INVISIBLE);
            }
        });

        back = (AppCompatButton) view.findViewById(R.id.Backlabtop);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_reFragment_to_one_laptopFragment, bundle);
            }
        });

        btnDayOk = (Button) view.findViewById(R.id.BtnDayOk);
        btnDayOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvYear.setText(Integer.toString(dPicker.getYear()));
                tvMonth.setText(Integer.toString(1 + dPicker.getMonth()));
                tvDay.setText(Integer.toString(dPicker.getDayOfMonth()));

                tvHour.setText(Integer.toString(tPicker.getCurrentHour()));
                tvMinute.setText(Integer.toString(tPicker.getCurrentMinute()));
                Time=""+tvYear.getText()+tvMonth.getText()+tvDay.getText()+tvHour.getText()+tvMinute.getText();
                re=spinner.getSelectedItem().toString().substring(0,2)+spinner.getSelectedItem().toString().substring(3,5)+
                        spinner.getSelectedItem().toString().substring(6);
                Log.d("qq", "시간"+re);
            }
        });

        btnLast = (Button) view.findViewById(R.id.res);
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/reservation.php", id,pw,re,seat);
            }
        });

        number = (TextView) view.findViewById(R.id.Number);
        number.setText(seat+"번좌석");
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("qq", "\n\n" + result);
            progressDialog.dismiss();
            result2 = result.toString().charAt(0);
            Log.d("qq", "\n\n" + result2);
            if (result2 == '1') {
                Toast.makeText(getContext(), "예약성공", Toast.LENGTH_LONG).show();
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_reFragment_to_roomFragment,bundle);
            } else {
                Toast.makeText(getContext(), "예약오류", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String ID = (String) params[1];
            String PW = (String) params[2];
            String RE = (String) params[3];
            String SEAT = (String) params[4];
            String serverURL = (String) params[0];
            serverURL = serverURL + "?" + "ID=" + ID + "&PW=" + PW + "&RESERVATION=" + RE + "&SEAT=" + SEAT;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "GET response code - " + responseStatusCode);

                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
}