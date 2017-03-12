package a3ti.atelier.mobile.atelier3ti2017;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import Models.User;

public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoadClick(View view) {
        /*String urlString="http://192.168.137.1:8090/scripts/test.php";
        LoadTask backgroundTask = new LoadTask();
        backgroundTask.execute(urlString);*/
        EditText usernameTxt=(EditText)findViewById(R.id.usernameTxtView);
        EditText passwordTxt=(EditText)findViewById(R.id.passwordTxtView);

        username=usernameTxt.getText().toString();
        password=passwordTxt.getText().toString();

        String urlString="http://192.168.43.243/gps/users.php";
        BackgroundTask task=new BackgroundTask();
        //PostDataTask task = new PostDataTask();
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
            ArrayList<User> users=new ArrayList<User>();
            try {
                Gson gson= new Gson();

                User[] usersList =  gson.fromJson(s, User[].class);

                /*JSONArray array = new JSONArray(s);
                for (int i=0;i<array.length();i++) {
                    JSONObject obj=array.getJSONObject(i);
                    String nom = obj.getString("Nom");
                    String prenom = obj.getString("Prenom");
                    Double latitude= obj.getDouble("Latitude");
                    Double longitude= obj.getDouble("Longitude");
                    User user=new User();
                    user.setNom(nom);
                    user.setPrenom(prenom);
                    user.setLatitude(latitude);
                    user.setLongitude(longitude);
                    users.add(user);
                }*/
                Toast.makeText(getBaseContext(),String.valueOf(usersList.length),Toast.LENGTH_LONG);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private class PostDataTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            URL url= null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String s = "username="+username+ "&password=" + password;
                connection.setFixedLengthStreamingMode(s.getBytes().length);
                PrintWriter out = new PrintWriter (connection.getOutputStream());
                out.print(s);
                out.close();

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
                //Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
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
