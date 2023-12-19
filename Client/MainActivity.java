package com.cookandroid.client;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    private long lastTimeBackPressed;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private static String tagName=null;
    TextView tagDesc;
    String goin;
    fileText fileText = new fileText();
    char result2=0;
    private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tagDesc = (TextView) findViewById(R.id.tagDesc);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE);
    }

    protected void onPause() {
        if(nfcAdapter!=null){
            nfcAdapter.disableForegroundDispatch(this);
        }
        Log.d("qq", "태그4");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(nfcAdapter!=null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            Log.d("qq", "태그1");
        }
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("qq", "태그6");
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG );
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages!=null){
            for (int i = 0; i < messages.length; i++){
                    setReadTagData((NdefMessage)messages[0]);
            }

        }else{
            return;
        }

        if(tag!=null){
            byte[] tagId =tag.getId();
            //tagDesc.setText(toHexString(tagId));
            tagName = toHexString(tagId);
        }else{
            Log.w("qq", "Unable to obtain NFC tag from intent!");
        }

    }
    public static final String CHARS = "0123456789ABCDEF";
    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<data.length;i++){
            sb.append(CHARS.charAt((data[i])>>4&0x0F)).append(CHARS.charAt(data[i] & 0x0f));
        }
        return sb.toString();
    }

    public void setReadTagData(NdefMessage ndefmsg){
        if(ndefmsg == null ) {
            return ;
        }
        String payloadStr="";
        String msgs = "";
        msgs += ndefmsg.toString() + "\n";
        NdefRecord [] records = ndefmsg.getRecords() ;
        for(NdefRecord rec : records) {
            byte [] payload = rec.getPayload() ;
            String textEncoding = "UTF-8" ;
            if(payload.length > 0)
                textEncoding = ( payload[0] & 0200 ) == 0 ? "UTF-8" : "UTF-16";
            Short tnf = rec.getTnf();
            String type = String.valueOf(rec.getType());
            payloadStr = new String(rec.getPayload(), Charset.forName(textEncoding));
            payloadStr=payloadStr.substring(1);
            Log.d("qq", "1->"+payloadStr);
        }
        String[] or = payloadStr.split("/");
        Log.d("qq", "1->"+or[1]);
        //fileText.fileDelete();

        switch (or[1]){
            case "goin.php":
                //fileText.fileDelete();
                fileText.TextWriter(payloadStr);
                StringBuffer strings = new StringBuffer();
                try {
                    strings = fileText.TextRead();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                goin=strings.toString();
                String data1[] = goin.split("-");
                Log.d("qq", "goin: "+goin);
                InsertData3 task = new InsertData3();
                task.execute("http://"+data1[2], data1[0],data1[1]);
                break;
            case "start.php":
                //fileText.fileDelete();
                fileText.TextWriter(payloadStr);
                StringBuffer strings1 = new StringBuffer();
                try {
                    strings1 = fileText.TextRead();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                goin=strings1.toString();
                String data2[] = goin.split("-");
                Log.d("qq", "goin: "+goin);
                InsertData4 task1 = new InsertData4();
                task1.execute("http://"+data2[2], data2[0],data2[1]);
                break;
        }


    }

    class InsertData3 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("qq", "확인1" + result);
            progressDialog.dismiss();
            result2 = result.toString().charAt(0);
            Log.d("qq", "확인" + result2);
            if (result2 == '1') {
                Toast.makeText(MainActivity.this, "입장 하셨습니다.", Toast.LENGTH_LONG).show();
                result2='0';
            } else {
                Toast.makeText(MainActivity.this, "입장 하실 수 없습니다. ", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String ID = (String) params[1];
            String PW = (String) params[2];

            String serverURL = (String) params[0];
            Log.d("qq", "왜이러지"+params[0]);
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
                fileText.fileDelete();
                fileText.TextWriter(ID);
                fileText.TextWriter(PW);
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    class InsertData4 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("qq", "\n\n" + result);
            progressDialog.dismiss();
            result2 = result.toString().charAt(0);
            Log.d("qq", "\n\n" + result2);
            if (result2 == '2'||result2=='1') {
                Toast.makeText(MainActivity.this, "자리를 착석 하셨습니다.", Toast.LENGTH_LONG).show();
                fileText.fileDelete();
                result2='0';
            } else {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String ID = (String) params[1];
            String PW = (String) params[2];

            String serverURL = (String) params[0];
            Log.d("qq", "왜이러지2"+params[0]+params[1]+params[2]);
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
                fileText.fileDelete();
                fileText.TextWriter(ID);
                fileText.TextWriter(PW);
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        //프래그먼트 onBackPressedListener사용
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragmentList){
            if(fragment instanceof onBackPressedListener){
                ((onBackPressedListener)fragment).onBackPressed();
                return;
            }
        }

        //두 번 클릭시 어플 종료
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
    }
}