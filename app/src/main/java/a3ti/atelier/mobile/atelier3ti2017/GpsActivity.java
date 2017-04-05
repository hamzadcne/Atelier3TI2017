package a3ti.atelier.mobile.atelier3ti2017;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GpsActivity extends AppCompatActivity {
    LocationManager manager;
    LocationListener locationChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        locationChangedListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double lat = location.getLatitude();
                Double lon = location.getLongitude();

                TextView latTxtView = (TextView) findViewById(R.id.latitude_value);
                latTxtView.setText(String.valueOf(lat));

                TextView lonTxtView = (TextView) findViewById(R.id.longitude_value);
                lonTxtView.setText(String.valueOf(lon));

                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if(locationChangedListener!=null)
                    manager.removeUpdates(locationChangedListener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationChangedListener);
        Location lastKnownLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }
}
