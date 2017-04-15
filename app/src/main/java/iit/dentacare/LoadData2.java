package iit.dentacare;

/**
 * Created by Thiloshon on 21-Jan-17.
 */

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.N)
public class LoadData2 extends Thread{
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD = "address";

    String myJSON;
    int num1=0;
    int num2=0;
    int num3=0;
    int num4=0;


    JSONArray peoples = null;
    ProgressBar p1;
    ProgressBar p2;
    ProgressBar p3;
    ProgressBar p4;

    DateFormat g = new SimpleDateFormat("HH:mm:ss");



    public LoadData2(ProgressBar p1, ProgressBar p2,ProgressBar p3,ProgressBar p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
        while(true){

            //System.out.println("afdafdsaf");
            //while (true) {
            getData();
            //test();
            p1.setProgress(num1);
            p2.setProgress(num2);
            p3.setProgress(num3);
            p4.setProgress(num4);
            //}


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void getData() {
        String ans = "";
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected String doInBackground(String... params) {


                long value = 0;
                long ans = 0;


                while(value<=1000){
                    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://sdgp.coolpage.biz/getdata.php");

                    // Depends on your web service
                    httppost.setHeader("Content-type", "application/json");

                    InputStream inputStream = null;
                    String result = null;
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity entity = response.getEntity();

                        inputStream = entity.getContent();
                        // json is UTF-8 by default
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                        StringBuilder sb = new StringBuilder();

                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        result = result.split(":")[2].split(",")[0];
                        //System.out.println("hey");
                        //System.out.println(result);
                        //todo MainActivity.dsf = result;
                    } catch (Exception e) {
                        // Oops
                    } finally {
                        try {
                            if (inputStream != null) inputStream.close();
                        } catch (Exception squish) {
                        }
                    }

                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                    long a1 = df.getCalendar().getTimeInMillis();
                    long a2 = g.getCalendar().getTimeInMillis();

                    long diff = (a1 - a2);
                    //System.out.println(diff);
                    g=df;

                    value += diff;
                    if (value >= 1000) {
                        value = 0;
                        System.out.println(ans);
                        ans++;
                        MainActivity.dsf++;
                        if(result.equalsIgnoreCase("\"UP_LEFT\"")){
                            num1++;

                        }else if (result.equalsIgnoreCase("\"UP_RIGHT\"")){
                            num2++;

                        }else if (result.equalsIgnoreCase("\"DOWN_LEFT\"")){
                            num3++;

                        }else if (result.equalsIgnoreCase("\"DOWN_RIGHT\"")){
                            num4++;

                        }


                        //p.setProgress(12);

                        //MainActivity.p;


                        //p.setProgress(p.getProgress() + 1);
                    }

                }

                //ans = result;
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                //showList();
            }
        }
        GetDataJSON g = new GetDataJSON();

        g.execute();



    }



}










































