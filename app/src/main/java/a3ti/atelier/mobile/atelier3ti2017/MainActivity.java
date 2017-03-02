package a3ti.atelier.mobile.atelier3ti2017;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoadClick(View view) {
        /*String urlString="http://192.168.137.1:8090/scripts/test.php";
        LoadTask backgroundTask = new LoadTask();
        backgroundTask.execute(urlString);*/

        String urlString="http://192.168.137.1/gps/test.php";
        BackgroundTask task=new BackgroundTask();
        task.execute(urlString);


    }


    private class BackgroundTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            URL url= null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setDoInput(true);
                connection.connect();

                int code= connection.getResponseCode();
                switch (code)
                {
                    case 200:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        String result =  sb.toString();
                        return result;

                    case 404:
                        return "page non trouvée";

                    default:
                        return "Code non géré";
                }

            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(getClass().getName())
                        .log(Level.SEVERE, null, e);
                Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                return "Erreur";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView textView = (TextView) findViewById(R.id.resultTextView);
            textView.setText(s);
        }
    }
}
