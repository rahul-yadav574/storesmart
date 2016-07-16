package com.example.brekkishhh.geekcombat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 574;
    final String TAG = "MainMapActivity";
    Button submitQueryButton;
    EditText placeSearch,radiusInput,quantityInput;
    LatLng userInputLatLng = null;
    Button conditionSelectSpinner;
    List<String> conditionsSKU;
    MultiSpinner multiSpinner;
    private ArrayAdapter<String> adapter;
    private String[] allConditions = new String[]{"Cold Conditions","Hot Conditions","Mild Conditions"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        submitQueryButton = (Button) findViewById(R.id.submitQueryButton);

        placeSearch = (EditText) findViewById(R.id.get_ware_house);
        radiusInput = (EditText) findViewById(R.id.radiusInput);
        quantityInput  = (EditText) findViewById(R.id.capacityInput);
        multiSpinner = (MultiSpinner) findViewById(R.id.spinnerMulti);
        conditionsSKU = new ArrayList<>();


        placeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();
            }
        });

        submitQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuery();
            }
        });

        // create spinner list elements
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(allConditions);

        // get spinner and set adapter

        multiSpinner.setAdapter(adapter, false, onSelectedListener);



    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {

            for (int i=0;i<selected.length;i++){

                if (selected[i]){
                    conditionsSKU.add(allConditions[i]);
                }
            }


        }
    };

    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(MainActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                userInputLatLng = place.getLatLng();
                placeSearch.setText(place.getName());
                Log.i(TAG,"Latitude And Longitude Are :"+place.getLatLng());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG,"Can,t Connect To Google Api Client");
    }

    private void submitQuery(){

        if (userInputLatLng==null){
            Toast.makeText(MainActivity.this, "Please Select A Place", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( radiusInput.getText().toString().length()<=0){
            Toast.makeText(MainActivity.this, "Please Select Radius", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( quantityInput.getText().toString().length()<=0){
            Toast.makeText(MainActivity.this, "Please Select Quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        if (conditionsSKU.size()<=0){
            Toast.makeText(MainActivity.this, "Please Select Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        startGettingBestDeals();
    }


    private void startGettingBestDeals(){
        Toast.makeText(MainActivity.this, "Viru Pitega", Toast.LENGTH_SHORT).show();
    }
}
