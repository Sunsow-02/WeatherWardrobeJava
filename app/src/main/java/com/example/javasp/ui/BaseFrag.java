package com.example.javasp.ui;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.example.javasp.R;

public class BaseFrag extends Fragment {
    protected int temperature_option = 0;
    protected float lvl2_min_val = 0;
    protected float lvl2_max_val = 0;
    protected float lvl3_max_val = 0;
    protected float humidity_umbrella_min_val = 0;
    protected float humidity_umbrella_max_val = 0;
    protected int wind_speed_option = 0;
    protected boolean scarf_enabled = false;
    protected boolean mittens_enabled = false;

    protected void restoreSettings() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        temperature_option = sharedPref.getInt(getString(R.string.temp_option_key), 0);
        lvl2_min_val = sharedPref.getFloat(getString(R.string.temp_lvl2min_key), 25);
        lvl2_max_val = sharedPref.getFloat(getString(R.string.temp_lvl2max_key), 45);
        lvl3_max_val = sharedPref.getFloat(getString(R.string.temp_lvl3max_key), 65);
        humidity_umbrella_min_val = sharedPref.getFloat(getString(R.string.humidity_umbrella_min_key), 65);
        humidity_umbrella_max_val = sharedPref.getFloat(getString(R.string.humidity_umbrella_max_key), 70);
        wind_speed_option = sharedPref.getInt(getString(R.string.wind_speed_option_key), 0);
        scarf_enabled = sharedPref.getBoolean(getString(R.string.scarf_enabled_key), true);
        mittens_enabled = sharedPref.getBoolean(getString(R.string.mittens_enabled_key), true);
    }

    protected void saveSettings() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE); //might cause problems (look into workaround for getDefaultSharedPref)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.temp_option_key), temperature_option);
        editor.putFloat(getString(R.string.temp_lvl2min_key), lvl2_min_val);
        editor.putFloat(getString(R.string.temp_lvl2max_key), lvl2_max_val);
        editor.putFloat(getString(R.string.temp_lvl3max_key), lvl3_max_val);
        editor.putFloat(getString(R.string.humidity_umbrella_min_key), humidity_umbrella_min_val);
        editor.putFloat(getString(R.string.humidity_umbrella_max_key), humidity_umbrella_max_val);
        editor.putInt(getString(R.string.wind_speed_option_key), wind_speed_option);
        editor.putBoolean(getString(R.string.scarf_enabled_key), scarf_enabled);
        editor.putBoolean(getString(R.string.mittens_enabled_key), mittens_enabled);
        editor.apply();
    }
}

