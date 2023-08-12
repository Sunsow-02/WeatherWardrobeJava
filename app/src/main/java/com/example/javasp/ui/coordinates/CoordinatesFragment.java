package com.example.javasp.ui.coordinates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CoordinatesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CoordinatesViewModel.class);

        binding = FragmentCoordinatesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button submit_button = root.findViewById(R.id.submit_coordinates_button);
        submit_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TextInputEditText longitude = getActivity().findViewById(R.id.coordinates_longitude_input);
                TextInputEditText latitude = getActivity().findViewById(R.id.coordinates_latitude_input);
                TextInputLayout long_layout = getActivity().findViewById(R.id.coordinates_longitude_layout);
                TextInputLayout lati_layout = getActivity().findViewById(R.id.coordinates_latitude_layout);
                float longitude_val = Float.parseFloat(String.valueOf(longitude.getText()));
                float latitude_val = Float.parseFloat(String.valueOf(latitude.getText()));
                int error = 0;
                //check to see we got input/valid coordinates
                if(latitude_val < -90 || latitude_val > 90) {
                        lati_layout.setError("Latitude is invalid"); lati_layout.setErrorEnabled(true);
                        error = 1;
                }

                if(longitude_val < -180 || longitude_val > 180) {
                        long_layout.setError("Longitude is invalid"); long_layout.setErrorEnabled(true);
                        error = 1;
                }

                if(error == 0) {
                    lati_layout.setErrorEnabled(false);
                    long_layout.setErrorEnabled(false);
                    //popup to notify user
                    Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                    , "Coordinates Submitted", Snackbar.LENGTH_SHORT)
                            .show();
                    //weather api and get the stuff
                }
                else {
                    Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                    , "Errors with Coordinates Submitted", Snackbar.LENGTH_SHORT)
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