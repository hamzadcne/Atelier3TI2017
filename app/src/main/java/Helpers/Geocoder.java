package Helpers;

import android.os.AsyncTask;
import android.os.Handler;
import android.telecom.Call;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import Models.Callback;
import Models.NominatimResult;
import Models.User;
import a3ti.atelier.mobile.atelier3ti2017.R;

/**
 * Created by LVSC on 08/04/2017.
 */

public class Geocoder {

    public String Geocode(String address){
        throw new UnsupportedOperationException();
    }
    public void ReverseGeocode(double lat, double lon, int zoom, Callback callback){
        String url= MessageFormat.format("http://nominatim.openstreetmap.org/reverse?format=json&lat={0}&lon={1}&zoom={2}&addressdetails=0",
                lat,lon,zoom);
        GeocodingTask task = new GeocodingTask(callback);
        task.execute(url);
    }
    private class GeocodingTask extends AsyncTask<String,Void,String>
    {
        private Callback mCallback;
        public GeocodingTask(Callback callback){
            this.mCallback = callback;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection connection = ((HttpURLConnection) new URL(params[0]).openConnection());
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent","Atelier3TI2017App");
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setDoInput(true);
                connection.connect();
                int code=connection.getResponseCode();
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
                        return sb.toString();
                    case 404:
                        return "Page non trouvée";
                    default:
                        return "Code non géré";
                }

            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                return "Erreur";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(mCallback!=null)
                mCallback.onFinished(s);
        }
    }

}
