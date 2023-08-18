package com.example.javasp.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
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
    float lvl2_min_val = 35; float lvl2_max_val = 45;
    float lvl3_max_val = 75;
    float lvl4_max_val = 85;
    float humidity_umbrella_max_val = 10;
    public boolean scarf_enabled = false;
    public boolean mittens_enabled = false;

    public void saveSettings() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE); //might cause problems (look into workaround for getDefaultSharedPref)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.temp_option_key), temperature_option);
        //TODO: put all the stuff for the rest of the strings/keys
        editor.apply();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
                int error = 0;
                if(!lvl2_min_input.getText().toString().equals("")) {
                    lvl2_min_val = Float.parseFloat(String.valueOf(lvl2_min_input.getText()));
                }
                else {
                    lvl2_min_layout.setError("No input"); lvl2_min_layout.setErrorEnabled(true);
                    error = 1;
                }
                TextInputEditText lvl2_max_input = getActivity().findViewById(R.id.temp_lvl_2_max_input);
                TextInputLayout lvl2_max_layout = getActivity().findViewById(R.id.temp_lvl_2_max_layout);
                if(!lvl2_max_input.getText().toString().equals("")) {
                    lvl2_max_val = Float.parseFloat(String.valueOf(lvl2_max_input.getText()));
                }
                else {
                    lvl2_max_layout.setError("No input"); lvl2_max_layout.setErrorEnabled(true);
                    error = 1;
                }
                TextInputEditText lvl3_max_input = getActivity().findViewById(R.id.temp_lvl_3_max_input);
                TextInputLayout lvl3_max_layout = getActivity().findViewById(R.id.temp_lvl_3_max_layout);
                if(!lvl3_max_input.getText().toString().equals("")) {
                    lvl3_max_val = Float.parseFloat(String.valueOf(lvl3_max_input.getText()));
                }
                else {
                    lvl3_max_layout.setError("No input"); lvl3_max_layout.setErrorEnabled(true);
                    error = 1;
                }
                TextInputEditText lvl4_max_input = getActivity().findViewById(R.id.temp_lvl_4_max_input);
                TextInputLayout lvl4_max_layout = getActivity().findViewById(R.id.temp_lvl_4_max_layout);
                if(!lvl4_max_input.getText().toString().equals("")) {
                    lvl4_max_val = Float.parseFloat(String.valueOf(lvl4_max_input.getText()));
                    lvl4_max_layout.setErrorEnabled(false);
                }
                else {
                    lvl4_max_layout.setError("No input"); lvl4_max_layout.setErrorEnabled(true);
                    error = 1;
                }
                TextInputEditText humidity_umbrella_max_input = getActivity().findViewById(R.id.humidity_umbrella_max_input);
                TextInputLayout humidity_umbrella_max_layout = getActivity().findViewById(R.id.humidity_umbrella_layout);
                if(!humidity_umbrella_max_input.getText().toString().equals("")) {
                    humidity_umbrella_max_val = Float.parseFloat(String.valueOf(humidity_umbrella_max_input.getText()));
                    humidity_umbrella_max_layout.setErrorEnabled(false);
                }
                else {
                    humidity_umbrella_max_layout.setError("No input");
                    humidity_umbrella_max_layout.setErrorEnabled(true);
                    error = 1;
                }
                //error checking after acquiring all needed elements
                if(error == 0) {
                    switch (temperature_option) {
                        case 0:
                            if (lvl2_min_val < -459.67) {
                                lvl2_min_layout.setError("Invalid temp");
                                lvl2_min_layout.setErrorEnabled(true);
                                error = 1;
                            } else {
                                lvl2_min_layout.setErrorEnabled(false);
                            }
                            break;
                        case 1:
                            if (lvl2_min_val < -273.15) {
                                lvl2_min_layout.setError("Invalid temp");
                                lvl2_min_layout.setErrorEnabled(true);
                                error = 1;
                            } else {
                                lvl2_min_layout.setErrorEnabled(false);
                            }
                            break;
                        default:
                            if (lvl2_min_val < 0) {
                                lvl2_min_layout.setError("Invalid temp");
                                lvl2_min_layout.setErrorEnabled(true);
                                error = 1;
                            } else {
                                lvl2_min_layout.setErrorEnabled(false);
                            }
                    }

                    if (error == 0) {
                        if (lvl2_min_val >= lvl2_max_val) {
                            lvl2_min_layout.setError("Lvl2 min can't be >/= Lvl2 max");
                            lvl2_min_layout.setErrorEnabled(true);
                            error = 1;
                        } else {
                            lvl2_min_layout.setErrorEnabled(false);
                        }
                    }

                    if (lvl2_max_val >= lvl3_max_val) {
                        lvl2_max_layout.setError("Lvl2 max can't be >/= Lvl3 max");
                        lvl2_max_layout.setErrorEnabled(true);
                        error = 1;
                    } else {
                        lvl2_max_layout.setErrorEnabled(false);
                    }

                    if (lvl3_max_val >= lvl4_max_val) {
                        lvl3_max_layout.setError("Lvl3 max can't be >/= Lvl4 max");
                        lvl3_max_layout.setErrorEnabled(true);
                        error = 1;
                    } else {
                        lvl3_max_layout.setErrorEnabled(false);
                    }
                }
                //popup to notify user (consider switching to case statement for better snackbar msgs??? - look at this at very end of polishing after api hell has been conquered)
                if(error == 0) {
                    saveSettings();
                    Snackbar.make(getActivity().findViewById(R.id.settings_parent)
                                    , "Changes Saved", Snackbar.LENGTH_SHORT)
                            .show();
                }
                else {
                    Snackbar.make(getActivity().findViewById(R.id.settings_parent)
                                    , "Please fix the errors displayed", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        //TODO: retrieve settings saved if applicable and restore them

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}