package com.cookandroid.client;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginFragment extends Fragment {
    char result2;

    View view2;
    Bundle bundle = new Bundle();
    private static String IP_ADDRESS = "172.20.10.8:8080";
    private static String TAG = "phptest";

    Button logingo;
    EditText id, pw;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        view2 = inflater.inflate(R.layout.fragment_login,container,false);
        return view2;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logingo=(Button) view.findViewById(R.id.loginGo);
        id = (EditText) view.findViewById(R.id.ID_edt);
        pw = (EditText) view.findViewById(R.id.PW_edt);

        logingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = id.getText().toString();
                String userPassword = pw.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/login.php", userID,userPassword);
                Log.d("qq",userID+userPassword);
            }
        });
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
                Toast.makeText(getContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("id", id.getText().toString() );
                bundle.putString("pw", pw.getText().toString() );
                NavController navController = Navigation.findNavController(view2);
                navController.navigate(R.id.action_loginFragment_to_roomFragment,bundle);
            } else {
                Toast.makeText(getContext(), "아이디 또는 비밀번호가 다릅니다. ", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String ID = (String) params[1];
            String PW = (String) params[2];

            String serverURL = (String) params[0];
            Log.d("qq", serverURL);
            serverURL = serverURL + "?" + "ID=" + ID + "&PW=" + PW;
            Log.d("qq", serverURL);
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