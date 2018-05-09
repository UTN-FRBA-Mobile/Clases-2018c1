package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.MainActivity;

public class MyPreferences {
    private static final String PREF_NAME = "PREF_NAME";
    private static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";

    public static String getFirebaseToken(Context context) {
        return getPreferences(context).getString(FIREBASE_TOKEN, null);
    }

    public static void setFirebaseToken(Context context, String token) {
        SharedPreferences.Editor editor = getPreferencesEditor(context);
        editor.putString(FIREBASE_TOKEN, token);
        editor.apply();
    }

    private static SharedPreferences.Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
