package com.example.getlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText latitude, longitude;
    Button getLocation;
    TextView address;
    String lat;
    String lon;
    boolean gps_enabled = false;//for checking if device gps and internet is enabled or not
    boolean network_enabled = false;

    //for get readable address
    Geocoder geocoder;
    List<Address> myaddress;


    //for getting locations coordinates
    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the xml views
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        getLocation = (Button) findViewById(R.id.get_location);
        address = (TextView) findViewById(R.id.address);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMyLocation();
            }
        });
        checkPermission();
    }


    public class MyLocationListener implements LocationListener {



        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                lat = "" + location.getLatitude();
                lon = "" + location.getLongitude();

                latitude.setText(lat);
                longitude.setText(lon);

                geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    myaddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String adres=myaddress.get(0).getAddressLine(0);
                address.setText(adres);

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

    public void getMyLocation()
    {
        try {
            gps_enabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        } catch (Exception e) {

        }

        try {
            network_enabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);
        } catch (Exception e) {

        }

        if (!gps_enabled && !network_enabled) {
            //warning message show on screen if gps is not turned on or no internet
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Turn On GPS!");
            builder.create().show();
        }

        if (gps_enabled)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        if(network_enabled)
        {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    private boolean checkPermission()
    {
        int locat= ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int locat2=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermission= new ArrayList<>();
        if(locat!=PackageManager.PERMISSION_GRANTED)
        {
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(locat2!=PackageManager.PERMISSION_GRANTED)
        {
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(!listPermission.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),1);
        }

        return true;
    }
}

