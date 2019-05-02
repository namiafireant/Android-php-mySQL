package com.mitezsolutions.loginform;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressDialog pDialog;
    private String url;
    boolean doubleBackToExitPressedOnce = false;

    EditText username, password;
    Button btn_login, btn_reset;
    TextView result;
    String getResult;
    Intent intent;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        result = (TextView) findViewById(R.id.txt_result);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String pass = password.getText().toString();


                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)){
                    Toast.makeText(MainActivity.this,"Please fill the form", Toast.LENGTH_LONG).show();
                }
                else{
                    url = "http://172.16.59.39/sample%20website/login.php?email="+email+"&password="+pass;
                    new getLogin().execute();
                }

            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                result.setText("");
            }
        });

    }

    public class getLogin extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

            if (s.equals("success")){
                intent = new Intent(MainActivity.this, NewMenu.class);
                startActivity(intent);
            }
            else{
                result.setText("cuba lagi");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            Httphandler hp = new Httphandler();
            String output = hp.createConnection(url);
            if (!TextUtils.isEmpty(output)){
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    output = jsonObject.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                    output = "error : "+e.getMessage();
                }
            }
            else{
                output = "empty";
            }
            return output;
        }
    }

    public class postLogin extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result.setText(s);
            getResult = s;
            pDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Httphandler hp = new Httphandler();
            String output = hp.createPostConnection(url,username,password);
            return output;
        }
    }

}
