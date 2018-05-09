package com.wdibstudios.platerate;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlateReviews extends AbstractRuntimePermission
         {

    TextView plateView, stateView, commentView;
    String plateString, stateString, commentString, writtenByString;

    FloatingActionButton goBackButton, addReviewButton;

    RatingBar ratingBar;
    float rating;

    ReviewData importedPlateData, serverData;
    Intent importedIntentData;

    private DocumentReference mDocRef;

    private static final int REQUESTION_PERMISSION = 10;

     boolean updatedAlready;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_reviews);
        updatedAlready = false;

        //Use sever data
        serverData = new ReviewData();



        // Load information of plate and state
        LoadIntent();



        // Setup all elements in activity
        SetReferences();
        SetButtons();
        SetRatingBar();

        // Set texts from intent data
        SetTextViews();

        mDocRef = FirebaseFirestore.getInstance().document("plate/state/" +
                importedPlateData.getPlateState() + "/" + importedPlateData.getPlateNumber());

        GetDataFromServer();


        /*
        //request permissions
        requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                R.string.permission_msg, REQUESTION_PERMISSION);
        */




        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewIntent(importedPlateData.getPlateNumber(), importedPlateData.getPlateState());
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

     @Override
     protected void onResume() {
         super.onResume();
         GetDataFromServer();
     }

             @Override
     public void onPermissionsGranted(int requestCode) {
        //WHen permission is granted
         Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
     }


//region Initialization Methods

    private void LoadIntent() {
        // Get Passed intent
        importedIntentData = getIntent();

        // Get ReviewData object from intent
        importedPlateData = (ReviewData) importedIntentData.getSerializableExtra("platedata");
    }

    public void SetReferences()
    {
        // get reference to ReviewData textviews
        plateView = findViewById(R.id.lookAtPlateNumber);
        stateView = findViewById(R.id.lookAtState);
        commentView = findViewById(R.id.lookAtComment);
    }

    public void SetButtons()
    {
        // reference buttons
        goBackButton = findViewById(R.id.lookAtUTurnButton);
        addReviewButton = findViewById(R.id.AddReviewButton);
    }

    private void SetRatingBar()
    {
        // reference rating
        ratingBar = findViewById(R.id.lookAtRating);
    }


//endregion


    public void GetDataFromServer()
    {
        mDocRef = FirebaseFirestore.getInstance().document("plate/state/" +
                importedPlateData.getPlateState() + "/" + importedPlateData.getPlateNumber());

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("DataCheck","SUCCESSFUL");

                    plateString = documentSnapshot.getString("plateNumber");
                    stateString = documentSnapshot.getString("plateState");
                    commentString = documentSnapshot.getString("reviewComment");
                    Log.e("DEBUG", "COMMENT VALUE: " + commentString);
                    if(documentSnapshot.getLong("plateRating") != null) {
                        rating = documentSnapshot.getLong("plateRating");
                    } else { rating = 3.0f; }

                    serverData.setPlateNumber(plateString);
                    serverData.setPlateState(stateString);
                    serverData.setReviewComment(commentString);
                    //serverData.setPlateRating(documentSnapshot.getLong("rating").floatValue());
                    //serverData.setWrittenBy(documentSnapshot.getString("writtenby"));
                    //returnData.timeSubmitted = documentSnapshot.getString("timesubmitted");
                    Log.e("DEBUG", "END OF SUCCESS LISTENER ");

                } else {
                    commentView.setText("Review Does Not Exist");
                    Log.e("Server Check", "Plate Does Not Exist");
                }

                SetElementsUsingServerData();
                Log.e("DEBUG", "LAST LINE OF SUCESS GET DATA FROM SERVER OVER ");
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                commentView.setText("No Server Connection");
                Log.e("DEBUG", "END OF FAILURE LISTENER ");
                return;
            }});
    }

//endregion

    public void NewIntent(String plateString, String stateString)
    {
        // Create intent pass class name
        Intent passIntent = new Intent("com.wdibstudios.platerate.LeaveReview");

        // Create ReviewData object
        ReviewData exportPlateData = new ReviewData();
        exportPlateData.setPlateNumber(plateString);
        exportPlateData.setPlateState(stateString);

        // Put data in intent
        passIntent.putExtra("platedata", exportPlateData);

        // Start Activity
        startActivity(passIntent);

    }

    private void SetElementsUsingServerData()
    {
        commentView.setText(serverData.getReviewComment());
        ratingBar.setStepSize(0.5f);
        ratingBar.setRating(rating);
    }

    private void SetTextViews()
    {
        plateView.setText(importedPlateData.getPlateNumber());
        stateView.setText(importedPlateData.getPlateState());
    }


//region Helper Methods
    private void DisplayConnectionError(View view)
    {
        Snackbar snackbar = Snackbar
                .make(view, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Try To Resubmit it
                        //DisplayConnectionError(view);
                        TryAgain();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    private void TryAgain()
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
//endregion

}
