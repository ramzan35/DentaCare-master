package iit.dentacare;

/**
 * Created by Thiloshon on 21-Jan-17.
 */

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)

public class LoadData2 extends Thread {

    private static DatabaseReference databaseUser;

    String myJSON;
    static int num1 = 0;
    static int num2 = 0;
    static int num3 = 0;
    static int num4 = 0;

    ProgressBar p1;
    ProgressBar p2;
    ProgressBar p3;
    ProgressBar p4;

    DateFormat g = new SimpleDateFormat("HH:mm:ss");

    public LoadData2(){}

    public LoadData2(ProgressBar p1, ProgressBar p2, ProgressBar p3, ProgressBar p4) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    /**
     * The method increases the counter on each region of the mouth
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
        long value = 0;
        while (true) {
            //retrieveRecords();

            value++;
//            System.out.println("value is " + value);

            if (value < 10) {
                num1++;

            } else if (value < 15) {
                num2++;

            } else if (value < 20) {
                num3++;

            } else if (value < 30) {
                num4++;

            }


            p1.setProgress(num1);
            p2.setProgress(num2);
            p3.setProgress(num3);
            p4.setProgress(num4);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This method gets data from the server
     */
    private void retrieveRecords() {
        class RetrieveJSONRecords extends AsyncTask<String, Void, String> {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected String doInBackground(String... params) {
                long value = 0;
                long ans = 0;

                while (value <= 1000) {
                    /*num1++;
                    num2++;
                    num3++;
                    num4++;*/

                    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                    HttpPost post = new HttpPost("http://sdgp.coolpage.biz/returnJSONData.php");

                    post.setHeader("Content-type", "application/json");

                    InputStream stream = null;
                    String result = null;
                    try {
                        HttpResponse httpResponse = httpclient.execute(post);
                        HttpEntity httpResponseEntity = httpResponse.getEntity();

                        stream = httpResponseEntity.getContent();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                        StringBuilder stringBuilder = new StringBuilder();

                        String record;
                        while ((record = bufferedReader.readLine()) != null) {
                            stringBuilder.append(record + "\n");
                        }
                        result = stringBuilder.toString().split(":")[2].split(",")[0];
                    } catch (Exception e) {
                        System.out.println(e);
                    } finally {
                        try {
                            if (stream != null) stream.close();
                        } catch (Exception squish) {
                        }
                    }

                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                    long a1 = df.getCalendar().getTimeInMillis();
                    long a2 = g.getCalendar().getTimeInMillis();

                    long diff = (a1 - a2);
                    g = df;

                    //value += diff;


                    if (value >= 30) {
                        value = 0;
                        System.out.println(value);
                        //ans++;
                        //MainActivity.dsf++;
                        /*if (result.equalsIgnoreCase("\"UP_LEFT\"")) {
                            num1++;

                        } else if (result.equalsIgnoreCase("\"UP_RIGHT\"")) {
                            num2++;

                        } else if (result.equalsIgnoreCase("\"DOWN_LEFT\"")) {
                            num3++;

                        } else if (result.equalsIgnoreCase("\"DOWN_RIGHT\"")) {
                            num4++;

                        }*/


                    }
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
            }
        }
        RetrieveJSONRecords g = new RetrieveJSONRecords();

        g.execute();
    }

    public static void saveData() {

        databaseUser = FirebaseDatabase.getInstance().getReference("dentaldiseaserecord");

        String status = "";

        if (num1 == 15 && num2 == 15 && num3 == 15 && num4 == 15) {
            status = "Good";
        } else if ((num1 > 12 && num1 < 18) && (num2 > 12 && num2 < 18) && (num3 > 12 && num3 < 18) && (num4 > 12 && num4 < 18)) {
            status = "Average";
        } else {
            status = "incomplete";
        }

        String id = "DC001";
        Date elapsedTime = new Date();
        DentalDiseaseRecord record = new DentalDiseaseRecord(id, elapsedTime, status, new Date ());

        databaseUser.child(id).setValue(record);

    }
}