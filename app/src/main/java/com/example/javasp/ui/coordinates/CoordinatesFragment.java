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
                //save changes (do this after implementing functionality of all other ui elements

                //popup to notify user
                Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                , "Coordinates Submitted", Snackbar.LENGTH_SHORT)
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