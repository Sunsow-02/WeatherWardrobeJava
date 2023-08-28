package com.example.javasp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javasp.MainActivity;
import com.example.javasp.R;
import com.example.javasp.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    public int temperature_option = 0;
    float lvl2_min_val = 0; float lvl2_max_val = 0;
    float lvl3_max_val = 0;
    float humidity_umbrella_min_val = 0;
    float humidity_umbrella_max_val = 0;
    public boolean scarf_enabled = false;
    public boolean mittens_enabled = false;
    float longitude_val = 0;
    float latitude_val = 0;
    public void restoreSettings() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        temperature_option = sharedPref.getInt(getString(R.string.temp_option_key), 0);
        lvl2_min_val = sharedPref.getFloat(getString(R.string.temp_lvl2min_key), 25);
        lvl2_max_val = sharedPref.getFloat(getString(R.string.temp_lvl2max_key), 45);
        lvl3_max_val = sharedPref.getFloat(getString(R.string.temp_lvl3max_key), 65);
        humidity_umbrella_min_val = sharedPref.getFloat(getString(R.string.humidity_umbrella_max_key), 5);
        humidity_umbrella_max_val = sharedPref.getFloat(getString(R.string.humidity_umbrella_max_key), 10);
        scarf_enabled = sharedPref.getBoolean(getString(R.string.scarf_enabled_key), true);
        mittens_enabled = sharedPref.getBoolean(getString(R.string.mittens_enabled_key), true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}