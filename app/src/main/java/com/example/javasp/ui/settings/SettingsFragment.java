package com.example.javasp.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javasp.R;
import com.example.javasp.databinding.FragmentSettingsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    public int temperature_option = 0; //change this to instead pull from saved data
    public boolean scarf_enabled = false;
    public boolean mittens_enabled = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SettingsViewModel notificationsViewModel =
        //        new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //radiogroup from temperature option
        RadioGroup radioGroup = root.findViewById(R.id.temp_option_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(checkedId == R.id.radio_f) {
                    temperature_option = 0;
                }
                else if(checkedId == R.id.radio_c) {
                    temperature_option = 1;
                }
                else {
                    temperature_option = 2;
                }
            }
        });

        SwitchCompat scarf_toggle = root.findViewById(R.id.scarf_toggle);
        scarf_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scarf_enabled = !scarf_enabled;
            }
        });
        SwitchCompat mittens_toggle = root.findViewById(R.id.mittens_toggle);
        mittens_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mittens_enabled = !mittens_enabled;
            }
        });

        Button save_button = root.findViewById(R.id.save_changes_button);
        save_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TextInputEditText lvl2_min_input = getActivity().findViewById(R.id.temp_lvl_2_min_input);
                TextInputLayout lvl2_min_layout = getActivity().findViewById(R.id.temp_lvl_2_min_layout);
                float lvl2_min_val = Float.parseFloat(String.valueOf(lvl2_min_input.getText()));
                TextInputEditText lvl2_max_input = getActivity().findViewById(R.id.temp_lvl_2_max_input);
                TextInputLayout lvl2_max_layout = getActivity().findViewById(R.id.temp_lvl_2_max_layout);
                float lvl2_max_val = Float.parseFloat(String.valueOf(lvl2_max_input.getText()));
                TextInputEditText lvl3_min_input = getActivity().findViewById(R.id.temp_lvl_3_min_input);
                TextInputLayout lvl3_min_layout = getActivity().findViewById(R.id.temp_lvl_3_min_layout);
                float lvl3_min_val = Float.parseFloat(String.valueOf(lvl3_min_input.getText()));
                TextInputEditText lvl3_max_input = getActivity().findViewById(R.id.temp_lvl_3_max_input);
                TextInputLayout lvl3_max_layout = getActivity().findViewById(R.id.temp_lvl_3_max_layout);
                float lvl3_max_val = Float.parseFloat(String.valueOf(lvl3_max_input.getText()));
                TextInputEditText lvl4_min_input = getActivity().findViewById(R.id.temp_lvl_4_min_input);
                TextInputLayout lvl4_min_layout = getActivity().findViewById(R.id.temp_lvl_4_min_layout);
                float lvl4_min_val = Float.parseFloat(String.valueOf(lvl4_min_input.getText()));
                TextInputEditText lvl4_max_input = getActivity().findViewById(R.id.temp_lvl_4_max_input);
                TextInputLayout lvl4_max_layout = getActivity().findViewById(R.id.temp_lvl_4_max_layout);
                float lvl4_max_val = Float.parseFloat(String.valueOf(lvl4_max_input.getText()));
                TextInputEditText humidity_umbrella_min_input = getActivity().findViewById(R.id.humidity_umbrella_min_input);
                TextInputLayout humidity_umbrella_min_layout = getActivity().findViewById(R.id.humidity_umbrella_layout);
                float humidity_umbrella_min_val = Float.parseFloat(String.valueOf(humidity_umbrella_min_input.getText()));
                TextInputEditText humidity_raincoat_min_input = getActivity().findViewById(R.id.humidity_raincoat_min_input);
                TextInputLayout humidity_raincoat_min_layout = getActivity().findViewById(R.id.humidity_raincoat_layout);
                float humidity_raincoat_min_val = Float.parseFloat(String.valueOf(humidity_raincoat_min_input.getText()));
                TextInputEditText windspeed_raincoat_min_input = getActivity().findViewById(R.id.windspeed_raincoat_min_input);
                TextInputLayout windspeed_raincoat_min_layout = getActivity().findViewById(R.id.windspeed_raincoat_layout);
                float windspeed_raincoat_min_val = Float.parseFloat(String.valueOf(windspeed_raincoat_min_input.getText()));
                //error checking after acquiring all needed elements
                int error = 0;
                if(lvl2_min_val >= lvl2_max_val ) {
                    lvl2_min_input.setError("Lvl2 min cannot be >/= Lvl2 max"); lvl2_min_layout.setErrorEnabled(true);
                    error = 1;
                }
                else {
                    lvl2_min_layout.setErrorEnabled(false);
                }

                if(lvl2_max_val >= lvl3_min_val ) {
                    lvl2_max_input.setError("Lvl2 max cannot be >/= Lvl3 min"); lvl2_max_layout.setErrorEnabled(true);
                    error = 1;
                }
                else {
                    lvl2_max_layout.setErrorEnabled(false);
                }

                if(lvl3_min_val >= lvl3_max_val ) {
                    lvl3_min_input.setError("Lvl3 min cannot be >/= Lvl3 max"); lvl3_min_layout.setErrorEnabled(true);
                    error = 1;
                }
                else {
                    lvl3_min_layout.setErrorEnabled(false);
                }

                if(lvl3_max_val >= lvl4_min_val ) {
                    lvl3_max_input.setError("Lvl3 max cannot be >/= Lvl4 min"); lvl3_max_layout.setErrorEnabled(true);
                    error = 1;
                }
                else {
                    lvl3_max_layout.setErrorEnabled(false);
                }

                if(lvl4_min_val >= lvl4_max_val ) {
                    lvl4_min_input.setError("Lvl4 min cannot be >/= Lvl4 max"); lvl4_min_layout.setErrorEnabled(true);
                    error = 1;
                }
                else {
                    lvl4_min_layout.setErrorEnabled(false);
                }

                if(humidity_umbrella_min_val >= humidity_raincoat_min_val) {
                    humidity_umbrella_min_layout.setError("Umbrella min cannot be >/= Raincoat min"); humidity_umbrella_min_layout.setErrorEnabled(true);
                    error = 1;
                }
                else {
                    humidity_umbrella_min_layout.setErrorEnabled(false);
                }

                //save changes

                //popup to notify user (consider switching to case statement for better snackbar msgs??? - look at this at very end of polishing after api hell has been conquered)
                if(error == 0) {
                    Snackbar.make(getActivity().findViewById(R.id.settings_parent)
                                    , "Changes Saved", Snackbar.LENGTH_SHORT)
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