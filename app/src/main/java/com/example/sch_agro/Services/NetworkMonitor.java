package com.example.sch_agro.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

public class NetworkMonitor {
    private final Context context;

    public NetworkMonitor(Context context) {
        this.context = context;
    }

    public void startMonitoring(Runnable onConnected) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest request = new NetworkRequest.Builder().build();

        connectivityManager.registerNetworkCallback(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                onConnected.run(); // Executa quando a internet está disponível
            }
        });
    }
}
