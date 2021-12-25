package com.rewaagasem.university_project_android.firebase;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasmineborno129.university_project_android.model.BMIRecord;


/**
 * Created by eyada on 23/11/2017.
 */

public class MyFirebaseProvider {
    private static DatabaseReference databaseReference;
    private static String currentUserUID;


    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null)
            databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference;
    }

    public static DatabaseReference getChildDatabaseReference(String childName) {
        return getDatabaseReference().child(childName);
    }

    public static void DoAddRecord(BMIRecord record, String user_id, final MessageListener messageListener) {

        DatabaseReference messageNode = getChildDatabaseReference("BMI_Rec").child(user_id).child(record.getID());
        messageNode.setValue(record).addOnSuccessListener(aVoid -> messageListener.onResult(true, record.getID())).addOnFailureListener(e -> messageListener.onResult(false, ""));
    }






}