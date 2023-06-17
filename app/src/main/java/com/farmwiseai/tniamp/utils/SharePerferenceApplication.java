package com.farmwiseai.tniamp.utils;

import android.app.Application;

public class SharePerferenceApplication extends Application {
    private static SharePerferenceApplication _instance;

    @Override
    public void onCreate() {


        super.onCreate();
        _instance = this;

    }


    public static synchronized SharePerferenceApplication getInstance() {
        return _instance;

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
