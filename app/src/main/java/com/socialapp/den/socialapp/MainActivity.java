package com.socialapp.den.socialapp;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonFacebook;
    private Button mButtonGoogle;
    private Button mButtonTwitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mButtonFacebook = (Button) findViewById(R.id.btnFacebook_AM);
        mButtonGoogle = (Button) findViewById(R.id.btnGoogle_AM);
        mButtonTwitter = (Button) findViewById(R.id.btnTwitter_AM);

        mButtonFacebook.setOnClickListener(this);
        mButtonGoogle.setOnClickListener(this);
        mButtonTwitter.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnFacebook_AM:

                startFacebook();

                break;
            case R.id.btnGoogle_AM:
                break;
            case R.id.btnTwitter_AM:
                break;
        }

    }

    private void startFacebook() {

        FacebookFragment fbFragment = new FacebookFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flCont_AM, fbFragment);
        ft.addToBackStack(null);
        ft.commit();

    }
}
