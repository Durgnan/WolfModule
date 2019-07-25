package com.durgesh.wolfmodule;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button button;
    FirebaseDatabase database;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5000, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        DatabaseReference m = database.getReference();

                        m.child("Location").child("lat").setValue(location.getLatitude());
                        m.child("Location").child("lng").setValue(location.getLongitude());

                        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {
                            Log.e("You are in",addresses.get(0).getLocality());
                            textToSpeech.speak("You are in "+ addresses.get(0).getLocality(), TextToSpeech.QUEUE_FLUSH, null);
                            Toast.makeText(getApplicationContext(),"You are in "+ addresses.get(0).getLocality(),Toast.LENGTH_LONG);
                            m.child("Location").child("text").setValue(addresses.get(0).getLocality());
                        }
                        else {
                            // do your stuff
                            Log.e("TAG","Not In the City");
                        }
                        mLocationManager.removeUpdates(this);

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
                });

            }
        });

    }
    public void init()
    {

        database = FirebaseDatabase.getInstance();
        button = findViewById(R.id.where);
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status!= TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.US);
                }

            }
        });

    }

}
