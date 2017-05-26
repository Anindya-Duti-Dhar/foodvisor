package foodvisor.com.foodvisor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PrefManager {
    SharedPreferences pref;
    Editor editor;
    Context mContext;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "foodvisor";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // is item opened
    private static final String PREFERENCE_ITEM_OPENED = "item_opened";
    private static final String APPLICATION_ITEM_OPENED = "ap_item_opened";

    public PrefManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void setItemOpened(final Context ctx, final String user) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PrefManager.PREFERENCE_ITEM_OPENED, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(PrefManager.APPLICATION_ITEM_OPENED, user);
        editor.commit();
    }

    public static String getItemOpened(final Context ctx) {
        return ctx.getSharedPreferences(
                PrefManager.PREFERENCE_ITEM_OPENED, Context.MODE_PRIVATE)
                .getString(PrefManager.APPLICATION_ITEM_OPENED, "");
    }
}
