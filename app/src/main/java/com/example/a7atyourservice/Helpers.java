package com.example.a7atyourservice;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static CollectionReference getCollectionReferenceForLifting() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("workouts").
                document(firebaseUser.getUid()).collection("my_workouts");
    }
    public static DocumentReference getCheckRef() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Check").
                document(firebaseUser.getUid());
    }

}
