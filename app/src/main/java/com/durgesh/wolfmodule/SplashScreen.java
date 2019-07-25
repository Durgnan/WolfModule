package com.durgesh.wolfmodule;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT>=23)
        {
            if (checkPermission())
            {}
            else
            {
                requestPermission();
            }
        }

        ImageView img = findViewById(R.id.splashimage);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        img.startAnimation(aniFade);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },3000);
    }
    public boolean checkPermission()
    {
        int Coarse= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int fine= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int sms = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
        return  Coarse== PackageManager.PERMISSION_GRANTED &&
                fine==PackageManager.PERMISSION_GRANTED && sms==PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(SplashScreen.this,new String[]
                {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.SEND_SMS
                },1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0)
                {
                    boolean fine=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean coarse=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean sms=grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    if (fine && coarse && sms)
                        ;
                    else
                        Log.e("TAG","Please give permissions");
                }
                break;
        }
    }

}
