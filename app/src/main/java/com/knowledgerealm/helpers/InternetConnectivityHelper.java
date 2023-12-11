package com.knowledgerealm.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.knowledgerealm.R;

/**
 * Helper class for checking the internet connectivity
 */
public class InternetConnectivityHelper {
    private final Context mContext;

    /**
     * Constructor for the InternetConnectivityHelper class
     *
     * @param context the context of the activity
     */
    public InternetConnectivityHelper(Context context) {
        this.mContext = context;
    }

    /**
     * Checks if the device is connected to the internet
     *
     * @return true if the device is connected to the internet, false otherwise
     */
    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNetworkConnection = networkInfo != null && networkInfo.isConnected();

        if (!isNetworkConnection) {
            Log.e("InternetConnectivity", "No internet connection");
        } else {
            Log.i("InternetConnectivity", "Internet connection is available");
        }

        return isNetworkConnection;
    }

    /**
     * Shows a dialog that prompts the user to go to the network settings
     */
    public void showNoInternetConnectionDialog(Class<?> activityClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.no_internet_connection_check_network_settings);
        builder.setTitle(R.string.no_internet_connection);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.no_connection);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            Intent intent = new Intent(mContext, activityClass);
            mContext.startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
