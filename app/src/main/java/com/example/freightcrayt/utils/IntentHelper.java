package com.example.freightcrayt.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.freightcrayt.activities.Login;

public class IntentHelper {

    public static void openIntent(Context context, String extraDetails, Class passTo) {

        Intent i = new Intent(context, passTo);

        i.putExtra("extraInfo", extraDetails);

        context.startActivity(i);
    }

    public static void openIntent(Context context, Bundle extraDetails, Class passTo) {

        Intent i = new Intent(context, passTo);

        i.putExtras(extraDetails);

        context.startActivity(i);
    }

    public static void shareIntent(Context context, String extraDetails) {

        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT, extraDetails);

        sendIntent.setType("text/plain");

        Intent sharedIntent = Intent.createChooser(sendIntent, null);

        context.startActivity(sharedIntent);
    }

    public static void logout(Context context) {

        openIntent(context, "Logout", Login.class);

        Intent broadcastLogout = new Intent();
        broadcastLogout.setAction("com.package.ACTION_LOGOUT");
        context.sendBroadcast(broadcastLogout);
    }
}
