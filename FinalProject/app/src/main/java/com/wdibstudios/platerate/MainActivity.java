package com.wdibstudios.platerate;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView plateView, stateView;
    String plateNumber, stateAbbreviation;
    Button rateButton;
    Spinner stateChoices;
    private ConstraintLayout constraintLayout;

    HashMap<String, String> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.constraintLayout);

        plateView = findViewById(R.id.PlateNumberInput);
        stateView = findViewById(R.id.PlateStateInput);
        rateButton = findViewById(R.id.LookUpPlateButton);
        stateChoices = findViewById(R.id.stateSelector);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        stateChoices.setAdapter(adapter);
        stateChoices.setOnItemSelectedListener(this);

        //Create state map/dictionary
        FillStateMap();

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLookUp();
            }
        });

    }

    public void OnLookUp()
    {
        // Assign the values form the textviews
        plateNumber = plateView.getText().toString();
        stateAbbreviation = stateView.getText().toString().toUpperCase();

        // Make sure the input is correct
        if(((plateNumber == null || plateNumber.isEmpty()) || (stateAbbreviation == null || stateAbbreviation.isEmpty())))
        {
            DisplayError("Plate Number and State Cannot Be Empty");
            return;
        }

        // Parse through input and make sure its correct
        plateNumber = (plateNumber.replaceAll("[^a-zA-Z0-9]+", "")).toUpperCase();
        stateAbbreviation = (stateAbbreviation.replaceAll("[^a-zA-Z0-9]+", "")).toUpperCase();

        NewIntent(plateNumber, stateAbbreviation);

    }

    /*
    public void NewIntent()
    {
        // Create intent pass class name
        Intent intent = new Intent("com.wdibstudios.platerate.PlateReviews");

        // Create ReviewData object
        ReviewData plateData = new ReviewData();
        plateData.setPlateNumber(plateNumber);
        plateData.setPlateState(stateAbbreviation);

        // Put data in intent
        intent.putExtra("platedata", plateData);

        // Start Activity
        startActivity(intent);

    } */


    private void FillStateMap()
    {
        stateList = new HashMap<>();

        stateList.put("Alabama", "AL");
        stateList.put("Alaska", "AK");
        stateList.put("Arizona", "AZ");
        stateList.put("Arkansas", "AR");

        stateList.put("California", "CA");
        stateList.put("Colorado", "CO");
        stateList.put("Connecticut", "CT");

        stateList.put("Delaware", "DE");

        stateList.put("Florida", "FL");

        stateList.put("Georgia", "GA");

        stateList.put("Hawaii", "HI");

        stateList.put("Idaho", "ID");
        stateList.put("Illinois", "IL");
        stateList.put("Indiana", "IN");
        stateList.put("Iowa", "IA");

        stateList.put("Kansas", "KS");
        stateList.put("Kentucky", "KY");

        stateList.put("Louisiana", "LA");

        stateList.put("Maine", "ME");
        stateList.put("Maryland", "MD");
        stateList.put("Massachusetts", "MA");
        stateList.put("Michigan", "MI");
        stateList.put("Minnesota", "MN");
        stateList.put("Mississippi", "MS");
        stateList.put("Missouri", "MO");
        stateList.put("Montana", "MT");

        stateList.put("Nebraska", "NE");
        stateList.put("Nevada", "NV");
        stateList.put("New Hampshire", "NH");
        stateList.put("New Jersey", "NJ");
        stateList.put("New Mexico", "NM");
        stateList.put("New York", "NY");
        stateList.put("North Carolina", "NC");
        stateList.put("North Dakota", "ND");

        stateList.put("Ohio", "OH");
        stateList.put("Oklahoma", "OK");
        stateList.put("Oregon", "OR");

        stateList.put("Pennsylvania", "PA");

        stateList.put("Rhode Island", "RI");

        stateList.put("South Carolina", "SC");
        stateList.put("South Dakota", "SD");

        stateList.put("Tennessee", "TN");
        stateList.put("Texas", "TX");

        stateList.put("Utah", "UT");

        stateList.put("Vermont", "VT");
        stateList.put("Virginia", "VA");

        stateList.put("Washington", "WA");
        stateList.put("West Virginia", "WV");
        stateList.put("Wisconsin", "WI");
        stateList.put("Wyoming", "WY");

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //Get String value from selection
        String stateSelected = (String) parent.getItemAtPosition(pos);

        //Set the text of the state view to the found state abbreviation
        stateView.setText(stateList.get(stateSelected));
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    private void DisplayError(String message)
    {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, message, Snackbar.LENGTH_LONG);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    public void NewIntent(String plateString, String stateString)
    {
        // Create intent pass class name
        Intent passIntent = new Intent("com.wdibstudios.platerate.PlateReviews");

        // Create ReviewData object
        ReviewData exportPlateData = new ReviewData();
        exportPlateData.setPlateNumber(plateString);
        exportPlateData.setPlateState(stateString);

        // Put data in intent
        passIntent.putExtra("platedata", exportPlateData);

        // Start Activity
        startActivity(passIntent);

    }

}
