package com.example.sch_agro.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

public class NetworkMonitor {
    private final Context context;
    private final ConnectivityManager.NetworkCallback networkCallback;
    private final ConnectivityManager connectivityManager;

    public NetworkMonitor(Context context) {
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                if (onConnectedRunnable != null) {
                    onConnectedRunnable.run();
                }
            }
        };
    }

    private Runnable onConnectedRunnable;

    public void startMonitoring(Runnable onConnected) {
        this.onConnectedRunnable = onConnected;
        NetworkRequest request = new NetworkRequest.Builder().build();
        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    public void stopMonitoring() {
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
}
