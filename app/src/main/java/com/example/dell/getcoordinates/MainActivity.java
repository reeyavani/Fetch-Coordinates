package com.example.dell.getcoordinates;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText ed1;
    String city,msg,lat,longi;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1=(EditText)findViewById(R.id.editText);
        t1=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView2);

    }

    public void onclear(View v)
    {
        t1.setText("");
        t2.setText("");
        ed1.setText("");
    }

    public void fetchData(View v)
    {

        city=ed1.getText().toString();
        if(city.equals(""))
        {
            Toast.makeText(this,"City Name cannot be empty",Toast.LENGTH_LONG).show();
        }
        else
        {
            InsertData insertData = new InsertData();
            insertData.execute();
        }
    }

    public class InsertData extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params) {

            try
            {
                String url1="http://192.168.1.6:3000/";

                URL url= new URL(url1);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                JSONObject jsonObject=new JSONObject();
                jsonObject.put("city",city);

                urlConnection.connect();

                DataOutputStream outputStream=new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(jsonObject.toString().getBytes());

                int res=urlConnection.getResponseCode();

                if(res==200)
                {
                    StringBuilder builder=new StringBuilder();
                    String str="";

                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    while((str=bufferedReader.readLine())!=null)
                    {
                        builder.append(str);
                    }

                    JSONObject object= new JSONObject(builder.toString());

                    msg=object.getString("adddata");

                    JSONArray jsonArray=new JSONArray(msg);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1=jsonArray.getJSONObject(i);

                        lat=jsonObject1.getString("lat");
                        longi=jsonObject1.getString("long");

                    }


                }



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



            return lat;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            t1.setText("Latitude: "+lat);
            t2.setText("Longitude: "+longi);

        }
    }
}
