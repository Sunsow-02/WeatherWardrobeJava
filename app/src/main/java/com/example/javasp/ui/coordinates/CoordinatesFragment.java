package com.example.javasp.ui.coordinates;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.javasp.MainActivity;
import com.example.javasp.R;
import com.example.javasp.databinding.FragmentCoordinatesBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.javasp.ui.BaseFrag;

public class CoordinatesFragment extends BaseFrag {
    private FragmentCoordinatesBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    float longitude_val = 0;
    float latitude_val = 0;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                    , "Location Permission Not Given", Snackbar.LENGTH_SHORT)
                            .show();
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        CoordinatesViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CoordinatesViewModel.class);

        binding = FragmentCoordinatesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        restoreSettings();
        Button submit_button = root.findViewById(R.id.submit_coordinates_button);
        Button current_cords_button = root.findViewById(R.id.current_coordinates_button);
        MainActivity activity = (MainActivity) getActivity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        current_cords_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //request perms if needed, get coordinates, and set text on the 2 textinputs
                TextInputEditText longitude_input = getActivity().findViewById(R.id.coordinates_longitude_input);
                TextInputEditText latitude_input = getActivity().findViewById(R.id.coordinates_latitude_input);
                if (ContextCompat.checkSelfPermission(
                        activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.
                    fusedLocationClient.getLastLocation();
                    longitude_input.setText(String.valueOf(longitude_val));
                    latitude_input.setText(String.valueOf(latitude_val));
                } else if (shouldShowRequestPermissionRationale("Getting current coordinates requires location permissions")) {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected, and what
                    // features are disabled if it's declined. In this UI, include a
                    // "cancel" or "no thanks" button that lets the user continue
                    // using your app without granting the permission.
                    // showInContextUI();
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION);
                    fusedLocationClient.getLastLocation();
                    longitude_input.setText(String.valueOf(longitude_val));
                    latitude_input.setText(String.valueOf(latitude_val));
                }
            }

        });

        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    longitude_val = (float) location.getLongitude();
                    latitude_val = (float) location.getLatitude();
                    TextInputEditText longitude_input = getView().findViewById(R.id.coordinates_longitude_input);
                    TextInputEditText latitude_input = getView().findViewById(R.id.coordinates_latitude_input);
                    longitude_input.setText(String.valueOf(longitude_val));
                    latitude_input.setText(String.valueOf(latitude_val));
                }
                else {
                    Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                    , "Please try again", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
        submit_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TextInputEditText longitude = getActivity().findViewById(R.id.coordinates_longitude_input);
                TextInputEditText latitude = getActivity().findViewById(R.id.coordinates_latitude_input);
                TextInputLayout long_layout = getActivity().findViewById(R.id.coordinates_longitude_layout);
                TextInputLayout lati_layout = getActivity().findViewById(R.id.coordinates_latitude_layout);

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
                                    , "Getting weather info for: " + latitude_val + "," + longitude_val, Snackbar.LENGTH_SHORT)
                            .show();
                    //weather api and get the stuff
                    Response info = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String currentDateAndTime = sdf.format(new Date());
                    String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" +
                    latitude_val + "%2C" + longitude_val + "/" + currentDateAndTime + "/" +
                            "?unitGroup=us&key=PLACEHOLDERKEY&contentType=json";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    try {
                        info = client.newCall(request).execute();
                    } catch (IOException e) {
                        Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                                        , "Error:" + e.toString(), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    if(info != null) {
                        String jsonData = null;
                        try {
                            jsonData = info.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            JSONArray weatherData2 = jsonObject.getJSONArray("days");
                            String recommended_level = "";
                            String recommened_level_description = "";
                            String recommended_clothing_items = "";
                            String temperature_str = "";
                            String wind_speed_str = "";
                            float humidity = 0; float wind_speed = 0;
                            JSONObject obj = weatherData2.getJSONObject(0);
                            Log.d("I", obj.toString());
                            float temperature = Float.parseFloat(obj.getString("temp"));
                            humidity = Float.parseFloat(obj.getString("humidity"));
                            wind_speed = Float.parseFloat(obj.getString("windspeed"));
                            switch (temperature_option) {
                                case 0:
                                    temperature_str += String.valueOf(temperature);
                                    temperature_str += "°F";
                                    break;
                                case 1:
                                    temperature = (temperature - 32) * 5/9;
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    temperature = Float.valueOf(df.format(temperature));
                                    temperature_str += String.valueOf(temperature);
                                    temperature_str += "°C";
                                    break;
                                default:
                                    temperature = (float) ((temperature - 32)* (5/9) + 273.15);
                                    temperature_str += String.valueOf(temperature);
                                    temperature_str += "°K";
                                    break;
                            }

                            if(humidity > humidity_umbrella_min_val) {
                                if (humidity <= humidity_umbrella_max_val) {
                                    recommended_clothing_items += "Umbrella\n";
                                } else {
                                    recommended_clothing_items += "Raincoat\n";
                                }
                            }

                            if(wind_speed_option != 0) {
                                //convert to km/h
                                wind_speed = (float) (wind_speed * 1.609);
                                wind_speed_str += String.valueOf(wind_speed);
                                wind_speed_str += "KM/H";
                            }
                            else {
                                wind_speed_str += String.valueOf(wind_speed);
                                wind_speed_str += "MPH";
                            }

                            //then use the settings/numbers fetched and determine recommended level/respective description
                            if(temperature < lvl2_min_val) {
                                //if scarf/mittens enabled, add them to recommended clothing items (check project proposal)
                                if(scarf_enabled) {
                                    recommended_clothing_items += "Scarf\n";
                                }
                                if(mittens_enabled) {
                                    recommended_clothing_items += "Mittens\n";
                                }
                                recommended_level += getResources().getString(R.string.level_1_header);
                                recommened_level_description += getResources().getString(R.string.level_1_description);
                            }
                            else if (temperature >= lvl2_min_val && temperature <= lvl2_max_val) {
                                recommended_level += getResources().getString(R.string.level_2_header);
                                recommened_level_description += getResources().getString(R.string.level_2_description);
                            }
                            else if (temperature > lvl2_max_val && temperature <= lvl3_max_val) {
                                recommended_level += getResources().getString(R.string.level_3_header);
                                recommened_level_description += getResources().getString(R.string.level_3_description);
                            }
                            else {
                                recommended_level += getResources().getString(R.string.level_4_header);
                                recommened_level_description += getResources().getString(R.string.level_4_description);
                            }

                            //settext on the edittext
                            TextInputEditText rec_level = getActivity().findViewById(R.id.coordinates_rec_level_text);
                            TextInputEditText rec_level_description = getActivity().findViewById(R.id.coordinates_rec_level_description_text);
                            TextInputEditText temp = getActivity().findViewById(R.id.coordinates_temp_text);
                            TextInputEditText humi = getActivity().findViewById(R.id.coordinates_humidity_text);
                            TextInputEditText windspd = getActivity().findViewById(R.id.coordinates_windspeed_text);
                            TextInputEditText rec_clothing_items_text = getActivity().findViewById(R.id.coordinates_clothing_items_text);
                            TextInputLayout rec_level_layout = getActivity().findViewById(R.id.coordinates_rec_level_layout);
                            TextInputLayout rec_level_description_layout = getActivity().findViewById(R.id.coordinates_rec_level_description_layout);
                            TextInputLayout rec_clothing_items_layout = getActivity().findViewById(R.id.coordinates_clothing_items_layout);
                            LinearLayout three_figs_layout = getActivity().findViewById(R.id.three_figs_layout);
                            rec_level.setText(recommended_level);
                            rec_level_description.setText(recommened_level_description);
                            temp.setText(temperature_str);
                            humi.setText(String.valueOf(humidity));
                            windspd.setText(wind_speed_str);
                            rec_clothing_items_text.setText(recommended_clothing_items);
                            //make them visible
                            rec_level_layout.setVisibility(View.VISIBLE);
                            rec_level_description_layout.setVisibility(View.VISIBLE);
                            three_figs_layout.setVisibility(View.VISIBLE);
                            rec_clothing_items_layout.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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