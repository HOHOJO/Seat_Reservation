package com.cookandroid.client;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.cardemulation.NfcFCardEmulation;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;


public class RoomFragment extends Fragment {
    String url = "http://172.20.10.8:8080/seat.php";
    String lab_n="";
    Button labtop_one, MyName;
    AppCompatButton logout;
    TextView labtop_n;
    public GettingPHP gPHP;
    String id,pw,tag;
    Bundle bundle;
    View view;
    fileText fileText = new fileText();

    char result2;
    private static String IP_ADDRESS = "172.20.10.8:8080";
    private static String TAG = "예약";

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room, container, false);
        bundle = getArguments();

        if(bundle != null) {

            id = bundle.getString("id", "0");
            pw = bundle.getString("pw", "0");

            Log.d("qq", "전달1: "+id+pw);

        }
        fileText.fileDelete();
        fileText.TextWriter(id);
        fileText.TextWriter(pw);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        labtop_n = (TextView) view.findViewById(R.id.Labtop_n);

        gPHP = new GettingPHP();
        gPHP.execute(url);

        labtop_one = (Button) view.findViewById(R.id.laptop_one);
        labtop_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/REtest.php", id,pw);
            }
        });

        logout = (AppCompatButton) view.findViewById(R.id.LogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileText.fileDelete();
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_roomFragment_to_mainFragment);
            }
        });

        MyName = (Button) view.findViewById(R.id.myname);
        MyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData1 task = new InsertData1();
                task.execute("http://" + IP_ADDRESS + "/student.php", id,pw);
            }
        });

    }

    class GettingPHP extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            lab_n=str;
            labtop_n.setText(lab_n);
        }
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
        protected void onPostExecute(String result1) {
            super.onPostExecute(result1);
            Log.d("qq", "\n\n" + result1);
            progressDialog.dismiss();
            result2 = result1.toString().charAt(0);
            Log.d("qq", "\n\n" + result2);
            if (result2 == '1') {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_roomFragment_to_one_laptopFragment,bundle);
            } else {
                Toast.makeText(getContext(), "이미예약중", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "POST response  - " + result1);
        }

        @Override
        protected String doInBackground(String... params) {
            String ID = (String) params[1];
            String PW = (String) params[2];
            String serverURL = (String) params[0];
            serverURL = serverURL + "?" + "ID=" + ID + "&PW=" + PW;
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

    class InsertData1 extends AsyncTask<String, Void, String> {
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
            progressDialog.dismiss();
            Log.d("qq", "\n\n" + result);
            bundle.putString("result", result);
            bundle.putString("id", id);
            bundle.putString("pw",pw);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_roomFragment_to_myFragment,bundle);
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String ID = (String) params[1];
            String PW = (String) params[2];
            String serverURL = (String) params[0];
            serverURL = serverURL + "?" + "ID=" + ID + "&PW=" + PW;
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