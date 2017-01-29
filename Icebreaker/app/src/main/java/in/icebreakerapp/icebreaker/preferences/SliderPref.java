package in.icebreakerapp.icebreaker.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DravitLochan on 23-01-2017.
 */

public class SliderPref {
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Icebreaker-slider";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SliderPref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


}
