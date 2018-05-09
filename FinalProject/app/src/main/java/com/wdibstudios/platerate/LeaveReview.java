package com.wdibstudios.platerate;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LeaveReview extends AppCompatActivity {

    Intent importedIntentData;
    ReviewData importedPlateData;

    ReviewData newReviewData;

    TextView plateView, stateView, commentView;
    String plateString, stateString, commentString, writtenByString;

    RatingBar ratingBar;
    float rating;

    FloatingActionButton goBackButton;
    Button saveReviewButton;

    DocumentReference mDocRef;
    Vibrator mVibrate;
    ToneGenerator tone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);

//region Initialization Calls
        // Load data from previous from intent
        LoadIntent();



        // Set up id references
        SetReferences();

        //Set Text in Textviews
        SetTextViews();
        SetButtons();
//endregion

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateReviewData();
                SaveToDatabase();
                finish();
            }
        });
    }


//region Initialization Methods
    private void LoadIntent()
    {
        // Get Passed intent
        importedIntentData = getIntent();

        // Get ReviewData object from intent
        importedPlateData = (ReviewData) importedIntentData.getSerializableExtra("platedata");
    }

    private void SetReferences()
    {
        plateView = findViewById(R.id.leaveReviewPlate);
        stateView = findViewById(R.id.leaveReviewState);
        commentView = findViewById(R.id.leaveReviewComment);
        ratingBar = findViewById(R.id.leaveReviewRating);
        ratingBar.setStepSize(0.5f);
    }

    private void SetTextViews()
    {
        plateView.setText(importedPlateData.getPlateNumber());
        stateView.setText(importedPlateData.getPlateState());
    }

    private void SetButtons()
    {
        goBackButton = findViewById(R.id.leaveReviewUTurn);
        saveReviewButton = findViewById(R.id.SavePlateButton);
    }

//endregion

//region Creating and Setting Data For Review
    private void SetReviewData()
    {
        plateString = plateView.getText().toString();
        stateString = stateView.getText().toString();
        commentString = commentView.getText().toString();
        rating = ratingBar.getRating();
        writtenByString = "TestUser";
    }

    private void CreateReviewData()
    {
        newReviewData = new ReviewData();

        SetReviewData();

        newReviewData.setPlateNumber(plateString);
        newReviewData.setPlateState(stateString);
        newReviewData.setReviewComment(commentString);
        newReviewData.setPlateRating(rating);
        newReviewData.setWrittenBy(writtenByString);
    }
//endregion

//region Database Related Methods
    public void SaveToDatabase()
    {
        mDocRef = FirebaseFirestore.getInstance().document("plate/state/" +
                newReviewData.getPlateState() + "/" + newReviewData.getPlateNumber());

        mDocRef.set(newReviewData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("PlateNumber", "License Plate Review has been saved!");
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                tone.startTone(ToneGenerator.TONE_PROP_ACK,75);
                mVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(mVibrate != null){
                    mVibrate.vibrate(100);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("PlateNumber", "License Plate was not saved!");
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                tone.startTone(ToneGenerator.TONE_SUP_ERROR,75);
                mVibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(mVibrate != null){
                    mVibrate.vibrate(100);
                }
            }
        });
    }

//endregion
}
