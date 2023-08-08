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
                //save changes (do this after implementing functionality of all other ui elements

                //popup to notify user
                Snackbar.make(getActivity().findViewById(R.id.settings_parent)
                                , "Changes Saved", Snackbar.LENGTH_SHORT)
                        .show();
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