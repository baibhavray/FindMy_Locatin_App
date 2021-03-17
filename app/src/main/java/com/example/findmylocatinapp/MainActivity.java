package com.example.findmylocatinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Button btnMyLocation;
    TextView tvLatitude, tvLongitude, tvAddress;
    LocationManager locationManager;
    double latitude, longitude;
    String result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.INTERNET"}, 1);
            }
        }


        btnMyLocation = findViewById(R.id.btnMyLocation);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvAddress = findViewById(R.id.tvAddress);

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, MainActivity.this);
            //"requestLocationUpdates" calls the "onLocationChanged" method
            //"LocationManager.GPS_PROVIDER" requests location from GPS
            //It will call the "onLocationChanged" in every 1000ms, or in every 5meter distance which ever comes fast.(2nd & 3rd parameter)
        } catch (Exception e) {
            tvAddress.setText("Error : " + e);
        }


        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLatitude.setText("Latitude : " + latitude);
                tvLongitude.setText("Longitude : " + longitude);
                tvAddress.setText("Address : " + result);
            }
        });

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //On location change
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            try
            {
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                //"Geocoder" gets address from google map.It has 3 things   latitude,longitude and address
                List<Address> addressList = geocoder.getFromLocation(latitude,longitude, 1);
                Address address = addressList.get(0);
                //it gets multiple addresses from the longitude and latitude,"addressList.get(0);" is used so that it will get the nearest address(or 1st address)
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                {
                    sb.append(address.getAddressLine(i)).append("\n");
                }

                sb.append(address.getSubLocality()).append(",");
                sb.append(address.getLocality()).append(",");
                sb.append(address.getSubAdminArea()).append(",");
                sb.append(address.getAdminArea()).append(",");
                sb.append(address.getPostalCode()).append(",");
                sb.append(address.getCountryName());
                result = sb.toString();
            }
            catch(Exception e)
            {
                tvAddress.setText("Error : "+e);
            }



        }

        else {
            try {
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
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                latitude = location1.getLatitude();
                longitude = location1.getLongitude();

                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocation(latitude,longitude, 1);
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                {
                    sb.append(address.getAddressLine(i)).append("\n");
                }

                sb.append(address.getSubLocality()).append(",");
                sb.append(address.getLocality()).append(",");
                sb.append(address.getSubAdminArea()).append(",");
                sb.append(address.getAdminArea()).append(",");
                sb.append(address.getPostalCode()).append(",");
                sb.append(address.getCountryName());
                result = sb.toString();

            }
            catch(Exception e){
                tvAddress.setText("Error : "+e);
            }
        }
    }




    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    //When gps is not working properly
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    //If gps is enabled on our phone it will call
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    //If gps is disabled on our phone it will call
    }
}