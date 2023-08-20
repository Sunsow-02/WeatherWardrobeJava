package com.example.javasp.ui.coordinates;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javasp.R;
import com.example.javasp.databinding.FragmentCoordinatesBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CoordinatesFragment extends Fragment {

    private FragmentCoordinatesBinding binding;
    public int temperature_option = 0;
    float lvl2_min_val = 0; float lvl2_max_val = 0;
    float lvl3_max_val = 0;
    float lvl4_max_val = 0;
    float humidity_umbrella_max_val = 0;
    public boolean scarf_enabled = false;
    public boolean mittens_enabled = false;

    public void restoreSettings() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        temperature_option = sharedPref.getInt(getString(R.string.temp_option_key), 0);
        lvl2_min_val = sharedPref.getFloat(getString(R.string.temp_lvl2min_key), 25);
        lvl2_max_val = sharedPref.getFloat(getString(R.string.temp_lvl2max_key), 45);
        lvl3_max_val = sharedPref.getFloat(getString(R.string.temp_lvl3max_key), 65);
        lvl4_max_val = sharedPref.getFloat(getString(R.string.temp_lvl4max_key), 85);
        humidity_umbrella_max_val = sharedPref.getFloat(getString(R.string.humidity_umbrella_max_key), 10);
        scarf_enabled = sharedPref.getBoolean(getString(R.string.scarf_enabled_key), true);
        mittens_enabled = sharedPref.getBoolean(getString(R.string.mittens_enabled_key), true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CoordinatesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CoordinatesViewModel.class);

        binding = FragmentCoordinatesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        restoreSettings(); //not sure if it should be oncreate or in the on submit coordinates button listener
        Button submit_button = root.findViewById(R.id.submit_coordinates_button);
        submit_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TextInputEditText longitude = getActivity().findViewById(R.id.coordinates_longitude_input);
                TextInputEditText latitude = getActivity().findViewById(R.id.coordinates_latitude_input);
                TextInputLayout long_layout = getActivity().findViewById(R.id.coordinates_longitude_layout);
                TextInputLayout lati_layout = getActivity().findViewById(R.id.coordinates_latitude_layout);

                float longitude_val = 0;
                float latitude_val = 0;
                if(!longitude.getText().toString().equals("")) {
                    longitude_val = Float.parseFloat(String.valueOf(longitude.getText()));
                }
                if(!latitude.getText().toString().equals("")) {
                    latitude_val = Float.parseFloat(String.valueOf(latitude.getText()));
                }
                int error = 0;
                //check to see we got input/valid coordinates
                if(latitude_val < -90 || latitude_val > 90) {
                        lati_layout.setError("Latitude is invalid"); lati_layout.setErrorEnabled(true);
                        error = 1;
                }
                else {
                    lati_layout.setErrorEnabled(false);
                }

                if(longitude_val < -180 || longitude_val > 180) {
                        long_layout.setError("Longitude is invalid"); long_layout.setErrorEnabled(true);
                        error = 1;
                }
                else {
                    long_layout.setErrorEnabled(false);
                }

                if(error == 0) {
                    //popup to notify user
                    Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                    , "Getting weather info for: " + longitude_val + "," + latitude_val, Snackbar.LENGTH_SHORT)
                            .show();
                    //weather api and get the stuff
                    //settext on the edittext
                    TextInputEditText rec_level = getActivity().findViewById(R.id.coordinates_rec_level_text);
                    TextInputEditText rec_level_description = getActivity().findViewById(R.id.coordinates_rec_level_description_text);
                    TextInputEditText temp = getActivity().findViewById(R.id.coordinates_temp_text);
                    TextInputEditText humi = getActivity().findViewById(R.id.coordinates_humidity_text);
                    TextInputEditText windspd = getActivity().findViewById(R.id.coordinates_windspeed_text);
                    TextInputLayout rec_level_layout = getActivity().findViewById(R.id.coordinates_rec_level_layout);
                    TextInputLayout rec_level_description_layout = getActivity().findViewById(R.id.coordinates_rec_level_description_layout);
                    LinearLayout three_figs_layout = getActivity().findViewById(R.id.three_figs_layout);
                    rec_level.setText("Placeholder");
                    rec_level_description.setText("Placeholder");
                    temp.setText("Null");
                    humi.setText("Null");
                    windspd.setText("Null");
                    //make them visible
                    rec_level_layout.setVisibility(View.VISIBLE);
                    rec_level_description_layout.setVisibility(View.VISIBLE);
                    three_figs_layout.setVisibility(View.VISIBLE);
                }
                else {
                    Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                    , "Errors with Coordinates", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}