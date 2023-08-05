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

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    public int temperature_option = 0; //change this to instead pull from saved data

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

        //SwitchCompat scarf_toggle = getView().findViewById(R.id.scarf_toggle);
        //SwitchCompat mittens_toggle = getView().findViewById(R.id.mittens_toggle);

        //Button save_button = getView().findViewById(R.id.save_changes_button);
        //Button cancel_button = getView().findViewById(R.id.cancel_changes_button);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}