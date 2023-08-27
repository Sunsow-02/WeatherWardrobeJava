package com.example.javasp.ui.coordinates;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javasp.R;
import com.example.javasp.databinding.FragmentCoordinatesBinding;
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

public class CoordinatesFragment extends Fragment {
    private FragmentCoordinatesBinding binding;
    public int temperature_option = 0;
    float lvl2_min_val = 0; float lvl2_max_val = 0;
    float lvl3_max_val = 0;
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
        humidity_umbrella_max_val = sharedPref.getFloat(getString(R.string.humidity_umbrella_max_key), 10);
        scarf_enabled = sharedPref.getBoolean(getString(R.string.scarf_enabled_key), true);
        mittens_enabled = sharedPref.getBoolean(getString(R.string.mittens_enabled_key), true);
    }

    public Response requestWeatherAPIinfo() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://weatherapi-com.p.rapidapi.com/current.json?q=" + longitude_val + "%2C" + latitude_val)
                .get()
                .addHeader("X-RapidAPI-Key", "6986dd8915msh2eef5c76f8cfe61p14a831jsn211856301e30")
                .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .build();

        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            Snackbar.make(getActivity().findViewById(R.id.coordinates_parent)
                            , "Error:" + e.toString(), Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
                    Response info = null;
                    String url = "https://weatherapi-com.p.rapidapi.com/current.json?q=" + longitude_val + "%2C" + latitude_val;
                    Log.d("I", url);
                    //consider switching apis as this is buggy at certain coordinates (0,0)
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("X-RapidAPI-Key", "6986dd8915msh2eef5c76f8cfe61p14a831jsn211856301e30")
                            .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
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
                            Log.d("I", "HERE HERE");
                            Log.d("I",jsonObject.toString());
                            JSONObject weatherData = jsonObject.getJSONObject("current");
                            //get temperature, humidity, and wind speed
                            String recommended_level = "";
                            String recommened_level_description = "";
                            String recommended_clothing_items = "";
                            float temperature = 0;
                            float humidity = 0; float wind_speed = 0;
                            switch(temperature_option) {
                                case 0:
                                    temperature = Float.parseFloat(weatherData.getString("temp_f"));
                                    break;
                                case 1:
                                    temperature = Float.parseFloat(weatherData.getString("temp_c"));
                                    break;
                                default:
                                    temperature = Float.parseFloat(weatherData.getString("temp_f"));
                                    temperature = (float) (273.5 + ((temperature - 32.0) * (5.0/9.0)));
                                    break;
                            }
                            humidity = Float.parseFloat(weatherData.getString("humidity"));
                            wind_speed = Float.parseFloat(weatherData.getString("wind_mph"));

                            //then use the settings/numbers fetched and determine recommended level/respective description
                            if(humidity > 10) {
                                if (humidity <= humidity_umbrella_max_val) {
                                    recommended_clothing_items += "Umbrella\n";
                                } else {
                                    recommended_clothing_items += "Raincoat\n";
                                }
                            }

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
                            temp.setText(String.valueOf(temperature));
                            humi.setText(String.valueOf(humidity));
                            windspd.setText(String.valueOf(wind_speed));
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