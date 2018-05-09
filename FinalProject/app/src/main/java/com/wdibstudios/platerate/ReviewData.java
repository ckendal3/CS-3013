package com.wdibstudios.platerate;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

public class ReviewData implements Serializable{

    public String plateNumber;
    public String plateState;
    public String reviewComment;
    public String writtenBy;

    public float plateRating;

    public ReviewData(String plate, String state, String comment, String leftBy,
                      float rating)
    {
        plateNumber = plate;
        plateState = state;
        reviewComment = comment;
        plateRating = rating;
        writtenBy = leftBy;
        plateRating = rating;
    }

    public ReviewData(){}

    public void SendToDatabase(ReviewData data, String user)
    {
        SendToServer(PackageData(data), user);
    }

    public HashMap PackageData(ReviewData data)
    {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("platenumber", plateNumber);
        dataMap.put("state", plateState);
        dataMap.put("comment", reviewComment);
        dataMap.put("rating", plateRating);
        dataMap.put("writtenby", writtenBy);
        dataMap.put("timesubmitted", FieldValue.serverTimestamp());

        return dataMap;
    }

    public void SendToServer(HashMap serverData, String user)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //THIS IS GOING TO NEED TO BE CHANGED POST TESTING -- TO "plate"
        db.collection("plate").document(serverData.get("state").toString()).collection(serverData.get("plateNumber").toString()).add(serverData);

        //uncomment and fix when keeping track of posts by specific user
        //db.collection("user").document("reviews").collection("review").add("plate/" + serverData.get("state").toString()) + "/" +)
    }

//region SetterNGetters
    String getPlateNumber() { return plateNumber; }
    void setPlateNumber(String newString) { plateNumber = newString; }

    String getPlateState() { return plateState; }
    void setPlateState(String newString) { plateState = newString; }

    String getReviewComment() { return reviewComment; }
    void setReviewComment(String newString) { reviewComment = newString; }

    String getWrittenBy() { return writtenBy; }
    void setWrittenBy(String newString) { writtenBy = newString; }

    float getPlateRating() { return plateRating; }
    void setPlateRating(float newFloat) { plateRating = newFloat; }

//endregion
}
