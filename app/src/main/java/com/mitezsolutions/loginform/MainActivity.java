package com.mitezsolutions.loginform;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

    EditText username, password;
    Button btn_login, btn_reset;
    TextView result;
    String getResult;
    private ProgressDialog pDialog;
    private String url; //"http://192.168.0.170/sample%20website/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    url = "http://192.168.49.74/sample%20website/testPost.php";
                    new postLogin().execute();
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
            result.setText(s);
            getResult = s;
            pDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            Httphandler hp = new Httphandler();
            String output = hp.createConnection(url);
            if (output != null){
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    output = jsonObject.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                    output = "error : "+e.getMessage();
                }
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
