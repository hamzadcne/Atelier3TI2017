package a3ti.atelier.mobile.atelier3ti2017;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoadClick(View view) {
        String urlString="http://192.168.137.1:8090/scripts/test.php";
        LoadTask backgroundTask = new LoadTask();
        backgroundTask.execute(urlString);
    }
    private class LoadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection c=null;
            try {
                String urlString=params[0];
                URL url=new URL(urlString);
                c=(HttpURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setConnectTimeout(15000 /* milliseconds */);
                c.setDoInput(true);

                c.connect();
                int mStatusCode = c.getResponseCode();
                switch (mStatusCode) {
                    case 200:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        String result =  sb.toString();
                        return result;
                }
                return "";
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                return "Error connecting to server";
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
//                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView textView = (TextView)findViewById(R.id.resultTextView);
            textView.setText(s);
        }
    }
}
