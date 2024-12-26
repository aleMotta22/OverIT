package it.motta.overit.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

public class OverItController {

    private static SharedPreferences sharedPreferences;

    private static final String PREF_GRID = "grid_layout";

    private static void checkShared(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void SaveLastGridView(Context context, boolean isGrid) {
        checkShared(context);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(PREF_GRID, isGrid);
        prefsEditor.apply();
    }

    public static boolean GetLastGridView(Context context) {
        checkShared(context);
        return sharedPreferences.getBoolean(PREF_GRID, false);
    }

}