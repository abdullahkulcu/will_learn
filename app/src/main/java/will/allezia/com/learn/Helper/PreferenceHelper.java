package will.allezia.com.learn.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Abdullah on 21.10.2017.
 */

public class PreferenceHelper {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public PreferenceHelper(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public static void setData(String key ,String value){
        if(!key.isEmpty() && !value.isEmpty())
            editor.putString(key,value);
        else
            Log.e("Editor","Null data is coming");

        editor.commit();
    }

    public static void setData(String key ,int value){
        if(!key.isEmpty() && !String.valueOf(value).isEmpty())
            editor.putInt(key,value);
        else
            Log.e("Editor","Null data is coming");
        editor.commit();
    }

    public static void setData(String key ,boolean value){
        if(!key.isEmpty() && !String.valueOf(value).isEmpty())
            editor.putBoolean(key,value);
        else
            Log.e("Editor","Null data is coming");
        editor.commit();
    }
    public static boolean getBooleanData(String key){
        if (!key.isEmpty())
            return preferences.getBoolean(key,false);
        else
            return false;
    }
    @Nullable
    public static String getStringData(String key){
        if (!key.isEmpty())
            return preferences.getString(key,null);
        else
            return null;
    }
    public static int getIntegerData(String key){
        if (!key.isEmpty())
            return preferences.getInt(key,0);
        else
            return 0;
    }
    public static void deleteData(String key){
        if(!key.isEmpty())
            editor.remove(key);

    }
}
