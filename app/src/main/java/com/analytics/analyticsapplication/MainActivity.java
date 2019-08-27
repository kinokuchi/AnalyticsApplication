package com.analytics.analyticsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.TextView;
import androidx.core.app.AppLaunchChecker;

import com.android.installreferrer.api.*;

public class MainActivity extends AppCompatActivity implements InstallReferrerStateListener {

    private InstallReferrerClient referrerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!AppLaunchChecker.hasStartedFromLauncher(this)){
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(this);
        }

        AppLaunchChecker.onActivityCreate(this);
    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        TextView textView = findViewById(R.id.text);

        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                // 接続完了
                try {
                    ReferrerDetails response = referrerClient.getInstallReferrer();
                    String installReferrer = response.getInstallReferrer();
                    textView.setText(installReferrer);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                textView.setText("No Referrer");
                // APIがサポートされない
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                textView.setText("No Referrer");
                // Play Storeアプリのアップデート中などで接続できなかった
                break;
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        // Try to restart the connection on the next request to
        // Google Play by calling the startConnection() method.
    }
}
