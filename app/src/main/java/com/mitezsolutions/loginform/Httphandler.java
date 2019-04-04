package com.mitezsolutions.loginform;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class Httphandler {

    private static final String TAG = "Httphandler";

    public Httphandler(){}

    public String createConnection (String myUrl){
        String response = null;

        try{
            //create connection with url
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //get data from url
            InputStream stream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            response = buffer.toString();
        }
        catch (Exception e){
            response = "error "+ e;
        }

        return response;
    }

    public String createPostConnection (String myUrl, EditText email, EditText pass){
        String response = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //set properties
            con.setRequestMethod("POST");
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            con.setDoInput(true);
            con.setDoOutput(true);

            //write output
            OutputStream os=con.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            //maklumat untuk dihantar
            bw.write(PacketData(email,pass));
            bw.flush();

            //RELEASE RES
            bw.close();
            os.close();


            //response form url
            int responseCode = con.getResponseCode();
            if (responseCode == con.HTTP_OK){
                //GET EXACT RESPONSE
                InputStream stream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                response = buffer.toString();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String PacketData(EditText email, EditText pass){
        String data = null;

        JSONObject jsonObj = new JSONObject();
        StringBuffer packetData = new StringBuffer();

        try {
            jsonObj.put("email",email.getText().toString());
            jsonObj.put("password",pass.getText().toString());

            Boolean firstValue=true;

            Iterator it = jsonObj.keys();

            do {
                String key = it.next().toString();
                String value = jsonObj.get(key).toString();

                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packetData.append("&");
                }

                packetData.append(URLEncoder.encode(key,"UTF-8"));
                packetData.append("=");
                packetData.append(URLEncoder.encode(value,"UTF-8"));

            }while (it.hasNext());

            data =  packetData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return data;
    }

}
