package bazararabia.com.and.config;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class MySharedPreferenceManager {
    public final String MY_PREFS_NAME = "bazararabia";

    public static final String UserId= "userId";
    public static final String UserEmail= "email";
    public static final String UserPasword= "password";
    public static final String ProfileIndex = "profileIndex";


    private Context context;
    public MySharedPreferenceManager(Context context)
    {
        this.context = context;

    }
    public String getValueString(String valueName , String defaultValue)
    {
        if(this.context == null ) return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString(valueName, defaultValue);

        return restoredText;
    }

    public void setValueString(String valueName , String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(valueName, value);
        editor.commit();
    }

    public int getValueInt(String valueName , int defaultValue)
    {
        if(this.context == null ) return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int rtVal = 0;
        rtVal = prefs.getInt(valueName, defaultValue);

        return rtVal;
    }

    public void setValueInt(String valueName , int value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(valueName, value);
        editor.commit();
    }

    public boolean getValueBoolean(String valueName , boolean defaultValue)
    {
        if(this.context == null ) return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean rtVal = false;
        rtVal = prefs.getBoolean(valueName, defaultValue);

        return rtVal;
    }

    public void setValueBoolean(String valueName , boolean value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(valueName, value);
        editor.commit();
    }
}
