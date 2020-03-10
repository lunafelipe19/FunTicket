package com.famtechnology.funticket.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.famtechnology.funticket.R;


/**
 * Created by Ailton on 08/07/2017 for artGS.<br>
 *
 * Used to check if must or not show a rate popup to the user.
 */
public class RateAssistent {

    /***** CONSTANTS *****/
    private static final String PREFS = "PREFS";
    private static final String PREFS_OPENED_TIMES = "PREFS_OPENED_TIMES";
    private static final String PREFS_DONT_SHOW_POPUP = "PREFS_DONT_SHOW_POPUP";
    private static final int OPEN_TIMES_TO_SHOW = 2; //User has to open the App this amount of times to show Rate us popup. Change to properly time if you need to.

    /**
     * Check if popup must be opened or nor
     * @param context App context
     */
    public static void checkStatus(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
            SharedPreferences.Editor editor = prefs.edit();
            int openedTimes = prefs.getInt(PREFS_OPENED_TIMES, 0);

            //If user doesn't want to see rate us popup
            if (prefs.getBoolean(PREFS_DONT_SHOW_POPUP, false))
                return;

            //If it was opened the number of times, shows the popup
            if (openedTimes == OPEN_TIMES_TO_SHOW) {
                openedTimes = -1;

                //Starts popup
                startPopupRateUsActivity(context);
            }

            //If its here, we must increment the opened times
            editor.putInt(PREFS_OPENED_TIMES, openedTimes+1);
            editor.apply();
        } catch (Exception error) {
            Log.e("Error", "Error while checking user rating status at checkStatus(). Error=" + error.getMessage());
        }
    }

    /**
     * Stop sound_count and shows rate us popup
     * @param context App context
     */
    public static void dontShowAgain(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(PREFS_DONT_SHOW_POPUP, true);
            editor.apply();
        } catch (Exception error) {
            Log.e("Error", "Error at dontShowAgain(). Error=" + error.getMessage());
        }
    }

    /**
     * Starts the rate us popup
     */
    private static void startPopupRateUsActivity(Context context) throws Exception {
        //Intent intent = new Intent(context, PopupRateUsActivity.class);
        //context.startActivity(intent);
    }

    /**
     * Open playstore to rate
     */
    public static void openPlaystorePage(Context context) throws Exception {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.playstore_market_intent) + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.playstore_url) + appPackageName)));
        }
    }
}
